package util;

import com.gembox.spreadsheet.*;
import javafx.scene.control.Alert;
import models.Afectaciones;
import models.HechosPorMunicipio;
import models.MunicipioServiciosAfectados;
import models.ResumenModels;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author Humberto Cabrera Dominguez
 */
public class Util {

    public static final String[] meses = {"Ene", "Feb", "Mar", "Abr", "May"
            , "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
    public static final String[] meses2 = {"Enero", "Febrero", "Marzo", "Abril", "Mayo"
            , "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    public static final String[] unidades_organizativa = {"DTPR", "DTAR", "DTMY",
            "DVLH", "DTIJ", "DTMZ", "DTCF", "DTVC", "DTSS", "DTCA", "DTCM", "DTLT", "DTHO", "DTGR", "DTSC", "DTGT"};

    public static ResultSet executeQuery(String sql) throws SQLException {

        Statement statement = Conexion.getConnection().createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        ResultSet resultSet = statement.executeQuery(sql);
        Conexion.getConnection().close();

        return resultSet;
    }

    public static String replaceWhiteSpacesByPercent20(String string) {
        string = string.replaceAll(" ", "%20");
        return string;
    }

    public static int count(String sql) {
        int cantidadHechos = 0;
        try {
            var resultset = Util.executeQuery(sql);
            if (resultset.next()) {
                cantidadHechos = resultset.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cantidadHechos;
    }

    public static String renombrarPath(String path) {
        return path.replaceAll("file:", "");
    }

    public static int countCant(LinkedList<HechosPorMunicipio> hechosPorMunicipios) {
        int cant = 0;
        for (HechosPorMunicipio hechosMunicipio : hechosPorMunicipios) {
            cant += hechosMunicipio.getCantHechos();
        }
        return cant;
    }

    public static int countCantServiciosAfectados(LinkedList<MunicipioServiciosAfectados> hechosPorMunicipios) {
        int cant = 0;
        for (MunicipioServiciosAfectados hechosMunicipio : hechosPorMunicipios) {
            cant += hechosMunicipio.getCant_servicios();
        }
        return cant;
    }

    public static int cantPextTpubTotal(LinkedList<ResumenModels> resumenModels) {
        int cant = 0;
        for (ResumenModels models : resumenModels) {
            cant += models.getCantHechos();
        }

        return cant;
    }

    public static int cantServiciosAfectadosTPub(LinkedList<ResumenModels> resumenModels) {
        int cant = 0;
        for (ResumenModels models : resumenModels) {
            cant += models.getServiciosAfectados();
        }
        return cant;
    }

    public static String formatedLetter(CellRange range) {
        //"Resumen!$A$80:$B$103";
        String value = "";
        String nameHoja = range.getWorksheet().getName();
        String inicialPosition = range.getStartPosition();
        String letraInicial = inicialPosition.substring(0, 1);
        String numeroInicial = inicialPosition.substring(1);

        String finalPosition = range.getEndPosition();
        String letraFinal = finalPosition.substring(0, 1);
        String numeroFinal = finalPosition.substring(1);

        value = "'" + nameHoja + "'" + "!$" + letraInicial + "$" + numeroInicial + ":$" + letraFinal + "$" + numeroFinal;

        return value;
    }

    //Hacer funcion para recuperar los datos de manera tocada
    public static LinkedList<HechosPorMunicipio> ordenandoHechosPorMunicipio(LinkedList<HechosPorMunicipio> hechosPorMunicipios) {
        LinkedList<HechosPorMunicipio> hechosdeUno = new LinkedList<>();
        LinkedList<HechosPorMunicipio> hechosDeDos = new LinkedList<>();
        LinkedList<HechosPorMunicipio> hechosRetorno = new LinkedList<>();

        hechosdeUno = hechosPorMunicipios.stream().filter(
                hechosPorMunicipio -> hechosPorMunicipio.getCantHechos() == 1
        ).collect(Collectors.toCollection(LinkedList::new));

        hechosDeDos = hechosPorMunicipios.stream().filter(
                hechosPorMunicipio -> hechosPorMunicipio.getCantHechos() == 2
        ).collect(Collectors.toCollection(LinkedList::new));

        hechosRetorno = hechosPorMunicipios.stream().filter(
                hechosPorMunicipio -> hechosPorMunicipio.getCantHechos() > 2
        ).collect(Collectors.toCollection(LinkedList::new));

        hechosRetorno.add(hechoModified(hechosDeDos));
        hechosRetorno.add(hechoModified(hechosdeUno));

        return hechosRetorno;
    }


    public static LinkedList<MunicipioServiciosAfectados> ordenandoHechosPorMunicipioServiciosAfectados(LinkedList<MunicipioServiciosAfectados> hechosPorMunicipios) {
        LinkedList<MunicipioServiciosAfectados> hechosdeUno = new LinkedList<>();
        LinkedList<MunicipioServiciosAfectados> hechosDeDos = new LinkedList<>();
        LinkedList<MunicipioServiciosAfectados> hechosRetorno = new LinkedList<>();

        hechosdeUno = hechosPorMunicipios.stream().filter(
                hechosPorMunicipio -> hechosPorMunicipio.getCant_servicios() == 1
        ).collect(Collectors.toCollection(LinkedList::new));

        hechosDeDos = hechosPorMunicipios.stream().filter(
                hechosPorMunicipio -> hechosPorMunicipio.getCant_servicios() == 2
        ).collect(Collectors.toCollection(LinkedList::new));

        hechosRetorno = hechosPorMunicipios.stream().filter(
                hechosPorMunicipio -> hechosPorMunicipio.getCant_servicios() > 2
        ).collect(Collectors.toCollection(LinkedList::new));

        hechosRetorno.add(hechoModifiedServiciosAfectados(hechosDeDos));
        hechosRetorno.add(hechoModifiedServiciosAfectados(hechosdeUno));

        return hechosRetorno;
    }

    private static HechosPorMunicipio hechoModified(LinkedList<HechosPorMunicipio> hechosPorMunicipios) {

        int cantNombre = hechosPorMunicipios.size();
        int cantHechos = hechosPorMunicipios.getFirst().getCantHechos();

        return new HechosPorMunicipio(cantNombre + " Municipios", cantHechos);
    }


    private static MunicipioServiciosAfectados hechoModifiedServiciosAfectados(LinkedList<MunicipioServiciosAfectados> hechosPorMunicipios) {
        try {
            int cantNombre = hechosPorMunicipios.size();
            Double cantHechos = hechosPorMunicipios.getFirst().getCant_servicios();

            return new MunicipioServiciosAfectados(cantNombre + " Municipios", cantHechos);
        } catch (NoSuchElementException e) {
            return null;
        }
    }


    public static LinkedList<Afectaciones> reordenandoAfectaciones(LinkedList<Afectaciones> afectacionesLista) {

        LinkedList<Afectaciones> hechosMenores050 = afectacionesLista.stream().filter(
                afectaciones -> afectaciones.getServiciosAfectadosVSEstacionesPublicas() <= 0.5
        ).collect(Collectors.toCollection(LinkedList::new));

        LinkedList<Afectaciones> hechosMenores1 = afectacionesLista.stream().filter(
                afectaciones -> afectaciones.getServiciosAfectadosVSEstacionesPublicas() > 0.5
                        && afectaciones.getServiciosAfectadosVSEstacionesPublicas() <= 1
        ).collect(Collectors.toCollection(LinkedList::new));

        LinkedList<Afectaciones> hechosMenores150 = afectacionesLista.stream().filter(
                afectaciones -> afectaciones.getServiciosAfectadosVSEstacionesPublicas() > 1
                        && afectaciones.getServiciosAfectadosVSEstacionesPublicas() <= 1.5
        ).collect(Collectors.toCollection(LinkedList::new));

        LinkedList<Afectaciones> retorno = afectacionesLista.stream().filter(
                afectaciones -> afectaciones.getServiciosAfectadosVSEstacionesPublicas() > 1.5
        ).collect(Collectors.toCollection(LinkedList::new));

        retorno.add(afectacionesModified(hechosMenores150));
        retorno.add(afectacionesModified(hechosMenores1));
        retorno.add(afectacionesModified(hechosMenores050));

        return retorno;
    }


    private static Afectaciones afectacionesModified(LinkedList<Afectaciones> afectacionesLinkedList) {
        int cantNombre = afectacionesLinkedList.size();
        double serviciosAfectacion = 0.0;
        try {
            serviciosAfectacion = afectacionesLinkedList.getFirst().getServiciosAfectadosVSEstacionesPublicas();
        } catch (NoSuchElementException e) {

        }
        if (serviciosAfectacion <= 0.50) {
            serviciosAfectacion = 0.5;
        } else if (serviciosAfectacion <= 1.0) {
            serviciosAfectacion = 1.0;
        } else {
            serviciosAfectacion = 1.50;
        }
        return new Afectaciones(cantNombre + " Municipios <=", serviciosAfectacion);
    }

    public static void dialogResult(String result, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(result);
        alert.setTitle(result);
        alert.showAndWait();
    }

    public static void showDialog(boolean result) {
        if (result) {
            Util.dialogResult("Exito", Alert.AlertType.INFORMATION);
        } else {
            Util.dialogResult("Fallo", Alert.AlertType.ERROR);
        }
    }

    public static CellStyle estiloColumnasHojasTotales() {
        CellStyle style = new CellStyle();
        style.setHorizontalAlignment(HorizontalAlignmentStyle.CENTER);
        style.setVerticalAlignment(VerticalAlignmentStyle.CENTER);
        style.getFont().setName("Calibri");
        style.getFont().setColor(SpreadsheetColor.fromName(ColorName.BLACK));
        style.setWrapText(true);
        style.getFillPattern().setSolid(SpreadsheetColor.fromArgb(161, 214, 214));
        style.getBorders().setBorders(EnumSet.of(MultipleBorders.RIGHT, MultipleBorders.TOP, MultipleBorders.LEFT, MultipleBorders.BOTTOM)
                , SpreadsheetColor.fromColor(Color.BLACK), LineStyle.THIN);

        return style;
    }

    public static CellStyle generarStilo() {
        CellStyle style = new CellStyle();
        style.setHorizontalAlignment(HorizontalAlignmentStyle.CENTER);
        style.setVerticalAlignment(VerticalAlignmentStyle.CENTER);
        style.getFont().setName("Calibri");
        style.getFont().setColor(SpreadsheetColor.fromName(ColorName.WHITE));
        style.setWrapText(true);
        style.getFillPattern().setSolid(SpreadsheetColor.fromName(ColorName.DARK_BLUE));
        style.getBorders().setBorders(EnumSet.of(MultipleBorders.RIGHT, MultipleBorders.TOP, MultipleBorders.LEFT, MultipleBorders.BOTTOM)
                , SpreadsheetColor.fromColor(Color.WHITE), LineStyle.THIN);

        return style;
    }

    public static CellStyle generarBordes() {
        CellStyle style = new CellStyle();
        style.setWrapText(true);
        style.setHorizontalAlignment(HorizontalAlignmentStyle.CENTER);
        style.setVerticalAlignment(VerticalAlignmentStyle.CENTER);
        style.getBorders().setBorders(EnumSet.of(MultipleBorders.RIGHT, MultipleBorders.TOP, MultipleBorders.LEFT, MultipleBorders.BOTTOM)
                , SpreadsheetColor.fromColor(Color.BLACK), LineStyle.THIN);
        return style;
    }


}
