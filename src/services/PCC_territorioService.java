package services;


import models.PCC_territorio;
import util.Conexion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PCC_territorioService {

    public PCC_territorio getOnePCCTerritorio(int id) {
        PCC_territorio pcc_territorio = new PCC_territorio();
        String query = "SELECT * FROM pcc_territorios WHERE id_territorio=" + Integer.toString(id);
        try {
            Statement statement = Conexion.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                pcc_territorio = recuperarResultSet(resultSet);
            }
            resultSet.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pcc_territorio;
    }

    private PCC_territorio recuperarResultSet(ResultSet resultSet) {
        PCC_territorio pcc_territorio = new PCC_territorio();
        try {
            pcc_territorio.setId_territorio(resultSet.getInt("id_territorio"));
            pcc_territorio.setTerritorio(resultSet.getString("territorio"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pcc_territorio;
    }
}
