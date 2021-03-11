package views.dialogs;


import informes_generate.GeneradorLocator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import models.Anno;
import org.controlsfx.dialog.ExceptionDialog;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;
import java.util.stream.Collectors;

public class DialogGenerarHechosPrevenidosController {

    @FXML
    private ComboBox<Integer> annos;
    @FXML
    private ProgressBar progressBar;

    private Stage dialogStage;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void initialize() {
        annos.getItems().addAll(
                ServiceLocator.getAnnoServicio().allAnno().stream().map(Anno::getAnno).collect(Collectors.toList())
        );
        progressBar.setVisible(false);
    }

    @FXML
    private void handleGenerar() {

        // GeneradorLocator.getGenerarHechosPrevenidos().generarHechosPrevenidos(annos.getSelectionModel().getSelectedItem());
        if (annos.getSelectionModel().isEmpty()) {
            Util.dialogResult("Seleccione un año", Alert.AlertType.WARNING);
        } else {
            progressBar.setVisible(true);
            Task<Boolean> task = new Task<Boolean>() {
                boolean result = false;

                @Override
                protected Boolean call() throws Exception {

                    this.result = GeneradorLocator.getGenerarHechosPrevenidos().generarHechosPrevenidos(annos.getSelectionModel().getSelectedItem());
                    return result;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    progressBar.setVisible(false);
                    String file = "src/informesGenerados/HechosPrevenidos.xlsx";
                    if (result) {
                        try {
                            Runtime.getRuntime().exec("cmd /c start " + file);
                        } catch (IOException e) {
                            ExceptionDialog dialog = new ExceptionDialog(e);
                            dialog.showAndWait();
                        }
                        Util.dialogResult("Exito", Alert.AlertType.INFORMATION);
                    } else {
                        Util.dialogResult("Error al generar", Alert.AlertType.ERROR);
                    }
                }
            };
            new Thread(task).start();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
