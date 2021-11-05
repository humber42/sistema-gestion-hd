package models;

import java.sql.Date;


/**
 * table Hechos
 *
 * @author humberto
 */
public class Hechos {


    /**
     * Fiscalia
     */
    private boolean esclarecido;
    /**
     * Primary key
     */
    private int id_reg;
    /**
     * Data
     */
    private String titulo;
    private Date fecha_ocurrencia;
    private Date fecha_parte;
    private String numero_parte;
    private String centro;
    private String lugar;
    private String numero_denuncia;
    private Double afectacion_usd;
    private Double afectacion_mn;
    private Double afectacion_servicio;
    private String observaciones;
    private String cod_cdnt;
    private boolean imputable;
    private boolean incidencias;
    private Integer cantidad;
    private boolean prevenido;
    private boolean expediente_inv_sac;
    private boolean sin_denuncia;
    private boolean articulo_83;
    private boolean articulo_82;
    private boolean medida_administrativa;
    private boolean menor_edad;
    private boolean expfp;
    private boolean pendiente_despacho;
    private boolean pendiente_juicio;
    private boolean priv_lib;
    private int cantidad_sancionados;
    private String sentencia;
    /**
     * External keys
     */
    private UnidadOrganizativa unidadOrganizativa;
    private TipoHecho tipoHecho;
    private Municipio municipio;
    private TipoMateriales materiales;
    private TipoVandalismo tipoVandalismo;
    private AveriasPext averiasPext;

    /**
     * Default Constructor
     */
    public Hechos() {
        this(null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, false);
    }

    /**
     * Constructor for hecho Model
     *
     * @param titulo
     * @param tipoHecho
     * @param fecha_ocurrencia
     * @param fecha_parte
     * @param unidadOrganizativa
     * @param centro
     * @param lugar
     * @param municipio
     * @param numero_denuncia
     * @param afectacion_usd
     * @param afectacion_mn
     * @param afectacion_servicio
     * @param observaciones
     * @param cod_cdnt
     */
    public Hechos(String titulo, TipoHecho tipoHecho, Date fecha_ocurrencia, Date fecha_parte,
                  UnidadOrganizativa unidadOrganizativa, String centro, String lugar, Municipio municipio,
                  String numero_denuncia, Double afectacion_usd, Double afectacion_mn, Double afectacion_servicio,
                  String observaciones, String cod_cdnt, boolean prevenido) {
        this.titulo = titulo;
        this.tipoHecho = tipoHecho;
        this.fecha_ocurrencia = fecha_ocurrencia;
        this.fecha_parte = fecha_parte;
        this.unidadOrganizativa = unidadOrganizativa;
        this.centro = centro;
        this.lugar = lugar;
        this.municipio = municipio;
        this.numero_denuncia = numero_denuncia;
        this.afectacion_usd = afectacion_usd;
        this.afectacion_mn = afectacion_mn;
        this.afectacion_servicio = afectacion_servicio;
        this.observaciones = observaciones;
        this.cod_cdnt = cod_cdnt;
        this.prevenido = prevenido;
    }

    public int getId_reg() {
        return id_reg;
    }

    public void setId_reg(int id_reg) {
        this.id_reg = id_reg;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFecha_ocurrencia() {
        return fecha_ocurrencia;
    }

    public void setFecha_ocurrencia(Date fecha_ocurrencia) {
        this.fecha_ocurrencia = fecha_ocurrencia;
    }

    public Date getFecha_parte() {
        return fecha_parte;
    }

    public void setFecha_parte(Date fecha_parte) {
        this.fecha_parte = fecha_parte;
    }

    public String getNumero_parte() {
        return numero_parte;
    }

    public void setNumero_parte(String numero_parte) {
        this.numero_parte = numero_parte;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getNumero_denuncia() {
        return numero_denuncia;
    }

    public void setNumero_denuncia(String numero_denuncia) {
        this.numero_denuncia = numero_denuncia;
    }

    public Double getAfectacion_usd() {
        return afectacion_usd;
    }

    public void setAfectacion_usd(Double afectacion_usd) {
        this.afectacion_usd = afectacion_usd;
    }

    public Double getAfectacion_mn() {
        return afectacion_mn;
    }

    public void setAfectacion_mn(Double afectacion_mn) {
        this.afectacion_mn = afectacion_mn;
    }

    public Double getAfectacion_servicio() {
        return afectacion_servicio;
    }

    public void setAfectacion_servicio(Double afectacion_servicio) {
        this.afectacion_servicio = afectacion_servicio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCod_cdnt() {
        return cod_cdnt;
    }

    public void setCod_cdnt(String cod_cdnt) {
        this.cod_cdnt = cod_cdnt;
    }

    public boolean isImputable() {
        return imputable;
    }

    public void setImputable(boolean imputable) {
        this.imputable = imputable;
    }

    public boolean isIncidencias() {
        return incidencias;
    }

    public void setIncidencias(boolean incidencias) {
        this.incidencias = incidencias;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isPrevenido() {
        return prevenido;
    }

    public void setPrevenido(boolean prevenido) {
        this.prevenido = prevenido;
    }

    public boolean isEsclarecido() {
        return esclarecido;
    }

    public void setEsclarecido(boolean esclarecido) {
        this.esclarecido = esclarecido;
    }

    public boolean isExpediente_inv_sac() {
        return expediente_inv_sac;
    }

    public void setExpediente_inv_sac(boolean expediente_inv_sac) {
        this.expediente_inv_sac = expediente_inv_sac;
    }

    public boolean isSin_denuncia() {
        return sin_denuncia;
    }

    public void setSin_denuncia(boolean sin_denuncia) {
        this.sin_denuncia = sin_denuncia;
    }

    public boolean isArticulo_83() {
        return articulo_83;
    }

    public void setArticulo_83(boolean articulo_83) {
        this.articulo_83 = articulo_83;
    }

    public boolean isArticulo_82() {
        return articulo_82;
    }

    public void setArticulo_82(boolean articulo_82) {
        this.articulo_82 = articulo_82;
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

    public int getCantidad_sancionados() {
        return cantidad_sancionados;
    }

    public void setCantidad_sancionados(int cantidad_sancionados) {
        this.cantidad_sancionados = cantidad_sancionados;
    }

    public String getSentencia() {
        return sentencia;
    }

    public void setSentencia(String sentencia) {
        this.sentencia = sentencia;
    }

    public UnidadOrganizativa getUnidadOrganizativa() {
        return unidadOrganizativa;
    }

    public void setUnidadOrganizativa(UnidadOrganizativa unidadOrganizativa) {
        this.unidadOrganizativa = unidadOrganizativa;
    }

    public TipoHecho getTipoHecho() {
        return tipoHecho;
    }

    public void setTipoHecho(TipoHecho tipoHecho) {
        this.tipoHecho = tipoHecho;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public TipoMateriales getMateriales() {
        return materiales;
    }

    public void setMateriales(TipoMateriales materiales) {
        this.materiales = materiales;
    }

    public TipoVandalismo getTipoVandalismo() {
        return tipoVandalismo;
    }

    public void setTipoVandalismo(TipoVandalismo tipoVandalismo) {
        this.tipoVandalismo = tipoVandalismo;
    }

    public AveriasPext getAveriasPext() {
        return averiasPext;
    }

    public void setAveriasPext(AveriasPext averiasPext) {
        this.averiasPext = averiasPext;
    }

    public String getSintesis(){
        String sintesis= getTitulo()+"."+ getLugar();
        return sintesis;
    }
}
