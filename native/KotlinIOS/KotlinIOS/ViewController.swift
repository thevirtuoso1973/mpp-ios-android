import UIKit
import SharedCode

class PickerDelegate: NSObject, UIPickerViewDelegate, UIPickerViewDataSource {
    private var pickerOptions: Array<String> = ["a", "b", "c"]
    func PickerDelegate() {}
    
    func pickerView(_: UIPickerView, titleForRow: Int, forComponent: Int) -> String? {
        return pickerOptions[titleForRow]
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerOptions.count
    }
    
    func setPickerOptions(newOptions: Array<String>) {
        pickerOptions = newOptions
    }
    
    func getPickerOption(at: Int) -> String {
        return pickerOptions[at]
    }
    
}

class ViewController: UIViewController {
    @IBOutlet private var firstStationPicker: UIPickerView!
    @IBOutlet private var secondStationPicker: UIPickerView!
    @IBOutlet private var submitButton: UIButton!
    @IBOutlet private var resultTable: UITableView!
    @IBOutlet private var loadingView: UIActivityIndicatorView!
    
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    private let stationPickerDelegate = PickerDelegate()
    
    private var traintimes: TrainTimes? = nil
    private let customCellIdentifier = "JOURNEY_CELL"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        firstStationPicker.delegate = stationPickerDelegate
        firstStationPicker.dataSource = stationPickerDelegate
        secondStationPicker.delegate = stationPickerDelegate
        secondStationPicker.dataSource = stationPickerDelegate
        
        resultTable.tableFooterView = UIView(frame: .zero)
        let nib = UINib(nibName: "JourneyCell", bundle: nil)
        resultTable.register(nib, forCellReuseIdentifier: customCellIdentifier)
        view.bringSubviewToFront(loadingView)
    }
    
    @IBAction func submitButtonPressed(_ sender: Any) {
        presenter.onSubmitPressed(result: AppSubmitResult(stationFromIndex: getStationFrom(), stationToIndex: getStationTo()))
    }
}

extension ViewController: ApplicationContractView {
    func displayTrainTimes(trainTimes: TrainTimes) {
        traintimes = trainTimes
        resultTable.reloadData()
    }
    
    func getStationFrom() -> Int32 {
        return Int32(firstStationPicker.selectedRow(inComponent: 0))
    }
    
    func getStationTo() -> Int32 {
        return Int32(secondStationPicker.selectedRow(inComponent: 0))
    }
    
    func openLink(link: String) {
        let url = URL(string: link)!
        UIApplication.shared.open(url)
    }
    
    func createAlert(msg: String) {
        let alert = UIAlertController(title: nil, message: msg, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: "Default action"), style: .default, handler: { _ in
            alert.dismiss(animated: true)
        }))
        self.present(alert, animated: true)
    }
    
    func setStations(stations: KotlinArray) {
        var newStations : Array<String> = []
        for i in 0..<stations.size {
            newStations.append((stations.get(index: i) as! StationApiResult.Station).name)
        }
        stationPickerDelegate.setPickerOptions(newOptions: newStations)
        firstStationPicker.reloadAllComponents()
        secondStationPicker.reloadAllComponents()
    }
    
    func getCurrentUnixTime() -> Int64 {
        return Int64(Date().timeIntervalSince1970 * 1000)
    }
    
    func setLoading(loading: Bool) {
        if loading {
            loadingView.startAnimating()
        } else {
            loadingView.stopAnimating()
        }
    }
}

extension ViewController: UITableViewDataSource, UITableViewDelegate {
    func getJourney(_ n: Int) -> TrainTimes.Journey {
        return traintimes!.journeys.get(index: Int32(n - 1)) as! TrainTimes.Journey
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: false)
        if (indexPath.row > 0) {
            let journey = getJourney(indexPath.row)
            let stationFrom = traintimes!.origin
            let stationTo = traintimes!.destination
            JourneyView.showPopup(parent: self,
                                  initialiser: {vc in vc.updateJourney(from: stationFrom, to: stationTo, journey: journey)})
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let traintimes = traintimes {
            return Int(traintimes.journeys.size + 1)
        }
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: customCellIdentifier) as! JourneyCell
        if indexPath.row > 0 {
            cell.updateCell(getJourney(indexPath.row))
        }
        return cell
    }
}
