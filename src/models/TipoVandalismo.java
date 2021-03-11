package models;


/***
 * Afectacion Vs telefonia publica
 * @author humberto
 */

public class TipoVandalismo {

    private Integer id_afect_tpublica;
    private String afect_tpublica;


    public TipoVandalismo() {
        this(null);
    }

    public TipoVandalismo(String afect_tpublica) {
        this.afect_tpublica = afect_tpublica;
    }

    public int getId_afect_tpublica() {
        return id_afect_tpublica;
    }

    public void setId_afect_tpublica(int id_afect_tpublica) {
        this.id_afect_tpublica = id_afect_tpublica;
    }

    public String getAfect_tpublica() {
        return afect_tpublica;
    }

    public void setAfect_tpublica(String afect_tpublica) {
        this.afect_tpublica = afect_tpublica;
    }
}
