package views.dialogs;

import com.jfoenix.controls.JFXProgressBar;
import informes_generate.GeneradorLocator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import models.Anno;
import models.Hechos;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

import static util.Util.showDialog;

public class DialogGenerarHechosPendientesController {

    private Stage dialogStage;
    @FXML
    private ComboBox<Integer> annos;
    @FXML
    private JFXProgressBar progressBar;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    @FXML
    private void initialize() {
        progressBar.setVisible(false);

    }

    @FXML
    private void handleGenerar() {

//        LinkedList<Hechos> pendientes = ServiceLocator.getHechosService()
//                .obtenerHechosDatosPendientes(annos.getSelectionModel().getSelectedItem());
//        GeneradorLocator.getGenerarHechosPendientes().generarHechosPendientes(pendientes);

        if (annos.getSelectionModel().isEmpty()) {
            Util.dialogResult("Seleccione un año", Alert.AlertType.WARNING);
        } else {
            String path = Util.selectPathToSaveReport(this.dialogStage, 0);
            if (path != null) {
                try {
                    progressBar.setVisible(true);
                    Task<Boolean> task = new Task<Boolean>() {
                        boolean result = false;
                        boolean dataEmpty = false;

                        @Override
                        protected Boolean call() throws Exception {
                            LinkedList<Hechos> pendientes = ServiceLocator.getHechosService()
                                    .obtenerHechosDatosPendientes(annos.getSelectionModel().getSelectedItem());
                            if (Objects.isNull(pendientes)) {
                                dataEmpty = true;
                            } else {
                                GeneradorLocator.getGenerarHechosPendientes().generarHechosPendientes(pendientes, path);
                                result = true;
                            }
                            return result;
                        }

                        @Override
                        protected void succeeded() {
                            super.succeeded();
                            progressBar.setVisible(false);
                            if (dataEmpty) {
                                Util.dialogResult("No existen datos", Alert.AlertType.INFORMATION);
                            } else {
                                String file = path+"/HechosPendientes.xlsx";
                                try {
                                    Runtime.getRuntime().exec("cmd /c start " + file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //showDialog(result);
                            }
                        }
                    };
                    new Thread(task).start();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void asignarAnnos() {
        annos.getItems().setAll(ServiceLocator.getAnnoServicio().allAnno().stream().map(Anno::getAnno).collect(Collectors.toList()));
    }

    @FXML
    private void handleCancel() {
        this.dialogStage.close();
    }
}
