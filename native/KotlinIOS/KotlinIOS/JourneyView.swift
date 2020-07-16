//
//  JourneyView.swift
//  KotlinIOS
//
//  Created by Abel Shields on 16/07/2020.
//  Copyright © 2020 Evgeny Petrenko. All rights reserved.
//

import Foundation
import UIKit
import SharedCode

class JourneyView: UIViewController {
    @IBOutlet private var fromLabel: UILabel!
    @IBOutlet private var toLabel: UILabel!
    @IBOutlet private var durationLabel: UILabel!
    @IBOutlet private var statusLabel: UILabel!
    @IBOutlet private var operatorLabel: UILabel!
    @IBOutlet private var changesLabel: UILabel!
    @IBOutlet private var doneButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    static func showPopup(parent: UIViewController, initialiser: (JourneyView) -> ()) {
        if let popupViewController = UIStoryboard(name: "JourneyView", bundle: nil).instantiateViewController(withIdentifier: "JourneyViewController") as? JourneyView {
            popupViewController.loadView() // Create labels etc. for initialiser
            initialiser(popupViewController)
            parent.present(popupViewController, animated: true)
        } else {
            print("FAILED to instantiate")
        }
    }
    
    func updateJourney(from: String, to: String, journey: TrainTimes.Journey) {
        fromLabel.text = "FROM: \(from)"
        toLabel.text = "TO: \(to)"
        let departDate: Date = Date.init(timeIntervalSince1970: TimeInterval(journey.departureTime / 1000))
        let arrivalDate = Date.init(timeIntervalSince1970: TimeInterval(journey.arrivalTime / 1000))
        let duration = Calendar.current.dateComponents([.hour, .minute], from: departDate, to: arrivalDate)
        durationLabel.text = "Duration: \(String(format: "%i:%02i", duration.hour!, duration.minute!))"
        statusLabel.text = "Status: \(journey.status)"
        operatorLabel.text = "Primary operator: \(journey.trainOperator)"
        if (journey.changes.size > 0) {
            changesLabel.text = "Changes: \n"
            for i in 0..<journey.changes.size {
                let station = journey.changes.get(index: Int32(i)) as! ApiResult.JourneyStation
                changesLabel.text! += station.name + "test\n"
            }
        } else {
            changesLabel.text = "No changes."
        }
    }
    
    @IBAction func doneButtonPressed(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
