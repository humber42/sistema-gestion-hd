package posiciones_agentes.services.impl;

import posiciones_agentes.models.ProveedorServicio;
import posiciones_agentes.services.ProveedorServicioService;
import util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ProveedoresServicioServiceImpl implements ProveedorServicioService {

    @Override
    public List<ProveedorServicio> getAllProveedorServicio() {
        var query = "Select * FROM proveedores_servicio_agentes";
        try{
            return this.recuperarListaResulset(Util.executeQuery(query));
        }catch (SQLException e ){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProveedorServicio getByName(String name) {
        return null;
    }

    @Override
    public ProveedorServicio getById(int id) {
        return null;
    }

    @Override
    public void deleteProveedorServicioById(int id) {

    }

    @Override
    public void registerProveedorServicio(ProveedorServicio proveedorServicio) {

    }

    @Override
    public void updateProveedorServicio(ProveedorServicio proveedorServicio) {

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
