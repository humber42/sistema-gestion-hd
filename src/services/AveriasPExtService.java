package services;

import models.AveriasPext;
import util.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class AveriasPExtService {
    /**
     * Method to search an object AveriaPext
     *
     * @param id Identifier
     * @return AveriasPExt Model
     */
    public AveriasPext searchAveriaPExt(int id) {
        AveriasPext averiasPext = new AveriasPext();
        String query = "SELECT * FROM codaverias WHERE id_avpext =" + Integer.toString(id);
        try {
            Statement statement = Conexion.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            averiasPext = recuperarResultSet(resultSet);
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return averiasPext;
    }

    public AveriasPext searchAveriaPextByName(String name) {
        AveriasPext averiasPext = new AveriasPext();
        String query = "Select * from codaverias where causa = '" + name + "'";
        try {
            ResultSet set = Util.executeQuery(query);
            averiasPext = recuperarResultSet(set);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return averiasPext;
    }

    /**
     * Method to fetch all AveriaPext
     *
     * @return List of AveriaPext model
     */
    public List<AveriasPext> fecthAllAveriaPext() {
        List<AveriasPext> averiasPextList = new LinkedList<AveriasPext>();
        String query = "SELECT * FROM codaverias";
        try {
            Statement statement = Conexion.getConnection().createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY
            );
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                AveriasPext pext = new AveriasPext(
                        resultSet.getInt("id_avpext"),
                        resultSet.getString("causa")
                );
                averiasPextList.add(pext);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return averiasPextList;
    }


    /**
     * Method to map an object from a database
     *
     * @param rs Result set
     * @return AveriasPext Object
     */
    private AveriasPext recuperarResultSet(ResultSet rs) {
        AveriasPext pext = new AveriasPext();
        try {
            if (rs.next()) {
                pext.setId_avpext(rs.getInt("id_avpext"));
                pext.setCausa(rs.getString("causa"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pext;
    }

    public void insertarTipoAveria(AveriasPext averiasPext)throws SQLException{
        String function = "{call insertar_averia_pext(?)}";
        CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
        callableStatement.setString(1,averiasPext.getCausa());
        callableStatement.execute();
        callableStatement.close();
    }

    public void editarTipoAveria(AveriasPext averiasPext)throws SQLException{
        String function = "{call editar_averia_pext(?,?)}";
        CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
        callableStatement.setInt(1,averiasPext.getId_avpext());
        callableStatement.setString(2,averiasPext.getCausa());
        callableStatement.execute();
        callableStatement.close();
    }

    public void eliminarTipoAveria(AveriasPext averiasPext)throws SQLException{
        String function = "{call eliminar_averia_pext(?)}";
        CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
        callableStatement.setInt(1,averiasPext.getId_avpext());
        callableStatement.execute();
        callableStatement.close();
    }

}
