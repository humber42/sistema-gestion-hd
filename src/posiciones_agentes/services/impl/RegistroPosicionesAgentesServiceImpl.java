package posiciones_agentes.services.impl;

import posiciones_agentes.models.RegistroPosicionesAgentes;
import posiciones_agentes.services.RegistroPosicionesAgentesService;
import util.Conexion;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

public class RegistroPosicionesAgentesServiceImpl implements RegistroPosicionesAgentesService {
    @Override
    public List<RegistroPosicionesAgentes> getAllRegistroPosicionesAgentes() {
        return null;
    }

    @Override
    public RegistroPosicionesAgentes getByID(int id) {
        return null;
    }

    @Override
    public void eliminarRegisterPosicionesAgentes(RegistroPosicionesAgentes registroPosicionesAgentes) {

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
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void updateRegisterPosicionesAgentes(RegistroPosicionesAgentes registroPosicionesAgentes) {

    }
}
