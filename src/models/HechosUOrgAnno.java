package models;

public class HechosUOrgAnno {

    private String unidad;
    private int cant_pext;
    private int cant_tpub;


    public HechosUOrgAnno(String unidad, int cant_pext, int cant_tpub) {
        this.unidad = unidad;
        this.cant_pext = cant_pext;
        this.cant_tpub = cant_tpub;
    }

    public HechosUOrgAnno() {
        this(null, 0, 0);
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public int getCant_pext() {
        return cant_pext;
    }

    public void setCant_pext(int cant_pext) {
        this.cant_pext = cant_pext;
    }

    public int getCant_tpub() {
        return cant_tpub;
    }

    public void setCant_tpub(int cant_tpub) {
        this.cant_tpub = cant_tpub;
    }
}
