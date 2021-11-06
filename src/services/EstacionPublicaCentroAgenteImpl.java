package services;

import models.EstacionPublicaCentroAgente;
import util.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class EstacionPublicaCentroAgenteImpl implements EstacionPublicaCentroAgenteService {

    @Override
    public int countAll() {
        String query = "SELECT COUNT(id_reg) FROM estacion_publica_centro_agente";
        return Util.count(query);
    }

    @Override
    public List<EstacionPublicaCentroAgente> getAll() {
        String query = "SELECT id_reg, municipio, unidad_organizativa, centros_agente, estaciones_publicas " +
                "FROM estacion_publica_centro_agente " +
                "JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = estacion_publica_centro_agente.id_uorg " +
                "JOIN municipios ON municipios.id_municipio = estacion_publica_centro_agente.id_municipio";
        List<EstacionPublicaCentroAgente> lista = new LinkedList<>();

        try {
            ResultSet rs = Util.executeQuery(query);
            lista = obtenerEstacionesFromRS(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<EstacionPublicaCentroAgente> getAllByIdUORG(int id_uorg) {
        String query = "SELECT id_reg, municipio, unidad_organizativa, centros_agente, estaciones_publicas " +
                "FROM estacion_publica_centro_agente " +
                "JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = estacion_publica_centro_agente.id_uorg " +
                "JOIN municipios ON municipios.id_municipio = estacion_publica_centro_agente.id_municipio " +
                "WHERE id_uorg = " + id_uorg;
        List<EstacionPublicaCentroAgente> lista = new LinkedList<>();

        try {
            ResultSet rs = Util.executeQuery(query);
            lista = obtenerEstacionesFromRS(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public EstacionPublicaCentroAgente getEstacionPublicaById(int id) {
        String query = "SELECT id_reg, municipio, unidad_organizativa, centros_agente, estaciones_publicas " +
                "FROM estacion_publica_centro_agente " +
                "JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = estacion_publica_centro_agente.id_uorg " +
                "JOIN municipios ON municipios.id_municipio = estacion_publica_centro_agente.id_municipio" +
                "WHERE id_reg = " + id;

        EstacionPublicaCentroAgente epca = null;

        try {
            ResultSet rs = Util.executeQuery(query);
            epca = obtenerEstacionFromRS(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return epca;
    }

    @Override
    public void addEstacionPublicaCentroAgente(EstacionPublicaCentroAgente estacion) {
        String function = "{call save_new_estacion_publica_centro_agente(?,?,?,?)}";
        int id_municipio = ServiceLocator.getMunicipiosService()
                .searchMunicipioByName(estacion.getMunicipio()).getId_municipio();
        int id_uorg =  ServiceLocator.getUnidadOrganizativaService()
                .searchUnidadOrganizativaByName(estacion.getUnidadOrganizativa()).getId_unidad_organizativa();
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, id_municipio);
            statement.setInt(2, id_uorg);
            statement.setInt(3, estacion.getCentroAgente());
            statement.setInt(4, estacion.getEstacionPublica());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateEstacionPublicaByIdMunicipio(EstacionPublicaCentroAgente estacion) {
        String function = "{call update_estacion_publica_centro_agente_by_id_municipio(?,?,?,?)}";
        int id_municipio = ServiceLocator.getMunicipiosService()
                .searchMunicipioByName(estacion.getMunicipio()).getId_municipio();
        int id_uorg =  ServiceLocator.getUnidadOrganizativaService()
                .searchUnidadOrganizativaByName(estacion.getUnidadOrganizativa()).getId_unidad_organizativa();
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, id_municipio);
            statement.setInt(2, id_uorg);
            statement.setInt(3, estacion.getCentroAgente());
            statement.setInt(4, estacion.getEstacionPublica());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateEstacionPublicaCentroAgente(EstacionPublicaCentroAgente estacion) {
        String function = "{call update_estacion_publica_centro_agente(?,?,?,?,?)}";
        int id_municipio = ServiceLocator.getMunicipiosService()
                .searchMunicipioByName(estacion.getMunicipio()).getId_municipio();
        int id_uorg =  ServiceLocator.getUnidadOrganizativaService()
                .searchUnidadOrganizativaByName(estacion.getUnidadOrganizativa()).getId_unidad_organizativa();
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, estacion.getIdReg());
            statement.setInt(2, id_municipio);
            statement.setInt(3, id_uorg);
            statement.setInt(4, estacion.getCentroAgente());
            statement.setInt(5, estacion.getEstacionPublica());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEstacionPublicaCentroAgente(int id) {
        String function = "{call delete_estacion_publica_centro_agente(?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, id);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private EstacionPublicaCentroAgente obtenerEstacionFromRS(ResultSet rs) {
        EstacionPublicaCentroAgente epca = null;

        try {
            if (rs.next())
                epca = new EstacionPublicaCentroAgente(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5)
                );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return epca;
    }

    private List<EstacionPublicaCentroAgente> obtenerEstacionesFromRS(ResultSet rs) {
        List<EstacionPublicaCentroAgente> estaciones = new LinkedList<>();

        try {
            while (rs.next()) {
                estaciones.add(
                        new EstacionPublicaCentroAgente(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getInt(4),
                                rs.getInt(5)
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return estaciones;
    }

}
