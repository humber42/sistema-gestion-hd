package sistema_identificativo.services;

import javafx.collections.ObservableList;
import javafx.stage.Stage;
import sistema_identificativo.models.Impresion;

import java.util.List;

public interface JasperReportService {
    void imprimirPasePermanente(String ci, Stage mainApp,ObservableList<Impresion> listaToUpdate);
    void imprimirPaseProvisional(String ci, Stage mainApp,ObservableList<Impresion> listaToUpdate);
    void imprimirPaseEspecial(String ci, Stage mainApp,ObservableList<Impresion> listaToUpdate);
    void imprimirPaseNegro(String ci, Stage mainApp,ObservableList<Impresion> listaToUpdate);
    void imprimirPasesPermanentesSelected(Stage mainApp, List<Impresion> impresions,ObservableList<Impresion> listaToUpdate);
    void imprimirPasesProvisionalesSelected(Stage mainApp, List<Impresion> impresions,ObservableList<Impresion> listaToUpdate);
    void imprimirPasesEspecialesSelected(Stage mainApp, List<Impresion> impresions,ObservableList<Impresion> listaToUpdate);
    void imprimirPasesNegrosSelected(Stage mainApp, List<Impresion> impresions, ObservableList<Impresion> listaToUpdate);
    void imprimirResumenGeneral(Stage mainApp);
    void imprimirResumenUnidadOrganizativa(int id, Stage mainApp);
    void imprimirResumenTipoPase(int tipoPase, Stage mainApp);
    void imprimirResumenBajas(Stage mainApp);
    void imprimirResumenPasesImpresos(Stage mainApp);
    void imprimirResumenFotosPendientes(Stage mainApp);
}
