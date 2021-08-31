package models;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Afectaciones {
    private String unidadOrganizativa;
    private int cantHechos;
    private int afectaciones;
    private int cantEstacionesPublicas;
    private double serviciosAfectadosVSEstacionesPublicas;


    public Afectaciones(String unidadOrganizativa, int cantHechos, int afectaciones, int cantEstacionesPublicas) {
        this.unidadOrganizativa = unidadOrganizativa;
        this.cantHechos = cantHechos;
        this.afectaciones = afectaciones;
        this.cantEstacionesPublicas = cantEstacionesPublicas;

        DecimalFormat format = new DecimalFormat("#.##");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        format.setDecimalFormatSymbols(dfs);
        this.serviciosAfectadosVSEstacionesPublicas = Double.valueOf(
                format.format(
                        (double) 100 * afectaciones / cantEstacionesPublicas)
        );
       // this.serviciosAfectadosVSEstacionesPublicas= (double)100*afectaciones/cantEstacionesPublicas;
    }

    public Afectaciones(String unidadOrganizativa, int cantHechos, int afectaciones, int cantEstacionesPublicas, double serviciosAfectadosVSEstacionesPublicas) {
        this.unidadOrganizativa = unidadOrganizativa;
        this.cantHechos = cantHechos;
        this.afectaciones = afectaciones;
        this.cantEstacionesPublicas = cantEstacionesPublicas;
        DecimalFormat format = new DecimalFormat("#.##");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        format.setDecimalFormatSymbols(dfs);
        this.serviciosAfectadosVSEstacionesPublicas = Double.valueOf(
                format.format(serviciosAfectadosVSEstacionesPublicas));

        //this.serviciosAfectadosVSEstacionesPublicas= serviciosAfectadosVSEstacionesPublicas;
    }

    public Afectaciones() {
        this(null, 0, 0, 0);
    }

    public Afectaciones(String nombre, Double percent) {
        this.unidadOrganizativa = nombre;
        this.serviciosAfectadosVSEstacionesPublicas = percent;
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

    public int getAfectaciones() {
        return afectaciones;
    }

    public void setAfectaciones(int afectaciones) {
        this.afectaciones = afectaciones;
    }

    public int getCantEstacionesPublicas() {
        return cantEstacionesPublicas;
    }

    public void setCantEstacionesPublicas(int cantEstacionesPublicas) {
        this.cantEstacionesPublicas = cantEstacionesPublicas;
    }

    public double getServiciosAfectadosVSEstacionesPublicas() {
        return serviciosAfectadosVSEstacionesPublicas;
    }

    public void setServiciosAfectadosVSEstacionesPublicas(double serviciosAfectadosVSEstacionesPublicas) {
        this.serviciosAfectadosVSEstacionesPublicas = serviciosAfectadosVSEstacionesPublicas;
    }
}
