package models;

public class HechosMesAnno {

    private String mes;
    private int pext;
    private int tpub;
    private int robo;
    private int hurto;
    private int fraude;
    private int otros_delitos;

    public HechosMesAnno() {
        this(null, 0, 0, 0, 0, 0, 0);
    }

    public HechosMesAnno(String mes, int pext, int tpub, int robo, int hurto, int fraude, int otros_delitos) {
        this.mes = mes;
        this.pext = pext;
        this.tpub = tpub;
        this.robo = robo;
        this.hurto = hurto;
        this.fraude = fraude;
        this.otros_delitos = otros_delitos;

    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getPext() {
        return pext;
    }

    public void setPext(int pext) {
        this.pext = pext;
    }

    public int getTpub() {
        return tpub;
    }

    public void setTpub(int tpub) {
        this.tpub = tpub;
    }

    public int getRobo() {
        return robo;
    }

    public void setRobo(int robo) {
        this.robo = robo;
    }

    public int getHurto() {
        return hurto;
    }

    public void setHurto(int hurto) {
        this.hurto = hurto;
    }

    public int getFraude() {
        return fraude;
    }

    public void setFraude(int fraude) {
        this.fraude = fraude;
    }

    public int getOtros_delitos() {
        return otros_delitos;
    }

    public void setOtros_delitos(int otros_delitos) {
        this.otros_delitos = otros_delitos;
    }
}
