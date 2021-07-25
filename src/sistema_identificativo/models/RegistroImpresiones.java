package sistema_identificativo.models;

public class RegistroImpresiones {
    private int id;
    private RegistroPase pase;
    private int cantidadImpresiones;
    private String ultimaImpresion;

    public RegistroImpresiones(int id, RegistroPase pase, int cantidadImpresiones, String ultimaImpresion) {
        this.id = id;
        this.pase = pase;
        this.cantidadImpresiones = cantidadImpresiones;
        this.ultimaImpresion = ultimaImpresion;
    }

    public RegistroImpresiones() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RegistroPase getPase() {
        return pase;
    }

    public void setPase(RegistroPase pase) {
        this.pase = pase;
    }

    public int getCantidadImpresiones() {
        return cantidadImpresiones;
    }

    public void setCantidadImpresiones(int cantidadImpresiones) {
        this.cantidadImpresiones = cantidadImpresiones;
    }

    public String getUltimaImpresion() {
        return ultimaImpresion;
    }

    public void setUltimaImpresion(String ultimaImpresion) {
        this.ultimaImpresion = ultimaImpresion;
    }
}
