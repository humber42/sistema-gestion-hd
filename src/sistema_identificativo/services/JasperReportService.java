package sistema_identificativo.services;

import javafx.stage.Stage;

public interface JasperReportService {
    void imprimirPasePermanente(String ci);
    void imprimirPaseProvisional(String ci);
    void imprimirPaseEspecial(String ci);
    void imprimirPaseNegro(String ci);
    void imprimirPasesPermanentesSelected();
    void imprimirPasesProvisionalesSelected();
    void imprimirPasesEspecialesSelected();
    void imprimirPasesNegrosSelected();
    void imprimirResumenGeneral(Stage dialogStage);
    void imprimirResumenUnidadOrganizativa(int id, Stage dialogStage);
    void imprimirResumenTipoPase(int tipoPase, Stage dialogStage);
    void imprimirResumenBajas(Stage dialogStage);
    void imprimirResumenPasesImpresos(Stage dialogStage);
    void imprimirResumenFotosPendientes(Stage dialogStage);
}
