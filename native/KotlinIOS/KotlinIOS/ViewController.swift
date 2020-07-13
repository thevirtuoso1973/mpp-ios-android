import UIKit
import SharedCode

class PickerDelegate: NSObject, UIPickerViewDelegate, UIPickerViewDataSource {
    private var pickerOptions: Array<String> = ["a", "b", "c"]
    
    func PickerDelegate() {
    }
    
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
    
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    private let stationPickerDelegate = PickerDelegate()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        firstStationPicker.delegate = stationPickerDelegate
        firstStationPicker.dataSource = stationPickerDelegate
        secondStationPicker.delegate = stationPickerDelegate
        secondStationPicker.dataSource = stationPickerDelegate
    }
    
    @IBAction func submitButtonPressed(_ sender: Any) {
        presenter.onSubmitPressed(view: self)
    }
}

extension ViewController: ApplicationContractView {
    func setStations(stations: KotlinArray) {
        var newStations : Array<String> = []
        for i in 0..<stations.size {
            newStations.append(stations.get(index: i) as! String)
        }
        stationPickerDelegate.setPickerOptions(newOptions: newStations)
    }
    
    func setStations(stations: Array<String>) {
        stationPickerDelegate.setPickerOptions(newOptions: stations)
    }
}
