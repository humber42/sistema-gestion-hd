package models.resumen_esclarecimiento;

import java.sql.Date;

public class HechosEsclarecimientoPExtTPub {
    private String unidad_organizativa;
    private Date fecha_ocurrencia;
    private String sintesis;
    private String municipio;
    private double afectacion_usd;
    private double afectacion_servicio;
    private String material_afectado_o_vandalismo;
    private String num_denuncia;
    private boolean exp_sac;
    private boolean sin_denuncia;
    private boolean articulo83;
    private boolean articulo82;
    private boolean medida_administrativa;
    private boolean menor_edad;
    private boolean expfp;
    private boolean pendiente_despacho;
    private boolean pendiente_juicio;
    private boolean priv_lib;
    private int cant_sancionados;
    private String sentencia;

    public HechosEsclarecimientoPExtTPub(){};

    public HechosEsclarecimientoPExtTPub(String unidad_organizativa, Date fecha_ocurrencia, String sintesis,
                                         String municipio, double afectacion_usd, double afectacion_servicio,
                                         String material_afectado_o_vandalismo, String num_denuncia, boolean exp_sac,
                                         boolean sin_denuncia, boolean articulo83, boolean articulo82,
                                         boolean medida_administrativa, boolean menor_edad, boolean expfp,
                                         boolean pendiente_despacho, boolean pendiente_juicio, boolean priv_lib,
                                         int cant_sancionados, String sentencia) {
        this.unidad_organizativa = unidad_organizativa;
        this.fecha_ocurrencia = fecha_ocurrencia;
        this.sintesis = sintesis;
        this.municipio = municipio;
        this.afectacion_usd = afectacion_usd;
        this.afectacion_servicio = afectacion_servicio;
        this.material_afectado_o_vandalismo = material_afectado_o_vandalismo;
        this.num_denuncia = num_denuncia;
        this.exp_sac = exp_sac;
        this.sin_denuncia = sin_denuncia;
        this.articulo83 = articulo83;
        this.articulo82 = articulo82;
        this.medida_administrativa = medida_administrativa;
        this.menor_edad = menor_edad;
        this.expfp = expfp;
        this.pendiente_despacho = pendiente_despacho;
        this.pendiente_juicio = pendiente_juicio;
        this.priv_lib = priv_lib;
        this.cant_sancionados = cant_sancionados;
        this.sentencia = sentencia;
    }

    public String getUnidad_organizativa() {
        return unidad_organizativa;
    }

    public void setUnidad_organizativa(String unidad_organizativa) {
        this.unidad_organizativa = unidad_organizativa;
    }

    public Date getFecha_ocurrencia() {
        return fecha_ocurrencia;
    }

    public void setFecha_ocurrencia(Date fecha_ocurrencia) {
        this.fecha_ocurrencia = fecha_ocurrencia;
    }

    public String getSintesis() {
        return sintesis;
    }

    public void setSintesis(String sintesis) {
        this.sintesis = sintesis;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public double getAfectacion_usd() {
        return afectacion_usd;
    }

    public void setAfectacion_usd(double afectacion_usd) {
        this.afectacion_usd = afectacion_usd;
    }

    public double getAfectacion_servicio() {
        return afectacion_servicio;
    }

    public void setAfectacion_servicio(double afectacion_servicio) {
        this.afectacion_servicio = afectacion_servicio;
    }

    public String getMaterial_afectado_o_vandalismo() {
        return material_afectado_o_vandalismo;
    }

    public void setMaterial_afectado_o_vandalismo(String material_afectado_o_vandalismo) {
        this.material_afectado_o_vandalismo = material_afectado_o_vandalismo;
    }

    public String getNum_denuncia() {
        return num_denuncia;
    }

    public void setNum_denuncia(String num_denuncia) {
        this.num_denuncia = num_denuncia;
    }

    public boolean isExp_sac() {
        return exp_sac;
    }

    public void setExp_sac(boolean exp_sac) {
        this.exp_sac = exp_sac;
    }

    public boolean isSin_denuncia() {
        return sin_denuncia;
    }

    public void setSin_denuncia(boolean sin_denuncia) {
        this.sin_denuncia = sin_denuncia;
    }

    public boolean isArticulo83() {
        return articulo83;
    }

    public void setArticulo83(boolean articulo83) {
        this.articulo83 = articulo83;
    }

    public boolean isArticulo82() {
        return articulo82;
    }

    public void setArticulo82(boolean articulo82) {
        this.articulo82 = articulo82;
    }

    public boolean isMedida_administrativa() {
        return medida_administrativa;
    }

    public void setMedida_administrativa(boolean medida_administrativa) {
        this.medida_administrativa = medida_administrativa;
    }

    public boolean isMenor_edad() {
        return menor_edad;
    }

    public void setMenor_edad(boolean menor_edad) {
        this.menor_edad = menor_edad;
    }

    public boolean isExpfp() {
        return expfp;
    }

    public void setExpfp(boolean expfp) {
        this.expfp = expfp;
    }

    public boolean isPendiente_despacho() {
        return pendiente_despacho;
    }

    public void setPendiente_despacho(boolean pendiente_despacho) {
        this.pendiente_despacho = pendiente_despacho;
    }

    public boolean isPendiente_juicio() {
        return pendiente_juicio;
    }

    public void setPendiente_juicio(boolean pendiente_juicio) {
        this.pendiente_juicio = pendiente_juicio;
    }

    public boolean isPriv_lib() {
        return priv_lib;
    }

    public void setPriv_lib(boolean priv_lib) {
        this.priv_lib = priv_lib;
    }

    public int getCant_sancionados() {
        return cant_sancionados;
    }

    public void setCant_sancionados(int cant_sancionados) {
        this.cant_sancionados = cant_sancionados;
    }

    public String getSentencia() {
        return sentencia;
    }

    public void setSentencia(String sentencia) {
        this.sentencia = sentencia;
    }
}
