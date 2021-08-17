package sistema_identificativo.models;

import models.UnidadOrganizativa;

import java.sql.Date;

public class RegistroPase {
    private int idReg;
    private TipoPase tipoPase;
    private CodigoPase codigoPase;
    private String numeroPase;
    private String numeroIdentidad;
    private String nombre;
    private UnidadOrganizativa unidadOrganizativa;
    private String acceso;
    private Date fechaValidez;
    private int baja;
    private String observaciones;
    private String imageUrl;

    public RegistroPase() {
    }

    public RegistroPase(int idReg, TipoPase tipoPase, CodigoPase codigoPase,
                        String numeroPase, String numeroIdentidad,
                        String nombre, UnidadOrganizativa unidadOrganizativa,
                        String acceso, Date fechaValidez, int baja,
                        String observaciones, String imageUrl) {
        this.idReg = idReg;
        this.tipoPase = tipoPase;
        this.codigoPase = codigoPase;
        this.numeroPase = numeroPase;
        this.numeroIdentidad = numeroIdentidad;
        this.nombre = nombre;
        this.unidadOrganizativa = unidadOrganizativa;
        this.acceso = acceso;
        this.fechaValidez = fechaValidez;
        this.baja = baja;
        this.observaciones = observaciones;
        this.imageUrl = imageUrl;
    }

    public int getIdReg() {
        return idReg;
    }

    public void setIdReg(int idReg) {
        this.idReg = idReg;
    }

    public TipoPase getTipoPase() {
        return tipoPase;
    }

    public void setTipoPase(TipoPase tipoPase) {
        this.tipoPase = tipoPase;
    }

    public CodigoPase getCodigoPase() {
        return codigoPase;
    }

    public void setCodigoPase(CodigoPase codigoPase) {
        this.codigoPase = codigoPase;
    }

    public String getNumeroPase() {
        return numeroPase;
    }

    public void setNumeroPase(String numeroPase) {
        this.numeroPase = numeroPase;
    }

    public String getNumeroIdentidad() {
        return numeroIdentidad;
    }

    public void setNumeroIdentidad(String numeroIdentidad) {
        this.numeroIdentidad = numeroIdentidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public UnidadOrganizativa getUnidadOrganizativa() {
        return unidadOrganizativa;
    }

    public void setUnidadOrganizativa(UnidadOrganizativa unidadOrganizativa) {
        this.unidadOrganizativa = unidadOrganizativa;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public Date getFechaValidez() {
        return fechaValidez;
    }

    public void setFechaValidez(Date fechaValidez) {
        this.fechaValidez = fechaValidez;
    }

    public int getBaja() {
        return baja;
    }

    public void setBaja(int baja) {
        this.baja = baja;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
