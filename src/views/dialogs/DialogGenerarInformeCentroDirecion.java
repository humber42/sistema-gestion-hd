package views.dialogs;

import com.jfoenix.controls.JFXProgressBar;
import informes_generate.GeneradorLocator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import util.Util;

import java.sql.Date;

public class DialogGenerarInformeCentroDirecion {

    @FXML
    private DatePicker fechaCierre;

    @FXML
    private JFXProgressBar progressBar;

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
        if (fechaCierre.getValue() == null) {
            Util.dialogResult("Seleccione una fecha de cierre.", Alert.AlertType.WARNING);
        } else {
            String path = Util.selectPathToSaveDatabase(this.dialogStage);
            if (path != null) {
                //GeneradorLocator.getGenerarInformeCentroDireccion().generarInformeCentroDireccion(Date.valueOf(fechaCierre.getValue()));
                progressBar.setVisible(true);
                Task<Boolean> task = new Task<Boolean>() {
                    boolean result = false;

                    @Override
                    protected Boolean call() throws Exception {
                        try {
                            this.result = GeneradorLocator.getGenerarInformeCentroDireccion().
                                    generarInformeCentroDireccion(Date.valueOf(fechaCierre.getValue()), path);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return result;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        progressBar.setVisible(false);
                        String file = path + "/ResumenCDNT.xlsx";
                        String command = Util.determineCommandToOpenAFile(file);
                        try {
                            Runtime.getRuntime().exec(command);
                        } catch (Exception e) {
                            System.out.println("Error al abrir el archivo");
                        }
                        //showDialog(result);
                    }

                };

                new Thread(task).start();
            }
        }
    }


}
