package sistema_identificativo.services.impl;

import services.ServiceLocator;
import sistema_identificativo.models.RegistroImpresiones;
import sistema_identificativo.models.RegistroPase;
import sistema_identificativo.services.RegistroImpresionesService;
import util.Conexion;
import util.Util;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RegistroImpresionesServiceImpl implements RegistroImpresionesService {

    @Override
    public void execNewOrUpdateImpressionRegister(String CI) throws SQLException{
        RegistroPase rp = ServiceLocator.getRegistroPaseService().getPaseByCI(CI);
        if(rp != null){
            int id_reg = rp.getIdReg();
            RegistroImpresiones ri = ServiceLocator.getRegistroImpresionesService().getRegistroImpresionesByIdReg(id_reg);
            if(ri == null){
                ServiceLocator.getRegistroImpresionesService().saveNuevoRegistroImpresion(id_reg);
            }
            else{
                ServiceLocator.getRegistroImpresionesService().updateLastImpressionAndQuanty(id_reg);
            }
        }
    }

    @Override
    public int saveRegistroImpresiones(RegistroImpresiones registroImpresiones) throws SQLException {
        String function = "{call add_impressions(?,?,?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, registroImpresiones.getPase().getIdReg());
        statement.setInt(2, registroImpresiones.getCantidadImpresiones());
        statement.setString(3, registroImpresiones.getUltimaImpresion());
        statement.execute();
        statement.close();
        return 0;
    }

    @Override
    public int updateRegistroImpresiones(RegistroImpresiones registroImpresiones) throws SQLException {
        String function = "{call update_impressions(?,?,?,?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, registroImpresiones.getId());
        statement.setInt(2, registroImpresiones.getPase().getIdReg());
        statement.setInt(3, registroImpresiones.getCantidadImpresiones());
        statement.setString(4, registroImpresiones.getUltimaImpresion());
        statement.execute();
        statement.close();
        return 0;
    }

    @Override
    public List<RegistroImpresiones> getAllRegistroImpresiones() {
        List<RegistroImpresiones> registroImpresionesList = new LinkedList<>();
        try {
            String query = "Select * from registro_impresiones";
            ResultSet rs = Util.executeQuery(query);
            registroImpresionesList = recuperarLista(rs);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registroImpresionesList;
    }

    @Override
    public RegistroImpresiones getRegistroImpresionesById(int id) {
        RegistroImpresiones registroImpresiones = new RegistroImpresiones();
        try {
            String query = "Select * registro_impresiones Where id=" + id;
            ResultSet rs = Util.executeQuery(query);
            registroImpresiones = this.recuperarObjeto(rs);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registroImpresiones;
    }

    @Override
    public RegistroImpresiones getRegistroImpresionesByIdReg(int idReg) {
        RegistroImpresiones registroImpresiones = new RegistroImpresiones();
        try {
            String query = "Select * From registro_impresiones WHERE id_reg=" + idReg;
            ResultSet rs = Util.executeQuery(query);
            registroImpresiones = recuperarObjeto(rs);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registroImpresiones;
    }

    @Override
    public void deleteRegistroImpresionesById(int id) throws SQLException {
        String function = "{call delete_impressions(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, id);
        statement.execute();
        statement.close();
    }

    @Override
    public int updateLastImpressionAndQuanty(int id) throws SQLException{
        String function = "{call update_last_impression_and_quanty(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1,id);
        statement.execute();
        statement.close();
        return 0;
    }

    @Override
    public int saveNuevoRegistroImpresion(int id_reg) throws SQLException{
        String function = "{call save_new_impression(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1,id_reg);
        statement.execute();
        statement.close();
        return 0;
    }

    //recuperar resultset
    private RegistroImpresiones recuperarObjeto(ResultSet set) throws SQLException {
        if(set.next()){
        return new RegistroImpresiones(
                set.getInt(1),
                ServiceLocator.getRegistroPaseService().getRegistroPaseById(set.getInt(2)),
                set.getInt(3),
                set.getString(4)
        );
        }
        else
            return null;
    }

    private LinkedList<RegistroImpresiones> recuperarLista(ResultSet set) throws SQLException {
        LinkedList<RegistroImpresiones> registroImpresionesLinkedList = new LinkedList<>();
        while (set.next()) {
            registroImpresionesLinkedList.add(this.recuperarObjeto(set));
        }
        return registroImpresionesLinkedList;
    }

    @Override
    public int countAllPasesImpresos() {
        String query = "SELECT COUNT(registro_impresiones.id) FROM registro_impresiones\n" +
                    "JOIN registro_pases rp on registro_impresiones.id_reg = rp.id_reg\n" +
                    "WHERE baja = 0";
        return Util.count(query);
    }

    @Override
    public int contPasesImpresosTipoPase(int id_tipo_pase) {
        String function = "{call cant_pases_impresos_tipo_pase(?)}";
        int cant = -1;

        try{
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, id_tipo_pase);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if(rs.next())
                cant = rs.getInt(1);
            statement.close();
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return cant;
    }
}
