package services;

import models.TipoVandalismo;
import util.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class TipoVandalismoService {

    public TipoVandalismo getOneTipoVandalismo(int id) {
        TipoVandalismo tipoVandalismo = new TipoVandalismo();
        var query = "SELECT * FROM tipo_vandalismo WHERE id_afect_tpublica=" + Integer.toString(id);

        try {
            Statement statement = Conexion.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                tipoVandalismo = recuperarTipoVandalismo(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoVandalismo;
    }

    public TipoVandalismo searchVandalismoByName(String name) {
        var query = "SELECT * FROM tipo_vandalismo WHERE afect_tpublica = '" + name + "'";
        TipoVandalismo tipoVandalismo = new TipoVandalismo();
        try {
            var resultSet = Util.executeQuery(query);
            if (resultSet.next())
                tipoVandalismo = recuperarTipoVandalismo(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoVandalismo;
    }

    public List<TipoVandalismo> fetchAll() {
        List<TipoVandalismo> tipoVandalismoList = new LinkedList<>();
        var query = "SELECT * FROM tipo_vandalismo";

        try {
            var resultSet = Util.executeQuery(query);
            while (resultSet.next()) {
                tipoVandalismoList.add(recuperarTipoVandalismo(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoVandalismoList;

    }

    private TipoVandalismo recuperarTipoVandalismo(ResultSet resultSet) {
        TipoVandalismo tipoVandalismo = new TipoVandalismo();
        try {
            tipoVandalismo.setId_afect_tpublica(resultSet.getInt("id_afect_tpublica"));
            tipoVandalismo.setAfect_tpublica(resultSet.getString("afect_tpublica"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoVandalismo;
    }

    public void insertarTipoDeVandalismo(TipoVandalismo tipoVandalismo)throws SQLException {
        var function = "{call inserta_tipo_vandalismo(?)}";
        CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
        callableStatement.setString(1,tipoVandalismo.getAfect_tpublica());
        callableStatement.execute();
        callableStatement.close();
    }

    public void editarTipoDeVandalismo(TipoVandalismo tipoVandalismo)throws SQLException{
        var function = "{call editar_tipo_vandalismo(?,?)}";
        CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
        callableStatement.setInt(1,tipoVandalismo.getId_afect_tpublica());
        callableStatement.setString(2,tipoVandalismo.getAfect_tpublica());
        callableStatement.execute();
        callableStatement.close();
    }

    public void eliminarTipoDeVandalismo(TipoVandalismo tipoVandalismo)throws SQLException{
        var fonction = "{call eliminar_tipo_vandalismo(?)}";
        CallableStatement callableStatement = Conexion.getConnection().prepareCall(fonction);
        callableStatement.setInt(1,tipoVandalismo.getId_afect_tpublica());
        callableStatement.execute();
        callableStatement.close();
    }

}
