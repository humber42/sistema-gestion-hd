package models;

public class HurtosRobosPrevUorg {

    private String uorg;
    private int cantRobos;
    private int cantHurtos;
    private int cantPrevenidos;

    public HurtosRobosPrevUorg(String uorg, int cantRobos, int cantHurtos, int cantPrevenidos) {
        this.uorg = uorg;
        this.cantRobos = cantRobos;
        this.cantHurtos = cantHurtos;
        this.cantPrevenidos = cantPrevenidos;
    }

    public HurtosRobosPrevUorg() {
        this(null, 0, 0, 0);
    }

    public String getUorg() {
        return uorg;
    }

    public void setUorg(String uorg) {
        this.uorg = uorg;
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

    public int getCantPrevenidos() {
        return cantPrevenidos;
    }

    public void setCantPrevenidos(int cantPrevenidos) {
        this.cantPrevenidos = cantPrevenidos;
    }
}
