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
import models.Hechos;
import org.apache.log4j.helpers.Loader;
import org.controlsfx.dialog.ExceptionDialog;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class HechosRegistradosViewController {
    private MainApp mainApp;
    public ObservableList sintesis;

    @FXML
    private TableView<Hechos> hechosTable;
    @FXML
    private TableColumn<Hechos, String> numeroHechosColum;
    @FXML
    private TableColumn<Hechos, String> codCDNTColum;
    @FXML
    private TableColumn<Hechos, String> uniOrgColum;
    @FXML
    private TableColumn<Hechos, String> tipoColum;
    @FXML
    private TableColumn<Hechos,String> municipioColum;
    @FXML
    private TableColumn<Hechos,String> ocurrenciaColum;
    @FXML
    private TableColumn<Hechos,String > sintesisColum;

    @FXML
    private JFXTextField hechoField;
    @FXML
    private Label municipioLabel;
    @FXML
    private DatePicker ocurreDate;
    @FXML
    private DatePicker parteDate;
    @FXML
    private Label tipoHechoLabel;
    @FXML
    private JFXTextField centroField;
    @FXML
    private JFXTextField lugarField;
    @FXML
    private JFXButton buttonEliminar;
    @FXML
    private JFXButton buttonEditar;

    private Hechos hechos;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public HechosRegistradosViewController() {
    }

    @FXML
    private void initialize() {
        buttonEditar.setDisable(true);
        buttonEliminar.setDisable(true);
        hechoField.setDisable(true);
        ocurreDate.setDisable(true);
        parteDate.setDisable(true);
        centroField.setDisable(true);
        lugarField.setDisable(true);
        cargarTabla();
    }

    public void converToEditable(){
        if (hechos!=null){
            hechoField.setEditable(true);
            ocurreDate.setEditable(true);
            parteDate.setEditable(true);
            centroField.setEditable(true);
            lugarField.setEditable(true);
        }

    }

    private void cargarTabla(){
        List<Hechos> list = ServiceLocator.getHechosService().fetchAllHechos("15","15");
        sintesis= null;
        // Initialize the person table with the two columns.
        //nomeroAveriaColum.setCellValueFactory(cellData -> cellData.getValue().id_avpextProperty();
        ObservableList<Hechos> observableList = FXCollections.observableList(list);
        hechosTable.setItems(observableList);
        numeroHechosColum.setCellValueFactory(
                cellData->
                        new SimpleStringProperty(
                                String.valueOf( cellData.getValue().getId_reg())
                        )
        );
        codCDNTColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getCod_cdnt().toUpperCase()));
        uniOrgColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getUnidadOrganizativa().getUnidad_organizativa()));
        tipoColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getTipoHecho().getTipo_hecho()));
        municipioColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getMunicipio().getMunicipio()));
        ocurrenciaColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getFecha_ocurrencia().toString()));
        sintesisColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getSintesis()));

        // Clear person details.
        showHechoDetails(null);

        hechosTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    showHechoDetails(newValue);
                    this.hechos = newValue;
                    buttonEditar.setDisable(false);
                    buttonEliminar.setDisable(false);
                    hechoField.setDisable(false);
                    ocurreDate.setDisable(false);
                    parteDate.setDisable(false);
                    centroField.setDisable(false);
                    lugarField.setDisable(false);
                });
    }

    private void showHechoDetails(Hechos hechos){
        if (hechos != null){
            hechoField.setText(hechos.getTitulo());
            municipioLabel.setText(hechos.getMunicipio().getMunicipio());
            ocurreDate.getEditor().setText(hechos.getFecha_ocurrencia().toString());
            parteDate.getEditor().setText(hechos.getFecha_parte().toString());
            tipoHechoLabel.setText(hechos.getTipoHecho().getTipo_hecho());
            centroField.setText(hechos.getCentro());
            lugarField.setText(hechos.getLugar());
        }
        else {
            hechoField.setText("");
            municipioLabel.setText("");
            ocurreDate.setDayCellFactory(null);
            parteDate.setDayCellFactory(null);
            tipoHechoLabel.setText("");
            centroField.setText("");
            lugarField.setText("");
        }

    }

    public boolean showDialogToEdit(){
        try {
            if (hechoField.getText().isEmpty() && centroField.getText().isEmpty() && lugarField.getText().isEmpty() ){
                Util.dialogResult("Campo vacio", Alert.AlertType.ERROR);
                hechoField.setText(hechos.getTitulo());
                hechoField.setUnFocusColor(Paint.valueOf("red"));
                hechoField.setFocusColor(Paint.valueOf("red"));
                centroField.setText(hechos.getCentro());
                centroField.setUnFocusColor(Paint.valueOf("red"));
                centroField.setFocusColor(Paint.valueOf("red"));
                lugarField.setText(hechos.getLugar());
                lugarField.setUnFocusColor(Paint.valueOf("red"));
                lugarField.setFocusColor(Paint.valueOf("red"));
            }
            else if (hechoField.getText().isEmpty() && centroField.getText().isEmpty()){
                hechoField.setText(hechos.getTitulo());
                hechoField.setUnFocusColor(Paint.valueOf("red"));
                hechoField.setFocusColor(Paint.valueOf("red"));
                centroField.setText(hechos.getCentro());
                centroField.setUnFocusColor(Paint.valueOf("red"));
                centroField.setFocusColor(Paint.valueOf("red"));
            }
            else if (hechoField.getText().isEmpty() && lugarField.getText().isEmpty()){
                hechoField.setText(hechos.getTitulo());
                hechoField.setUnFocusColor(Paint.valueOf("red"));
                hechoField.setFocusColor(Paint.valueOf("red"));
                lugarField.setText(hechos.getLugar());
                lugarField.setUnFocusColor(Paint.valueOf("red"));
                lugarField.setFocusColor(Paint.valueOf("red"));
            }
            else if (centroField.getText().isEmpty() && lugarField.getText().isEmpty()){
                centroField.setText(hechos.getCentro());
                centroField.setUnFocusColor(Paint.valueOf("red"));
                centroField.setFocusColor(Paint.valueOf("red"));
                lugarField.setText(hechos.getLugar());
                lugarField.setUnFocusColor(Paint.valueOf("red"));
                lugarField.setFocusColor(Paint.valueOf("red"));
            }
            else if (hechoField.getText().isEmpty()){
                hechoField.setText(hechos.getTitulo());
                hechoField.setUnFocusColor(Paint.valueOf("red"));
                hechoField.setFocusColor(Paint.valueOf("red"));
            }
            else if (centroField.getText().isEmpty()){
                centroField.setText(hechos.getCentro());
                centroField.setUnFocusColor(Paint.valueOf("red"));
                centroField.setFocusColor(Paint.valueOf("red"));
            }
            else if (lugarField.getText().isEmpty()){
                lugarField.setText(hechos.getLugar());
                lugarField.setUnFocusColor(Paint.valueOf("red"));
                lugarField.setFocusColor(Paint.valueOf("red"));
            }
            else {
                Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmación");
                alert.setHeaderText(null);
                alert.setContentText("Desea editar este hecho");
                Optional<ButtonType> confirmacion = alert.showAndWait();
                hechos.setTitulo(hechoField.getText());
                hechos.setCentro(centroField.getText());
                hechos.setLugar(lugarField.getText());
                ServiceLocator.getHechosService().editarHechos(hechos);
                cargarTabla();
                desactivarButton();
                hechoField.setFocusColor(Paint.valueOf("blue"));
                hechoField.setUnFocusColor(Paint.valueOf("white"));
                centroField.setFocusColor(Paint.valueOf("blue"));
                centroField.setUnFocusColor(Paint.valueOf("white"));
                lugarField.setFocusColor(Paint.valueOf("blue"));
                lugarField.setUnFocusColor(Paint.valueOf("white"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    private void desactivarButton(){
        buttonEliminar.setDisable(true);
        buttonEditar.setDisable(true);
    }

    public void showNewDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HechosRegistradosViewController.class.getResource("../views/RegistrarView.fxml"));
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registrar Hecho");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.stage);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene(anchorPane));

            RegistrarViewController controller = loader.getController();
            controller.setDialogStage(stage);
            controller.setFromPrincipal(false);
            stage.showAndWait();
            cargarTabla();
        }catch (IOException e){
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }



    public void deleteHechos(){
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText(null);
            alert.setContentText("Desea eliminar este hecho");
            Optional<ButtonType> confirmacion = alert.showAndWait();
            if (confirmacion.get().equals(ButtonType.OK)){
                ServiceLocator.getHechosService().eliminarHechos(hechos);
                cargarTabla();
                desactivarButton();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //personTable.setItems(mainApp.getPersonData());
    }
}
