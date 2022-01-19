package views.dialogs;


import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.Hechos;
import views.BuscarHechosViewController;
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
    private TitledPane causaAveriaPextPanel;
    @FXML
    private TextField causaTxt;
    @FXML
    private TextField tipoHechoComboBox;
    @FXML
    private TextField municipioComboBox;
    @FXML
    private TextField unidadOrganizativaComboBox;
    @FXML
    private TextField materialComboBox;
    @FXML
    private TextField afectacionService;
    @FXML
    private RadioButton radioButtonIncidente;
    @FXML
    private RadioButton radioButtonImputable;
    @FXML
    private TextField vandalismoComboBox;
    @FXML
    private JFXTextField txtFechaOcurrencia;
    @FXML
    private JFXTextField txtFechaResumen;
    @FXML
    private CheckBox checkPrevenido;

    private Stage dialogStage;

    private BorderPane mainApp;

    private boolean preven;
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
        causaAveriaPextPanel.setExpanded(false);
        accidenteTransitoPanel.setExpanded(false);
        delitoVSPExtPanel.setExpanded(false);
        delitoVSTPublPanel.setExpanded(false);
        causaAveriaPextPanel.setDisable(false);
        accidenteTransitoPanel.setDisable(true);
        delitoVSPExtPanel.setDisable(true);
        delitoVSTPublPanel.setDisable(true);

        this.checkPrevenido.setOnMouseClicked(event -> {
            this.checkPrevenido.setSelected(preven);
        });
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
            this.tipoHechoComboBox.setText(tipoH);
            if(tipoH.equalsIgnoreCase("Delito vs PExt")) {
                this.activePaneDelitoVSPExt();
                String material = hechoSelected.getMateriales().getMateriales();
                this.materialComboBox.setText(material);
                this.cantidad.setText(hechoSelected.getCantidad()+"");
            }
            else if(tipoH.equalsIgnoreCase("Delito vs TPúb")) {
                this.activePaneDelitoVSTPubl();
                String afect = hechoSelected.getTipoVandalismo().getAfect_tpublica();
                this.vandalismoComboBox.setText(afect);
            }
            else if(tipoH.equalsIgnoreCase("Acc. Tránsito")) {
                this.activePaneAccTransito();
                this.radioButtonImputable.setSelected(hechoSelected.isImputable());
                this.radioButtonIncidente.setSelected(hechoSelected.isIncidencias());
            }
            else if(tipoH.equalsIgnoreCase("Avería PExt")){
                this.activePaneAveriaPext();
                this.causaTxt.setText(hechoSelected.getAveriasPext().getCausa());
            }
            else if(tipoH.equalsIgnoreCase("Robo")) {
                this.checkPrevenido.setVisible(true);
                this.causaAveriaPextPanel.setDisable(true);
                this.preven=hechoSelected.isPrevenido();
                this.checkPrevenido.setSelected(hechoSelected.isPrevenido());
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
            this.municipioComboBox.setText(municip);
            String uo = hechoSelected.getUnidadOrganizativa().getUnidad_organizativa();
            this.unidadOrganizativaComboBox.setText(uo);
            this.afectacionMN.setText(hechoSelected.getAfectacion_mn()+"");
            this.afectacionMLC.setText(hechoSelected.getAfectacion_usd()+"");
            this.afectacionService.setText(hechoSelected.getAfectacion_servicio()+"");
            this.observaciones.setText(hechoSelected.getObservaciones() == null
                    ? "(sin información)"
                    : hechoSelected.getObservaciones());

        }
    }

    private void activePaneDelitoVSPExt() {
        causaAveriaPextPanel.setExpanded(false);
        accidenteTransitoPanel.setExpanded(false);
        delitoVSPExtPanel.setExpanded(true);
        delitoVSTPublPanel.setExpanded(false);
        causaAveriaPextPanel.setDisable(true);
        accidenteTransitoPanel.setDisable(true);
        delitoVSPExtPanel.setDisable(false);
        delitoVSTPublPanel.setDisable(true);
        hideCheckPrevenido();
    }

    private void activePaneDelitoVSTPubl() {
        causaAveriaPextPanel.setExpanded(false);
        accidenteTransitoPanel.setExpanded(false);
        delitoVSPExtPanel.setExpanded(false);
        delitoVSTPublPanel.setExpanded(true);
        causaAveriaPextPanel.setDisable(true);
        accidenteTransitoPanel.setDisable(true);
        delitoVSTPublPanel.setDisable(false);
        delitoVSPExtPanel.setDisable(true);
        hideCheckPrevenido();
    }

    private void activePaneAccTransito() {
        causaAveriaPextPanel.setExpanded(false);
        accidenteTransitoPanel.setExpanded(true);
        delitoVSPExtPanel.setExpanded(false);
        delitoVSTPublPanel.setExpanded(false);
        causaAveriaPextPanel.setDisable(true);
        delitoVSPExtPanel.setDisable(true);
        accidenteTransitoPanel.setDisable(false);
        delitoVSTPublPanel.setDisable(true);
        hideCheckPrevenido();
    }

    private void activePaneAveriaPext(){
        causaAveriaPextPanel.setExpanded(true);
        accidenteTransitoPanel.setExpanded(false);
        delitoVSPExtPanel.setExpanded(false);
        delitoVSTPublPanel.setExpanded(false);
        causaAveriaPextPanel.setDisable(false);
        delitoVSPExtPanel.setDisable(true);
        accidenteTransitoPanel.setDisable(true);
        delitoVSTPublPanel.setDisable(true);
        hideCheckPrevenido();
    }

    private void disableALLPanes() {
        causaAveriaPextPanel.setExpanded(false);
        accidenteTransitoPanel.setExpanded(false);
        delitoVSPExtPanel.setExpanded(false);
        delitoVSTPublPanel.setExpanded(false);
        causaAveriaPextPanel.setDisable(true);
        delitoVSPExtPanel.setDisable(true);
        accidenteTransitoPanel.setDisable(true);
        delitoVSTPublPanel.setDisable(true);
        hideCheckPrevenido();
    }

    private void hideCheckPrevenido(){
        this.checkPrevenido.setVisible(false);
        this.checkPrevenido.setSelected(false);
    }

//    private void llenarComboMaterial() {
//        this.materialComboBox.setPromptText("Seleccione");
//        this.materialComboBox.getItems().setAll(
//                ServiceLocator.getTipoMaterialesService()
//                .fetchAll()
//                .stream()
//                .map(TipoMateriales::getMateriales)
//                .collect(Collectors.toList())
//        );
//    }

//    private void llenarComboAfectacion() {
//        this.vandalismoComboBox.setPromptText("Seleccione");
//        this.vandalismoComboBox.getItems().setAll(
//                ServiceLocator.getTipoVandalismoService()
//                        .fetchAll()
//                        .stream()
//                        .map(TipoVandalismo::getAfect_tpublica)
//                        .collect(Collectors.toList())
//        );
//    }

}
