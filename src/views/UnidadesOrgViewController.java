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
import models.UnidadOrganizativa;
import org.controlsfx.dialog.ExceptionDialog;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginViewController;
import services.ServiceLocator;
import util.Util;
import views.dialogs.UnidadOrgEditDialogController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UnidadesOrgViewController {

    private MainApp mainApp;
    @FXML
    private TableView<UnidadOrganizativa> unidadTable;
    @FXML
    private TableColumn<UnidadOrganizativa, String> numeroUnidadColum;
    @FXML
    private TableColumn<UnidadOrganizativa, String> unidadColum;
    @FXML
    private JFXButton buttonEliminar;
    @FXML
    private JFXButton buttonEditar;
    @FXML
    private JFXButton btnNuevo;

    private UnidadOrganizativa uo;

    @FXML
    private JFXTextField unidadLabel;

    private UserLoggedIn logged;

    public UnidadesOrgViewController() {
    }

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        this.logged = LoginViewController.getUserLoggedIn();

        if(logged.isSuperuser()){
            this.btnNuevo.setVisible(true);
            this.buttonEditar.setVisible(true);
            this.buttonEliminar.setVisible(true);
        } else if(logged.hasPermiso_pases() || logged.hasPermiso_visualizacion()){
            this.buttonEditar.setVisible(false);
            this.btnNuevo.setVisible(false);
            this.buttonEliminar.setVisible(false);
        }

        unidadLabel.setDisable(true);
        buttonEditar.setDisable(true);
        buttonEliminar.setDisable(true);
        // Initialize the person table with the two columns.
        //nomeroAveriaColum.setCellValueFactory(cellData -> cellData.getValue().id_avpextProperty();
       cargarTabla();
    }

    public void converToEditable(){
        if (uo!=null){
            unidadLabel.setEditable(true);
        }
    }

    private void cargarTabla(){
        List<UnidadOrganizativa> list = ServiceLocator.getUnidadOrganizativaService().fetchAll();
        ObservableList<UnidadOrganizativa> observableList = FXCollections.observableList(list);
        unidadTable.setItems(observableList);
        numeroUnidadColum.setCellValueFactory(
                cellData->
                        new SimpleStringProperty(
                                String.valueOf( cellData.getValue().getId_unidad_organizativa())
                        )
        );
        unidadColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getUnidad_organizativa()));

        // Clear person details.
        showUnidadDetails(null);

        unidadTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->{
                    showUnidadDetails(newValue);
                    this.uo = newValue;
                    buttonEliminar.setDisable(false);
                    buttonEditar.setDisable(false);
                    unidadLabel.setDisable(false);
                });

    }

    private void showUnidadDetails(UnidadOrganizativa unidadOrganizativa){
        if (unidadOrganizativa != null){
            unidadLabel.setText(unidadOrganizativa.getUnidad_organizativa());
        }
        else unidadLabel.setText("");
    }

    public boolean showDialogToEdit(){
//        try{
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(UnidadesOrgViewController.class.getResource("../views/dialog/UnidadOrgEditDialog.fxml"));
//            AnchorPane pane = loader.load();
//
//            Stage stage = new Stage();
//            stage.setTitle("Editar Unidad Organizativa");
//            stage.initModality(Modality.WINDOW_MODAL);
//            stage.initOwner(this.mainApp.getPrimaryStage());
//            stage.setResizable(false);
//            stage.setMaximized(false);
//            stage.setScene(new Scene(pane));
//
//            UnidadOrgEditDialogController controller = loader.getController();
//            controller.setEdition(true);
//            controller.setDialogStage(stage);
//            controller.setUnidadOrganizativa(uo);
//            controller.getOkButton().setText("Editar");
//            controller.getVariableLAbel().setText("Editar U/O");
//            stage.showAndWait();
//        }catch (IOException e){
//            ExceptionDialog exceptionDialog = new ExceptionDialog(e);
//            exceptionDialog.showAndWait();
//        }
        try {

            if(unidadLabel.getText().isEmpty()){
                Util.dialogResult("Campo nombre vacio", Alert.AlertType.ERROR);
                unidadLabel.setText(uo.getUnidad_organizativa());
                unidadLabel.setFocusColor(Paint.valueOf("red"));
                unidadLabel.setUnFocusColor(Paint.valueOf("red"));
            }
            else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setContentText("Desea editar esta unidad");
                alert.setTitle("Confirmación");
                Optional<ButtonType> confirmacion = alert.showAndWait();
                if (confirmacion.get().equals(ButtonType.OK)){
                    uo.setUnidad_organizativa(unidadLabel.getText());
                    ServiceLocator.getUnidadOrganizativaService().editarUnidadOrganizativa(uo);
                    cargarTabla();
                    desactivarButtons();
                    unidadLabel.setFocusColor(Paint.valueOf("blue"));
                    unidadLabel.setUnFocusColor(Paint.valueOf("white"));
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
            loader.setLocation(UnidadesOrgViewController.class.getResource("../views/dialogs/UnidadOrgEditDialog.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Insertar Unidad Organizativa");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.stage);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene(pane));

            UnidadOrgEditDialogController controller = loader.getController();
            controller.setEdition(false);
            controller.setDialogStage(stage);
            controller.setUnidadOrganizativa(null);
            controller.getOkButton().setText("Insertar");
            controller.getOkButton().setStyle("-fx-background-color: green");
            controller.getVariableLAbel().setText("Insertar U/O");
            stage.showAndWait();
            cargarTabla();
            desactivarButtons();
        }catch (IOException e){
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }

        return true;
    }

    public void deleteUnidad(){
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Desea eliminar esta unidad");
            alert.setTitle("Confirmación");
            Optional<ButtonType> confirmacion = alert.showAndWait();
            if(confirmacion.get().equals(ButtonType.OK)){
                ServiceLocator.getUnidadOrganizativaService().eliminarUnidadOrganizativa(uo);
                cargarTabla();
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
