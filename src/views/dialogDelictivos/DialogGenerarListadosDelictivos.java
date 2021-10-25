package views.dialogDelictivos;

import informes_generate.GeneradorLocator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import models.Anno;
import models.Hechos;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static util.Util.showDialog;

public class DialogGenerarListadosDelictivos {

    @FXML
    ComboBox<String> meses;
    @FXML
    ComboBox<String> anno;
    @FXML
    ProgressBar progressBar;

    @FXML
    private Label labelTitulo;
    private String direccionFile;
    private int tipoHecho;
    private Stage dialogStage;

    public void getLabelTitulo(String text) {
        this.labelTitulo.setText(text);
    }

    public void setTipoHecho(int tipo) {
        this.tipoHecho = tipo;
    }

    public void setDireccionFile(String address) {
        this.direccionFile = address;
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    @FXML
    private void initialize() {
        this.meses.getItems().add("<<Todos>>");
        this.meses.getItems().addAll(Util.meses2);


        try {
            this.anno.getItems().addAll(this.llenarComboBoxAnno());
        } catch (SQLException e) {
            e.printStackTrace();

        }


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
            int mes = Util.obtenerNumeroMes(this.obtenerMes());
            //LinkedList<Hechos> hechos = ServiceLocator.getHechosService().obtenerHechosByTypeAndDate(anno,mes,tipoHecho);
            //GeneradorLocator.getGenerarListados().generarListado(hechos,direccionFile);
            progressBar.setVisible(true);
            Task<Boolean> task = new Task<Boolean>() {
                boolean result = false;
                boolean datosVacios = false;

                @Override
                protected Boolean call() throws Exception {

                    LinkedList<Hechos> hechos = ServiceLocator.getHechosService().obtenerHechosByTypeAndDate(anno, mes, tipoHecho);
                    if (hechos.isEmpty()) {
                        datosVacios = true;
                    } else {
                        if (tipoHecho < 8)
                            result = GeneradorLocator.getGenerarListados().generarListado(hechos, direccionFile);
                        else
                            result = GeneradorLocator.getGenerarListados().generarListado(hechos, direccionFile, tipoHecho);
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
                        String file = direccionFile;
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

    private List<String> llenarComboBoxAnno() throws SQLException {
        return ServiceLocator.getAnnoServicio()
                .allAnno().stream()
                .map(Anno::getAnno)
                .map(integer -> integer.toString())
                .collect(Collectors.toList());
    }
}
