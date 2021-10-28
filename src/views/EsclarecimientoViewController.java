package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Hechos;
import models.HechosEsclarecimiento;
import models.UnidadOrganizativa;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginViewController;
import services.ServiceLocator;
import util.DateUtil;
import util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class EsclarecimientoViewController {

    @FXML
    private TextField codCDNT;
    @FXML
    private TextField unidadOrganizativa;
    @FXML
    private TextField fecha;
    @FXML
    private TextField municipio;
    @FXML
    private TextField tipo;
    @FXML
    private TextField denuncia;
    @FXML
    private TextField afectacionMN;
    @FXML
    private TextField afectacionMLC;
    @FXML
    private TextField afectacionServicios;
    @FXML
    private TextField valorChangerByInfractionType;
    @FXML
    private TextArea sintesis;
    @FXML
    private JFXRadioButton sinDenuncia;
    @FXML
    private JFXRadioButton expedienteSinAC;
    @FXML
    private JFXRadioButton esclarecido;

    @FXML
    private TitledPane tipoEsclarecimiento;
    @FXML
    private JFXButton btnSave;


    @FXML
    private RadioButton articulo83;
    @FXML
    private RadioButton articulo82;
    @FXML
    private RadioButton medidasAdministrativas;
    @FXML
    private RadioButton pendienteAJuicio;
    @FXML
    private RadioButton expendienteFasePrepartoria;
    @FXML
    private RadioButton menorEdad;
    @FXML
    private RadioButton pendienteDespacho;
    @FXML
    private RadioButton privacionLibertad;

    @FXML
    private JFXTextField cantSancionados;
    @FXML
    private JFXTextArea sentencia;
    @FXML
    private TitledPane estadoHecho;
    @FXML
    private HBox buttonBox;
    @FXML
    private AnchorPane panelPrincipal;
    @FXML
    private ComboBox<String> cboxUorg;

    //Variables for the correct searching and movement in data
    private LinkedList<HechosEsclarecimiento> hechosCopyOriginalList;
    private LinkedList<HechosEsclarecimiento> hechosList;

    private HechosEsclarecimiento hechoActual;

    private MainApp mainApp;
    private Stage dialogStage;

    private UserLoggedIn logged;

    @FXML
    private void handleNext() {
        try {
            int position = hechosList.indexOf(hechoActual) + 1;
            cargarDatos(hechosList.get(position));
        } catch (NoSuchElementException | IndexOutOfBoundsException e) {
            dialogElemento("primer");
        }
    }


    @FXML
    private void handleLast() {
        try {
            cargarDatos(hechosList.getLast());
        } catch (NoSuchElementException | IndexOutOfBoundsException e) {
            dialogElemento("primer");
        }
    }

    @FXML
    private void handlePrevious() {
        try {
            int position = hechosList.indexOf(hechoActual) - 1;
            cargarDatos(hechosList.get(position));
        } catch (NoSuchElementException | IndexOutOfBoundsException e) {
            dialogElemento("último");
        }
    }

    @FXML
    private void handleFirst() {
        try {
            cargarDatos(hechosList.getFirst());
        } catch (NoSuchElementException | IndexOutOfBoundsException e) {
            dialogElemento("último");
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dailogStage) {
        this.dialogStage = dailogStage;

    }

    @FXML
    private void initialize() {
        this.logged = LoginViewController.getUserLoggedIn();

        if (logged.isSuperuser()) {
            this.btnSave.setVisible(true);
        } else if (logged.hasPermiso_visualizacion() || logged.hasPermiso_pases()) {
            this.btnSave.setVisible(false);
        }
        //try {
        this.hechosList = (LinkedList<HechosEsclarecimiento>) obtenerHechosPextTpub();
//        } catch (Exception e) {
//            ExceptionDialog dialog = new ExceptionDialog(e);
//            dialog.showAndWait();
//        }

        this.cantSancionados.setVisible(false);
        this.sentencia.setVisible(false);
        this.fecha.setEditable(false);

        //Toggle group to the radio buttons tipo esclarecimiento
        ToggleGroup toggleGroup1 = new ToggleGroup();
        this.articulo82.setToggleGroup(toggleGroup1);
        this.articulo83.setToggleGroup(toggleGroup1);
        this.medidasAdministrativas.setToggleGroup(toggleGroup1);
        this.pendienteAJuicio.setToggleGroup(toggleGroup1);
        this.expendienteFasePrepartoria.setToggleGroup(toggleGroup1);
        this.menorEdad.setToggleGroup(toggleGroup1);
        this.pendienteDespacho.setToggleGroup(toggleGroup1);
        this.privacionLibertad.setToggleGroup(toggleGroup1);

        //The toggle group for establish the status of the hecho model
        ToggleGroup toggleGroup2 = new ToggleGroup();
        this.expedienteSinAC.setToggleGroup(toggleGroup2);
        this.esclarecido.setToggleGroup(toggleGroup2);
        this.sinDenuncia.setToggleGroup(toggleGroup2);

        this.tipoEsclarecimiento.setExpanded(false);
        this.tipoEsclarecimiento.setAnimated(true);
        this.tipoEsclarecimiento.setVisible(false);
        this.tipoEsclarecimiento.setDisable(true);

        this.cboxUorg.getItems().add("<<Todos>>");
        this.cboxUorg.getItems().addAll(
                ServiceLocator.getUnidadOrganizativaService().fetchAll()
                        .stream()
                        .map(UnidadOrganizativa::getUnidad_organizativa)
                        .collect(Collectors.toList())
        );

        this.cboxUorg.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    if (!newValue.equalsIgnoreCase("<<Todos>>"))
                        this.advancedSearchByUOrg();
                    else {
                        this.hechosList.clear();
                        this.hechosList.addAll(this.obtenerHechosPextTpub());
                        this.cargarDatos(this.hechosList.getFirst());
                    }
                }
                ));

        this.esclarecido.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!oldValue) {
                        try {
                            this.modificationToScreen();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        this.tipoEsclarecimiento.setDisable(false);
                        this.tipoEsclarecimiento.setVisible(true);
                        this.tipoEsclarecimiento.setExpanded(true);
                    } else {
                        this.toNormalScreen();
                        this.tipoEsclarecimiento.setDisable(true);
                        this.tipoEsclarecimiento.setVisible(false);
                        this.tipoEsclarecimiento.setExpanded(false);
                        this.articulo83.selectedProperty().setValue(false);
                        this.articulo82.selectedProperty().setValue(false);
                        this.medidasAdministrativas.selectedProperty().setValue(false);
                        this.pendienteAJuicio.selectedProperty().setValue(false);
                        this.expendienteFasePrepartoria.selectedProperty().setValue(false);
                        this.menorEdad.selectedProperty().setValue(false);
                        this.pendienteDespacho.selectedProperty().setValue(false);
                        this.privacionLibertad.selectedProperty().setValue(false);

                    }
                });

        this.privacionLibertad.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!oldValue) {
                        this.sentencia.setVisible(true);
                        this.cantSancionados.setVisible(true);
                    } else {
                        this.sentencia.setVisible(false);
                        this.cantSancionados.setVisible(false);
                        this.sentencia.setText("");
                        this.cantSancionados.setText("");
                    }
                }
        );

//        try {
//            this.hechosList = (LinkedList<HechosEsclarecimiento>) obtenerHechosPextTpub();
//        } catch (Exception e) {
//            ExceptionDialog dialog = new ExceptionDialog(e);
//            dialog.showAndWait();
//        }

        this.cargarDatos(hechosList.getFirst());

        //Clonando la lista original para hacer la carga mucho mas rapida luego que se elimino
        //   all en la busqueda

//        try {
//            this.hechosCopyOriginalList = (LinkedList<HechosEsclarecimiento>) hechosList.clone();
//        } catch (ClassCastException e) {
//            e.printStackTrace();
//        }
    }

    private void advancedSearchByUOrg() {
        String uorgSeleccionada = this.cboxUorg.getSelectionModel().getSelectedItem();

        if (uorgSeleccionada != null || (!uorgSeleccionada.isEmpty())) {
            this.hechosList.clear();
            try {
                hechosList.addAll(this.obtenerHechosBySubStringCodCDNT(uorgSeleccionada));
                cargarDatos(hechosList.getFirst());
            } catch (NoSuchElementException e) {
                hechosList.clear();
                hechosList.addAll(this.obtenerHechosPextTpub());
                cargarDatos(hechosList.getFirst());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("No se ha encontrado ningún elemento");
                alert.setTitle("Elemento no encontrado");
                alert.showAndWait();
            }
        } else {
            Util.dialogResult("Debe seleccionar una unidad organizativa para realizar la búsqueda.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleCancel() {
        this.dialogStage.close();
    }

    /**
     * @param hechos data who be charged in the user interface
     */
    private void cargarDatos(HechosEsclarecimiento hechos) {
        this.hechoActual = hechos;
        Hechos hecho = ServiceLocator.getHechosService().getHecho(hechos.getIdReg());

        this.tipo.setText(hecho.getTipoHecho().getTipo_hecho());
        this.codCDNT.setText(hecho.getCod_cdnt());
        this.unidadOrganizativa.setText(hecho.getUnidadOrganizativa().getUnidad_organizativa());
        this.municipio.setText(hecho.getMunicipio().getMunicipio());

        this.fecha.setText(DateUtil.format(hecho.getFecha_ocurrencia().toLocalDate()));
        this.afectacionServicios.setText(hecho.getAfectacion_servicio().toString());
        this.afectacionMLC.setText(hecho.getAfectacion_usd().toString());
        this.afectacionMN.setText(hecho.getAfectacion_mn().toString());
        if (hecho.getTipoHecho().getId_tipo_hecho() == 1) {
            this.valorChangerByInfractionType.setText(hecho.getMateriales().getMateriales());
        } else {
            this.valorChangerByInfractionType.setText(hecho.getTipoVandalismo().getAfect_tpublica());
        }
        this.denuncia.setText(hecho.getNumero_denuncia());
        this.sintesis.setText(hecho.getTitulo() + ". " + hecho.getLugar());

        //Cargar boolean data
        this.chargeBooleanDataToUI(hecho);
    }

    private void chargeBooleanDataToUI(Hechos hechos) {
        this.expedienteSinAC.selectedProperty().setValue(hechos.isExpediente_inv_sac());
        this.sinDenuncia.selectedProperty().setValue(hechos.isSin_denuncia());
        this.esclarecido.selectedProperty().setValue(hechos.isEsclarecido());

        this.articulo83.selectedProperty().setValue(hechos.isArticulo_83());
        this.articulo82.selectedProperty().setValue(hechos.isArticulo_82());
        this.medidasAdministrativas.selectedProperty().setValue(hechos.isMedida_administrativa());
        this.pendienteAJuicio.selectedProperty().setValue(hechos.isPendiente_juicio());
        this.expendienteFasePrepartoria.selectedProperty().setValue(hechos.isExpfp());
        this.menorEdad.selectedProperty().setValue(hechos.isMenor_edad());
        this.pendienteDespacho.selectedProperty().setValue(hechos.isPendiente_despacho());
        this.privacionLibertad.selectedProperty().setValue(hechos.isPriv_lib());

        this.cantSancionados.setText(String.valueOf(hechos.getCantidad_sancionados()));
        this.sentencia.setText(hechos.getSentencia());
    }

    /**
     * Method to change the screen size when esclarecido is selected
     */
    private void modificationToScreen() {
        this.dialogStage.setWidth(650);
        this.dialogStage.setHeight(660);
        this.panelPrincipal.setPrefWidth(631);
        this.panelPrincipal.setPrefHeight(616);

        this.estadoHecho.setPrefWidth(631);
        this.estadoHecho.setPrefHeight(234);
        this.estadoHecho.parentToLocal(0, 0);

        this.tipoEsclarecimiento.setPrefWidth(599);
        this.tipoEsclarecimiento.setPrefHeight(147);
        this.tipoEsclarecimiento.parentToLocal(0, 0);

        this.buttonBox.setLayoutX(381);
        this.buttonBox.setLayoutY(576);
    }

    /**
     * Method to change the screen size when esclarecido is unselected
     */
    private void toNormalScreen() {
        this.dialogStage.setWidth(650);
        this.dialogStage.setHeight(560);
        this.panelPrincipal.setPrefWidth(631);
        this.panelPrincipal.setPrefHeight(523);

        this.estadoHecho.setPrefWidth(631);
        this.estadoHecho.setPrefHeight(121);
        this.estadoHecho.parentToLocal(0, 0);

        this.tipoEsclarecimiento.setPrefWidth(599);
        this.tipoEsclarecimiento.setPrefHeight(30);
        this.tipoEsclarecimiento.parentToLocal(0, 0);

        this.buttonBox.setLayoutX(381);
        this.buttonBox.setLayoutY(469);
    }


    @FXML
    private void handleAccept() {
        boolean sinDenunciaData = this.sinDenuncia.selectedProperty().getValue();
        boolean expSACData = this.expedienteSinAC.selectedProperty().getValue();
        boolean esclarecidoData = this.esclarecido.selectedProperty().getValue();
        boolean articulo83Data = this.articulo83.selectedProperty().getValue();
        boolean articulo82Data = this.articulo82.selectedProperty().getValue();
        boolean medidasAdministrativasData = this.medidasAdministrativas.selectedProperty().getValue();
        boolean pendienteAJuicioData = this.pendienteAJuicio.selectedProperty().getValue();
        boolean expedienteFasePreparatoriaData = this.expendienteFasePrepartoria.selectedProperty().getValue();
        boolean menorEdadData = this.menorEdad.selectedProperty().getValue();
        boolean pendienteDespachoData = this.pendienteDespacho.selectedProperty().getValue();
        boolean privacionLibertadData = this.privacionLibertad.selectedProperty().getValue();
        String cantSancionadosData = cantSancionados.getText();
        String sentenciaData = sentencia.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Desea actualizar este hecho?"
                , ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            Hechos hecho = ServiceLocator.getHechosService().getHecho(hechoActual.getIdReg());
            ServiceLocator.
                    getHechosService().
                    esclarecimientoHechoPExtTpubl(hecho.getId_reg(),
                            sinDenunciaData, expSACData, esclarecidoData, articulo83Data,
                            articulo82Data, medidasAdministrativasData, pendienteAJuicioData,
                            expedienteFasePreparatoriaData, menorEdadData, pendienteDespachoData,
                            privacionLibertadData, cantSancionadosData, sentenciaData);
            Util.dialogResult("Hecho actualizado correctamente.", Alert.AlertType.INFORMATION);
        }
    }


    /**
     * Method to create a dialog which some information
     *
     * @param estado local variable to know where are you
     */
    private void dialogElemento(String estado) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información");
        alert.setContentText("Usted se encuentra en el " + estado + " elemento");
        alert.showAndWait();
    }

    private List<HechosEsclarecimiento> obtenerHechosPextTpub() {
        List<HechosEsclarecimiento> hechos = new LinkedList<>();
        String query = "SELECT id_reg " +
                "From hechos " +
                "WHERE (hechos.id_tipo_hecho = 1 or hechos.id_tipo_hecho =2) " +
                "ORDER BY fecha_ocurrencia DESC";
        try {
            ResultSet rs = Util.executeQuery(query);
            hechos = getDataFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hechos;
    }

    private List<HechosEsclarecimiento> obtenerHechosBySubStringCodCDNT(String uo) {
        List<HechosEsclarecimiento> hechos = new LinkedList<>();
        String query = "SELECT  id_reg " +
                "From hechos " +

                "JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg " +

                "WHERE (hechos.id_tipo_hecho = 1 or hechos.id_tipo_hecho = 2)" +
                "and unidades_organizativas.unidad_organizativa = '" + uo + "'  ORDER BY fecha_ocurrencia DESC";

        try {
            ResultSet rs = Util.executeQuery(query);
            hechos = getDataFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hechos;
    }

    private List<HechosEsclarecimiento> getDataFromResultSet(ResultSet rs) throws SQLException {
        List<HechosEsclarecimiento> hechos = new LinkedList<>();

        while (rs.next()) {
            hechos.add(new HechosEsclarecimiento(
                    rs.getInt(1)
            ));
        }

        return hechos;
    }
}
