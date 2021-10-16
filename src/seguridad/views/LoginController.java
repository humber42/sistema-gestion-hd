package seguridad.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seguridad.models.User;
import seguridad.models.UserLoggedIn;
import seguridad.utils.SHA1Encrypt;
import services.ServiceLocator;
import util.Util;
import views.MainApp;
import views.PanatallaPrincipalModulos;
import views.UrlLocation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    private Stage primaryStage;

    private MainApp mainApp;

    private UserLoggedIn userLoggedIn;

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize(){
    }

    @FXML
    private void login(){
        if (!emptyFields()){
            String textUsername = this.username.getText();
            String passwordText = this.password.getText();
            try {
                String passwordEncrypted = SHA1Encrypt.encrypt(SHA1Encrypt.encrypt(passwordText));
                User user = ServiceLocator.getUserService().getUserByUserName(textUsername);
                if(user != null){
                    if(user.getPassword().equals(passwordEncrypted)){
                        System.out.println("Logueado");
                        this.userLoggedIn = new UserLoggedIn(
                                user.getNombre(),
                                user.getUsername(),
                                user.getPassword(),
                                ServiceLocator.getRolService().getRolById(user.getId_rol()).getNombre());
                        this.primaryStage.close();
                        loadPrincipalHechos();
                    }
                }
                else{
                    Util.dialogResult("Usuario o contraseña incorrectos.", Alert.AlertType.WARNING);
                    cleanFields();
                }
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleClose(){
        this.primaryStage.close();
    }

    private boolean emptyFields(){
        boolean empty = true;

        if(this.username.getText().isEmpty() && this.password.getText().isEmpty()){
            Util.dialogResult("Campos vacíos. Por favor, ingrese su nombre de usuario y contraseña.", Alert.AlertType.WARNING);
        }
        else if(this.password.getText().isEmpty()){
            Util.dialogResult("El campo de contraseña no puede estar vacío.", Alert.AlertType.WARNING);
        }
        else if(this.username.getText().isEmpty()){
            Util.dialogResult("El campo de nombre de usuario no puede estar vacío.", Alert.AlertType.WARNING);
        }
        else
            empty = false;

        return empty;
    }

    private void cleanFields(){
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
            PanatallaPrincipalModulos controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setPrimaryStage(primaryStage);
            this.primaryStage.setScene(scene);
            this.primaryStage.show();
            controller.userLoggedInfo(this.userLoggedIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
