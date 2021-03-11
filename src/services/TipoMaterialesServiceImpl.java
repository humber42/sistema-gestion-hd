package services;

import models.MaterialsFiscaliaModels;
import models.TipoMateriales;
import util.Conexion;
import util.Util;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class TipoMaterialesServiceImpl implements TipoMaterialesService {

    public TipoMateriales getOneTipo(int id) {
        TipoMateriales tipoMateriales = new TipoMateriales();
        var query = "SELECT * FROM tipo_materiales WHERE id_materiales=" + Integer.toString(id);
        try {
            Statement statement = Conexion.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                tipoMateriales = recuperarResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoMateriales;
    }


    public TipoMateriales searchMaterialesByName(String name) {
        var query = "SELECT * FROM tipo_materiales WHERE materiales= '" + name + "'";
        TipoMateriales tipoMateriales = new TipoMateriales();
        try {
            var resultSet = Util.executeQuery(query);

            if (resultSet.next())
                tipoMateriales = recuperarResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoMateriales;
    }


    @Override
    public List<TipoMateriales> fetchAll() {
        List<TipoMateriales> tipoMaterialesList = new LinkedList<>();
        var query = "Select * From tipo_materiales";
        try {
            var resultSet = Util.executeQuery(query);
            while (resultSet.next()) {
                tipoMaterialesList.add(recuperarResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipoMaterialesList;
    }


    @Override
    public LinkedList<MaterialsFiscaliaModels> materialesPorAnno(Date date) {
        String function = "{call cantidad_materiales_anno_cierre_fiscalia(?,?)}";
        String fechaInicio = String.valueOf(date.toLocalDate().getYear()) + "-01-01";
        LinkedList<MaterialsFiscaliaModels> retorno = new LinkedList<>();
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setString(2, fechaInicio);
            statement.execute();
            retorno = this.recuperarListaMaterialesFiscaliaModels(statement.getResultSet());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    private LinkedList<MaterialsFiscaliaModels> recuperarListaMaterialesFiscaliaModels(ResultSet set) {
        LinkedList<MaterialsFiscaliaModels> materialsFiscaliaModels = new LinkedList<>();
        try {
            while (set.next()) {
                materialsFiscaliaModels.add(new MaterialsFiscaliaModels(
                        set.getString(1),
                        set.getInt(2),
                        set.getInt(3)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materialsFiscaliaModels;
    }


    private TipoMateriales recuperarResultSet(ResultSet resultSet) {
        TipoMateriales tipoMateriales = new TipoMateriales();

        try {
            tipoMateriales.setId_materiales(resultSet.getInt("id_materiales"));
            tipoMateriales.setMateriales(resultSet.getString("materiales"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tipoMateriales;
    }

}
