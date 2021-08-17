package sistema_identificativo.services.impl;

import services.ServiceLocator;
import sistema_identificativo.models.RegistroImpresiones;
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
    public int saveRegistroImpresiones(RegistroImpresiones registroImpresiones) throws SQLException {
        var function = "{call add_impressions(?,?,?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, registroImpresiones.getPase().getIdReg());
        statement.setInt(2, registroImpresiones.getCantidadImpresiones());
        statement.setString(3, registroImpresiones.getUltimaImpresion());
        return 0;
    }

    @Override
    public int updateRegistroImpresiones(RegistroImpresiones registroImpresiones) throws SQLException {
        var function = "{call update_impressions(?,?,?,?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, registroImpresiones.getId());
        statement.setInt(2, registroImpresiones.getPase().getIdReg());
        statement.setInt(3, registroImpresiones.getCantidadImpresiones());
        statement.setString(4, registroImpresiones.getUltimaImpresion());

        return 0;
    }

    @Override
    public List<RegistroImpresiones> getAllRegistroImpresiones() {
        List<RegistroImpresiones> registroImpresionesList = new LinkedList<>();
        try {
            var query = "Select * from registro_impresiones";
            registroImpresionesList = recuperarLista(Util.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registroImpresionesList;
    }

    @Override
    public RegistroImpresiones getRegistroImpresionesById(int id) {
        RegistroImpresiones registroImpresiones = new RegistroImpresiones();
        try {
            var query = "Select * registro_impresiones Where id=" + id;
            registroImpresiones = this.recuperarObjeto(Util.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registroImpresiones;
    }

    @Override
    public RegistroImpresiones getRegistroImpresionesByIdReg(int idReg) {
        RegistroImpresiones registroImpresiones = new RegistroImpresiones();
        try {
            var query = "Select * From registro_impresiones WHERE id_reg=" + idReg;
            registroImpresiones = recuperarObjeto(Util.executeQuery(query));
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return registroImpresiones;
    }

    @Override
    public void deleteRegistroImpresionesById(int id) throws SQLException {
        var function = "{call delete_impressions(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, id);
        statement.execute();
        statement.close();
    }

    //recuperar resultset
    private RegistroImpresiones recuperarObjeto(ResultSet set) throws SQLException {
        return new RegistroImpresiones(
                set.getInt(1),
                ServiceLocator.getRegistroPaseService().getRegistroPaseById(set.getInt(2)),
                set.getInt(3),
                set.getString(4)
        );
    }

    private LinkedList<RegistroImpresiones> recuperarLista(ResultSet set) throws SQLException {
        LinkedList<RegistroImpresiones> registroImpresionesLinkedList = new LinkedList<>();
        while (set.next()) {
            registroImpresionesLinkedList.add(this.recuperarObjeto(set));
        }
        return registroImpresionesLinkedList;
    }

}
