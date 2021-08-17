package views.dialogs;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DialogLoadingController {
    @FXML
    private Label loading;

    @FXML
    private void initialize() {

    }

    public void setLabelText(String text) {
        this.loading.setText(text);
    }
}
