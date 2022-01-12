package views;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.AveriasPext;
import org.controlsfx.dialog.ExceptionDialog;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginViewController;
import services.ServiceLocator;
import util.Util;
import views.dialogs.TipoDeAveriaDialogController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class TipoDeAveriaViewController {


    public List<AveriasPext> list = ServiceLocator.getAveriasPExtService().fecthAllAveriaPext();

    private Stage mainApp;

    @FXML
    private TableView<AveriasPext> averiasTable;
    @FXML
    private TableColumn<AveriasPext, String> numeroAveriaColum;
    @FXML
    private TableColumn<AveriasPext, String> causaColum;
    @FXML
    private JFXButton buttonEliminar;
    @FXML
    private JFXButton buttonEditar;
    @FXML
    private JFXButton btnNuevo;

    private AveriasPext averiasPext;

    @FXML
    private JFXTextField causaField;

    private UserLoggedIn logged;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TipoDeAveriaViewController() {
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
            this.buttonEditar.setVisible(false);
            this.buttonEliminar.setVisible(false);
        }

        causaField.setDisable(true);
        buttonEditar.setDisable(true);
        buttonEliminar.setDisable(true);
        cargarTabla();
    }

    public void converToEditable(){
        if (averiasPext!=null)
            causaField.setEditable(true);
    }

    private void cargarTabla(){
        List<AveriasPext> list = ServiceLocator.getAveriasPExtService().fecthAllAveriaPext();
        // Initialize the person table with the two columns.
        //nomeroAveriaColum.setCellValueFactory(cellData -> cellData.getValue().id_avpextProperty();
        ObservableList<AveriasPext> observableList = FXCollections.observableList(list);
        averiasTable.setItems(observableList);
        numeroAveriaColum.setCellValueFactory(
                cellData->
                        new SimpleStringProperty(
                                String.valueOf( cellData.getValue().getId_avpext())
                        )
        );
        causaColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getCausa()));

        // Clear person details.
        showAveriaDetails(null);

        averiasTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    showAveriaDetails(newValue);
                    this.averiasPext=newValue;
                    causaField.setDisable(false);
                    buttonEditar.setDisable(false);
                    buttonEliminar.setDisable(false);
                });
    }

    private void showAveriaDetails(AveriasPext averiasPext){
        if (averiasPext != null){
            causaField.setText(averiasPext.getCausa());
        }
        else causaField.setText("");
    }

    public boolean showDialogToEdit(){
        try {
            if (causaField.getText().isEmpty()){
                Util.dialogResult("Campo vacio", Alert.AlertType.ERROR);
                causaField.setText(averiasPext.getCausa());
                causaField.setFocusColor(Paint.valueOf("red"));
                causaField.setUnFocusColor(Paint.valueOf("red"));
            }
            else {
                Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmacion");
                alert.setHeaderText(null);
                alert.setContentText("Desea editar este tipo de averia");
                Optional<ButtonType> confirmacion = alert.showAndWait();
                if (confirmacion.get().equals(ButtonType.OK)){
                    averiasPext.setCausa(causaField.getText());
                    ServiceLocator.getAveriasPExtService().editarTipoAveria(averiasPext);
                    cargarTabla();
                    desactivarButtons();
                    causaField.setFocusColor(Paint.valueOf("blue"));
                    causaField.setUnFocusColor(Paint.valueOf("white"));
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
            loader.setLocation(TipoDeAveriaDialogController.class.getResource("TipoDeAveriaDialog.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Insertar Tipo de Averia");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.stage);
//            stage.initOwner(this.mainApp.getPrimaryStage());
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene(pane));

            TipoDeAveriaDialogController controller = loader.getController();
            controller.setEdition(false);
            controller.setStage(stage);
            controller.setAveriasPext(null);
            controller.getOkButton().setText("Insertar");
            controller.getOkButton().setStyle("-fx-background-color: green");
            controller.getVariableLabel().setText("Insertar tipo de averia");
            stage.showAndWait();
            cargarTabla();
            desactivarButtons();
        }catch (IOException e){
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }

        return true;
    }

    public void deleteTipoAveria(){
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Desea eliminar este tipo de averia");
            alert.setTitle("Confirmación");
            Optional<ButtonType> confirmacion = alert.showAndWait();
            if (confirmacion.get().equals(ButtonType.OK)){
                ServiceLocator.getAveriasPExtService().eliminarTipoAveria(averiasPext);
                cargarTabla();
                desactivarButtons();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setMainApp(Stage mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //personTable.setItems(mainApp.getPersonData());
    }
}

