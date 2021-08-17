package sistema_identificativo.services.impl;

import java.sql.SQLException;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sistema_identificativo.services.JasperReportService;
import util.Conexion;

public class JasperReportServiceImpl implements JasperReportService {

    //pase impreso tipo permanente
    public void imprimirPasePermanente(String CI){
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("numero_identidad",CI);
        try{
            JasperPrint print = JasperFillManager.fillReport("src/sistema_identificativo/jasper_reports/pase_impreso_permanente.jasper", parameter, Conexion.getConnection());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch (JRException | SQLException e){
            e.printStackTrace();
        }
    }

    //pase impreso tipo especial
    public void imprimirPaseEspecial(String CI){
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("numero_identidad",CI);
        try{
            JasperPrint print = JasperFillManager.fillReport("src/sistema_identificativo/jasper_reports/pase_impreso_especial.jasper", parameter, Conexion.getConnection());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch (JRException | SQLException e){
            e.printStackTrace();
        }
    }

    //pase impreso tipo provisional
    public void imprimirPaseProvisional(String CI){
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("numero_identidad",CI);
        try{
            JasperPrint print = JasperFillManager.fillReport("src/sistema_identificativo/jasper_reports/pase_impreso_provisional.jasper", parameter, Conexion.getConnection());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch (JRException | SQLException e){
            e.printStackTrace();
        }
    }

    public void imprimirPaseNegro(String CI){
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("numero_identidad", CI);
        try{
            JasperPrint print = JasperFillManager.fillReport("src/sistema_identificativo/jasper_reports/pase_impreso_negro.jasper",parameter, Conexion.getConnection());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch(JRException | SQLException e){
            e.printStackTrace();
        }
    }
}
