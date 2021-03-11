package models;

public class TipoHecho {

    private Integer id_tipo_hecho;
    private String tipo_hecho;

    public TipoHecho() {
        this(null);
    }

    public TipoHecho(String tipo_hecho) {
        this.tipo_hecho = tipo_hecho;
    }

    public int getId_tipo_hecho() {
        return id_tipo_hecho;
    }

    public void setId_tipo_hecho(int id_tipo_hecho) {
        this.id_tipo_hecho = id_tipo_hecho;
    }

    public String getTipo_hecho() {
        return tipo_hecho;
    }

    public void setTipo_hecho(String tipo_hecho) {
        this.tipo_hecho = tipo_hecho;
    }
}
