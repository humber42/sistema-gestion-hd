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
import models.TipoVandalismo;
import org.controlsfx.dialog.ExceptionDialog;
import services.ServiceLocator;
import util.Util;
import views.dialogs.TipoDeAfectacionDialogController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TipoAfectacionViewController {

    private MainApp mainApp;

    @FXML
    private TableView<TipoVandalismo> afectacionTable;
    @FXML
    private TableColumn<TipoVandalismo, String> numeroAfectacionColum;
    @FXML
    private TableColumn<TipoVandalismo, String> afectacionColum;
    @FXML
    private JFXButton buttonEliminar;
    @FXML
    private JFXButton buttonEditar;

    private TipoVandalismo tipoVandalismo;

    @FXML
    private JFXTextField afectacionField;

    public TipoAfectacionViewController() {
    }

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        afectacionField.setDisable(true);
        buttonEditar.setDisable(true);
        buttonEliminar.setDisable(true);
        cargarTabla();
    }

    public void converToEditable(){
        if (tipoVandalismo!=null)
            afectacionField.setEditable(true);
    }

    private void cargarTabla(){
        List<TipoVandalismo> list = ServiceLocator.getTipoVandalismoService().fetchAll();

        // Initialize the person table with the two columns.
        //nomeroAveriaColum.setCellValueFactory(cellData -> cellData.getValue().id_avpextProperty();
        ObservableList<TipoVandalismo> observableList = FXCollections.observableList(list);
        afectacionTable.setItems(observableList);
        numeroAfectacionColum.setCellValueFactory(
                cellData->
                        new SimpleStringProperty(
                                String.valueOf( cellData.getValue().getId_afect_tpublica())
                        )
        );
        afectacionColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getAfect_tpublica()));

        // Clear person details.
        showAfectacionDetails(null);

        afectacionTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    showAfectacionDetails(newValue);
                    this.tipoVandalismo = newValue;
                    afectacionField.setDisable(false);
                    buttonEliminar.setDisable(false);
                    buttonEditar.setDisable(false);
                });
    }

    private void showAfectacionDetails(TipoVandalismo tipoVandalismo){
        if (tipoVandalismo != null){
            afectacionField.setText(tipoVandalismo.getAfect_tpublica());
        }
        else afectacionField.setText("");
    }

    public boolean showDialogToEdit(){
        try {
            if (afectacionField.getText().isEmpty()){
                Util.dialogResult("Campo vacio", Alert.AlertType.ERROR);
                afectacionField.setText(tipoVandalismo.getAfect_tpublica());
                afectacionField.setFocusColor(Paint.valueOf("red"));
                afectacionField.setUnFocusColor(Paint.valueOf("red"));
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmacion");
                alert.setHeaderText(null);
                alert.setContentText("Desea editar este tipo de afectacion");
                Optional<ButtonType> confirmacion = alert.showAndWait();
                if (confirmacion.get().equals(ButtonType.OK)){
                    tipoVandalismo.setAfect_tpublica(afectacionField.getText());
                    ServiceLocator.getTipoVandalismoService().editarTipoDeVandalismo(tipoVandalismo);
                    cargarTabla();
                    desactivarButtons();
                    afectacionField.setFocusColor(Paint.valueOf("blue"));
                    afectacionField.setUnFocusColor(Paint.valueOf("white"));
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
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TipoDeAveriaViewController.class.getResource("../views/dialogs/TipoDeAfectacionDialog.fxml"));
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Insertar tipo de afectacion");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.stage);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene(anchorPane));

            TipoDeAfectacionDialogController controller = loader.getController();
            controller.setEdition(false);
            controller.setStage(stage);
            controller.setTipoVandalismo(null);
            controller.getOkButton().setText("Insertar");
            controller.getOkButton().setStyle("-fx-background-color: green");
            controller.getVariableLabel().setText("Insertar tipo de afectacion");
            stage.showAndWait();
            cargarTabla();
            desactivarButtons();
        }catch (IOException e){
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
        return true;
    }

    public void deleteTipoAfectacion(){
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Confirmacion");
            alert.setContentText("Desea eliminar este tipo de afectacion");
            Optional<ButtonType> confirmacion = alert.showAndWait();
            if (confirmacion.get().equals(ButtonType.OK)){
                ServiceLocator.getTipoVandalismoService().eliminarTipoDeVandalismo(tipoVandalismo);
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
