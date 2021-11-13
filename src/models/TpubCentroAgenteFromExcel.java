package models;


public class TpubCentroAgenteFromExcel {

    private String municipio;
    private int tpubs;
    private int centroAgentes;

    public TpubCentroAgenteFromExcel(String municipio, int tpubs, int centroAgentes) {
        this.municipio = municipio;
        this.tpubs = tpubs;
        this.centroAgentes = centroAgentes;
    }

    public TpubCentroAgenteFromExcel() {
        this(null,0,0);
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public int getTpubs() {
        return tpubs;
    }

    public void setTpubs(int tpubs) {
        this.tpubs = tpubs;
    }

    public int getCentroAgentes() {
        return centroAgentes;
    }

    public void setCentroAgentes(int centroAgentes) {
        this.centroAgentes = centroAgentes;
    }
}
