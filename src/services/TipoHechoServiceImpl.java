package services;

import models.TipoHecho;
import util.Conexion;
import util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class TipoHechoServiceImpl implements TipoHechoService {

    public TipoHecho getTipoHechoOfHechoByIdReg(int idReg) {
        String query = "Select id_tipo_hecho from hechos where id_reg=" + idReg;
        try {
            ResultSet rs = Util.executeQuery(query);
            int idTipoHecho = 0;
            if (rs.next()) {
                idTipoHecho = rs.getInt(1);
                return this.getOneTipoHecho(idTipoHecho);
            } else {
                return new TipoHecho();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public TipoHecho getOneTipoHecho(int id) {
        TipoHecho tipoHecho = new TipoHecho();
        String query = "SELECT * FROM tipo_hechos WHERE id_tipo_hecho=" + Integer.toString(id);

        try {
            Statement statement = Conexion.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                tipoHecho = recuperarResultSet(resultSet);
            }
            resultSet.close();
            statement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoHecho;
    }

    public List<TipoHecho> fetchAll() {
        String query = "SELECT * FROM tipo_hechos";
        List<TipoHecho> tipoHechoList = new LinkedList<>();

        try {
            ResultSet resultSet = Util.executeQuery(query);
            while (resultSet.next()) {
                tipoHechoList.add(recuperarResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tipoHechoList;
    }


    public TipoHecho searchTipoHechoByName(String name) {
        String query = "SELECT * FROM tipo_hechos WHERE tipo_hechos.tipo_hecho = '" + name + "'";
        TipoHecho tipoHecho = new TipoHecho();
        try {
            ResultSet resultSet = Util.executeQuery(query);
            if (resultSet.next()) {
                tipoHecho = recuperarResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoHecho;
    }

    private TipoHecho recuperarResultSet(ResultSet resultSet) {
        TipoHecho tipoHecho = new TipoHecho();
        try {
            tipoHecho.setId_tipo_hecho(resultSet.getInt("id_tipo_hecho"));
            tipoHecho.setTipo_hecho(resultSet.getString("tipo_hecho"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoHecho;
    }
}
