package services;


import models.UnidadOrganizativa;
import util.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class UnidadOrganizativaService {

    public UnidadOrganizativa getOneUnidadOrganizativa(int id) {
        UnidadOrganizativa unidadOrganizativa = new UnidadOrganizativa();
        var query = "SELECT * FROM unidades_organizativas WHERE id_unidad_organizativa=" + Integer.toString(id);

        try {
            Statement statement = Conexion.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                unidadOrganizativa = recuperarUnidadOraganizativa(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return unidadOrganizativa;
    }

    public List<UnidadOrganizativa> fetchAll() {
        var query = "SELECT * FROM unidades_organizativas";
        List<UnidadOrganizativa> unidadOrganizativaList = new LinkedList<>();

        try {
            var resultSet = Util.executeQuery(query);
            while (resultSet.next()) {
                unidadOrganizativaList.add(recuperarUnidadOraganizativa(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unidadOrganizativaList;
    }

    public UnidadOrganizativa searchUnidadOrganizativaByName(String name) {
        var query = "Select * FROM unidades_organizativas WHERE unidad_organizativa = '" + name + "'";
        UnidadOrganizativa unidadOrganizativa = new UnidadOrganizativa();

        try {
            var resutltSet = Util.executeQuery(query);
            if (resutltSet.next())
                unidadOrganizativa = recuperarUnidadOraganizativa(resutltSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unidadOrganizativa;
    }

    /**
     * Recuperar result set
     *
     * @param resultSet respuesta de la BD
     * @return Unidad organizativa object
     */
    private UnidadOrganizativa recuperarUnidadOraganizativa(ResultSet resultSet) {
        UnidadOrganizativa unidadOrganizativa = new UnidadOrganizativa();

        try {
            unidadOrganizativa.setId_unidad_organizativa(resultSet.getInt("id_unidad_organizativa"));
            unidadOrganizativa.setUnidad_organizativa(resultSet.getString("unidad_organizativa"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return unidadOrganizativa;
    }

    public void insertarUnidadOrganizativa (UnidadOrganizativa unidadOrganizativa) throws SQLException{

        var function ="{call insertar_unidad_organizativa(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setString(1,unidadOrganizativa.getUnidad_organizativa());
        statement.execute();
        statement.close();
    }

    public void editarUnidadOrganizativa (UnidadOrganizativa unidadOrganizativa) throws SQLException{
        var function ="{call editar_unidad_organizativa(?,?)}";
        CallableStatement callableStatement =Conexion.getConnection().prepareCall(function);
        callableStatement.setInt(1,unidadOrganizativa.getId_unidad_organizativa());
        callableStatement.setString(2,unidadOrganizativa.getUnidad_organizativa());
        callableStatement.execute();
        callableStatement.close();
    }

    public void eliminarUnidadOrganizativa(UnidadOrganizativa unidadOrganizativa) throws  SQLException{
        var function ="{call eliminar_unidad_organizativa(?)}";
        CallableStatement callableStatement =Conexion.getConnection().prepareCall(function);
        callableStatement.setInt(1,unidadOrganizativa.getId_unidad_organizativa());
        callableStatement.execute();
        callableStatement.close();
    }

}
