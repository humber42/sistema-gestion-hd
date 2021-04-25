package controllers;

import com.gembox.spreadsheet.SpreadsheetInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.Conexion;
import util.ConexionObserver;
import views.PanatallaPrincipalModulos;

import java.io.IOException;
import java.util.Observer;


public class MainApp extends Application {

    private Stage primaryStage;
    private AnchorPane principal;

    public static void main(String[] args) {
//        try {
//            Conexion conexion = new Conexion("localhost", 5432, "sysSP", "postgres", "postgres");
//        }catch (SQLException e){
//
//        }catch (IOException e){
//
//        }catch (ClassNotFoundException e){
//
//        }
        Observer o1 = new ConexionObserver();
        Conexion.getObservable().addObserver(o1);
        SpreadsheetInfo.setLicense("FREE-LIMITED-KEY");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sistema Informativo");
        this.primaryStage.setResizable(false);
        initRootLayout();

    }

    private void initRootLayout() {
        loadPrincipalHechos();
    }

    private void loadPrincipalHechos() {
        try {

            //Load principal view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../views/PantallaPrincipalModulos.fxml"));
            this.principal = (AnchorPane) loader.load();

            //Show the scene containing the principal view
            Scene scene = new Scene(this.principal);

            //Give the controller access to the main app
            PanatallaPrincipalModulos controller = loader.getController();
            controller.setMainApp(this);
            controller.setPrimaryStage(primaryStage);
            this.primaryStage.setScene(scene);
            this.primaryStage.initStyle(StageStyle.UNDECORATED);
            this.primaryStage.show();

        } catch (IOException e) {
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
