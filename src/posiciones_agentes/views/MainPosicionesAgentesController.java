package posiciones_agentes.views;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainPosicionesAgentesController {
    private Stage mainApp;
    private BorderPane panelPosicionesAgentes;
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

    public void setPanelPosicionesAgentes(BorderPane pane) {
        this.panelPosicionesAgentes = pane;
    }
}
