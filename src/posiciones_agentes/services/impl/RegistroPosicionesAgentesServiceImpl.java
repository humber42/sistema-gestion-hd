package posiciones_agentes.services.impl;

import posiciones_agentes.models.RegistroPosicionesAgentes;
import posiciones_agentes.services.RegistroPosicionesAgentesService;
import services.ServiceLocator;
import util.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RegistroPosicionesAgentesServiceImpl implements RegistroPosicionesAgentesService {
    @Override
    public List<RegistroPosicionesAgentes> getAllRegistroPosicionesAgentes() {
        var query = "select * from registro_posiciones_agentes " +
                " group by registro_posiciones_agentes.id_uorg, registro_posiciones_agentes.id_reg";
        List<RegistroPosicionesAgentes> posList = new LinkedList<>();
        try{
            posList = obtenerLista(Util.executeQuery(query));
        } catch (SQLException e){
            e.printStackTrace();
        }
        return posList;
    }

    @Override
    public RegistroPosicionesAgentes getByID(int id) {
        var query = "Select * from registro_posiciones_agentes where id_reg = " + id;
        RegistroPosicionesAgentes rpa = null;
        try {
            ResultSet rs = Util.executeQuery(query);
            rpa = obtenerObjeto(rs);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rpa;
    }

    @Override
    public void eliminarRegisterPosicionesAgentes(RegistroPosicionesAgentes registroPosicionesAgentes) {
        var query = "DELETE FROM registro_posiciones_agentes WHERE id_reg = " + registroPosicionesAgentes.getIdReg();
        try{
            Util.executeQuery(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void registerRegisterPosicionesAgentes(RegistroPosicionesAgentes registroPosicionesAgentes) {
        var function = "{call save_registro_posiciones_agentes(?,?,?,?,?,?)}";
        try{
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setString(1,registroPosicionesAgentes.getInstalacion());
            statement.setInt(2,registroPosicionesAgentes.getUnidadOrganizativa().getId_unidad_organizativa());
            statement.setInt(3,registroPosicionesAgentes.getProveedorServicio().getId());
            statement.setInt(4,registroPosicionesAgentes.getHorasDiasLaborables());
            statement.setInt(5,registroPosicionesAgentes.getHorasDiasNoLaborables());
            statement.setInt(6,registroPosicionesAgentes.getCantidadEfectivos());
            statement.execute();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateRegisterPosicionesAgentes(RegistroPosicionesAgentes registroPosicionesAgentes) {
        var function = "{call update_registro_posiciones_agentes(?,?,?,?)}";
        try{
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, registroPosicionesAgentes.getIdReg());
            statement.setInt(2, registroPosicionesAgentes.getHorasDiasLaborables());
            statement.setInt(3, registroPosicionesAgentes.getHorasDiasNoLaborables());
            statement.setInt(4, registroPosicionesAgentes.getCantidadEfectivos());
            statement.execute();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<RegistroPosicionesAgentes> getAllRegistrosByUOrg(int idUorg) {
        List<RegistroPosicionesAgentes> list = new LinkedList<>();
        var query = "Select * from registro_posiciones_agentes where registro_posiciones_agentes.id_uorg = " + idUorg;
        try {
            list = obtenerLista(Util.executeQuery(query));
        } catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }

    private RegistroPosicionesAgentes obtenerObjeto(ResultSet rs){
        RegistroPosicionesAgentes rpa = null;
        try {
            if(rs.next()){
                rpa = new RegistroPosicionesAgentes(
                        rs.getInt(1),
                        rs.getString(2),
                        ServiceLocator.getUnidadOrganizativaService().getOneUnidadOrganizativa(rs.getInt(3)),
                        ServiceLocator.getProveedorServicioService().getById(rs.getInt(4)),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getInt(7)
                );
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return rpa;
    }

    private List<RegistroPosicionesAgentes> obtenerLista(ResultSet rs){
        List<RegistroPosicionesAgentes> list = new LinkedList<>();
        try{
            while (rs.next()){
                list.add(obtenerObjeto(rs));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}
