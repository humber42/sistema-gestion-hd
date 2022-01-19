package models;

public class AveriasPext {

    private Integer id_avpext;
    private String causa;

    public AveriasPext() {
        this(null);
    }

    public AveriasPext(int id_avpext, String causa) {
        this.id_avpext = id_avpext;
        this.causa = causa;
    }

    public AveriasPext(String causa) {
        this.causa = causa;
    }

    public int getId_avpext() {
        return id_avpext;
    }

    public void setId_avpext(int id_avpext) {
        this.id_avpext = id_avpext;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }
}
