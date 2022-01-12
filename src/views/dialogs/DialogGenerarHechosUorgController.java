package views.dialogs;

import com.jfoenix.controls.JFXProgressBar;
import informes_generate.GeneradorLocator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import models.Anno;
import models.Hechos;
import models.UnidadOrganizativa;
import org.controlsfx.control.textfield.TextFields;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Humberto Cabrera Dominguez
 */
public class DialogGenerarHechosUorgController {

    @FXML
    private ComboBox<String> unidadesOrganizativas;
    @FXML
    private ComboBox<String> meses;
    @FXML
    private ComboBox<Integer> annos;
    @FXML
    private CheckBox delitoPext;
    @FXML
    private CheckBox delitoTPub;
    @FXML
    private CheckBox robo;
    @FXML
    private CheckBox hurto;
    @FXML
    private CheckBox fraude;
    @FXML
    private CheckBox accionesCR;
    @FXML
    private CheckBox otrosDelitos;
    @FXML
    private CheckBox averiasPext;
    @FXML
    private CheckBox accTransito;
    @FXML
    private CheckBox segInf;
    @FXML
    private CheckBox incInteriores;
    @FXML
    private CheckBox incExteriores;
    @FXML
    private CheckBox diferOrigen;
    @FXML
    private CheckBox otrosHechos;
    @FXML
    private JFXProgressBar progressBar;

    private Stage stage;

    public void setDialogStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        unidadesOrganizativas.getItems().addAll(cargarComboUOrg());
        annos.getItems().addAll(cargarComboAnnos());
        meses.getItems().add("<<Todos>>");
        meses.getItems().addAll(Util.meses2);
        TextFields.bindAutoCompletion(unidadesOrganizativas.getEditor(), unidadesOrganizativas.getItems());

    }


    private List<String> cargarComboUOrg() {
        return ServiceLocator.getUnidadOrganizativaService().fetchAll().stream().map(UnidadOrganizativa::getUnidad_organizativa).collect(Collectors.toList());
    }

    private List<Integer> cargarComboAnnos() {
        return ServiceLocator.getAnnoServicio().allAnno().stream().map(Anno::getAnno).collect(Collectors.toList());
    }

    private boolean reviewDataCombo() {
        boolean datosCorrectos = true;

        if (unidadesOrganizativas.getSelectionModel().isEmpty()
                || meses.getSelectionModel().isEmpty()
                || annos.getSelectionModel().isEmpty()) {
            datosCorrectos = false;
        }

        return datosCorrectos;
    }

    private boolean reviewCheckBox() {
        boolean datosCorrectos = true;
        if (!delitoPext.isSelected() && !delitoTPub.isSelected()
                && !robo.isSelected() && !hurto.isSelected()
                && !fraude.isSelected() && !accionesCR.isSelected()
                && !otrosDelitos.isSelected() && !averiasPext.isSelected()
                && !accTransito.isSelected() && !segInf.isSelected()
                && !incExteriores.isSelected() && !incExteriores.isSelected()
                && !otrosHechos.isSelected()
        ) {
            datosCorrectos = false;
        }
        return datosCorrectos;
    }

    @FXML
    private void generarInforme() {
        if (!reviewDataCombo()) {
            Util.dialogResult("Seleccione algún dato", Alert.AlertType.ERROR);
        } else if (!reviewCheckBox()) {
            Util.dialogResult("Marque al menos un tipo de hecho", Alert.AlertType.ERROR);
        } else {
            executeInforme();
        }
    }

    private void executeInforme() throws NullPointerException {
        String path = Util.selectPathToSaveDatabase(this.stage);
        if(path != null) {
            Task<Boolean> tarea = new Task<Boolean>() {
                boolean result = false;
                LinkedList<Hechos> hechos = new LinkedList<>();
                int mes = 0;
                boolean datosVacios = false;

                @Override
                protected Boolean call() throws Exception {
                    progressBar.setVisible(true);
                    if (!meses.getSelectionModel().getSelectedItem().equalsIgnoreCase("<<todos>>")) {
                        mes = obtenerNumeroMes(meses.getSelectionModel().getSelectedItem());
                    }
                    hechos = filtrarHechos(ServiceLocator.getHechosService()
                            .obtenerHechosParaUnidadOrganizativaPorMesYAnno(
                                    mes,
                                    annos.getSelectionModel().getSelectedItem(),
                                    obtenerUOrg(unidadesOrganizativas.getSelectionModel().getSelectedItem())
                            )
                    );
                    if (hechos.isEmpty()) {
                        datosVacios = true;
                    } else {
                        this.result = GeneradorLocator.getGenerarHechosParaUOrg().generarHechosParaUorg(hechos, path);
                    }
                    return true;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    progressBar.setVisible(false);
                    String file = path+"/HechosParaUnidadOrganizativa.xlsx";
                    if (!datosVacios && result) {
                        try {
                            Runtime.getRuntime().exec("cmd /c start " + file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                       // Util.dialogResult("Exito", Alert.AlertType.INFORMATION);
                    } else if (datosVacios) {
                        Util.dialogResult("No hay datos que mostrar", Alert.AlertType.INFORMATION);
                    } else {
                        Util.dialogResult("Error al generar", Alert.AlertType.ERROR);
                    }
                }
            };
            new Thread(tarea).start();
        }
    }

    private LinkedList<Hechos> filtrarHechos(LinkedList<Hechos> hechosUOrg) {
        LinkedList<Hechos> retorno = new LinkedList<>();
        try {
            hechosUOrg.forEach(hecho -> {
                if (delitoPext.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Delito vs PExt")) {
                    retorno.add(hecho);
                } else if (delitoTPub.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Delito vs TPúb")) {
                    retorno.add(hecho);
                } else if (robo.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Robo")) {
                    retorno.add(hecho);
                } else if (hurto.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Hurto")) {
                    retorno.add(hecho);
                } else if (fraude.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Fraude")) {
                    retorno.add(hecho);
                } else if (accionesCR.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Acción C/R")) {
                    retorno.add(hecho);
                } else if (otrosDelitos.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Otros Delitos")) {
                    retorno.add(hecho);
                } else if (averiasPext.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Avería PExt")) {
                    retorno.add(hecho);
                } else if (accTransito.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Acc. Tránsito")) {
                    retorno.add(hecho);
                } else if (segInf.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Seg. Informática")) {
                    retorno.add(hecho);
                } else if (incInteriores.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Incendio Int.")) {
                    retorno.add(hecho);
                } else if (incExteriores.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Incendio Ext.")) {
                    retorno.add(hecho);
                } else if (diferOrigen.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Difer. Origen")) {
                    retorno.add(hecho);
                } else if (otrosHechos.isSelected() && hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Otros Hechos")) {
                    retorno.add(hecho);
                }
            });
        } catch (NullPointerException e) {
            return null;
        }
        return retorno;
    }


    public void handleCancelar() {
        stage.close();
    }

    private int obtenerUOrg(String unidadOrganizativa) {
        return ServiceLocator.getUnidadOrganizativaService().searchUnidadOrganizativaByName(unidadOrganizativa).getId_unidad_organizativa();
    }

    private int obtenerNumeroMes(String name) {
        int mes = 0;
        boolean notFound = true;
        for (int i = 0; i < Util.meses2.length && notFound; i++) {
            if (Util.meses2[i].equalsIgnoreCase(name)) {
                notFound = false;
                mes = i + 1;
            }
        }
        return mes;
    }
}
