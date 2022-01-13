package posiciones_agentes.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import posiciones_agentes.models.ProveedorServicio;
import services.ServiceLocator;
import util.Util;

import java.util.stream.Collectors;

public class GestionProveedoresController {
    @FXML
    private JFXListView<String> providersList;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXTextField txtProvider;
    @FXML
    private ImageView imgAccept;

    private String providerSelected;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize(){
        this.disableBtns();

        this.txtProvider.setPromptText("Nuevo proveedor");

        this.initializeList();

        this.providersList.setOnMouseClicked(event ->
                this.setProviderOnTxt()
        );

        this.txtProvider.setOnKeyTyped(event ->
                {
                    if(this.txtProvider.getText().length() > 0 && providerSelected == null)
                        this.showIcon();
                    else if (txtProvider.getText().length() == 0){
                        this.hideIcon();
                        this.disableBtns();
                        this.providersList.getSelectionModel().clearSelection();
                        this.providerSelected = null;
                    }
                }
        );

        this.imgAccept.setOnMouseClicked(event ->
                this.handleNew()
        );
    }

    private void initializeList(){
        this.providersList.getItems().clear();
        this.providersList.getItems().setAll(
                ServiceLocator.getProveedorServicioService()
                        .getAllProveedorServicio()
                        .stream()
                        .map(ProveedorServicio::getProveedorServicio)
                        .collect(Collectors.toList())
        );
        this.providersList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.providersList.getSelectionModel().clearSelection();
    }

    private void setProviderOnTxt(){
        providerSelected = this.providersList.getSelectionModel().getSelectedItem();
        if(providerSelected != null) {
            this.txtProvider.setText(providerSelected);
            this.txtProvider.setPromptText("Editar proveedor");
            this.hideIcon();
            this.enableBtns();
        }
    }

    private void showIcon(){
        imgAccept.setVisible(true);
        imgAccept.setDisable(false);
    }

    private void hideIcon(){
        imgAccept.setVisible(false);
        imgAccept.setDisable(true);
    }

    private void enableBtns(){
        this.btnDelete.setDisable(false);
        this.btnEdit.setDisable(false);
    }

    private void disableBtns(){
        this.btnDelete.setDisable(true);
        this.btnEdit.setDisable(true);
    }

    @FXML
    private void handleDelete(){
        if (providerSelected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Está seguro que desea eliminar el elemento seleccionado?",
                    ButtonType.OK, ButtonType.CANCEL);
            alert.initOwner(this.dialogStage);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                int idProvider = ServiceLocator.getProveedorServicioService()
                        .getByName(providerSelected).getId();
                if(idProvider > 0){
                    ServiceLocator.getProveedorServicioService().
                            deleteProveedorServicioById(idProvider);
                    Util.dialogResult("Eliminación exitosa.", Alert.AlertType.INFORMATION);
                    this.providerSelected = null;
                    this.initializeList();
                    this.disableBtns();
                    this.txtProvider.setText(null);
                    this.txtProvider.setPromptText("Nuevo proveedor");
                }
            }
        }
    }

    @FXML
    private void handleEdit(){
        if (providerSelected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Está seguro que desea editar el elemento seleccionado?",
                    ButtonType.OK, ButtonType.CANCEL);
            alert.initOwner(this.dialogStage);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                ProveedorServicio prov = ServiceLocator.getProveedorServicioService()
                        .getByName(providerSelected);
                if(prov != null){
                    ProveedorServicio update = new ProveedorServicio();
                    update.setId(prov.getId());
                    update.setProveedorServicio(txtProvider.getText());
                    ServiceLocator.getProveedorServicioService().updateProveedorServicio(update);
                    Util.dialogResult("Modificación exitosa.", Alert.AlertType.INFORMATION);
                    this.providerSelected = null;
                    this.initializeList();
                    this.disableBtns();
                    this.txtProvider.setText(null);
                    this.txtProvider.setPromptText("Nuevo proveedor");
                }
                else {
                    Util.dialogResult("Ha ocurrido un error. No se ha encontrado el proveedor.",
                            Alert.AlertType.ERROR);
                }
            }
        }
    }

    private void handleNew(){
        ProveedorServicio provNew = new ProveedorServicio();
        provNew.setProveedorServicio(this.txtProvider.getText());
        if(ServiceLocator.getProveedorServicioService().getByName(provNew.getProveedorServicio()) != null)
            Util.dialogResult("El proveedor " + provNew.getProveedorServicio() + " ya está registrado.",
                    Alert.AlertType.INFORMATION);
        else{
            ServiceLocator.getProveedorServicioService().registerProveedorServicio(provNew);
            this.hideIcon();
            Util.dialogResult("Registro exitoso.", Alert.AlertType.INFORMATION);
            this.initializeList();
            this.disableBtns();
            this.txtProvider.setText(null);
        }
    }
}
