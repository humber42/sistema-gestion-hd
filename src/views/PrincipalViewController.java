package views;


import com.gembox.internal.core.DivideByZeroException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.TipoHecho;
import org.controlsfx.dialog.ExceptionDialog;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginViewController;
import services.ServiceLocator;
import util.Conexion;
import util.PieChartUtils;
import util.Util;
import views.dialogDelictivos.DialogGenerarListadosDelictivos;
import views.dialogs.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.stream.Collectors;

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
    @FXML
    private PieChart jfxPieChart;
    @FXML
    private BarChart jfxBarChart;
    @FXML
    private TitledPane panePieChart;
    @FXML
    private TitledPane paneBarChart;
    @FXML
    private ComboBox<String> cboxTipoHechos;

    private UserLoggedIn logged;

    private int currentYear;

    public void setPanelGrande(BorderPane panelGrande) {
        this.panelGrande = panelGrande;
    }

    @FXML
    public void initialize() {
        this.logged = LoginViewController.getUserLoggedIn();
        this.userLoggedInfo();

        if(logged.hasPermiso_visualizacion() || logged.hasPermiso_pases()){
            this.menuRegistrarHecho.setVisible(false);
        } else if(logged.isSuperuser()){
            this.menuRegistrarHecho.setVisible(true);
        }

        this.currentYear = LocalDate.now().getYear();

        this.panePieChart.setText("Comportamiento de los hechos en el año " + LocalDate.now().getYear());
        //cargando el grafico de pastel
        this.loadPieChart();

        this.cboxTipoHechos.getItems().setAll(
            ServiceLocator.getTipoHechoService().fetchAll()
            .stream()
            .map(TipoHecho::getTipo_hecho)
            .collect(Collectors.toList())
        );
        //cargando el grafico de barras
        this.cboxTipoHechos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    this.paneBarChart.setText("Comportamiento de los hechos de tipo: " + newValue);
                    this.loadBarChart();
                }
        );


    }

    private void userLoggedInfo() {
        this.lblNombre.setText(lblNombre.getText() + " " + logged.getNombre());
       // this.lblUsername.setText(logged.getUsername());
       // this.lblRol.setText(logged.getRol());
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

    private void loadPieChart(){
        PieChartUtils pieChartUtils = new PieChartUtils(this.jfxPieChart);
        //pieChartUtils.operatePieChart();
        int totalHechos = ServiceLocator.getHechosService().countAllHechosByAnno(currentYear);

        if(totalHechos > 0){
            int hechosPext = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(1, currentYear);
            int hechosTpub = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(2, currentYear);
            int hechosRobo = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(3, currentYear);
            int hechosHurto = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(4, currentYear);
            int hechosFraude = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(5, currentYear);
            int hechosAccionCR = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(6, currentYear);
            int hechosOtrosDelitos = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(7, currentYear);
            int hechosAveria = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(8, currentYear);
            int hechosTransito = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(9, currentYear);
            int hechosSegInf = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(10, currentYear);
            int hechosIncendioInt = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(11, currentYear);
            int hechosIncendioExt = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(12, currentYear);
            int hechosDifOrigen = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(13, currentYear);
            int otrosHechos = ServiceLocator.getHechosService().
                    cantHechosByTipoHechoAndAnno(14, currentYear);

            //insertando datos en el grafico
            try{
                pieChartUtils.addData("Delito vs PExt",
                        Util.getPercent(hechosPext, totalHechos));
                pieChartUtils.addData("Delito vs TPúb",
                        Util.getPercent(hechosTpub, totalHechos));
                pieChartUtils.addData("Robo",
                        Util.getPercent(hechosRobo, totalHechos));
                pieChartUtils.addData("Hurto",
                        Util.getPercent(hechosHurto, totalHechos));
                pieChartUtils.addData("Fraude",
                        Util.getPercent(hechosFraude, totalHechos));
                pieChartUtils.addData("Acción C/R",
                        Util.getPercent(hechosAccionCR, totalHechos));
                pieChartUtils.addData("Otros Delitos",
                        Util.getPercent(hechosOtrosDelitos, totalHechos));
                pieChartUtils.addData("Avería PExt",
                        Util.getPercent(hechosAveria, totalHechos));
                pieChartUtils.addData("Acc. Tránsito",
                        Util.getPercent(hechosTransito, totalHechos));
                pieChartUtils.addData("Seg. Informática",
                        Util.getPercent(hechosSegInf, totalHechos));
                pieChartUtils.addData("Incendio Int.",
                        Util.getPercent(hechosIncendioInt, totalHechos));
                pieChartUtils.addData("Incendio Ext.",
                        Util.getPercent(hechosIncendioExt, totalHechos));
                pieChartUtils.addData("Difer. Origen",
                        Util.getPercent(hechosDifOrigen, totalHechos));
                pieChartUtils.addData("Otros Hechos",
                        Util.getPercent(otrosHechos, totalHechos));
            } catch (DivideByZeroException e){
                Util.dialogResult("Ocurrió un error. División por 0.", Alert.AlertType.ERROR);
            }

            //mostrando grafico
            pieChartUtils.showChart();

          /*  //estableciendo el color
            pieChartUtils.setDataColor(0, "red");
            pieChartUtils.setDataColor(1, "blue");
            pieChartUtils.setDataColor(2, "green");
            pieChartUtils.setDataColor(3, "yellow");
            pieChartUtils.setDataColor(4, "orange");
            pieChartUtils.setDataColor(5, "black");
            pieChartUtils.setDataColor(6, "brown");
            pieChartUtils.setDataColor(7, "pink");
            pieChartUtils.setDataColor(8, "purple");
            pieChartUtils.setDataColor(9, "ligth blue");
            pieChartUtils.setDataColor(10, "white");
            pieChartUtils.setDataColor(11, "violet");
            pieChartUtils.setDataColor(12, "golden");
            pieChartUtils.setDataColor(13, "silver");

            pieChartUtils.setMarkVisible(true);

            // Establecer el color de la serie de datos del gráfico
            HashMap<Integer, String> hashMap = new HashMap<>();
            hashMap.put(0, "red");
            hashMap.put(1, "blue");
            hashMap.put(2, "green");
            hashMap.put(3, "yellow");
            hashMap.put(4, "orange");
            hashMap.put(5, "black");
            hashMap.put(6, "brown");
            hashMap.put(7, "pink");
            hashMap.put(8, "purple");
            hashMap.put(9, "ligth blue");
            hashMap.put(10, "white");
            hashMap.put(11, "violet");
            hashMap.put(12, "golden");
            hashMap.put(13, "silver");
*/
            //poniendo los colores en la leyenda
         //   pieChartUtils.setLegendColor(hashMap);
            pieChartUtils.setLegendSide("Bottom");
        } else{
            Util.dialogResult("No existen hechos registrados en el año " + currentYear, Alert.AlertType.INFORMATION);
        }
    }

    private void loadBarChart(){
        final CategoryAxis xAxis = (CategoryAxis) jfxBarChart.getXAxis();
        final NumberAxis yAxis = (NumberAxis) jfxBarChart.getYAxis();

        xAxis.setLabel("Año");
        yAxis.setLabel("Cantidad");

        XYChart.Series<Integer, Integer> series = new XYChart.Series<>();

        int id_tipo_hecho = ServiceLocator.getTipoHechoService().
                searchTipoHechoByName( this.cboxTipoHechos.getSelectionModel().getSelectedItem())
                .getId_tipo_hecho();

        while (currentYear >= 2007){
            int year = currentYear;
            series.getData().add(new XYChart.Data<>(year,
                    ServiceLocator.getHechosService().cantHechosByTipoHechoAndAnno(id_tipo_hecho ,year)));
            currentYear--;
        }

        jfxBarChart.getData().add(series);
    }
}
