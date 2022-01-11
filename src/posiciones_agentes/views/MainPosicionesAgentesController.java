package posiciones_agentes.views;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.dialog.ExceptionDialog;
import posiciones_agentes.excels_generators.ExcelGeneratorLocator;
import posiciones_agentes.models.ProveedorGasto;
import posiciones_agentes.utils.CalculoByOurg;
import posiciones_agentes.views.dialogs.DialogGenerarResumenPorUOController;
import posiciones_agentes.views.dialogs.DialogLoadFromExcelFileController;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginViewController;
import util.Util;
import views.dialogs.DialogLoadingController;
import views.dialogs.DialogLoadingUrl;

import java.io.IOException;
import java.util.List;


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
    @FXML
    private Menu menuArchivo;
    @FXML
    private BarChart<String, Number> barChart;

    private UserLoggedIn logged;

    private Stage dialogStage;

    public void setPanelGrande(BorderPane panelGrande) {
        this.panelGrande = panelGrande;
    }

    @FXML
    public void initialize() {
        this.logged = LoginViewController.getUserLoggedIn();
        userLoggedInfo();

        if(logged.isSuperuser()){
            this.menuArchivo.setVisible(true);
        }
        else if(logged.hasPermiso_pases() || logged.hasPermiso_visualizacion()){
            this.menuArchivo.setVisible(false);
        }
    }

    private void userLoggedInfo() {
        this.lblNombre.setText(lblNombre.getText() + " " + logged.getNombre());
       // this.lblUsername.setText(logged.getUsername());
       // this.lblRol.setText(logged.getRol());
    }

    @FXML
    private void cargarRegister(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainPosicionesAgentesController.class.getResource("GestionPosicionesAgentesView.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Gestionar Posiciones Agentes");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            GestionPosicionesAgentesController controller = loader.getController();
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
        String path = Util.selectPathToSaveReport(this.dialogStage, 0) + "/ResumenGeneralPosiciones.xlsx";
        if(!path.contains("null")) {
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    ExcelGeneratorLocator.getResumenGeneralGenerator()
                            .generarResumen(path);
                    return true;
                }

                @Override
                protected void succeeded() {
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

    private void loadDialogLoading(Stage mainApp){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DialogLoadingUrl.class.getResource("DialogLoading.fxml"));
            StackPane panel = loader.load();
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
    private void generarResumenPS() {
        String path = Util.selectPathToSaveReport(this.dialogStage, 0) + "/ResumenProveedoresServicio.xlsx";
        if (!path.contains("null")) {
            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    ExcelGeneratorLocator.getResumenProveedorServicio()
                            .generarResumenProveedorServicio(path);
                    return true;
                }

                @Override
                protected void succeeded() {
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

    @FXML
    private void loadFromExcel() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DialogLoadFromExcelFileController.class.getResource("DialogLoadFromFile.fxml"));
            AnchorPane panel = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(panel));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp);
            DialogLoadFromExcelFileController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBarChart() {
        this.barChart.getData().clear();
        final CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        final NumberAxis yAxis = (NumberAxis) barChart.getYAxis();

        xAxis.setLabel("Proveedor de Servicio");
        yAxis.setLabel("Cantidad de posiciones");

        List<ProveedorGasto> proveedorGastoList = CalculoByOurg.calculoProveedorGasto();

        if (!proveedorGastoList.isEmpty()) {
            for (ProveedorGasto pg : proveedorGastoList) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(pg.getProveedor());
                series.getData().add(new XYChart.Data<>(pg.getProveedor(), pg.getCantPosiciones()));
                this.barChart.getData().add(series);
            }
        }

        this.barChart.setAnimated(false);
    }
}
