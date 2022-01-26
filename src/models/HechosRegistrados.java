package models;

import java.sql.Date;

public class HechosRegistrados {
    private int id_reg;
    private String codCDNT;
    private String uo;
    private String tipoHecho;
    private Date fechaOcurre;
    private String titulo;
    private String municipio;
    private String numeroDenuncia;
    private double perdidasMn;

    public HechosRegistrados(int id_reg, String codCDNT, String uo,
                             String tipoHecho, Date fechaOcurre, String titulo,
                             String municipio, String numeroDenuncia, double perdidasMn) {
        this.id_reg = id_reg;
        this.codCDNT = codCDNT;
        this.uo = uo;
        this.tipoHecho = tipoHecho;
        this.fechaOcurre = fechaOcurre;
        this.titulo = titulo;
        this.municipio = municipio;
        this.numeroDenuncia = numeroDenuncia;
        this.perdidasMn = perdidasMn;
    }

    public int getId_reg() {
        return id_reg;
    }

    public void setId_reg(int id_reg) {
        this.id_reg = id_reg;
    }

    public String getCodCDNT() {
        return codCDNT;
    }

    public void setCodCDNT(String codCDNT) {
        this.codCDNT = codCDNT;
    }

    public String getUo() {
        return uo;
    }

    public void setUo(String uo) {
        this.uo = uo;
    }

    public String getTipoHecho() {
        return tipoHecho;
    }

    public void setTipoHecho(String tipoHecho) {
        this.tipoHecho = tipoHecho;
    }

    public Date getFechaOcurre() {
        return fechaOcurre;
    }

    public void setFechaOcurre(Date fechaOcurre) {
        this.fechaOcurre = fechaOcurre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getNumeroDenuncia() {
        return numeroDenuncia;
    }

    public void setNumeroDenuncia(String numeroDenuncia) {
        this.numeroDenuncia = numeroDenuncia;
    }

    public double getPerdidasMn() {
        return perdidasMn;
    }

    public void setPerdidasMn(double perdidasMn) {
        this.perdidasMn = perdidasMn;
    }
}
