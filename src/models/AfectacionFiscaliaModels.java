package models;

public class AfectacionFiscaliaModels {

    private String tipoAfectacion;
    private int cantHechos;

    public AfectacionFiscaliaModels() {
        this(null, 0);
    }

    public AfectacionFiscaliaModels(String tipoAfectacion, int cantHechos) {
        this.tipoAfectacion = tipoAfectacion;
        this.cantHechos = cantHechos;
    }

    public String getTipoAfectacion() {
        return tipoAfectacion;
    }

    public void setTipoAfectacion(String tipoAfectacion) {
        this.tipoAfectacion = tipoAfectacion;
    }

    public int getCantHechos() {
        return cantHechos;
    }

    public void setCantHechos(int cantHechos) {
        this.cantHechos = cantHechos;
    }
}
