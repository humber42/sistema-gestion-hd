package sistema_identificativo.services.impl;

import sistema_identificativo.models.CodigoPase;
import sistema_identificativo.services.CodigoPaseService;
import util.Conexion;
import util.Util;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class CodigoPaseServiceImpl implements CodigoPaseService {

    @Override
    public CodigoPase getCodigoPaseById(int id) {
        CodigoPase codigoPase = new CodigoPase();

        var query = "Select * From codigo_pase_identificativo Where id=" + id;
        try {
            var resultSet = Util.executeQuery(query);
            codigoPase = recuperarResulsetOneObject(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return codigoPase;
    }

    @Override
    public CodigoPase getCodigoByName(String name) {
        CodigoPase codigoPase = new CodigoPase();
        try {
            var query = "Select * From codigo_pase_identificativo Where codigo='" + name + "'";
            codigoPase = recuperarResulsetOneObject(Util.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return codigoPase;
    }

    @Override
    public int saveCodigoPase(CodigoPase codigoPase) throws SQLException {
        var function = "{call save_codigo_pase(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setString(1, codigoPase.getCodigo());
        statement.execute();
        statement.close();
        return 0;
    }

    @Override
    public List<CodigoPase> getAllCodigo() {
        List<CodigoPase> codigoPaseList = new LinkedList<>();
        try {
            var function = "Select * from codigo_pase_identificativo";
            codigoPaseList = recuperarListaCodigoPase(Util.executeQuery(function));
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return codigoPaseList;
    }

    @Override
    public void deleteCodigoPase(int id) throws SQLException {
        var function = "{call eliminar_codigo_pase(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, id);
        statement.execute();
        statement.close();
    }

    // recuperar resulset
    private CodigoPase recuperarResulsetOneObject(ResultSet set) throws SQLException {
        set.next();
        return new CodigoPase(
                set.getInt(1),
                set.getString(2)
        );
    }


    private LinkedList<CodigoPase> recuperarListaCodigoPase(ResultSet set) throws SQLException {
        LinkedList<CodigoPase> codigoPaseLinkedList = new LinkedList<>();
        while (set.next()) {

            codigoPaseLinkedList.add(new CodigoPase(
                    set.getInt(1),
                    set.getString(2)
            ));
        }
        return codigoPaseLinkedList;

    }
}
