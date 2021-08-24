package sistema_identificativo.services.impl;

import java.sql.SQLException;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sistema_identificativo.services.JasperReportService;
import util.Conexion;
import util.ConfigProperties;

public class JasperReportServiceImpl implements JasperReportService {

    private static final String URL_SERVER = ConfigProperties.getProperties().getProperty("URL_DOWNLOAD_IMAGE");

    @Override
    public void imprimirPasePermanente(String CI) {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("numero_identidad", CI);
        parameter.put("url_server", URL_SERVER);
        try {
            JasperPrint print = JasperFillManager.fillReport("src/sistema_identificativo/jasper_reports/pase_impreso_permanente.jasper", parameter, Conexion.getConnection());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirPaseEspecial(String CI) {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("numero_identidad", CI);
        parameter.put("url_server", URL_SERVER);
        try {
            JasperPrint print = JasperFillManager.fillReport("src/sistema_identificativo/jasper_reports/pase_impreso_especial.jasper", parameter, Conexion.getConnection());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirPaseProvisional(String CI) {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("numero_identidad", CI);
        parameter.put("url_server", URL_SERVER);
        try {
            JasperPrint print = JasperFillManager.fillReport("src/sistema_identificativo/jasper_reports/pase_impreso_provisional.jasper", parameter, Conexion.getConnection());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirPaseNegro(String CI) {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("numero_identidad", CI);
        parameter.put("url_server", URL_SERVER);
        try {
            JasperPrint print = JasperFillManager.fillReport("src/sistema_identificativo/jasper_reports/pase_impreso_negro.jasper", parameter, Conexion.getConnection());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirPasesEspecialesSelected() {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("url_server", URL_SERVER);
        try{
            JasperPrint print = JasperFillManager.fillReport("src/sistema_identificativo/jasper_reports/pases_impresos_especiales.jasper",parameter, Conexion.getConnection());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch (JRException | SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirPasesNegrosSelected() {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("url_server", URL_SERVER);
        try{
            JasperPrint print = JasperFillManager.fillReport("src/sistema_identificativo/jasper_reports/pases_impresos_negros.jasper",parameter, Conexion.getConnection());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch (JRException | SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirPasesPermanentesSelected() {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("url_server", URL_SERVER);
        try{
            JasperPrint print = JasperFillManager.fillReport("src/sistema_identificativo/jasper_reports/pases_impresos_permanentes.jasper",parameter, Conexion.getConnection());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch (JRException | SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirPasesProvisionalesSelected() {
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("url_server", URL_SERVER);
        try{
            JasperPrint print = JasperFillManager.fillReport("src/sistema_identificativo/jasper_reports/pases_impresos_provisionales.jasper",parameter, Conexion.getConnection());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch (JRException | SQLException e){
            e.printStackTrace();
        }
    }
}
