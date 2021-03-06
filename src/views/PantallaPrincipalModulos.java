package views;

import com.jfoenix.controls.JFXButton;
import database_manage.DataBaseManagementController;
import icons.ImageLocation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.dialog.ExceptionDialog;
import posiciones_agentes.views.MainPosicionesAgentesController;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginUrl;
import seguridad.views.LoginViewController;
import seguridad.views.UserRegisterViewController;
import sistema_identificativo.views.MainSistemaIdentificativoController;

import java.io.IOException;
import java.net.URISyntaxException;

public class PantallaPrincipalModulos {

    @FXML
    private JFXButton bottonHechosExtraordinarios;
    @FXML
    private JFXButton bottonSistemaIdentificativo;
    @FXML
    private JFXButton bottonPosicionesAgentes;
    @FXML
    private JFXButton bottonSalvaBD;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblRol;
    @FXML
    private ImageView etecsaImg;
    @FXML
    private JFXButton btnSalva;

    private static double xOffset = 0;


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

    private static double yOffset = 0;
    @FXML
    private Pane topPane;

    @FXML
    private void initialize() {
        this.logged = LoginViewController.getUserLoggedIn();
        userLoggedInfo();
        this.etecsaImg.setOnMouseClicked(event ->
                this.handleUsers()
        );

        this.topPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        this.topPane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }

    private void userLoggedInfo(){
        this.lblNombre.setText(lblNombre.getText() + " " + logged.getNombre());
       // this.lblUsername.setText(logged.getUsername());
       // this.lblRol.setText(logged.getRol());

        if (logged.hasPermiso_pases()) {
            this.bottonPosicionesAgentes.setDisable(true);
            this.bottonHechosExtraordinarios.setDisable(true);
            this.bottonSistemaIdentificativo.setDisable(false);
            this.btnSalva.setDisable(true);
            this.settingSistemaIdentificativo();
        }
        else if(logged.hasPermiso_visualizacion()){
            this.bottonPosicionesAgentes.setDisable(false);
            this.bottonHechosExtraordinarios.setDisable(false);
            this.bottonSistemaIdentificativo.setDisable(false);
            this.btnSalva.setDisable(true);
            this.settingHechosExtraordinarios();
        }
        else if(logged.isSuperuser()){
            this.bottonPosicionesAgentes.setDisable(false);
            this.bottonHechosExtraordinarios.setDisable(false);
            this.bottonSistemaIdentificativo.setDisable(false);
            this.btnSalva.setDisable(false);
            this.settingHechosExtraordinarios();
        }
    }

    private void setEmptyTextOnUserInfo(){
        this.lblNombre.setText("");
       // this.lblUsername.setText("");
       // this.lblRol.setText("");
    }

    private void handleUsers(){
        if(logged.isSuperuser()) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(LoginUrl.class.getResource("UserRegisterView.fxml"));
                AnchorPane pane = loader.load();
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Administraci??n de usuarios y roles");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                try {
                    dialogStage.getIcons().add(new Image(ImageLocation.class.getResource("icon_app.png").toURI().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                dialogStage.setResizable(false);
                dialogStage.initOwner(this.primaryStage);
                Scene scene = new Scene(pane);
                dialogStage.setScene(scene);

                UserRegisterViewController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                dialogStage.showAndWait();

            } catch (IOException e) {
                ExceptionDialog dialog = new ExceptionDialog(e);
                dialog.showAndWait();
            }
        }
    }

    @FXML
    private void close(){
        Platform.exit();
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
            loader.setLocation(PantallaPrincipalModulos.class.getResource("PrinicipalView.fxml"));
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
            loader.setLocation(PantallaPrincipalModulos.class.getResource("/sistema_identificativo/views/MainSistemaIdentificativoView.fxml"));
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
            loader.setLocation(PantallaPrincipalModulos.class.getResource("/posiciones_agentes/views/MainPosicionesAgentesView.fxml"));
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
    private void manejar_database() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DataBaseManagementController.class.getResource("DataBaseManagement.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Base de datos");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            DataBaseManagementController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void minimize(){
        this.primaryStage.setIconified(true);
    }
}
