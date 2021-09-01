package posiciones_agentes.views;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import posiciones_agentes.models.ProveedorServicio;
import posiciones_agentes.models.RegistroPosicionesAgentes;
import services.ServiceLocator;
import util.Util;

import java.util.stream.Collectors;

public class RegistrarPosicionesAgentesController {

    @FXML
    private ComboBox<String> unidadOrgananizativa;
    @FXML
    private ComboBox<String> proveedoresServicio;
    @FXML
    private Spinner<Integer> horasDL;
    @FXML
    private Spinner<Integer> horasNL;
    @FXML
    private Spinner<Integer> cantEfectivo;
    @FXML
    private TextArea instalacion;


    private Stage dialogStage;


    public void setDialogStage(Stage dialogStage){
        this.dialogStage= dialogStage;

    }

    @FXML
    public void initialize(){
        this.proveedoresServicio.getItems().setAll(
                ServiceLocator.getProveedorServicioService()
                .getAllProveedorServicio()
                .stream()
                .map(ProveedorServicio::getProveedorServicio)
                .collect(Collectors.toList())
        );

        this.unidadOrgananizativa.getItems().setAll(
                ServiceLocator.getUnidadOrganizativaService()
                        .fetchAll()
                        .stream()
                        .map(UnidadOrganizativa::getUnidad_organizativa)
                        .collect(Collectors.toList())
        );
        TextFields.bindAutoCompletion(this.unidadOrgananizativa.getEditor(),this.unidadOrgananizativa.getItems());
        this.horasNL.setEditable(false);
        this.horasDL.setEditable(false);
        this.cantEfectivo.setEditable(false);
        this.horasDL.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,24,1));
        this.horasNL.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,24,1));
        this.cantEfectivo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100000,1));
    }

    @FXML
    private void cancelar(){
        this.dialogStage.close();
    }

    private boolean emptyFields(){
        boolean emptyFields = false;
        try{
        if(this.unidadOrgananizativa.getValue().isEmpty()){
            emptyFields= true;
        }
        else if(this.proveedoresServicio.getValue().isEmpty()){
            emptyFields = true;
        }
        else if(this.cantEfectivo.getValue() == null){
            emptyFields=true;
        }
        else if(this.horasDL.getValue() == null){
            emptyFields=true;
        }
        else if(this.horasNL.getValue() == null){
            emptyFields=true;
        }
        else if(this.instalacion.getText().isEmpty()){
            emptyFields = true;
        }
        }catch (NullPointerException e){
            emptyFields=true;
        }
        return emptyFields;
    }

    @FXML
    private void registrar(){
        if(!emptyFields()){
            UnidadOrganizativa unidadOrganizativa = ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName(this.unidadOrgananizativa.getValue());
            ProveedorServicio proveedorServicio = ServiceLocator.getProveedorServicioService().getByName(this.proveedoresServicio.getValue());
            RegistroPosicionesAgentes registroPosicionesAgentes = new RegistroPosicionesAgentes(
                    0,
                    this.instalacion.getText()
                    ,unidadOrganizativa,
                    proveedorServicio,
                    this.horasDL.getValue(),
                    this.horasNL.getValue(),
                    this.cantEfectivo.getValue());
            registroPosicionesAgentes.register();
        }
        else{
            Util.dialogResult("Hay campos vacíos", Alert.AlertType.ERROR);
        }
    }
}
