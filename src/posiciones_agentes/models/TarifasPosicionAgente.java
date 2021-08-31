package posiciones_agentes.models;


import models.UnidadOrganizativa;

/**
 * @model TarifasPosicionAgente
 * @author Humberto Cabrera Dominguez
 */

public class TarifasPosicionAgente {
    private int idTarifa;
    private UnidadOrganizativa unidadOrganizativa;
    private ProveedorServicio proveedorServicio;
    private double tarifa;

    public TarifasPosicionAgente(int idTarifa, UnidadOrganizativa unidadOrganizativa, ProveedorServicio proveedorServicio, double tarifa) {
        this.idTarifa = idTarifa;
        this.unidadOrganizativa = unidadOrganizativa;
        this.proveedorServicio = proveedorServicio;
        this.tarifa = tarifa;
    }

    public TarifasPosicionAgente() {
    }

    public int getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(int idTarifa) {
        this.idTarifa = idTarifa;
    }

    public UnidadOrganizativa getUnidadOrganizativa() {
        return unidadOrganizativa;
    }

    public void setUnidadOrganizativa(UnidadOrganizativa unidadOrganizativa) {
        this.unidadOrganizativa = unidadOrganizativa;
    }

    public ProveedorServicio getProveedorServicio() {
        return proveedorServicio;
    }

    public void setProveedorServicio(ProveedorServicio proveedorServicio) {
        this.proveedorServicio = proveedorServicio;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }
}
