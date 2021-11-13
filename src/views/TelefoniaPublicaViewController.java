package views;

import com.jfoenix.controls.JFXButton;
import informes_generate.ProcessExcelTPub;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.EstacionPublicaCentroAgente;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginViewController;
import services.ServiceLocator;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TelefoniaPublicaViewController {
    @FXML
    private TableView<EstacionPublicaCentroAgente> tableEstaciones;
    @FXML
    private TableColumn<EstacionPublicaCentroAgente, String> uoColumn;
    @FXML
    private TableColumn<EstacionPublicaCentroAgente, String> municipioColumn;
    @FXML
    private TableColumn<EstacionPublicaCentroAgente, String> centroAgColumn;
    @FXML
    private TableColumn<EstacionPublicaCentroAgente, String> estacionColumn;
    @FXML
    private ComboBox<String> cboxUORG;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnNew;

    private UserLoggedIn logged;

    private Stage dialogStage;

    private MainApp mainApp;

    private List<EstacionPublicaCentroAgente> estaciones;

    private static EstacionPublicaCentroAgente estacionSelected;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        this.logged = LoginViewController.getUserLoggedIn();

        if(logged.isSuperuser()){
            this.btnEdit.setVisible(true);
            this.btnDelete.setVisible(true);
            this.btnNew.setVisible(true);
        } else if(logged.hasPermiso_pases() || logged.hasPermiso_visualizacion()){
            this.btnEdit.setVisible(false);
            this.btnDelete.setVisible(false);
            this.btnNew.setVisible(false);
        }

        this.cboxUORG.getItems().add("<<Todos>>");
        this.cboxUORG.getItems().addAll(
                ServiceLocator.getUnidadOrganizativaService().fetchAll()
                        .stream()
                        .map(UnidadOrganizativa::getUnidad_organizativa)
                        .collect(Collectors.toList())
        );

        TextFields.bindAutoCompletion(this.cboxUORG.getEditor(), this.cboxUORG.getItems());

        this.cboxUORG.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    if (!newValue.equalsIgnoreCase("<<Todos>>")) {
                        this.modifyTable(false);
                        this.estaciones = ServiceLocator.getEstacionPublicaCentroAgenteService()
                                .getAllByIdUORG(ServiceLocator.getUnidadOrganizativaService()
                                        .searchUnidadOrganizativaByName(newValue).getId_unidad_organizativa());
                        this.initializeTableTelefonia(estaciones, false);
                    } else {
                        this.modifyTable(true);
                        this.estaciones = ServiceLocator.getEstacionPublicaCentroAgenteService().getAll();
                        this.initializeTableTelefonia(estaciones, true);
                    }
                }
                ));

        this.cboxUORG.editorProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.getText().equalsIgnoreCase("<<Todos>>")) {
                this.modifyTable(false);
                this.estaciones = ServiceLocator.getEstacionPublicaCentroAgenteService()
                        .getAllByIdUORG(ServiceLocator.getUnidadOrganizativaService()
                                .searchUnidadOrganizativaByName(newValue.getText()).getId_unidad_organizativa());
                this.initializeTableTelefonia(estaciones, false);
            } else {
                this.modifyTable(true);
                this.estaciones = ServiceLocator.getEstacionPublicaCentroAgenteService().getAll();
                this.initializeTableTelefonia(estaciones, true);
            }
        });

        this.estaciones = ServiceLocator.getEstacionPublicaCentroAgenteService().getAll();
        this.initializeTableTelefonia(estaciones, true);

        this.tableEstaciones.setOnMouseClicked(
                event -> {
                    estacionSelected = tableEstaciones.getSelectionModel().getSelectedItem();
                    if (estacionSelected != null)
                        this.enableBtns();
                    else
                        this.disableBtns();
                }
        );
    }

    private void modifyTable(boolean all) {
        if (!all) {
            this.uoColumn.setVisible(false);
            this.municipioColumn.setPrefWidth(171);
            this.centroAgColumn.setPrefWidth(177);
            this.estacionColumn.setPrefWidth(179);
        } else {
            this.uoColumn.setVisible(true);
            this.uoColumn.setPrefWidth(122);
            this.municipioColumn.setPrefWidth(132);
            this.centroAgColumn.setPrefWidth(133);
            this.estacionColumn.setPrefWidth(142);
        }
    }

    private void initializeTableTelefonia(List<EstacionPublicaCentroAgente> estaciones, boolean allColumns) {
        if (!estaciones.isEmpty()) {
            this.tableEstaciones.getItems().clear();
            ObservableList<EstacionPublicaCentroAgente> estacionesList =
                    FXCollections.observableList(estaciones);

            this.tableEstaciones.setItems(estacionesList);
            System.out.println(estaciones.size());

            if (allColumns) {
                this.uoColumn.setCellValueFactory(
                        cellData ->
                                new SimpleStringProperty(
                                        cellData.getValue().getUnidadOrganizativa()
                                )
                );
            }
            this.municipioColumn.setCellValueFactory(
                    cellData ->
                            new SimpleStringProperty(
                                    cellData.getValue().getMunicipio()
                            )
            );
            this.centroAgColumn.setCellValueFactory(
                    cellData ->
                            new SimpleStringProperty(
                                    String.valueOf(cellData.getValue().getCentroAgente())
                            )
            );
            this.estacionColumn.setCellValueFactory(
                    cellData ->
                            new SimpleStringProperty(
                                    String.valueOf(cellData.getValue().getEstacionPublica())
                            )
            );
            this.tableEstaciones.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } else {
            this.tableEstaciones.getItems().clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "No existen resultados para la búsqueda.");
            alert.setHeaderText(null);
            alert.setTitle("Información");
            alert.initOwner(this.dialogStage);
            alert.showAndWait();
        }
    }

    private void enableBtns() {
        this.btnDelete.setDisable(false);
        this.btnEdit.setDisable(false);
    }

    private void disableBtns() {
        this.btnDelete.setDisable(true);
        this.btnEdit.setDisable(true);
    }

    @FXML
    private void handleNew() {
        this.tableEstaciones.getSelectionModel().clearSelection();
        estacionSelected = null;
        this.disableBtns();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UrlLocation.class.getResource("RegistrarEstacionTelefoniaView.fxml"));
            AnchorPane page = loader.load();
            Stage hechoStage = new Stage();
            hechoStage.initModality(Modality.WINDOW_MODAL);
            hechoStage.setMaximized(false);
            hechoStage.setResizable(false);
            hechoStage.initOwner(this.dialogStage);
            hechoStage.setScene(new Scene(page));
            hechoStage.setTitle("Registrar Telefonía Pública");

            RegistrarEstacionTelefoniaViewController controller = loader.getController();

            hechoStage.setOnCloseRequest(p->{
                        this.initializeTableTelefonia(
                                ServiceLocator.getEstacionPublicaCentroAgenteService().getAll(), true);
                    }
            );

            controller.setDialogStage(hechoStage);
            controller.setEstacionPublicaCentroAgentes(this.tableEstaciones.getItems());
            hechoStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoadDataFromExcelFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files","*.*"),
                new FileChooser.ExtensionFilter("Excel Files 2007 And High", "*.xlsx"),
                new FileChooser.ExtensionFilter("Excel Files 97-2003","*.xls")

        );
        File fileExcel = fileChooser.showOpenDialog(this.dialogStage);
        if(fileExcel!=null){
            if(fileExcel.getPath().contains(".xslx"))
                ProcessExcelTPub.getDataFromExcel(fileExcel,true);
            else
                ProcessExcelTPub.getDataFromExcel(fileExcel);
        }
    }

    @FXML
    private void handleEdit() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UrlLocation.class.getResource("RegistrarEstacionTelefoniaView.fxml"));
            AnchorPane page = loader.load();
            Stage hechoStage = new Stage();
            hechoStage.initModality(Modality.WINDOW_MODAL);
            hechoStage.setMaximized(false);
            hechoStage.setResizable(false);
            hechoStage.initOwner(this.dialogStage);
            hechoStage.setScene(new Scene(page));
            hechoStage.setTitle("Editar Telefonía Pública");

            RegistrarEstacionTelefoniaViewController controller = loader.getController();

            hechoStage.setOnCloseRequest(p->{
                        this.initializeTableTelefonia(
                                ServiceLocator.getEstacionPublicaCentroAgenteService().getAll(), true);
                    }
            );

            controller.setDialogStage(hechoStage);
            controller.setEstacionPublicaCentroAgentes(this.tableEstaciones.getItems());
            hechoStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.tableEstaciones.getSelectionModel().clearSelection();
        estacionSelected = null;
        this.disableBtns();
    }

    @FXML
    private void handleDelete() {
        if(estacionSelected !=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Está seguro que desea eliminar el elemento seleccionado?",
                                ButtonType.OK, ButtonType.CANCEL);
            alert.initOwner(this.dialogStage);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.OK){
                ServiceLocator.getEstacionPublicaCentroAgenteService().
                        deleteEstacionPublicaCentroAgente(estacionSelected.getIdReg());
                Util.dialogResult("Eliminación exitosa.", Alert.AlertType.INFORMATION);
                estaciones = ServiceLocator.getEstacionPublicaCentroAgenteService().getAll();
                this.initializeTableTelefonia(estaciones, true);
                this.tableEstaciones.getSelectionModel().clearSelection();
                this.disableBtns();
            }
        }
    }

    public static EstacionPublicaCentroAgente getEstacionPublicaCentroAgenteSelected(){
        return estacionSelected;
    }
}
