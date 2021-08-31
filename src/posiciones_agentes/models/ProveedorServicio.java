package posiciones_agentes.models;

/**
 * @model ProveedorServicio
 * @author Humberto Cabrera Dominguez
 */
public class ProveedorServicio {
    private int id;
    private String proveedorServicio;

    public void register(){

    }

    public void delete(){

    }

    public ProveedorServicio(int id, String proveedorServicio) {
        this.id = id;
        this.proveedorServicio = proveedorServicio;
    }

    public ProveedorServicio() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProveedorServicio() {
        return proveedorServicio;
    }

    public void setProveedorServicio(String proveedorServicio) {
        this.proveedorServicio = proveedorServicio;
    }
}
