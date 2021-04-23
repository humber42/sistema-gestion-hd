package views;

import controllers.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.ExceptionDialog;
import views.dialogs.*;

import java.io.IOException;

public class PrincipalViewController {


    private MainApp mainApp;


    @FXML
    public void initialize() {

    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("../views/RegistrarView.fxml"));
            AnchorPane page = loader.load();

            RegistrarViewController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            mainApp.getPrincipal().setCenter(page);
            mainApp.getPrimaryStage().setWidth(805);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEsclarecer() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("../views/EsclarecimientoView.fxml"));
            AnchorPane page = loader.load();
            //Create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Esclarecer hechos");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
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
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
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
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
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
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
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
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
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
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
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
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
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
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
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
            dialogStage.initOwner(this.mainApp.getPrimaryStage());
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
            loader.setLocation(PrincipalViewController.class.getResource("../views/TipoDeAveriaView.fxml"));
            AnchorPane page = loader.load();
            Stage averiaStage = new Stage();
            averiaStage.initModality(Modality.WINDOW_MODAL);
            averiaStage.setMaximized(false);
            averiaStage.setResizable(false);
            averiaStage.initOwner(this.mainApp.getPrimaryStage());
            averiaStage.setScene(new Scene(page));
            averiaStage.setTitle("Codificador Averia");
            TipoDeAveriaViewController controller = loader.getController();
            controller.setStage(averiaStage);
            averiaStage.setWidth(805);
            averiaStage.setHeight(500);
            averiaStage.showAndWait();
//            mainApp.getPrincipal().setCenter(page);
//            mainApp.getPrimaryStage().setWidth(805);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMunicipio(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("../views/ListaMunicipiosView.fxml"));
            AnchorPane page =  loader.load();
            Stage municipioStage = new Stage();
            municipioStage.initModality(Modality.WINDOW_MODAL);
            municipioStage.setMaximized(false);
            municipioStage.setResizable(false);
            municipioStage.initOwner(this.mainApp.getPrimaryStage());
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
            loader.setLocation(PrincipalViewController.class.getResource("../views/UnidadesOrgView.fxml"));
            AnchorPane page =  loader.load();
            Stage uniOrgStage = new Stage();
            uniOrgStage.initModality(Modality.WINDOW_MODAL);
            uniOrgStage.setMaximized(false);
            uniOrgStage.setResizable(false);
            uniOrgStage.initOwner(this.mainApp.getPrimaryStage());
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
            loader.setLocation(PrincipalViewController.class.getResource("../views/TipoAfectacionView.fxml"));
            AnchorPane page =  loader.load();
            Stage afectacionStage = new Stage();
            afectacionStage.initModality(Modality.WINDOW_MODAL);
            afectacionStage.setMaximized(false);
            afectacionStage.setResizable(false);
            afectacionStage.initOwner(this.mainApp.getPrimaryStage());
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
            loader.setLocation(PrincipalViewController.class.getResource("../views/TipoMaterialView.fxml"));
            AnchorPane page =  loader.load();
            Stage tipoMateStage = new Stage();
            tipoMateStage.initModality(Modality.WINDOW_MODAL);
            tipoMateStage.setMaximized(false);
            tipoMateStage.setResizable(false);
            tipoMateStage.initOwner(this.mainApp.getPrimaryStage());
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
            loader.setLocation(PrincipalViewController.class.getResource("../views/HechosRegistradosView.fxml"));
            AnchorPane page =  loader.load();
            Stage hechoStage = new Stage();
            hechoStage.initModality(Modality.WINDOW_MODAL);
            hechoStage.setMaximized(false);
            hechoStage.setResizable(false);
            hechoStage.initOwner(this.mainApp.getPrimaryStage());
            hechoStage.setScene(new Scene(page));
            hechoStage.setTitle("Codificador Hechos");

            HechosRegistradosViewController controller = loader.getController();
            controller.setStage(hechoStage);
            hechoStage.setHeight(550);
            hechoStage.setWidth(1000);
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


    @FXML
    private void handleCerrar() {
        this.mainApp.getPrimaryStage().close();
    }

            //mainApp.getPrimaryStage().setWidth(1000);

          //  mainApp.getPrimaryStage().setWidth(788);

    public void setMainApp(MainApp mainAppL) {
        this.mainApp = mainAppL;
    }
}
