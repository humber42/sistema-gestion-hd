package sistema_identificativo.services.impl;

import java.sql.SQLException;
import java.util.HashMap;

import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sistema_identificativo.services.JasperReportService;
import util.Conexion;
import util.ConfigProperties;

public class JasperReportServiceImpl implements JasperReportService {

    private static final String URL_SERVER = ConfigProperties.getProperties().getProperty("URL_DOWNLOAD_IMAGE");

    //TODO: Make some thread to dont freeze the User Interface;

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

    @Override
    public void imprimirResumenBajas(Stage dialogStage) {
        try{
            JasperPrint print = JasperFillManager
                    .fillReport("src/sistema_identificativo/jasper_reports/resumen_pases_baja.jasper",null
                            ,Conexion.getConnection());
            JasperViewer viewer = new JasperViewer(print,false);
            viewer.setVisible(true);
        }catch (JRException | SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirResumenFotosPendientes(Stage dialogStage) {
        try{
            JasperPrint print = JasperFillManager
                    .fillReport("src/sistema_identificativo/jasper_reports/resumen_pases_pendiente_de_foto.jasper",
                            null,Conexion.getConnection());
            JasperViewer view = new JasperViewer(print,false);
            view.setVisible(true);
        }catch (JRException | SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirResumenGeneral(Stage dialogStage) {
        try{
            JasperPrint print = JasperFillManager
                    .fillReport("src/sistema_identificativo/jasper_reports/resumen_general_pases.jasper"
                            ,null,Conexion.getConnection());
            JasperViewer view = new JasperViewer(print,false);
            view.setVisible(true);

        }catch (JRException | SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirResumenPasesImpresos(Stage dialogStage) {
        try{
            JasperPrint print = JasperFillManager
                    .fillReport("src/sistema_identificativo/jasper_reports/resumen_pases_impresos.jasper",
                            null,Conexion.getConnection());
            JasperViewer view = new JasperViewer(print,false);
            view.setVisible(true);
        }catch (SQLException| JRException e){
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirResumenTipoPase(int tipoPase,Stage dialogStage) {
        HashMap<String,Integer> parameter = new HashMap<>();
        parameter.put("id_pase",tipoPase);
        try{
            JasperPrint print = JasperFillManager
                    .fillReport("src/sistema_identificativo/jasper_reports/resumen_pases_segun_tipo.jasper",
                            parameter,Conexion.getConnection());
            JasperViewer view = new JasperViewer(print,false);
            view.setVisible(true);
        }catch (SQLException | JRException e){
            e.printStackTrace();
        }
    }

    @Override
    public void imprimirResumenUnidadOrganizativa(int id,Stage dialogStage) {
        HashMap<String,Integer> parameter = new HashMap<>();
        parameter.put("id_uorg",id);
        try{
            JasperPrint print = JasperFillManager
                    .fillReport("src/sistema_identificativo/jasper_reports/resumen_pases_uorg.jasper",
                            parameter,Conexion.getConnection());
            JasperViewer view = new JasperViewer(print,false);
            view.setVisible(true);
        }catch (SQLException|JRException e){
            e.printStackTrace();
        }

    }

}
