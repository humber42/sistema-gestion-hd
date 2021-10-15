package seguridad.models;

public class Rol {
    private Integer id_rol;
    private String nombre;

    public Rol(Integer id_rol, String nombre) {
        this.id_rol = id_rol;
        this.nombre = nombre;
    }

    public Integer getId_rol() {
        return id_rol;
    }

    public void setId_rol(Integer id_rol) {
        this.id_rol = id_rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
