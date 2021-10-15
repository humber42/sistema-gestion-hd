package seguridad.services.impl;

import seguridad.models.User;
import seguridad.services.UserService;
import util.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public List<User> getAllUsers() {
        List<User> userList = new LinkedList<>();
        try {
            String query = "SELECT * FROM usuario";
            userList = getUsuariosFromRS(Util.executeQuery(query));
        } catch (SQLException e){
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public User getUserById(int id) {
        User user = null;
        String query = "SELECT * FROM usuario WHERE user_id = " + id;
        try {
            ResultSet rs = Util.executeQuery(query);
            user = getUsuarioFromRS(rs);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User getUserByUserName(String username) {
        User user = null;
        String query = "SELECT * FROM usuario WHERE username = " + username;
        try {
            ResultSet rs = Util.executeQuery(query);
            user = getUsuarioFromRS(rs);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void deleteUserById(int id) {
        String query = "DELETE FROM usuario WHERE user_id = " + id;
        try {
            Util.executeQuery(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(User user) {
        String function = "{call insertar_usuario(?,?,?,?)}";
        try{
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setString(1, user.getNombre());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getId_rol());
            statement.execute();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        String function = "{call update_usuario(?,?,?,?,?)}";
        try{
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, user.getId_user());
            statement.setString(2, user.getNombre());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getPassword());
            statement.setInt(5, user.getId_rol());
            statement.execute();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private User getUsuarioFromRS(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getInt(5)
        );
    }

    private LinkedList<User> getUsuariosFromRS(ResultSet rs) throws SQLException{
        LinkedList<User> userLinkedList = new LinkedList<>();
        while (rs.next()){
            userLinkedList.add(this.getUsuarioFromRS(rs));
        }
        return userLinkedList;
    }
}
