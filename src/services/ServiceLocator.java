package services;


import sistema_identificativo.services.*;
import sistema_identificativo.services.impl.*;

import sistema_identificativo.services.CodigoPaseService;
import sistema_identificativo.services.RegistroImpresionesService;
import sistema_identificativo.services.RegistroPaseService;
import sistema_identificativo.services.TipoPaseService;
import sistema_identificativo.services.impl.CodigoPaseServiceImpl;
import sistema_identificativo.services.impl.RegistroImpresionesServiceImpl;
import sistema_identificativo.services.impl.RegistroPaseServiceImpl;
import sistema_identificativo.services.impl.TipoPaseServiceImpl;


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
    private static CodigoPaseService codigoPaseService = null;
    private static RegistroImpresionesService registroImpresionesService = null;
    private static RegistroPaseService registroPaseService = null;
    private static TipoPaseService tipoPaseService = null;

    private static ImpresionService impresionService = null;
    private static JasperReportService jasperReportService = null;


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

    public static CodigoPaseService getCodigoPaseService(){
        if(codigoPaseService == null)
            codigoPaseService = new CodigoPaseServiceImpl();
        return codigoPaseService;
    }


    public static JasperReportService getJasperReportService(){
        if(jasperReportService == null)
            jasperReportService = new JasperReportServiceImpl();
        return jasperReportService;
    }
    public static ImpresionService getImpresionService(){
        if(impresionService == null)
            impresionService = new ImpresionServiceImpl();
        return impresionService;
    }

    public static RegistroImpresionesService getRegistroImpresionesService() {
        if (registroImpresionesService == null)
            registroImpresionesService = new RegistroImpresionesServiceImpl();
        return registroImpresionesService;
    }

    public static RegistroPaseService getRegistroPaseService() {
        if (registroPaseService == null)
            registroPaseService = new RegistroPaseServiceImpl();
        return registroPaseService;
    }

    public static TipoPaseService getTipoPaseService() {
        if (tipoPaseService == null)
            tipoPaseService = new TipoPaseServiceImpl();
        return tipoPaseService;
    }

}
