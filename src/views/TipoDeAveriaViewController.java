package views;

import controllers.MainApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.AveriasPext;
import services.ServiceLocator;

import java.util.List;


public class TipoDeAveriaViewController {

    public List<AveriasPext> list = ServiceLocator.getAveriasPExtService().fecthAllAveriaPext();
    private MainApp mainApp;
    @FXML
    private TableView<AveriasPext> averiasTable;
    @FXML
    private TableColumn<AveriasPext, String> nomeroAveriaColum;
    @FXML
    private TableColumn<AveriasPext, String> causaColum;

    @FXML
    private Label causaLabel;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TipoDeAveriaViewController() {
    }

    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        //nomeroAveriaColum.setCellValueFactory(cellData -> cellData.getValue().id_avpextProperty();
        ObservableList<AveriasPext> observableList = FXCollections.observableList(list);
        averiasTable.setItems(observableList);
        nomeroAveriaColum.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                String.valueOf(cellData.getValue().getId_avpext())
                        )
        );
        causaColum.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCausa()));

        // Clear person details.
        showAveriaDetails(null);

        averiasTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showAveriaDetails(newValue));


    }

    private void showAveriaDetails(AveriasPext averiasPext) {
        if (averiasPext != null) {
            causaLabel.setText(averiasPext.getCausa());
        } else causaLabel.setText("");
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //personTable.setItems(mainApp.getPersonData());
    }
}

