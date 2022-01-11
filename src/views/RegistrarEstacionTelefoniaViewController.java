package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import models.EstacionPublicaCentroAgente;
import models.Municipio;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import services.ServiceLocator;
import util.Util;

import java.util.stream.Collectors;

public class RegistrarEstacionTelefoniaViewController {
    @FXML
    private ComboBox<String> cboxUorg;
    @FXML
    private ComboBox<String> cboxMunicipio;
    @FXML
    private JFXTextField txtCentros;
    @FXML
    private JFXTextField txtEstaciones;
    @FXML
    private JFXButton btnRegister;
    @FXML
    private Label lblCentros;
    @FXML
    private Label lblEstaciones;
    @FXML
    private Label lblTitle;

    private Stage dialogStage;

    private EstacionPublicaCentroAgente estacionToUpdate;

    ObservableList<EstacionPublicaCentroAgente> estacionPublicaCentroAgentes;

    private boolean toUpdate;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEstacionPublicaCentroAgentes(ObservableList<EstacionPublicaCentroAgente> estacionPublicaCentroAgentes) {
        this.estacionPublicaCentroAgentes = estacionPublicaCentroAgentes;
    }

    @FXML
    private void initialize() {
        this.lblTitle.setText("Registrar Telefonía Pública");
        this.txtCentros.setOnKeyTyped(event ->
                this.validarTXTsOnlyNumericValues(event, this.txtCentros, this.lblCentros));

        this.txtEstaciones.setOnKeyTyped(event ->
                this.validarTXTsOnlyNumericValues(event, this.txtEstaciones, this.lblEstaciones));

        this.cboxUorg.getItems().setAll(
                ServiceLocator.getUnidadOrganizativaService().fetchAll()
                        .stream()
                        .map(UnidadOrganizativa::getUnidad_organizativa)
                        .collect(Collectors.toList())
        );

        TextFields.bindAutoCompletion(this.cboxUorg.getEditor(), this.cboxUorg.getItems());

        this.cboxMunicipio.getItems().setAll(
                ServiceLocator.getMunicipiosService().fetchAll()
                        .stream()
                        .map(Municipio::getMunicipio)
                        .collect(Collectors.toList())
        );

        TextFields.bindAutoCompletion(this.cboxMunicipio.getEditor(), this.cboxMunicipio.getItems());

        this.estacionToUpdate = TelefoniaPublicaViewController.getEstacionPublicaCentroAgenteSelected();
        if (estacionToUpdate != null)
            this.toUpdate = true;
        else
            this.toUpdate = false;

        if (toUpdate) {
            this.lblTitle.setText("Editar Telefonía Pública");
            this.btnRegister.setText("Editar");
            this.btnRegister.setStyle("-fx-background-color: orange");
            loadInfoEstacionToUpdate();
        }
    }

    private void loadInfoEstacionToUpdate() {
        if (this.estacionToUpdate != null) {
            this.cboxUorg.getSelectionModel().select(estacionToUpdate.getUnidadOrganizativa());
            this.cboxMunicipio.getSelectionModel().select(estacionToUpdate.getMunicipio());
            this.txtCentros.setText(estacionToUpdate.getCentroAgente() + "");
            this.txtEstaciones.setText(estacionToUpdate.getEstacionPublica() + "");
        }
    }

    @FXML
    private void handleClose() {
        this.toUpdate = false;
        this.estacionToUpdate = null;
        this.estacionPublicaCentroAgentes.clear();
        this.estacionPublicaCentroAgentes.setAll(
                ServiceLocator.getEstacionPublicaCentroAgenteService().getAll()
        );
        this.dialogStage.close();
    }

    @FXML
    private void handleRegister() {
        if (validateFields()) {
            if (!toUpdate) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "¿Está seguro que desea registrar esta Telefonía Pública?",
                        ButtonType.OK, ButtonType.CANCEL);
                alert.initOwner(this.dialogStage);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    EstacionPublicaCentroAgente toInsert =
                            new EstacionPublicaCentroAgente();
                    toInsert.setUnidadOrganizativa(cboxUorg.getSelectionModel().getSelectedItem());
                    toInsert.setMunicipio(cboxMunicipio.getSelectionModel().getSelectedItem());
                    toInsert.setCentroAgente(Integer.parseInt(txtCentros.getText()));
                    toInsert.setEstacionPublica(Integer.parseInt(txtEstaciones.getText()));
                    ServiceLocator.getEstacionPublicaCentroAgenteService().
                            addEstacionPublicaCentroAgente(toInsert);
                    Util.dialogResult("Registro realizado con éxito.", Alert.AlertType.INFORMATION);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "¿Está seguro que desea editar esta Telefonía Pública?",
                        ButtonType.OK, ButtonType.CANCEL);
                alert.initOwner(this.dialogStage);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    EstacionPublicaCentroAgente toUpdate =
                            new EstacionPublicaCentroAgente();
                    toUpdate.setIdReg(estacionToUpdate.getIdReg());
                    toUpdate.setUnidadOrganizativa(cboxUorg.getSelectionModel().getSelectedItem());
                    toUpdate.setMunicipio(cboxMunicipio.getSelectionModel().getSelectedItem());
                    toUpdate.setCentroAgente(Integer.parseInt(txtCentros.getText()));
                    toUpdate.setEstacionPublica(Integer.parseInt(txtEstaciones.getText()));
                    ServiceLocator.getEstacionPublicaCentroAgenteService().
                            updateEstacionPublicaCentroAgente(toUpdate);
                    Util.dialogResult("Modificación realizada con éxito.", Alert.AlertType.INFORMATION);
                }
            }
        }
    }

    private void validarTXTsOnlyNumericValues(KeyEvent event, JFXTextField txt, Label lbl) {
        String valueTXT = txt.getText();

        if (valueTXT.isEmpty()) {
            Util.dialogResult("Campo " + lbl.getText() + " vacío.", Alert.AlertType.WARNING);
        } else if (valueTXT.toCharArray()[valueTXT.length() - 1] < '0'
                || valueTXT.toCharArray()[valueTXT.length() - 1] > '9') {
            event.consume();
            txt.setText(valueTXT.substring(0, valueTXT.length() - 1));
            txt.end();
            Util.dialogResult("El campo " + lbl.getText() + " solo admite valores numéricos", Alert.AlertType.INFORMATION);
        }
    }

    private boolean validateFields() {
        boolean valid = true;
        String message = "";

        if (this.cboxUorg.getSelectionModel().isEmpty() ||
                this.cboxUorg.getSelectionModel().getSelectedItem() == null) {
            valid = false;
            message += "- Unidad Organizativa no seleccionada.\n";
        }
        if (this.cboxMunicipio.getSelectionModel().isEmpty() ||
                this.cboxMunicipio.getSelectionModel().getSelectedItem() == null) {
            valid = false;
            message += "- Municipio no seleccionado.\n";
        }
        if (this.txtCentros.getText().isEmpty()) {
            valid = false;
            message += "- Centros Agentes vacío.\n";
        }
        if (this.txtEstaciones.getText().isEmpty()) {
            valid = false;
            message += "- Estaciones públicas vacío.";
        }

        if (!valid) {
            Alert alert = new Alert(Alert.AlertType.WARNING, message);
            alert.initOwner(this.dialogStage);
            alert.showAndWait();
        }

        return valid;
    }
}
