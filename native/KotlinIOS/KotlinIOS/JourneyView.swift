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
    @IBOutlet private var priceLabel: UILabel!
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
        fromLabel.text = "From: \(from)"
        toLabel.text = "To: \(to)"
        priceLabel.text = String(format: "Price: £%i.%02i", journey.price / 100, journey.price % 100)
        
        durationLabel.text = "Duration: \(journey.diffTimeFormatted)"
        statusLabel.text = "Status: \(journey.status)"
        operatorLabel.text = "Primary operator: \(journey.trainOperator)"
        if (journey.changes.size > 0) {
            var changeText = "Changes: \n"
            for i in 0..<journey.changes.size {
                let station = journey.changes.get(index: Int32(i)) as! ApiResult.JourneyStation
                changeText += "  " + station.name + "\n"
            }
            changesLabel.text = changeText
        } else {
            changesLabel.text = "No changes."
        }
    }
    
    @IBAction func doneButtonPressed(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
