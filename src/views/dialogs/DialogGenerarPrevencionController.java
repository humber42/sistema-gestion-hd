package views.dialogs;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXRadioButton;
import informes_generate.GeneradorLocator;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import models.Anno;
import services.ServiceLocator;

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
            this.paneSeleccion.setCollapsible(true);
            this.paneSeleccion.setExpanded(false);
            this.paneSeleccion.setCollapsible(false);

        });

        this.radioSemestral.setOnAction(p -> {
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

    @FXML
    private void handleGenerar() {
        GeneradorLocator.getGenerarPrevencion().generarInformePrevencion(null, null);
    }

    @FXML
    private void handleCancel() {
        this.dialogStage.close();
    }
}
