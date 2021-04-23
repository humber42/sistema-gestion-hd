package views.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.UnidadOrganizativa;
import services.ServiceLocator;
import util.Util;

import java.sql.SQLException;


public class UnidadOrgEditDialogController {

    @FXML
    private JFXTextField unidadOrgField;

    @FXML
    private Label variableLAbel;

    @FXML
    private JFXButton okButton;

    private Stage dialogStage;
    private Boolean isEdition;
    private UnidadOrganizativa unidadOrganizativa;

    public void setDialogStage(Stage stage){
        this.dialogStage = stage;
    }

    public void setEdition(Boolean edition) {
        isEdition = edition;
    }

    public void setUnidadOrganizativa(UnidadOrganizativa unidadOrganizativa) {
        this.unidadOrganizativa = unidadOrganizativa;
        if(this.unidadOrganizativa!=null)
            unidadOrgField.setText(unidadOrganizativa.getUnidad_organizativa());
    }

    public JFXButton getOkButton(){
        return okButton;
    }

    public Label getVariableLAbel() {
        return variableLAbel;
    }

    @FXML
    private void initialize() {

    }




    public UnidadOrgEditDialogController(){

    }

    @FXML
    private void handleCancel(){
        dialogStage.close();
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            if(isEdition){

            }
            else{
                boolean insertado = false;
                int cantIntentos = 0;
                while (!insertado&&cantIntentos<1000){
                    insertado = insertar();
                    cantIntentos++;
                }

                if(insertado){
                    Util.dialogResult("Insercion exitosa", Alert.AlertType.INFORMATION);
                    dialogStage.close();
                }else
                    Util.dialogResult("Fallo al insertar", Alert.AlertType.ERROR);

            }
        }else
            showDialog(false);
    }

    private boolean insertar(){
        boolean result = true;
        try {
            unidadOrganizativa = new UnidadOrganizativa(unidadOrgField.getText());
            ServiceLocator.getUnidadOrganizativaService().insertarUnidadOrganizativa(unidadOrganizativa);
        }catch (SQLException e){
            result = false;
        }
        return result;
    }
    private boolean isInputValid() {
        String errorMessage = "";
        boolean result = false;

        if (unidadOrgField.getText() == null || unidadOrgField.getText().length() == 0) {
            errorMessage += "Revise lo introducido!\n";
        }

        if (errorMessage.length() == 0) {
            result = true;
        } else {
            // Show the error message.
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
