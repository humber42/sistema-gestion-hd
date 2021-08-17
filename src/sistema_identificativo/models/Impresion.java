package sistema_identificativo.models;

public class Impresion {
    private String numero_pase;
    private String identidad;
    private String nombre;
    private String tipoPase;
    private String codigoPase;
    private int cantImpresiones;

    public Impresion(String numero_pase, String identidad, String nombre, String tipoPase, String codigoPase, int cantImpresiones) {
        this.numero_pase = numero_pase;
        this.identidad = identidad;
        this.nombre = nombre;
        this.tipoPase = tipoPase;
        this.codigoPase = codigoPase;
        this.cantImpresiones = cantImpresiones;
    }

    public String getNumero_pase() {
        return numero_pase;
    }

    public void setNumero_pase(String numero_pase) {
        this.numero_pase = numero_pase;
    }

    public String getIdentidad() {
        return identidad;
    }

    public void setIdentidad(String identidad) {
        this.identidad = identidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoPase() {
        return tipoPase;
    }

    public void setTipoPase(String tipoPase) {
        this.tipoPase = tipoPase;
    }

    public String getCodigoPase() {
        return codigoPase;
    }

    public void setCodigoPase(String codigoPase) {
        this.codigoPase = codigoPase;
    }

    public int getCantImpresiones() {
        return cantImpresiones;
    }

    public void setCantImpresiones(int cantImpresiones) {
        this.cantImpresiones = cantImpresiones;
    }
}
