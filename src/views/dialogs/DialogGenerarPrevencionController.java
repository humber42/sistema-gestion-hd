package views.dialogs;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXRadioButton;
import informes_generate.GeneradorLocator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import models.Anno;
import org.controlsfx.dialog.ExceptionDialog;
import services.ServiceLocator;
import util.Util;

import java.sql.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public class DialogGenerarPrevencionController {

    private Stage dialogStage;

    @FXML
    private JFXComboBox<Integer> annos;

    @FXML
    private JFXRadioButton radioTrimestral;
    @FXML
    private JFXRadioButton radioSemestral;
    @FXML
    private JFXRadioButton radioAnual;

    @FXML
    private JFXRadioButton primer;

    @FXML
    private JFXRadioButton segundo;

    @FXML
    private JFXRadioButton tercer;

    @FXML
    private JFXRadioButton cuarto;

    @FXML
    private TitledPane paneSeleccion;

    @FXML
    private JFXProgressBar progressBar;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        progressBar.setVisible(false);

        this.annos.getItems().setAll(
                ServiceLocator.getAnnoServicio().allAnno().stream().map(Anno::getAnno).collect(Collectors.toList())
        );

        ToggleGroup toggleTipoFecha = new ToggleGroup();
        this.radioAnual.setToggleGroup(toggleTipoFecha);
        this.radioSemestral.setToggleGroup(toggleTipoFecha);
        this.radioTrimestral.setToggleGroup(toggleTipoFecha);

        ToggleGroup toggleTiempo = new ToggleGroup();
        this.primer.setToggleGroup(toggleTiempo);
        this.segundo.setToggleGroup(toggleTiempo);
        this.tercer.setToggleGroup(toggleTiempo);
        this.cuarto.setToggleGroup(toggleTiempo);

        this.radioAnual.setOnAction(p -> {
            this.unSelectItems();
            this.paneSeleccion.setDisable(true);
            this.paneSeleccion.setCollapsible(true);
            this.paneSeleccion.setExpanded(false);
            this.paneSeleccion.setCollapsible(false);

        });

        this.radioSemestral.setOnAction(p -> {
            this.paneSeleccion.setDisable(false);
            this.paneSeleccion.setCollapsible(true);
            this.paneSeleccion.setExpanded(true);
            this.paneSeleccion.setCollapsible(false);
            this.primer.setVisible(true);
            this.primer.setText("1er Semestre");
            this.segundo.setVisible(true);
            this.segundo.setText("2do Semestre");
            this.tercer.setVisible(false);
            this.cuarto.setVisible(false);
            this.unSelectItems();



        });

        this.radioTrimestral.setOnAction(p -> {
            this.paneSeleccion.setDisable(false);
            this.paneSeleccion.setCollapsible(true);
            this.paneSeleccion.setExpanded(true);
            this.paneSeleccion.setCollapsible(false);
            this.primer.setVisible(true);
            this.primer.setText("1er Trimestre");
            this.segundo.setVisible(true);
            this.segundo.setText("2do Trimestre");
            this.tercer.setVisible(true);
            this.tercer.setText("3er Trimestre");
            this.cuarto.setVisible(true);
            this.cuarto.setText("4to Trimestre");
            this.unSelectItems();
        });
    }

    private void unSelectItems() {
        this.primer.setSelected(false);
        this.segundo.setSelected(false);
        this.tercer.setSelected(false);
        this.cuarto.setSelected(false);
    }

    private LinkedList<Date> devolverFechas() {
        LinkedList<Date> fechas = null;
        if (Objects.isNull(this.annos.getSelectionModel().getSelectedItem())) {
            Util.dialogResult("Debe seleccionar un año", Alert.AlertType.ERROR);
        } else {
            fechas = new LinkedList<>();
            int anno = this.annos.getSelectionModel().getSelectedItem();
            //Determinando si es trimestral
            if (this.radioTrimestral.isSelected()) {
                //Determinando en que trimestre
                if (this.primer.isSelected()) {
                    String fechaInicial = anno + "-01-01";
                    String fechaFinal = anno + "-04-01";
                    fechas.add(Date.valueOf(fechaInicial));
                    fechas.add(Date.valueOf(fechaFinal));
                } else if (this.segundo.isSelected()) {
                    String fechaInicial = anno + "-04-01";
                    String fechaFinal = anno + "-07-01";
                    fechas.add(Date.valueOf(fechaInicial));
                    fechas.add(Date.valueOf(fechaFinal));
                } else if (this.tercer.isSelected()) {
                    String fechaInicial = anno + "-07-01";
                    String fechaFinal = anno + "-10-01";
                    fechas.add(Date.valueOf(fechaInicial));
                    fechas.add(Date.valueOf(fechaFinal));
                } else if (this.cuarto.isSelected()) {
                    String fechaInicial = anno + "-10-01";
                    String fechaFinal = (1 + anno) + "-01-01";
                    fechas.add(Date.valueOf(fechaInicial));
                    fechas.add(Date.valueOf(fechaFinal));
                } else {
                    Util.dialogResult("Debe seleccionar algun trimestre", Alert.AlertType.ERROR);
                    fechas = null;
                }
            }
            //Determinando si es semestral
            else if (this.radioSemestral.isSelected()) {
                //Determinando en que semestre
                if (this.primer.isSelected()) {
                    String fechaInicial = anno + "-01-01";
                    String fechaFinal = anno + "-07-01";
                    fechas.add(Date.valueOf(fechaInicial));
                    fechas.add(Date.valueOf(fechaFinal));
                } else if (this.segundo.isSelected()) {
                    String fechaInicial = anno + "-07-01";
                    String fechaFinal = (1 + anno) + "-01-01";
                    fechas.add(Date.valueOf(fechaInicial));
                    fechas.add(Date.valueOf(fechaFinal));
                } else {
                    Util.dialogResult("Debe seleccionar algún semestre", Alert.AlertType.ERROR);
                    fechas = null;
                }
            }
            //Determinando si es anual
            else if (this.radioAnual.isSelected()) {
                String fechaInicial = anno + "-01-01";
                String fechaFinal = (anno) + "-12-31";
                fechas.add(Date.valueOf(fechaInicial));
                fechas.add(Date.valueOf(fechaFinal));
            }
        }

        return fechas;
    }

    @FXML
    private void handleGenerar() {
        String path = Util.selectPathToSaveReport(this.dialogStage, 0);
        if(path != null) {
            LinkedList<Date> fechas = devolverFechas();
//        GeneradorLocator.getGenerarPrevencion()
//                    .generarInformePrevencion(fechas.getFirst(),fechas.getLast());
            if (Objects.nonNull(fechas)) {
                progressBar.setVisible(true);
                Task<Boolean> task = new Task<Boolean>() {
                    boolean result = false;

                    @Override
                    protected Boolean call() throws Exception {
                        this.result = GeneradorLocator.getGenerarPrevencion()
                                .generarInformePrevencion(fechas.getFirst(), fechas.getLast(), path);

                        return result;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        progressBar.setVisible(false);
                        String file = path+"/InformePrevencion.xlsx";
                        try {
                            Runtime.getRuntime().exec("cmd /c start " + file);
                        } catch (Exception e) {
                            ExceptionDialog x = new ExceptionDialog(e);
                            x.showAndWait();
                        }
                        //Util.showDialog(result);
                    }
                };

                new Thread(task).start();
            }
        }
    }

    @FXML
    private void handleCancel() {
        this.dialogStage.close();
    }
}
