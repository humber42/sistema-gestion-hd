package views.dialogs;

import com.jfoenix.controls.JFXProgressBar;
import informes_generate.GeneradorLocator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import util.Util;

public class DialogGenerarInformeFiscaliaController {

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
                progressBar.setVisible(true);
//                GeneradorLocator.getGenerateInformeFiscalia().
//                        generarInformeCompleto(fechaCierre.getValue(), path);
                Task<Boolean> task = new Task<Boolean>() {
                    boolean result = false;

                    @Override
                    protected Boolean call() throws Exception {
                        this.result = GeneradorLocator.getGenerateInformeFiscalia().
                                generarInformeCompleto(fechaCierre.getValue(), path);
                        return result;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        progressBar.setVisible(false);
                        String file = path + "/ResumenFGR.xlsx";
                        try {
                            Runtime.getRuntime().exec("cmd /c start " + file);
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

    private void showDialog(boolean result) {
        if (result) {
            Util.dialogResult("Exito", Alert.AlertType.INFORMATION);
        } else {
            Util.dialogResult("Fallo", Alert.AlertType.ERROR);
        }
    }


}
