package views.dialogs;

import com.jfoenix.controls.JFXProgressBar;
import informes_generate.GeneradorLocator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import util.Util;

import java.io.IOException;

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
        this.progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
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
//            GeneradorLocator.getGenerarConsolidado()
//                    .generarConsolidado(fechaCierre.getValue(),
//                            path + "/ConsolidadoConciliaciones-" + fechaCierre.getValue() + ".xlsx");
            if (path != null) {
                progressBar.setVisible(true);
                Task<Boolean> task = new Task<Boolean>() {
                    boolean result = false;

                    @Override
                    protected Boolean call() throws Exception {
                        this.result = GeneradorLocator.getGenerateInformeFiscalia().
                                generarInformeCompleto(fechaCierre.getValue(),
                                        path);

                        //progressBar.setProgress(0.5);
                        this.result = GeneradorLocator.getGenerarConsolidado()
                                .generarConsolidado(fechaCierre.getValue(),
                                        path + "/ConsolidadoConciliaciones-" + fechaCierre.getValue() + ".xlsx");
                        //progressBar.setProgress(1);
                        return result;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        progressBar.setVisible(false);
                        String file = path + "/ResumenFGR-" + fechaCierre.getValue() + ".xlsx";
                        String command1 = Util.determineCommandToOpenAFile(file);
                        try {
                            Runtime.getRuntime().exec(command1);
                        } catch (Exception e) {
                            System.out.println("Error al abrir el archivo");
                        }
                        String file2 = path + "/ConsolidadoConciliaciones-" + fechaCierre.getValue() + ".xlsx";
                        String command2 = Util.determineCommandToOpenAFile(file2);
                        try {
                            Runtime.getRuntime().exec(command2);
                        } catch (IOException e) {
                            e.printStackTrace();
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
