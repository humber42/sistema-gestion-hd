package views;


import controllers.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.UnidadOrganizativa;

public class UnidadesOrgViewController {

    private MainApp mainApp;

    @FXML
    private TableView<UnidadOrganizativa> unidadTable;
    @FXML
    private TableColumn<UnidadOrganizativa, Integer> nomeroUnidadColum;
    @FXML
    private TableColumn<UnidadOrganizativa, String> unidadColum;

    @FXML
    private Label unidadLabel;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        // averiasTable.setItems(mainApp.);
    }

}
