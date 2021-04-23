package models;

public class AfectacionesServiciosAfectados {

    private double usd;
    private double mn;
    private double serv_afectados;


    public AfectacionesServiciosAfectados(double usd, double mn, double servicios) {
        this.mn = mn;
        this.usd = usd;
        this.serv_afectados = servicios;
    }

    public AfectacionesServiciosAfectados() {
        this(0, 0, 0);
    }

    public double getMn() {
        return mn;
    }

    public void setMn(double mn) {
        this.mn = mn;
    }

    public double getServ_afectados() {
        return serv_afectados;
    }

    public void setServ_afectados(double serv_afectados) {
        this.serv_afectados = serv_afectados;
    }

    public double getUsd() {
        return usd;
    }

    public void setUsd(double usd) {
        this.usd = usd;
    }


}
