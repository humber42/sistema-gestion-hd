package models;

import java.sql.Date;

public class HechosBusqueda {
    private int idReg;
    private int idUnidadOrganizativa;
    private Date fechaOcurrencia;
    private String codCDNT;
    private String descripcion;

    public HechosBusqueda() {
    }

    public HechosBusqueda(int idReg, int idUnidadOrganizativa, Date fechaOcurrencia, String codCDNT, String descripcion) {
        this.idReg = idReg;
        this.idUnidadOrganizativa = idUnidadOrganizativa;
        this.fechaOcurrencia = fechaOcurrencia;
        this.codCDNT = codCDNT;
        this.descripcion = descripcion;
    }

    public int getIdReg() {
        return idReg;
    }

    public void setIdReg(int idReg) {
        this.idReg = idReg;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdUnidadOrganizativa() {
        return idUnidadOrganizativa;
    }

    public void setIdUnidadOrganizativa(int idUnidadOrganizativa) {
        this.idUnidadOrganizativa = idUnidadOrganizativa;
    }

    public Date getFechaOcurrencia() {
        return fechaOcurrencia;
    }

    public void setFechaOcurrencia(Date fechaOcurrencia) {
        this.fechaOcurrencia = fechaOcurrencia;
    }

    public String getCodCDNT() {
        return codCDNT;
    }

    public void setCodCDNT(String codCDNT) {
        this.codCDNT = codCDNT;
    }
}
