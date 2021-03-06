package seguridad.models;

public class UserLoggedIn {
    private String nombre;
    private String username;
    private String password;
    private String rol;
    private Boolean permiso_pases;
    private Boolean permiso_visualizacion;
    private Boolean superuser;

    public UserLoggedIn(){}

    public UserLoggedIn(String nombre, String username, String password, String rol){
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.rol = rol;

        if(rol.equalsIgnoreCase("Admin")){
             permiso_pases = false;
             permiso_visualizacion = false;
             superuser = true;
        }
        else if(rol.equalsIgnoreCase("Jefe")){
             permiso_visualizacion = true;
             permiso_pases = false;
             superuser = false;
        }
        else if(rol.equalsIgnoreCase("Especialista")){
             permiso_pases = true;
             permiso_visualizacion = false;
             superuser = false;
        }
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean hasPermiso_pases() {
        return permiso_pases;
    }

    public void setPermiso_pases(Boolean permiso_pases) {
        this.permiso_pases = permiso_pases;
    }

    public Boolean hasPermiso_visualizacion() {
        return permiso_visualizacion;
    }

    public void setPermiso_visualizacion(Boolean permiso_visualizacion) {
        this.permiso_visualizacion = permiso_visualizacion;
    }

    public Boolean isSuperuser() {
        return superuser;
    }

    public void setSuperuser(Boolean superuser) {
        this.superuser = superuser;
    }
}
