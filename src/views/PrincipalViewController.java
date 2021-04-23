package views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.ExceptionDialog;
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

    public void setPanelGrande(BorderPane panelGrande) {
        this.panelGrande = panelGrande;
    }

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
            controller.setMainApp(this.panelPrincipal);
            panelPrincipal.setCenter(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("../views/BuscarView.fxml"));
            AnchorPane pane = loader.load();
            BuscarViewController controller = loader.getController();
            controller.setPrincipalView(this.panelPrincipal);
            panelPrincipal.setCenter(pane);
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
            dialogStage.initOwner(mainApp);
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
            loader.setLocation(PrincipalViewController.class.getResource("../views/TipoDeAveriaView.fxml"));
            AnchorPane page = loader.load();

            TipoDeAveriaViewController controller = loader.getController();
            controller.setMainApp(this.panelPrincipal);
            //mainApp.getPrincipal().setCenter(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void generarDelitoPextListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/PextListado.xlsx",
                1, "Delito VS PExt");
    }

    @FXML
    private void generarDelitoTpubListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/TPubListado.xlsx",
                2, "Delito vs TPub");
    }

    @FXML
    private void generarDelitoHurtoListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/HurtoListado.xlsx", 4,
                "Delito Hurto");
    }

    @FXML
    private void generarDelitoRoboListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/RoboListado.xlsx", 3,
                "Delito Robo");
    }

    @FXML
    private void generarDelitoFraudeListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/FraudeListado.xlsx", 5,
                "Delito Fraude");
    }

    @FXML
    private void generarDelitoAccionCRListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/HurtoListado.xlsx", 6,
                "Delito Acción C/R");
    }

    @FXML
    private void generarDelitoOtrosListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/OtrosDelitosListado.xlsx", 7,
                "Otros Delitos");
    }

    @FXML
    private void generarAveriaPextListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/AveriaPextListado.xlsx",
                8, "Averia Pext");
    }

    @FXML
    private void generarAccTransitoListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/AccTransitoListado.xlsx",
                9, "Accidente de Tránsito");
    }

    @FXML
    private void generarSegInformaticaListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/SegInfListado.xlsx",
                10, "Seguridad Informática");
    }

    @FXML
    private void generarIncendioListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/IncendioListado.xlsx",
                11, "Incendio");
    }

    @FXML
    private void generarDiferOrigenListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/DiferOrigenListado.xlsx",
                13, "Diferente Origen");
    }

    @FXML
    private void generarOtrosHechosListado() {
        this.cargarPantallaDialogosListadosDelictivos("src/informesGenerados/OtrosHechosListado.xlsx",
                14, "Otros Hechos");
    }


    private void cargarPantallaDialogosListadosDelictivos(String direccionAddres, int tipoHecho, String nombreHecho) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(PrincipalViewController.class.getResource("../views/dialogDelictivos/DialogGenerarListadosDelictivos.fxml"));
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
    private void handleCerrar() {
        this.panelGrande.setCenter(null);
    }


    public void setMainApp(Stage mainAppL) {
        this.mainApp = mainAppL;
    }

    public void setPanelPrincipal(BorderPane pane) {
        this.panelPrincipal = pane;
    }
}
