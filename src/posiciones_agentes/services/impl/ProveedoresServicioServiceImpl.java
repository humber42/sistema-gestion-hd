package posiciones_agentes.services.impl;

import posiciones_agentes.models.ProveedorServicio;
import posiciones_agentes.services.ProveedorServicioService;
import util.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ProveedoresServicioServiceImpl implements ProveedorServicioService {

    @Override
    public List<ProveedorServicio> getAllProveedorServicio() {
        String query = "Select * FROM proveedores_servicio_agentes";
        try{
            return this.recuperarListaResulset(Util.executeQuery(query));
        }catch (SQLException e ){
            e.printStackTrace();
            return null;
        }
    }

    @Override

    public ProveedorServicio getByName(String name){
        String query = "Select * from proveedores_servicio_agentes where proveedores_servicio = '"+ name +"'";
        ProveedorServicio ps = null;
        try {
            ResultSet rs = Util.executeQuery(query);
            ps = recuperarObjeto(rs);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ps;

    }

    @Override
    public ProveedorServicio getById(int id) {
        String query = "Select * from proveedores_servicio_agentes where id = " + id;
        ProveedorServicio ps = null;
        try {
            ResultSet rs = Util.executeQuery(query);
            ps = recuperarObjeto(rs);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ps;
    }

    @Override
    public void deleteProveedorServicioById(int id) {
        String query = "DELETE FROM proveedores_servicio_agentes where id = " + id;
        try{
            Util.executeQuery(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void registerProveedorServicio(ProveedorServicio proveedorServicio) {
        String function = "{call insert_proveedor(?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setString(1, proveedorServicio.getProveedorServicio());
            statement.execute();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateProveedorServicio(ProveedorServicio proveedorServicio) {
        String function = "{call update_proveedor(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, proveedorServicio.getId());
            statement.setString(2, proveedorServicio.getProveedorServicio());
            statement.execute();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private List<ProveedorServicio> recuperarListaResulset(ResultSet set){
        List<ProveedorServicio> proveedorServicios = new LinkedList<>();
        try {
            while (set.next()) {
                proveedorServicios.add(new ProveedorServicio(
                        set.getInt(1),
                        set.getString(2)
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return proveedorServicios;
    }

    private ProveedorServicio recuperarObjeto(ResultSet set){
        ProveedorServicio proveedorServicio = null;
        try{
            if(set.next()){
                proveedorServicio=new ProveedorServicio(set.getInt(1),set.getString(2));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return  proveedorServicio;
    }
}
