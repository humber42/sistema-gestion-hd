package views.dialogDelictivos;

import com.jfoenix.controls.JFXProgressBar;
import informes_generate.GeneradorLocator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

public class DialogGenerarListadosDelictivos {

    @FXML
    private ComboBox<String> meses;
    @FXML
    private ComboBox<String> anno;
    @FXML
    private JFXProgressBar progressBar;

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
        if(anno.getSelectionModel().getSelectedItem() == null){
            Util.dialogResult("Seleccione un año.", Alert.AlertType.INFORMATION);
        }
        else if(meses.getSelectionModel().getSelectedItem() == null){
            Util.dialogResult("Seleccione un mes.", Alert.AlertType.INFORMATION);
        }
        else {
            String path = Util.selectPathToSaveDatabase(this.dialogStage) + this.direccionFile;
            if (!path.contains("null")) {
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
                            LinkedList<Hechos> hechos = new LinkedList<>();
                            if (tipoHecho == 11 || tipoHecho == 12) {
                                hechos.addAll(ServiceLocator.getHechosService().obtenerHechosByTypeAndDate(anno, mes, 11));
                                hechos.addAll(ServiceLocator.getHechosService().obtenerHechosByTypeAndDate(anno, mes, 12));
                            } else {
                                hechos.addAll(ServiceLocator.getHechosService().obtenerHechosByTypeAndDate(anno, mes, tipoHecho));
                            }


                            if (hechos.isEmpty()) {
                                datosVacios = true;
                            } else {
                                if (tipoHecho < 8)
                                    result = GeneradorLocator.getGenerarListados().generarListado(hechos, path);
                                else
                                    result = GeneradorLocator.getGenerarListados().generarListado(hechos, path, tipoHecho);
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
                                String file = path;
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

    private List<String> llenarComboBoxAnno() throws SQLException {
        return ServiceLocator.getAnnoServicio()
                .allAnno().stream()
                .map(Anno::getAnno)
                .map(integer -> integer.toString())
                .collect(Collectors.toList());
    }
}
