//
//  JourneyCell.swift
//  KotlinIOS
//
//  Created by Abel Shields on 14/07/2020.
//  Copyright © 2020 Evgeny Petrenko. All rights reserved.
//

import Foundation
import UIKit
import SharedCode

class JourneyCell: UITableViewCell {
    @IBOutlet var priceLabel: UILabel!
    @IBOutlet var departureLabel: UILabel!
    @IBOutlet var arrivalLabel: UILabel!
    @IBOutlet var legsLabel: UILabel!
    
    func updateCell(_ journey: TrainTimes.Journey) {
        priceLabel.text = String(format: "£%i.%02i", journey.price / 100, journey.price % 100)
        legsLabel.text = "\(journey.numberChanges + 1)"
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "HH:mm:ss dd/MM/yy"
        
        let departDate: Date = Date.init(timeIntervalSince1970: TimeInterval(journey.departureTime / 1000))
        let arrivalDate = Date.init(timeIntervalSince1970: TimeInterval(journey.arrivalTime / 1000))
        departureLabel.text = dateFormatter.string(from: departDate)
        arrivalLabel.text = dateFormatter.string(from: arrivalDate)
    }
}
