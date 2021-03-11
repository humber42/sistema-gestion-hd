package models;


/**
 * @author humberto
 */
public class Municipio {

    private Integer id_municipio;
    private String municipio;

    public Municipio() {
        this(null);
    }

    private Municipio(String municipio) {
        this.setMunicipio(municipio);
    }

    public int getId_municipio() {
        return id_municipio;
    }

    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
}
