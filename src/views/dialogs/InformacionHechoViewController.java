package views.dialogs;


import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import services.ServiceLocator;
import util.Util;
import views.BuscarHechosViewController;
import views.BuscarViewController;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Dialog to show the information about one fact
 *
 * @author Henry Serra Morejón
 */
public class InformacionHechoViewController {
    boolean incidente;
    boolean imputable;
    @FXML
    private TextField titulo;
    @FXML
    private TextArea lugarOcurrencia;
    @FXML
    private TextField codCDNT;
    @FXML
    private TextField centro;
    @FXML
    private TextField denuncia;
    @FXML
    private TextArea observaciones;
    @FXML
    private TextField cantidad;
    @FXML
    private TextField afectacionMN;
    @FXML
    private TextField afectacionMLC;
    @FXML
    private TitledPane delitoVSPExtPanel;
    @FXML
    private TitledPane delitoVSTPublPanel;
    @FXML
    private TitledPane accidenteTransitoPanel;
    @FXML
    private ComboBox<String> tipoHechoComboBox;
    @FXML
    private ComboBox<String> municipioComboBox;
    @FXML
    private ComboBox<String> unidadOrganizativaComboBox;
    @FXML
    private ComboBox<String> materialComboBox;
    @FXML
    private TextField afectacionService;
    @FXML
    private RadioButton radioButtonIncidente;
    @FXML
    private RadioButton radioButtonImputable;
    @FXML
    private ComboBox<String> vandalismoComboBox;
    @FXML
    private JFXTextField txtFechaOcurrencia;
    @FXML
    private JFXTextField txtFechaResumen;

    private Stage dialogStage;

    private BorderPane mainApp;

    private Hechos hechoSelected;

    public void setMainApp(BorderPane mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Sets the stage of this dialog
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    /**
     * Called when the user clicks cancel
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    @FXML
    private void initialize() {
        municipioComboBox.getItems().setAll(
            ServiceLocator.getMunicipiosService().fetchAll()
            .stream().map(Municipio::getMunicipio).collect(Collectors.toList())
        );
        tipoHechoComboBox.getItems().setAll(
                ServiceLocator.getTipoHechoService().fetchAll()
                .stream().map(TipoHecho::getTipo_hecho).collect(Collectors.toList())
        );
        unidadOrganizativaComboBox.getItems().setAll(
            ServiceLocator.getUnidadOrganizativaService().fetchAll()
            .stream().map(UnidadOrganizativa::getUnidad_organizativa).collect(Collectors.toList())
        );

        accidenteTransitoPanel.setExpanded(false);
        delitoVSPExtPanel.setExpanded(false);
        delitoVSTPublPanel.setExpanded(false);
        accidenteTransitoPanel.setDisable(true);
        delitoVSPExtPanel.setDisable(true);
        delitoVSTPublPanel.setDisable(true);

        //Established group for RadioButtons
        final ToggleGroup group = new ToggleGroup();
        radioButtonImputable.setToggleGroup(group);
        radioButtonIncidente.setToggleGroup(group);

        radioButtonImputable.selectedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    this.imputable = true;
                    this.incidente = false;
                });
        radioButtonIncidente.selectedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    this.imputable = false;
                    this.incidente = true;
                });
        this.cargarInformacionHechoSeleccionadoBusqueda();
    }


    private void cargarInformacionHechoSeleccionadoBusqueda(){
        this.hechoSelected = BuscarHechosViewController.getHechoSeleccionado();
        if(hechoSelected != null){
            this.titulo.setText(hechoSelected.getTitulo());
            this.codCDNT.setText(hechoSelected.getCod_cdnt() == null
                    ? "(sin información)"
                    : hechoSelected.getCod_cdnt());
            String tipoH = hechoSelected.getTipoHecho().getTipo_hecho();
            this.tipoHechoComboBox.getSelectionModel().select(tipoH);
            if(tipoH.equalsIgnoreCase("Delito vs PExt")) {
                this.activePaneDelitoVSPExt();
                String material = hechoSelected.getMateriales().getMateriales();
                this.materialComboBox.getSelectionModel().select(material);
                this.cantidad.setText(hechoSelected.getCantidad()+"");
            }
            else if(tipoH.equalsIgnoreCase("Delito vs TPúb")) {
                this.activePaneDelitoVSTPubl();
                String afect = hechoSelected.getTipoVandalismo().getAfect_tpublica();
                this.vandalismoComboBox.getSelectionModel().select(afect);
            }
            else if(tipoH.equalsIgnoreCase("Acc. Tránsito")) {
                this.activePaneAccTransito();
                this.radioButtonImputable.setSelected(hechoSelected.isImputable());
                this.radioButtonIncidente.setSelected(hechoSelected.isIncidencias());
            }
            else {
                this.disableALLPanes();
            }
            this.txtFechaOcurrencia.setText(hechoSelected.getFecha_ocurrencia().toString());
            this.txtFechaResumen.setText(hechoSelected.getFecha_parte().toString());
            this.denuncia.setText(hechoSelected.getNumero_denuncia() == null
                    ? "(sin información)"
                    : hechoSelected.getNumero_denuncia());
            this.centro.setText(hechoSelected.getCentro());
            this.lugarOcurrencia.setText(hechoSelected.getLugar());
            String municip = hechoSelected.getMunicipio().getMunicipio();
            this.municipioComboBox.getSelectionModel().select(municip);
            String uo = hechoSelected.getUnidadOrganizativa().getUnidad_organizativa();
            this.unidadOrganizativaComboBox.getSelectionModel().select(uo);
            this.afectacionMN.setText(hechoSelected.getAfectacion_mn()+"");
            this.afectacionMLC.setText(hechoSelected.getAfectacion_usd()+"");
            this.afectacionService.setText(hechoSelected.getAfectacion_servicio()+"");
            this.observaciones.setText(hechoSelected.getObservaciones() == null
                    ? "(sin información)"
                    : hechoSelected.getObservaciones());

        }
    }

    private void activePaneDelitoVSPExt() {
        accidenteTransitoPanel.setExpanded(false);
        delitoVSPExtPanel.setExpanded(true);
        delitoVSTPublPanel.setExpanded(false);
        accidenteTransitoPanel.setDisable(true);
        delitoVSPExtPanel.setDisable(false);
        delitoVSTPublPanel.setDisable(true);
        llenarComboMaterial();
    }

    private void activePaneDelitoVSTPubl() {
        accidenteTransitoPanel.setExpanded(false);
        delitoVSPExtPanel.setExpanded(false);
        delitoVSTPublPanel.setExpanded(true);
        accidenteTransitoPanel.setDisable(true);
        delitoVSTPublPanel.setDisable(false);
        delitoVSPExtPanel.setDisable(true);
        llenarComboAfectacion();
    }

    private void activePaneAccTransito() {
        accidenteTransitoPanel.setExpanded(true);
        delitoVSPExtPanel.setExpanded(false);
        delitoVSTPublPanel.setExpanded(false);
        delitoVSPExtPanel.setDisable(true);
        accidenteTransitoPanel.setDisable(false);
        delitoVSTPublPanel.setDisable(true);
    }

    private void disableALLPanes() {
        accidenteTransitoPanel.setExpanded(false);
        delitoVSPExtPanel.setExpanded(false);
        delitoVSTPublPanel.setExpanded(false);
        delitoVSPExtPanel.setDisable(true);
        accidenteTransitoPanel.setDisable(true);
        delitoVSTPublPanel.setDisable(true);
    }

    private void llenarComboMaterial() {
        this.materialComboBox.setPromptText("Seleccione");
        this.materialComboBox.getItems().setAll(
                ServiceLocator.getTipoMaterialesService()
                .fetchAll()
                .stream()
                .map(TipoMateriales::getMateriales)
                .collect(Collectors.toList())
        );
    }

    private void llenarComboAfectacion() {
        this.vandalismoComboBox.setPromptText("Seleccione");
        this.vandalismoComboBox.getItems().setAll(
                ServiceLocator.getTipoVandalismoService()
                        .fetchAll()
                        .stream()
                        .map(TipoVandalismo::getAfect_tpublica)
                        .collect(Collectors.toList())
        );
    }

}
