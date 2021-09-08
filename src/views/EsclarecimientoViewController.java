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
import org.controlsfx.dialog.ExceptionDialog;
import org.postgresql.util.PSQLException;
import services.ServiceLocator;
import util.DateUtil;

import java.util.LinkedList;
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
    private LinkedList<Hechos> hechosCopyOriginalList;
    private LinkedList<Hechos> hechosList;
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
                    hechosList.addAll(ServiceLocator.getHechosService().fetchBySubStringCodCDNT(subStringFormada, String.valueOf(buscarOffset)));
                    try {
                        cargarDatos(hechosList.getFirst());
                    } catch (NoSuchElementException exc) {
                        dialogElemento("ultimo");
                    }
                } else {
                    dialogElemento("ultimo");
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
                    try {
                        hechosList.addAll(ServiceLocator.getHechosService().fetchHechosPextTpub(Integer.toString(offset)));
                    } catch (PSQLException exc) {
                        ExceptionDialog dialog = new ExceptionDialog(e);
                        dialog.showAndWait();
                    }
                    try {
                        cargarDatos(hechosList.getFirst());
                    } catch (NoSuchElementException exc) {
                        dialogElemento("ultimo");
                    }
                } else {
                    dialogElemento("ultimo");
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
            try {
                hechosList.addAll(ServiceLocator.getHechosService().fetchHechosPextTpub(Integer.toString(offset)));
            } catch (PSQLException e) {
                ExceptionDialog dialog = new ExceptionDialog(e);
                dialog.showAndWait();
            }
            cargarDatos(hechosList.getLast());
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
                    hechosList.addAll(ServiceLocator.getHechosService().fetchBySubStringCodCDNT(subStringFormada, String.valueOf(buscarOffset)));
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
                        hechosList.addAll(ServiceLocator.getHechosService().fetchHechosPextTpub(Integer.toString(offset)));
                    } catch (PSQLException exc) {
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
                hechosList.addAll(ServiceLocator.getHechosService().fetchHechosPextTpub(Integer.toString(offset)));
            } catch (PSQLException e) {
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
            this.hechosList = ServiceLocator.getHechosService().fetchHechosPextTpub("0");
        } catch (PSQLException e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }

        this.cargarDatos(hechosList.getFirst());




        /*Clonando la lista original para hacer la carga mucho mas rapida luego que se elimino
            all en la busqueda
         */
        try {
            this.hechosCopyOriginalList = (LinkedList<Hechos>) hechosList.clone();
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
                hechosList.addAll(ServiceLocator.getHechosService().fetchBySubStringCodCDNT(subStringFormada, String.valueOf(buscarOffset)));
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
                    hechosList.addAll(ServiceLocator.getHechosService().fetchBySubStringCodCDNT(subStringFormada, String.valueOf(buscarOffset)));
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
                    hechosList.addAll(ServiceLocator.getHechosService().fetchBySubStringCodCDNT(subStringFormada, String.valueOf(buscarOffset)));
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
    private void cargarDatos(Hechos hechos) {
        this.hechoActual = hechos;

        this.tipo.setText(hechos.getTipoHecho().getTipo_hecho());
        this.codCDNT.setText(hechos.getCod_cdnt());
        this.unidadOrganizativa.setText(hechos.getUnidadOrganizativa().getUnidad_organizativa());
        this.municipio.setText(hechos.getMunicipio().getMunicipio());

        this.fecha.setText(DateUtil.format(hechos.getFecha_ocurrencia().toLocalDate()));
        this.afectacionServicios.setText(hechos.getAfectacion_servicio().toString());
        this.afectacionMLC.setText(hechos.getAfectacion_usd().toString());
        this.afectacionMN.setText(hechos.getAfectacion_mn().toString());
        if (hechos.getTipoHecho().getId_tipo_hecho() == 1) {
            this.valorChangerByInfractionType.setText(hechos.getMateriales().getMateriales());
        } else {
            this.valorChangerByInfractionType.setText(hechos.getTipoVandalismo().getAfect_tpublica());
        }
        this.denuncia.setText(hechos.getNumero_denuncia());
        this.sintesis.setText(hechos.getTitulo() + ". " + hechos.getLugar());

        //Cargar boolean data
        this.chargeBooleanDataToUI(hechos);
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

        this.buttonBox.setLayoutX(468);
        this.buttonBox.setLayoutY(576);
    }

    /**
     * Method to change the screen size when esclarecido is unselected
     */
    private void toNormalScreen() {
        this.dialogStage.setWidth(650);
        this.dialogStage.setHeight(538);
        this.panelPrincipal.setPrefWidth(631);
        this.panelPrincipal.setPrefHeight(507);

        this.estadoHecho.setPrefWidth(631);
        this.estadoHecho.setPrefHeight(121);
        this.estadoHecho.parentToLocal(0, 0);

        this.tipoEsclarecimiento.setPrefWidth(599);
        this.tipoEsclarecimiento.setPrefHeight(30);
        this.tipoEsclarecimiento.parentToLocal(0, 0);

        this.buttonBox.setLayoutX(468);
        this.buttonBox.setLayoutY(458);
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
        alert.setContentText("Udsted se encuentra en el " + estado + " elemento");
        alert.showAndWait();
    }


}
