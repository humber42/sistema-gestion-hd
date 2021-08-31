package sistema_identificativo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ServiceLocator;
import sistema_identificativo.models.Impresion;
import sistema_identificativo.models.TipoPase;
import util.Util;

import java.sql.SQLException;
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
    private JFXButton btnAplicar;
    @FXML
    private JFXButton btnAceptar;
    @FXML
    private Label lblSize;
    @FXML
    private Label lblSelection;

    private Stage dialogStage;

    private Boolean activeFilters = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        filterPane.setVisible(false);
        this.txtName.setOnKeyTyped(event ->
                Util.eventToSetUpperCaseToFirstNameAndLastName(event,this.txtName));

        activateFilters.setOnAction(event -> {
            if (activeFilters) {
                activateFilters.setText("Activar filtros");
                activeFilters = false;
                filterPane.setCollapsible(false);
                filterPane.setDisable(true);
                filterPane.setExpanded(false);
                filterPane.setVisible(false);
                txtName.setText("");
                lblSelection.setText("Ningún elemento seleccionado");
                List<Impresion> impresionList = ServiceLocator.getImpresionService().getAllImpressions();
                initializeTable(impresionList);
            } else {
                activateFilters.setText("Desactivar filtros");
                activeFilters = true;
                filterPane.setCollapsible(true);
                filterPane.setDisable(false);
                filterPane.setExpanded(true);
                filterPane.setVisible(true);
                btnAplicar.setDisable(false);
                passType.getItems().setAll(this.getAllPassType());
            }
        });
        List<Impresion> impresionList = ServiceLocator.getImpresionService().getAllImpressions();
        initializeTable(impresionList);
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
        ObservableList<Impresion> impresionList = table.getSelectionModel().getSelectedItems();
        if(!impresionList.isEmpty()){
            Impresion impression = impresionList.get(0);
            if(impresionList.size() == 1){
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
            else{
                if(sameTypePass(impresionList)){
                    String typePass = impresionList.get(0).getTipoPase();
                    try {
                        for (Impresion imp : impresionList) {
                            ServiceLocator.getRegistroPaseService().updateSeleccionado(imp.getIdentidad());
                            ServiceLocator.getRegistroImpresionesService().execNewOrUpdateImpressionRegister(imp.getIdentidad());
                        }
                        if (typePass.equalsIgnoreCase("Permanente"))
                            ServiceLocator.getJasperReportService().imprimirPasesPermanentesSelected();
                        else if (typePass.equalsIgnoreCase("Especial"))
                            ServiceLocator.getJasperReportService().imprimirPasesEspecialesSelected();
                        else if (typePass.equalsIgnoreCase("Provisional"))
                            ServiceLocator.getJasperReportService().imprimirPasesProvisionalesSelected();
                        else if (typePass.equalsIgnoreCase("Negro"))
                            ServiceLocator.getJasperReportService().imprimirPasesNegrosSelected();

                        ServiceLocator.getRegistroPaseService().deselectAllSelections();

                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                } else{
                    Util.dialogResult("Los pases a imprimir deben ser del mismo tipo.", Alert.AlertType.WARNING);
                }
            }
            List<Impresion> impresions = ServiceLocator.getImpresionService().getAllImpressions();
            initializeTable(impresions);
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
}
