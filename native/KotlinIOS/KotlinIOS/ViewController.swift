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
    
}

class ViewController: UIViewController {
    @IBOutlet private var firstStationPicker: UIPickerView!
    @IBOutlet private var secondStationPicker: UIPickerView!
    @IBOutlet private var submitButton: UIButton!
    @IBOutlet private var resultTable: UITableView!
    
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
            newStations.append((stations.get(index: i) as! Station).fullName)
        }
        stationPickerDelegate.setPickerOptions(newOptions: newStations)
    }
    
    func getCurrentUnixTime() -> Int64 {
        return Int64(Date().timeIntervalSince1970 * 1000)
    }
}

extension ViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let traintimes = traintimes {
            return Int(traintimes.journeys.size)
        }
        return 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: customCellIdentifier) as! JourneyCell
        cell.updateCell(traintimes!.journeys.get(index: Int32(indexPath.row)) as! TrainTimes.Journey)
        return cell
    }
}
