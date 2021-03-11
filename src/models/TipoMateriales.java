package models;

public class TipoMateriales {

    private Integer id_materiales;
    private String materiales;

    public TipoMateriales() {
        this(null);
    }

    public TipoMateriales(String materiales) {
        this.materiales = materiales;
    }

    public int getId_materiales() {
        return id_materiales;
    }

    public void setId_materiales(int id_materiales) {
        this.id_materiales = id_materiales;
    }

    public String getMateriales() {
        return materiales;
    }

    public void setMateriales(String materiales) {
        this.materiales = materiales;
    }
}
