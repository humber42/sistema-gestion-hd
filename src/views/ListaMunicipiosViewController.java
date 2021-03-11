package views;

import controllers.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Municipio;

public class ListaMunicipiosViewController {
    private MainApp mainApp;

    @FXML
    private TableView<Municipio> municipioTable;
    @FXML
    private TableColumn<Municipio, Integer> nomeroMunicipioColum;
    @FXML
    private TableColumn<Municipio, String> municipioColum;

    @FXML
    private Label municipioLabel;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        // averiasTable.setItems(mainApp.);
    }
}


