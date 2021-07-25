package sistema_identificativo.models;

public class TipoPase {
    private int id;
    private String tipoPase;

    public TipoPase() {
    }

    public TipoPase(int id, String tipoPase) {
        this.id = id;
        this.tipoPase = tipoPase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoPase() {
        return tipoPase;
    }

    public void setTipoPase(String tipoPase) {
        this.tipoPase = tipoPase;
    }
}
