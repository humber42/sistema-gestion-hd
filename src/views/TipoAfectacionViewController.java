package views;

import controllers.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.TipoVandalismo;

public class TipoAfectacionViewController {

    private MainApp mainApp;

    @FXML
    private TableView<TipoVandalismo> afectacionTable;
    @FXML
    private TableColumn<TipoVandalismo, Integer> nomeroAfectacionColum;
    @FXML
    private TableColumn<TipoVandalismo, String> afectacionColum;

    @FXML
    private Label afectacionLabel;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        // averiasTable.setItems(mainApp.);
    }
}
