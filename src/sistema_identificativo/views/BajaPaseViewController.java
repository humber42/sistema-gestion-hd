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
import sistema_identificativo.models.RegistroPase;
import sistema_identificativo.models.TipoPase;
import util.Util;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BajaPaseViewController {
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
        this.passType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
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

        this.table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.actualizeLblSize(impresionList.size());
    }

    @FXML
    private void aplicar() {
        String passTypeSelected;
        String text;
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
            this.table.setLayoutX(0);
            this.table.setLayoutY(132);
        }else{
            activateFilters.setText("Desactivar filtros");
            activeFilters=true;
            this.passType.setPromptText("Seleccione");
            this.passType.getItems().setAll(this.getAllPassType());
            this.filterPane.setCollapsible(true);
            this.filterPane.setDisable(false);
            this.filterPane.setExpanded(true);
            this.filterPane.setVisible(true);
            this.table.setLayoutY(236);
            this.table.setPrefHeight(249);
        }
    }

    @FXML
    private void handleBaja(){
        ObservableList<Impresion> lista = this.table.getSelectionModel().getSelectedItems();
        if(!lista.isEmpty()){
            Impresion data = lista.get(0);
            if(lista.size() == 1){
                try {
                    RegistroPase pase = ServiceLocator.getRegistroPaseService().getPaseByCI(data.getIdentidad());
                    if(pase != null){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                "¿Desea dar baja al pase de " + pase.getNombre() + "?"
                                ,ButtonType.OK, ButtonType.CANCEL);
                        alert.showAndWait();
                        if(alert.getResult() == ButtonType.OK) {
                            ServiceLocator.getRegistroPaseService().darBajaPase(pase.getIdReg());
                            Util.dialogResult("El pase fue dado de baja con éxito.", Alert.AlertType.INFORMATION);
                            List<Impresion> impresionList = ServiceLocator.getImpresionService().getAllImpressions();
                            this.initializeTable(impresionList);
                        }
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "¿Desea dar baja a los pases seleccionados?"
                        ,ButtonType.OK, ButtonType.CANCEL);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.OK) {
                    RegistroPase pase;
                    try {
                        int contFalloPases = 0;
                        for (Impresion imp : lista) {
                            pase = ServiceLocator.getRegistroPaseService().getPaseByCI(imp.getIdentidad());
                            if(pase != null){
                                ServiceLocator.getRegistroPaseService().darBajaPase(pase.getIdReg());
                            } else{
                                contFalloPases++;
                            }
                        }
                        if(contFalloPases > 0){
                            Util.dialogResult("El proceso de dar baja a los pases falló en " + contFalloPases
                                    + " ocasiones.", Alert.AlertType.ERROR);
                        } else if(contFalloPases == 0){
                            Util.dialogResult("Los pases seleccionados fueron dados de baja con éxito.", Alert.AlertType.INFORMATION);
                            List<Impresion> impresionList = ServiceLocator.getImpresionService().getAllImpressions();
                            this.initializeTable(impresionList);
                        }
                    } catch (SQLException e){
                        Util.dialogResult("Ha ocurrido un error al dar baja a los pases.", Alert.AlertType.ERROR);
                    }
                }
            }
        } else {
            Util.dialogResult("No hay elementos seleccionados.", Alert.AlertType.INFORMATION);
        }
    }
}
