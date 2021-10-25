package sistema_identificativo.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.ExceptionDialog;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginViewController;
import services.ServiceLocator;
import sistema_identificativo.views.dialogs.AddPicturePendingToPass;
import sistema_identificativo.views.dialogs.DialogGenerarResumenPasesUO;


import java.io.IOException;

public class MainSistemaIdentificativoController {

    private Stage mainApp;
    private BorderPane panelSistemaIdentificativo;
    private BorderPane panelGrande;

    @FXML
    private Label lblNombre;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblRol;
    @FXML
    private Menu menuArchivo;
    @FXML
    private MenuItem mnFotoPasePending;

    private UserLoggedIn logged;

    public void setPanelGrande(BorderPane panelGrande) {
        this.panelGrande = panelGrande;
    }

    @FXML
    public void initialize() {
        this.logged = LoginViewController.getUserLoggedIn();
        userLoggedInfo();

        if(logged.isSuperuser() || logged.hasPermiso_pases()){
            this.menuArchivo.setVisible(true);
            this.mnFotoPasePending.setVisible(true);
        }
        else if(logged.hasPermiso_visualizacion()){
            this.menuArchivo.setVisible(false);
            this.mnFotoPasePending.setVisible(false);
        }
    }

    private void userLoggedInfo() {
        this.lblNombre.setText(logged.getNombre());
        this.lblUsername.setText(logged.getUsername());
        this.lblRol.setText(logged.getRol());
    }

    /*@FXML
    private void handleCerrar() {
        this.panelGrande.setCenter(null);
    }*/

    public void setMainApp(Stage mainApp) {
        this.mainApp = mainApp;
    }


    public void registrarPase() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainSistemaIdentificativoController.class.getResource("RegistroPasesView.fxml"));
            AnchorPane pane = loader.load();
            //Create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Registrar Pase");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            RegistroPasesController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

        } catch (IOException e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    @FXML
    private void addPicturesToPassPendents() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainSistemaIdentificativoController.class.getResource("dialogs/AddPicturePendingToPass.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Añadir foto a pases pendientes");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            AddPicturePendingToPass controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {

            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    @FXML
    private void generarResumenPasesUnidadOrganizativa(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainSistemaIdentificativoController.class.getResource("dialogs/DialogGenerarResumenPasesUO.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Generar resumen de pases por Unidad Organizativa");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            DialogGenerarResumenPasesUO controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

        }catch (IOException e){
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    @FXML
    private void generarResumenPasesPermanentes(){
        ServiceLocator.getJasperReportService().imprimirResumenTipoPase(1,this.mainApp);
    }

    @FXML
    private void generarResumenPasesProvisional(){
        ServiceLocator.getJasperReportService().imprimirResumenTipoPase(2,this.mainApp);
    }

    @FXML
    private void generarResumenPaseEspecial(){
        ServiceLocator.getJasperReportService().imprimirResumenTipoPase(3,this.mainApp);
    }

    @FXML
    private void generarResumenPaseNegro(){
        ServiceLocator.getJasperReportService().imprimirResumenTipoPase(4,this.mainApp);
    }

    @FXML
    private void generarResumenGeneral(){
        ServiceLocator.getJasperReportService().imprimirResumenGeneral(this.mainApp);
    }

    @FXML
    private void generarResumenPasesBaja(){
        ServiceLocator.getJasperReportService().imprimirResumenBajas(this.mainApp);
    }

    @FXML
    private void generarResumenPasesPendienteFoto(){
        ServiceLocator.getJasperReportService().imprimirResumenFotosPendientes(this.mainApp);
    }

    @FXML
    private void generarResumenPasesImpresos(){
        ServiceLocator.getJasperReportService().imprimirResumenPasesImpresos(this.mainApp);
    }

    public void setPanelSistemaIdentificativo(BorderPane pane) {
        this.panelSistemaIdentificativo = pane;
    }

    @FXML
    private void imprimirPase(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainSistemaIdentificativoController.class.getResource("ImprimirPasesView.fxml"));
            AnchorPane pane = loader.load();
            //Create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Imprimir Pases");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            ImprimirPasesController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e){
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    @FXML
    private void bajaPase(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainSistemaIdentificativoController.class.getResource("BajaPaseView.fxml"));
            AnchorPane pane = loader.load();
            //Create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dar baja pases");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            BajaPaseViewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e){
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

}
