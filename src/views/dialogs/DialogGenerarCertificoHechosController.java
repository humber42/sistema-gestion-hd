package views.dialogs;

import com.jfoenix.controls.JFXProgressBar;
import informes_generate.GeneradorLocator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import models.Anno;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DialogGenerarCertificoHechosController {

    @FXML
    ComboBox<String> meses;
    @FXML
    ComboBox<String> anno;
    @FXML
    JFXProgressBar progressBar;

    private Stage dialogStage;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    @FXML
    private void initialize() {
        this.meses.getItems().add("<<Todos>>");
        this.meses.getItems().addAll(Util.meses2);

        this.anno.getItems().addAll(this.llenarComboBoxAnno());

        this.anno.setOnAction(e ->
                handleActive());


    }

    private void handleActive() {
        this.meses.setDisable(false);
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void handleGenerate() {
        if(anno.getSelectionModel().getSelectedItem() == null){
            Util.dialogResult("Seleccione un año.", Alert.AlertType.INFORMATION);
        }
        else if(meses.getSelectionModel().getSelectedItem() == null){
            Util.dialogResult("Seleccione un mes.", Alert.AlertType.INFORMATION);
        }
        else {
            String path = Util.selectPathToSaveDatabase(this.dialogStage);
            if (path != null) {
                try {
                    int anno = this.obtenerAnno();
                    String mes = this.obtenerMes();

                    progressBar.setVisible(true);
                    Task<Boolean> task = new Task<Boolean>() {
                        boolean result = false;

                        @Override
                        protected Boolean call() throws Exception {
                            this.result = GeneradorLocator.getGenerarCertificoHechos().generarCertificoHechos(
                                    anno, mes, path
                            );
                            return result;
                        }

                        @Override
                        protected void succeeded() {
                            super.succeeded();
                            progressBar.setVisible(false);
                            String file = path + "/CertificoHechos.xlsx";
                            String commando = Util.determineCommandToOpenAFile(file);
                            try {
                                Runtime.getRuntime().exec(commando);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //showDialog(result);
                        }
                    };
                    new Thread(task).start();
                } catch (NullPointerException e) {
                    Util.dialogResult("Seleccione un año", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        }
    }

    private int obtenerAnno() {
        if (this.anno.getValue().isEmpty()) {
            throw new NullPointerException();
        } else
            return Integer.valueOf(this.anno.getValue());
    }

    private String obtenerMes() {
        String devolver = null;
        try {
            devolver = this.meses.getValue();
        } catch (NullPointerException e) {
            devolver = "<<Todos>>";
        }
        return devolver;
    }

    private List<String> llenarComboBoxAnno() {
        return ServiceLocator.getAnnoServicio()
                .allAnno().stream()
                .map(Anno::getAnno)
                .map(integer -> integer.toString())
                .collect(Collectors.toList());
    }


}
