package posiciones_agentes.views;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.dialog.ExceptionDialog;
import posiciones_agentes.models.PosicionAgente;
import posiciones_agentes.models.RegistroPosicionesAgentes;
import services.ServiceLocator;
import util.Util;
import views.dialogs.DialogLoadingController;
import views.dialogs.DialogLoadingUrl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GestionPosicionesAgentesController {
    @FXML
    private TableView<PosicionAgente> tablePosAgentes;
    @FXML
    private TableColumn<PosicionAgente, String> uoColumn;
    @FXML
    private TableColumn<PosicionAgente, String> posicionColumn;
    @FXML
    private TableColumn<PosicionAgente, String> proveedorColumn;
    @FXML
    private TableColumn<PosicionAgente, String> efectivosColumn;
    @FXML
    private TableColumn<PosicionAgente, String> diasLabColumn;
    @FXML
    private TableColumn<PosicionAgente, String> diasNoLabColumn;
    @FXML
    private ComboBox<String> cboxUorg;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnAdd;

    private Stage dialogStage;

    private Stage dialogLoading;

    private List<PosicionAgente> instalaciones;

    private PosicionAgente posicionAgenteSelected;
    private static RegistroPosicionesAgentes instalacionSelected;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public static RegistroPosicionesAgentes getInstalacionSelected() {
        return instalacionSelected;
    }

    @FXML
    private void initialize() {
        this.cboxUorg.getItems().add("<<Todos>>");
        this.cboxUorg.getItems().addAll(
                ServiceLocator.getUnidadOrganizativaService().fetchAll()
                        .stream()
                        .map(UnidadOrganizativa::getUnidad_organizativa)
                        .collect(Collectors.toList())
        );

        TextFields.bindAutoCompletion(this.cboxUorg.getEditor(), this.cboxUorg.getItems());

        this.cboxUorg.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    if (!newValue.equalsIgnoreCase("<<Todos>>")) {
                        this.modifyTable(false);
                        this.instalaciones = ServiceLocator.getRegistroPosicionesAgentesService()
                                .getAllByUorg(ServiceLocator.getUnidadOrganizativaService()
                                        .searchUnidadOrganizativaByName(newValue).getId_unidad_organizativa());
                        this.initializeTablePosicionesAgentes(instalaciones, false);
                    } else {
                        this.modifyTable(true);
                        this.instalaciones = ServiceLocator.getRegistroPosicionesAgentesService().getAll();
                        this.initializeTablePosicionesAgentes(instalaciones, true);
                    }
                }
                ));

        this.cboxUorg.editorProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.getText().equalsIgnoreCase("<<Todos>>")) {
                this.modifyTable(false);
                this.instalaciones = ServiceLocator.getRegistroPosicionesAgentesService()
                        .getAllByUorg(ServiceLocator.getUnidadOrganizativaService()
                                .searchUnidadOrganizativaByName(newValue.getText()).getId_unidad_organizativa());
                this.initializeTablePosicionesAgentes(instalaciones, false);
            } else {
                this.modifyTable(true);
                this.instalaciones = ServiceLocator.getRegistroPosicionesAgentesService().getAll();
                this.initializeTablePosicionesAgentes(instalaciones, true);
            }
        });

        this.instalaciones = ServiceLocator.getRegistroPosicionesAgentesService().getAll();
        this.initializeTablePosicionesAgentes(instalaciones, true);

        this.tablePosAgentes.setOnMouseClicked(
                event -> {
                    posicionAgenteSelected = tablePosAgentes.getSelectionModel().getSelectedItem();
                    System.out.println("ID posicion ag " + posicionAgenteSelected.getIdReg());
                    if (posicionAgenteSelected != null) {
                        instalacionSelected = ServiceLocator.getRegistroPosicionesAgentesService()
                                .getByID(posicionAgenteSelected.getIdReg());
                        System.out.println("ID instalacion " + instalacionSelected.getIdReg());
                        if (instalacionSelected != null)
                            this.enableBtns();
                        else
                            this.disableBtns();
                    }
                }
        );
    }

    private void enableBtns() {
        this.btnDelete.setDisable(false);
        this.btnEdit.setDisable(false);
    }

    private void disableBtns() {
        this.btnDelete.setDisable(true);
        this.btnEdit.setDisable(true);
    }

    private void modifyTable(boolean all) {
        disableBtns();
        if (!all) {
            this.uoColumn.setVisible(false);
            this.posicionColumn.setPrefWidth(222);
            this.proveedorColumn.setPrefWidth(94);
            this.efectivosColumn.setPrefWidth(83);
            this.diasLabColumn.setPrefWidth(104);
            this.diasNoLabColumn.setPrefWidth(93);
        } else {
            this.uoColumn.setVisible(true);
            this.uoColumn.setPrefWidth(73);
            this.posicionColumn.setPrefWidth(149);
            this.proveedorColumn.setPrefWidth(94);
            this.efectivosColumn.setPrefWidth(83);
            this.diasLabColumn.setPrefWidth(104);
            this.diasNoLabColumn.setPrefWidth(93);
        }
    }

    private void initializeTablePosicionesAgentes(List<PosicionAgente> posicionesAgentes, boolean allColumns) {
        if (!posicionesAgentes.isEmpty()) {
            this.tablePosAgentes.getItems().clear();
            ObservableList<PosicionAgente> posicionesList =
                    FXCollections.observableList(posicionesAgentes);

            this.tablePosAgentes.setItems(posicionesList);
            System.out.println(posicionesAgentes.size());

            if (allColumns) {
                this.uoColumn.setCellValueFactory(
                        cellData ->
                                new SimpleStringProperty(
                                        cellData.getValue().getUnidadOrganizativa()
                                )
                );
            }
            this.posicionColumn.setCellValueFactory(
                    cellData ->
                            new SimpleStringProperty(
                                    cellData.getValue().getInstalacion()
                            )
            );
            this.proveedorColumn.setCellValueFactory(
                    cellData ->
                            new SimpleStringProperty(
                                    cellData.getValue().getProveedor()
                            )
            );
            this.efectivosColumn.setCellValueFactory(
                    cellData ->
                            new SimpleStringProperty(
                                    String.valueOf(cellData.getValue().getCantEfectivos())
                            )
            );
            this.diasLabColumn.setCellValueFactory(
                    cellData ->
                            new SimpleStringProperty(
                                    String.valueOf(cellData.getValue().getHorasDL())
                            )
            );
            this.diasNoLabColumn.setCellValueFactory(
                    cellData ->
                            new SimpleStringProperty(
                                    String.valueOf(cellData.getValue().getHorasDNL())
                            )
            );
            this.tablePosAgentes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } else {
            this.tablePosAgentes.getItems().clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "No existen resultados para la búsqueda.");
            alert.setHeaderText(null);
            alert.setTitle("Información");
            alert.initOwner(this.dialogStage);
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNew() {
        this.tablePosAgentes.getSelectionModel().clearSelection();
        instalacionSelected = null;
        this.disableBtns();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainPosicionesAgentesController.class.getResource("RegistrarPosicionesAgentes.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Registrar Posiciones Agentes");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.initOwner(this.dialogStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            RegistrarPosicionesAgentesController controller = loader.getController();

            dialogStage.setOnCloseRequest(p -> {
                        this.initializeTablePosicionesAgentes(
                                ServiceLocator.getRegistroPosicionesAgentesService().getAll(),
                                true);
                        this.cboxUorg.getSelectionModel().select("<<Todos>>");
                        this.modifyTable(true);
                    }
            );

            controller.setDialogStage(dialogStage);
            controller.setInstalacionesAgentes(this.tablePosAgentes.getItems());
            dialogStage.showAndWait();
        } catch (IOException e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
    }

    @FXML
    private void handleEdit() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainPosicionesAgentesController.class.getResource("RegistrarPosicionesAgentes.fxml"));
            AnchorPane page = loader.load();
            Stage hechoStage = new Stage();
            hechoStage.initModality(Modality.WINDOW_MODAL);
            hechoStage.setMaximized(false);
            hechoStage.setResizable(false);
            hechoStage.initOwner(this.dialogStage);
            hechoStage.setScene(new Scene(page));
            hechoStage.setTitle("Editar Posiciones Agentes");

            RegistrarPosicionesAgentesController controller = loader.getController();

            hechoStage.setOnCloseRequest(p -> {
                        this.initializeTablePosicionesAgentes(
                                ServiceLocator.getRegistroPosicionesAgentesService().getAll(),
                                true);
                        this.cboxUorg.getSelectionModel().select("<<Todos>>");
                        this.modifyTable(true);
                    }
            );

            controller.setDialogStage(hechoStage);
            controller.setInstalacionesAgentes(this.tablePosAgentes.getItems());
            hechoStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.tablePosAgentes.getSelectionModel().clearSelection();
        instalacionSelected = null;
        this.disableBtns();
    }

    @FXML
    private void handleDelete() {
        if (instalacionSelected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Está seguro que desea eliminar el elemento seleccionado?",
                    ButtonType.OK, ButtonType.CANCEL);
            alert.initOwner(this.dialogStage);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                ServiceLocator.getRegistroPosicionesAgentesService().
                        deletePosicionAgente(instalacionSelected.getIdReg());
                Util.dialogResult("Eliminación exitosa.", Alert.AlertType.INFORMATION);
                instalaciones = ServiceLocator.getRegistroPosicionesAgentesService().getAll();
                this.initializeTablePosicionesAgentes(instalaciones, true);
                this.tablePosAgentes.getSelectionModel().clearSelection();
                this.disableBtns();
            }
        }
    }

    private void loadDialogLoading(Stage mainApp) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DialogLoadingUrl.class.getResource("DialogLoading.fxml"));
            StackPane panel = loader.load();
            dialogLoading = new Stage();
            dialogLoading.setScene(new Scene(panel));
            dialogLoading.initModality(Modality.WINDOW_MODAL);
            dialogLoading.initOwner(mainApp);
            dialogLoading.initStyle(StageStyle.UNDECORATED);
            DialogLoadingController controller = loader.getController();
            controller.setLabelText("Procesando");
            //controller.setLabelText("Cargando");
            dialogLoading.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
