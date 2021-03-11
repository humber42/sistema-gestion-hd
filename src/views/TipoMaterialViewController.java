package views;

import controllers.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.TipoMateriales;

public class TipoMaterialViewController {

    private MainApp mainApp;

    @FXML
    private TableView<TipoMateriales> materialesTable;
    @FXML
    private TableColumn<TipoMateriales, Integer> nomeroMaterialdColum;
    @FXML
    private TableColumn<TipoMateriales, String> materialesColum;

    @FXML
    private Label materialesLabel;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        // averiasTable.setItems(mainApp.);
    }
}
