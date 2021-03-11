package models;

/**
 * Unidades organizativas
 *
 * @author humberto
 */
public class UnidadOrganizativa {

    private Integer id_unidad_organizativa;
    private String unidad_organizativa;

    public UnidadOrganizativa() {
        this(null);
    }

    public UnidadOrganizativa(String unidad_organizativa) {
        this.unidad_organizativa = unidad_organizativa;
    }

    public int getId_unidad_organizativa() {
        return id_unidad_organizativa;
    }

    public void setId_unidad_organizativa(int id_unidad_organizativa) {
        this.id_unidad_organizativa = id_unidad_organizativa;
    }

    public String getUnidad_organizativa() {
        return unidad_organizativa;
    }

    public void setUnidad_organizativa(String unidad_organizativa) {
        this.unidad_organizativa = unidad_organizativa;
    }
}
