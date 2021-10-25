package views;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.dialog.ExceptionDialog;
import services.ServiceLocator;
import util.Util;
import views.dialogs.DialogLoadingUrl;
import views.dialogs.InformacionHechoViewController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BuscarHechosViewController {
    @FXML
    private ComboBox<String> tipoHecho;
    @FXML
    private ComboBox<String> unidadOrganizativa;
    @FXML
    private ComboBox<String> cboxAnno;
    @FXML
    private ComboBox<String> cboxMes;
    @FXML
    private TableView<HechosBusqueda> tabla;
    @FXML
    private TableColumn<HechosBusqueda, String> tituloHechoColumn;
    @FXML
    private TableColumn<HechosBusqueda, String> fechaHechoColumn;
    @FXML
    private TableColumn<HechosBusqueda, String> unidadOrgColumn;
    @FXML
    private TableColumn<HechosBusqueda, String> codCDNTColumn;

    private static Hechos hechoSelected;

    private Stage dialogStage;

    private Stage mainApp;

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
        this.tipoHecho.getItems().setAll(this.getAllTipoHechos());
        this.unidadOrganizativa.getItems().setAll(this.getUnidadesOrganizativas());
        this.cboxAnno.getItems().setAll(getAllAnnos());
        this.cboxMes.getItems().addAll(Util.meses2);
        TextFields.bindAutoCompletion(unidadOrganizativa.getEditor(), unidadOrganizativa.getItems());
        this.tabla.setOnMouseClicked(event ->
                this.showDialogHechoInformation()
        );
    }

    private List<String> getAllAnnos(){
        return ServiceLocator.getAnnoServicio().allAnno().stream().map(Anno::getAnno)
                .map(integer -> integer.toString()).collect(Collectors.toList());
    }

    private List<String> getAllTipoHechos() {
        return ServiceLocator.getTipoHechoService().fetchAll().stream().map(TipoHecho::getTipo_hecho).collect(Collectors.toList());
    }

    private List<String> getUnidadesOrganizativas() {
        return ServiceLocator.getUnidadOrganizativaService().fetchAll().stream().map(UnidadOrganizativa::getUnidad_organizativa).collect(Collectors.toList());
    }

    private String obtenerMesFromComboBoxMeses() {
        String devolver;
        try {
            devolver = this.cboxMes.getValue();
        } catch (NullPointerException e) {
            devolver = "<<Todos>>";
        }
        return devolver;
    }

    private void showDialogHechoInformation(){
        if(this.tabla.getSelectionModel().getSelectedItem() != null) {
            HechosBusqueda hb = (HechosBusqueda) this.tabla.getSelectionModel().getSelectedItem();
            hechoSelected = ServiceLocator.getHechosService().getHecho(hb.getIdReg());
            if (hechoSelected != null) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(DialogLoadingUrl.class.getResource("InformacionHechoView.fxml"));
                    AnchorPane anchorPane = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Información del hecho seleccionado");
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(this.dialogStage);
                    stage.setResizable(false);
                    stage.setMaximized(false);
                    stage.setScene(new Scene(anchorPane));

                    InformacionHechoViewController controller = loader.getController();
                    controller.setDialogStage(stage);
                    stage.showAndWait();
                } catch (IOException e) {
                    ExceptionDialog dialog = new ExceptionDialog(e);
                    dialog.showAndWait();
                }
            }
        }
    }

    @FXML
    private void buscar() {
        if(this.unidadOrganizativa.getSelectionModel().isEmpty() &&
            this.cboxMes.getSelectionModel().isEmpty() &&
            this.cboxAnno.getSelectionModel().isEmpty() &&
            this.tipoHecho.getSelectionModel().isEmpty()){

            Util.dialogResult("Campos de búsqueda vacíos.", Alert.AlertType.WARNING);

        } else {
            String unidadOrganizativaSql = unidadOrganizativa.getSelectionModel().isEmpty()
                    ? ""
                    : " and id_uorg=" +
                    ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName(
                            unidadOrganizativa.getSelectionModel().getSelectedItem()
                    ).getId_unidad_organizativa();

            String tipoHechoSql = tipoHecho.getSelectionModel().isEmpty()
                    ? ""
                    : " and id_tipo_hecho=" +
                    ServiceLocator.getTipoHechoService().searchTipoHechoByName(
                            tipoHecho.getSelectionModel().getSelectedItem()
                    ).getId_tipo_hecho();

            String annoSql = cboxAnno.getSelectionModel().isEmpty()
                    ? ""
                    : " and date_part('year', hechos.fecha_ocurrencia) = "
                    + Integer.parseInt(cboxAnno.getSelectionModel().getSelectedItem());

            String mesSql = cboxMes.getSelectionModel().isEmpty()
                    ? ""
                    : " and date_part('mons', hechos.fecha_ocurrencia) = "
                    + Util.obtenerNumeroMes(obtenerMesFromComboBoxMeses());

            List<HechosBusqueda> hechos = new LinkedList<>();

            String query = "SELECT id_reg, id_uorg, fecha_ocurrencia, cod_cdnt, titulo FROM hechos WHERE id_reg > 0"
                    + unidadOrganizativaSql + tipoHechoSql + annoSql + mesSql;

            try {
                ResultSet rs = Util.executeQuery(query);
                while(rs.next())
                    hechos.add(new HechosBusqueda(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getDate(3),
                            rs.getString(4),
                            rs.getString(5)
                    ));
            } catch (SQLException e){
                e.printStackTrace();
            }

            if(!hechos.isEmpty()) {
                loadTable(hechos);
            } else{
                this.tabla.getItems().clear();
                Util.dialogResult("No se han encontrado resultados para la búsqueda.", Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    private void cleanSearchFields(){
        this.tabla.getItems().clear();
        this.cboxAnno.getSelectionModel().clearSelection();
        this.cboxAnno.setPromptText("Seleccione");
        this.cboxMes.getSelectionModel().clearSelection();
        this.cboxMes.setPromptText("Seleccione");
        this.unidadOrganizativa.getSelectionModel().clearSelection();
        this.tipoHecho.getSelectionModel().clearSelection();
        this.tipoHecho.setPromptText("Seleccione");
    }

    private void loadTable(List<HechosBusqueda> hechos) {
        tabla.getItems().clear();
        ObservableList<HechosBusqueda> observableList = FXCollections.observableList(hechos);
        tabla.setItems(observableList);
        this.unidadOrgColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        ServiceLocator.getUnidadOrganizativaService()
                        .getOneUnidadOrganizativa(cellData.getValue().getIdUnidadOrganizativa()).getUnidad_organizativa()
                )
        );
        this.fechaHechoColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        cellData.getValue().getFechaOcurrencia().toString()
                )
        );
        this.tituloHechoColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        cellData.getValue().getDescripcion()
                )
        );
        this.codCDNTColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                        cellData.getValue().getCodCDNT() == null ? "(sin información)" : cellData.getValue().getCodCDNT().toUpperCase()
                )
        );
    }
}