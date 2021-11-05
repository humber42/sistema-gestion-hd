package models;

public class EstacionPublicaCentroAgente {
    private int idReg;
    private String municipio;
    private String unidadOrganizativa;
    private int centroAgente;
    private int estacionPublica;

    public EstacionPublicaCentroAgente() {
    }

    public EstacionPublicaCentroAgente(int idReg, String municipio, String unidadOrganizativa, int centroAgente, int estacionPublica) {
        this.idReg = idReg;
        this.municipio = municipio;
        this.unidadOrganizativa = unidadOrganizativa;
        this.centroAgente = centroAgente;
        this.estacionPublica = estacionPublica;
    }

    public int getIdReg() {
        return idReg;
    }

    public void setIdReg(int idReg) {
        this.idReg = idReg;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUnidadOrganizativa() {
        return unidadOrganizativa;
    }

    public void setUnidadOrganizativa(String unidadOrganizativa) {
        this.unidadOrganizativa = unidadOrganizativa;
    }

    public int getCentroAgente() {
        return centroAgente;
    }

    public void setCentroAgente(int centroAgente) {
        this.centroAgente = centroAgente;
    }

    public int getEstacionPublica() {
        return estacionPublica;
    }

    public void setEstacionPublica(int estacionPublica) {
        this.estacionPublica = estacionPublica;
    }
}
