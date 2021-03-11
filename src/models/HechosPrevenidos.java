package models;

public class HechosPrevenidos {
    private String unidadOrganizativa;
    private int cantHechosDelictivos;
    private int cantPext;
    private int cantTpub;
    private int cantRobos;
    private int cantHurtos;
    private int cantFraudes;
    private int cantAccionesCR;
    private int cantOtros;

    public HechosPrevenidos(String unidadOrganizativa, int cantHechosDelictivos, int cantPext, int cantTpub, int cantRobos, int cantHurtos, int cantFraudes, int cantAccionesCR, int cantOtros) {
        this.unidadOrganizativa = unidadOrganizativa;
        this.cantHechosDelictivos = cantHechosDelictivos;
        this.cantPext = cantPext;
        this.cantTpub = cantTpub;
        this.cantRobos = cantRobos;
        this.cantHurtos = cantHurtos;
        this.cantFraudes = cantFraudes;
        this.cantAccionesCR = cantAccionesCR;
        this.cantOtros = cantOtros;
    }

    public HechosPrevenidos() {
        this(null, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public String getUnidadOrganizativa() {
        return unidadOrganizativa;
    }

    public void setUnidadOrganizativa(String unidadOrganizativa) {
        this.unidadOrganizativa = unidadOrganizativa;
    }

    public int getCantHechosDelictivos() {
        return cantHechosDelictivos;
    }

    public void setCantHechosDelictivos(int cantHechosDelictivos) {
        this.cantHechosDelictivos = cantHechosDelictivos;
    }

    public int getCantPext() {
        return cantPext;
    }

    public void setCantPext(int cantPext) {
        this.cantPext = cantPext;
    }

    public int getCantTpub() {
        return cantTpub;
    }

    public void setCantTpub(int cantTpub) {
        this.cantTpub = cantTpub;
    }

    public int getCantRobos() {
        return cantRobos;
    }

    public void setCantRobos(int cantRobos) {
        this.cantRobos = cantRobos;
    }

    public int getCantHurtos() {
        return cantHurtos;
    }

    public void setCantHurtos(int cantHurtos) {
        this.cantHurtos = cantHurtos;
    }

    public int getCantFraudes() {
        return cantFraudes;
    }

    public void setCantFraudes(int cantFraudes) {
        this.cantFraudes = cantFraudes;
    }

    public int getCantAccionesCR() {
        return cantAccionesCR;
    }

    public void setCantAccionesCR(int cantAccionesCR) {
        this.cantAccionesCR = cantAccionesCR;
    }

    public int getCantOtros() {
        return cantOtros;
    }

    public void setCantOtros(int cantOtros) {
        this.cantOtros = cantOtros;
    }
}
