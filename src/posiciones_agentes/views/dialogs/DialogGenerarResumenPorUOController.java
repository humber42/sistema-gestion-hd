package posiciones_agentes.views.dialogs;

import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import posiciones_agentes.excels_generators.ExcelGeneratorLocator;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;
import java.util.stream.Collectors;

public class DialogGenerarResumenPorUOController {

    @FXML
    private ComboBox<String> unidadOrganizativa;
    @FXML
    private ProgressBar loading;

    @FXML
    private JFXButton generar;
    @FXML
    private JFXButton cancelar;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize(){
        unidadOrganizativa.getItems()
                .setAll(ServiceLocator.getUnidadOrganizativaService()
                        .fetchAll()
                        .stream()
                        .map(UnidadOrganizativa::getUnidad_organizativa)
                        .collect(Collectors.toList()
                        ));
        TextFields.bindAutoCompletion(this.unidadOrganizativa.getEditor(),this.unidadOrganizativa.getItems());
        this.loading.setVisible(false);
    }

    @FXML
    private void generarResumenXUOrg(){
        try{
            String unidadOrganizativa = this.unidadOrganizativa.getValue();
            if(unidadOrganizativa.equals("")){
                Util.dialogResult("Seleccione una unidad organizativa", Alert.AlertType.ERROR);
            } else {
                this.loading.setVisible(true);
                this.generar.setDisable(true);
                this.cancelar.setDisable(true);
                Task<Boolean> task = new Task<Boolean>() {
                    boolean result = false;
                    String file = "src/informesGenerados/ResumenPosiciones" + unidadOrganizativa + ".xlsx";

                    @Override
                    protected Boolean call() throws Exception {
                        this.result = ExcelGeneratorLocator.getResumenUnidadOrganizativa().generarResumenUnidadOrganizativa(file,
                                ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName(unidadOrganizativa));
                        return result;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        loading.setVisible(false);
                        generar.setDisable(false);
                        cancelar.setDisable(false);
                        try {
                            Runtime.getRuntime().exec("cmd /c start " + file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Util.showDialog(result);
                    }
                };
                new Thread(task).start();
            }
        }catch (NullPointerException e){
            Util.dialogResult("Seleccione una unidad organizativa", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelar(){
        this.dialogStage.close();
    }

}
