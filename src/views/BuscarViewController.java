package views;

import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import models.Hechos;
import models.Municipio;
import models.TipoHecho;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import services.ServiceLocator;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BuscarViewController {

    private BorderPane principalView;
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
    private Boolean activoOrDeactive = false;

    public void setPrincipalView(BorderPane principalView) {
        this.principalView = principalView;
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

    @FXML
    private void buscar() {
        List<Hechos> hechos = new LinkedList<>();
        hechos = ServiceLocator.getHechosService().getHechosBySqlExpresion("Select * from hechos where titulo='" + titulo.getText() + "'");
        for (Hechos hecho : hechos) {
            System.out.println(hecho.getId_reg());
        }
    }
}
