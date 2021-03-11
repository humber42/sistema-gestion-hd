package models;

public class ResumenModels {

    private String unidadOrganizativa;
    private int cantHechos;
    private double serviciosAfectados;
    private double afectacionMlc;

    public ResumenModels() {
        this(null, 0, 0, 0);
    }

    public ResumenModels(String unidadOrganizativa, int cantHechos, double afectacionMlc, double serviciosAfectados) {
        this.afectacionMlc = afectacionMlc;
        this.cantHechos = cantHechos;
        this.serviciosAfectados = serviciosAfectados;
        this.unidadOrganizativa = unidadOrganizativa;
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

    public double getServiciosAfectados() {
        return serviciosAfectados;
    }

    public void setServiciosAfectados(double serviciosAfectados) {
        this.serviciosAfectados = serviciosAfectados;
    }

    public double getAfectacionMlc() {
        return afectacionMlc;
    }

    public void setAfectacionMlc(double afectacionMlc) {
        this.afectacionMlc = afectacionMlc;
    }
}
