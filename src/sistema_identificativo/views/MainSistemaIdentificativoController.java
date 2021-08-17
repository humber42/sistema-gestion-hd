package sistema_identificativo.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.ExceptionDialog;
import sistema_identificativo.views.dialogs.AddPicturePendingToPass;

import java.io.IOException;

public class MainSistemaIdentificativoController {

    private Stage mainApp;
    private BorderPane panelSistemaIdentificativo;
    private BorderPane panelGrande;

    public void setPanelGrande(BorderPane panelGrande) {
        this.panelGrande = panelGrande;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleCerrar() {
        this.panelGrande.setCenter(null);
    }

    public void setMainApp(Stage mainApp) {
        this.mainApp = mainApp;
    }

    public void registrarPase() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainSistemaIdentificativoController.class.getResource("../views/RegistroPasesView.fxml"));
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
            loader.setLocation(MainSistemaIdentificativoController.class.getResource("../views/dialogs/AddPicturePendingToPass.fxml"));
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

    public void setPanelSistemaIdentificativo(BorderPane pane) {
        this.panelSistemaIdentificativo = pane;
    }


}
