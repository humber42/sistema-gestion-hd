package sistema_identificativo.services;

import javafx.stage.Stage;
import sistema_identificativo.models.Impresion;

import java.util.List;

public interface JasperReportService {
    void imprimirPasePermanente(String ci, Stage mainApp);
    void imprimirPaseProvisional(String ci, Stage mainApp);
    void imprimirPaseEspecial(String ci, Stage mainApp);
    void imprimirPaseNegro(String ci, Stage mainApp);
    void imprimirPasesPermanentesSelected(Stage mainApp, List<Impresion> impresions);
    void imprimirPasesProvisionalesSelected(Stage mainApp, List<Impresion> impresions);
    void imprimirPasesEspecialesSelected(Stage mainApp, List<Impresion> impresions);
    void imprimirPasesNegrosSelected(Stage mainApp, List<Impresion> impresions);
    void imprimirResumenGeneral(Stage mainApp);
    void imprimirResumenUnidadOrganizativa(int id, Stage mainApp);
    void imprimirResumenTipoPase(int tipoPase, Stage mainApp);
    void imprimirResumenBajas(Stage mainApp);
    void imprimirResumenPasesImpresos(Stage mainApp);
    void imprimirResumenFotosPendientes(Stage mainApp);
}
