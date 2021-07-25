package sistema_identificativo.views;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainSistemaIdentificativoController {

    private Stage mainApp;
    private BorderPane panelSistemaIdentificativo;
    private BorderPane panelGrande;

    public void setPanelGrande(BorderPane panelGrande) {
        this.panelGrande = panelGrande;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleCerrar() {
        this.panelGrande.setCenter(null);
    }

    public void setMainApp(Stage mainApp) {
        this.mainApp = mainApp;
    }

    public void setPanelSistemaIdentificativo(BorderPane pane) {
        this.panelSistemaIdentificativo = pane;
    }


}
