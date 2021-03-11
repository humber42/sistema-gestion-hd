package models;

public class PCC_territorio {

    private int id_territorio;
    private String territorio;

    public PCC_territorio() {
        this(null);
    }

    public PCC_territorio(String territorio) {
        this.territorio = territorio;
    }

    public int getId_territorio() {
        return id_territorio;
    }

    public void setId_territorio(int id_territorio) {
        this.id_territorio = id_territorio;
    }

    public String getTerritorio() {
        return territorio;
    }

    public void setTerritorio(String territorio) {
        this.territorio = territorio;
    }
}
