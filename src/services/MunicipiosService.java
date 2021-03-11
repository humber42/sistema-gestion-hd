package services;

import models.Municipio;
import util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MunicipiosService {

    public Municipio getOne(int id) {
        Municipio municipio = new Municipio();
        String query = "SELECT * FROM municipios WHERE id_municipio=" + Integer.toString(id);
        try {
            var resultSet = Util.executeQuery(query);
            if (resultSet.next()) {
                municipio = recuperarResultSet(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return municipio;
    }

    public List<Municipio> fetchAll() {
        List<Municipio> municipioList = new LinkedList<>();
        var query = "SELECT * FROM municipios";
        try {
            var resultSet = Util.executeQuery(query);
            while (resultSet.next()) {
                municipioList.add(recuperarResultSet(resultSet));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return municipioList;
    }

    public Municipio searchMunicipioByName(String name) {
        Municipio municipio = new Municipio();
        var query = "Select * FROM municipios WHERE municipio = '" + name + "'";

        try {
            var resultSet = Util.executeQuery(query);
            if (resultSet.next())
                municipio = recuperarResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return municipio;
    }

    private Municipio recuperarResultSet(ResultSet resultSet) {
        Municipio municipio = new Municipio();
        try {
            municipio.setId_municipio(resultSet.getInt("id_municipio"));
            municipio.setMunicipio(resultSet.getString("municipio"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return municipio;
    }
}
