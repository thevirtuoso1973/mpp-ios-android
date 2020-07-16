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
    @IBOutlet var changesLabel: UILabel!
    
    func updateCell(_ journey: TrainTimes.Journey) {
        priceLabel.text = journey.priceFormatted
        changesLabel.text = "\(journey.changes.size)"
        
        departureLabel.text = journey.departureDateFormatted
        arrivalLabel.text = journey.arrivalDateFormatted
    }

}
