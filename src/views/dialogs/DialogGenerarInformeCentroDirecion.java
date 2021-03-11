package views.dialogs;

import informes_generate.GeneradorLocator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.sql.Date;

import static util.Util.showDialog;

public class DialogGenerarInformeCentroDirecion {

    @FXML
    private DatePicker fechaCierre;

    @FXML
    private ProgressBar progressBar;

    private Stage dialogStage;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    @FXML
    private void initialize() {
        this.progressBar.setVisible(false);
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleGenerate() {

        //GeneradorLocator.getGenerarInformeCentroDireccion().generarInformeCentroDireccion(Date.valueOf(fechaCierre.getValue()));
        progressBar.setVisible(true);
        Task<Boolean> task = new Task<>() {
            boolean result = false;

            @Override
            protected Boolean call() throws Exception {
                try {
                    this.result = GeneradorLocator.getGenerarInformeCentroDireccion().generarInformeCentroDireccion(Date.valueOf(fechaCierre.getValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                progressBar.setVisible(false);
                String file = "src/informesGenerados/ResumenCDNT.xlsx";
                try {
                    Runtime.getRuntime().exec("cmd /c start " + file);
                } catch (Exception e) {
                    System.out.println("Error al abrir el archivo");
                }
                showDialog(result);
            }

        };

        new Thread(task).start();
    }


}
