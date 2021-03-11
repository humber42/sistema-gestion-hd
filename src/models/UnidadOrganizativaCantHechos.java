package models;

public class UnidadOrganizativaCantHechos {
    private String unidadOrganizativa;
    private int cantHechos;

    public UnidadOrganizativaCantHechos(String unidadOrganizativa, int cantHechos) {
        this.unidadOrganizativa = unidadOrganizativa;
        this.cantHechos = cantHechos;
    }

    public UnidadOrganizativaCantHechos() {
        this(null, 0);
    }

    public String getUnidadOrganizativa() {
        return unidadOrganizativa;
    }

    public void setUnidadOrganizativa(String unidadOrganizativa) {
        this.unidadOrganizativa = unidadOrganizativa;
    }

    public int getCantHechos() {
        return cantHechos;
    }

    public void setCantHechos(int cantHechos) {
        this.cantHechos = cantHechos;
    }
}
