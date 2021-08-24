package sistema_identificativo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ServiceLocator;
import sistema_identificativo.models.Impresion;
import sistema_identificativo.models.TipoPase;
import util.Util;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ImprimirPasesController {

    @FXML
    private AnchorPane anchor;
    @FXML
    private HBox hbox;
    @FXML
    private JFXToggleButton activateFilters;
    @FXML
    private ComboBox<String> passType;
    @FXML
    private TextField txtName;
    @FXML
    private TitledPane filterPane;
    @FXML
    private TableView<Impresion> table;
    @FXML
    private TableColumn<Impresion, String> passNumberColumn;
    @FXML
    private TableColumn<Impresion, String> identityColumn;
    @FXML
    private TableColumn<Impresion, String> nameColumn;
    @FXML
    private TableColumn<Impresion, String> passTypeColumn;
    @FXML
    private TableColumn<Impresion, String> passCodeColumn;
    @FXML
    private TableColumn<Impresion, String> cantImpresionesColumn;
    @FXML
    private JFXButton btnAplicar;
    @FXML
    private JFXButton btnAceptar;
    @FXML
    private Label lblSize;

    private Stage dialogStage;

    private Boolean activeFilters = false;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        filterPane.setVisible(false);
        this.txtName.setOnKeyTyped(this::eventToSetUpperCaseToFirstNameAndLastName);

        activateFilters.setOnAction(event -> {
            if (activeFilters) {
                activateFilters.setText("Activar filtros");
                activeFilters = false;
                filterPane.setCollapsible(false);
                filterPane.setDisable(true);
                filterPane.setExpanded(false);
                filterPane.setVisible(false);
                txtName.setText("");
                List<Impresion> impresionList = ServiceLocator.getImpresionService().getAllImpressions();
                initializeTable(impresionList);
//                table.setPrefHeight(652);
//                table.setPrefWidth(308);
//                table.setLayoutX(0);
//                table.setLayoutY(142);
//            //    anchor.setPrefHeight(509);
//            //    hbox.setTranslateY(0);
            } else {
                activateFilters.setText("Desactivar filtros");
                activeFilters = true;
                filterPane.setCollapsible(true);
                filterPane.setDisable(false);
                filterPane.setExpanded(true);
                filterPane.setVisible(true);
                btnAplicar.setDisable(false);
                passType.getItems().setAll(this.getAllPassType());
                //  anchor.setPrefHeight(700);
                // hbox.setTranslateY(95);
            }
        });
        List<Impresion> impresionList = ServiceLocator.getImpresionService().getAllImpressions();
        initializeTable(impresionList);
    }

    @FXML
    private void eventToSetUpperCaseToFirstNameAndLastName(KeyEvent event) {
        String txtAhoraMismo = this.txtName.getText();
        try {
            if (txtAhoraMismo.length() == 1) {
                txtAhoraMismo = event.getCharacter().toUpperCase();
                this.txtName.setText(txtAhoraMismo);
                this.txtName.end();
            } else if (txtAhoraMismo.toCharArray()[txtAhoraMismo.toCharArray().length-2] == ' '
            && event.getCode()!=KeyCode.BACK_SPACE) {
                txtAhoraMismo = this.txtName.getText().substring(0,this.txtName.getText().length()-1);
                txtAhoraMismo += event.getCharacter().toUpperCase();
                this.txtName.setText(txtAhoraMismo);
                this.txtName.end();
            }
//            }else if(event.getCode()==KeyCode.BACK_SPACE){
//                if(this.subStringToName.length()==event.getText().length()){
//                    this.subStringToName="";
//                    this.txtName.setText(this.subStringToName);
//                }else{
//                    this.subStringToName=this.subStringToName
//                            .substring(0,this.subStringToName.length()-1);
//                    System.out.println("Despues de dar BACKSPACE: " +this.subStringToName);
//                    this.txtName.setText(this.subStringToName);
//                }
//                event.consume();
//                this.txtName.end();
//            }

        } catch (NullPointerException e) {
            txtAhoraMismo = event.getCharacter().toUpperCase();
            this.txtName.setText(txtAhoraMismo);
            event.consume();
        }catch (IndexOutOfBoundsException e){
            Util.dialogResult("El campo ya está vacío", Alert.AlertType.INFORMATION);
        }
        //event.consume();
    }

    private List<String> getAllPassType() {
        return ServiceLocator.getTipoPaseService().getAllTipoPase().stream().map(TipoPase::getTipoPase).collect(Collectors.toList());
    }

    private void initializeTable(List<Impresion> impresionList) {
        table.getItems().clear();
        ObservableList<Impresion> observableList = FXCollections.observableList(impresionList);

        table.setItems(observableList);
        this.passNumberColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData.getValue().getNumero_pase()
                        )
        );
        this.identityColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData.getValue().getIdentidad()
                        )
        );
        this.nameColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData.getValue().getNombre()
                        )
        );
        this.passTypeColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData.getValue().getTipoPase()
                        )
        );
        this.passCodeColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData.getValue().getCodigoPase()
                        )
        );
        this.cantImpresionesColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                String.valueOf(cellData.getValue().getCantImpresiones())
                        )
        );

        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        actualizeLblSize(impresionList.size());
    }

    @FXML
    private void aplicar() {
        String passTypeSelected, text;
        List<Impresion> impresionList = new LinkedList<>();
        if (!passType.getSelectionModel().isEmpty() && txtName.getText().isEmpty()) {
            passTypeSelected = passType.getSelectionModel().getSelectedItem();
            impresionList = ServiceLocator.getImpresionService().getAllByPassType(
                    ServiceLocator.getTipoPaseService().getPassCodeByPassType(passTypeSelected));
        } else if (!txtName.getText().isEmpty() && passType.getSelectionModel().isEmpty()) {
            text = txtName.getText();
            impresionList = ServiceLocator.getImpresionService().getAllByContainName(text);
        } else if (!passType.getSelectionModel().isEmpty() && !txtName.getText().isEmpty()) {
            passTypeSelected = passType.getSelectionModel().getSelectedItem();
            text = txtName.getText();
            impresionList = ServiceLocator.getImpresionService().getAllByPassTypeAndContainName(
                    ServiceLocator.getTipoPaseService().getPassCodeByPassType(passTypeSelected), text
            );
        }

        if (!impresionList.isEmpty())
            initializeTable(impresionList);
        else
            table.getItems().clear();
    }

    @FXML
    private void closeOrCancelDialog() {
        this.dialogStage.close();
    }

    private void actualizeLblSize(int size) {
        if (size == 1)
            lblSize.setText(size + " elemento encontrado");
        else
            lblSize.setText(size + " elementos encontrados");
    }

    @FXML
    private void imprimir() {
        Impresion impression = table.getSelectionModel().getSelectedItem();
        if (impression != null) {
            // System.out.println(impression.getIdentidad());
            String typePass = impression.getTipoPase();
            if (typePass.equalsIgnoreCase("Permanente"))
                ServiceLocator.getJasperReportService().imprimirPasePermanente(impression.getIdentidad());
            else if (typePass.equalsIgnoreCase("Especial"))
                ServiceLocator.getJasperReportService().imprimirPaseEspecial(impression.getIdentidad());
            else if (typePass.equalsIgnoreCase("Provisional"))
                ServiceLocator.getJasperReportService().imprimirPaseProvisional(impression.getIdentidad());
            else if (typePass.equalsIgnoreCase("Negro"))
                ServiceLocator.getJasperReportService().imprimirPaseNegro(impression.getIdentidad());
        }
        ObservableList<Impresion> impresionList = table.getSelectionModel().getSelectedItems();
        for (Impresion i : impresionList)
            System.out.println(i.getNumero_pase());
    }
}
