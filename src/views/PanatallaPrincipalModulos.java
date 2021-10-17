package views;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.ExceptionDialog;
import posiciones_agentes.views.MainPosicionesAgentesController;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginUrl;
import seguridad.views.UserRegisterController;
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
    private ImageView etecsaImg;

    @FXML
    private BorderPane pane;

    private MainApp mainApp;

    private Stage primaryStage;

    private UserLoggedIn logged;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
            this.etecsaImg.setOnMouseClicked(event ->
                    this.handleUsers()
            );
    }

    public void userLoggedInfo(UserLoggedIn userlin){
        this.lblNombre.setText(userlin.getNombre());
        this.lblUsername.setText(userlin.getUsername());
        this.lblRol.setText(userlin.getRol());

        logged = userlin;

        if(userlin.hasPermiso_pases()){
            System.out.println("Especialista");
        }
        else if(userlin.hasPermiso_visualizacion()){
            System.out.println("Jefe");
            this.bottonPosicionesAgentes.setDisable(true);
            this.bottonHechosExtraordinarios.setDisable(true);
        }
        else if(userlin.hasPermiso_todo()){
            System.out.println("Admin");
        }
        else if(userlin.isSuperuser()){
            System.out.println("SuperUser");
        }
    }

    private void setEmptyTextOnUserInfo(){
        this.lblNombre.setText("");
        this.lblUsername.setText("");
        this.lblRol.setText("");
    }

    private void handleUsers(){
        if(logged.isSuperuser()) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(LoginUrl.class.getResource("UserRegister.fxml"));
                AnchorPane pane = loader.load();
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Administración de usuarios y roles");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.setResizable(false);
                dialogStage.initOwner(this.primaryStage);
                Scene scene = new Scene(pane);
                dialogStage.setScene(scene);

                UserRegisterController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                dialogStage.showAndWait();

            } catch (IOException e) {
                ExceptionDialog dialog = new ExceptionDialog(e);
                dialog.showAndWait();
            }
        }
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
