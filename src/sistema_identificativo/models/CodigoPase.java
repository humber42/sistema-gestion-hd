package sistema_identificativo.models;

public class CodigoPase {
    private int id;
    private String codigo;

    public CodigoPase(int id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }

    public CodigoPase() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
