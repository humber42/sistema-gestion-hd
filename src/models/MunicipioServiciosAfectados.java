package models;

public class MunicipioServiciosAfectados {
    private String municipio;
    private Double cant_servicios;

    public MunicipioServiciosAfectados(String municipio, Double cant_servicios) {
        this.municipio = municipio;
        this.cant_servicios = cant_servicios;
    }

    public MunicipioServiciosAfectados() {
        this(null, 0.0);
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public Double getCant_servicios() {
        return cant_servicios;
    }

    public void setCant_servicios(Double cant_servicios) {
        this.cant_servicios = cant_servicios;
    }
}