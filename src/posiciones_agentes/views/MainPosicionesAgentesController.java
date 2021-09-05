package posiciones_agentes.views;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.UnidadOrganizativa;
import org.controlsfx.dialog.ExceptionDialog;
import posiciones_agentes.excels_generators.ExcelGeneratorLocator;
import services.ServiceLocator;
import util.Util;
import views.dialogs.DialogLoadingController;

import java.io.IOException;
import java.sql.SQLException;


public class MainPosicionesAgentesController {
    private Stage mainApp;
    private BorderPane panelPosicionesAgentes;
    private BorderPane panelGrande;

    private Stage dialogStage;

    public void setPanelGrande(BorderPane panelGrande) {
        this.panelGrande = panelGrande;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    private void cargarRegister(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainPosicionesAgentesController.class.getResource("../views/RegistrarPosicionesAgentes.fxml"));
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

    @FXML
    private void handleCerrar() {
        this.panelGrande.setCenter(null);
    }

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
            loader.setLocation(MainPosicionesAgentesController.class.getResource("../views/EdicionTarifasView.fxml"));
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
        ExcelGeneratorLocator.getResumenUnidadOrganizativa().generarResumenUnidadOrganizativa("src/informesGenerados/ResumenPosiciones"+"DTPR"+".xlsx",
                ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName("DTPR"));
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
            loader.setLocation(MainPosicionesAgentesController.class.getResource("../../views/dialogs/DialogLoading.fxml"));
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
}
