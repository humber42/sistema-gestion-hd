package models;

public class CantidadDelitoRangoFecha {

    private String mes;
    private int pext;
    private int tpub;
    private int robo;
    private int hurto;
    private int fraude;
    private int accioncr;
    private int otros;

    public CantidadDelitoRangoFecha(String mes, int pext, int tpub, int robo, int hurto, int fraude, int accioncr, int otros) {
        this.mes = mes;
        this.pext = pext;
        this.tpub = tpub;
        this.robo = robo;
        this.hurto = hurto;
        this.fraude = fraude;
        this.accioncr = accioncr;
        this.otros = otros;
    }

    public CantidadDelitoRangoFecha() {
        this(null, 0, 0, 0, 0, 0, 0, 0);
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

    public int getAccioncr() {
        return accioncr;
    }

    public void setAccioncr(int accioncr) {
        this.accioncr = accioncr;
    }

    public int getOtros() {
        return otros;
    }

    public void setOtros(int otros) {
        this.otros = otros;
    }
}
