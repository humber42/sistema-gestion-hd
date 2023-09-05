package sistema_identificativo.views.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import services.ServiceLocator;
import sistema_identificativo.models.RegistroPase;
import util.Util;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class CambiarUnidadOrganizativaController {


    private Stage dialogStage;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelNumeroPase;
    @FXML
    private Label labelCarneIdentidad;
    @FXML
    private Label labelTipoPase;
    @FXML
    private Label labelCategoriaPase;
    @FXML
    private ListView<String> listaPases;

    @FXML
    private TitledPane panePases;

    @FXML
    private JFXTextField textName;
    @FXML
    private AnchorPane panelImagen;

    @FXML
    private JFXButton buttonCancel;
    @FXML
    private JFXButton buttonEdit;

    @FXML
    private TextField textUorg;
    @FXML
    private ComboBox<String> uOrgsComboBox;

    private RegistroPase paseSeleccionado;

    private boolean activeEdit;

    private Stage dialogUploading;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {

        this.activeEdit = false;
        this.textUorg.setEditable(false);

        this.uOrgsComboBox.setVisible(true);

        this.textName.setOnKeyTyped(event -> {
            Util.eventToSetUpperCaseToFirstNameAndLastName(event, this.textName);
            this.applySearch();
        });

        this.labelCarneIdentidad.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNumeroIdentidad() : "(No hay datos seleccionados)");
        this.labelCategoriaPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getCodigoPase().getCodigo() : "(No hay datos seleccionados)");
        this.labelNombre.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNombre() : "(No hay datos seleccionados)");
        this.labelNumeroPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNumeroPase() : "(No hay datos seleccionados)");
        this.labelTipoPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getTipoPase().getTipoPase() : "(No hay datos seleccionados)");
        this.textUorg.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getUnidadOrganizativa().getUnidad_organizativa() : "(No hay datos seleccionados)");

        this.listaPases.getItems().addAll(
                ServiceLocator.getRegistroPaseService().getAllPasesName()
        );

        this.listaPases.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.listaPases.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    this.setValuesOnToLabels(newValue);
                }
        );


        this.textName.setOnKeyTyped(event -> {
            Util.eventToSetUpperCaseToFirstNameAndLastName(event, this.textName);
            this.applySearch();
        });


        this.buttonCancel.setVisible(false);
        this.buttonEdit.setDisable(true);
    }

    /**
     * On this method search to the list
     * <p>
     * If is empty the search text field so show all the list
     * else search on the database
     */
    private void applySearch() {
        this.listaPases.getItems().clear();
        //TODO:Add all pases in search
        this.listaPases.getItems().addAll(
                this.textName.getText().isEmpty() ?
                        ServiceLocator.getRegistroPaseService().getAllPasesName() :
                        ServiceLocator.getRegistroPaseService().getAllPasesByContainName(this.textName.getText())
        );

    }


    private void setValuesOnToLabels(String value) {
        if (value != null) {
            this.paseSeleccionado = ServiceLocator.getRegistroPaseService().getPaseByPassName(value);
            this.labelCarneIdentidad.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNumeroIdentidad() : "(No hay datos)");
            this.labelCategoriaPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getCodigoPase().getCodigo() : "(No hay datos)");
            this.labelNombre.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNombre() : "(No hay datos)");
            this.labelNumeroPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getNumeroPase() : "(No hay datos)");
            this.labelTipoPase.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getTipoPase().getTipoPase() : "(No hay datos)");
            this.textUorg.setText(this.paseSeleccionado != null ? this.paseSeleccionado.getUnidadOrganizativa().getUnidad_organizativa() : ("No hay datos"));
            if (activeEdit) {
                this.disableEdit();
            }
        } else {
            this.labelCarneIdentidad.setText("(No hay datos)");
            this.labelCategoriaPase.setText("(No hay datos)");
            this.labelNombre.setText("(No hay datos)");
            this.labelNumeroPase.setText("(No hay datos)");
            this.labelTipoPase.setText("(No hay datos)");
            this.textUorg.setText("(No hay datos");
        }
        this.buttonEdit.setDisable(this.paseSeleccionado == null);
    }

    @FXML
    private void closeDialog() {
        this.dialogStage.close();
    }

    @FXML
    private void buttonEditActions() {
        if (activeEdit) {
            int id_uorg = ServiceLocator.getUnidadOrganizativaService()
                    .searchUnidadOrganizativaByName(this.uOrgsComboBox.getValue()).getId_unidad_organizativa();
            ServiceLocator.getRegistroPaseService().updateUnidadOrg(paseSeleccionado.getIdReg(), id_uorg);
            this.disableEdit();
        } else {
            this.enableEdit();
        }
    }

    private void disableEdit() {
        this.buttonEdit.setText("Editar");
        this.textUorg.setVisible(true);
        this.uOrgsComboBox.setVisible(false);
        this.textUorg.setText(ServiceLocator.getRegistroPaseService()
                .getRegistroPaseById(paseSeleccionado.getIdReg()).getUnidadOrganizativa().getUnidad_organizativa());
        this.activeEdit = false;
        this.buttonCancel.setVisible(false);
    }

    private void enableEdit() {
        this.buttonEdit.setText("Guardar");
        this.textUorg.setVisible(false);
        this.uOrgsComboBox.setVisible(true);


        String nombreUOrg = ServiceLocator.getRegistroPaseService()
                .getRegistroPaseById(paseSeleccionado.getIdReg()).getUnidadOrganizativa().getUnidad_organizativa();

        this.uOrgsComboBox.getItems()
                .addAll(ServiceLocator.getUnidadOrganizativaService()
                        .fetchAll().stream().map(UnidadOrganizativa::getUnidad_organizativa)
                        .collect(Collectors.toList()));

        this.uOrgsComboBox.setEditable(true);

        int index = ServiceLocator.getUnidadOrganizativaService()
                .fetchAll()
                .stream()
                .map(UnidadOrganizativa::getUnidad_organizativa)
                .collect(Collectors.toCollection(LinkedList::new))
                .indexOf(nombreUOrg);

        TextFields.bindAutoCompletion(this.uOrgsComboBox.getEditor(), this.uOrgsComboBox.getItems());

        this.uOrgsComboBox.getSelectionModel().select(index);
        this.activeEdit = true;
        this.buttonCancel.setVisible(true);
    }

    @FXML
    private void cancelEdition() {
        this.disableEdit();
    }

}
