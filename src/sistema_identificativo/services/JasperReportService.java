package sistema_identificativo.services;

public interface JasperReportService {
    void imprimirPasePermanente(String ci);
    void imprimirPaseProvisional(String ci);
    void imprimirPaseEspecial(String ci);
    void imprimirPaseNegro(String ci);
    void imprimirPasesPermanentesSelected();
    void imprimirPasesProvisionalesSelected();
    void imprimirPasesEspecialesSelected();
    void imprimirPasesNegrosSelected();
}
