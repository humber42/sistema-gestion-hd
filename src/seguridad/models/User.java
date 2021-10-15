package seguridad.models;

public class User {
    private Integer id_user;
    private String nombre;
    private String username;
    private String password;
    private Integer id_rol;

    public User(String nombre, String username, String password, Integer id_rol) {
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.id_rol = id_rol;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId_rol() {
        return id_rol;
    }

    public void setId_rol(Integer id_rol) {
        this.id_rol = id_rol;
    }
}
