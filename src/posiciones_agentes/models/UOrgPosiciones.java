package posiciones_agentes.models;

public class UOrgPosiciones {
    private String uorg;
    private int cantPosiciones;
    private int sepsa;
    private int sepcom;
    private int cap;
    private int agesp;
    private int otros;
    private double gasto;

    public UOrgPosiciones(){
        this(null,0,0,0,0,0,0,0);
    }

    public UOrgPosiciones(String uorg, int cantPosiciones, int sepsa, int sepcom, int cap, int agesp, int otros, double gasto) {
        this.uorg = uorg;
        this.cantPosiciones = cantPosiciones;
        this.sepsa = sepsa;
        this.sepcom = sepcom;
        this.cap = cap;
        this.agesp = agesp;
        this.otros = otros;
        this.gasto = gasto;
    }

    public String getUorg() {
        return uorg;
    }

    public void setUorg(String uorg) {
        this.uorg = uorg;
    }

    public int getCantPosiciones() {
        return cantPosiciones;
    }

    public void setCantPosiciones(int cantPosiciones) {
        this.cantPosiciones = cantPosiciones;
    }

    public int getSepsa() {
        return sepsa;
    }

    public void setSepsa(int sepsa) {
        this.sepsa = sepsa;
    }

    public int getSepcom() {
        return sepcom;
    }

    public void setSepcom(int sepcom) {
        this.sepcom = sepcom;
    }

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public int getAgesp() {
        return agesp;
    }

    public void setAgesp(int agesp) {
        this.agesp = agesp;
    }

    public int getOtros() {
        return otros;
    }

    public void setOtros(int otros) {
        this.otros = otros;
    }

    public double getGasto() {
        return gasto;
    }

    public void setGasto(double gasto) {
        this.gasto = gasto;
    }
}
