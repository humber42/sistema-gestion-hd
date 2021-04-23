package services;


import models.*;
import org.postgresql.util.PSQLException;
import util.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;


public class HechosServiceImpl implements HechosService {


    public Hechos getHecho(int id) {
        Hechos hechos = new Hechos();
        String query = "SELECT * FROM hechos WHERE id_reg=" + Integer.toString(id);
        try {
            var resultSet = Util.executeQuery(query);
            if (resultSet.next())
                hechos = recuperarResultSetHechos(resultSet);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechos;
    }

    public LinkedList<Hechos> fetchAllHechos(String limit, String offset) {
        var query = "SELECT * FROM hechos ORDER BY hechos.id_reg DESC LIMIT " + limit + " OFFSET " + offset;
        return this.recuperarListaResultSet(query);
    }

    /**
     * Delito vs TPubl
     *
     * @param hechos     Hechos object
     * @param vandalismo Vandalism object to update hechos object
     */
    public void registrarHecho(Hechos hechos, TipoVandalismo vandalismo) throws SQLException {
        this.registrarHecho(hechos);
        var function = "{call registrar_hecho_delito_tpubl(?,?)}";
        try {
            CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
            callableStatement.setInt(1, this.searchHechoByCODCDNT(hechos.getCod_cdnt()).getId_reg());
            callableStatement.setInt(2, vandalismo.getId_afect_tpublica());
            callableStatement.execute();
            callableStatement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accidente de transito
     *
     * @param hechos    Hechos object
     * @param imputable boolean data corresponding to transit accident
     * @param incidente boolean data corresponding to transit accident
     */
    public void registrarHecho(Hechos hechos, boolean imputable, boolean incidente) throws SQLException {
        this.registrarHecho(hechos);
        //Actualizar con accidente de transito
        var function = "{call registrar_hecho_accidente_transito(?,?,?)}";
        try {
            CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
            callableStatement.setInt(1, this.searchHechoByCODCDNT(hechos.getCod_cdnt()).getId_reg());
            callableStatement.setBoolean(2, imputable);
            callableStatement.setBoolean(3, incidente);
            callableStatement.execute();
            callableStatement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Delito vs Pext
     *
     * @param hechos     Hechos Object
     * @param materiales material to insert
     * @param cantidad   quantity of materials
     */
    public void registrarHecho(Hechos hechos, TipoMateriales materiales, int cantidad) throws SQLException {
        this.registrarHecho(hechos);
        //Actualizar el hecho con los materiales afectados y la cantidad
        var function = "{call registrar_hecho_delito_vs_pext(?,?,?)}";
        try {
            CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
            callableStatement.setInt(1, this.searchHechoByCODCDNT(hechos.getCod_cdnt()).getId_reg());
            callableStatement.setInt(2, materiales.getId_materiales());
            callableStatement.setInt(3, cantidad);
            callableStatement.execute();
            callableStatement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcion para salvar el hecho en la base de datos
     *
     * @param hechos hechos model
     */
    public void registrarHecho(Hechos hechos) throws SQLException {
        String function = "{call registrar_hecho(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";


        CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
        callableStatement.setString(1, hechos.getTitulo());
        callableStatement.setInt(2, hechos.getTipoHecho().getId_tipo_hecho());
        callableStatement.setDate(3, hechos.getFecha_ocurrencia());
        callableStatement.setDate(4, hechos.getFecha_parte());
        callableStatement.setInt(5, hechos.getUnidadOrganizativa().getId_unidad_organizativa());
        callableStatement.setString(6, hechos.getCentro());
        callableStatement.setString(7, hechos.getLugar());
        callableStatement.setInt(8, hechos.getMunicipio().getId_municipio());
        callableStatement.setString(9, hechos.getNumero_denuncia());
        callableStatement.setDouble(10, hechos.getAfectacion_usd());
        callableStatement.setDouble(11, hechos.getAfectacion_mn());
        callableStatement.setDouble(12, hechos.getAfectacion_servicio());
        callableStatement.setString(13, hechos.getObservaciones());
        callableStatement.setString(14, hechos.getCod_cdnt());
        callableStatement.execute();
        callableStatement.close();
        Conexion.getConnection().close();


    }

    public Hechos searchHechoByCODCDNT(String cod_cdnt) {
        Hechos hechos = new Hechos();
        var query = "Select * FROM hechos WHERE cod_cdnt= '" + cod_cdnt + "'";
        try {
            var resultset = Util.executeQuery(query);
            if (resultset.next())
                hechos = this.recuperarResultSetHechos(resultset);
            resultset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechos;
    }

    @Override
    public LinkedList<Hechos> fetchHechosPextTpub(String offset) throws PSQLException {
        var query = "SELECT * From hechos WHERE hechos.id_tipo_hecho = 1 or hechos.id_tipo_hecho =2 ORDER BY fecha_ocurrencia Limit 30 Offset " + offset;
        return recuperarListaResultSet(query);
    }

    @Override
    public int countHechos() {
        var query = "Select count(id_reg) From hechos WHERE id_tipo_hecho=1 or id_tipo_hecho=2";
        return Util.count(query);
    }

    @Override
    public LinkedList<Hechos> fetchBySubStringCodCDNT(String codCdnt, String offset) {
        var query = "SELECT * From hechos WHERE (hechos.id_tipo_hecho = 1 or hechos.id_tipo_hecho =2) and cod_cdnt LIKE '%" + codCdnt + "%' ORDER BY fecha_ocurrencia LIMIT 10 OFFSET " + offset;
        return recuperarListaResultSet(query);

    }

    @Override
    public int countAllHechos() {
        var query = "SELECT count(id_reg) FROM hechos";
        return Util.count(query);
    }

    @Override
    public int countfetchBySubStringCodCDNT(String codCdnt) {
        var query = "SELECT count(id_reg) From hechos WHERE (hechos.id_tipo_hecho = 1 or hechos.id_tipo_hecho =2) and cod_cdnt LIKE '%" + codCdnt + "%'";
        return Util.count(query);
    }

    @Override
    public void esclarecimientoHechoPExtTpubl(int id, boolean sinDenuncia, boolean expSAC, boolean esclarecido, boolean articulo83, boolean articulo82, boolean medidasAdministrativas, boolean pendienteJuicio, boolean expFasePreparatoria, boolean menorEdad, boolean pendienteDespacho, boolean privacionLibertad, String cantSancionados, String sentencia) {
        String function = "{call actualizar_exclarecimiento(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
            callableStatement.setInt(1, id);
            callableStatement.setBoolean(2, sinDenuncia);
            callableStatement.setBoolean(3, expSAC);
            callableStatement.setBoolean(4, esclarecido);
            callableStatement.setBoolean(5, articulo83);
            callableStatement.setBoolean(6, articulo82);
            callableStatement.setBoolean(7, medidasAdministrativas);
            callableStatement.setBoolean(8, pendienteJuicio);
            callableStatement.setBoolean(9, expFasePreparatoria);
            callableStatement.setBoolean(10, menorEdad);
            callableStatement.setBoolean(11, pendienteDespacho);
            callableStatement.setBoolean(12, privacionLibertad);
            callableStatement.setInt(13, Integer.parseInt(cantSancionados));
            callableStatement.setString(14, sentencia);
            callableStatement.execute();
            callableStatement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public LinkedList<ResumenModels> cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre(Date date, int tipo) {
        String function = "{call contar_hechos_pext_por_unidades_organizativas_annos(?,?,?)}";
        String fechaInicio = String.valueOf(date.toLocalDate().getYear()) + "-01-01";
        LinkedList<ResumenModels> resumenModels = new LinkedList<>();

        try {
            CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
            callableStatement.setDate(1, date);
            callableStatement.setString(2, fechaInicio);
            callableStatement.setInt(3, tipo);
            callableStatement.execute();
            resumenModels = this.recuperarListaResumenModels(callableStatement.getResultSet());
            callableStatement.close();
            Conexion.getConnection().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resumenModels;
    }


    public int cantHechosPextCierreFiscalia(Date date, int tipoHecho) {
        String function = "{call cant_hechos_pext_hastacierre(?,?,?)}";
        String fechaInicio = String.valueOf(date.toLocalDate().getYear()) + "-01-01";
        int cant = 0;
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setString(2, fechaInicio);
            statement.setInt(3, tipoHecho);
            statement.execute();
            var resultSet = statement.getResultSet();
            if (resultSet.next()) {
                cant = resultSet.getInt(1);
            }
            statement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cant;
    }


    @Override
    public LinkedList<AfectacionFiscaliaModels> cantidadAfectacionesHechosFiscalia(Date date) {
        LinkedList<AfectacionFiscaliaModels> afectaciones = new LinkedList<>();
        String fechaInicio = String.valueOf(date.toLocalDate().getYear()) + "-01-01";
        String function = "{call cantidad_afectaciones_anno_cierre_fiscalia(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setString(2, fechaInicio);
            statement.execute();
            afectaciones = this.recuperarListaAfectacionFiscaliaModels(statement.getResultSet());
            statement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return afectaciones;
    }

    @Override
    public LinkedList<HechosByAnno> cantidadHechosByAnno(int anno) {
        LinkedList<HechosByAnno> hechosByAnnos = new LinkedList<>();
        String function = "{call obtener_cant_hechos_pext_mes_anno(?)}";

        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDouble(1, Double.parseDouble(String.valueOf(anno)));
            statement.execute();
            hechosByAnnos = this.recuperarHechosByAnno(statement.getResultSet());
            statement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hechosByAnnos;
    }

    @Override
    public LinkedList<EstacionesPublicas> getEstacionesPublicasCant() {
        LinkedList<EstacionesPublicas> estacionesPublicas = new LinkedList<>();
        try {
            var resultSet = Util.executeQuery("SELECT * from cantidad_estaciones_publicas_unidades_organizativas()");
            while (resultSet.next()) {
                estacionesPublicas.add(
                        new EstacionesPublicas(
                                resultSet.getString(1),
                                resultSet.getInt(2)
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estacionesPublicas;

    }

    @Override
    public LinkedList<EsclarecimientoHechos> getEsclarecimientoConciliados(Date date) {
        LinkedList<EsclarecimientoHechos> esclarecimientoHechos = new LinkedList<>();
        String function = "{call obtener_total_conciliados_anho(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setString(2, date.toLocalDate().getYear() + "-01-01");
            statement.execute();
            esclarecimientoHechos = recuperarEsclarecimiento(statement.getResultSet());
            statement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return esclarecimientoHechos;
    }

    public LinkedList<EsclarecimientoHechos> getEsclaremientoConciliados(Date date, boolean esclarecido) {
        LinkedList<EsclarecimientoHechos> esclarecimientoHechos = new LinkedList<>();
        String function = "{call obtener_total_hechos_esclarecidos_o_no_anho(?,?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setBoolean(2, esclarecido);
            statement.setString(3, date.toLocalDate().getYear() + "-01-01");
            statement.execute();
            esclarecimientoHechos = recuperarEsclarecimiento(statement.getResultSet());
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return esclarecimientoHechos;
    }


    @Override
    public LinkedList<HechosPorMunicipio> obtenerHechosPorMunicipio(Date date, int tipoHecho) {
        LinkedList<HechosPorMunicipio> hechosPorMunicipios = new LinkedList<>();
        String function = "{call obtener_hechos_por_municipio(?,?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setInt(2, tipoHecho);
            statement.setString(3, date.toLocalDate().getYear() + "-01-01");
            statement.execute();
            hechosPorMunicipios = recuperarHechosPorMunicipio(statement.getResultSet());
            statement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hechosPorMunicipios;
    }

    @Override
    public LinkedList<MunicipioServiciosAfectados> serviciosAfectadosPorMunicipio(Date date, int tipoHecho) {
        LinkedList<MunicipioServiciosAfectados> hechosPorMunicipios = new LinkedList<>();
        String function = "{call obtener_servicios_por_municipio(?,?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setInt(2, tipoHecho);
            statement.setString(3, date.toLocalDate().getYear() + "-01-01");
            statement.execute();
            hechosPorMunicipios = recuperarHechosPorServiciosAfectados(statement.getResultSet());
            statement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosPorMunicipios;
    }

    @Override
    public LinkedList<Afectaciones> afectacionesTPubMunicipio(Date date) {
        LinkedList<Afectaciones> afectacionesLinkedList = new LinkedList<>();
        var function = "{call obtener_telefonia_publica_municipios_anno(?,?)}";

        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setString(2, date.toLocalDate().getYear() + "-01-01");
            statement.execute();
            afectacionesLinkedList = recuperararAfectacionesTpub(statement.getResultSet());
            statement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return afectacionesLinkedList;
    }

    @Override
    public LinkedList<Afectaciones> calculoServiciosAfectadosEstacionesPublicas(Date date) {
        LinkedList<Afectaciones> afectacionesLinkedList = new LinkedList<>();
        var query = "Select * From obtener_telefonia_publica_anno(" + date.toLocalDate().getYear() + "::double precision)";
        try {
            var resulset = Util.executeQuery(query);
            afectacionesLinkedList = recuperararAfectaciones(resulset);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "Select * FROM obtener_telefonia_publica_habana_anno(" + date.toLocalDate().getYear() + "::double precision,29,30,31,32)";
        try {
            var resultset = Util.executeQuery(query);
            if (resultset.next()) {
                afectacionesLinkedList.add(new Afectaciones(
                        resultset.getString(1),
                        resultset.getInt(2),
                        resultset.getInt(3),
                        resultset.getInt(4)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        afectacionesLinkedList = ordenarAfectaciones(afectacionesLinkedList);

        return afectacionesLinkedList;
    }

    @Override
    public LinkedList<HechosMesAnno> hechosMesesAnno(Date date) {
        LinkedList<HechosMesAnno> hechosMesAnnos = new LinkedList<>();
        var query = "SELECT * FROM obtener_hechos_mes_anno(" + date.toLocalDate().getYear() + "::double precision)";
        try {
            hechosMesAnnos = recuperarHechosMesAnno(Util.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosMesAnnos;
    }

    @Override
    public LinkedList<HechosUOrgAnno> hechosUnidadOranizativaList(Date date) {
        LinkedList<HechosUOrgAnno> hechosUOrgAnnos = new LinkedList<>();
        var function = "{call obtener_hechos_uorg_hasta_fecha(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setString(2, date.toLocalDate().getYear() + "-01-01");
            statement.execute();
            hechosUOrgAnnos = recuperarHechosUorg(statement.getResultSet());
            statement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosUOrgAnnos;
    }

    @Override
    public LinkedList<HechosUorgMesAnno> obtenerCantidadHechosUOrgPorMes(int anno, int tipoHecho) {
        LinkedList<HechosUorgMesAnno> hechosUorgMesAnnos = new LinkedList<>();
        var function = "{call obtener_cantidad_hechos_uorg_por_mes(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDouble(1, anno);
            statement.setInt(2, tipoHecho);
            statement.execute();
            hechosUorgMesAnnos = recuperarHechosUorgMesAnno(statement.getResultSet());
            statement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosUorgMesAnnos;
    }

    @Override
    public LinkedList<HechosCertifico> obtenerHechosParaCertifico(int anno, int mes) {
        LinkedList<HechosCertifico> hechosCertificos = new LinkedList<>();
        var query = "SELECT * FROM obtener_cant_hechos_delictivos_mes_anno(" + anno + "," + mes + ")";
        try {
            hechosCertificos = recuperarHechosCertifico(Util.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosCertificos;
    }

    @Override
    public LinkedList<Hechos> obtenerHechosParaUnidadOrganizativaPorMesYAnno(int mes, int anno, int uorg) {
        var query = "Select * From hechos_por_uorg_mes_anno(" + uorg + "," + anno + "," + mes + ")";
        return recuperarListaResultSet(query);
    }

    @Override
    public LinkedList<Hechos> obtenerHechosResumenMincom(int anno, int mes) {
        var query = "Select * from obtener_hechos_para_resumen_mincom(" + anno + "," + mes + ")";
        return recuperarListaResultSet(query);
    }

    @Override
    public LinkedList<HechosPrevenidos> obtenerHechosPrevenidos(int year) {
        LinkedList<HechosPrevenidos> prevenidos = new LinkedList<>();
        var query = "Select * from obtener_cantidad_hechos_prevenidos(" + year + ")";
        try {
            prevenidos = recuperarHechosPrevenidos(Util.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prevenidos;
    }

    @Override
    public LinkedList<Hechos> obtenerHechosDatosPendientes(int year) {
        var query = "Select * FROM obtener_hechos_denuncia_perdidas(" + year + ")";
        return recuperarListaResultSet(query);
    }

    @Override
    public AfectacionesServiciosAfectados obtenerAfectacionServicio(Date inicio, Date fin, int tipoHecho) {
        AfectacionesServiciosAfectados afectado = new AfectacionesServiciosAfectados();
        var function = "{call obtener_afectaciones_rango_fecha(?,?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, inicio);
            statement.setDate(2, fin);
            statement.setInt(3, tipoHecho);
            statement.execute();
            afectado = recuperarAfectaciones(statement.getResultSet());
            statement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return afectado;
    }


    @Override
    public LinkedList<CantidadDelitoRangoFecha> obtenerCantidadDelitoRangoFecha(Date inicio, Date fin) {
        LinkedList<CantidadDelitoRangoFecha> delitoRangoFechas = new LinkedList<>();
        var function = "{call obtener_cant_hechos_delictivos_rango_fecha(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, inicio);
            statement.setDate(2, fin);
            statement.execute();
            delitoRangoFechas = recuperarDelitoRangoFecha(statement.getResultSet());
            statement.close();
            Conexion.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return delitoRangoFechas;
    }

    @Override
    public LinkedList<Hechos> obtenerHechosByTypeAndDate(double anno, int mes, int tipoHecho) {
        var query = " Select * from obtener_cant_hechos__mes_o_anno(" + anno + "," + mes + "," + tipoHecho + ")";
        return recuperarListaResultSet(query);
    }

    ///////////////// Recuperacion de result sets/////////////////////////////

    private LinkedList<CantidadDelitoRangoFecha> recuperarDelitoRangoFecha(ResultSet set) throws SQLException {
        LinkedList<CantidadDelitoRangoFecha> rangoFechas = new LinkedList<>();
        while (set.next()) {
            rangoFechas.add(new CantidadDelitoRangoFecha(
                    set.getString(1),
                    set.getInt(2),
                    set.getInt(3),
                    set.getInt(4),
                    set.getInt(5),
                    set.getInt(6),
                    set.getInt(7),
                    set.getInt(8)
            ));
        }
        return rangoFechas;
    }

    private AfectacionesServiciosAfectados recuperarAfectaciones(ResultSet set) throws SQLException {
        AfectacionesServiciosAfectados afectados = new AfectacionesServiciosAfectados();
        while (set.next()) {
            afectados = new AfectacionesServiciosAfectados(
                    set.getDouble(1),
                    set.getDouble(2),
                    set.getDouble(3)
            );
        }
        return afectados;
    }

    private LinkedList<HechosPrevenidos> recuperarHechosPrevenidos(ResultSet set) throws SQLException {
        LinkedList<HechosPrevenidos> prevenidos = new LinkedList<>();
        while (set.next()) {
            prevenidos.add(
                    new HechosPrevenidos(
                            set.getString(1),
                            set.getInt(2),
                            set.getInt(3),
                            set.getInt(4),
                            set.getInt(5),
                            set.getInt(6),
                            set.getInt(7),
                            set.getInt(8),
                            set.getInt(9)
                    )
            );
        }
        return prevenidos;
    }

    private LinkedList<HechosCertifico> recuperarHechosCertifico(ResultSet set) throws SQLException {
        LinkedList<HechosCertifico> hechosCertificos = new LinkedList<>();
        while (set.next()) {
            hechosCertificos.add(
                    new HechosCertifico(
                            set.getString(1),
                            set.getInt(2),
                            set.getInt(3),
                            set.getInt(4),
                            set.getInt(5),
                            set.getInt(6),
                            set.getInt(7)
                    )
            );
        }
        return hechosCertificos;
    }

    private LinkedList<HechosUorgMesAnno> recuperarHechosUorgMesAnno(ResultSet set) throws SQLException {
        LinkedList<HechosUorgMesAnno> list = new LinkedList<>();
        while (set.next()) {
            list.add(
                    new HechosUorgMesAnno(
                            set.getDouble(1),
                            set.getString(2),
                            set.getString(3),
                            set.getInt(4)
                    )
            );
        }
        return list;
    }

    private LinkedList<HechosUOrgAnno> recuperarHechosUorg(ResultSet set) {
        LinkedList<HechosUOrgAnno> list = new LinkedList<>();
        try {
            while (set.next()) {
                list.add(new HechosUOrgAnno(
                                set.getString(1),
                                set.getInt(2),
                                set.getInt(3)
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    private LinkedList<HechosMesAnno> recuperarHechosMesAnno(ResultSet set) {
        LinkedList<HechosMesAnno> hechosMesAnnos = new LinkedList<>();
        try {
            while (set.next()) {
                hechosMesAnnos.add(new HechosMesAnno(
                        set.getString(2),
                        set.getInt(3),
                        set.getInt(4),
                        set.getInt(5),
                        set.getInt(6),
                        set.getInt(7),
                        set.getInt(8)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosMesAnnos;
    }


    private LinkedList<HechosPorMunicipio> recuperarHechosPorMunicipio(ResultSet set) {
        LinkedList<HechosPorMunicipio> hechosPorMunicipios = new LinkedList<>();
        try {
            while (set.next()) {
                hechosPorMunicipios.add(new HechosPorMunicipio(
                        set.getString(1),
                        set.getInt(2)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosPorMunicipios;
    }

    private LinkedList<MunicipioServiciosAfectados> recuperarHechosPorServiciosAfectados(ResultSet set) {
        LinkedList<MunicipioServiciosAfectados> hechosPorMunicipios = new LinkedList<>();
        try {
            while (set.next()) {
                hechosPorMunicipios.add(new MunicipioServiciosAfectados(
                        set.getString(1),
                        set.getDouble(2)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosPorMunicipios;
    }


    private LinkedList<EsclarecimientoHechos> recuperarEsclarecimiento(ResultSet set) {
        LinkedList<EsclarecimientoHechos> esclarecimientoHechos = new LinkedList<>();
        try {
            while (set.next()) {
                esclarecimientoHechos.add(new EsclarecimientoHechos(
                        set.getString(1),
                        set.getInt(2),
                        set.getInt(3),
                        set.getInt(4)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return esclarecimientoHechos;
    }

    private LinkedList<Afectaciones> ordenarAfectaciones(LinkedList<Afectaciones> lista) {
        LinkedList<Afectaciones> afectacionesLinkedList = lista;
        Collections.sort(afectacionesLinkedList, Comparator.comparingDouble(Afectaciones::getServiciosAfectadosVSEstacionesPublicas));
        Collections.reverse(afectacionesLinkedList);
        return afectacionesLinkedList;
    }

    private LinkedList<Afectaciones> recuperararAfectacionesTpub(ResultSet set) {
        LinkedList<Afectaciones> afectacionesLinkedList = new LinkedList<>();

        try {
            while (set.next()) {
                afectacionesLinkedList.add(new Afectaciones(
                        set.getString(1),
                        set.getInt(2),
                        set.getInt(3),
                        set.getInt(4),
                        set.getDouble(5)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return afectacionesLinkedList;
    }

    private LinkedList<Afectaciones> recuperararAfectaciones(ResultSet set) {
        LinkedList<Afectaciones> afectacionesLinkedList = new LinkedList<>();
        try {
            while (set.next()) {

                afectacionesLinkedList.add(new Afectaciones(
                        set.getString(1),
                        set.getInt(2),
                        set.getInt(3),
                        set.getInt(4)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return afectacionesLinkedList;

    }

    private LinkedList<HechosByAnno> recuperarHechosByAnno(ResultSet set) {
        LinkedList<HechosByAnno> hechosByAnnos = new LinkedList<>();
        try {
            while (set.next()) {
                hechosByAnnos.add(new HechosByAnno(
                        set.getDouble(1),
                        set.getDouble(2),
                        set.getInt(3),
                        set.getInt(4)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosByAnnos;
    }


    private LinkedList<AfectacionFiscaliaModels> recuperarListaAfectacionFiscaliaModels(ResultSet set) {
        LinkedList<AfectacionFiscaliaModels> lista = new LinkedList<>();
        try {
            while (set.next()) {
                lista.add(new AfectacionFiscaliaModels(
                        set.getString(1),
                        set.getInt(2)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

//    private LinkedList<MaterialsFiscaliaModels> recuperarListaMaterialesFiscaliaModels(ResultSet set) {
//        LinkedList<MaterialsFiscaliaModels> materialsFiscaliaModels = new LinkedList<>();
//        try {
//            while (set.next()) {
//                materialsFiscaliaModels.add(new MaterialsFiscaliaModels(
//                        set.getString(1),
//                        set.getInt(2),
//                        set.getInt(3)
//                ));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return materialsFiscaliaModels;
//    }


    @Override
    public LinkedList<Hechos> getHechosBySqlExpresion(String sql) {
        return recuperarListaResultSet(sql);
    }

    /**
     * Recuperar lista de hechos dado una consulta sql
     *
     * @param query consulta
     * @return lista con los hechos
     */

    private LinkedList<Hechos> recuperarListaResultSet(String query) {
        LinkedList<Hechos> hechos = new LinkedList<>();
        try {
            var resultset = Util.executeQuery(query);
            while (resultset.next()) {
                hechos.add(recuperarResultSetHechos(resultset));
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return hechos;
    }

    /**
     * Method who help to recover an Hecho
     *
     * @param resultSet cursor from database
     * @return Hechos type
     */

    private Hechos recuperarResultSetHechos(ResultSet resultSet) {
        Hechos hechos = new Hechos();
        try {
            hechos.setId_reg(resultSet.getInt("id_reg"));
            hechos.setTitulo(resultSet.getString("titulo"));
            hechos.setFecha_ocurrencia(resultSet.getDate("fecha_ocurrencia"));
            hechos.setFecha_parte(resultSet.getDate("fecha_parte"));
            hechos.setNumero_parte(resultSet.getString("numero_parte"));
            hechos.setCentro(resultSet.getString("centro"));
            hechos.setLugar(resultSet.getString("lugar"));
            hechos.setNumero_denuncia(resultSet.getString("numero_denuncia"));
            hechos.setAfectacion_usd(resultSet.getDouble("afectacion_usd"));
            hechos.setAfectacion_mn(resultSet.getDouble("afectacion_mn"));
            hechos.setAfectacion_servicio(resultSet.getDouble("afectacion_servicio"));
            hechos.setObservaciones(resultSet.getString("observaciones"));
            hechos.setCod_cdnt(resultSet.getString("cod_cdnt"));
            hechos.setImputable(resultSet.getBoolean("imputable"));
            hechos.setIncidencias(resultSet.getBoolean("incidencias"));
            hechos.setCantidad(resultSet.getInt("cantidad"));
            hechos.setEsclarecido(resultSet.getBoolean("esclarecido"));
            hechos.setExpediente_inv_sac(resultSet.getBoolean("expediente_inv_sac"));
            hechos.setSin_denuncia(resultSet.getBoolean("sin_denuncia"));
            hechos.setArticulo_82(resultSet.getBoolean("articulo_82"));
            hechos.setArticulo_83(resultSet.getBoolean("articulo_83"));
            hechos.setMedida_administrativa(resultSet.getBoolean("medida_administrativa"));
            hechos.setMenor_edad(resultSet.getBoolean("menor_edad"));
            hechos.setExpfp(resultSet.getBoolean("expfp"));
            hechos.setPendiente_despacho(resultSet.getBoolean("pendiente_despacho"));
            hechos.setPendiente_juicio(resultSet.getBoolean("pendiente_juicio"));
            hechos.setPriv_lib(resultSet.getBoolean("priv_lib"));
            hechos.setCantidad_sancionados(resultSet.getInt("cantidad_sancionados"));
            hechos.setSentencia(resultSet.getString("sentencia"));
            hechos.setPrevenido(resultSet.getBoolean("prevenido"));

            hechos.setAveriasPext(
                    ServiceLocator.getAveriasPExtService()
                            .searchAveriaPExt(resultSet.getInt("id_averia_planta_exterior"))
            );

            hechos.setTipoHecho(
                    ServiceLocator.getTipoHechoService().getOneTipoHecho(resultSet.getInt("id_tipo_hecho"))
            );

            hechos.setMateriales(
                    ServiceLocator.getTipoMaterialesService().getOneTipo(resultSet.getInt("id_materialespe"))
            );

            hechos.setTipoVandalismo(
                    ServiceLocator.getTipoVandalismoService().getOneTipoVandalismo(resultSet.getInt("id_afectacion_telefonia_publica"))
            );
            hechos.setMunicipio(
                    ServiceLocator.getMunicipiosService().getOne(resultSet.getInt("id_municipio"))
            );
            hechos.setUnidadOrganizativa(
                    ServiceLocator.getUnidadOrganizativaService().getOneUnidadOrganizativa(resultSet.getInt("id_uorg"))
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechos;
    }

    /**
     * Funcion para recuperar una lista con el resumen models
     *
     * @param resultSet resultset
     * @return lista con los modelos
     */

    private LinkedList<ResumenModels> recuperarListaResumenModels(ResultSet resultSet) {
        LinkedList<ResumenModels> resumenModels = new LinkedList<>();
        try {
            while (resultSet.next()) {
                ResumenModels models = new ResumenModels(
                        resultSet.getString(1),
                        resultSet.getInt(2),
                        resultSet.getDouble(4),
                        resultSet.getDouble(3)

                );
                resumenModels.add(models);
            }
        } catch (SQLException e0) {
            e0.printStackTrace();
        }
        return resumenModels;
    }

    public void editarHechos(Hechos hechos)throws SQLException{
        var function = "{call editar_hechos(?,?,?,?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1,hechos.getId_reg());
        statement.setString(2,hechos.getTitulo());
        statement.setString(3,hechos.getCentro());
        statement.setString(4,hechos.getLugar());
        statement.execute();
        statement.close();
    }

    public void eliminarHechos (Hechos hechos)throws SQLException{
        var function = "{call eliminar_hechos(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1,hechos.getId_reg());
        statement.execute();
        statement.close();
    }
}
