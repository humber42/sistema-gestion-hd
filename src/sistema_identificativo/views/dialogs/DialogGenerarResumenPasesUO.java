package sistema_identificativo.views.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import services.ServiceLocator;
import util.Util;

import java.util.stream.Collectors;

public class DialogGenerarResumenPasesUO {

    @FXML
    private ComboBox<String> unidadOrganizativa;

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
    }

    @FXML
    private void generarPase(){
        int idUOrg= ServiceLocator.getUnidadOrganizativaService()
                .searchUnidadOrganizativaByName(unidadOrganizativa.getValue()).getId_unidad_organizativa();
        ServiceLocator.getJasperReportService().imprimirResumenUnidadOrganizativa(idUOrg,dialogStage);
    }

    @FXML
    private void cancelar(){
        this.dialogStage.close();
    }

}
