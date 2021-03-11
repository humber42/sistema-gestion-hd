package models;

public class EstacionesPublicas {

    private String unidadOrganizativa;
    private int cantEstaciones;

    public EstacionesPublicas() {
        this(null, 0);
    }

    public EstacionesPublicas(String unidadOrganizativa, int cantEstaciones) {
        this.unidadOrganizativa = unidadOrganizativa;
        this.cantEstaciones = cantEstaciones;
    }

    public String getUnidadOrganizativa() {
        return unidadOrganizativa;
    }

    public void setUnidadOrganizativa(String unidadOrganizativa) {
        this.unidadOrganizativa = unidadOrganizativa;
    }

    public int getCantEstaciones() {
        return cantEstaciones;
    }

    public void setCantEstaciones(int cantEstaciones) {
        this.cantEstaciones = cantEstaciones;
    }
}
