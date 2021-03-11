package models;

public class HechosCertifico {

    private String uorg;
    private int pext;
    private int tpub;
    private int robo;
    private int hurto;
    private int fraude;
    private int otros;

    public HechosCertifico() {
        this(null, 0, 0, 0, 0, 0, 0);
    }

    public HechosCertifico(String uorg, int pext, int tpub, int robo, int hurto, int fraude, int otros) {
        this.uorg = uorg;
        this.pext = pext;
        this.tpub = tpub;
        this.robo = robo;
        this.hurto = hurto;
        this.fraude = fraude;
        this.otros = otros;
    }

    public String getUorg() {
        return uorg;
    }

    public void setUorg(String uorg) {
        this.uorg = uorg;
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

    public int getOtros() {
        return otros;
    }

    public void setOtros(int otros) {
        this.otros = otros;
    }
}
