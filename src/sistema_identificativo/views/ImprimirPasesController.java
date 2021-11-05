package sistema_identificativo.views;

import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private Label lblSize;
    @FXML
    private Label lblSelection;

    private Stage dialogStage;

    private boolean activeFilters = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        filterPane.setVisible(false);
        this.txtName.setOnKeyTyped(event -> {
            Util.eventToSetUpperCaseToFirstNameAndLastName(event, this.txtName);
            this.aplicar();
        });
        activateFilters.setOnAction(event ->
            this.modificatingTableSizes()
        );
        List<Impresion> impresionList = ServiceLocator.getImpresionService().getAllImpressions();
        initializeTable(impresionList);

        this.passType.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) ->
            this.aplicar()
        );
    }

    private List<String> getAllPassType() {
        return ServiceLocator.getTipoPaseService().getAllTipoPase().stream().map(TipoPase::getTipoPase).collect(Collectors.toList());
    }

    private void initializeTable(List<Impresion> impresionList) {
        this.table.getItems().clear();
        ObservableList<Impresion> observableList = FXCollections.observableList(impresionList);

        this.table.setItems(observableList);
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
        this.table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.actualizeLblSize(impresionList.size());
    }

    @FXML
    private void aplicar() {
        String passTypeSelected, text;
        List<Impresion> impresionList;
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
        } else {
            impresionList = ServiceLocator.getImpresionService().getAllImpressions();
        }
        lblSelection.setText("Ningún elemento seleccionado");

        if (!impresionList.isEmpty())
            initializeTable(impresionList);
        else {
            table.getItems().clear();
            lblSize.setText("Ningún elemento encontrado");
        }
    }

    @FXML
    private void closeOrCancelDialog() {
        this.dialogStage.close();
    }

    private void actualizeLblSize(int size) {
        if (size == 1)
            this.lblSize.setText(size + " elemento encontrado");
        else
            this.lblSize.setText(size + " elementos encontrados");
    }

    @FXML
    private void imprimir() {
        ObservableList<Impresion> impresionList = table.getSelectionModel().getSelectedItems();

        if(!impresionList.isEmpty()){
            Impresion impression = impresionList.get(0);
            if(impresionList.size() == 1){
                String typePass = impression.getTipoPase();
            if (typePass.equalsIgnoreCase("Permanente")) {
                ServiceLocator.getJasperReportService().imprimirPasePermanente(impression.getIdentidad(), this.dialogStage,this.table.getItems());

            }
            else if (typePass.equalsIgnoreCase("Especial")) {
                ServiceLocator.getJasperReportService().imprimirPaseEspecial(impression.getIdentidad(), this.dialogStage,this.table.getItems());

            }
            else if (typePass.equalsIgnoreCase("Provisional")) {
                ServiceLocator.getJasperReportService().imprimirPaseProvisional(impression.getIdentidad(), this.dialogStage,this.table.getItems());
            }
            else if (typePass.equalsIgnoreCase("Negro")) {
                ServiceLocator.getJasperReportService().imprimirPaseNegro(impression.getIdentidad(), this.dialogStage,this.table.getItems());
            }
            }
            else{
                if(sameTypePass(impresionList)){
                    String typePass = impresionList.get(0).getTipoPase();
                        if (typePass.equalsIgnoreCase("Permanente"))
                            ServiceLocator.getJasperReportService().imprimirPasesPermanentesSelected(this.dialogStage, impresionList,this.table.getItems());
                        else if (typePass.equalsIgnoreCase("Especial"))
                            ServiceLocator.getJasperReportService().imprimirPasesEspecialesSelected(this.dialogStage,impresionList,this.table.getItems());
                        else if (typePass.equalsIgnoreCase("Provisional"))
                            ServiceLocator.getJasperReportService().imprimirPasesProvisionalesSelected(this.dialogStage,impresionList,this.table.getItems());
                        else if (typePass.equalsIgnoreCase("Negro"))
                            ServiceLocator.getJasperReportService().imprimirPasesNegrosSelected(this.dialogStage,impresionList,this.table.getItems());
                } else{
                    Util.dialogResult("Los pases a imprimir deben ser del mismo tipo.", Alert.AlertType.WARNING);
                }
            }
        }
        else{
            Util.dialogResult("No hay elementos seleccionados.", Alert.AlertType.INFORMATION);
        }
    }

    private boolean sameTypePass(ObservableList<Impresion> impresionList){
        boolean same = true;
        String firstTypePass = impresionList.get(0).getTipoPase();
        int i = 1;
        while (i < impresionList.size() && same){
            Impresion imp = impresionList.get(i);
            if(firstTypePass.equalsIgnoreCase(imp.getTipoPase()))
                i++;
            else
                same = false;
        }
        return same;
    }

    @FXML
    private void actualizeSelections(){
        int cantSelections = table.getSelectionModel().getSelectedItems().size();
        if(cantSelections == 1)
            lblSelection.setText(cantSelections + " elemento seleccionado");
        else
            lblSelection.setText(cantSelections + " elementos seleccionados");
    }

    private void modificatingTableSizes(){
            if(this.activeFilters){
                this.activateFilters.setText("Activar filtros");
                this.activeFilters=false;
                this.filterPane.setCollapsible(false);
                this.filterPane.setDisable(true);
                this.filterPane.setExpanded(false);
                this.filterPane.setVisible(false);
                this.passType.getSelectionModel().clearSelection();
                this.passType.setPromptText("Seleccione");
                this.txtName.clear();
                this.initializeTable(ServiceLocator.getImpresionService().getAllImpressions());
                this.table.setPrefHeight(353);
                this.table.setPrefWidth(652);
                this.table.setLayoutY(132);
                this.table.setLayoutX(0);
            }else{
                activateFilters.setText("Desactivar filtros");
                activeFilters=true;
                this.passType.setPromptText("Seleccione");
                this.passType.getItems().setAll(this.getAllPassType());
                this.filterPane.setCollapsible(true);
                this.filterPane.setDisable(false);
                this.filterPane.setExpanded(true);
                this.filterPane.setVisible(true);
                this.table.setPrefHeight(249);
                this.table.setLayoutY(236);
            }
    }
}
