package posiciones_agentes.views;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import posiciones_agentes.models.PosicionAgente;
import posiciones_agentes.models.ProveedorServicio;
import posiciones_agentes.models.RegistroPosicionesAgentes;
import services.ServiceLocator;
import util.Util;

import java.util.stream.Collectors;

public class RegistrarPosicionesAgentesController {

    @FXML
    private ComboBox<String> unidadOrgananizativa;
    @FXML
    private ComboBox<String> proveedoresServicio;
    @FXML
    private Spinner<Integer> horasDL;
    @FXML
    private Spinner<Integer> horasNL;
    @FXML
    private Spinner<Integer> cantEfectivo;
    @FXML
    private TextArea instalacion;
    @FXML
    private Label title;
    @FXML
    private JFXButton btnRegistrar;

    private Stage dialogStage;

    private boolean toUpdate;

    private RegistroPosicionesAgentes posicionToUpdate;

    ObservableList<PosicionAgente> instalacionesAgentes;

    public void setInstalacionesAgentes(ObservableList<PosicionAgente> instalaciones) {
        this.instalacionesAgentes = instalaciones;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void initialize() {
        this.title.setText("Registrar Posición de Agente");
        this.proveedoresServicio.getItems().setAll(
                ServiceLocator.getProveedorServicioService()
                        .getAllProveedorServicio()
                        .stream()
                        .map(ProveedorServicio::getProveedorServicio)
                        .collect(Collectors.toList())
        );

        this.unidadOrgananizativa.getItems().setAll(
                ServiceLocator.getUnidadOrganizativaService()
                        .fetchAll()
                        .stream()
                        .map(UnidadOrganizativa::getUnidad_organizativa)
                        .collect(Collectors.toList())
        );
        TextFields.bindAutoCompletion(this.unidadOrgananizativa.getEditor(), this.unidadOrgananizativa.getItems());
        this.horasNL.setEditable(false);
        this.horasDL.setEditable(false);
        this.cantEfectivo.setEditable(false);
        this.horasDL.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, 1));
        this.horasNL.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, 1));
        this.cantEfectivo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 1));

        this.posicionToUpdate = GestionPosicionesAgentesController.getInstalacionSelected();
        if (posicionToUpdate != null)
            this.toUpdate = true;
        else
            this.toUpdate = false;

        if (toUpdate) {
            this.title.setText("Editar Posición de Agente");
            this.btnRegistrar.setText("Editar");
            this.btnRegistrar.setStyle("-fx-background-color: orange");
            this.proveedoresServicio.setDisable(true);
            this.unidadOrgananizativa.setDisable(true);
            this.instalacion.setEditable(false);
            loadInfoPosicionAgenteToUpdate();
        }
    }

    private void loadInfoPosicionAgenteToUpdate() {
        if (this.posicionToUpdate != null) {
            this.unidadOrgananizativa.getSelectionModel().select(posicionToUpdate.getUnidadOrganizativa().getUnidad_organizativa());
            this.proveedoresServicio.getSelectionModel().select(posicionToUpdate.getProveedorServicio().getProveedorServicio());
            this.horasDL.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, posicionToUpdate.getHorasDiasLaborables(), 1));
            this.horasNL.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, posicionToUpdate.getHorasDiasNoLaborables(), 1));
            this.cantEfectivo.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, posicionToUpdate.getCantidadEfectivos(), 1)
            );
            this.instalacion.setText(posicionToUpdate.getInstalacion());
        }
    }

    @FXML
    private void handleClose() {
        this.toUpdate = false;
        this.posicionToUpdate = null;
        this.instalacionesAgentes.clear();
        this.instalacionesAgentes.setAll(
                ServiceLocator.getRegistroPosicionesAgentesService().getAll()
        );
        this.dialogStage.close();
    }

    private boolean emptyFields() {
        boolean emptyFields = false;
        try {
            if (this.unidadOrgananizativa.getValue().isEmpty()) {
                emptyFields = true;
            } else if (this.proveedoresServicio.getValue().isEmpty()) {
                emptyFields = true;
            } else if (this.cantEfectivo.getValue() == null) {
                emptyFields = true;
            } else if (this.horasDL.getValue() == null) {
                emptyFields = true;
            } else if (this.horasNL.getValue() == null) {
                emptyFields = true;
            } else if (this.instalacion.getText().isEmpty()) {
                emptyFields = true;
            }
        } catch (NullPointerException e) {
            emptyFields = true;
        }
        return emptyFields;
    }

    @FXML
    private void registrar() {
        if (!emptyFields()) {
            if (!toUpdate) {
                UnidadOrganizativa unidadOrganizativa = ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName(this.unidadOrgananizativa.getValue());
                ProveedorServicio proveedorServicio = ServiceLocator.getProveedorServicioService().getByName(this.proveedoresServicio.getValue());
                RegistroPosicionesAgentes registroPosicionesAgentes = new RegistroPosicionesAgentes(
                        0,
                        this.instalacion.getText()
                        , unidadOrganizativa,
                        proveedorServicio,
                        this.horasDL.getValue(),
                        this.horasNL.getValue(),
                        this.cantEfectivo.getValue());
                registroPosicionesAgentes.register();

//            unidadOrgananizativa.getSelectionModel().clearSelection();
//            proveedoresServicio.getSelectionModel().clearSelection();
//            horasDL.getEditor().setText("0");
//            horasNL.getEditor().setText("0");
//            cantEfectivo.getEditor().setText("0");
//            instalacion.clear();
                Util.dialogResult("Se ha registrado correctamente", Alert.AlertType.INFORMATION);
                //dialogStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "¿Está seguro que desea editar esta Posición de Agente?",
                        ButtonType.OK, ButtonType.CANCEL);
                alert.initOwner(this.dialogStage);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    RegistroPosicionesAgentes toUpdate =
                            new RegistroPosicionesAgentes();
                    toUpdate.setIdReg(posicionToUpdate.getIdReg());
                    toUpdate.setUnidadOrganizativa(
                            ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName(
                                    unidadOrgananizativa.getSelectionModel().getSelectedItem())
                            );
                    toUpdate.setProveedorServicio(
                            ServiceLocator.getProveedorServicioService().getByName(
                                    proveedoresServicio.getSelectionModel().getSelectedItem())
                            );
                    toUpdate.setInstalacion(instalacion.getText());
                    toUpdate.setCantidadEfectivos(cantEfectivo.getValue());
                    toUpdate.setHorasDiasLaborables(horasDL.getValue());
                    toUpdate.setHorasDiasNoLaborables(horasNL.getValue());
                    ServiceLocator.getRegistroPosicionesAgentesService().
                            updateRegisterPosicionesAgentes(toUpdate);
                    Util.dialogResult("Modificación realizada con éxito.", Alert.AlertType.INFORMATION);
                }
            }
        } else {
            Util.dialogResult("Hay campos vacíos", Alert.AlertType.ERROR);
        }
    }
}
