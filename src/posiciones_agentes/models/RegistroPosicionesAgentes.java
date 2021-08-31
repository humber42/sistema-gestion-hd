package posiciones_agentes.models;

import models.UnidadOrganizativa;

/**
 * @model RegistroPosicionesAgentes
 * @author Humberto Cabrera Dominguez
 */
public class RegistroPosicionesAgentes {

    private int idReg;
    private String instalacion;
    private UnidadOrganizativa unidadOrganizativa;
    private ProveedorServicio proveedorServicio;
    private int horasDiasNoLaborables;
    private int horasDiasLaborables;
    private int cantidadEfectivos;


    public RegistroPosicionesAgentes(int idReg, String instalacion, UnidadOrganizativa unidadOrganizativa, ProveedorServicio proveedorServicio, int horasDiasNoLaborables, int horasDiasLaborables, int cantidadEfectivos) {
        this.idReg = idReg;
        this.instalacion = instalacion;
        this.unidadOrganizativa = unidadOrganizativa;
        this.proveedorServicio = proveedorServicio;
        this.horasDiasNoLaborables = horasDiasNoLaborables;
        this.horasDiasLaborables = horasDiasLaborables;
        this.cantidadEfectivos = cantidadEfectivos;
    }

    public RegistroPosicionesAgentes() {
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

    public int getHorasDiasNoLaborables() {
        return horasDiasNoLaborables;
    }

    public void setHorasDiasNoLaborables(int horasDiasNoLaborables) {
        this.horasDiasNoLaborables = horasDiasNoLaborables;
    }

    public int getHorasDiasLaborables() {
        return horasDiasLaborables;
    }

    public void setHorasDiasLaborables(int horasDiasLaborables) {
        this.horasDiasLaborables = horasDiasLaborables;
    }

    public int getCantidadEfectivos() {
        return cantidadEfectivos;
    }

    public void setCantidadEfectivos(int cantidadEfectivos) {
        this.cantidadEfectivos = cantidadEfectivos;
    }
}
