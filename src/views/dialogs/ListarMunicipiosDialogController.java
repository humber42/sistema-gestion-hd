package views.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Municipio;
import services.ServiceLocator;
import util.Util;

import java.sql.SQLException;

public class ListarMunicipiosDialogController {

    @FXML
    private JFXTextField municipioField;

    @FXML
    private Label variableLabel;

    @FXML
    private JFXButton okButton;

    private Stage stage;
    private Boolean isEdition;
    private Municipio municipio;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setEdition(Boolean edition) {
        isEdition = edition;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
        if (this.municipio!=null)
            municipioField.setText(municipio.getMunicipio());
    }

    public Label getVariableLabel() {
        return variableLabel;
    }

    public JFXButton getOkButton() {
        return okButton;
    }

    @FXML
    public void initialize(){

    }

    public ListarMunicipiosDialogController() {
    }

    @FXML
    private void handleCancel(){stage.close();}

    @FXML
    private void handleOk(){
        if (isInputValid()){
            if (isEdition){
            }
            else {
                boolean insertado =false;
                int cantIntentos = 0;
                while (!insertado && cantIntentos < 1000) {
                    insertado = insertar();
                    cantIntentos++;
                }
                if (insertado){
                    Util.dialogResult("Insercion exitosa", Alert.AlertType.INFORMATION);
                    stage.close();
                }
                else
                    Util.dialogResult("Error al insertar", Alert.AlertType.ERROR);
            }
        }
        else
            showDialog(false);
    }

    private boolean isInputValid(){
        String errorMessage ="";
        boolean result = false;

        if (municipioField.getText()==null || municipioField.getText().length()==0)
            errorMessage +="Revise lo introducido!\n";
        if (errorMessage.length()==0) {
            result = true;
        }
        else {
            // show de error message
        }
        return result;
    }

    private boolean insertar(){
        boolean result = true;
        try {
            municipio = new Municipio(municipioField.getText());
            ServiceLocator.getMunicipiosService().insertarMunicipio(municipio);
        }catch (SQLException e){
            result = false;
        }
        return result;
    }

    public void showDialog(boolean result){
        if (result)
            Util.dialogResult("Exito", Alert.AlertType.INFORMATION);
        else
            Util.dialogResult("Revise los campos", Alert.AlertType.ERROR);
    }
}
