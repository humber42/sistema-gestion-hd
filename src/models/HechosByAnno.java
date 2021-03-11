package models;

public class HechosByAnno {

    private double anno;
    private double mes;
    private int cantPext;
    private int cantTpub;

    public HechosByAnno() {
        this(0, 0, 0, 0);
    }

    public HechosByAnno(double anno, double mes, int cantPext, int cantTpub) {
        this.anno = anno;
        this.mes = mes;
        this.cantPext = cantPext;
        this.cantTpub = cantTpub;
    }

    public double getAnno() {
        return anno;
    }

    public void setAnno(double anno) {
        this.anno = anno;
    }

    public double getMes() {
        return mes;
    }

    public void setMes(double mes) {
        this.mes = mes;
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
