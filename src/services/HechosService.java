package services;

import models.*;
import org.postgresql.util.PSQLException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public interface HechosService {

    Hechos getHecho(int id);

    LinkedList<Hechos> fetchAllHechos(String limit, String offset);

    void updateHecho(Hechos hechos) throws SQLException;

    void registrarHecho(Hechos hechos) throws SQLException;

    void registrarHecho(Hechos hechos, TipoVandalismo vandalismo) throws SQLException;

    void registrarHecho(Hechos hechos, TipoMateriales materiales, int cantidad) throws SQLException;

    void registrarHecho(Hechos hechos, boolean imputable, boolean incidente) throws SQLException;

    Hechos searchHechoByCODCDNT(String cdnt);

    Hechos getHechoByUOandFechaOcurrenciaAndTitulo(int id_uorg, Date fecha, String title);

    List<Hechos> fetchHechosPextTpub(String offset) throws PSQLException;

    List<Hechos> fetchBySubStringCodCDNT(String codCdnt, String offset);

    int countfetchBySubStringCodCDNT(String codCdnt);

    int countHechos();

    void esclarecimientoHechoPExtTpubl(int id, boolean sinDenuncia, boolean expSAC, boolean esclarecido,
                                       boolean articulo83, boolean articulo82, boolean medidasAdministrativas,
                                       boolean pendienteJuicio, boolean expFasePreparatoria, boolean menorEdad,
                                       boolean pendienteDespacho, boolean privacionLibertad, String cantSancionados,
                                       String sentencia);

    int countAllHechos();

    int countAllHechosByAnno(int anno);

    int cantHechosByTipoHechoAndAnno(int tipoHecho, int anno);

    int cantHechosPextCierreFiscalia(Date date, int tipoHecho);

    LinkedList<ResumenModels> cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre(Date date, int tipo);

    LinkedList<AfectacionFiscaliaModels> cantidadAfectacionesHechosFiscalia(Date date);

    LinkedList<HechosByAnno> cantidadHechosByAnno(int anno);

    LinkedList<EstacionesPublicas> getEstacionesPublicasCant();

    LinkedList<EsclarecimientoHechos> getEsclarecimientoConciliados(Date date);

    LinkedList<EsclarecimientoHechos> getEsclaremientoConciliados(Date date, boolean esclarecido);

    LinkedList<HechosPorMunicipio> obtenerHechosPorMunicipio(Date date, int tipoHecho);

    LinkedList<?> calculoServiciosAfectadosEstacionesPublicas(Date date);

    LinkedList<MunicipioServiciosAfectados> serviciosAfectadosPorMunicipio(Date date, int tipoHecho);

    LinkedList<Afectaciones> afectacionesTPubMunicipio(Date date);

    LinkedList<HechosMesAnno> hechosMesesAnno(Date date);

    LinkedList<HechosUOrgAnno> hechosUnidadOranizativaList(Date date);

    LinkedList<HechosUorgMesAnno> obtenerCantidadHechosUOrgPorMes(int anno, int tipoHecho);

    LinkedList<HechosCertifico> obtenerHechosParaCertifico(int anno, int mes);

    LinkedList<Hechos> obtenerHechosParaUnidadOrganizativaPorMesYAnno(int mes, int anno, int uorg);

    LinkedList<Hechos> obtenerHechosResumenMincom(int anno, int mes);

    LinkedList<HechosPrevenidos> obtenerHechosPrevenidos(int year);

    LinkedList<Hechos> obtenerHechosDatosPendientes(int year);

    AfectacionesServiciosAfectados obtenerAfectacionServicio(Date inicio, Date fin, int tipoHecho);

    LinkedList<CantidadDelitoRangoFecha> obtenerCantidadDelitoRangoFecha(Date inicio, Date fin);

    LinkedList<Hechos> obtenerHechosByTypeAndDate(double anno, int mes, int tipoHecho);

    LinkedList<Hechos> getHechosBySqlExpresion(String sql);

    LinkedList<Hechos> fetchAllHechos2(String limit, String offset);

    LinkedList<HechosByAnno> cantidadRobosHurtosByAnno(int anno);

    LinkedList<HurtosRobosPrevUorg> obtenerRobosHurtosPrev(Date fecha);

}
