package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.MainApp;
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
import models.Municipio;
import org.controlsfx.dialog.ExceptionDialog;
import services.ServiceLocator;
import util.Util;
import views.dialogs.ListarMunicipiosDialogController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ListaMunicipiosViewController {
    private MainApp mainApp;

    @FXML
    private TableView<Municipio> municipioTable;
    @FXML
    private TableColumn<Municipio, String> numeroMunicipioColum;
    @FXML
    private TableColumn<Municipio, String> municipioColum;
    @FXML
    private JFXButton buttonEliminar;
    @FXML
    private JFXButton buttonEditar;

    private Municipio municipio;

    @FXML
    private JFXTextField municipioField;

    public ListaMunicipiosViewController() {
    }

    @FXML
    private void initialize() {
        municipioField.setDisable(true);
        buttonEditar.setDisable(true);
        buttonEliminar.setDisable(true);

        cargarTabla();

    }

    public void converToEditable(){
        if (municipio!=null)
            municipioField.setEditable(true);
    }

    private void cargarTabla(){
        List<Municipio> list = ServiceLocator.getMunicipiosService().fetchAll();
        // Initialize the person table with the two columns.
        //nomeroAveriaColum.setCellValueFactory(cellData -> cellData.getValue().id_avpextProperty();
        ObservableList<Municipio> observableList = FXCollections.observableList(list);
        municipioTable.setItems(observableList);
        numeroMunicipioColum.setCellValueFactory(
                cellData->
                        new SimpleStringProperty(
                                String.valueOf( cellData.getValue().getId_municipio())
                        )
        );
        municipioColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getMunicipio()));

        // Clear person details.
        showMunicipioDetails(null);

        municipioTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    showMunicipioDetails(newValue);
                    this.municipio=newValue;
                    municipioField.setDisable(false);
                    buttonEditar.setDisable(false);
                    buttonEliminar.setDisable(false);
                });

    }
    private void showMunicipioDetails(Municipio municipio){
        if (municipio != null){
            municipioField.setText(municipio.getMunicipio());
        }
        else municipioField.setText("");
    }

    public boolean showDialogToEdit(){
        try {
            if (municipioField.getText().isEmpty()){
                Util.dialogResult("Campo vacio", Alert.AlertType.ERROR);
                municipioField.setText(municipio.getMunicipio());
                municipioField.setFocusColor(Paint.valueOf("red"));
                municipioField.setUnFocusColor(Paint.valueOf("red"));
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmacion");
                alert.setHeaderText(null);
                alert.setContentText("Desea editar el nombre del Municipio");
                Optional<ButtonType> confirmacion = alert.showAndWait();
                if (confirmacion.get().equals(ButtonType.OK)){
                    municipio.setMunicipio(municipioField.getText());
                    ServiceLocator.getMunicipiosService().editarMunicipio(municipio);
                    cargarTabla();
                    desactivarButtons();
                    municipioField.setFocusColor(Paint.valueOf("blue"));
                    municipioField.setUnFocusColor(Paint.valueOf("white"));
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
        try {
            FXMLLoader loader =new FXMLLoader();
            loader.setLocation(ListaMunicipiosViewController.class.getResource("../views/dialogs/ListarMunicipiosDialog.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Insertar Municipio");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.mainApp.getPrimaryStage());
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene(pane));

            ListarMunicipiosDialogController controller = loader.getController();
            controller.setEdition(false);
            controller.setStage(stage);
            controller.setMunicipio(null);
            controller.getOkButton().setText("Insertar");
            controller.getOkButton().setStyle("-fx-background-color: green");
            controller.getVariableLabel().setText("Insertar Municipio");
            stage.showAndWait();
            cargarTabla();
            desactivarButtons();

        }catch (IOException e){
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
        return true;
    }

    public void deleteMunicipio(){
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Desea eliminar este municipio");
            alert.setTitle("Confirmación");
            Optional<ButtonType> confirmacion = alert.showAndWait();
            if (confirmacion.get().equals(ButtonType.OK)){
                ServiceLocator.getMunicipiosService().eliminarMunicipio(municipio);
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


