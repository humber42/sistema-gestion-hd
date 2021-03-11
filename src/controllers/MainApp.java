package controllers;

import com.gembox.spreadsheet.SpreadsheetInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import views.PrincipalViewController;

import java.io.IOException;


public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane principal;

    public static void main(String[] args) {
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
            loader.setLocation(MainApp.class.getResource("../views/PrinicipalView.fxml"));
            this.principal = (BorderPane) loader.load();

            //Show the scene containing the principal view
            Scene scene = new Scene(this.principal);
            this.primaryStage.setScene(scene);
            this.primaryStage.show();

            //Give the controller access to the main app
            PrincipalViewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BorderPane getPrincipal() {
        return this.principal;
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

}
