package services;

public class ServiceLocator {

    private static AnnoServiceImpl annoServicio = null;
    private static AveriasPExtService averiasPExtService = null;
    private static HechosServiceImpl hechosService = null;
    private static MunicipiosService municipiosService = null;
    private static PCC_territorioService pcc_territorioService = null;
    private static TipoHechoServiceImpl tipoHechoService = null;
    private static TipoMaterialesServiceImpl tipoMaterialesService = null;
    private static TipoVandalismoService tipoVandalismoService = null;
    private static UnidadOrganizativaService unidadOrganizativaService = null;

    public static UnidadOrganizativaService getUnidadOrganizativaService() {
        if (unidadOrganizativaService == null) {
            unidadOrganizativaService = new UnidadOrganizativaService();
        }
        return unidadOrganizativaService;
    }

    public static TipoVandalismoService getTipoVandalismoService() {
        if (tipoVandalismoService == null) {
            tipoVandalismoService = new TipoVandalismoService();
        }
        return tipoVandalismoService;
    }

    public static TipoMaterialesServiceImpl getTipoMaterialesService() {
        if (tipoMaterialesService == null)
            tipoMaterialesService = new TipoMaterialesServiceImpl();
        return tipoMaterialesService;
    }

    public static PCC_territorioService pcc_territorioService() {
        if (pcc_territorioService == null) {
            pcc_territorioService = new PCC_territorioService();
        }
        return pcc_territorioService;
    }


    public static MunicipiosService getMunicipiosService() {
        if (municipiosService == null) {
            municipiosService = new MunicipiosService();
        }
        return municipiosService;
    }

    public static HechosServiceImpl getHechosService() {
        if (hechosService == null) {
            hechosService = new HechosServiceImpl();
        }
        return hechosService;
    }

    public static TipoHechoServiceImpl getTipoHechoService() {
        if (tipoHechoService == null)
            tipoHechoService = new TipoHechoServiceImpl();
        return tipoHechoService;
    }

    public static AnnoServiceImpl getAnnoServicio() {
        if (annoServicio == null) {
            annoServicio = new AnnoServiceImpl();
        }
        return annoServicio;
    }

    public static AveriasPExtService getAveriasPExtService() {
        if (averiasPExtService == null)
            averiasPExtService = new AveriasPExtService();
        return averiasPExtService;
    }
}
