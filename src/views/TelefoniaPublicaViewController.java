package views;

import com.jfoenix.controls.JFXButton;
import informes_generate.GeneradorLocator;
import informes_generate.ProcessExcelTPub;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.EstacionPublicaCentroAgente;
import models.Municipio;
import models.TpubCentroAgenteFromExcel;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginViewController;
import services.ServiceLocator;
import util.Util;
import views.dialogs.DialogLoadingController;
import views.dialogs.DialogLoadingUrl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
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
    private JFXButton inserccionExcel;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnNew;

    private UserLoggedIn logged;

    private Stage dialogStage;

    private Stage dialogLoading;

    private MainApp mainApp;

    private List<EstacionPublicaCentroAgente> estaciones;

    private static EstacionPublicaCentroAgente estacionSelected;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        this.logged = LoginViewController.getUserLoggedIn();
        this.inserccionExcel.setVisible(logged.isSuperuser());

        if (logged.isSuperuser()) {
            this.btnEdit.setVisible(true);
            this.btnDelete.setVisible(true);
            this.btnNew.setVisible(true);
        } else if (logged.hasPermiso_pases() || logged.hasPermiso_visualizacion()) {
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
                    if (Util.doesntExistsThatUorgInComboEditableEvent(newValue)) {
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
                }
                ));

        this.cboxUORG.editorProperty().addListener((observable, oldValue, newValue) -> {
            if (Util.doesntExistsThatUorgInComboEditableEvent(newValue.getText())) {
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
        this.disableBtns();
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

            hechoStage.setOnCloseRequest(p -> {
                        this.initializeTableTelefonia(
                                ServiceLocator.getEstacionPublicaCentroAgenteService().getAll(), true);
                        this.cboxUORG.getSelectionModel().select("<<Todos>>");
                        this.modifyTable(true);
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
    private void handleLoadDataFromExcelFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Excel Files 2007 And High", "*.xlsx"),
                new FileChooser.ExtensionFilter("Excel Files 97-2003", "*.xls")

        );


        List<TpubCentroAgenteFromExcel> data = new LinkedList<>();
        File fileExcel = fileChooser.showOpenDialog(this.dialogStage);

        Task<Boolean> task = new Task<Boolean>() {
            private
            List<TpubCentroAgenteFromExcel> data;

            @Override
            protected Boolean call() throws Exception {
                data = new LinkedList<>();
                if (fileExcel != null) {
                    if (fileExcel.getPath().contains(".xslx"))
                        data = ProcessExcelTPub.getDataFromExcel(fileExcel, true);
                    else
                        data = ProcessExcelTPub.getDataFromExcel(fileExcel);
                }
                for (TpubCentroAgenteFromExcel tpubEstacion : data) {
                    String municipioName = tpubEstacion.getMunicipio();
                    try {
                        Municipio municipio = ServiceLocator.getMunicipiosService().searchMunicipioByName(tpubEstacion.getMunicipio());
                        ServiceLocator.getEstacionPublicaCentroAgenteService().updateEstacionPublicaByIdMunicipio(
                                municipio.getId_municipio(),
                                tpubEstacion.getCentroAgentes(),
                                tpubEstacion.getTpubs()
                        );
                    } catch (Exception e) {
                        System.out.println("Error con el municipio: " + municipioName);
                        e.printStackTrace();
                    }
                }
                return true;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                dialogLoading.close();
                initializeTableTelefonia(ServiceLocator.getEstacionPublicaCentroAgenteService().getAll(), true);
            }

            @Override
            protected void failed() {
                super.failed();
                dialogLoading.close();
                Util.dialogResult("Error", Alert.AlertType.ERROR);
            }
        };

        Thread th = new Thread(task);
        loadDialogLoading(this.dialogStage);
        th.start();
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

            hechoStage.setOnCloseRequest(p -> {
                        this.initializeTableTelefonia(
                                ServiceLocator.getEstacionPublicaCentroAgenteService().getAll(), true);
                        this.cboxUORG.getSelectionModel().select("<<Todos>>");
                        this.modifyTable(true);
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
        if (estacionSelected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Está seguro que desea eliminar el elemento seleccionado?",
                    ButtonType.OK, ButtonType.CANCEL);
            alert.initOwner(this.dialogStage);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
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

    public static EstacionPublicaCentroAgente getEstacionPublicaCentroAgenteSelected() {
        return estacionSelected;
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

    @FXML
    private void generarTotales() {
        LinkedList<EstacionPublicaCentroAgente> estacionPublicaCentroAgentes = new LinkedList<>();
        for (String unidad : Util.unidades_organizativa) {
            this.tableEstaciones.getItems()
                    .stream()
                    .filter(filter -> filter.getUnidadOrganizativa().equalsIgnoreCase(unidad))
                    .forEach(estacion -> {
                        if (estacionPublicaCentroAgentes.stream().filter(filter -> filter.getUnidadOrganizativa().equalsIgnoreCase(unidad)).count() < 1) {
                            estacionPublicaCentroAgentes.add(estacion);
                        } else {
                            estacionPublicaCentroAgentes.stream()
                                    .filter(p -> p.getUnidadOrganizativa()
                                            .equalsIgnoreCase(estacion.getUnidadOrganizativa()))
                                    .forEach(state -> {
                                        state.setIdReg(estacion.getIdReg());
                                        state.setUnidadOrganizativa(estacion.getUnidadOrganizativa());
                                        state.setMunicipio(estacion.getMunicipio());
                                        state.setEstacionPublica(estacion.getEstacionPublica() + state.getEstacionPublica());
                                        state.setCentroAgente(estacion.getCentroAgente() + state.getCentroAgente());
                                    });

                        }
                    });
        }


        String path = Util.selectPathToSaveDatabase(this.dialogStage);
        if (path != null) {
            Task<Boolean> task = new Task<Boolean>() {
                boolean result = false;

                @Override
                protected Boolean call() throws Exception {
                    this.result = GeneradorLocator.getGenerateTotalesTpub().generarTotales(path, estacionPublicaCentroAgentes);
                    return result;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    dialogLoading.close();
                    String file = path + "/TotalesDesnsidad.xlsx";
                    try {
                        Runtime.getRuntime().exec("cmd /c start " + file);
                    } catch (Exception e) {
                        System.out.println("Error al abrir el archivo");
                    }
                }
            };
            Thread th = new Thread(task);
            loadDialogLoading(this.dialogStage);
            th.start();

        }
    }
}
