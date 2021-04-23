package views.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.TipoMateriales;
import services.ServiceLocator;
import util.Util;

import java.sql.SQLException;

public class TipoMaterialDialogController {

    @FXML
    private JFXTextField tipoMaterialField;
    @FXML
    private Label variableLabel;
    @FXML
    private JFXButton okButton;

    private Stage dialogStage;
    private Boolean isEdition;
    private TipoMateriales tipoMateriales;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEdition(Boolean edition) {
        isEdition = edition;
    }

    public void setTipoMateriales(TipoMateriales tipoMateriales) {
        this.tipoMateriales = tipoMateriales;
        if (tipoMateriales!=null)
            tipoMaterialField.setText(tipoMateriales.getMateriales());
    }

    public Label getVariableLabel() {
        return variableLabel;
    }

    public JFXButton getOkButton() {
        return okButton;
    }

    @FXML
    private void initialize(){

    }

    public TipoMaterialDialogController() {
    }

    @FXML
    private void handleCancel(){
        dialogStage.close();
    }

    @FXML
    private void handleOk(){
        if (isInputValid()){
            if (isEdition){
            }
            else {
                boolean insertado = false;
                int cantIntentos = 0;
                while (!insertado&&cantIntentos<1000){
                    insertado = insertar();
                    cantIntentos++;
                }
                if (insertado){
                    Util.dialogResult("Insercion exitosa", Alert.AlertType.INFORMATION);
                    dialogStage.close();
                }
                else
                    Util.dialogResult("Fallo al insertar", Alert.AlertType.ERROR);
            }
        }else
            showDialog(false);
    }

    private boolean isInputValid() {
        String errorMessage = "";
        boolean result = false;

        if (tipoMaterialField.getText() == null || tipoMaterialField.getText().length() == 0) {
            errorMessage += "Revise lo introducido!\n";
        }

        if (errorMessage.length() == 0) {
            result = true;
        } else {
            // Show the error message.
        }

        return result;
    }

    private boolean insertar(){
        boolean result = true;
        try {
            tipoMateriales = new TipoMateriales(tipoMaterialField.getText());
            ServiceLocator.getTipoMaterialesService().insertarTipoMaterial(tipoMateriales);
        }catch (SQLException e){
            result = false;
        }
        return result;
    }

    public void showDialog(boolean result){
        if(result){
            Util.dialogResult("Exito", Alert.AlertType.INFORMATION);
        }else{
            Util.dialogResult("Invalid fields", Alert.AlertType.ERROR);
        }
    }
}
