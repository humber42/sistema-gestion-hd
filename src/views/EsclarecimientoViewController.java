package views;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Hechos;
import models.HechosEsclarecimiento;
import org.controlsfx.dialog.ExceptionDialog;
import org.postgresql.util.PSQLException;
import services.ServiceLocator;
import util.DateUtil;
import util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    private String subStringFormada = "";

    //Variables for the correct searching and movement in data
    private LinkedList<HechosEsclarecimiento> hechosCopyOriginalList;
    private LinkedList<HechosEsclarecimiento> hechosList;

    private Hechos hechoActual;

    private int offset;
    private int buscarOffset;
    private int offsetBusquedaMaximo;
    private boolean isSearch = false;
    private JFXTextField buscarPorCodCDNT;
    private int offsetMaximo;
    private MainApp mainApp;
    private Stage dialogStage;

    @FXML
    private void handleNext() {
        if (isSearch) {
            try {
                cargarDatos(hechosList.get(hechosList.indexOf(hechoActual) + 1));
            } catch (IndexOutOfBoundsException e) {
                if (buscarOffset + 10 != offsetBusquedaMaximo) {
                    hechosList.clear();
                    buscarOffset += 10;
                    hechosList.addAll(this.obtenerHechosBySubStringCodCDNT(subStringFormada, buscarOffset));
                    try {
                        cargarDatos(hechosList.getFirst());
                    } catch (NoSuchElementException exc) {
                        dialogElemento("último");
                    }
                } else {
                    dialogElemento("último");
                }
            }
        }
        /*
        Aqui no se encuentra buscando
         */
        else {
            try {
                cargarDatos(hechosList.get(hechosList.indexOf(hechoActual) + 1));
            } catch (IndexOutOfBoundsException e) {
                if (offset + 30 != offsetMaximo) {
                    hechosList.clear();
                    offset += 30;

                    hechosList.addAll(this.obtenerHechosPextTpub(offset));

                    try {
                        cargarDatos(hechosList.getFirst());
                    } catch (NoSuchElementException exc) {
                        dialogElemento("último");
                    }
                } else {
                    dialogElemento("último");
                }
            }
        }
    }

    @FXML
    private void handleLast() {
        if (isSearch) {
            cargarDatos(hechosList.getLast());
        } else {
            hechosList.clear();
            offset = offsetMaximo - 30;

            hechosList.addAll(this.obtenerHechosPextTpub(offset));

            try {
                cargarDatos(hechosList.getLast());
            } catch (NoSuchElementException e){
                Util.dialogResult("Usted se encuentra en el último elemento.", Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    private void handlePrevious() {
        if (isSearch) {
            try {
                cargarDatos(hechosList.get(hechosList.indexOf(hechoActual) - 1));
            } catch (IndexOutOfBoundsException e) {
                hechosList.clear();
                if (!(buscarOffset >= 10)) {
                    dialogElemento("primer");
                } else {
                    buscarOffset -= 10;
                    hechosList.addAll(this.obtenerHechosBySubStringCodCDNT(subStringFormada, buscarOffset));
                    try {
                        cargarDatos(hechosList.getFirst());
                    } catch (NoSuchElementException exc) {
                        dialogElemento("primer");
                    }
                }
            }
            /*
             Aqui no se encuentra en la busqueda
             */
        } else {
            try {
                cargarDatos(hechosList.get(hechosList.indexOf(hechoActual) - 1));
            } catch (IndexOutOfBoundsException e) {
                hechosList.clear();
                if (!(offset >= 30)) {
                    dialogElemento("primer");
                } else {
                    offset -= 30;
                    try {
                        hechosList.addAll(this.obtenerHechosPextTpub(offset));
                    } catch (Exception exc) {
                        dialogElemento("primer");
                        offset = 0;
                    }
                    try {
                        cargarDatos(hechosList.getFirst());
                    } catch (NoSuchElementException exc) {
                        dialogElemento("primer");
                    }
                }
            }
        }
    }

    @FXML
    private void handleFirst() {
        if (this.isSearch) {
            cargarDatos(hechosList.getFirst());
        } else {
            hechosList.clear();
            offset = 0;
            try {
                hechosList.addAll(this.obtenerHechosPextTpub(offset));
            } catch (Exception e) {
                ExceptionDialog dialog = new ExceptionDialog(e);
                dialog.showAndWait();
            }
            cargarDatos(hechosList.getFirst());
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
        this.buscarOffset = 0;

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

        try {
            this.hechosList = (LinkedList<HechosEsclarecimiento>) obtenerHechosPextTpub(offset);
        } catch (Exception e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }

        this.cargarDatos(hechosList.getFirst());

        /*Clonando la lista original para hacer la carga mucho mas rapida luego que se elimino
            all en la busqueda
         */
        try {
            this.hechosCopyOriginalList = (LinkedList<HechosEsclarecimiento>) hechosList.clone();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        this.offsetMaximo = ServiceLocator.getHechosService().countHechos();
    }

    @FXML
    public void advancedSearchByCodCDNT(KeyEvent event) {
        buscarOffset = 0;
        if (event.getCode() != KeyCode.BACK_SPACE) {
            this.subStringFormada += event.getText();
            hechosList.clear();
            this.isSearch = true;
            try {
                offsetBusquedaMaximo = ServiceLocator.getHechosService().countfetchBySubStringCodCDNT(subStringFormada);
                hechosList.addAll(this.obtenerHechosBySubStringCodCDNT(subStringFormada, buscarOffset));
                cargarDatos(hechosList.getFirst());
                event.consume();
            } catch (NoSuchElementException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("No se ha encontrado ningún elemento");
                alert.setTitle("Elemento no encontrado");
                alert.showAndWait();
                if (subStringFormada.length() <= 1) {
                    this.subStringFormada = "";
                    this.isSearch = false;
                    System.out.println("Im here");
                } else {
                    this.subStringFormada = this.subStringFormada.substring(0, subStringFormada.length() - 1);
                    offsetBusquedaMaximo = ServiceLocator.getHechosService().countfetchBySubStringCodCDNT(subStringFormada);
                    hechosList.addAll(this.obtenerHechosBySubStringCodCDNT(subStringFormada, buscarOffset));
                    cargarDatos(hechosList.getFirst());
                    System.out.println("Busque otros hechos");
                }
            }
        } else {
            try {
                this.subStringFormada = this.subStringFormada.substring(0, subStringFormada.length() - 1);
                offsetBusquedaMaximo = ServiceLocator.getHechosService().countfetchBySubStringCodCDNT(subStringFormada);
                if (subStringFormada.length() != 0) {
                    hechosList.clear();
                    hechosList.addAll(this.obtenerHechosBySubStringCodCDNT(subStringFormada, buscarOffset));
                    cargarDatos(hechosList.getFirst());
                    event.consume();
                } else {
                    this.subStringFormada = "";
                    hechosList.clear();
                    hechosList.addAll(this.hechosCopyOriginalList);
                    cargarDatos(hechosList.getFirst());
                    this.isSearch = false;
                }
            } catch (StringIndexOutOfBoundsException e) {
                this.isSearch = false;
                hechosList.clear();
                hechosList.addAll(this.hechosCopyOriginalList);
                cargarDatos(hechosList.getFirst());
            }
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
        this.hechoActual = ServiceLocator.getHechosService().getHecho(hechos.getIdReg());

        this.tipo.setText(hechos.getTipoHecho());
        this.codCDNT.setText(hechos.getCodCDNT());
        this.unidadOrganizativa.setText(hechos.getUnidadOrganizativa());
        this.municipio.setText(hechos.getMunicipio());

        this.fecha.setText(DateUtil.format(hechos.getFechaOcurrencia().toLocalDate()));
        this.afectacionServicios.setText(hechos.getServiciosAfect().toString());
        this.afectacionMLC.setText(hechos.getAfectacionMLC().toString());
        this.afectacionMN.setText(hechos.getAfectacionMN().toString());
        if (ServiceLocator.getTipoHechoService().searchTipoHechoByName(
                hechos.getTipoHecho()).getId_tipo_hecho() == 1) {
            this.valorChangerByInfractionType.setText(hechoActual.getMateriales().getMateriales());
        } else {
            this.valorChangerByInfractionType.setText(hechos.getAfectacion());
        }
        this.denuncia.setText(hechos.getNumDenuncia());
        this.sintesis.setText(hechos.getTitulo() + ". " + hechoActual.getLugar());

        //Cargar boolean data
        this.chargeBooleanDataToUI(hechoActual);
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

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Desea actualizar este hecho");
        alert.setTitle("Confirmación");
        Optional<ButtonType> confirmacion = alert.showAndWait();

        if (confirmacion.get().equals(ButtonType.OK)) {
            ServiceLocator.
                    getHechosService().
                    esclarecimientoHechoPExtTpubl(hechoActual.getId_reg(),
                            sinDenunciaData, expSACData, esclarecidoData, articulo83Data,
                            articulo82Data, medidasAdministrativasData, pendienteAJuicioData,
                            expedienteFasePreparatoriaData, menorEdadData, pendienteDespachoData,
                            privacionLibertadData, cantSancionadosData, sentenciaData);
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

    private List<HechosEsclarecimiento> obtenerHechosPextTpub(int offset){
        List<HechosEsclarecimiento> hechos = new LinkedList<>();
        String query = "SELECT id_reg, cod_cdnt, fecha_ocurrencia, tipo_hecho, afectacion_mn, afectacion_servicio, " +
                "unidad_organizativa, municipio, numero_denuncia, afectacion_usd, afect_tpublica, titulo " +
                "From hechos " +
                "JOIN tipo_hechos ON tipo_hechos.id_tipo_hecho = hechos.id_tipo_hecho " +
                "JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg " +
                "JOIN municipios ON municipios.id_municipio = hechos.id_municipio " +
                "JOIN tipo_vandalismo ON tipo_vandalismo.id_afect_tpublica = hechos.id_afectacion_telefonia_publica " +
                "WHERE (hechos.id_tipo_hecho = 1 or hechos.id_tipo_hecho =2) "+
                "ORDER BY fecha_ocurrencia DESC LIMIT 30 OFFSET "+ offset;
        try {
            ResultSet rs = Util.executeQuery(query);
            hechos = getDataFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hechos;
    }

    private List<HechosEsclarecimiento> obtenerHechosBySubStringCodCDNT(String codCdnt, int offset){
        List<HechosEsclarecimiento> hechos = new LinkedList<>();
        String query = "SELECT id_reg, cod_cdnt, fecha_ocurrencia, tipo_hecho, afectacion_mn, afectacion_servicio, " +
                "unidad_organizativa, municipio, numero_denuncia, afectacion_usd, afect_tpublica, titulo " +
                "From hechos " +
                "JOIN tipo_hechos ON tipo_hechos.id_tipo_hecho = hechos.id_tipo_hecho " +
                "JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg " +
                "JOIN municipios ON municipios.id_municipio = hechos.id_municipio " +
                "JOIN tipo_vandalismo ON tipo_vandalismo.id_afect_tpublica = hechos.id_afectacion_telefonia_publica " +
                "WHERE (hechos.id_tipo_hecho = 1 or hechos.id_tipo_hecho =2) " +
                "and cod_cdnt LIKE '%" + codCdnt + "%' ORDER BY fecha_ocurrencia DESC LIMIT 10 OFFSET " + offset;

        try {
            ResultSet rs = Util.executeQuery(query);
            hechos = getDataFromResultSet(rs);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return hechos;
    }

    private List<HechosEsclarecimiento> getDataFromResultSet(ResultSet rs) throws SQLException{
        List<HechosEsclarecimiento> hechos = new LinkedList<>();

        while(rs.next()){
            hechos.add(new HechosEsclarecimiento(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getDate(3),
                    rs.getString(4),
                    rs.getDouble(5),
                    rs.getDouble(6),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9),
                    rs.getDouble(10),
                    rs.getString(11),
                    rs.getString(12)
            ));
        }

        return hechos;
    }
}
