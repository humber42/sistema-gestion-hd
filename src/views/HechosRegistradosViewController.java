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
import org.controlsfx.dialog.ExceptionDialog;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
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
    @FXML
    private TextField offsetMaximoField;
    @FXML
    private TextField offsetField;

    private Hechos hechos;
    private Stage stage;


    private LinkedList<Hechos> hechosList;
    private int offsetMaximo;
    private int offset;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public HechosRegistradosViewController() {
    }

    @FXML
    private void initialize() {
        buttonEditar.setDisable(true);
        buttonEliminar.setDisable(true);
        municipioLabel.setDisable(true);
        tipoHechoLabel.setDisable(true);
        hechoField.setDisable(true);
        ocurreDate.setDisable(true);
        parteDate.setDisable(true);
        centroField.setDisable(true);
        lugarField.setDisable(true);
        cargarTabla(ServiceLocator.getHechosService().fetchAllHechos2("15","0"));
        offset = 0;
        offsetMaximo = ServiceLocator.getHechosService().countAllHechos();


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
                    municipioLabel.setDisable(false);
                    tipoHechoLabel.setDisable(false);
                });
        offsetMaximoField.setEditable(false);
        ponerNumeroDeTablas();
        ponerOffsetDetablaActual();
    }

    public void ponerOffsetDetablaActual(){
        int resto = offset%15;
        int count = offset/15;
        if (resto!= 0){
            offsetField.setText(Integer.toString(count+1));
        }else{
            offsetField.setText(Integer.toString(count));
        }
    }
    public void ponerNumeroDeTablas(){
        int resto = offsetMaximo%15;
        int count = offsetMaximo/15;
        if (resto!=0){
            offsetMaximoField.setText(Integer.toString(count+1));
        }else {
            offsetMaximoField.setText(Integer.toString(count));
        }
    }
    public void buscarTabla(){
        try {


            int busqueda = Integer.parseInt(offsetField.getText());
            offset = busqueda * 15;
            if (offset > offsetMaximo || offset < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Entrada Incorrecta");
                alert.setContentText("Por favor, introduzca valores entre 0 y " + offsetMaximoField.getText());
                alert.showAndWait();
            } else
                cargarTabla(ServiceLocator.getHechosService().fetchAllHechos2("15", Integer.toString(offset)));
        }
        catch (NumberFormatException e){
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("Por favor, solo se aceptan números entre 0 y "+ offsetMaximoField.getText());
            alert1.setHeaderText(null);
            alert1.setTitle("Entrada Incorrecta");
            alert1.showAndWait();
        }
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
                Util.dialogResult("Campo vacío", Alert.AlertType.ERROR);
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

                desactivarButton();
                hechoField.setFocusColor(Paint.valueOf("blue"));
                hechoField.setUnFocusColor(Paint.valueOf("white"));
                centroField.setFocusColor(Paint.valueOf("blue"));
                centroField.setUnFocusColor(Paint.valueOf("white"));
                lugarField.setFocusColor(Paint.valueOf("blue"));
                lugarField.setUnFocusColor(Paint.valueOf("white"));
            }
            cargarTabla(ServiceLocator.getHechosService().fetchAllHechos2("15",Integer.toString(offset)));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    private void desactivarButton(){
        buttonEliminar.setDisable(true);
        buttonEditar.setDisable(true);
        municipioLabel.setDisable(true);
        tipoHechoLabel.setDisable(true);
        hechoField.setDisable(true);
        ocurreDate.setDisable(true);
        parteDate.setDisable(true);
        centroField.setDisable(true);
        lugarField.setDisable(true);
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
            handleFirst();
            desactivarButton();
            ponerNumeroDeTablas();

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

                desactivarButton();
            }
            cargarTabla(ServiceLocator.getHechosService().fetchAllHechos2("15",Integer.toString(offset)));
            ponerNumeroDeTablas();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleNext(){
        offset +=15;
        int howmuch = offsetMaximo-offset;

        try {
            if (howmuch>0) {
                cargarTabla(ServiceLocator.getHechosService().fetchAllHechos2("15",Integer.toString(offset)));
            } else {
                dialogElemento("último");
                offset -=15;
            }
            desactivarButton();
            showHechoDetails(null);
        } catch (IndexOutOfBoundsException e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
        ponerOffsetDetablaActual();
    }

    @FXML
    public void handlerPrevious(){
        int howmuch = offsetMaximo - offset;
        offset -=15;
        try {
            if (howmuch==0){
                cargarTabla(ServiceLocator.getHechosService().fetchAllHechos2("15",Integer.toString(offsetMaximo-30)));
                offset -=15;

            }
            else{
                if (offset >= 0){
                    cargarTabla(ServiceLocator.getHechosService().fetchAllHechos2("15",Integer.toString(offset)));
                } else {
                    offset += 15;
                    dialogElemento("primer");
                }
            }
            showHechoDetails(null);
            desactivarButton();
        } catch (IndexOutOfBoundsException e){
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
        ponerOffsetDetablaActual();
    }

    @FXML
    private void handleFirst() {
        cargarTabla(ServiceLocator.getHechosService().fetchAllHechos2("15","0"));
        offset = 0;
        showHechoDetails(null);
        desactivarButton();
        ponerOffsetDetablaActual();
    }

    @FXML
    private void handleLast(){
        cargarTabla(ServiceLocator.getHechosService().fetchAllHechos2("15",Integer.toString(offsetMaximo-15)));
        offset = offsetMaximo;

        // Clear person details.
        showHechoDetails(null);
        desactivarButton();
        ponerOffsetDetablaActual();
    }



    private void dialogElemento(String estado) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información");
        alert.setContentText("Usted se encuentra en el " + estado + " elemento");
        alert.showAndWait();
    }


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //personTable.setItems(mainApp.getPersonData());
    }

    private void cargarTabla(List<Hechos> hechos){
        ObservableList<Hechos> observableList = FXCollections.observableList(hechos);
        hechosTable.setItems(observableList);
        numeroHechosColum.setCellValueFactory(
                cellData->
                        new SimpleStringProperty(
                                String.valueOf( cellData.getValue().getId_reg())
                        )
        );
        codCDNTColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getCod_cdnt()==null?"(no info)":cellData.getValue().getCod_cdnt().toUpperCase()));
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
    }
}
