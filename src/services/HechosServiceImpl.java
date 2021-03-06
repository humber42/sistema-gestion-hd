package services;


import models.*;
import models.resumen_esclarecimiento.HechosEsclarecimientoPExtTPub;
import models.resumen_esclarecimiento.HechosEsclarecimientoResumen;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
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
import java.util.List;
import java.util.stream.Collectors;


public class HechosServiceImpl implements HechosService {


    public Hechos getHecho(int id) {
        Hechos hechos = new Hechos();
        String query = "SELECT * FROM hechos WHERE id_reg=" + Integer.toString(id);
        try {
            ResultSet resultSet = Util.executeQuery(query);
            if (resultSet.next())
                hechos = recuperarResultSetHechos(resultSet);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechos;
    }

    public LinkedList<Hechos> fetchAllHechos(String limit, String offset) {
        String query = "SELECT * FROM hechos ORDER BY hechos.id_reg DESC LIMIT " + limit + " OFFSET " + offset;
        return this.recuperarListaResultSet(query);
    }

    public LinkedList<Hechos> fetchAllHechos2(String limit, String offset) {
        String query = "SELECT * FROM hechos Order By fecha_ocurrencia Desc LIMIT " + limit + " OFFSET " + offset;
        return this.recuperarListaResultSet(query);
    }

    @Override
    public void updateHecho(Hechos hechos) throws SQLException {
        String function = "{call update_hecho(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, hechos.getId_reg());
        statement.setString(2, hechos.getTitulo());
        statement.setInt(3, hechos.getTipoHecho().getId_tipo_hecho());
        statement.setDate(4, hechos.getFecha_ocurrencia());
        statement.setDate(5, hechos.getFecha_parte());
        statement.setInt(6, hechos.getUnidadOrganizativa().getId_unidad_organizativa());
        statement.setString(7, hechos.getCentro());
        statement.setString(8, hechos.getLugar());
        statement.setInt(9, hechos.getMunicipio().getId_municipio());
        statement.setString(10, hechos.getNumero_denuncia());
        statement.setDouble(11, hechos.getAfectacion_usd());
        statement.setDouble(12, hechos.getAfectacion_mn());
        statement.setDouble(13, hechos.getAfectacion_servicio());
        statement.setString(14, hechos.getObservaciones());
        statement.setString(15, hechos.getCod_cdnt());
        statement.setBoolean(16, hechos.isImputable());
        statement.setBoolean(17, hechos.isIncidencias());
        statement.setObject(18, hechos.getMateriales() == null
                ? null
                : hechos.getMateriales().getId_materiales());
        statement.setInt(19, hechos.getCantidad());
        statement.setObject(20, hechos.getTipoVandalismo() == null
                ? null
                : hechos.getTipoVandalismo().getId_afect_tpublica());
        statement.setObject(21, hechos.getAveriasPext() == null
                ? null
                : hechos.getAveriasPext().getId_avpext());
        statement.setBoolean(22, hechos.isPrevenido());
        statement.execute();
        statement.close();
    }

    /**
     * Delito vs TPubl
     *
     * @param hechos     Hechos object
     * @param vandalismo Vandalism object to update hechos object
     */
    public void registrarHecho(Hechos hechos, TipoVandalismo vandalismo) throws SQLException {
        this.registrarHecho(hechos);
        String function = "{call registrar_hecho_delito_tpubl(?,?)}";
        try {
            CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
            callableStatement.setInt(1, this.searchHechoByCODCDNT(hechos.getCod_cdnt()).getId_reg());
            callableStatement.setInt(2, vandalismo.getId_afect_tpublica());
            callableStatement.execute();
            callableStatement.close();
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
        String function = "{call registrar_hecho_accidente_transito(?,?,?)}";
        try {
            CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
            callableStatement.setInt(1, this.searchHechoByCODCDNT(hechos.getCod_cdnt()).getId_reg());
            callableStatement.setBoolean(2, imputable);
            callableStatement.setBoolean(3, incidente);
            callableStatement.execute();
            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Averias PExt
     *
     * @param hechos      Hechos object
     * @param averiasPext AveriaPext data corresponding to de id AveriaPext
     */
    public void registrarHechos(Hechos hechos, AveriasPext averiasPext) throws SQLException {
        this.registrarHecho(hechos);
        String function = "{call registrar_hecho_averia_pext(?,?)}";
        try {
            CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
            callableStatement.setInt(1, this.searchHechoByCODCDNT(hechos.getCod_cdnt()).getId_reg());
            callableStatement.setInt(2, averiasPext.getId_avpext());
            callableStatement.execute();
            callableStatement.close();
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
        String function = "{call registrar_hecho_delito_vs_pext(?,?,?)}";
        try {
            CallableStatement callableStatement = Conexion.getConnection().prepareCall(function);
            callableStatement.setInt(1, this.searchHechoByCODCDNT(hechos.getCod_cdnt()).getId_reg());
            callableStatement.setInt(2, materiales.getId_materiales());
            callableStatement.setInt(3, cantidad);
            callableStatement.execute();
            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcion para salString el hecho en la base de datos
     *
     * @param hechos hechos model
     */
    public void registrarHecho(Hechos hechos) throws SQLException {
        String function = "{call registrar_hecho_modified(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

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
        callableStatement.setBoolean(15, hechos.isPrevenido());
        callableStatement.execute();
        callableStatement.close();
    }

    public Hechos searchHechoByCODCDNT(String cod_cdnt) {
        Hechos hechos = new Hechos();
        String query = "Select * FROM hechos WHERE cod_cdnt= '" + cod_cdnt + "'";
        try {
            ResultSet resultset = Util.executeQuery(query);
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
        String query = "SELECT * From hechos WHERE hechos.id_tipo_hecho = 1 or hechos.id_tipo_hecho =2 ORDER BY fecha_ocurrencia Limit 30 Offset " + offset;
        return recuperarListaResultSet(query);
    }

    @Override
    public int countHechos() {
        String query = "Select count(id_reg) From hechos WHERE id_tipo_hecho=1 or id_tipo_hecho=2";
        return Util.count(query);
    }

    @Override
    public List<Hechos> fetchBySubStringCodCDNT(String codCdnt, String offset) {
        String query = "SELECT * From hechos WHERE (hechos.id_tipo_hecho = 1 or hechos.id_tipo_hecho =2) and cod_cdnt LIKE '%" + codCdnt + "%' ORDER BY fecha_ocurrencia LIMIT 10 OFFSET " + offset;
        return recuperarListaResultSet(query);
    }

    @Override
    public int countAllHechos() {
        String query = "SELECT count(id_reg) FROM hechos";
        return Util.count(query);
    }

    @Override
    public int countAllHechosByAnno(int anno) {
        String query = "SELECT count(id_reg) FROM hechos WHERE date_part('year', fecha_ocurrencia) = " + anno;
        return Util.count(query);
    }

    @Override
    public int cantHechosByTipoHechoAndAnno(int tipoHecho, int anno) {
        String function = "{call cant_hechos_anno(?,?)}";
        int cant = -1;
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setInt(1, tipoHecho);
            statement.setInt(2, anno);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if (rs.next()) {
                cant = rs.getInt(1);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cant;
    }

    @Override
    public int countfetchBySubStringCodCDNT(String codCdnt) {
        String query = "SELECT count(id_reg) From hechos WHERE (hechos.id_tipo_hecho = 1 or hechos.id_tipo_hecho =2) and cod_cdnt LIKE '%" + codCdnt + "%'";
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public LinkedList<ResumenModels> cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre(Date date, int tipo) {
        String function = "";
        if (date.toLocalDate().getYear() < 2021) {
            function = "{call contar_hechos_pext_por_unidades_organizativas_annos(?,?,?)}";
        } else {
            function = "{call contar_hechos_por_unidades_organizativas_annos_afectacion_mn(?,?,?)}";
        }
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
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                cant = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
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
            ResultSet rs = statement.getResultSet();
            afectaciones = this.recuperarListaAfectacionFiscaliaModels(rs);
            rs.close();
            statement.close();
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
            ResultSet rs = statement.getResultSet();
            hechosByAnnos = this.recuperarHechosByAnno(rs);
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hechosByAnnos;
    }

    @Override
    public LinkedList<HechosByAnno> cantidadRobosHurtosByAnno(int anno) {
        LinkedList<HechosByAnno> hechosByAnnos = new LinkedList<>();
        String function = "{call obtener_cant_hechos_hurto_robo_mes_anno(?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDouble(1, Double.parseDouble(String.valueOf(anno)));
            statement.execute();
            ResultSet rs = statement.getResultSet();
            hechosByAnnos = this.recuperarHechosByAnno(rs);
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosByAnnos;
    }

    @Override
    public LinkedList<HurtosRobosPrevUorg> obtenerRobosHurtosPrev(Date fecha) {
        LinkedList<HurtosRobosPrevUorg> hechosByUorg = new LinkedList<>();
        String function = "{call obtener_robos_hurtos_prev(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, fecha);
            statement.setDate(2, Date.valueOf(fecha.toLocalDate().getYear() + "-01-01"));
            statement.execute();
            ResultSet rs = statement.getResultSet();
            hechosByUorg = this.recuperarHechosHurtosRobosPrevUorg(rs);
            rs.close();
            statement.closeOnCompletion();
        } catch (SQLException e) {
            Logger.getLogger(this.getClass()).log(Level.ERROR, "Ha ocurrido una excepcion", e);
        }
        return hechosByUorg;
    }

    @Override
    public LinkedList<EstacionesPublicas> getEstacionesPublicasCant() {
        LinkedList<EstacionesPublicas> estacionesPublicas = new LinkedList<>();
        try {
            ResultSet resultSet = Util.executeQuery("SELECT * from cantidad_estaciones_publicas_unidades_organizativas()");
            while (resultSet.next()) {
                estacionesPublicas.add(
                        new EstacionesPublicas(
                                resultSet.getString(1),
                                resultSet.getInt(2)
                        )
                );
            }
            resultSet.close();
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
            ResultSet rs = statement.getResultSet();
            esclarecimientoHechos = recuperarEsclarecimiento(rs);
            rs.close();
            statement.close();
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
            ResultSet rs = statement.getResultSet();
            esclarecimientoHechos = recuperarEsclarecimiento(rs);
            rs.close();
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
            ResultSet rs = statement.getResultSet();
            hechosPorMunicipios = recuperarHechosPorMunicipio(rs);
            rs.close();
            statement.close();
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
            ResultSet rs = statement.getResultSet();
            hechosPorMunicipios = recuperarHechosPorServiciosAfectados(rs);
            rs.close();
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
        String function = "{call obtener_telefonia_publica_municipios_anno(?,?)}";

        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setString(2, date.toLocalDate().getYear() + "-01-01");
            statement.execute();
            ResultSet rs = statement.getResultSet();
            afectacionesLinkedList = recuperararAfectacionesTpub(rs);
            rs.close();
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
        String function = "{call obtener_telefonia_publica_anno_modified(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setDate(2, Date.valueOf(date.toLocalDate().getYear() + "-01-01"));
            statement.execute();
            ResultSet rs = statement.getResultSet();
            afectacionesLinkedList = recuperararAfectaciones(rs);
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        function = "{call obtener_telefonia_publica_habana_anno_modified(?,?,?,?,?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setInt(2, 29);
            statement.setInt(3, 30);
            statement.setInt(4, 31);
            statement.setInt(5, 32);
            statement.setDate(6, Date.valueOf(date.toLocalDate().getYear() + "-01-01"));
            statement.execute();
            ResultSet resultset = statement.getResultSet();
            if (resultset.next()) {
                afectacionesLinkedList.add(new Afectaciones(
                        resultset.getString(1),
                        resultset.getInt(2),
                        resultset.getInt(3),
                        resultset.getInt(4)
                ));
            }
            resultset.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }

        afectacionesLinkedList = ordenarAfectaciones(afectacionesLinkedList);

        return afectacionesLinkedList;
    }

    @Override
    public LinkedList<HechosMesAnno> hechosMesesAnno(Date date) {
        LinkedList<HechosMesAnno> hechosMesAnnos = new LinkedList<>();
        String function = " {call obtener_hechos_mes_anno_modified(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setDate(2, Date.valueOf(date.toLocalDate().getYear() + "-01-01"));
            statement.execute();
            ResultSet rs = statement.getResultSet();
            hechosMesAnnos = recuperarHechosMesAnno(rs);
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosMesAnnos;
    }

    @Override
    public LinkedList<HechosUOrgAnno> hechosUnidadOranizativaList(Date date) {
        LinkedList<HechosUOrgAnno> hechosUOrgAnnos = new LinkedList<>();
        String function = "{call obtener_hechos_uorg_hasta_fecha(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, date);
            statement.setString(2, date.toLocalDate().getYear() + "-01-01");
            statement.execute();
            ResultSet rs = statement.getResultSet();
            hechosUOrgAnnos = recuperarHechosUorg(rs);
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosUOrgAnnos;
    }

    @Override
    public LinkedList<HechosUorgMesAnno> obtenerCantidadHechosUOrgPorMes(int anno, int tipoHecho) {
        LinkedList<HechosUorgMesAnno> hechosUorgMesAnnos = new LinkedList<>();
        String function = "{call obtener_cantidad_hechos_uorg_por_mes(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDouble(1, anno);
            statement.setInt(2, tipoHecho);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            hechosUorgMesAnnos = recuperarHechosUorgMesAnno(rs);
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosUorgMesAnnos;
    }

    @Override
    public LinkedList<HechosCertifico> obtenerHechosParaCertifico(int anno, int mes) {
        LinkedList<HechosCertifico> hechosCertificos = new LinkedList<>();
        String query = "SELECT * FROM obtener_cant_hechos_delictivos_mes_anno(" + anno + "," + mes + ")";
        try {
            ResultSet rs = Util.executeQuery(query);
            hechosCertificos = recuperarHechosCertifico(rs);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hechosCertificos;
    }

    @Override
    public LinkedList<Hechos> obtenerHechosParaUnidadOrganizativaPorMesYAnno(int mes, int anno, int uorg) {
        String query = "Select * From hechos_por_uorg_mes_anno(" + uorg + "," + anno + "," + mes + ")";
        return recuperarListaResultSet(query);
    }

    @Override
    public LinkedList<Hechos> obtenerHechosResumenMincom(int anno, int mes) {
        String query = "Select * from obtener_hechos_para_resumen_mincom(" + anno + "," + mes + ")";
        return recuperarListaResultSet(query);
    }

    @Override
    public LinkedList<HechosPrevenidos> obtenerHechosPrevenidos(int year) {
        LinkedList<HechosPrevenidos> prevenidos = new LinkedList<>();
        String query = "Select * from obtener_cantidad_hechos_prevenidos(" + year + ")";
        try {
            ResultSet rs = Util.executeQuery(query);
            prevenidos = recuperarHechosPrevenidos(rs);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prevenidos;
    }

    @Override
    public LinkedList<Hechos> obtenerHechosDatosPendientes(int year) {
        String query = "Select * FROM obtener_hechos_denuncia_perdidas(" + year + ")";
        return recuperarListaResultSet(query);
    }

    @Override
    public AfectacionesServiciosAfectados obtenerAfectacionServicio(Date inicio, Date fin, int tipoHecho) {
        AfectacionesServiciosAfectados afectado = new AfectacionesServiciosAfectados();
        String function = "{call obtener_afectaciones_rango_fecha(?,?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, inicio);
            statement.setDate(2, fin);
            statement.setInt(3, tipoHecho);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            afectado = recuperarAfectaciones(rs);
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return afectado;
    }


    @Override
    public LinkedList<CantidadDelitoRangoFecha> obtenerCantidadDelitoRangoFecha(Date inicio, Date fin) {
        LinkedList<CantidadDelitoRangoFecha> delitoRangoFechas = new LinkedList<>();
        String function = "{call obtener_cant_hechos_delictivos_rango_fecha(?,?)}";
        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, inicio);
            statement.setDate(2, fin);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            delitoRangoFechas = recuperarDelitoRangoFecha(rs);
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return delitoRangoFechas;
    }

    @Override
    public LinkedList<Hechos> obtenerHechosByTypeAndDate(double anno, int mes, int tipoHecho) {
        String query = " Select * from obtener_cant_hechos__mes_o_anno(" + anno + "," + mes + "," + tipoHecho + ")";
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
            ResultSet resultset = Util.executeQuery(query);
            while (resultset.next()) {
                hechos.add(recuperarResultSetHechos(resultset));
            }
            resultset.close();
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


    private LinkedList<HurtosRobosPrevUorg> recuperarHechosHurtosRobosPrevUorg(ResultSet set) {
        LinkedList<HurtosRobosPrevUorg> hechos = new LinkedList<>();
        try {
            while (set.next()) {
                hechos.add(new HurtosRobosPrevUorg(
                        set.getString(1),
                        set.getInt(2),
                        set.getInt(3),
                        set.getInt(4)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hechos;
    }

    public void editarHechos(Hechos hechos) throws SQLException {
        String function = "{call editar_hechos(?,?,?,?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, hechos.getId_reg());
        statement.setString(2, hechos.getTitulo());
        statement.setString(3, hechos.getCentro());
        statement.setString(4, hechos.getLugar());
        statement.execute();
        statement.close();
    }

    public void eliminarHechos(Hechos hechos) throws SQLException {
        String function = "{call eliminar_hechos(?)}";
        CallableStatement statement = Conexion.getConnection().prepareCall(function);
        statement.setInt(1, hechos.getId_reg());
        statement.execute();
        statement.close();
    }

    @Override
    public Hechos getHechoByUOandFechaOcurrenciaAndTitulo(int id_uorg, Date fecha, String title) {
        Hechos hecho = new Hechos();
        String query = "SELECT * FROM hechos WHERE id_uorg = " + id_uorg +
                " AND titulo = '" + title + "' AND fecha_ocurrencia = '" + fecha + "'";
        try {
            ResultSet resultSet = Util.executeQuery(query);
            if (resultSet.next())
                hecho = recuperarResultSetHechos(resultSet);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hecho;
    }

    @Override
    public List<HechosEsclarecimientoPExtTPub> obtenerEsclarecimientoPExtDateRange(Date inicio, Date fin) {
        List<HechosEsclarecimientoPExtTPub> hechos = new LinkedList<>();
        String function = "{call obtener_hechos_esclarecimiento_pext(?,?)}";

        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, inicio);
            statement.setDate(2, fin);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            hechos = recuperarRSEsclarecimientoPExtTPub(rs);
            statement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hechos;
    }

    @Override
    public List<HechosEsclarecimientoPExtTPub> obtenerEsclarecimientoTPubDateRange(Date inicio, Date fin) {
        List<HechosEsclarecimientoPExtTPub> hechos = new LinkedList<>();
        String function = "{call obtener_hechos_esclarecimiento_tpub(?,?)}";

        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, inicio);
            statement.setDate(2, fin);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            hechos = recuperarRSEsclarecimientoPExtTPub(rs);
            statement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hechos;
    }

    private List<HechosEsclarecimientoPExtTPub> recuperarRSEsclarecimientoPExtTPub(ResultSet rs) {
        List<HechosEsclarecimientoPExtTPub> lista = new LinkedList<>();

        try {
            while (rs.next()) {
                lista.add(
                        new HechosEsclarecimientoPExtTPub(
                                rs.getString(1),
                                rs.getDate(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getDouble(5),
                                rs.getDouble(6),
                                rs.getString(7),
                                rs.getString(8),
                                rs.getBoolean(9),
                                rs.getBoolean(10),
                                rs.getBoolean(11),
                                rs.getBoolean(12),
                                rs.getBoolean(13),
                                rs.getBoolean(14),
                                rs.getBoolean(15),
                                rs.getBoolean(16),
                                rs.getBoolean(17),
                                rs.getBoolean(18),
                                rs.getInt(19),
                                rs.getString(20)
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<HechosEsclarecimientoResumen> obtenerEsclarecimientoResumenDateRange(Date inicio, Date fin) {
        List<HechosEsclarecimientoResumen> listaResumen = new LinkedList<>();
        String function = "{call obtener_resumen_hechos_esclarecidos_o_no_por_uorg(?,?)}";

        try {
            CallableStatement statement = Conexion.getConnection().prepareCall(function);
            statement.setDate(1, inicio);
            statement.setDate(2, fin);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            listaResumen = recuperarRSEsclarecimientoResumen(rs);
            statement.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaResumen;
    }

    private List<HechosEsclarecimientoResumen> recuperarRSEsclarecimientoResumen(ResultSet rs) {
        LinkedList<HechosEsclarecimientoResumen> lista = new LinkedList<>();
        List<HechosEsclarecimientoResumen> listToRetturn = new LinkedList<>();

        boolean salidaRs = false;

        try {
            while (rs.next()) {
                HechosEsclarecimientoResumen h =
                        new HechosEsclarecimientoResumen(
                                rs.getString(1),
                                rs.getInt(2),
                                rs.getInt(3),
                                rs.getInt(4),
                                rs.getInt(5),
                                rs.getInt(6),
                                rs.getInt(7),
                                rs.getInt(8),
                                rs.getInt(9),
                                rs.getInt(10),
                                rs.getInt(11),
                                rs.getInt(12),
                                rs.getInt(13),
                                rs.getInt(14),
                                rs.getInt(15),
                                rs.getInt(16),
                                rs.getInt(17),
                                rs.getInt(18),
                                rs.getInt(19),
                                rs.getInt(20),
                                rs.getInt(21),
                                rs.getInt(22)
                        );
                lista.add(h);
            }
            HechosEsclarecimientoResumen dvlh = new HechosEsclarecimientoResumen();
            dvlh.setUnidad_organizativa("DVLH");
            int i = 0;

            while (i < lista.size()) {
                if (lista.get(i).getUnidad_organizativa().equalsIgnoreCase("dtno")
                        || lista.get(i).getUnidad_organizativa().equalsIgnoreCase("dtes")
                        || lista.get(i).getUnidad_organizativa().equalsIgnoreCase("dtoe")
                        || lista.get(i).getUnidad_organizativa().equalsIgnoreCase("dtsr")
                ) {
                    this.obtenerDatosParaDVLH(dvlh, lista.get(i));
                }
                i++;
            }

            lista.add(dvlh);

            listToRetturn = lista.stream().filter(p -> !p.getUnidad_organizativa().equalsIgnoreCase("dtno"))
                    .filter(p -> !p.getUnidad_organizativa().equalsIgnoreCase("dtes"))
                    .filter(p -> !p.getUnidad_organizativa().equalsIgnoreCase("dtsr"))
                    .filter(p -> !p.getUnidad_organizativa().equalsIgnoreCase("dtoe"))
                    .collect(Collectors.toList());


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listToRetturn;
    }

    private void obtenerDatosParaDVLH(HechosEsclarecimientoResumen unidDVLH, HechosEsclarecimientoResumen unidad) {

        unidDVLH.setTotal_conciliados(unidad.getTotal_conciliados() + unidDVLH.getTotal_conciliados());
        unidDVLH.setCant_pext(unidad.getCant_pext() + unidDVLH.getCant_pext());
        unidDVLH.setCant_tpub(unidad.getCant_tpub() + unidDVLH.getCant_tpub());
        unidDVLH.setTotal_no_esclarecidos(unidad.getTotal_no_esclarecidos() + unidDVLH.getTotal_no_esclarecidos());
        unidDVLH.setCant_pext_no_esc(unidad.getCant_pext_no_esc() + unidDVLH.getCant_pext_no_esc());
        unidDVLH.setCant_tpub_no_esc(unidad.getCant_tpub_no_esc() + unidDVLH.getCant_tpub_no_esc());
        unidDVLH.setCant_exp_sac(unidad.getCant_exp_sac() + unidDVLH.getCant_exp_sac());
        unidDVLH.setCant_sin_denuncia(unidad.getCant_sin_denuncia() + unidDVLH.getCant_sin_denuncia());
        unidDVLH.setTotal_esclarecidos(unidad.getTotal_esclarecidos() + unidDVLH.getTotal_esclarecidos());
        unidDVLH.setCant_pext_esc(unidad.getCant_pext_esc() + unidDVLH.getCant_pext_esc());
        unidDVLH.setCant_tpub_esc(unidad.getCant_tpub_esc() + unidDVLH.getCant_tpub_esc());
        unidDVLH.setCant_art_83(unidad.getCant_art_83() + unidDVLH.getCant_art_83());
        unidDVLH.setCant_art_82(unidad.getCant_art_82() + unidDVLH.getCant_art_82());
        unidDVLH.setCant_med_admin(unidad.getCant_med_admin() + unidDVLH.getCant_med_admin());
        unidDVLH.setCant_menor(unidad.getCant_menor() + unidDVLH.getCant_menor());
        unidDVLH.setCant_fase_prep(unidad.getCant_fase_prep() + unidDVLH.getCant_fase_prep());
        unidDVLH.setCant_pend_desp(unidad.getCant_pend_desp() + unidDVLH.getCant_pend_desp());
        unidDVLH.setCant_pend_juicio(unidad.getCant_pend_juicio() + unidDVLH.getCant_pend_juicio());
        unidDVLH.setCant_casos(unidad.getCant_casos() + unidDVLH.getCant_casos());
        unidDVLH.setCant_sanciones(unidad.getCant_sanciones() + unidDVLH.getCant_sanciones());
        unidDVLH.setSentencias(unidad.getSentencias() + unidDVLH.getSentencias());

    }

}
