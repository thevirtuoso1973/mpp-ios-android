import UIKit
import SharedCode

class ViewController: UIViewController {

    @IBOutlet private var label: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        label.text = CommonKt.createApplicationScreenMessage()
    }
}
