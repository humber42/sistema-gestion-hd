package posiciones_agentes.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.UnidadOrganizativa;
import org.controlsfx.dialog.ExceptionDialog;
import posiciones_agentes.excels_generators.ExcelGeneratorLocator;
import posiciones_agentes.views.dialogs.DialogGenerarResumenPorUOController;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;
import java.sql.SQLException;


public class MainPosicionesAgentesController {
    private Stage mainApp;
    private BorderPane panelPosicionesAgentes;
    private BorderPane panelGrande;

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
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainPosicionesAgentesController.class.getResource("../views/dialogs/DialogGenerarResumenPorUO.fxml"));
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
        ExcelGeneratorLocator.getResumenGeneralGenerator()
                .generarResumen("src/informesGenerados/ResumenGeneralPosiciones.xlsx");
        Util.dialogResult("Generado", Alert.AlertType.INFORMATION);
    }
}
