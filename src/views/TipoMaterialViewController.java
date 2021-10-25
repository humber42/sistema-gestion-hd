package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.TipoMateriales;
import org.controlsfx.dialog.ExceptionDialog;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginViewController;
import services.ServiceLocator;
import util.Util;
import views.dialogs.TipoMaterialDialogController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

//TODO: Validacion de editar
public class TipoMaterialViewController {

    private MainApp mainApp;

    @FXML
    private TableView<TipoMateriales> materialesTable;
    @FXML
    private TableColumn<TipoMateriales, String> numeroMaterialdColum;
    @FXML
    private TableColumn<TipoMateriales, String> materialesColum;
    @FXML
    private JFXButton buttonEliminar;
    @FXML
    private JFXButton buttonEditar;
    @FXML
    private JFXButton btnNuevo;

    private TipoMateriales tipoMateriales;

    @FXML
    private JFXTextField materialesLabel;

    private UserLoggedIn logged;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TipoMaterialViewController() {
    }

    @FXML
    private void initialize() {
        this.logged = LoginViewController.getUserLoggedIn();

        if(logged.isSuperuser()){
            this.btnNuevo.setVisible(true);
            this.buttonEditar.setVisible(true);
            this.buttonEliminar.setVisible(true);
        } else if(logged.hasPermiso_pases() || logged.hasPermiso_visualizacion()){
            this.btnNuevo.setVisible(false);
            this.buttonEliminar.setVisible(false);
            this.buttonEditar.setVisible(false);
        }

        materialesLabel.setDisable(true);
        buttonEditar.setDisable(true);
        buttonEliminar.setDisable(true);
        // Initialize the person table with the two columns.
        //nomeroAveriaColum.setCellValueFactory(cellData -> cellData.getValue().id_avpextProperty();
        cargarTable();

    }
    public void converToEditable(){
        if (tipoMateriales!=null){
            materialesLabel.setEditable(true);
        }
    }

    private void cargarTable(){
        List<TipoMateriales> list = ServiceLocator.getTipoMaterialesService().fetchAll();
        ObservableList<TipoMateriales> observableList = FXCollections.observableList(list);
        materialesTable.setItems(observableList);
        numeroMaterialdColum.setCellValueFactory(
                cellData->
                        new SimpleStringProperty(
                                String.valueOf( cellData.getValue().getId_materiales())
                        )
        );
        materialesColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getMateriales()));

        // Clear person details.
        showTipoMaterialesDetails(null);

        materialesTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->{
                    showTipoMaterialesDetails(newValue);
                    this.tipoMateriales = newValue;
                    buttonEliminar.setDisable(false);
                    buttonEditar.setDisable(false);
                    materialesLabel.setDisable(false);
                });


    }

    private void showTipoMaterialesDetails(TipoMateriales tipoMateriales){
        if (tipoMateriales != null){
            materialesLabel.setText(tipoMateriales.getMateriales());
        }
        else materialesLabel.setText("");
    }

    public boolean showDialogToEdit(){
        try {
            if (materialesLabel.getText().isEmpty()){
                Util.dialogResult("Campo vacio", Alert.AlertType.ERROR);
                materialesLabel.setText(tipoMateriales.getMateriales());
                materialesLabel.setFocusColor(Paint.valueOf("red"));
                materialesLabel.setUnFocusColor(Paint.valueOf("red"));
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setContentText("Desea editar el tipo de material");
                alert.setTitle("Confirmación");
                Optional<ButtonType> confirmacion = alert.showAndWait();
                if (confirmacion.get().equals(ButtonType.OK)){
                    tipoMateriales.setMateriales(materialesLabel.getText());
                    ServiceLocator.getTipoMaterialesService().editarTipoMaterial(tipoMateriales);
                    cargarTable();
                    desactivarButtons();
                    materialesLabel.setFocusColor(Paint.valueOf("blue"));
                    materialesLabel.setUnFocusColor(Paint.valueOf("white"));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
        }

    private void desactivarButtons(){
        buttonEditar.setDisable(true);
        buttonEliminar.setDisable(true);
    }

    public boolean showNewDialog(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TipoMaterialViewController.class.getResource("../views/dialogs/TipoMaterialDialog.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Insertar Tipo de Material");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.stage);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene(pane));

            TipoMaterialDialogController controller = loader.getController();
            controller.setEdition(false);
            controller.setDialogStage(stage);
            controller.setTipoMateriales(null);
            controller.getOkButton().setText("Insertar");
            controller.getOkButton().setStyle("-fx-background-color: green");
            controller.getVariableLabel().setText("Insertar Material");
            stage.showAndWait();
            cargarTable();
            desactivarButtons();
        }catch (IOException e){
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }

        return true;
    }

    public void eliminarTipoMaterial(){
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText(null);
            alert.setContentText("Desea eliminar este tipo de material");
            Optional<ButtonType> confirmacion = alert.showAndWait();
            if (confirmacion.get().equals(ButtonType.OK)){
                ServiceLocator.getTipoMaterialesService().eliminarTipoMaterial(tipoMateriales);
                cargarTable();
                desactivarButtons();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        // averiasTable.setItems(mainApp.);
    }
}
