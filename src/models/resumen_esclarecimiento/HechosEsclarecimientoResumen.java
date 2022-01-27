package models.resumen_esclarecimiento;

public class HechosEsclarecimientoResumen {
    private String unidad_organizativa;
    private int total_conciliados;
    private int cant_pext;
    private int cant_tpub;
    private int total_no_esclarecidos;
    private int cant_pext_no_esc;
    private int cant_tpub_no_esc;
    private int cant_exp_sac;
    private int cant_sin_denuncia;
    private int total_esclarecidos;
    private int cant_pext_esc;
    private int cant_tpub_esc;
    private int cant_art_83;
    private int cant_art_82;
    private int cant_med_admin;
    private int cant_menor;
    private int cant_fase_prep;
    private int cant_pend_desp;
    private int cant_pend_juicio;
    private int cant_casos;
    private int cant_sanciones;
    private int sentencias;

    public HechosEsclarecimientoResumen() {
        this.setTotal_conciliados(0);
        this.setCant_pext(0);
        this.setCant_tpub(0);
        this.setTotal_no_esclarecidos(0);
        this.setCant_pext_no_esc(0);
        this.setCant_tpub_no_esc(0);
        this.setCant_exp_sac(0);
        this.setCant_sin_denuncia(0);
        this.setTotal_esclarecidos(0);
        this.setCant_pext_esc(0);
        this.setCant_tpub_esc(0);
        this.setCant_art_82(0);
        this.setCant_art_83(0);
        this.setCant_med_admin(0);
        this.setCant_menor(0);
        this.setCant_fase_prep(0);
        this.setCant_pend_desp(0);
        this.setCant_pend_juicio(0);
        this.setCant_casos(0);
        this.setCant_sanciones(0);
        this.setSentencias(0);
    }

    public HechosEsclarecimientoResumen(String unidad_organizativa, int total_conciliados, int cant_pext, int cant_tpub, int total_no_esclarecidos, int cant_pext_no_esc, int cant_tpub_no_esc, int cant_exp_sac, int cant_sin_denuncia, int total_esclarecidos, int cant_pext_esc, int cant_tpub_esc, int cant_art_83, int cant_art_82, int cant_med_admin, int cant_menor, int cant_fase_prep, int cant_pend_desp, int cant_pend_juicio, int cant_casos, int cant_sanciones, int sentencias) {
        this.unidad_organizativa = unidad_organizativa;
        this.total_conciliados = total_conciliados;
        this.cant_pext = cant_pext;
        this.cant_tpub = cant_tpub;
        this.total_no_esclarecidos = total_no_esclarecidos;
        this.cant_pext_no_esc = cant_pext_no_esc;
        this.cant_tpub_no_esc = cant_tpub_no_esc;
        this.cant_exp_sac = cant_exp_sac;
        this.cant_sin_denuncia = cant_sin_denuncia;
        this.total_esclarecidos = total_esclarecidos;
        this.cant_pext_esc = cant_pext_esc;
        this.cant_tpub_esc = cant_tpub_esc;
        this.cant_art_83 = cant_art_83;
        this.cant_art_82 = cant_art_82;
        this.cant_med_admin = cant_med_admin;
        this.cant_menor = cant_menor;
        this.cant_fase_prep = cant_fase_prep;
        this.cant_pend_desp = cant_pend_desp;
        this.cant_pend_juicio = cant_pend_juicio;
        this.cant_casos = cant_casos;
        this.cant_sanciones = cant_sanciones;
        this.sentencias = sentencias;
    }

    public String getUnidad_organizativa() {
        return unidad_organizativa;
    }

    public void setUnidad_organizativa(String unidad_organizativa) {
        this.unidad_organizativa = unidad_organizativa;
    }

    public int getTotal_conciliados() {
        return total_conciliados;
    }

    public void setTotal_conciliados(int total_conciliados) {
        this.total_conciliados = total_conciliados;
    }

    public int getCant_pext() {
        return cant_pext;
    }

    public void setCant_pext(int cant_pext) {
        this.cant_pext = cant_pext;
    }

    public int getCant_tpub() {
        return cant_tpub;
    }

    public void setCant_tpub(int cant_tpub) {
        this.cant_tpub = cant_tpub;
    }

    public int getTotal_no_esclarecidos() {
        return total_no_esclarecidos;
    }

    public void setTotal_no_esclarecidos(int total_no_esclarecidos) {
        this.total_no_esclarecidos = total_no_esclarecidos;
    }

    public int getCant_pext_no_esc() {
        return cant_pext_no_esc;
    }

    public void setCant_pext_no_esc(int cant_pext_no_esc) {
        this.cant_pext_no_esc = cant_pext_no_esc;
    }

    public int getCant_tpub_no_esc() {
        return cant_tpub_no_esc;
    }

    public void setCant_tpub_no_esc(int cant_tpub_no_esc) {
        this.cant_tpub_no_esc = cant_tpub_no_esc;
    }

    public int getCant_exp_sac() {
        return cant_exp_sac;
    }

    public void setCant_exp_sac(int cant_exp_sac) {
        this.cant_exp_sac = cant_exp_sac;
    }

    public int getCant_sin_denuncia() {
        return cant_sin_denuncia;
    }

    public void setCant_sin_denuncia(int cant_sin_denuncia) {
        this.cant_sin_denuncia = cant_sin_denuncia;
    }

    public int getTotal_esclarecidos() {
        return total_esclarecidos;
    }

    public void setTotal_esclarecidos(int total_esclarecidos) {
        this.total_esclarecidos = total_esclarecidos;
    }

    public int getCant_pext_esc() {
        return cant_pext_esc;
    }

    public void setCant_pext_esc(int cant_pext_esc) {
        this.cant_pext_esc = cant_pext_esc;
    }

    public int getCant_tpub_esc() {
        return cant_tpub_esc;
    }

    public void setCant_tpub_esc(int cant_tpub_esc) {
        this.cant_tpub_esc = cant_tpub_esc;
    }

    public int getCant_art_83() {
        return cant_art_83;
    }

    public void setCant_art_83(int cant_art_83) {
        this.cant_art_83 = cant_art_83;
    }

    public int getCant_art_82() {
        return cant_art_82;
    }

    public void setCant_art_82(int cant_art_82) {
        this.cant_art_82 = cant_art_82;
    }

    public int getCant_med_admin() {
        return cant_med_admin;
    }

    public void setCant_med_admin(int cant_med_admin) {
        this.cant_med_admin = cant_med_admin;
    }

    public int getCant_menor() {
        return cant_menor;
    }

    public void setCant_menor(int cant_menor) {
        this.cant_menor = cant_menor;
    }

    public int getCant_fase_prep() {
        return cant_fase_prep;
    }

    public void setCant_fase_prep(int cant_fase_prep) {
        this.cant_fase_prep = cant_fase_prep;
    }

    public int getCant_pend_desp() {
        return cant_pend_desp;
    }

    public void setCant_pend_desp(int cant_pend_desp) {
        this.cant_pend_desp = cant_pend_desp;
    }

    public int getCant_pend_juicio() {
        return cant_pend_juicio;
    }

    public void setCant_pend_juicio(int cant_pend_juicio) {
        this.cant_pend_juicio = cant_pend_juicio;
    }

    public int getCant_casos() {
        return cant_casos;
    }

    public void setCant_casos(int cant_casos) {
        this.cant_casos = cant_casos;
    }

    public int getCant_sanciones() {
        return cant_sanciones;
    }

    public void setCant_sanciones(int cant_sanciones) {
        this.cant_sanciones = cant_sanciones;
    }

    public int getSentencias() {
        return sentencias;
    }

    public void setSentencias(int sentencias) {
        this.sentencias = sentencias;
    }

    @Override
    public String toString() {
        return "HechosEsclarecimientoResumen{" +
                "unidad_organizativa='" + unidad_organizativa + '\'' +
                ", total_conciliados=" + total_conciliados +
                ", cant_pext=" + cant_pext +
                ", cant_tpub=" + cant_tpub +
                ", total_no_esclarecidos=" + total_no_esclarecidos +
                ", cant_pext_no_esc=" + cant_pext_no_esc +
                ", cant_tpub_no_esc=" + cant_tpub_no_esc +
                ", cant_exp_sac=" + cant_exp_sac +
                ", cant_sin_denuncia=" + cant_sin_denuncia +
                ", total_esclarecidos=" + total_esclarecidos +
                ", cant_pext_esc=" + cant_pext_esc +
                ", cant_tpub_esc=" + cant_tpub_esc +
                ", cant_art_83=" + cant_art_83 +
                ", cant_art_82=" + cant_art_82 +
                ", cant_med_admin=" + cant_med_admin +
                ", cant_menor=" + cant_menor +
                ", cant_fase_prep=" + cant_fase_prep +
                ", cant_pend_desp=" + cant_pend_desp +
                ", cant_pend_juicio=" + cant_pend_juicio +
                ", cant_casos=" + cant_casos +
                ", cant_sanciones=" + cant_sanciones +
                ", sentencias=" + sentencias +
                '}';
    }
}
