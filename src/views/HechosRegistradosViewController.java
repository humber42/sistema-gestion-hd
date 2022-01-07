package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import informes_generate.GeneradorLocator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.dialog.ExceptionDialog;
import seguridad.models.UserLoggedIn;
import seguridad.views.LoginViewController;
import services.ServiceLocator;
import util.Util;
import views.dialogs.DialogLoadingController;
import views.dialogs.DialogLoadingUrl;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HechosRegistradosViewController {
    private static final int LIMIT_OF_ROWS = 18;

    private MainApp mainApp;

    @FXML
    private TableView<HechosRegistrados> hechosTable;
    @FXML
    private TableColumn<HechosRegistrados, String> numeroHechosColum;
    @FXML
    private TableColumn<HechosRegistrados, String> codCDNTColum;
    @FXML
    private TableColumn<HechosRegistrados, String> uniOrgColum;
    @FXML
    private TableColumn<HechosRegistrados, String> tipoColum;
    @FXML
    private TableColumn<HechosRegistrados, String> municipioColum;
    @FXML
    private TableColumn<HechosRegistrados, String> ocurrenciaColum;
    @FXML
    private TableColumn<HechosRegistrados, String> sintesisColum;

    private Stage dialogStage;
    @FXML
    private TextArea hechoArea;
    @FXML
    private Label municipioLabel;
    @FXML
    private DatePicker ocurreDate;
    @FXML
    private DatePicker parteDate;
    @FXML
    private Label tipoHechoLabel;
    @FXML
    private JFXTextField centroField;
    @FXML
    private JFXTextField lugarField;
    @FXML
    private JFXButton buttonEliminar;
    @FXML
    private JFXButton buttonEditar;
    @FXML
    private JFXButton btnClean;
    @FXML
    private TitledPane infoHechoSelected;

    @FXML
    private TextField offsetMaximoField;
    @FXML
    private TextField offsetField;
    @FXML
    private ComboBox<String> cboxAnno;
    @FXML
    private ComboBox<String> cboxUorg;
    @FXML
    private ComboBox<String> cboxTipoHecho;

    private HechosRegistrados hechoRegistradoSelected;

    private UserLoggedIn logged;

    private Stage stage;

    private int offsetMaximo;
    private int offset;

    private boolean usingFilters;

    private String annoPartQuery;
    private String uorgPartQuery;
    private String tipoHechoPartQuery;

    private static Hechos hechoSeleccionado;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public HechosRegistradosViewController() {
    }

    public static Hechos getHechoSeleccionado() {
        if (hechoSeleccionado == null)
            hechoSeleccionado = new Hechos();
        return hechoSeleccionado;
    }

    public static void setHechoSeleccionado(Hechos hechoSelect) {
        hechoSeleccionado = hechoSelect;
    }

    @FXML
    private void initialize() {
        this.logged = LoginViewController.getUserLoggedIn();
        this.usingFilters = false;

        if (logged.hasPermiso_visualizacion() || logged.hasPermiso_pases()) {
            this.buttonEditar.setVisible(false);
            this.buttonEliminar.setVisible(false);
            //this.infoHechoSelected.setVisible(false);
            //this.btnNuevo.setVisible(false);
        } else if (logged.isSuperuser()) {
            this.buttonEditar.setVisible(true);
            this.buttonEliminar.setVisible(true);
            //this.infoHechoSelected.setVisible(true);
            //this.btnNuevo.setVisible(true);
        }

        buttonEditar.setDisable(true);
        buttonEliminar.setDisable(true);
        btnClean.setDisable(true);
        municipioLabel.setDisable(true);
        tipoHechoLabel.setDisable(true);
        hechoArea.setDisable(true);
        ocurreDate.setDisable(true);
        parteDate.setDisable(true);
        centroField.setDisable(true);
        lugarField.setDisable(true);
        cargarTabla(this.obtenerHechosRegistrados(LIMIT_OF_ROWS, 0));
        offset = 0;
        offsetMaximo = ServiceLocator.getHechosService().countAllHechos();

        hechosTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    showHechoDetails(newValue);
                    this.hechoRegistradoSelected = newValue;
                    buttonEditar.setDisable(false);
                    buttonEliminar.setDisable(false);
                    btnClean.setDisable(false);
                    hechoArea.setDisable(false);
                    ocurreDate.setDisable(false);
                    parteDate.setDisable(false);
                    centroField.setDisable(false);
                    lugarField.setDisable(false);
                    municipioLabel.setDisable(false);
                    tipoHechoLabel.setDisable(false);
                });
        offsetMaximoField.setEditable(false);
        ponerNumeroDeTablas();
        ponerOffsetDetablaActual();
        this.cboxUorg.getItems().setAll(
                ServiceLocator.getUnidadOrganizativaService()
                        .fetchAll()
                        .stream()
                        .map(UnidadOrganizativa::getUnidad_organizativa)
                        .collect(Collectors.toList())
        );
        this.cboxAnno.getItems().setAll(
                ServiceLocator.getAnnoServicio()
                        .allAnno()
                        .stream()
                        .map(Anno::getAnno)
                        .map(integer -> integer.toString())
                        .collect(Collectors.toList())
        );
        this.cboxTipoHecho.getItems().setAll(
                ServiceLocator.getTipoHechoService()
                        .fetchAll()
                        .stream()
                        .map(TipoHecho::getTipo_hecho)
                        .collect(Collectors.toList())
        );
        TextFields.bindAutoCompletion(this.cboxUorg.getEditor(), this.cboxUorg.getItems());
        TextFields.bindAutoCompletion(this.cboxAnno.getEditor(), this.cboxAnno.getItems());
    }

    private Hechos getHechoSelected() {
        Hechos h = null;
        if (hechoRegistradoSelected != null) {
            h = ServiceLocator.getHechosService().getHecho(hechoRegistradoSelected.getId_reg());
        }
        return h;
    }

    private void ponerOffsetDetablaActual() {
        int resto = offset % LIMIT_OF_ROWS;
        int count = offset / LIMIT_OF_ROWS;

        if (resto != 0) {
            offsetField.setText(Integer.toString(count + 1));
        } else {
            offsetField.setText(Integer.toString(count + 1));
        }
    }

    private void ponerNumeroDeTablas() {
        int resto = offsetMaximo % LIMIT_OF_ROWS;
        int count = offsetMaximo / LIMIT_OF_ROWS;
        if (resto != 0) {
            offsetMaximoField.setText(Integer.toString(count + 1));
        } else {
            offsetMaximoField.setText(Integer.toString(count));
        }
    }

    @FXML
    private void buscarTabla() {
        try {
            int valueOfOffsetField = Integer.parseInt(offsetField.getText());
            int busqueda = valueOfOffsetField - 1;
            offset = busqueda * LIMIT_OF_ROWS;
            if (offset > offsetMaximo || valueOfOffsetField <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Entrada Incorrecta");
                alert.setContentText("Por favor, introduzca valores entre 0 y " + offsetMaximoField.getText());
                alert.showAndWait();
            } else {
                if (usingFilters) {
                    cargarTabla(this.obtenerHechosUsingFilters(annoPartQuery, uorgPartQuery,
                            tipoHechoPartQuery, LIMIT_OF_ROWS, offset));
                } else {
                    cargarTabla(this.obtenerHechosRegistrados(LIMIT_OF_ROWS, offset));
                }
            }
        } catch (NumberFormatException e) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("Por favor, solo se aceptan números entre 0 y " + offsetMaximoField.getText());
            alert1.setHeaderText(null);
            alert1.setTitle("Entrada Incorrecta");
            alert1.showAndWait();
        }
    }
//
//    private void converToEditable(){
//        if (hechoRegistradoSelected!=null){
//            hechoArea.setEditable(true);
//            ocurreDate.setEditable(true);
//            parteDate.setEditable(true);
//            centroField.setEditable(true);
//            lugarField.setEditable(true);
//        }
//    }

    private void showHechoDetails(HechosRegistrados selected) {
        if (selected != null) {
            Hechos hechos = ServiceLocator.getHechosService().getHecho(selected.getId_reg());
            if (hechos != null) {
                hechoArea.setText(hechos.getTitulo());
                municipioLabel.setText(hechos.getMunicipio().getMunicipio());
                ocurreDate.getEditor().setText(hechos.getFecha_ocurrencia().toString());
                parteDate.getEditor().setText(hechos.getFecha_parte().toString());
                tipoHechoLabel.setText(hechos.getTipoHecho().getTipo_hecho());
                centroField.setText(hechos.getCentro());
                lugarField.setText(hechos.getLugar());
            }
        } else {
            hechoArea.setText("");
            municipioLabel.setText("");
            ocurreDate.setDayCellFactory(null);
            parteDate.setDayCellFactory(null);
            tipoHechoLabel.setText("");
            centroField.setText("");
            lugarField.setText("");
        }
    }

    @FXML
    private void handleEdit() {
        if (this.getHechoSelected() != null) {
            hechoSeleccionado = this.getHechoSelected();
            this.openDialogRegisterFact();
        }
    }

    private void openDialogRegisterFact() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PrincipalViewController.class.getResource("RegistrarView.fxml"));
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Modificar Hecho");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.stage);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene(anchorPane));

            RegistrarViewController controller = loader.getController();
            controller.setDialogStage(stage);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* @FXML
    private boolean showDialogToEdit(){
        try {
            Hechos hecho = getHechoSelected();
            if (hechoArea.getText().isEmpty() && centroField.getText().isEmpty() && lugarField.getText().isEmpty() ){
                Util.dialogResult("Campo vacío", Alert.AlertType.ERROR);
                hechoArea.setText(hecho.getTitulo());
               // hechoArea.setUnFocusColor(Paint.valueOf("red"));
               // hechoArea.setFocusColor(Paint.valueOf("red"));
                centroField.setText(hecho.getCentro());
                centroField.setUnFocusColor(Paint.valueOf("red"));
                centroField.setFocusColor(Paint.valueOf("red"));
                lugarField.setText(hecho.getLugar());
                lugarField.setUnFocusColor(Paint.valueOf("red"));
                lugarField.setFocusColor(Paint.valueOf("red"));
            }
            else if (hechoArea.getText().isEmpty() && centroField.getText().isEmpty()){
                hechoArea.setText(hecho.getTitulo());
              //  hechoField.setUnFocusColor(Paint.valueOf("red"));
              //  hechoField.setFocusColor(Paint.valueOf("red"));
                centroField.setText(hecho.getCentro());
                centroField.setUnFocusColor(Paint.valueOf("red"));
                centroField.setFocusColor(Paint.valueOf("red"));
            }
            else if (hechoArea.getText().isEmpty() && lugarField.getText().isEmpty()){
                hechoArea.setText(hecho.getTitulo());
               // hechoField.setUnFocusColor(Paint.valueOf("red"));
              //  hechoField.setFocusColor(Paint.valueOf("red"));
                lugarField.setText(hecho.getLugar());
                lugarField.setUnFocusColor(Paint.valueOf("red"));
                lugarField.setFocusColor(Paint.valueOf("red"));
            }
            else if (centroField.getText().isEmpty() && lugarField.getText().isEmpty()){
                centroField.setText(hecho.getCentro());
                centroField.setUnFocusColor(Paint.valueOf("red"));
                centroField.setFocusColor(Paint.valueOf("red"));
                lugarField.setText(hecho.getLugar());
                lugarField.setUnFocusColor(Paint.valueOf("red"));
                lugarField.setFocusColor(Paint.valueOf("red"));
            }
            else if (hechoArea.getText().isEmpty()){
                hechoArea.setText(hecho.getTitulo());
               // hechoField.setUnFocusColor(Paint.valueOf("red"));
              //  hechoField.setFocusColor(Paint.valueOf("red"));
            }
            else if (centroField.getText().isEmpty()){
                centroField.setText(hecho.getCentro());
                centroField.setUnFocusColor(Paint.valueOf("red"));
                centroField.setFocusColor(Paint.valueOf("red"));
            }
            else if (lugarField.getText().isEmpty()){
                lugarField.setText(hecho.getLugar());
                lugarField.setUnFocusColor(Paint.valueOf("red"));
                lugarField.setFocusColor(Paint.valueOf("red"));
            }
            else {
                Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmación");
                alert.setHeaderText(null);
                alert.setContentText("Desea editar este hecho");
                Optional<ButtonType> confirmacion = alert.showAndWait();
                hecho.setTitulo(hechoArea.getText());
                hecho.setCentro(centroField.getText());
                hecho.setLugar(lugarField.getText());
                ServiceLocator.getHechosService().editarHechos(hecho);

                desactivarButton();
               // hechoField.setFocusColor(Paint.valueOf("blue"));
               // hechoField.setUnFocusColor(Paint.valueOf("white"));
                centroField.setFocusColor(Paint.valueOf("blue"));
                centroField.setUnFocusColor(Paint.valueOf("white"));
                lugarField.setFocusColor(Paint.valueOf("blue"));
                lugarField.setUnFocusColor(Paint.valueOf("white"));
            }
            cargarTabla(this.obtenerHechosRegistrados(LIMIT_OF_ROWS, offset));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }
    */

    private void desactivarButton() {
        buttonEliminar.setDisable(true);
        buttonEditar.setDisable(true);
        btnClean.setDisable(true);
        municipioLabel.setDisable(true);
        tipoHechoLabel.setDisable(true);
        hechoArea.setDisable(true);
        ocurreDate.setDisable(true);
        parteDate.setDisable(true);
        centroField.setDisable(true);
        lugarField.setDisable(true);
    }

    @FXML
    private void deleteHechos() {
        try {
            Hechos hecho = getHechoSelected();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText(null);
            alert.setContentText("Desea eliminar este hecho");
            Optional<ButtonType> confirmacion = alert.showAndWait();
            if (confirmacion.get().equals(ButtonType.OK)) {
                ServiceLocator.getHechosService().eliminarHechos(hecho);
                desactivarButton();
            } else {
                this.hechosTable.getSelectionModel().clearSelection();
                desactivarButton();
            }
            cargarTabla(this.obtenerHechosRegistrados(LIMIT_OF_ROWS, offset));
            ponerNumeroDeTablas();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleNext() {
        offset += LIMIT_OF_ROWS;
        int howmuch = offsetMaximo - offset;

        try {
            if (howmuch > 0) {
                if (usingFilters) {
                    cargarTabla(this.obtenerHechosUsingFilters(annoPartQuery, uorgPartQuery,
                            tipoHechoPartQuery, LIMIT_OF_ROWS, offset));
                } else {
                    cargarTabla(this.obtenerHechosRegistrados(LIMIT_OF_ROWS, offset));
                }
            } else {
                dialogElemento("último");
                offset -= LIMIT_OF_ROWS;
            }
            desactivarButton();
            showHechoDetails(null);
        } catch (IndexOutOfBoundsException e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
        ponerOffsetDetablaActual();
    }

    @FXML
    public void handlerPrevious() {
        int howmuch = offsetMaximo - offset;
        offset -= LIMIT_OF_ROWS;
        try {
            if (howmuch == 0) {
                if (usingFilters) {
                    cargarTabla(this.obtenerHechosUsingFilters(annoPartQuery, uorgPartQuery,
                            tipoHechoPartQuery, LIMIT_OF_ROWS, offset));
                } else {
                    cargarTabla(this.obtenerHechosRegistrados(LIMIT_OF_ROWS, offsetMaximo - (LIMIT_OF_ROWS * 2)));
                    offset -= LIMIT_OF_ROWS;
                }
            } else {
                if (offset >= 0) {
                    if (usingFilters) {
                        cargarTabla(this.obtenerHechosUsingFilters(annoPartQuery, uorgPartQuery,
                                tipoHechoPartQuery, LIMIT_OF_ROWS, offset));
                    } else {
                        cargarTabla(this.obtenerHechosRegistrados(LIMIT_OF_ROWS, offset));
                    }
                } else {
                    offset += LIMIT_OF_ROWS;
                    dialogElemento("primer");
                }
            }
            showHechoDetails(null);
            desactivarButton();
        } catch (IndexOutOfBoundsException e) {
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();
        }
        ponerOffsetDetablaActual();
    }

    @FXML
    private void handleFirst() {
        if (usingFilters) {
            cargarTabla(this.obtenerHechosUsingFilters(annoPartQuery, uorgPartQuery, tipoHechoPartQuery, LIMIT_OF_ROWS, 0));
        } else {
            cargarTabla(this.obtenerHechosRegistrados(LIMIT_OF_ROWS, 0));
        }
        offset = 0;
        showHechoDetails(null);
        desactivarButton();
        ponerOffsetDetablaActual();
    }

    @FXML
    private void handleLast() {
        if (usingFilters) {
            cargarTabla(this.obtenerHechosUsingFilters(annoPartQuery, uorgPartQuery, tipoHechoPartQuery, LIMIT_OF_ROWS, offsetMaximo - LIMIT_OF_ROWS));
        } else {
            int pos = offsetMaximo - LIMIT_OF_ROWS;
            cargarTabla(this.obtenerHechosRegistrados(LIMIT_OF_ROWS, pos));
        }
        offset = offsetMaximo;
        showHechoDetails(null);
        desactivarButton();
        ponerOffsetDetablaActual();
    }

    private void dialogElemento(String estado) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información");
        alert.setContentText("Usted se encuentra en el " + estado + " elemento");
        alert.showAndWait();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //personTable.setItems(mainApp.getPersonData());
    }

    private void cargarTabla(List<HechosRegistrados> hechos) {
        ObservableList<HechosRegistrados> observableList = FXCollections.observableList(hechos);
        hechosTable.setItems(observableList);
        numeroHechosColum.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                String.valueOf(cellData.getValue().getId_reg())
                        )
        );
        codCDNTColum.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCodCDNT() == null ? "(no info)" : cellData.getValue().getCodCDNT().toUpperCase()));
        uniOrgColum.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUo()));
        tipoColum.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTipoHecho()));
        municipioColum.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMunicipio()));
        ocurrenciaColum.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaOcurre().toString()));
        sintesisColum.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTitulo()));
    }

    @FXML
    private void handleCleanFieldsEdition() {
        this.desactivarButton();
        this.hechoArea.setText(null);
        this.tipoHechoLabel.setText(null);
        this.municipioLabel.setText(null);
        this.ocurreDate.setValue(null);
        this.parteDate.setValue(null);
        this.centroField.setText(null);
        this.lugarField.setText(null);
    }

    @FXML
    private void handleCleanFieldsSearch() {
        this.cboxTipoHecho.getSelectionModel().clearSelection();
        this.cboxTipoHecho.setPromptText("Seleccione");
        this.cboxAnno.getSelectionModel().clearSelection();
        this.cboxAnno.setPromptText("Seleccione");
        this.cboxUorg.getSelectionModel().clearSelection();
        this.cboxUorg.setPromptText("Seleccione");
        cargarTabla(this.obtenerHechosRegistrados(LIMIT_OF_ROWS, 0));
        offsetMaximo = ServiceLocator.getHechosService().countAllHechos();
        offset = 0;
        ponerOffsetDetablaActual();
        ponerNumeroDeTablas();
        this.usingFilters = false;
    }

    @FXML
    private void handleSearch() {
        offset = 0;
        ponerNumeroDeTablas();
        String anno = this.cboxAnno.getSelectionModel().getSelectedItem();
        String tipoHecho = this.cboxTipoHecho.getSelectionModel().getSelectedItem();
        String uorg = this.cboxUorg.getSelectionModel().getSelectedItem();

        if ((anno == null || anno.isEmpty()) &&
                (uorg == null || uorg.isEmpty()) &&
                (tipoHecho == null || tipoHecho.isEmpty())) {
            Util.dialogResult("Los campos de búsqueda están vacíos.", Alert.AlertType.WARNING);
        } else {
            annoPartQuery = (anno == null || anno.isEmpty())
                    ? "" :
                    " AND date_part('year', hechos.fecha_ocurrencia) = " + Integer.parseInt(anno);
            tipoHechoPartQuery = (tipoHecho == null || tipoHecho.isEmpty())
                    ? "" :
                    " AND hechos.id_tipo_hecho = " +
                            ServiceLocator.getTipoHechoService().searchTipoHechoByName(tipoHecho).getId_tipo_hecho();
            uorgPartQuery = (uorg == null || uorg.isEmpty())
                    ? "" :
                    " AND id_uorg = " +
                            ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName(uorg).getId_unidad_organizativa();

            cargarTabla(this.obtenerHechosUsingFilters(annoPartQuery, uorgPartQuery, tipoHechoPartQuery, LIMIT_OF_ROWS, 0));
            this.usingFilters = true;
            offsetMaximo = countAllHechosUsingFilters(annoPartQuery, uorgPartQuery, tipoHechoPartQuery);
            ponerOffsetDetablaActual();
            ponerNumeroDeTablas();
        }
    }

    private int countAllHechosUsingFilters(String anno, String uorg, String tipoHecho) {
        String query = "SELECT COUNT(id_reg) FROM hechos WHERE id_reg > 0" + uorg + tipoHecho + anno;
        int count = 0;

        try {
            ResultSet rs = Util.executeQuery(query);
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    private List<HechosRegistrados> obtenerHechosUsingFilters(String anno, String uorg, String tipoHecho, int limit, int offset) {
        List<HechosRegistrados> hechos = new LinkedList<>();
        String fullQuery =
                "SELECT id_reg, cod_cdnt, unidad_organizativa, tipo_hecho, fecha_ocurrencia, titulo, municipio\n" +
                        "FROM hechos\n" +
                        "JOIN unidades_organizativas o on hechos.id_uorg = o.id_unidad_organizativa\n" +
                        "JOIN municipios m2 on hechos.id_municipio = m2.id_municipio\n" +
                        "JOIN tipo_hechos h2 on hechos.id_tipo_hecho = h2.id_tipo_hecho\n" +
                        "WHERE id_reg > 0"
                        + uorg + tipoHecho + anno
                        + " ORDER BY id_reg DESC"
                        + " LIMIT " + limit
                        + " OFFSET " + offset;

        try {
            ResultSet rs = Util.executeQuery(fullQuery);
            hechos = getDataFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hechos;
    }

    private List<HechosRegistrados> obtenerHechosRegistrados(int limit, int offset) {
        List<HechosRegistrados> hechos = new LinkedList<>();
        String query = "SELECT id_reg, cod_cdnt, unidad_organizativa, tipo_hecho, fecha_ocurrencia, titulo, municipio " +
                "FROM hechos " +
                "JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg " +
                "JOIN tipo_hechos ON tipo_hechos.id_tipo_hecho = hechos.id_tipo_hecho " +
                "JOIN municipios ON municipios.id_municipio = hechos.id_municipio" +
                " Order By hechos.id_reg Desc LIMIT " + limit + " OFFSET " + offset;

        try {
            ResultSet rs = Util.executeQuery(query);
            hechos = getDataFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hechos;
    }

    private List<HechosRegistrados> getDataFromResultSet(ResultSet rs) throws SQLException {
        List<HechosRegistrados> hechos = new LinkedList<>();

        while (rs.next()) {
            hechos.add(new HechosRegistrados(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getDate(5),
                    rs.getString(6),
                    rs.getString(7)
            ));
        }

        return hechos;
    }

    @FXML
    private void toExcelFile() {
        if (this.hechosTable.getItems().isEmpty()) {
            Util.dialogResult("No hay información a exportar", Alert.AlertType.INFORMATION);
        } else {
            String urlFile = Util.selectPathToSaveDatabase(this.stage);
            Task<Boolean> task = new Task<Boolean>() {
                String url = "";
                @Override
                protected Boolean call() throws Exception {

                    if (usingFilters) {
                        int count = ServiceLocator.getHechosService().countAllHechos();
                        List<HechosRegistrados> hechos = obtenerHechosUsingFilters(annoPartQuery, uorgPartQuery, tipoHechoPartQuery, count, 0);
                        try {
                            url = GeneradorLocator.getExportarExcel().generarExcel(urlFile, hechos);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Util.dialogResult("Error al generar", Alert.AlertType.ERROR);
                        }
                    } else {
                        int count = ServiceLocator.getHechosService().countAllHechos();
                        List<HechosRegistrados> hechos = obtenerHechosRegistrados(count, 0);
                        try {
                            url = GeneradorLocator.getExportarExcel().generarExcel(urlFile, hechos);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Util.dialogResult("Error al generar", Alert.AlertType.ERROR);
                        }
                    }
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    dialogStage.close();
                    try {
                        Desktop.getDesktop().open(new File(url));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            if (urlFile != null) {
                this.loadDialogLoading(this.stage);
                Thread th = new Thread(task);
                th.setDaemon(true);
                th.start();
            }

        }
    }

    /**
     * Dialogo de cargar
     *
     * @param mainApp
     */
    private void loadDialogLoading(Stage mainApp) {
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
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
