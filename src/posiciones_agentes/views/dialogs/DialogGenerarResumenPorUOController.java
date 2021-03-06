package posiciones_agentes.views.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
    private JFXProgressBar loading;

    @FXML
    private JFXButton generar;
    @FXML
    private JFXButton cancelar;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        unidadOrganizativa.getItems()
                .setAll(ServiceLocator.getUnidadOrganizativaService()
                        .fetchAll()
                        .stream()
                        .map(UnidadOrganizativa::getUnidad_organizativa)
                        .collect(Collectors.toList()
                        ));
        TextFields.bindAutoCompletion(this.unidadOrganizativa.getEditor(), this.unidadOrganizativa.getItems());
        this.loading.setVisible(false);
    }

    @FXML
    private void generarResumenXUOrg() {
        if (this.unidadOrganizativa.getSelectionModel().isEmpty()) {
            Util.dialogResult("Seleccione una unidad organizativa", Alert.AlertType.WARNING);
        } else {
            String path = Util.selectPathToSaveDatabase(this.dialogStage);
            if (path != null) {
                try {
                    String unidadOrganizativa = this.unidadOrganizativa.getValue();
                    if (unidadOrganizativa.equals("")) {
                        Util.dialogResult("Seleccione una unidad organizativa", Alert.AlertType.ERROR);
                    } else {
                        this.loading.setVisible(true);
                        this.generar.setDisable(true);
                        this.cancelar.setDisable(true);
                        Task<Boolean> task = new Task<Boolean>() {
                            boolean result = false;
                            String file = path + "/ResumenPosiciones" + unidadOrganizativa + ".xlsx";

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
                } catch (NullPointerException e) {
                    Util.dialogResult("Seleccione una unidad organizativa", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void cancelar() {
        this.dialogStage.close();
    }

}
