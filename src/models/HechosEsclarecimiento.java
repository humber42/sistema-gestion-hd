package models;

import java.sql.Date;

public class HechosEsclarecimiento {
    private int idReg;
    private String codCDNT;
    private Date fechaOcurrencia;
    private String tipoHecho;
    private Double afectacionMN;
    private Double serviciosAfect;
    private String unidadOrganizativa;
    private String municipio;
    private String numDenuncia;
    private Double afectacionMLC;
    private String afectacion;
    private String titulo;

    public HechosEsclarecimiento() {
    }

    public HechosEsclarecimiento(int idReg, String codCDNT, Date fechaOcurrencia, String tipoHecho, Double afectacionMN, Double serviciosAfect, String unidadOrganizativa, String municipio, String numDenuncia, Double afectacionMLC, String afectacion, String titulo) {
        this.idReg = idReg;
        this.codCDNT = codCDNT;
        this.fechaOcurrencia = fechaOcurrencia;
        this.tipoHecho = tipoHecho;
        this.afectacionMN = afectacionMN;
        this.serviciosAfect = serviciosAfect;
        this.unidadOrganizativa = unidadOrganizativa;
        this.municipio = municipio;
        this.numDenuncia = numDenuncia;
        this.afectacionMLC = afectacionMLC;
        this.afectacion = afectacion;
        this.titulo = titulo;
    }

    public int getIdReg() {
        return idReg;
    }

    public void setIdReg(int idReg) {
        this.idReg = idReg;
    }

    public String getCodCDNT() {
        return codCDNT;
    }

    public void setCodCDNT(String codCDNT) {
        this.codCDNT = codCDNT;
    }

    public Date getFechaOcurrencia() {
        return fechaOcurrencia;
    }

    public void setFechaOcurrencia(Date fechaOcurrencia) {
        this.fechaOcurrencia = fechaOcurrencia;
    }

    public String getTipoHecho() {
        return tipoHecho;
    }

    public void setTipoHecho(String tipoHecho) {
        this.tipoHecho = tipoHecho;
    }

    public Double getAfectacionMN() {
        return afectacionMN;
    }

    public void setAfectacionMN(Double afectacionMN) {
        this.afectacionMN = afectacionMN;
    }

    public Double getServiciosAfect() {
        return serviciosAfect;
    }

    public void setServiciosAfect(Double serviciosAfect) {
        this.serviciosAfect = serviciosAfect;
    }

    public String getUnidadOrganizativa() {
        return unidadOrganizativa;
    }

    public void setUnidadOrganizativa(String unidadOrganizativa) {
        this.unidadOrganizativa = unidadOrganizativa;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getNumDenuncia() {
        return numDenuncia;
    }

    public void setNumDenuncia(String numDenuncia) {
        this.numDenuncia = numDenuncia;
    }

    public Double getAfectacionMLC() {
        return afectacionMLC;
    }

    public void setAfectacionMLC(Double afectacionMLC) {
        this.afectacionMLC = afectacionMLC;
    }

    public String getAfectacion() {
        return afectacion;
    }

    public void setAfectacion(String afectacion) {
        this.afectacion = afectacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
