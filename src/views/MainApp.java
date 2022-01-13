package views;

import com.gembox.spreadsheet.SpreadsheetInfo;
import icons.ImageLocation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.dialog.ExceptionDialog;
import seguridad.views.LoginUrl;
import seguridad.views.LoginViewController;
import util.Conexion;
import util.ConexionObserver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Observer;


public class MainApp extends Application {
    private Stage primaryStage;
    private AnchorPane principal;

    public static void main(String[] args) {
        Observer o1 = new ConexionObserver();
        Conexion.getObservable().addObserver(o1);
        SpreadsheetInfo.setLicense("FREE-LIMITED-KEY");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sistema Informativo de Seguridad y Protección ETECSA");
        this.primaryStage.setResizable(false);


        initRootLayout();
    }

    private void initRootLayout() {
        boolean dataBaseNotFound = true;
        int intentos = 0;
        while (dataBaseNotFound && intentos < 3) {
            try {
                Conexion.getConnection().getClientInfo();
                dataBaseNotFound = false;
                intentos = 3;
                this.loadLogin();
            } catch (SQLException e) {
                ExceptionDialog dialog = new ExceptionDialog(e);
                dialog.showAndWait();
                intentos++;
            }
        }
    }

    private void loadLogin(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LoginUrl.class.getResource("LoginView.fxml"));
            this.principal = (AnchorPane) loader.load();
            Scene scene = new Scene(this.principal);

            LoginViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setPrimaryStage(primaryStage);
            this.primaryStage.setScene(scene);
            this.primaryStage.initStyle(StageStyle.UNDECORATED);
            try {
                this.primaryStage.getIcons().add(new Image(ImageLocation.class.getResource("icon_app.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            this.primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public AnchorPane getPrincipal() {
        return this.principal;
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

}
