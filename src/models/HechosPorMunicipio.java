package models;

public class HechosPorMunicipio {
    private String nombreMunicipio;
    private int cantHechos;

    public HechosPorMunicipio() {
        this(null, 0);
    }

    public HechosPorMunicipio(String nombreMunicipio, int cantHechos) {
        this.nombreMunicipio = nombreMunicipio;
        this.cantHechos = cantHechos;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public int getCantHechos() {
        return cantHechos;
    }

    public void setCantHechos(int cantHechos) {
        this.cantHechos = cantHechos;
    }
}
