package views.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.AveriasPext;
import services.ServiceLocator;
import util.Util;

import java.sql.SQLException;

public class TipoDeAveriaDialogController {

    @FXML
    private JFXTextField tipoAveriaField;
    @FXML
    private Label variableLabel;
    @FXML
    private JFXButton okButton;

    private AveriasPext averiasPext;
    private Stage stage;
    private Boolean isEdition;

    public void setEdition(Boolean edition) {
        isEdition = edition;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAveriasPext(AveriasPext averiasPext) {
        this.averiasPext = averiasPext;
        if (averiasPext!=null)
            tipoAveriaField.setText(averiasPext.getCausa());
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

    public TipoDeAveriaDialogController() {
    }

    @FXML
    private void handleCancel(){
        stage.close();
    }

    @FXML
    private void handleOk(){
        if (isInputValid()){
            if(isEdition){
            }
            else {
                boolean insertado = false;
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

    private boolean isInputValid() {
        String errorMessage = "";
        boolean result = false;

        if (tipoAveriaField.getText() == null || tipoAveriaField.getText().length() == 0)
            errorMessage += "Revise lo introducido!\n";

        if (errorMessage.length() == 0)
            result = true;
        else {
            //Show the error message
        }

        return result;
    }

    private boolean insertar(){
        boolean result =true;
        try {
            averiasPext= new AveriasPext(tipoAveriaField.getText());
            ServiceLocator.getAveriasPExtService().insertarTipoAveria(averiasPext);
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
