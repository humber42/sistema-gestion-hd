package views;

import com.jfoenix.controls.JFXButton;
import controllers.MainApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import posiciones_agentes.views.MainPosicionesAgentesController;
import sistema_identificativo.views.MainSistemaIdentificativoController;

import java.io.IOException;

public class PanatallaPrincipalModulos {

    @FXML
    private JFXButton bottonHechosExtraordinarios;
    @FXML
    private JFXButton bottonSistemaIdentificativo;
    @FXML
    private JFXButton bottonPosicionesAgentes;

    @FXML
    private BorderPane pane;

    private MainApp mainApp;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void settingHechosExtraordinarios() {
        try {
            //Load principal view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PanatallaPrincipalModulos.class.getResource("../views/PrinicipalView.fxml"));
            BorderPane panelHechos = (BorderPane) loader.load();

            PrincipalViewController controller = loader.getController();
            controller.setMainApp(primaryStage);
            pane.setCenter(panelHechos);
            controller.setPanelPrincipal(panelHechos);
            controller.setPanelGrande(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void settingSistemaIdentificativo() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PanatallaPrincipalModulos.class.getResource("../sistema_identificativo/views/MainSistemaIdentificativoView.fxml"));
            BorderPane panelSistemaIdentificativo = (BorderPane) loader.load();

            MainSistemaIdentificativoController controller = loader.getController();
            controller.setMainApp(primaryStage);
            pane.setCenter(panelSistemaIdentificativo);
            controller.setPanelSistemaIdentificativo(panelSistemaIdentificativo);
            controller.setPanelGrande(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void settingsPosicionesAgentes(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PanatallaPrincipalModulos.class.getResource("../posiciones_agentes/views/MainPosicionesAgentesView.fxml"));
            BorderPane panelPosicionesAgentes = (BorderPane) loader.load();

            MainPosicionesAgentesController controller = loader.getController();
            controller.setMainApp(primaryStage);
            pane.setCenter(panelPosicionesAgentes);
            controller.setPanelPosicionesAgentes(panelPosicionesAgentes);
            controller.setPanelGrande(pane);
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    @FXML
    private void closeSystem() {
        Platform.exit();
    }
}
