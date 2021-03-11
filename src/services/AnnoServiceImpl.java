package services;

import models.Anno;
import util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


/**
 * Services for the Anno Model
 */
public class AnnoServiceImpl implements AnnoService {

    /**
     * Method that search an anno in the database
     *
     * @param id_anno this is the identifier of one anno
     * @return Anno object
     */
    public Anno searchAnno(int id_anno) {
        Anno anno = new Anno();
        String query = "SELECT * FROM anno WHERE anno.id_anno =" + Integer.toString(id_anno);
        try {
            var rs = Util.executeQuery(query);
            if (rs.next()) {
                anno = recuperarResultSet(rs);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anno;
    }

    /**
     * Method to obtain the active year
     *
     * @return Anno object
     */
    public Anno getActiveAnno() {
        Anno anno = new Anno();
        String query = "SELECT * FROM annos WHERE annos.active = true";
        try {
            var resultSet = Util.executeQuery(query);
            if (resultSet.next()) {
                anno = recuperarResultSet(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }


        return anno;
    }

    /**
     * Fetch all anno in the database
     *
     * @return List of anno
     */

    public List<Anno> allAnno() {
        List<Anno> annoList = new LinkedList<Anno>();
        String query = "SELECT * FROM annos ORDER BY annos.anno DESC";
        try {

            var resultSet = Util.executeQuery(query);

            while (resultSet.next()) {
                Anno anno = recuperarResultSet(resultSet);
                annoList.add(anno);
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return annoList;
    }


    /**
     * Method to map an Anno who comes from the database
     *
     * @param rs is the result from a query
     * @return Anno Object
     */
    private Anno recuperarResultSet(ResultSet rs) {
        Anno anno = new Anno();
        try {
            anno.setId_anno(rs.getInt("id_anno"));
            anno.setAnno(rs.getInt("anno"));
            anno.setActive(rs.getBoolean("active"));

        } catch (Exception e0) {
            e0.printStackTrace();
        }
        return anno;
    }
}
