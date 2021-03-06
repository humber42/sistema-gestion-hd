package seguridad.views;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seguridad.models.User;
import seguridad.models.UserLoggedIn;
import seguridad.utils.SHA1Encrypt;
import services.ServiceLocator;
import util.Util;
import views.MainApp;
import views.PantallaPrincipalModulos;
import views.UrlLocation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


public class LoginViewController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private JFXButton btnLogin;
    private static double xOffset = 0;

    private Stage primaryStage;

    private MainApp mainApp;

    private static UserLoggedIn userLoggedIn = null;

    public static UserLoggedIn getUserLoggedIn() {
        if (userLoggedIn == null)
            userLoggedIn = new UserLoggedIn();
        return userLoggedIn;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private static double yOffset = 0;
    @FXML
    private Pane topPane;

    @FXML
    private void initialize() {
        this.topPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        this.topPane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }

    @FXML
    private void doClickOnLoginBtn(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            login();
    }

    @FXML
    private void login() {
        if (!emptyFields()) {
            String textUsername = this.username.getText();
            String passwordText = this.password.getText();
            try {
                String passwordEncrypted = SHA1Encrypt.encrypt(SHA1Encrypt.encrypt(passwordText));
                User user = ServiceLocator.getUserService().getUserByUserName(textUsername);
                if (user != null) {
                    if (user.getPassword().equals(passwordEncrypted)) {
                        userLoggedIn = new UserLoggedIn(
                                user.getNombre(),
                                user.getUsername(),
                                user.getPassword(),
                                ServiceLocator.getRolService().getRolById(user.getId_rol()).getNombre());
                        this.primaryStage.close();
                        loadPrincipalHechos();
                    } else {
                        Util.dialogResult("Usuario o contrase??a incorrectos.", Alert.AlertType.WARNING);
                        cleanFields();
                    }
                } else {
                    Util.dialogResult("Usuario o contrase??a incorrectos.", Alert.AlertType.WARNING);
                    cleanFields();
                }
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleClose() {
        Platform.exit();
    }

    private boolean emptyFields() {
        boolean empty = true;

        if (this.username.getText().isEmpty() && this.password.getText().isEmpty()) {
            Util.dialogResult("Campos vac??os. Por favor, ingrese su nombre de usuario y contrase??a.", Alert.AlertType.WARNING);
        } else if (this.password.getText().isEmpty()) {
            Util.dialogResult("El campo de contrase??a no puede estar vac??o.", Alert.AlertType.WARNING);
        } else if (this.username.getText().isEmpty()) {
            Util.dialogResult("El campo de nombre de usuario no puede estar vac??o.", Alert.AlertType.WARNING);
        } else
            empty = false;

        return empty;
    }

    private void cleanFields() {
        this.username.setText(null);
        this.password.setText(null);
    }

    private void loadPrincipalHechos() {
        try {
            //Load principal view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UrlLocation.class.getResource("PantallaPrincipalModulos.fxml"));
            AnchorPane pane = loader.load();
            //Show the scene containing the principal view
            Scene scene = new Scene(pane);

            //Give the controller access to the main app
            PantallaPrincipalModulos controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setPrimaryStage(primaryStage);
            this.primaryStage.setScene(scene);
            this.primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
