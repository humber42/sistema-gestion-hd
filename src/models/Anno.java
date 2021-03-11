package models;

public class Anno {

    private Integer id_anno;
    private int anno;
    private boolean active;

    public Anno() {
        this(0, true);
    }

    public Anno(int anno, boolean active) {
        this.anno = anno;
        this.active = active;
    }

    public int getId_anno() {
        return id_anno;
    }

    public void setId_anno(int id_anno) {
        this.id_anno = id_anno;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
