package views.dialogs;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DialogLoadingController2 {
    @FXML
    private Label loading;

    private Process process;

    public void setProcess(Process process) {
        this.process = process;
    }

    @FXML
    private void cancelProcess() {
        this.process.destroy();
    }

    @FXML
    private void initialize() {

    }

    public void setLabelText(String text) {
        this.loading.setText(text);
    }
}
