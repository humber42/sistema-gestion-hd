package views;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import posiciones_agentes.views.MainPosicionesAgentesController;
import seguridad.models.UserLoggedIn;
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
    private Label lblNombre;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblRol;

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

    public void userLoggedInfo(UserLoggedIn userlin){
        this.lblNombre.setText(userlin.getNombre());
        this.lblUsername.setText(userlin.getUsername());
        this.lblRol.setText(userlin.getRol());
    }

    private void setEmptyTextOnUserInfo(){
        this.lblNombre.setText("");
        this.lblUsername.setText("");
        this.lblRol.setText("");
    }

    @FXML
    private void logout(){
        setEmptyTextOnUserInfo();
        this.primaryStage.close();
        this.mainApp.start(new Stage());
    }

    @FXML
    private void settingHechosExtraordinarios() {
        try {
            //Load principal view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PanatallaPrincipalModulos.class.getResource("PrinicipalView.fxml"));
            BorderPane panelHechos = loader.load();

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
            loader.setLocation(PanatallaPrincipalModulos.class.getResource("/sistema_identificativo/views/MainSistemaIdentificativoView.fxml"));
            BorderPane panelSistemaIdentificativo = loader.load();

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
            loader.setLocation(PanatallaPrincipalModulos.class.getResource("/posiciones_agentes/views/MainPosicionesAgentesView.fxml"));
            BorderPane panelPosicionesAgentes = loader.load();

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
