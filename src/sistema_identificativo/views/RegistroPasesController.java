package sistema_identificativo.views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import services.ServiceLocator;
import sistema_identificativo.models.CodigoPase;
import sistema_identificativo.models.TipoPase;


import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.util.StringUtils;
import services.ServiceLocator;
import sistema_identificativo.models.CodigoPase;
import sistema_identificativo.models.RegistroPase;
import sistema_identificativo.models.TipoPase;
import util.ConfigProperties;
import util.PostFile;
import util.Util;
import views.dialogs.DialogLoadingController;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RegistroPasesController {

    private Stage dialogStage;


    private Stage dialogExecuting;

    @FXML
    private JFXTextField nameAndLastName;
    @FXML
    private JFXTextField identificationPass;
    @FXML
    private JFXTextField passNumber;
    @FXML
    private JFXComboBox<String> passType;
    @FXML
    private Label passTypeLabel;
    @FXML
    private Label passCategoryLabel;
    @FXML
    private JFXComboBox<String> passCategory;
    @FXML
    private JFXCheckBox passLow;
    @FXML
    private JFXComboBox<String> organUnity;
    @FXML
    private Label unidadOrganizativa;
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
    private void initialize() {

        this.nameAndLastName.setOnKeyTyped(
                event->Util.eventToSetUpperCaseToFirstNameAndLastName(event,this.nameAndLastName)
        );

        this.organUnity.getItems().setAll(ServiceLocator
                .getUnidadOrganizativaService().fetchAll().stream().map(UnidadOrganizativa::getUnidad_organizativa)
                .collect(Collectors.toList())
        );
        this.organUnity.setEditable(true);
        TextFields.bindAutoCompletion(this.organUnity.getEditor(), this.organUnity.getItems());

        this.passType.getItems().setAll(ServiceLocator.getTipoPaseService().getAllTipoPase().stream().map(TipoPase::getTipoPase).collect(Collectors.toList())
        );
        this.passCategory.getItems().setAll(ServiceLocator
                .getCodigoPaseService().getAllCodigo().stream().map(CodigoPase::getCodigo).collect(Collectors.toList())
        );

        this.identificationPass.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                this.eventUnFocusIdentificationPass();
            }
        });


        this.passType.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    if (!newValue.isEmpty()) {
                        if (!this.passCategory.getSelectionModel().isEmpty()) {
                            this.passNumber.setText(ServiceLocator.getRegistroPaseService().ultimoRegisroPase(
                                    this.passType.getSelectionModel().getSelectedItem(),
                                    this.passCategory.getSelectionModel().getSelectedItem()
                            ));
                        }
                    }
                })
        );

        this.passCategory.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!this.passType.getSelectionModel().isEmpty()) {
                    this.passNumber.setText(ServiceLocator.getRegistroPaseService().ultimoRegisroPase(
                            this.passType.getSelectionModel().getSelectedItem(),
                            this.passCategory.getSelectionModel().getSelectedItem()
                    ));
                }
            }
        }));
    }


    private boolean valityEmptyFields() {
        boolean emptyFields = false;
        if (this.nameAndLastName.getText().isEmpty()) {
            emptyFields = true;
        }
        if (this.identificationPass.getText().isEmpty()) {
            emptyFields = true;
        }

        try {
            if (this.passType.getValue().isEmpty()) {
                emptyFields = true;
                this.passTypeLabel.setTextFill(Color.RED);
            }
        } catch (NullPointerException e) {
            emptyFields = true;
            this.passTypeLabel.setTextFill(Color.RED);
        }

        try {
            if (this.passCategory.getValue().isEmpty()) {
                emptyFields = true;
                this.passCategoryLabel.setTextFill(Color.RED);
            }
        } catch (NullPointerException e) {
            emptyFields = true;
            this.passCategoryLabel.setTextFill(Color.RED);
        }

        try {
            if (this.organUnity.getValue().isEmpty()) {
                emptyFields = true;
                this.unidadOrganizativa.setTextFill(Color.RED);
            }
        } catch (NullPointerException e) {
            emptyFields = true;
            this.unidadOrganizativa.setTextFill(Color.RED);
        }

        return emptyFields;
    }

    @FXML
    private void closeOrCancelDialog() {

        this.dialogStage.close();
    }

//    @FXML
//    private void eventToSetUpperCaseToFirstNameAndLastName(KeyEvent event) {
//        String txtAhoraMismo = this.nameAndLastName.getText();
//        try {
//            if (txtAhoraMismo.length() == 1) {
//                txtAhoraMismo = event.getCharacter().toUpperCase();
//                this.nameAndLastName.setText(txtAhoraMismo);
//                this.nameAndLastName.end();
//            } else if (txtAhoraMismo.toCharArray()[txtAhoraMismo.toCharArray().length-2] == ' '
//                    && event.getCode()!= KeyCode.BACK_SPACE) {
//                txtAhoraMismo = this.nameAndLastName.getText().substring(0,this.nameAndLastName.getText().length()-1);
//                txtAhoraMismo += event.getCharacter().toUpperCase();
//                this.nameAndLastName.setText(txtAhoraMismo);
//                this.nameAndLastName.end();
//            }
////            }else if(event.getCode()==KeyCode.BACK_SPACE){
////                if(this.subStringToName.length()==event.getText().length()){
////                    this.subStringToName="";
////                    this.txtName.setText(this.subStringToName);
////                }else{
////                    this.subStringToName=this.subStringToName
////                            .substring(0,this.subStringToName.length()-1);
////                    System.out.println("Despues de dar BACKSPACE: " +this.subStringToName);
////                    this.txtName.setText(this.subStringToName);
////                }
////                event.consume();
////                this.txtName.end();
////            }
//
//        } catch (NullPointerException e) {
//            txtAhoraMismo = event.getCharacter().toUpperCase();
//            this.nameAndLastName.setText(txtAhoraMismo);
//            event.consume();
//        }catch (IndexOutOfBoundsException e){
//            Util.dialogResult("El campo ya está vacío", Alert.AlertType.INFORMATION);
//        }
//        //event.consume();
//    }

    private void eventUnFocusIdentificationPass(){
        Task<Boolean>task = new Task<Boolean>() {
            boolean exist = false;
            @Override
            protected Boolean call() throws Exception {
                exist=detectingIfIdentificationNumberExist();
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if(this.exist){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Dato existente");
                    alert.setHeaderText(null);
                    alert.setContentText("Lo sentimos, pero hemos detectado que existe un pase con ese " +
                            "número de identidad");
                    alert.showAndWait();
                    identificationPass.setText("");
                    identificationPass.setFocusColor(Paint.valueOf(Color.RED.toString()));
                }
            }
        };

        Thread th = new Thread(task);
        th.start();
    }

    private boolean detectingIfIdentificationNumberExist(){
        boolean exist= false;
        try {
           ResultSet set = Util.executeQuery("Select * From registro_pases where numero_identidad='" + this.identificationPass.getText()+"'");
           if(set.next()){
               exist=true;
           }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return exist;
    }


    @FXML
    private void onClickRegister() {

        //Aync task to save
        Task<Boolean> tarea = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                uploadPhoto();
                registerPass();
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                dialogExecuting.close();
                Util.dialogResult("Se registro con exito", Alert.AlertType.INFORMATION);
                printPase();
                cleanData();
            }
        };


        if (!this.valityEmptyFields()) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(RegistroPasesController.class.getResource("../../views/dialogs/DialogLoading.fxml"));
                AnchorPane panel = loader.load();
                dialogExecuting = new Stage();
                dialogExecuting.setScene(new Scene(panel));
                dialogExecuting.initOwner(this.dialogStage);
                dialogExecuting.initModality(Modality.WINDOW_MODAL);
                dialogExecuting.initStyle(StageStyle.UNDECORATED);
                DialogLoadingController controller = loader.getController();
                controller.setLabelText("Registrando");
                dialogExecuting.show();

                Thread th = new Thread(tarea);
                th.setDaemon(true);
                th.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Util.dialogResult("Existen campos requeridos en blanco", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void imageSearch() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File fileImage = fileChooser.showOpenDialog(this.dialogStage);

        if (fileImage != null) {
            Image image = new Image("file:" + fileImage.getAbsolutePath());
            this.profilePhoto.setImage(image);
        }

    }


    private void printPase(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText("Desea imprimir el pase");
        Optional<ButtonType> action=alert.showAndWait();
        if(action.get().equals(ButtonType.OK)){
            String ci = this.identificationPass.getText();
            if(this.passType.getValue().equalsIgnoreCase("permanente"))
                ServiceLocator.getJasperReportService().imprimirPasePermanente(ci);
            else if(this.passType.getValue().equalsIgnoreCase("Provisional"))
                ServiceLocator.getJasperReportService().imprimirPaseProvisional(ci);
            else if(this.passType.getValue().equalsIgnoreCase("especial"))
                ServiceLocator.getJasperReportService().imprimirPaseEspecial(ci);
            else if(this.passType.getValue().equalsIgnoreCase("negro"))
                ServiceLocator.getJasperReportService().imprimirPaseNegro(ci);
        }
    }

    /**
     * Method to register a pass
     * This method iterate to search the correct sequence on the server Postgres
     */
    private void registerPass() {
        TipoPase tipoPase = ServiceLocator.getTipoPaseService()
                .getTipoPaseByName(this.passType.getSelectionModel().getSelectedItem());
        CodigoPase codigoPase = ServiceLocator.getCodigoPaseService()
                .getCodigoByName(this.passCategory.getSelectionModel().getSelectedItem());
        String passNumber = this.addCerosToPassNumber(this.passNumber.getText());
        String ci = this.identificationPass.getText();
        String name = this.nameAndLastName.getText();
        UnidadOrganizativa unidadOrganizativa = ServiceLocator.getUnidadOrganizativaService()
                .searchUnidadOrganizativaByName(this.organUnity.getSelectionModel().getSelectedItem());
        String acceso = this.access.getText();

        Date fecha = this.valityDate
                .getValue() == null ? null : Date.valueOf(this.valityDate.getValue());

        int baja = this.passLow.isSelected() ? 1 : 0;
        String observacion = this.observations.getText();
        String urlImage = StringUtils.cleanPath(new File(Util.renombrarPath(this.profilePhoto.getImage().getUrl())).getName());

        RegistroPase pase = new RegistroPase(0,
                tipoPase, codigoPase, passNumber, ci,
                name, unidadOrganizativa, acceso, fecha,
                baja, observacion, urlImage
        );

        //Salvando el pase
        int intentos = 0;
        boolean salvado = false;
        while (!salvado && intentos < 10000) {
            try {
                ServiceLocator.getRegistroPaseService().saveRegistroPase(pase);
                salvado = true;
            } catch (SQLException e) {
                e.printStackTrace();
                salvado = false;
                intentos++;
            }
        }
    }

    private String addCerosToPassNumber(String passNumber) {
        System.out.println("El tamanno del numero de pase es: " + passNumber.length());
        if (passNumber.length() < 4) {
            while (passNumber.length() < 4) {
                passNumber = 0 + passNumber;
            }
        }
        return passNumber;
    }

    private void cleanData() {
        this.identificationPass.clear();
        this.nameAndLastName.clear();
        this.profilePhoto.setImage(new Image("@../../icons/no-img.jpg"));
        this.passNumber.clear();
        this.passType.getSelectionModel().clearSelection();
        this.passCategory.getSelectionModel().clearSelection();
        this.passLow.setSelected(false);
        this.organUnity.getSelectionModel().clearSelection();
        this.valityDate.getEditor().clear();
        this.access.clear();
        this.observations.clear();
    }


    private void uploadPhoto() {
        try {
            PostFile postFile = new PostFile(ConfigProperties.getProperties().getProperty("URL_IMAGE_SERVER"),
                    Util.renombrarPath(this.profilePhoto.getImage().getUrl()));
            postFile.post();
        } catch (Exception e) {
            Logger.getLogger(PostFile.class.getName()).log(Level.SEVERE, null, e);
        }
    }


}
