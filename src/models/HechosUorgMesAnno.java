package models;

public class HechosUorgMesAnno {

    private double datePart;
    private String unidad;
    private String mes;
    private int cantHechos;

    public HechosUorgMesAnno(double datePart, String unidad, String mes, int cantHechos) {
        this.datePart = datePart;
        this.unidad = unidad;
        this.mes = mes;
        this.cantHechos = cantHechos;
    }

    public HechosUorgMesAnno() {
        this(0, null, null, 0);
    }

    public double getDatePart() {
        return datePart;
    }

    public void setDatePart(double datePart) {
        this.datePart = datePart;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getCantHechos() {
        return cantHechos;
    }

    public void setCantHechos(int cantHechos) {
        this.cantHechos = cantHechos;
    }
}
