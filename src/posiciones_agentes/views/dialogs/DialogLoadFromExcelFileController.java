package posiciones_agentes.views.dialogs;

import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import posiciones_agentes.models.RegistroPosicionesAgentes;
import posiciones_agentes.utils.ProcessinExcelPositions;
import services.ServiceLocator;
import util.Util;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class DialogLoadFromExcelFileController {

    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private ComboBox<String> stringComboBox;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        this.progressBar.setVisible(false);
        this.stringComboBox.getItems().addAll(ServiceLocator
                .getUnidadOrganizativaService()
                .fetchAll()
                .stream()
                .map(UnidadOrganizativa::getUnidad_organizativa)
                .collect(Collectors.toList())
        );

        TextFields.bindAutoCompletion(this.stringComboBox.getEditor(), this.stringComboBox.getItems());

    }

    @FXML
    private void loadAndSaveData() {
        if (!this.stringComboBox.getSelectionModel().isEmpty()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files", "*.*"),
                    new FileChooser.ExtensionFilter("Excel Files 2007 And High", "*.xlsx"),
                    new FileChooser.ExtensionFilter("Excel Files 97-2003", "*.xls")

            );
            File excelFile = fileChooser.showOpenDialog(this.dialogStage);

            UnidadOrganizativa unidadOrganizativa = ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName(
                    this.stringComboBox.getValue()
            );
            List<RegistroPosicionesAgentes> data = ProcessinExcelPositions.getDataFromExcel(excelFile, unidadOrganizativa);
            for (RegistroPosicionesAgentes posicionesAgentes : data) {
                ServiceLocator.getRegistroPosicionesAgentesService().registerRegisterPosicionesAgentes(posicionesAgentes);
            }
            Util.dialogResult("Finalizado con exito", Alert.AlertType.INFORMATION);
        } else {
            Util.dialogResult("Seleccione una unidad organizativa", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleCancelar() {
        this.dialogStage.close();
    }
}
