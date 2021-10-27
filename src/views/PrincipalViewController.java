package views;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.ExceptionDialog;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginViewController;
import util.Conexion;
import util.Util;
import views.dialogDelictivos.DialogGenerarListadosDelictivos;
import views.dialogs.*;

import java.io.IOException;
import java.sql.SQLException;

public class PrincipalViewController {
    private Stage mainApp;
    private BorderPane panelPrincipal;
    private BorderPane panelGrande;

    @FXML
    private Label lblNombre;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblRol;
    @FXML
    private MenuItem menuRegistrarHecho;

    private UserLoggedIn logged;

    public void setPanelGrande(BorderPane panelGrande) {
        this.panelGrande = panelGrande;
    }

    @FXML
    public void initialize() {
        this.logged = LoginViewController.getUserLoggedIn();
        userLoggedInfo();
        if(logged.hasPermiso_visualizacion() || logged.hasPermiso_pases()){
            this.menuRegistrarHecho.setVisible(false);
        } else if(logged.isSuperuser()){
            this.menuRegistrarHecho.setVisible(true);
        }
    }

    private void userLoggedInfo() {
        this.lblNombre.setText(logged.getNombre());
        this.lblUsername.setText(logged.getUsername());
        this.lblRol.setText(logged.getRol());
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("RegistrarView.fxml"));
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registrar Hecho");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.mainApp);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene(anchorPane));

            RegistrarViewController controller = loader.getController();
            controller.setDialogStage(stage);
            controller.setFromPrincipal(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("BuscarHechosView.fxml"));
            BorderPane pane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Buscar hechos");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            BuscarHechosViewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEsclarecer() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("EsclarecimientoView.fxml"));
            AnchorPane page = loader.load();
            //Create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Esclarecer hechos");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            EsclarecimientoViewController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }


    @FXML
    private void handelFiscaliaInforme() {
        try {
            FXMLLoader loader = new FXMLLoader(PrincipalViewController.class.getResource("dialogs/DialogGenerarInformeFiscalia.fxml"));
            AnchorPane pane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Generar Informe Fiscalia");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp);
            dialogStage.setScene(new Scene(pane));

            DialogGenerarInformeFiscaliaController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

        } catch (IOException e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }

    }

    @FXML
    private void handleCentroDireccionInforme() {
        try {
            FXMLLoader loader = new FXMLLoader(PrincipalViewController.class.getResource("dialogs/DialogGenerarInformeCentroDirecion.fxml"));
            AnchorPane pane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Generar Informe Centro de Direccion");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp);
            dialogStage.setScene(new Scene(pane));

            DialogGenerarInformeCentroDirecion controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

        } catch (IOException e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    @FXML
    private void handleCertificoHechos() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("dialogs/DialogGenerarCertificoHechos.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Generar Certifico de Hechos");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp);
            dialogStage.setScene(new Scene(pane));

            DialogGenerarCertificoHechosController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    @FXML
    private void handleDatosPendientes() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("dialogs/DialogGenerarHechosPendientes.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp);
            dialogStage.setResizable(false);
            dialogStage.setMaximized(false);
            dialogStage.setTitle("Hechos con datos pendientes");
            dialogStage.setScene(new Scene(pane));
            DialogGenerarHechosPendientesController controller = loader.getController();
            controller.asignarAnnos();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    @FXML
    private void handleDatosPrevencion() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("dialogs/DialogGenerarPrevencion.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp);
            dialogStage.setResizable(false);
            dialogStage.setMaximized(false);
            dialogStage.setTitle("Resumen de prevencion");
            dialogStage.setScene(new Scene(pane));
            DialogGenerarPrevencionController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    @FXML
    private void handleHechosPrevenidos() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("dialogs/DialogGenerarHechosPrevenidos.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp);
            dialogStage.setResizable(false);
            dialogStage.setMaximized(false);
            dialogStage.setScene(new Scene(pane));
            dialogStage.setTitle("Resumen de Hechos Prevenidos");
            DialogGenerarHechosPrevenidosController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    @FXML
    private void handleHechosParaUOrg() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("dialogs/DialogGenerarHechosUorg.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.mainApp);
            dialogStage.setScene(new Scene(pane));

            dialogStage.setTitle("Hechos por Unidad Organizativa");
            dialogStage.setResizable(false);
            dialogStage.setMaximized(false);
            DialogGenerarHechosUorgController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHechosParaResumenMINCOM() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("dialogs/DialogGenerarResumenMINCOM.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            dialogStage.setScene(new Scene(pane));
            dialogStage.setTitle("Resumen MINCOM");
            DialogGenerarResumenMINCOM controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            new ExceptionDialog(e).showAndWait();
        }
    }

    @FXML
    private void handleAveria() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("TipoDeAveriaView.fxml"));
            AnchorPane page = loader.load();
            Stage averiaStage = new Stage();
            averiaStage.initModality(Modality.WINDOW_MODAL);
            averiaStage.setMaximized(false);
            averiaStage.setResizable(false);
            averiaStage.initOwner(this.mainApp);
            averiaStage.setScene(new Scene(page));
            averiaStage.setTitle("Codificador Averia");
            TipoDeAveriaViewController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            averiaStage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void generarDelitoPextListado() {
        this.cargarPantallaDialogosListadosDelictivos("/PextListado.xlsx",
                1, "Delito VS PExt");
    }

    @FXML
    private void generarDelitoTpubListado() {
        this.cargarPantallaDialogosListadosDelictivos("/TPubListado.xlsx",
                2, "Delito vs TPub");
    }

    @FXML
    private void generarDelitoHurtoListado() {
        this.cargarPantallaDialogosListadosDelictivos("/HurtoListado.xlsx", 4,
                "Delito Hurto");
    }

    @FXML
    private void generarDelitoRoboListado() {
        this.cargarPantallaDialogosListadosDelictivos("/RoboListado.xlsx", 3,
                "Delito Robo");
    }

    @FXML
    private void generarDelitoFraudeListado() {
        this.cargarPantallaDialogosListadosDelictivos("/FraudeListado.xlsx", 5,
                "Delito Fraude");
    }

    @FXML
    private void generarDelitoAccionCRListado() {
        this.cargarPantallaDialogosListadosDelictivos("/HurtoListado.xlsx", 6,
                "Delito Acción C/R");
    }

    @FXML
    private void generarDelitoOtrosListado() {
        this.cargarPantallaDialogosListadosDelictivos("/OtrosDelitosListado.xlsx", 7,
                "Otros Delitos");
    }

    @FXML
    private void generarAveriaPextListado() {
        this.cargarPantallaDialogosListadosDelictivos("/AveriaPextListado.xlsx",
                8, "Averia Pext");
    }

    @FXML
    private void generarAccTransitoListado() {
        this.cargarPantallaDialogosListadosDelictivos("/AccTransitoListado.xlsx",
                9, "Accidente de Tránsito");
    }

    @FXML
    private void generarSegInformaticaListado() {
        this.cargarPantallaDialogosListadosDelictivos("/SegInfListado.xlsx",
                10, "Seguridad Informática");
    }

    @FXML
    private void generarIncendioListado() {
        this.cargarPantallaDialogosListadosDelictivos("/IncendioListado.xlsx",
                11, "Incendio");
    }

    @FXML
    private void generarDiferOrigenListado() {
        this.cargarPantallaDialogosListadosDelictivos("/DiferOrigenListado.xlsx",
                13, "Diferente Origen");
    }

    @FXML
    private void generarOtrosHechosListado() {
        this.cargarPantallaDialogosListadosDelictivos("/OtrosHechosListado.xlsx",
                14, "Otros Hechos");
    }


    private void cargarPantallaDialogosListadosDelictivos(String direccionAddres, int tipoHecho, String nombreHecho) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(PrincipalViewController.class.getResource("dialogDelictivos/DialogGenerarListadosDelictivos.fxml"));
        try {
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            dialogStage.setScene(new Scene(pane));
            dialogStage.setTitle("Generar Listado " + nombreHecho);
            DialogGenerarListadosDelictivos controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDireccionFile(direccionAddres);
            controller.setTipoHecho(tipoHecho);
            controller.getLabelTitulo("Generar Listado " + nombreHecho);
            try {
                Conexion.getConnection().getClientInfo();
                dialogStage.showAndWait();
            } catch (SQLException e) {
                Util.dialogResult("No hay conexion", Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Util.dialogResult("No hay conexion", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void handleMunicipio(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("ListaMunicipiosView.fxml"));
            AnchorPane page =  loader.load();
            Stage municipioStage = new Stage();
            municipioStage.initModality(Modality.WINDOW_MODAL);
            municipioStage.setMaximized(false);
            municipioStage.setResizable(false);
            municipioStage.initOwner(this.mainApp);
            municipioStage.setScene(new Scene(page));
            municipioStage.setTitle("Codificador Municipio");
            ListaMunicipiosViewController controller = loader.getController();
            controller.setStage(municipioStage);
            municipioStage.setWidth(805);
            municipioStage.setHeight(500);
            municipioStage.showAndWait();
//            mainApp.getPrincipal().setCenter(page);
//            mainApp.getPrimaryStage().setWidth(805);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUnidadOrganizativa(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("UnidadesOrgView.fxml"));
            AnchorPane page =  loader.load();
            Stage uniOrgStage = new Stage();
            uniOrgStage.initModality(Modality.WINDOW_MODAL);
            uniOrgStage.setMaximized(false);
            uniOrgStage.setResizable(false);
            uniOrgStage.initOwner(this.mainApp);
            uniOrgStage.setScene(new Scene(page));
            uniOrgStage.setTitle("Codificador Unidad Organizativa");
            UnidadesOrgViewController controller = loader.getController();
            controller.setStage(uniOrgStage);
            uniOrgStage.setWidth(805);
            uniOrgStage.setHeight(500);
            uniOrgStage.showAndWait();
//            mainApp.getPrincipal().setCenter(page);
//            mainApp.getPrimaryStage().setWidth(805);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAfectacion(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("TipoAfectacionView.fxml"));
            AnchorPane page =  loader.load();
            Stage afectacionStage = new Stage();
            afectacionStage.initModality(Modality.WINDOW_MODAL);
            afectacionStage.setMaximized(false);
            afectacionStage.setResizable(false);
            afectacionStage.initOwner(this.mainApp);
            afectacionStage.setScene(new Scene(page));
            afectacionStage.setTitle("Codificador Afectacion");

            TipoAfectacionViewController controller = loader.getController();
            controller.setStage(afectacionStage);
            afectacionStage.setWidth(805);
            afectacionStage.setHeight(500);
            afectacionStage.showAndWait();
//            mainApp.getPrincipal().setCenter(page);
//            mainApp.getPrimaryStage().setWidth(805);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTipoMateriales(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("TipoMaterialView.fxml"));
            AnchorPane page =  loader.load();
            Stage tipoMateStage = new Stage();
            tipoMateStage.initModality(Modality.WINDOW_MODAL);
            tipoMateStage.setMaximized(false);
            tipoMateStage.setResizable(false);
            tipoMateStage.initOwner(this.mainApp);
            tipoMateStage.setScene(new Scene(page));
            tipoMateStage.setTitle("Codificador Tipo de Material");

            TipoMaterialViewController controller = loader.getController();
            controller.setStage(tipoMateStage);
            tipoMateStage.setWidth(805);
            tipoMateStage.setHeight(500);
            tipoMateStage.showAndWait();
//            mainApp.getPrincipal().setCenter(page);
//            mainApp.getPrimaryStage().setWidth(805);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHechosRegistrados(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("HechosRegistradosView.fxml"));
            AnchorPane page =  loader.load();
            Stage hechoStage = new Stage();
            hechoStage.initModality(Modality.WINDOW_MODAL);
            hechoStage.setMaximized(false);
            hechoStage.setResizable(false);
            hechoStage.initOwner(this.mainApp);
            hechoStage.setScene(new Scene(page));
            hechoStage.setTitle("Hechos Registrados");

            HechosRegistradosViewController controller = loader.getController();
            controller.setStage(hechoStage);
            hechoStage.showAndWait();
//            mainApp.getPrincipal().setCenter(page);
//            mainApp.getPrimaryStage().setWidth(1000);


            //mainApp.getPrimaryStage().setHeight(660);
            //mainApp.getPrincipal().setPrefWidth(631);
            //mainApp.getPrincipal().setPrefHeight(616);

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /*@FXML
    private void handleCerrar() {
        this.panelGrande.setCenter(null);
    }*/

            //mainApp.getPrimaryStage().setWidth(1000);

          //  mainApp.getPrimaryStage().setWidth(788);

    public void setMainApp(Stage mainAppL) {
        this.mainApp = mainAppL;
    }

    public void setPanelPrincipal(BorderPane pane) {
        this.panelPrincipal = pane;
    }
}
