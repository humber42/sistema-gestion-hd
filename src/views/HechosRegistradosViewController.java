package views;

import controllers.MainApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Hechos;
import services.ServiceLocator;

import java.util.List;

public class HechosRegistradosViewController {

    private MainApp mainApp;
    public List<Hechos> list = ServiceLocator.getHechosService().fetchAllHechos("10","10");

    @FXML
    private TableView<Hechos> hechosTable;
    @FXML
    private TableColumn<Hechos, String> numeroHechosColum;
    @FXML
    private TableColumn<Hechos, String> hechosColum;
    @FXML
    private TableColumn<Hechos,String> municipioColum;
    @FXML
    private TableColumn<Hechos,String> ocurrenciaColum;

    @FXML
    private Label hechoLabel;
    @FXML
    private Label municipioLabel;
    @FXML
    private Label ocurreLabel;
    @FXML
    private Label tipoHechoLabel;
    @FXML
    private Label centroLabel;

    public HechosRegistradosViewController() {
    }

    @FXML
    private void initialize() {
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
        hechosColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getTitulo()));
        municipioColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getMunicipio().getMunicipio()));
        ocurrenciaColum.setCellValueFactory(cellData->
                new SimpleStringProperty(cellData.getValue().getFecha_ocurrencia().toString()));

        // Clear person details.
        showHechoDetails(null);

        hechosTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showHechoDetails(newValue));


    }

    private void showHechoDetails(Hechos hechos){
        if (hechos != null){
            hechoLabel.setText(hechos.getTitulo());
            municipioLabel.setText(hechos.getMunicipio().getMunicipio());
            ocurreLabel.setText(hechos.getFecha_ocurrencia().toString());
            tipoHechoLabel.setText(hechos.getTipoHecho().getTipo_hecho());
            centroLabel.setText(hechos.getCentro());
        }
        else {
            hechoLabel.setText("");
            municipioLabel.setText("");
            ocurreLabel.setText("");
            tipoHechoLabel.setText("");
            centroLabel.setText("");
        }

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //personTable.setItems(mainApp.getPersonData());
    }
}
