package sistema_identificativo.views;

import com.gembox.internal.core.DivideByZeroException;
import icons.ImageLocation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.ExceptionDialog;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginViewController;
import services.ServiceLocator;
import sistema_identificativo.views.dialogs.AddPicturePendingToPass;
import sistema_identificativo.views.dialogs.DialogGenerarResumenPasesUO;
import util.PieChartUtils;
import util.Util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

public class MainSistemaIdentificativoController {

    private Stage mainApp;
    private BorderPane panelSistemaIdentificativo;
    private BorderPane panelGrande;

    @FXML
    private Label lblNombre;
    @FXML
    private Label lblImpresos;
    @FXML
    private Label lblRegistrados;
    @FXML
    private Menu menuArchivo;
    @FXML
    private MenuItem mnFotoPasePending;
    @FXML
    private PieChart pieChartRegistrados;
    @FXML
    private PieChart pieChartImpresos;

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
        this.loadPieChartPasesRegistrados();
        this.loadPieChartPasesImpresos();
    }

    private void userLoggedInfo() {
        this.lblNombre.setText(lblNombre.getText() + " " + logged.getNombre());
     //   this.lblUsername.setText(logged.getUsername());
      //  this.lblRol.setText(logged.getRol());
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
            try {
                dialogStage.getIcons().add(new Image(ImageLocation.class.getResource("icon_app.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
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
            dialogStage.setTitle("Añadir o cambiar foto a pases");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            try {
                dialogStage.getIcons().add(new Image(ImageLocation.class.getResource("icon_app.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
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
            try {
                dialogStage.getIcons().add(new Image(ImageLocation.class.getResource("icon_app.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
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
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainSistemaIdentificativoController.class.getResource("ImprimirPasesView.fxml"));
            AnchorPane pane = loader.load();
            //Create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Imprimir Pases");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.mainApp);
            try {
                dialogStage.getIcons().add(new Image(ImageLocation.class.getResource("icon_app.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
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
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainSistemaIdentificativoController.class.getResource("BajaPaseView.fxml"));
            AnchorPane pane = loader.load();
            //Create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dar baja pases");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            try {
                dialogStage.getIcons().add(new Image(ImageLocation.class.getResource("icon_app.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
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

    private void loadPieChartPasesRegistrados(){
        PieChartUtils pieChartUtils = new PieChartUtils(this.pieChartRegistrados);

        int totalPasesRegistrados = ServiceLocator.getRegistroPaseService().countAllPasesRegistrados();

        if(totalPasesRegistrados > 0){
            int pases_permanentes = ServiceLocator.getRegistroPaseService()
                    .cantPasesRegistradosByTipoPase(1);
            int pases_provisionales = ServiceLocator.getRegistroPaseService()
                    .cantPasesRegistradosByTipoPase(2);
            int pases_especiales = ServiceLocator.getRegistroPaseService()
                    .cantPasesRegistradosByTipoPase(3);
            int pases_negros = ServiceLocator.getRegistroPaseService()
                    .cantPasesRegistradosByTipoPase(4);

            try{
                pieChartUtils.addData("Permanente",
                        Util.getPercent(pases_permanentes, totalPasesRegistrados));
                pieChartUtils.addData("Provisional",
                        Util.getPercent(pases_provisionales, totalPasesRegistrados));
                pieChartUtils.addData("Especial",
                        Util.getPercent(pases_especiales, totalPasesRegistrados));
                pieChartUtils.addData("Negro",
                        Util.getPercent(pases_negros, totalPasesRegistrados));
            } catch (DivideByZeroException e) {
                Util.dialogResult("Ocurrió un error. División por 0.", Alert.AlertType.ERROR);
            }

            pieChartUtils.showChart();



            pieChartUtils.setMarkVisible(true);

            HashMap<Integer, String> colors = new HashMap<>();
            colors.put(0, "darkblue");
            colors.put(1, "darkorange");
            colors.put(2, "green");
            colors.put(3, "black");

            pieChartUtils.setLegendColor(colors);

            this.pieChartRegistrados.setLabelsVisible(false);
            pieChartUtils.setLegendSide("Bottom");
            this.lblRegistrados.setTextFill(Color.WHITE);


            for (PieChart.Data data : this.pieChartRegistrados.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
                    this.lblRegistrados.setTranslateX(e.getX() - 30);
                    this.lblRegistrados.setTranslateY(e.getY());
                    String text = String.format("%.1f%%", data.getPieValue());
                    this.lblRegistrados.setText(data.getName() + " " + text);
                });
            }
            pieChartRegistrados.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                this.lblRegistrados.setText("");
            });
        }
    }

    private void loadPieChartPasesImpresos(){
        PieChartUtils pieChartUtils = new PieChartUtils(this.pieChartImpresos);

        int totalPasesImpresos = ServiceLocator.getRegistroImpresionesService().countAllPasesImpresos();

        if(totalPasesImpresos > 0){
            int pases_permanentes = ServiceLocator.getRegistroImpresionesService()
                    .contPasesImpresosTipoPase(1);
            int pases_provisionales = ServiceLocator.getRegistroImpresionesService()
                    .contPasesImpresosTipoPase(2);
            int pases_especiales = ServiceLocator.getRegistroImpresionesService()
                    .contPasesImpresosTipoPase(3);
            int pases_negros = ServiceLocator.getRegistroImpresionesService()
                    .contPasesImpresosTipoPase(4);

            try{
                pieChartUtils.addData("Permanente",
                        Util.getPercent(pases_permanentes, totalPasesImpresos));
                pieChartUtils.addData("Provisional",
                        Util.getPercent(pases_provisionales, totalPasesImpresos));
                pieChartUtils.addData("Especial",
                        Util.getPercent(pases_especiales, totalPasesImpresos));
                pieChartUtils.addData("Negro",
                        Util.getPercent(pases_negros, totalPasesImpresos));
            } catch (DivideByZeroException e) {
                Util.dialogResult("Ocurrió un error. División por 0.", Alert.AlertType.ERROR);
            }

            pieChartUtils.showChart();

            pieChartUtils.setDataColor(0, "darkblue");
            pieChartUtils.setDataColor(1, "darkorange");
            pieChartUtils.setDataColor(2, "green");
            pieChartUtils.setDataColor(3, "black");

            pieChartUtils.setMarkVisible(true);

            HashMap<Integer, String> colors = new HashMap<>();
            colors.put(0, "darkblue");
            colors.put(1, "darkorange");
            colors.put(2, "green");
            colors.put(3, "black");

            pieChartUtils.setLegendColor(colors);
            pieChartUtils.setLegendSide("Bottom");

            this.lblImpresos.setTextFill(Color.WHITE);
            this.pieChartImpresos.setLabelsVisible(false);
            for (PieChart.Data data : this.pieChartImpresos.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
                    this.lblImpresos.setTranslateX(e.getX() - 30);
                    this.lblImpresos.setTranslateY(e.getY());
                    String text = String.format("%.1f%%", data.getPieValue());
                    this.lblImpresos.setText(data.getName() + " " + text);
                });
            }
            this.pieChartImpresos.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                this.lblImpresos.setText("");
            });
        }
    }
}
