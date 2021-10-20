package posiciones_agentes.views;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.dialog.ExceptionDialog;
import posiciones_agentes.excels_generators.ExcelGeneratorLocator;
import posiciones_agentes.views.dialogs.DialogGenerarResumenPorUOController;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginController;
import util.Util;
import views.dialogs.DialogLoadingController;
import views.dialogs.DialogLoadingUrl;

import java.io.IOException;


public class MainPosicionesAgentesController {
    private Stage mainApp;
    private BorderPane panelPosicionesAgentes;
    private BorderPane panelGrande;

    @FXML
    private Label lblNombre;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblRol;

    private UserLoggedIn logged;

    private Stage dialogStage;

    public void setPanelGrande(BorderPane panelGrande) {
        this.panelGrande = panelGrande;
    }

    @FXML
    public void initialize() {
        this.logged = LoginController.getUserLoggedIn();
        userLoggedInfo();
    }

    private void userLoggedInfo() {
        this.lblNombre.setText(logged.getNombre());
        this.lblUsername.setText(logged.getUsername());
        this.lblRol.setText(logged.getRol());
    }

    @FXML
    private void cargarRegister(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainPosicionesAgentesController.class.getResource("RegistrarPosicionesAgentes.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Registrar Posiciones Agentes");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            RegistrarPosicionesAgentesController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        }catch (IOException e){
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    /*@FXML
    private void handleCerrar() {
        this.panelGrande.setCenter(null);
    }*/

    public void setMainApp(Stage mainApp) {
        this.mainApp = mainApp;
    }

    public void setPanelPosicionesAgentes(BorderPane pane) {
        this.panelPosicionesAgentes = pane;
    }

    @FXML
    private void editTarifa(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainPosicionesAgentesController.class.getResource("EdicionTarifasView.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar tarifas");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            EdicionTarifasContoller controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {

            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    @FXML
    private void generarResumenPorUOrg(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainPosicionesAgentesController.class.getResource("dialogs/DialogGenerarResumenPorUO.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Generar resumen por unidad organizativa");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            DialogGenerarResumenPorUOController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {

            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    @FXML
    private void generarResumenGeneral(){
        String path = "src/informesGenerados/ResumenGeneralPosiciones.xlsx";
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                ExcelGeneratorLocator.getResumenGeneralGenerator()
                        .generarResumen(path);
                return true;
            }
            @Override
            protected void succeeded(){
                super.succeeded();
                dialogStage.close();
                Util.dialogResult("Generado", Alert.AlertType.INFORMATION);
                try {
                    Runtime.getRuntime().exec("cmd /c start " + path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private void loadDialogLoading(Stage mainApp){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DialogLoadingUrl.class.getResource("DialogLoading.fxml"));
            AnchorPane panel = loader.load();
            dialogStage = new Stage();
            dialogStage.setScene(new Scene(panel));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            DialogLoadingController controller = loader.getController();
            controller.setLabelText("Cargando");
            dialogStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void generarResumenPS(){
        String path = "src/informesGenerados/ResumenProveedoresServicio.xlsx";
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                ExcelGeneratorLocator.getResumenProveedorServicio()
                        .generarResumenProveedorServicio(path);
                return true;
            }
            @Override
            protected void succeeded(){
                super.succeeded();
                dialogStage.close();
                Util.dialogResult("Generado", Alert.AlertType.INFORMATION);
                try {
                    Runtime.getRuntime().exec("cmd /c start " + path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}
