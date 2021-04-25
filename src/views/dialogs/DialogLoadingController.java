package views.dialogs;

import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;

public class DialogLoadingController {

    @FXML
    private JFXSpinner spinner;


    @FXML
    private void initialize() {
        spinner.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
    }
}
