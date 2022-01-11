package posiciones_agentes.models;

public class PosicionAgente {
    private int idReg;
    private String instalacion;
    private String unidadOrganizativa;
    private String proveedor;
    private int cantEfectivos;
    private int horasDL;
    private int horasDNL;

    public PosicionAgente(int idReg, String instalacion, String unidadOrganizativa, String proveedor, int cantEfectivos, int horasDL, int horasDNL) {
        this.idReg = idReg;
        this.instalacion = instalacion;
        this.unidadOrganizativa = unidadOrganizativa;
        this.proveedor = proveedor;
        this.cantEfectivos = cantEfectivos;
        this.horasDL = horasDL;
        this.horasDNL = horasDNL;
    }

    public int getIdReg() {
        return idReg;
    }

    public void setIdReg(int idReg) {
        this.idReg = idReg;
    }

    public String getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(String instalacion) {
        this.instalacion = instalacion;
    }

    public String getUnidadOrganizativa() {
        return unidadOrganizativa;
    }

    public void setUnidadOrganizativa(String unidadOrganizativa) {
        this.unidadOrganizativa = unidadOrganizativa;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public int getCantEfectivos() {
        return cantEfectivos;
    }

    public void setCantEfectivos(int cantEfectivos) {
        this.cantEfectivos = cantEfectivos;
    }

    public int getHorasDL() {
        return horasDL;
    }

    public void setHorasDL(int horasDL) {
        this.horasDL = horasDL;
    }

    public int getHorasDNL() {
        return horasDNL;
    }

    public void setHorasDNL(int horasDNL) {
        this.horasDNL = horasDNL;
    }
}
