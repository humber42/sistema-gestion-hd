package views.dialogs;

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
import java.util.List;
import java.util.stream.Collectors;

import static util.Util.showDialog;

public class DialogGenerarResumenMINCOM {

    @FXML
    ComboBox<String> meses;
    @FXML
    ComboBox<String> anno;
    @FXML
    ProgressBar progressBar;

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

        try {
            int anno = this.obtenerAnno();
            int mes = obtenerNumeroMes(this.obtenerMes());
            //LinkedList<Hechos> hechos = ServiceLocator.getHechosService().obtenerHechosResumenMincom(anno, mes);
            //GeneradorLocator.getGenerarResumenMINCOM().generarResumenMINCOM(hechos);
            progressBar.setVisible(true);
            Task<Boolean> task = new Task<Boolean>() {
                boolean result = false;
                boolean datosVacios = false;

                @Override
                protected Boolean call() throws Exception {

                    LinkedList<Hechos> hechos = ServiceLocator.getHechosService().obtenerHechosResumenMincom(anno, mes);
                    if (hechos.isEmpty()) {
                        datosVacios = true;
                    } else {
                        result = GeneradorLocator.getGenerarResumenMINCOM().generarResumenMINCOM(hechos);
                    }
                    return result;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    progressBar.setVisible(false);
                    if (datosVacios && !result) {
                        Util.dialogResult("No hay datos que mostrar", Alert.AlertType.INFORMATION);
                    } else if (!result) {
                        Util.showDialog(result);
                    } else {
                        String file = "src/informesGenerados/ResumenMINCOM.xlsx";
                        try {
                            Runtime.getRuntime().exec("cmd /c start " + file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        showDialog(result);
                    }
                }
            };
            new Thread(task).start();
        } catch (NullPointerException e) {
            Util.dialogResult("Seleccione un año", Alert.AlertType.ERROR);
            e.printStackTrace();
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

    private int obtenerNumeroMes(String name) {
        int mes = 0;
        boolean notFound = true;
        for (int i = 0; i < Util.meses2.length && notFound; i++) {
            if (Util.meses2[i].equalsIgnoreCase(name)) {
                notFound = false;
                mes = i + 1;
            }
        }
        return mes;
    }


}
