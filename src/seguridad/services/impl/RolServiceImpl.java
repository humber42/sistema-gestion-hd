package seguridad.services.impl;

import seguridad.models.Rol;
import seguridad.services.RolService;
import util.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RolServiceImpl implements RolService {
    @Override
    public List<Rol> getAllRols() {
        List<Rol> rolList = new LinkedList<>();
        try {
            String query = "SELECT * FROM rol";
            rolList = getRolesFromRS(Util.executeQuery(query));
        } catch (SQLException e){
            e.printStackTrace();
        }
        return rolList;
    }

    @Override
    public Rol getRolById(int id) {
        Rol rol = null;
        String query = "SELECT * FROM rol WHERE id_rol = " + id;
        try {
            ResultSet rs = Util.executeQuery(query);
            rol = getRolFromRS(rs);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return rol;
    }

    @Override
    public Rol getRolByName(String nombre) {
        Rol rol = null;
        String query = "SELECT * FROM rol WHERE nombre = " + nombre;
        try {
            ResultSet rs = Util.executeQuery(query);
            rol = getRolFromRS(rs);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return rol;
    }

    @Override
    public void saveRol(Rol rol) {
        String function = "{call insertar_rol(?)}";
        try{
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setString(1, rol.getNombre());
            statement.execute();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateRol(Rol rol) {
        String function = "{call update_rol(?,?)}";
        try{
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, rol.getId_rol());
            statement.setString(2, rol.getNombre());
            statement.execute();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRolById(int id) {
        String query = "DELETE FROM rol WHERE id_rol = "+ id;
        try {
            Util.executeQuery(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Rol getRolFromRS(ResultSet rs) throws SQLException {
        return new Rol(
                rs.getInt(1),
                rs.getString(2)
        );
    }

    private LinkedList<Rol> getRolesFromRS(ResultSet rs) throws SQLException{
        LinkedList<Rol> rolLinkedList = new LinkedList<>();
        while (rs.next()){
            rolLinkedList.add(this.getRolFromRS(rs));
        }
        return rolLinkedList;
    }
}
