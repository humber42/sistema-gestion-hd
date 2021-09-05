package posiciones_agentes.models;

public class ProveedorGasto {
    private String proveedor;
    private int cantPosiciones;
    private double gasto;

    public ProveedorGasto() {
        this(null,0,0);
    }

    public ProveedorGasto(String proveedor, int cantPosiciones, double gasto) {
        this.proveedor = proveedor;
        this.cantPosiciones = cantPosiciones;
        this.gasto = gasto;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public int getCantPosiciones() {
        return cantPosiciones;
    }

    public void setCantPosiciones(int cantPosiciones) {
        this.cantPosiciones = cantPosiciones;
    }

    public double getGasto() {
        return gasto;
    }

    public void setGasto(double gasto) {
        this.gasto = gasto;
    }
}
