package sistema_identificativo.services.impl;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import services.ServiceLocator;
import sistema_identificativo.models.Impresion;
import sistema_identificativo.services.JasperReportService;
import util.Conexion;
import util.ConfigProperties;
import views.dialogs.DialogLoadingController;
import views.dialogs.DialogLoadingUrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Henry A. Serra Morejon
 * @implSpec  JasperReportService
 */
public class JasperReportServiceImpl implements JasperReportService {

    private static final String URL_SERVER = ConfigProperties.getProperties().getProperty("URL_DOWNLOAD_IMAGE");
    private static final String PATH = "src/sistema_identificativo/jasper_reports/";
    private Stage dialogStage;

    //TODO: Make some thread to dont freeze the User Interface;
    private void loadDialogLoading(Stage mainApp){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DialogLoadingUrl.class.getResource("DialogLoading.fxml"));
            StackPane panel = loader.load();
            dialogStage = new Stage();

            dialogStage.initOwner(mainApp);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            panel.setStyle(
                    "-fx-background-color: rgba(144,144,144,0.5);" +
                            "-fx-background-insets: 50;"
            );

            Scene scene = new Scene(panel);
            scene.setFill(Color.TRANSPARENT);
            dialogStage.setScene(scene);
            //dialogStage.setScene(new Scene(panel));
            DialogLoadingController controller = loader.getController();
            controller.setLabelText("Cargando");
            dialogStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirPasePermanente(String CI, Stage mainApp, ObservableList<Impresion> listToUpdate) {
        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("numero_identidad", CI);
                parameter.put("url_server", URL_SERVER);
                try{
                    JasperPrint print = JasperFillManager
                            .fillReport(PATH + "pase_impreso_permanente.jasper"
                                    , parameter, Conexion.getConnection());
                    view = new JasperViewer(print, false);
                    ServiceLocator.getRegistroImpresionesService().execNewOrUpdateImpressionRegister(CI);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if (Objects.nonNull(listToUpdate)) {
                    listToUpdate.clear();
                    listToUpdate.setAll(ServiceLocator.getImpresionService().getAllImpressions());
                }
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    @Override
    public void imprimirPaseEspecial(String CI, Stage mainApp, ObservableList<Impresion> listToUpdate) {
        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("numero_identidad", CI);
                parameter.put("url_server", URL_SERVER);
                try {
                    JasperPrint print = JasperFillManager.fillReport(PATH + "pase_impreso_especial.jasper", parameter, Conexion.getConnection());
                    view = new JasperViewer(print, false);
                    ServiceLocator.getRegistroImpresionesService().execNewOrUpdateImpressionRegister(CI);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if (Objects.nonNull(listToUpdate)) {
                    listToUpdate.clear();
                    listToUpdate.setAll(ServiceLocator.getImpresionService().getAllImpressions());
                }
                dialogStage.close();
                view.setVisible(true);
            }
        };
        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    @Override
    public void imprimirPaseProvisional(String CI, Stage mainApp, ObservableList<Impresion> listToUpdate) {
        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("numero_identidad", CI);
                parameter.put("url_server", URL_SERVER);
                try{
                    JasperPrint print = JasperFillManager.fillReport(PATH + "pase_impreso_provisional.jasper", parameter, Conexion.getConnection());
                    view = new JasperViewer(print, false);
                    ServiceLocator.getRegistroImpresionesService().execNewOrUpdateImpressionRegister(CI);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if (Objects.nonNull(listToUpdate)) {
                    listToUpdate.clear();
                    listToUpdate.setAll(ServiceLocator.getImpresionService().getAllImpressions());
                }
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    @Override
    public void imprimirPaseNegro(String CI, Stage mainApp, ObservableList<Impresion> listToUpdate) {
        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("numero_identidad", CI);
                parameter.put("url_server", URL_SERVER);
                try {
                    JasperPrint print = JasperFillManager.fillReport(PATH + "pase_impreso_negro.jasper", parameter, Conexion.getConnection());
                    view = new JasperViewer(print, false);
                    ServiceLocator.getRegistroImpresionesService().execNewOrUpdateImpressionRegister(CI);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if (Objects.nonNull(listToUpdate)) {
                    listToUpdate.clear();
                    listToUpdate.setAll(ServiceLocator.getImpresionService().getAllImpressions());
                }
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    @Override
    public void imprimirPasesEspecialesSelected(Stage mainApp, List<Impresion> impresions, ObservableList<Impresion> listToUpdate) {
        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("url_server", URL_SERVER);
                try{
                    updateTable(impresions);
                    JasperPrint print = JasperFillManager.fillReport(PATH + "pases_impresos_especiales.jasper", parameter, Conexion.getConnection());
                    view = new JasperViewer(print, false);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                if (Objects.nonNull(listToUpdate)) {
                    listToUpdate.clear();
                    listToUpdate.setAll(ServiceLocator.getImpresionService().getAllImpressions());
                }
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    @Override
    public void imprimirPasesNegrosSelected(Stage mainApp,List<Impresion> impresions, ObservableList<Impresion> listToUpdate) {
        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("url_server", URL_SERVER);
                try{
                    updateTable(impresions);
                    JasperPrint print = JasperFillManager.fillReport(PATH + "pases_impresos_negros.jasper", parameter, Conexion.getConnection());
                    view = new JasperViewer(print, false);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if (Objects.nonNull(listToUpdate)) {
                    listToUpdate.clear();
                    listToUpdate.setAll(ServiceLocator.getImpresionService().getAllImpressions());
                }
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();


    }

    @Override
    public void imprimirPasesPermanentesSelected(Stage mainApp,List<Impresion> impresions, ObservableList<Impresion> listToUpdate) {
        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("url_server", URL_SERVER);
                try{
                    updateTable(impresions);
                    JasperPrint print = JasperFillManager.fillReport(PATH + "pases_impresos_permanentes.jasper", parameter, Conexion.getConnection());
                    view = new JasperViewer(print, false);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if (Objects.nonNull(listToUpdate)) {
                    listToUpdate.clear();
                    listToUpdate.setAll(ServiceLocator.getImpresionService().getAllImpressions());
                }
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    @Override
    public void imprimirPasesProvisionalesSelected( Stage mainApp, List<Impresion> impresions, ObservableList<Impresion> listToUpdate) {
        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                updateTable(impresions);
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("url_server", URL_SERVER);
                try{
                    JasperPrint print = JasperFillManager.fillReport(PATH + "pases_impresos_provisionales.jasper", parameter, Conexion.getConnection());
                    view = new JasperViewer(print, false);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if (Objects.nonNull(listToUpdate)) {
                    listToUpdate.clear();
                    listToUpdate.setAll(ServiceLocator.getImpresionService().getAllImpressions());
                }
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    @Override
    public void imprimirResumenBajas(Stage mainApp) {
        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                try{
                    JasperPrint print = JasperFillManager
                            .fillReport(PATH + "resumen_pases_baja.jasper",null
                                    ,Conexion.getConnection());
                    view = new JasperViewer(print, false);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    private void updateTable(List<Impresion> impresionList)throws SQLException{
        ServiceLocator.getRegistroPaseService().deselectAllSelections();
        for (Impresion imp : impresionList) {
            ServiceLocator.getRegistroPaseService().updateSeleccionado(imp.getIdentidad());
            ServiceLocator.getRegistroImpresionesService().execNewOrUpdateImpressionRegister(imp.getIdentidad());
        }
    }

    @Override
    public void imprimirResumenFotosPendientes(Stage mainApp) {

        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                try{
                    JasperPrint print = JasperFillManager
                            .fillReport(PATH + "resumen_pases_pendiente_de_foto.jasper",null
                                    ,Conexion.getConnection());
                    view = new JasperViewer(print, false);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    @Override
    public void imprimirResumenGeneral(Stage mainApp) {
        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                try{
                    JasperPrint print = JasperFillManager
                            .fillReport(PATH + "resumen_general_pases.jasper",null
                                    ,Conexion.getConnection());
                    view = new JasperViewer(print, false);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    @Override
    public void imprimirResumenPasesImpresos(Stage mainApp) {
        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                try{
                    JasperPrint print = JasperFillManager
                            .fillReport(PATH + "resumen_pases_impresos.jasper",null
                                    ,Conexion.getConnection());
                    view = new JasperViewer(print, false);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    @Override
    public void imprimirResumenTipoPase(int tipoPase,Stage mainApp) {
        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                HashMap<String,Integer> parameter = new HashMap<>();
                parameter.put("id_pase",tipoPase);
                try{
                    JasperPrint print = JasperFillManager
                            .fillReport(PATH + "resumen_pases_segun_tipo.jasper",
                                    parameter,Conexion.getConnection());
                    view = new JasperViewer(print, false);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    @Override
    public void imprimirResumenUnidadOrganizativa(int id,Stage mainApp) {

        Task<Boolean> task = new Task<Boolean>() {
            JasperViewer view = null;
            @Override
            protected Boolean call() throws Exception {
                HashMap<String,Integer> parameter = new HashMap<>();
                parameter.put("id_uorg",id);
                try{
                    JasperPrint print = JasperFillManager
                            .fillReport(PATH + "resumen_pases_uorg.jasper",
                                    parameter,Conexion.getConnection());
                    view = new JasperViewer(print, false);
                }catch (JRException | SQLException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                dialogStage.close();
                view.setVisible(true);
            }
        };

        this.loadDialogLoading(mainApp);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

}
