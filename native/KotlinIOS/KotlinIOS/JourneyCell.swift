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
        priceLabel.text = String(format: "£%i.%02i", journey.price / 100, journey.price % 100)
        changesLabel.text = "\(journey.numberChanges)"
        
        
        
        let departDate: Date = Date.init(timeIntervalSince1970: TimeInterval(journey.departureTime / 1000))
        let arrivalDate = Date.init(timeIntervalSince1970: TimeInterval(journey.arrivalTime / 1000))
        departureLabel.text = toHumanReadableDate(departDate)
        arrivalLabel.text = toHumanReadableDate(arrivalDate)
    }
    
    func toHumanReadableDate(_ date: Date) -> String {
        // If today: HH:mm (in xx min/hr)
        let hourFormatter = DateFormatter()
        hourFormatter.dateFormat = "HH:mm"
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "HH:mm:ss dd/MM/yy"
        
        let now = Date()
        if (Calendar.current.isDate(date, inSameDayAs: now)) {
            let diff = Calendar.current.dateComponents([.hour, .minute], from: now, to: date)
            let diffStr = (diff.hour! == 0) ? ("\(diff.minute!) min.") : String(format: "%i:%02i", diff.hour!, diff.minute!)
            return "\(hourFormatter.string(from: date)) (in \(diffStr))"
        }
        return ""
    }
}
