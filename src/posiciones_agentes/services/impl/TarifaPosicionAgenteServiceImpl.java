package posiciones_agentes.services.impl;

import posiciones_agentes.models.TarifasPosicionAgente;
import posiciones_agentes.services.TarifaPosicionAgenteService;
import services.ServiceLocator;
import util.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TarifaPosicionAgenteServiceImpl implements TarifaPosicionAgenteService {
    @Override
    public List<TarifasPosicionAgente> getAll(){
        String query = "Select * from tarifas_posiciones_agentes";
        List<TarifasPosicionAgente> tarifas = new LinkedList<>();
        try{
            ResultSet rs = Util.executeQuery(query);
            tarifas = getTarifasFromRS(rs);
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return tarifas;
    }

    @Override
    public TarifasPosicionAgente getTarifaById(int id){
        TarifasPosicionAgente tarifa = null;
        String query = "Select * from tarifas_posiciones_agentes where id_tarifa = " + id;
        try {
            ResultSet rs = Util.executeQuery(query);
            tarifa = getTarifaFromRS(rs);
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return tarifa;
    }

    @Override
    public void deleteByID(int id){
        String query = "Delete from tarifas_posiciones_agentes Where id_tarifa = " + id;
        try {
            Util.executeQuery(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public TarifasPosicionAgente getTarifaByUoAndProv(int id_uorg, int id_prov){
        String query = "Select * from tarifas_posiciones_agentes where id_uorg = " + id_uorg + "" +
                " and id_pservicio = " + id_prov;
        TarifasPosicionAgente tarifa = null;
        try {
            ResultSet rs = Util.executeQuery(query);
            tarifa = getTarifaFromRS(rs);
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return tarifa;
    }

    @Override
    public void registerTarifa(TarifasPosicionAgente tarifasPosicionAgente){
        String function = "{call insert_tarifa(?,?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, tarifasPosicionAgente.getUnidadOrganizativa().getId_unidad_organizativa());
            statement.setInt(2, tarifasPosicionAgente.getProveedorServicio().getId());
            statement.setDouble(3, tarifasPosicionAgente.getTarifa());
            statement.execute();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateTarifa(TarifasPosicionAgente tarifasPosicionAgente){
        String function = "{call update_tarifa(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, tarifasPosicionAgente.getIdTarifa());
            statement.setDouble(2, tarifasPosicionAgente.getTarifa());
            statement.execute();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private TarifasPosicionAgente getTarifaFromRS(ResultSet rs){
        TarifasPosicionAgente tp = null;
        try {
            if(rs.next()){
                tp = new TarifasPosicionAgente(
                        rs.getInt(1),
                        ServiceLocator.getUnidadOrganizativaService().getOneUnidadOrganizativa(rs.getInt(2)),
                        ServiceLocator.getProveedorServicioService().getById(rs.getInt(3)),
                        rs.getDouble(4)
                );
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return tp;
    }

    private List<TarifasPosicionAgente> getTarifasFromRS(ResultSet rs){
        List<TarifasPosicionAgente> tarifas = new LinkedList<>();

        try {
            while (rs.next()) {
                tarifas.add(new TarifasPosicionAgente(
                        rs.getInt(1),
                        ServiceLocator.getUnidadOrganizativaService().getOneUnidadOrganizativa(rs.getInt(2)),
                        ServiceLocator.getProveedorServicioService().getById(rs.getInt(3)),
                        rs.getDouble(4)
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return tarifas;
    }
}
