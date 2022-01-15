package views;


import com.jfoenix.controls.JFXButton;
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

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Dialog to register a new Hechos into the database
 *
 * @author Humberto Cabrera Domínguez
 */
public class RegistrarViewController {

    boolean incidente;
    boolean imputable;
    boolean fromPrincipal = true;
    @FXML
    private TextField titulo;
    @FXML
    private Label tituloLabel;
    @FXML
    private TextArea lugarOcurrencia;
    @FXML
    private Label lugarOcurrenciaLabel;
    @FXML
    private TextField codCDNT;
    @FXML
    private Label codCDNTLabel;
    @FXML
    private TextField centro;
    @FXML
    private Label centroLabel;
    @FXML
    private TextField denuncia;
    @FXML
    private Label denunciaLabel;
    @FXML
    private TextArea observaciones;
    @FXML
    private Label observacionesLabel;
    @FXML
    private TextField cantidad;
    @FXML
    private Label cantidadLabel;
    @FXML
    private TextField afectacionMN;
    @FXML
    private Label afectacionMNLabel;
    @FXML
    private TextField afectacionMLC;
    @FXML
    private Label afectacionMLCLabel;
    @FXML
    private TitledPane delitoVSPExtPanel;
    @FXML
    private TitledPane delitoVSTPublPanel;
    @FXML
    private TitledPane accidenteTransitoPanel;
    @FXML
    private ComboBox<String> tipoHechoComboBox;
    @FXML
    private Label tipoHechoLabel;
    @FXML
    private ComboBox<String> municipioComboBox;
    @FXML
    private Label municipioLabel;
    @FXML
    private ComboBox<String> unidadOrganizativaComboBox;
    @FXML
    private Label unidadOragnizativaLabel;
    @FXML
    private ComboBox<String> materialComboBox;
    @FXML
    private Label materialLabel;
    @FXML
    private Label vandalismoLabel;
    @FXML
    private TextField afectacionService;
    @FXML
    private Label afectacionLabelLabel;
    @FXML
    private RadioButton radioButtonIncidente;
    @FXML
    private RadioButton radioButtonImputable;
    @FXML
    private DatePicker fechaOcurrencia;
    @FXML
    private CheckBox checkPrevenido;

    public void setFromPrincipal(boolean principal) {
        this.fromPrincipal = principal;
    }

    /**
     * This a callback to no make selectable the cell in the calendar before
     *
     * @see this.fechaOcurrencia
     */
    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker datePicker) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item.isBefore(fechaOcurrencia.getValue())) {
                        setDisable(true);
                    }
                }
            };
        }
    };
    @FXML
    private Label fechaOcurrenciaLabel;
    @FXML
    private DatePicker fechaResumen;
    @FXML
    private Label fechaResumenLabel;
    private Stage dialogStage;
    @FXML
    private ComboBox<String> vandalismoComboBox;
    @FXML
    private JFXButton btnRegister;

    private Hechos hechoToUpdate;
    private boolean toUpdate;

    private BorderPane mainApp;

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
        HechosRegistradosViewController.setHechoSeleccionado(null);
        dialogStage.close();
    }


    @FXML
    private void initialize() {
        this.titulo.requestFocus();
        this.afectacionMLC.setText("0.0");
        this.afectacionMN.setText("0.0");

        /*
        Initializing the comboBox that is necesary
         */
        municipioComboBox.getItems().addAll(llenarComboMunicipios());
        tipoHechoComboBox.getItems().addAll(llenarComboTipoHechos());
        unidadOrganizativaComboBox.getItems().addAll(llenarUnidadOrganizativa());


        tipoHechoComboBox.setEditable(false);

        tipoHechoComboBox.setOnAction(e ->
                handleTipoHechoPanes(tipoHechoComboBox.getValue()));


        TextFields.bindAutoCompletion(municipioComboBox.getEditor(), municipioComboBox.getItems());
        TextFields.bindAutoCompletion(unidadOrganizativaComboBox.getEditor(), unidadOrganizativaComboBox.getItems());

        //Disable the values in the date picker before at the occurrence day
        fechaResumen.setDayCellFactory(dayCellFactory);
        /*
        Set Expanded in false when the dialog is open
         */
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
                    System.out.println("Imputable " + this.imputable);
                    System.out.println("Incidente" + this.incidente);
                });
        radioButtonIncidente.selectedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    this.imputable = false;
                    this.incidente = true;
                    System.out.println("Imputable " + this.imputable);
                    System.out.println("Incidente" + this.incidente);
                });

        this.toUpdate = false;

        this.hechoToUpdate = HechosRegistradosViewController.getHechoSeleccionado();
        if (hechoToUpdate != null && hechoToUpdate.getId_reg() > 0) {
            this.btnRegister.setText("Guardar");
            this.toUpdate = true;
            System.out.println("ToUpdate");
            this.cargarInformacionHechoSeleccionadoBusqueda();
        }
    }

    private void cargarInformacionHechoSeleccionadoBusqueda() {
        if (hechoToUpdate != null) {
            this.titulo.setText(hechoToUpdate.getTitulo());
            this.codCDNT.setText(hechoToUpdate.getCod_cdnt() == null
                    ? "(sin información)"
                    : hechoToUpdate.getCod_cdnt());
            String tipoH = hechoToUpdate.getTipoHecho().getTipo_hecho();
            this.tipoHechoComboBox.getSelectionModel().select(tipoH);
            if (tipoH.equalsIgnoreCase("Delito vs PExt")) {
                this.activePaneDelitoVSPExt();
                String material = hechoToUpdate.getMateriales().getMateriales();
                this.materialComboBox.getSelectionModel().select(material);
                this.cantidad.setText(hechoToUpdate.getCantidad() + "");
            } else if (tipoH.equalsIgnoreCase("Delito vs TPúb")) {
                this.activePaneDelitoVSTPubl();
                String afect = hechoToUpdate.getTipoVandalismo().getAfect_tpublica();
                this.vandalismoComboBox.getSelectionModel().select(afect);
            } else if (tipoH.equalsIgnoreCase("Acc. Tránsito")) {
                this.activePaneAccTransito();
                this.radioButtonImputable.setSelected(hechoToUpdate.isImputable());
                this.radioButtonIncidente.setSelected(hechoToUpdate.isIncidencias());
            } else if (tipoH.equalsIgnoreCase("Robo")) {
                this.disableALLPanes();
                this.checkPrevenido.setVisible(true);
                this.checkPrevenido.setSelected(hechoToUpdate.isPrevenido());
            } else {
                this.disableALLPanes();
            }
            this.fechaOcurrencia.setValue(hechoToUpdate.getFecha_ocurrencia().toLocalDate());
            this.fechaResumen.setValue(hechoToUpdate.getFecha_parte().toLocalDate());
            this.denuncia.setText(hechoToUpdate.getNumero_denuncia() == null
                    ? "(sin información)"
                    : hechoToUpdate.getNumero_denuncia());
            this.centro.setText(hechoToUpdate.getCentro());
            this.lugarOcurrencia.setText(hechoToUpdate.getLugar());
            String municip = hechoToUpdate.getMunicipio().getMunicipio();
            this.municipioComboBox.getSelectionModel().select(municip);
            String uo = hechoToUpdate.getUnidadOrganizativa().getUnidad_organizativa();
            this.unidadOrganizativaComboBox.getSelectionModel().select(uo);
            this.afectacionMN.setText(hechoToUpdate.getAfectacion_mn() + "");
            this.afectacionMLC.setText(hechoToUpdate.getAfectacion_usd() + "");
            this.afectacionService.setText(hechoToUpdate.getAfectacion_servicio() + "");
            this.observaciones.setText(hechoToUpdate.getObservaciones() == null
                    ? "(sin información)"
                    : hechoToUpdate.getObservaciones());
        }
    }

    private List<String> llenarComboMunicipios() {
        List<Municipio> municipioList = ServiceLocator.getMunicipiosService().fetchAll();
        List<String> nombreMunicipios = new LinkedList<>();
        municipioList
                .stream()
                .forEach(
                        municipio -> nombreMunicipios
                                .add(
                                        municipio.getMunicipio()
                                )
                );
        return nombreMunicipios;
    }

    private List<String> llenarComboTipoHechos() {
        List<String> nombreTipoHechos = new LinkedList<>();
        ServiceLocator.
                getTipoHechoService()
                .fetchAll()
                .stream()
                .forEach(
                        tipoHecho -> nombreTipoHechos
                                .add(
                                        tipoHecho.getTipo_hecho()
                                )
                );

        return nombreTipoHechos;
    }

    private List<String> llenarUnidadOrganizativa() {
        List<String> nombreUnidadOrganizativa = new LinkedList<>();
        ServiceLocator
                .getUnidadOrganizativaService()
                .fetchAll()
                .stream()
                .forEach(
                        unidadOrganizativa ->
                                nombreUnidadOrganizativa
                                        .add(unidadOrganizativa.getUnidad_organizativa())

                );
        return nombreUnidadOrganizativa;
    }


    @FXML
    private void handleTipoHechoPanes(String value) {
        if (value.equalsIgnoreCase("Delito vs PExt")) {
            this.activePaneDelitoVSPExt();
        } else if (value.equalsIgnoreCase("Delito vs TPúb")) {
            this.activePaneDelitoVSTPubl();
        } else if (value.equalsIgnoreCase("Acc. Tránsito")) {
            this.activePaneAccTransito();
        } else if (value.equalsIgnoreCase("Robo")) {
            disableALLPanes();
            this.checkPrevenido.setVisible(true);
        } else {
            disableALLPanes();
        }
    }

    private void hideCheckPrevenido(){
        this.checkPrevenido.setVisible(false);
        this.checkPrevenido.setSelected(false);
    }

    private void activePaneDelitoVSPExt() {
        hideCheckPrevenido();
        accidenteTransitoPanel.setExpanded(false);
        delitoVSPExtPanel.setExpanded(true);
        delitoVSTPublPanel.setExpanded(false);
        accidenteTransitoPanel.setDisable(true);
        delitoVSTPublPanel.setDisable(true);
        delitoVSPExtPanel.setDisable(false);
        llenarComboMaterial();
    }

    private void activePaneDelitoVSTPubl() {
        hideCheckPrevenido();
        accidenteTransitoPanel.setExpanded(false);
        delitoVSPExtPanel.setExpanded(false);
        delitoVSTPublPanel.setExpanded(true);
        accidenteTransitoPanel.setDisable(true);
        delitoVSPExtPanel.setDisable(true);
        delitoVSTPublPanel.setDisable(false);
        llenarComboAfectacion();
    }

    private void activePaneAccTransito() {
        hideCheckPrevenido();
        accidenteTransitoPanel.setExpanded(true);
        delitoVSPExtPanel.setExpanded(false);
        delitoVSTPublPanel.setExpanded(false);
        accidenteTransitoPanel.setDisable(false);
        delitoVSPExtPanel.setDisable(true);
        delitoVSTPublPanel.setDisable(true);
    }

    private void disableALLPanes() {
        hideCheckPrevenido();
        accidenteTransitoPanel.setExpanded(false);
        delitoVSPExtPanel.setExpanded(false);
        delitoVSTPublPanel.setExpanded(false);
        accidenteTransitoPanel.setDisable(true);
        delitoVSPExtPanel.setDisable(true);
        delitoVSTPublPanel.setDisable(true);
    }

    private void llenarComboMaterial() {
        List<String> materialesList = new LinkedList<>();
        ServiceLocator.getTipoMaterialesService()
                .fetchAll()
                .stream()
                .forEach(p -> materialesList.add(p.getMateriales()));
        this.materialComboBox.setPromptText("Seleccione");
        this.materialComboBox.getItems().addAll(materialesList);
    }

    private void llenarComboAfectacion() {
        List<String> tipoVandalismo = new LinkedList<>();
        ServiceLocator.getTipoVandalismoService()
                .fetchAll()
                .stream()
                .forEach(p -> tipoVandalismo.add(p.getAfect_tpublica()));
        this.vandalismoComboBox.setPromptText("Seleccione");
        this.vandalismoComboBox.getItems().setAll(tipoVandalismo);
    }

    private boolean validarCamposVacios() {
        boolean correcto = true;
        if (this.codCDNT.getText().isEmpty()) {
            correcto = false;
            this.codCDNTLabel.setText(this.codCDNTLabel.getText() + " *");
            this.codCDNTLabel.setTextFill(Color.RED);
        }
        try {
            if (tipoHechoComboBox.getValue().isEmpty()) {
                correcto = false;
                this.tipoHechoLabel.setText(this.tipoHechoLabel.getText() + " *");
                this.tipoHechoLabel.setTextFill(Color.RED);
            }
        } catch (NullPointerException e) {
            correcto = false;
            this.tipoHechoLabel.setText(this.tipoHechoLabel.getText() + " *");
            this.tipoHechoLabel.setTextFill(Color.RED);
        }
        if (this.titulo.getText().isEmpty()) {
            correcto = false;
            this.tituloLabel.setText(this.tituloLabel.getText() + " *");
            this.tituloLabel.setTextFill(Color.RED);
        }
        try {
            if (this.unidadOrganizativaComboBox.getValue().isEmpty()) {
                correcto = false;
            }
        } catch (NullPointerException e) {
            correcto = false;
            this.unidadOragnizativaLabel.setText(this.unidadOragnizativaLabel.getText() + " *");
            this.unidadOragnizativaLabel.setTextFill(Color.RED);

        }
        if (this.centro.getText().isEmpty()) {
            correcto = false;
            this.centroLabel.setText(this.centroLabel.getText() + " *");
            this.centroLabel.setTextFill(Color.RED);

        }
        try {
            if (this.municipioComboBox.getValue().isEmpty()) {
                correcto = false;
                this.municipioLabel.setText(this.municipioLabel.getText() + " *");
                this.municipioLabel.setTextFill(Color.RED);
            }
        } catch (NullPointerException e) {
            correcto = false;
            this.municipioLabel.setText(this.municipioLabel.getText() + " *");
            this.municipioLabel.setTextFill(Color.RED);
        }
        if (this.lugarOcurrencia.getText().isEmpty()) {
            correcto = false;
            this.lugarOcurrenciaLabel.setTextFill(Color.RED);
            this.lugarOcurrenciaLabel.setText(this.lugarOcurrenciaLabel.getText() + " *");
        }
        try {
            if (this.fechaResumen.getValue().toString().isEmpty()) {
                correcto = false;
            }
        } catch (NullPointerException e) {
            correcto = false;
            this.fechaResumenLabel.setText(this.fechaResumenLabel.getText() + " *");
            this.fechaResumenLabel.setTextFill(Color.RED);
        }
        try {
            if (this.fechaOcurrencia.getValue().toString().isEmpty()) {
                correcto = false;
            }
        } catch (NullPointerException e) {
            correcto = false;
            this.fechaOcurrenciaLabel.setText(this.fechaOcurrenciaLabel.getText() + " *");
            this.fechaOcurrenciaLabel.setTextFill(Color.RED);

        }
        try {
            if (this.tipoHechoComboBox.getValue().equalsIgnoreCase("Delito vs PExt")) {
                try {
                    if (this.materialComboBox.getValue().isEmpty()) {
                        correcto = false;
                    }
                } catch (NullPointerException e) {
                    correcto = false;
                    this.materialLabel.setText(this.materialLabel.getText() + " *");
                    this.materialLabel.setTextFill(Color.RED);
                }
                if (this.cantidad.getText().isEmpty()) {
                    correcto = false;
                    this.cantidadLabel.setText(this.cantidadLabel.getText() + " *");
                    this.cantidadLabel.setTextFill(Color.RED);
                }
            } else if (this.tipoHechoComboBox.getValue().equalsIgnoreCase("Delito vs TPúb")) {
                try {
                    if (this.vandalismoComboBox.getValue().isEmpty()) {
                        correcto = false;
                    }
                } catch (NullPointerException e) {
                    correcto = false;
                    this.vandalismoLabel.setText(this.vandalismoLabel.getText() + " *");
                    this.vandalismoLabel.setTextFill(Color.RED);
                }
            } //else if (this.tipoHechoComboBox.getValue().equalsIgnoreCase("Acc. Tránsito")) {
//
//                if (!imputable && !incidente) {
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("No definido el tipo de accidente");
//                    alert.setHeaderText(null);
//                    alert.setContentText("Debe seleccionar el tipo de accidente de tránsito");
//                    alert.showAndWait();
//                    correcto = false;
//                }
//            }
        } catch (NullPointerException e) {

            this.tipoHechoLabel.setTextFill(Color.RED);
        }

        return correcto;
    }

    private void settearColoresNormalesLetras() {
        this.cantidadLabel.setTextFill(Color.BLACK);
        this.cantidadLabel.setText("Cantidad");
        this.vandalismoLabel.setTextFill(Color.BLACK);
        this.vandalismoLabel.setText("Afectación");
        this.codCDNTLabel.setTextFill(Color.BLACK);
        this.codCDNTLabel.setText("Cod CDNT");
        this.tituloLabel.setTextFill(Color.BLACK);
        this.tituloLabel.setText("Titulo");
        this.materialLabel.setTextFill(Color.BLACK);
        this.materialLabel.setText("Material");
        this.fechaOcurrenciaLabel.setTextFill(Color.BLACK);
        this.fechaOcurrenciaLabel.setText("Fecha ocurrido");
        this.fechaResumenLabel.setTextFill(Color.BLACK);
        this.fechaResumenLabel.setText("Fecha resumen");
        this.lugarOcurrenciaLabel.setTextFill(Color.BLACK);
        this.lugarOcurrenciaLabel.setText("Lugar");
        this.municipioLabel.setTextFill(Color.BLACK);
        this.municipioLabel.setText("Municipio");
        this.centroLabel.setTextFill(Color.BLACK);
        this.centroLabel.setText("Centro");
        this.unidadOragnizativaLabel.setTextFill(Color.BLACK);
        this.unidadOragnizativaLabel.setText("Unidad Organizativa");
        this.tipoHechoLabel.setTextFill(Color.BLACK);
        this.tipoHechoLabel.setText("Tipo de Hecho");
    }

    @FXML
    public void handleRegistrar() {
        this.settearColoresNormalesLetras();
        if (validarCamposVacios()) {
            if (!toUpdate) {
                boolean insertado = false;
                int cantIntentos = 0;

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("Confirmación");
                alert.setContentText("¿Desea registrar el hecho?");
                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK) {
                    while (!insertado && cantIntentos < 100000) {
                        insertado = registrarHecho();
                        cantIntentos++;
                    }
                }
                if (insertado) {
                    Util.dialogResult("Se registró correctamente el hecho.", Alert.AlertType.INFORMATION);
                    this.cleanData();
                } else
                    Util.dialogResult("Error al registrar", Alert.AlertType.ERROR);
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "¿Desea editar realmente este hecho?"
                        , ButtonType.OK, ButtonType.CANCEL);
                alert.showAndWait();
                boolean updated = false;
                if (alert.getResult() == ButtonType.OK) {
                    updated = this.updateHecho();
                }
                if (updated) {
                    Util.dialogResult("El hecho fue modificado correctamente.", Alert.AlertType.INFORMATION);
                } else
                    Util.dialogResult("Ha ocurrido un error en la modificación.", Alert.AlertType.ERROR);

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Campos en blanco");
            alert.setContentText("Existen campos en blanco por favor rellenelos");
            alert.showAndWait();
        }
    }

    public boolean updateHecho() {
        boolean updated = false;

        hechoToUpdate.setTitulo(titulo.getText());
        hechoToUpdate.setUnidadOrganizativa(this.buscarUnidadOrganizativa(
                unidadOrganizativaComboBox.getSelectionModel().getSelectedItem())
        );
        hechoToUpdate.setMunicipio(this.buscarMunicipio(
                municipioComboBox.getSelectionModel().getSelectedItem())
        );
        hechoToUpdate.setTipoHecho(this.buscarTipoHecho(
                tipoHechoComboBox.getSelectionModel().getSelectedItem())
        );
        hechoToUpdate.setNumero_denuncia(denuncia.getText());
        hechoToUpdate.setCod_cdnt(codCDNT.getText());
        hechoToUpdate.setLugar(lugarOcurrencia.getText());
        hechoToUpdate.setCentro(centro.getText());
        hechoToUpdate.setObservaciones(observaciones.getText());
        hechoToUpdate.setFecha_ocurrencia(Date.valueOf(fechaOcurrencia.getValue()));
        hechoToUpdate.setFecha_parte(Date.valueOf(fechaResumen.getValue()));
        hechoToUpdate.setAfectacion_mn(Double.valueOf(afectacionMN.getText()));
        hechoToUpdate.setAfectacion_usd(Double.valueOf(afectacionMLC.getText()));
        hechoToUpdate.setAfectacion_servicio(Double.valueOf(afectacionService.getText()));

        if (!delitoVSPExtPanel.isDisabled()) {
            TipoMateriales material = this.buscarMaterial(this.materialComboBox.getValue());
            hechoToUpdate.setMateriales(material);
            int cantidadMaterial = Integer.valueOf(this.cantidad.getText());
            hechoToUpdate.setCantidad(cantidadMaterial);
            hechoToUpdate.setTipoVandalismo(null);
            hechoToUpdate.setImputable(false);
            hechoToUpdate.setIncidencias(false);
        } else if (!delitoVSTPublPanel.isDisabled()) {
            TipoVandalismo afectacion = this.buscarAfectacion(this.vandalismoComboBox.getValue());
            hechoToUpdate.setTipoVandalismo(afectacion);
            hechoToUpdate.setMateriales(null);
            hechoToUpdate.setCantidad(0);
            hechoToUpdate.setImputable(false);
            hechoToUpdate.setIncidencias(false);
        } else if (!accidenteTransitoPanel.isDisabled()) {
            hechoToUpdate.setImputable(this.imputable);
            hechoToUpdate.setIncidencias(this.incidente);
            hechoToUpdate.setMateriales(null);
            hechoToUpdate.setCantidad(0);
            hechoToUpdate.setTipoVandalismo(null);
        } else {
            hechoToUpdate.setImputable(false);
            hechoToUpdate.setIncidencias(false);
            hechoToUpdate.setMateriales(null);
            hechoToUpdate.setCantidad(0);
            hechoToUpdate.setTipoVandalismo(null);
            hechoToUpdate.setPrevenido(this.checkPrevenido.isSelected());
        }

        try {
            ServiceLocator.getHechosService().updateHecho(hechoToUpdate);
            updated = true;
        } catch (SQLException e){
            e.printStackTrace();
        }

        return updated;
    }

    public boolean registrarHecho() {
        boolean insertado = false;
        //Fechas
        Date dateOcurrencia = Date.valueOf(this.fechaOcurrencia.getValue());
        Date dateResumen = Date.valueOf(this.fechaResumen.getValue());

        //Strings
        String titulo = this.titulo.getText();
        String lugarOcurrencia = this.lugarOcurrencia.getText();
        String codCDNT = this.codCDNT.getText();
        String centro = this.centro.getText();
        String denuncia = this.denuncia.getText();
        String perdidaMN = this.afectacionMN.getText().isEmpty() ? "0.0" : this.afectacionMN.getText();
        String perdiadMLC = this.afectacionMLC.getText().isEmpty() ? "0.0" : this.afectacionMLC.getText();
        int serviciosAfectados = this.afectacionService.getText().isEmpty() ? '0' : Integer.valueOf(this.afectacionService.getText());
        String observaciones = this.observaciones.getText();
        boolean prevenido = this.checkPrevenido.isSelected();

        //Obteniendo los datos del combobox para recuperarlos después
        TipoHecho tipoHecho = this.buscarTipoHecho(tipoHechoComboBox.getValue());
        System.out.println(tipoHecho);
        Municipio municipio = this.buscarMunicipio(municipioComboBox.getValue());
        System.out.println(municipio);
        UnidadOrganizativa unidadOrganizativa = this.buscarUnidadOrganizativa(unidadOrganizativaComboBox.getValue());
        Hechos hechos = new Hechos(titulo, tipoHecho, dateOcurrencia, dateResumen, unidadOrganizativa,
                centro, lugarOcurrencia, municipio, denuncia, Double.valueOf(perdiadMLC), Double.valueOf(perdidaMN),
                Double.valueOf(Integer.toString(serviciosAfectados)), observaciones, codCDNT, prevenido);

        try {
            //Moderando los datos respecto a algunos tipo de delitos
            if (!delitoVSPExtPanel.isDisabled()) {
                TipoMateriales material = this.buscarMaterial(this.materialComboBox.getValue());
                int cantidadMaterial = Integer.valueOf(this.cantidad.getText());
                //Saving hecho vs pext
                ServiceLocator.getHechosService()
                        .registrarHecho(hechos, material, cantidadMaterial);


            } else if (!delitoVSTPublPanel.isDisabled()) {
                TipoVandalismo afectacion = this.buscarAfectacion(this.vandalismoComboBox.getValue());
                //Saving hecho vs tpubl
                ServiceLocator.getHechosService()
                        .registrarHecho(hechos, afectacion);


            } else if (!accidenteTransitoPanel.isDisabled()) {
                //saving accidente transito
                ServiceLocator.getHechosService()
                        .registrarHecho(hechos, this.imputable, this.incidente);

            } else {
                //hecho ordinario
                ServiceLocator.getHechosService()
                        .registrarHecho(hechos);
            }
            insertado = true;
        } catch (SQLException e) {
            insertado = false;
        }

        return insertado;
    }

    private TipoVandalismo buscarAfectacion(String name) {
        return ServiceLocator.getTipoVandalismoService().searchVandalismoByName(name);
    }

    private TipoHecho buscarTipoHecho(String name) {
        return ServiceLocator.getTipoHechoService().searchTipoHechoByName(name);
    }

    private Municipio buscarMunicipio(String name) {
        return ServiceLocator.getMunicipiosService().searchMunicipioByName(name);
    }

    private UnidadOrganizativa buscarUnidadOrganizativa(String name) {
        return ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName(name);
    }

    private TipoMateriales buscarMaterial(String name) {
        return ServiceLocator.getTipoMaterialesService().searchMaterialesByName(name);
    }

    private void cleanData() {
        this.titulo.clear();
        this.lugarOcurrencia.clear();
        this.codCDNT.clear();
        this.centro.clear();
        this.denuncia.clear();
        this.observaciones.clear();
        this.cantidad.clear();
        this.afectacionMLC.clear();
        this.afectacionMN.clear();
        this.afectacionService.clear();
        this.tipoHechoComboBox.getSelectionModel().clearSelection();
        this.municipioComboBox.getSelectionModel().clearSelection();

        this.unidadOrganizativaComboBox.getSelectionModel().clearSelection();
        this.materialComboBox.getSelectionModel().clearSelection();
        this.radioButtonImputable.setSelected(false);
        this.radioButtonIncidente.setSelected(false);
        this.checkPrevenido.setSelected(false);
        this.fechaOcurrencia.setValue(null);
        this.fechaResumen.setValue(null);
    }
}
