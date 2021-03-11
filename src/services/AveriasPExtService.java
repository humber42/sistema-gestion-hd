package services;

import models.AveriasPext;
import util.Conexion;

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
            if (resultSet.next()) {
                averiasPext = recuperarResultSet(resultSet);
            }
            resultSet.close();
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
                AveriasPext pext = recuperarResultSet(resultSet);
                averiasPextList.add(pext);
            }
            resultSet.close();
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
            pext.setId_avpext(rs.getInt("id_avpext"));
            pext.setCausa(rs.getString("causa"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pext;
    }

}
