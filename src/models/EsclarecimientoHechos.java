package models;

public class EsclarecimientoHechos {

    private String direccionTerritorial;
    private int totalConciliados;
    private int cantPext;
    private int cantTpub;

    public EsclarecimientoHechos() {
        this(null, 0, 0, 0);
    }

    public EsclarecimientoHechos(String direccionTerritorial, int totalConciliados, int cantPext, int cantTpub) {
        this.direccionTerritorial = direccionTerritorial;
        this.totalConciliados = totalConciliados;
        this.cantPext = cantPext;
        this.cantTpub = cantTpub;
    }

    public String getDireccionTerritorial() {
        return direccionTerritorial;
    }

    public void setDireccionTerritorial(String direccionTerritorial) {
        this.direccionTerritorial = direccionTerritorial;
    }

    public int getTotalConciliados() {
        return totalConciliados;
    }

    public void setTotalConciliados(int totalConciliados) {
        this.totalConciliados = totalConciliados;
    }

    public int getCantPext() {
        return cantPext;
    }

    public void setCantPext(int cantPext) {
        this.cantPext = cantPext;
    }

    public int getCantTpub() {
        return cantTpub;
    }

    public void setCantTpub(int cantTpub) {
        this.cantTpub = cantTpub;
    }
}
