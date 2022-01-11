package posiciones_agentes.services.impl;

import posiciones_agentes.models.PosicionAgente;
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
        String query = "select * from registro_posiciones_agentes order by id_uorg";
        List<RegistroPosicionesAgentes> posList = new LinkedList<>();
        try {
            posList = obtenerLista(Util.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posList;
    }

    @Override
    public RegistroPosicionesAgentes getByID(int id) {
        String query = "Select * from registro_posiciones_agentes where id_reg = " + id;
        RegistroPosicionesAgentes rpa = null;
        try {
            ResultSet rs = Util.executeQuery(query);
            rpa = obtenerObjeto(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rpa;
    }

    @Override
    public void deletePosicionAgente(int id) {
        String function = "{call delete_posicion_agente(?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, id);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerRegisterPosicionesAgentes(RegistroPosicionesAgentes registroPosicionesAgentes) {
        String function = "{call save_registro_posiciones_agentes(?,?,?,?,?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setString(1, registroPosicionesAgentes.getInstalacion());
            statement.setInt(2, registroPosicionesAgentes.getUnidadOrganizativa().getId_unidad_organizativa());
            statement.setInt(3, registroPosicionesAgentes.getProveedorServicio().getId());
            statement.setInt(4, registroPosicionesAgentes.getHorasDiasLaborables());
            statement.setInt(5, registroPosicionesAgentes.getHorasDiasNoLaborables());
            statement.setInt(6, registroPosicionesAgentes.getCantidadEfectivos());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRegisterPosicionesAgentes(RegistroPosicionesAgentes registroPosicionesAgentes) {
        String function = "{call update_registro_posiciones_agentes(?,?,?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, registroPosicionesAgentes.getIdReg());
            statement.setInt(2, registroPosicionesAgentes.getHorasDiasLaborables());
            statement.setInt(3, registroPosicionesAgentes.getHorasDiasNoLaborables());
            statement.setInt(4, registroPosicionesAgentes.getCantidadEfectivos());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<RegistroPosicionesAgentes> getAllRegistrosByUOrg(int idUorg) {
        List<RegistroPosicionesAgentes> list = new LinkedList<>();
        String query = "Select * from registro_posiciones_agentes where registro_posiciones_agentes.id_uorg = " + idUorg;
        try {
            list = obtenerLista(Util.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<String> getAllUorgNames() {
        List<String> uorgs = new LinkedList<>();

        String query = "Select Distinct unidad_organizativa, id_unidad_organizativa From unidades_organizativas\n" +
                "Join registro_posiciones_agentes ON registro_posiciones_agentes.id_uorg = unidades_organizativas.id_unidad_organizativa" +
                " Order By id_unidad_organizativa";

        try {
            ResultSet rs = Util.executeQuery(query);
            while (rs.next()) {
                uorgs.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uorgs;
    }

    private RegistroPosicionesAgentes obtenerObjeto(ResultSet rs) {
        RegistroPosicionesAgentes rpa = null;
        try {
            if (rs.next()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rpa;
    }

    private List<RegistroPosicionesAgentes> obtenerLista(ResultSet rs) {
        List<RegistroPosicionesAgentes> list = new LinkedList<>();
        try {
            while (rs.next()) {
                list.add(new RegistroPosicionesAgentes(
                        rs.getInt(1),
                        rs.getString(2),
                        ServiceLocator.getUnidadOrganizativaService().getOneUnidadOrganizativa(rs.getInt(3)),
                        ServiceLocator.getProveedorServicioService().getById(rs.getInt(4)),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getInt(7)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<PosicionAgente> getAll() {
        List<PosicionAgente> posiciones = new LinkedList<>();

        String query = "select id_reg, instalacion, unidad_organizativa, proveedores_servicio, cantidad_efectivos, horas_dias_laborables, horas_no_laborales\n" +
                       "from registro_posiciones_agentes\n" +
                       "join unidades_organizativas on unidades_organizativas.id_unidad_organizativa = registro_posiciones_agentes.id_uorg\n" +
                       "join proveedores_servicio_agentes on proveedores_servicio_agentes.id = registro_posiciones_agentes.id_pservicio\n" +
                       "order by id_uorg";

        try{
            ResultSet rs = Util.executeQuery(query);
            while (rs.next()){
                posiciones.add(
                        new PosicionAgente(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getInt(5),
                                rs.getInt(6),
                                rs.getInt(7)
                        )
                );
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return posiciones;
    }

    @Override
    public List<PosicionAgente> getAllByUorg(int id) {
        List<PosicionAgente> posiciones = new LinkedList<>();

        String query = "select id_reg, instalacion, unidad_organizativa, proveedores_servicio, cantidad_efectivos, horas_dias_laborables, horas_no_laborales\n" +
                "from registro_posiciones_agentes\n" +
                "join unidades_organizativas on unidades_organizativas.id_unidad_organizativa = registro_posiciones_agentes.id_uorg\n" +
                "join proveedores_servicio_agentes on proveedores_servicio_agentes.id = registro_posiciones_agentes.id_pservicio\n" +
                "where id_uorg = " + id;

        try{
            ResultSet rs = Util.executeQuery(query);
            while (rs.next()){
                posiciones.add(
                        new PosicionAgente(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getInt(5),
                                rs.getInt(6),
                                rs.getInt(7)
                        )
                );
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return posiciones;
    }
}
