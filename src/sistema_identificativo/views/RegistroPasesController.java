package sistema_identificativo.views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import services.ServiceLocator;
import sistema_identificativo.models.CodigoPase;
import sistema_identificativo.models.TipoPase;

import java.util.stream.Collectors;

public class RegistroPasesController {

    private Stage dialogStage;
    @FXML
    private JFXTextField nameAndLastName;
    @FXML
    private JFXTextField identificationPass;
    @FXML
    private JFXTextField passNumber;
    @FXML
    private JFXComboBox<String> passType;
    @FXML
    private JFXComboBox<String> passCategory;
    @FXML
    private JFXCheckBox passLow;
    @FXML
    private JFXComboBox<String> organUnity;
    @FXML
    private JFXDatePicker valityDate;
    @FXML
    private TextArea access;
    @FXML
    private TextArea observations;
    @FXML
    private ImageView profilePhoto;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize(){

        this.organUnity.getItems().setAll(ServiceLocator
                .getUnidadOrganizativaService().fetchAll().stream().map(UnidadOrganizativa::getUnidad_organizativa)
                .collect(Collectors.toList())
        );
        this.organUnity.setEditable(true);

        TextFields.bindAutoCompletion(this.organUnity.getEditor(),this.organUnity.getItems());

        this.passType.getItems().setAll(ServiceLocator.getTipoPaseService()
        .getAllTipoPase().stream().map(TipoPase::getTipoPase).collect(Collectors.toList())
        );
        this.passCategory.getItems().setAll(ServiceLocator
            .getCodigoPaseService().getAllCodigo().stream().map(CodigoPase::getCodigo).collect(Collectors.toList())
        );
    }

    @FXML
    private void closeOrCancelDialog(){
        this.dialogStage.close();
    }

    @FXML
    private void registerPass(){

    }
}
