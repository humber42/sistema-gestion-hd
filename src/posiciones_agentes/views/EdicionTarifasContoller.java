package posiciones_agentes.views;

import com.jfoenix.controls.JFXTextField;
import icons.ImageLocation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import posiciones_agentes.models.ProveedorServicio;
import posiciones_agentes.models.TarifasPosicionAgente;
import services.ServiceLocator;
import util.Util;

import java.io.File;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class EdicionTarifasContoller {

    @FXML
    private ComboBox<String> unidadesOrganizativas;
    @FXML
    private ComboBox<String> proveedores;
  //  @FXML
  // private Spinner<Double> tarifa;
    @FXML
    private ImageView imageEdit;
    @FXML
    private ImageView imageCancel;
    @FXML
    private JFXTextField txtTarifa;

    private Stage dialogStage;

    private Boolean activeSpinner;

    private TarifasPosicionAgente tp;

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize(){
        this.activeSpinner = false;

        this.txtTarifa.setOnKeyTyped(event ->
                this.validTarifaValue(event, this.txtTarifa));

        this.imageEdit.setOnMouseClicked(event ->
                this.enableSpinnerTarifa())
        ;
        this.imageCancel.setOnMouseClicked(event ->
                this.disableSpinnerTarifa());

        this.proveedores.getItems().setAll(
                ServiceLocator.getProveedorServicioService()
                .getAllProveedorServicio()
                .stream()
                .map(ProveedorServicio::getProveedorServicio)
                .collect(Collectors.toList())
        );
        this.unidadesOrganizativas.getItems().setAll(
                ServiceLocator.getUnidadOrganizativaService()
                        .fetchAll()
                        .stream()
                        .map(UnidadOrganizativa::getUnidad_organizativa)
                        .collect(Collectors.toList())
        );
        TextFields.bindAutoCompletion(this.unidadesOrganizativas.getEditor(),this.unidadesOrganizativas.getItems());
        //setValueOnSpinnerTarifas(0);
    }

    /*private void setValueOnSpinnerTarifas(double value){
        this.tarifa.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE,value,1));
    }*/

    private void setValueOnTxtTarifa(double value){
        this.txtTarifa.setText(value+"");
    }

    private void enableSpinnerTarifa(){
        if(activeSpinner){
            if(validateFields()) {
                String uorg = this.unidadesOrganizativas.getSelectionModel().getSelectedItem();
                String prov = this.proveedores.getSelectionModel().getSelectedItem();
                executeCheck(uorg,prov);
            }
        }
        else {
           // File file = new File("../icons/Checkmark_48px.png");
            activeSpinner = true;
            this.txtTarifa.setDisable(false);
            this.imageCancel.setVisible(true);
            this.imageEdit.setImage(new Image(ImageLocation.class.getResource("Checkmark_48px.png").toExternalForm()));
        }
    }

    private void disableSpinnerTarifa(){
        activeSpinner = false;
        this.txtTarifa.setDisable(true);
        //this.txtTarifa.setEditable(false);
        this.imageCancel.setVisible(false);

        //File file = new File("../icons/Edit_48px.png");
        this.imageEdit.setImage(new Image(ImageLocation.class.getResource("Edit_48px.png").toExternalForm()));

        cleanFields();
    }

    private void cleanFields(){
        this.proveedores.getSelectionModel().clearSelection();
        this.unidadesOrganizativas.getSelectionModel().clearSelection();
        this.txtTarifa.setText(null);
        //setValueOnSpinnerTarifas(0);
    }

    @FXML
    private void handleFinish(){
        this.dialogStage.close();
    }

    @FXML
    private void loadValueOnSpinnerTarifa(){
        if(!this.unidadesOrganizativas.getSelectionModel().isEmpty() &&
            !this.proveedores.getSelectionModel().isEmpty()){
            String uorg = this.unidadesOrganizativas.getSelectionModel().getSelectedItem();
            UnidadOrganizativa uo = ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName(uorg);
            if(uo != null){
                String proveedor = this.proveedores.getSelectionModel().getSelectedItem();
                ProveedorServicio proveedorServicio = ServiceLocator.getProveedorServicioService().getByName(proveedor);
                if(proveedorServicio != null) {
                    TarifasPosicionAgente tarifasPosicionAgente = ServiceLocator
                               .getTarifaPosicionAgenteService().getTarifaByUoAndProv(uo.getId_unidad_organizativa(),
                                       proveedorServicio.getId());
                    if(tarifasPosicionAgente != null) {
                        double valueTarifa = tarifasPosicionAgente.getTarifa();
                        setValueOnTxtTarifa(valueTarifa);
                        tp = tarifasPosicionAgente;
                    }
                    else{
                        //setValueOnTxtTarifa(0.00);
                        tp = null;
                    }
                }
            }
        }
    }

    private boolean validateFields(){
        boolean valid = false;
        if(!this.unidadesOrganizativas.getSelectionModel().isEmpty()
                && this.proveedores.getSelectionModel().isEmpty()){
            Util.dialogResult("Debe seleccionar un proveedor de servicios.", Alert.AlertType.WARNING);
        }
        else if(this.unidadesOrganizativas.getSelectionModel().isEmpty()
                && !this.proveedores.getSelectionModel().isEmpty()){
            Util.dialogResult("Debe seleccionar una unidad organizativa.", Alert.AlertType.WARNING);
        }
        else if(this.unidadesOrganizativas.getSelectionModel().isEmpty()
                && this.proveedores.getSelectionModel().isEmpty())
            Util.dialogResult("Debe seleccionar una unidad organizativa y un proveedor de servicios.", Alert.AlertType.WARNING);
        else
            valid = true;

        return valid;
    }

    private void executeCheck(String uorg, String prov){
        UnidadOrganizativa uo = ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName(uorg);
        if(uo != null){
            ProveedorServicio ps = ServiceLocator.getProveedorServicioService().getByName(prov);
            if(ps != null) {
                String stringValue = this.txtTarifa.getText();
                if (!stringValue.isEmpty()) {
                    try {
                        double tarifaValue = Double.parseDouble(stringValue);
                        if (tp != null) {
                            tp.setTarifa(tarifaValue);
                            ServiceLocator.getTarifaPosicionAgenteService().updateTarifa(tp);
                            Util.dialogResult("Tarifa modificada correctamente", Alert.AlertType.INFORMATION);
                        } else {
                            tp = new TarifasPosicionAgente(0, uo, ps, tarifaValue);
                            ServiceLocator.getTarifaPosicionAgenteService().registerTarifa(tp);
                            Util.dialogResult("Tarifa registrada correctamente", Alert.AlertType.INFORMATION);
                            tp.setIdTarifa(ServiceLocator.getTarifaPosicionAgenteService()
                                    .getTarifaByUoAndProv(uo.getId_unidad_organizativa(), ps.getId()).getIdTarifa());
                        }
                    } catch (NumberFormatException e) {
                        Util.dialogResult("El valor de tarifa no puede tener más de un punto.", Alert.AlertType.ERROR);
                    }
                }
                else{
                    Util.dialogResult("Tarifa vacía.", Alert.AlertType.WARNING);
                }
            }
        }
    }

    private void validTarifaValue(KeyEvent event, JFXTextField txt){
        String tarifa = txt.getText();

        if(tarifa.isEmpty()){
            Util.dialogResult("Tarifa vacía.", Alert.AlertType.WARNING);
        }
        else if((tarifa.toCharArray()[tarifa.length()-1] < '0'
                ||  tarifa.toCharArray()[tarifa.length()-1] > '9')
                && (tarifa.toCharArray()[tarifa.length()-1] < '.'
                || tarifa.toCharArray()[tarifa.length()-1] > '.')){
            event.consume();
            txt.setText(tarifa.substring(0, tarifa.length()-1));
            txt.end();
            Util.dialogResult("El campo de tarifa solo admite valores numéricos y el caracter '.'", Alert.AlertType.INFORMATION);
        }
    }
}
