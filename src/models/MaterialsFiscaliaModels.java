package models;

public class MaterialsFiscaliaModels {

    private String materialAfectado;
    private int cantHechos;
    private int cantMaterial;

    public MaterialsFiscaliaModels(String materialAfectado, int cantHechos, int cantMaterial) {
        this.materialAfectado = materialAfectado;
        this.cantHechos = cantHechos;
        this.cantMaterial = cantMaterial;
    }

    public MaterialsFiscaliaModels() {
        this(null, 0, 0);
    }

    public String getMaterialAfectado() {
        return materialAfectado;
    }

    public void setMaterialAfectado(String materialAfectado) {
        this.materialAfectado = materialAfectado;
    }

    public int getCantHechos() {
        return cantHechos;
    }

    public void setCantHechos(int cantHechos) {
        this.cantHechos = cantHechos;
    }

    public int getCantMaterial() {
        return cantMaterial;
    }

    public void setCantMaterial(int cantMaterial) {
        this.cantMaterial = cantMaterial;
    }
}
