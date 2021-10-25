package views;

import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Hechos;
import models.Municipio;
import models.TipoHecho;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.dialog.ExceptionDialog;
import services.ServiceLocator;
import util.Util;
import views.dialogs.DialogLoadingController;
import views.dialogs.DialogLoadingUrl;

import java.io.IOException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BuscarViewController {
    @FXML
    private JFXToggleButton activarFiltros;
    @FXML
    private TextField titulo;
    @FXML
    private ComboBox<String> tipoHecho;
    @FXML
    private ComboBox<String> municipio;
    @FXML
    private ComboBox<String> unidadOrganizativa;
    @FXML
    private DatePicker fecha;
    @FXML
    private TitledPane paneFilter;
    @FXML
    private TableView tabla;
    @FXML
    private TableColumn<Hechos, String> tituloHechoColumn;
    @FXML
    private TableColumn<Hechos, String> fechaHechoColumn;
    @FXML
    private TableColumn<Hechos, String> tipoHechoColumn;
    @FXML
    private TableColumn<Hechos, String> unidadOrgColumn;
    @FXML
    private TableColumn<Hechos, String> codCDNTColumn;

    private static Hechos hechoSelected;

    private Stage dialogStage;

    private Stage mainApp;

    private Boolean activoOrDeactive = false;

    public static Hechos getHechoSeleccionado(){
        if(hechoSelected == null)
            hechoSelected = new Hechos();
        return hechoSelected;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

    }

    public void setMainApp(Stage stage) {
        mainApp = stage;
    }

    @FXML
    private void initialize() {

        municipio.setEditable(true);
        unidadOrganizativa.setEditable(true);

        paneFilter.setVisible(false);
        activarFiltros.setOnAction(event -> {
            if (activoOrDeactive) {
                activarFiltros.setText("Activar filtros");
                activoOrDeactive = false;
                paneFilter.setCollapsible(false);
                paneFilter.setDisable(true);
                paneFilter.setExpanded(false);
                paneFilter.setVisible(false);
                tabla.setPrefHeight(368);
                tabla.setPrefWidth(631);
                tabla.setLayoutX(0);
                tabla.setLayoutY(155);

            } else {
                activarFiltros.setText("Desactivar filtros");
                activoOrDeactive = true;
                paneFilter.setCollapsible(true);
                paneFilter.setDisable(false);
                paneFilter.setExpanded(true);
                paneFilter.setVisible(true);
                tabla.setPrefHeight(255);
                tabla.setLayoutY(269);
            }
        });

        tipoHecho.getItems().setAll(this.getAllTipoHechos());
        municipio.getItems().setAll(this.getAllMunicipios());
        unidadOrganizativa.getItems().setAll(this.getUnidadesOrganizativas());
        TextFields.bindAutoCompletion(municipio.getEditor(), municipio.getItems());
        TextFields.bindAutoCompletion(unidadOrganizativa.getEditor(), unidadOrganizativa.getItems());
    }


    private List<String> getAllTipoHechos() {
        return ServiceLocator.getTipoHechoService().fetchAll().stream().map(TipoHecho::getTipo_hecho).collect(Collectors.toList());
    }

    private List<String> getAllMunicipios() {
        return ServiceLocator.getMunicipiosService().fetchAll().stream().map(Municipio::getMunicipio).collect(Collectors.toList());
    }

    private List<String> getUnidadesOrganizativas() {
        return ServiceLocator.getUnidadOrganizativaService().fetchAll().stream().map(UnidadOrganizativa::getUnidad_organizativa).collect(Collectors.toList());
    }

    /*private void showDialogRegister(){
        if(this.tabla.getSelectionModel().getSelectedItem() != null) {
            hechoSelected = (Hechos)this.tabla.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(PrincipalViewController.class.getResource("RegistrarView.fxml"));
                AnchorPane anchorPane = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Registrar Hecho");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(this.dialogStage);
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.setScene(new Scene(anchorPane));

                RegistrarViewController controller = loader.getController();
                controller.setDialogStage(stage);
                controller.setFromPrincipal(false);
                stage.showAndWait();
            } catch (IOException e) {
                ExceptionDialog dialog = new ExceptionDialog(e);
                dialog.showAndWait();
            }
        }
    }*/

    @FXML
    private void buscar() {
        Task<Boolean> tarea = new Task<Boolean>() {
            List<Hechos> hechos = new LinkedList<>();

            @Override
            protected Boolean call() throws Exception {
                if (activoOrDeactive) {
                    String municipioSql = municipio.getSelectionModel().isEmpty()
                            ? ""
                            : " and id_municipio=" +
                            ServiceLocator.getMunicipiosService().searchMunicipioByName(
                                    municipio.getSelectionModel().getSelectedItem()
                            ).getId_municipio();
                    String unidadOrganizativaSql = unidadOrganizativa.getSelectionModel().isEmpty()
                            ? ""
                            : " and id_uorg=" +
                            ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName(
                                    unidadOrganizativa.getSelectionModel().getSelectedItem()
                            ).getId_unidad_organizativa();
                    String fechaSql = fecha.getValue() == null
                            ? ""
                            : " and fecha_ocurrencia='" + Date.valueOf(fecha.getValue()) + "'";
                    String tipoHechoSql = tipoHecho.getSelectionModel().isEmpty()
                            ? ""
                            : " and id_tipo_hecho=" +
                            ServiceLocator.getTipoHechoService().searchTipoHechoByName(
                                    tipoHecho.getSelectionModel().getSelectedItem()
                            ).getId_tipo_hecho();
                    hechos = titulo.getText().equalsIgnoreCase("*") || titulo.getText().equalsIgnoreCase("'*'")
                            ? ServiceLocator.getHechosService().getHechosBySqlExpresion(
                            "SELECT * FROM hechos WHERE id_reg>0"
                                    + municipioSql + unidadOrganizativaSql + fechaSql + tipoHechoSql
                    )
                            : ServiceLocator.getHechosService().getHechosBySqlExpresion(
                            "SELECT * FROM hechos WHERE titulo='" + titulo.getText() + "'"
                                    + municipioSql + unidadOrganizativaSql + fechaSql + tipoHechoSql
                    );

                } else {
                    hechos = titulo.getText().equalsIgnoreCase("*")
                            ? ServiceLocator.getHechosService().getHechosBySqlExpresion(
                            "SELECT * FROM hechos")
                            : ServiceLocator.getHechosService().getHechosBySqlExpresion(
                            "SELECT * FROM hechos WHERE titulo='" + titulo.getText() + "'");


                }
                synchronized (this) {
                    this.wait(3000);
                    loadTable(hechos);
                }
                return null;
            }


            @Override
            protected void succeeded() {
                super.succeeded();
                dialogStage.close();
            }
        };

        if (titulo.getText().isEmpty()) {
            Util.dialogResult("Introduzca el titulo", Alert.AlertType.ERROR);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(DialogLoadingUrl.class.getResource("DialogLoading.fxml"));
                StackPane panel = loader.load();
                dialogStage = new Stage();

                dialogStage.initOwner(mainApp);
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initStyle(StageStyle.TRANSPARENT);
                panel.setStyle(
                        "-fx-background-color: rgba(144,144,144,0.5);" +
                                "-fx-background-insets: 50;"
                );

                Scene scene = new Scene(panel);
                scene.setFill(Color.TRANSPARENT);
                dialogStage.setScene(scene);
                //dialogStage.setScene(new Scene(panel));
                DialogLoadingController controller = loader.getController();
                controller.setLabelText("Cargando");
                dialogStage.showAndWait();

                Thread th = new Thread(tarea);
                th.setDaemon(true);
                th.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadTable(List<Hechos> hechos) {

        tabla.getItems().clear();
        ObservableList<Hechos> observableList = FXCollections.observableList(hechos);
        tabla.setItems(observableList);
        this.fechaHechoColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        cellData.getValue().getFecha_ocurrencia().toString()
                )
        );
       /*this.tipoHechoColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        cellData.getValue().getTipoHecho().getTipo_hecho()
                )
        );*/
        this.tituloHechoColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        cellData.getValue().getTitulo()
                )
        );
        this.unidadOrgColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        cellData.getValue().getUnidadOrganizativa().getUnidad_organizativa()
                )
        );
        this.codCDNTColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        cellData.getValue().getCod_cdnt() == null ? "(sin información)" : cellData.getValue().getCod_cdnt().toUpperCase()
                )
        );
    }
}
