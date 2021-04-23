package informes_generate;


import com.gembox.spreadsheet.*;
import models.*;
import services.ServiceLocator;
import util.Util;
import util.UtilToGenerateTables;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

import static informes_generate.GeneradoresTablas.generarTablasParaGraficos;
import static informes_generate.GraphicGenerator.*;
import static util.Util.*;


public class GenerateInformeFiscaliaImpl implements GenerateInformeFiscalia {


    private LinkedList<CellRange> cellRanges;

    public static ArrayList<Integer> executingConsult(LocalDate date) {
        int actualPext = ServiceLocator.getHechosService().cantHechosPextCierreFiscalia(Date.valueOf(date), 1);
        int anteriorPext = ServiceLocator.getHechosService().cantHechosPextCierreFiscalia(Date.valueOf(date.minusYears(1)), 1);
        int actualTPub = ServiceLocator.getHechosService().cantHechosPextCierreFiscalia(Date.valueOf(date), 2);
        int anteriorTPub = ServiceLocator.getHechosService().cantHechosPextCierreFiscalia(Date.valueOf(date.minusYears(1)), 2);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(anteriorPext);
        list.add(actualPext);
        list.add(anteriorTPub);
        list.add(actualTPub);
        return list;
    }

    @Override
    public boolean generarInformeCompleto(LocalDate localDate) {
        ExcelFile workbook = new ExcelFile();
        String DIRECCION_ARCHIVO = "src/informesGenerados/ResumenFGR.xlsx";
        boolean result = false;
        if (generarTotales(localDate, workbook)
                && generarResumen(localDate, workbook)
                && generarEsclarecimiento(localDate, workbook)
                && generarGraficoPext(localDate, workbook)
                && generarGraficoTPubl(localDate, workbook)
        ) {
            try {
                workbook.save(DIRECCION_ARCHIVO);
                result = true;
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean generarTotales(LocalDate localDate, ExcelFile workbook) {

        ExcelWorksheet hojaTotales = workbook.addWorksheet("Totales");
        return GeneradoresTablas.generarTotales(localDate, hojaTotales, executingConsult(localDate));

    }

    private boolean generarEsclarecimiento(LocalDate localDate, ExcelFile workbook) {

        ExcelWorksheet worksheet = workbook.addWorksheet("Esclarecimiento");

        modificarCell("Dirección Territorial", worksheet.getCells().getSubrange("A1:A2"));
        modificarCell("TOTAL DE HECHOS", worksheet.getCells().getSubrange("B1:D1"));
        modificarCell("HECHOS SIN ESCLARECER", worksheet.getCells().getSubrange("E1:G1"));
        modificarCell("HECHOS ESCLARECIDOS", worksheet.getCells().getSubrange("H1:J1"));
        modificarCell("Total Conciliados", worksheet.getCell("B2"));

        modificarCell("PExt", worksheet.getCell("C2"));
        modificarCell("TPúb", worksheet.getCell("D2"));
        modificarCell("Total sin Esclarecer", worksheet.getCell("E2"));
        modificarCell("PExt", worksheet.getCell("F2"));
        modificarCell("TPúb", worksheet.getCell("G2"));
        modificarCell("Total esclarecidos", worksheet.getCell("H2"));
        modificarCell("PExt", worksheet.getCell("I2"));
        modificarCell("TPúb", worksheet.getCell("J2"));

        for (int row = 2; row < 18; row++) {
            for (int column = 0; column < 10; column++) {
                worksheet.getCell(row, column).setValue(0);
                if (column == 1 || column == 4 || column == 7) {
                    worksheet.getCell(row, column).setStyle(estiloColumnasHojasTotales());
                } else {
                    worksheet.getCell(row, column).setStyle(generarBordes());
                }
            }
        }

        modificarCell("Totales:", worksheet.getCell("A19"));

        String[] letras = {"B", "C", "D", "E", "F", "G", "H", "I", "J"};


        for (int column = 1; column < 10; column++) {
            modificarCell(
                    0,
                    worksheet.getCell(18, column)
            );
        }

        worksheet.getCell("B19").setFormula("=SUM(B3:B18)");
        worksheet.getCell("B19").calculate();
        worksheet.getCell("C19").setFormula("=SUM(C3:C18)");
        worksheet.getCell("C19").calculate();
        worksheet.getCell("D19").setFormula("=SUM(D3:D18)");
        worksheet.getCell("D19").calculate();
        worksheet.getCell("E19").setFormula("=SUM(E3:E18)");
        worksheet.getCell("E19").calculate();
        worksheet.getCell("F19").setFormula("=SUM(F3:F18)");
        worksheet.getCell("F19").calculate();
        worksheet.getCell("G19").setFormula("=SUM(G3:G18)");
        worksheet.getCell("G19").calculate();
        worksheet.getCell("H19").setFormula("=SUM(H3:H18)");
        worksheet.getCell("H19").calculate();
        worksheet.getCell("I19").setFormula("=SUM(I3:I18)");
        worksheet.getCell("I19").calculate();
        worksheet.getCell("J19").setFormula("=SUM(J3:J18)");
        worksheet.getCell("J19").calculate();

        worksheet.getCell("A3").setValue("DTPR");
        worksheet.getCell("A4").setValue("DTAR");
        worksheet.getCell("A5").setValue("DTMY");
        worksheet.getCell("A6").setValue("DVLH");
        worksheet.getCell("A7").setValue("DTIJ");
        worksheet.getCell("A8").setValue("DTMZ");
        worksheet.getCell("A9").setValue("DTCF");
        worksheet.getCell("A10").setValue("DTVC");
        worksheet.getCell("A11").setValue("DTSS");
        worksheet.getCell("A12").setValue("DTCA");
        worksheet.getCell("A13").setValue("DTCM");
        worksheet.getCell("A14").setValue("DTLT");
        worksheet.getCell("A15").setValue("DTHO");
        worksheet.getCell("A16").setValue("DTGR");
        worksheet.getCell("A17").setValue("DTSC");
        worksheet.getCell("A18").setValue("DTGT");

        worksheet.getColumn("B").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
        worksheet.getColumn("E").setWidth(11, LengthUnit.ZERO_CHARACTER_WIDTH);
        worksheet.getColumn("H").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);

        ArrayList<String> listaLetrasBD = new ArrayList<>();
        for (int row = 3; row < 19; row++) {
            listaLetrasBD.add("B" + row);
            listaLetrasBD.add("C" + row);
            listaLetrasBD.add("D" + row);
        }

        ArrayList<String> listaLetrasEG = new ArrayList<>();
        for (int row = 3; row < 19; row++) {
            listaLetrasEG.add("E" + row);
            listaLetrasEG.add("F" + row);
            listaLetrasEG.add("G" + row);
        }
        ArrayList<String> listaLetrasHJ = new ArrayList<>();
        for (int row = 3; row < 19; row++) {
            listaLetrasHJ.add("H" + row);
            listaLetrasHJ.add("I" + row);
            listaLetrasHJ.add("J" + row);
        }

        this.llenarEsclarecimiento(
                ServiceLocator.getHechosService().getEsclarecimientoConciliados(Date.valueOf(localDate)),
                worksheet,
                listaLetrasBD
        );

        this.llenarEsclarecimiento(
                ServiceLocator.getHechosService().getEsclaremientoConciliados(Date.valueOf(localDate), true),
                worksheet,
                listaLetrasHJ
        );

        this.llenarEsclarecimiento(
                ServiceLocator.getHechosService().getEsclaremientoConciliados(Date.valueOf(localDate), false),
                worksheet,
                listaLetrasEG
        );

        return true;
    }

    private boolean generarGraficoPext(LocalDate date, ExcelFile workbook) {
        cellRanges = generarTablasParaGraficos(workbook.getWorksheet(0), date, 7);
        ExcelWorksheet worksheet = workbook.addWorksheet("Grafico Pext");
        generarGraficoPastel(cellRanges.get(0), worksheet, "B17", "J37", "PExt - Cantidad de Hechos por Material Afectado(" + date.minusYears(1).getYear() + ")", 1);
        generarGraficoPastel(cellRanges.get(1), worksheet, "L17", "T37", "PExt - Cantidad de Hechos por Material Afectado(" + date.getYear() + ")", 1);

        generarGraficoPastel(cellRanges.get(0), worksheet, "B39", "J59", "PExt - Cantidad de Material Afectado(" + date.minusYears(1).getYear() + ")", 0);
        generarGraficoPastel(cellRanges.get(1), worksheet, "L39", "T59", "PExt - Cantidad de Material Afectado(" + date.getYear() + ")", 0);
        generarGraficoLineas(cellRanges.get(2), worksheet, "B61", "M81", "Cantidad de Hechos por Mes y Año", true);
        generarGraficoBarrasDobles("PExt - Cantidad de hechos", "B2", "M15", worksheet, date,
                workbook.getWorksheet(1).getCells().getSubrange("B4:B19"),
                workbook.getWorksheet(1).getCells().getSubrange("E4:E19"),
                workbook.getWorksheet(1).getCells().getSubrange("A4:A19"),
                Util.
                        cantPextTpubTotal
                                (ServiceLocator.getHechosService().cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre
                                        (Date.valueOf(date.minusYears(1)), 1)),
                Util.cantPextTpubTotal(
                        ServiceLocator.getHechosService().cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre(Date.valueOf(date), 1)
                )
        );


        generarGraficoBarras(cellRanges.get(3), worksheet, "O61", "U81", null, true);


        return true;
    }

    private boolean generarGraficoTPubl(LocalDate date, ExcelFile workbook) {

        LinkedList<ResumenModels> resumenModelsAnnoAnterior = ServiceLocator.getHechosService().cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre(
                Date.valueOf(date.minusYears(1)), 2
        );
        LinkedList<ResumenModels> resumenModelsAnnoActual = ServiceLocator.getHechosService().cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre(
                Date.valueOf(date), 2
        );
        ExcelWorksheet sheet = workbook.addWorksheet("Graficos TPub");
        generarGraficoBarrasDobles("TPub Hechos x Territorios (" + date.minusYears(1).getYear() + "-" + date.getYear() + ")",
                "B2", "K15", sheet, date,
                workbook.getWorksheet(1).getCells().getSubrange("B22:B37"),
                workbook.getWorksheet(1).getCells().getSubrange("E22:E37"),
                workbook.getWorksheet(1).getCells().getSubrange("A22:A37"),
                Util.cantPextTpubTotal(resumenModelsAnnoAnterior),
                Util.cantPextTpubTotal(resumenModelsAnnoActual)
        );
        generarGraficoBarrasDobles("TPub Servicios Afectados x Territorios (" + date.minusYears(1).getYear() + "-" + date.getYear() + ")", "M2", "V15", sheet, date,
                workbook.getWorksheet(1).getCells().getSubrange("C22:C37"),
                workbook.getWorksheet(1).getCells().getSubrange("F22:F37"),
                workbook.getWorksheet(1).getCells().getSubrange("A22:A37"),
                Util.cantServiciosAfectadosTPub(resumenModelsAnnoAnterior),
                Util.cantServiciosAfectadosTPub(resumenModelsAnnoActual)
        );

        generarGraficoPastel(cellRanges.get(4),
                sheet, "B17", "J37", "TPub Hechos por Tipo de Afectacion (" + date.minusYears(1).getYear() + ")", 1);
        generarGraficoPastel(cellRanges.get(5),
                sheet, "L17", "T37", "TPub Hechos por Tipo de Afectacion (" + date.getYear() + ")", 1);
        generarGraficoLineas(cellRanges.get(6), sheet, "B39", "M59", null, true);
        generarGraficoBarrasNormal(null, "O39", "V59", sheet, date, cellRanges.get(7));

        generarGraficoBarras(cellRanges.get(8), sheet, "B61", "H76", null, true);
        generarGraficoBarras(cellRanges.get(9), sheet, "J61", "P76", null, true);

        return true;
    }

    private boolean generarResumen(LocalDate date, ExcelFile workbook) {

        ArrayList<String> letrasListPextAnnoAnterior = new ArrayList<>();
        for (int i = 4; i < 20; i++) {
            letrasListPextAnnoAnterior.add("B" + i);
            letrasListPextAnnoAnterior.add("C" + i);
            letrasListPextAnnoAnterior.add("D" + i);
        }

        ArrayList<String> letrasListPextAnnoActual = new ArrayList<>();
        for (int i = 4; i < 20; i++) {
            letrasListPextAnnoActual.add("E" + i);
            letrasListPextAnnoActual.add("F" + i);
            letrasListPextAnnoActual.add("G" + i);
        }

        ArrayList<String> letrasListTpubAnnoAnterior = new ArrayList<>();
        for (int i = 22; i < 38; i++) {
            letrasListTpubAnnoAnterior.add("B" + i);
            letrasListTpubAnnoAnterior.add("C" + i);
            letrasListTpubAnnoAnterior.add("D" + i);
        }

        ArrayList<String> letrasListTpubAnnoActual = new ArrayList<>();
        for (int i = 22; i < 38; i++) {
            letrasListTpubAnnoActual.add("E" + i);
            letrasListTpubAnnoActual.add("F" + i);
            letrasListTpubAnnoActual.add("G" + i);
        }

        ExcelWorksheet worksheet = workbook.addWorksheet("Resumen");
        worksheet.getCells().getSubrange("A1:A2").setStyle(generarStilo());
        worksheet.getCells().getSubrange("A1:A2").setMerged(true);
        worksheet.getCells().get("A1").setValue("División Territorial");

        CellRange cellsB1D1 = worksheet.getCells().getSubrange("B1:D1");
        cellsB1D1.setMerged(true);
        cellsB1D1.setStyle(generarStilo());
        worksheet.getCell("B1").setValue(date.minusYears(1).getYear());
        worksheet.getCell("B2").setValue("Cantidad Hechos");
        worksheet.getCell("B2").setStyle(generarStilo());
        worksheet.getCell("C2").setValue("Servicios Afectados");
        worksheet.getCell("C2").setStyle(generarStilo());
        worksheet.getCell("D2").setValue("Pérdidas Económicas");
        worksheet.getCell("D2").setStyle(generarStilo());

        CellRange cellsE1G1 = worksheet.getCells().getSubrange("E1:G1");
        cellsE1G1.setMerged(true);
        cellsE1G1.setStyle(generarStilo());
        worksheet.getCell("E1").setValue(date.getYear());
        worksheet.getCell("E2").setValue("Cantidad Hechos");
        worksheet.getCell("E2").setStyle(generarStilo());
        worksheet.getCell("F2").setValue("Servicios Afectados");
        worksheet.getCell("F2").setStyle(generarStilo());
        worksheet.getCell("G2").setValue("Pérdidas Económicas");
        worksheet.getCell("G2").setStyle(generarStilo());

        CellRange rangoFila3 = worksheet.getCells().getSubrange("A3:G3");
        rangoFila3.setMerged(true);
        CellStyle stilo = generarStilo();
        stilo.getFillPattern().setSolid(SpreadsheetColor.fromName(ColorName.DARK_RED));
        rangoFila3.setStyle(stilo);
        worksheet.getCell("A3").setValue("Planta Exterior");

        for (int row = 3; row < 37; row++) {
            for (int column = 0; column < 7; column++) {
                if (row != 20) {
                    worksheet.getCell(row, column).setValue(0);
                    if (column > 3) {
                        worksheet.getCell(row, column).setStyle(estiloColumnasHojasTotales());
                    } else {
                        worksheet.getCell(row, column).setStyle(generarBordes());
                    }
                }
            }
        }
        for (int x = 0; x < 7; x++) {
            worksheet.getCell(19, x).setStyle(generarStilo());
        }
        //Formando las celdas
        worksheet.getCell("A4").setValue("DTPR");
        worksheet.getCell("A5").setValue("DTAR");
        worksheet.getCell("A6").setValue("DTMY");
        worksheet.getCell("A7").setValue("DVLH");
        worksheet.getCell("A8").setValue("DTIJ");
        worksheet.getCell("A9").setValue("DTMZ");
        worksheet.getCell("A10").setValue("DTCF");
        worksheet.getCell("A11").setValue("DTVC");
        worksheet.getCell("A12").setValue("DTSS");
        worksheet.getCell("A13").setValue("DTCA");
        worksheet.getCell("A14").setValue("DTCM");
        worksheet.getCell("A15").setValue("DTLT");
        worksheet.getCell("A16").setValue("DTHO");
        worksheet.getCell("A17").setValue("DTGR");
        worksheet.getCell("A18").setValue("DTSC");
        worksheet.getCell("A19").setValue("DTGT");
        worksheet.getCell("A20").setValue("Total:");

        worksheet.getCell("B20").setFormula("=SUM(B4:B19)");
        worksheet.getCell("B20").calculate();
        worksheet.getCell("C20").setFormula("=SUM(C4:C19)");
        worksheet.getCell("C20").calculate();
        worksheet.getCell("D20").setFormula("=SUM(D4:D19)");
        worksheet.getCell("D20").calculate();
        worksheet.getCell("D20").getStyle().setNumberFormat("@");
        worksheet.getCell("E20").setFormula("=SUM(E4:E19)");
        worksheet.getCell("E20").calculate();
        worksheet.getCell("F20").setFormula("=SUM(F4:F19)");
        worksheet.getCell("F20").calculate();
        worksheet.getCell("G20").setFormula("=SUM(G4:G19)");
        worksheet.getCell("G20").calculate();
        worksheet.getCell("G20").getStyle().setNumberFormat("@");

        CellRange rangoA21G21 = worksheet.getCells().getSubrange("A21:G21");
        rangoA21G21.setMerged(true);
        rangoA21G21.setStyle(stilo);
        worksheet.getCell("A21").setValue("Telefonía Pública");
        //FormandoCeldas
        worksheet.getCell("A22").setValue("DTPR");
        worksheet.getCell("A23").setValue("DTAR");
        worksheet.getCell("A24").setValue("DTMY");
        worksheet.getCell("A25").setValue("DVLH");
        worksheet.getCell("A26").setValue("DTIJ");
        worksheet.getCell("A27").setValue("DTMZ");
        worksheet.getCell("A28").setValue("DTCF");
        worksheet.getCell("A29").setValue("DTVC");
        worksheet.getCell("A30").setValue("DTSS");
        worksheet.getCell("A31").setValue("DTCA");
        worksheet.getCell("A32").setValue("DTCM");
        worksheet.getCell("A33").setValue("DTLT");
        worksheet.getCell("A34").setValue("DTHO");
        worksheet.getCell("A35").setValue("DTGR");
        worksheet.getCell("A36").setValue("DTSC");
        worksheet.getCell("A37").setValue("DTGT");

        worksheet.getCell("A38").setValue("Total:");
        for (int x = 0; x < 7; x++) {
            worksheet.getCell(37, x).setStyle(generarStilo());
        }

        worksheet.getCell("B38").setFormula("=SUM(B22:B37)");
        worksheet.getCell("B38").calculate();
        worksheet.getCell("C38").setFormula("=SUM(C22:C37)");
        worksheet.getCell("C38").calculate();
        worksheet.getCell("D38").setFormula("=SUM(D22:D37)");
        worksheet.getCell("D38").calculate();
        worksheet.getCell("E38").setFormula("=SUM(E22:E37)");
        worksheet.getCell("E38").calculate();
        worksheet.getCell("F38").setFormula("=SUM(F22:F37)");
        worksheet.getCell("F38").calculate();
        worksheet.getCell("G38").setFormula("=SUM(G22:G37)");
        worksheet.getCell("G38").calculate();

        LinkedList<ResumenModels> resumenModelsPextActual = ServiceLocator.getHechosService().
                cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre(Date.valueOf(date), 1);
        LinkedList<ResumenModels> resumenModelsTPubActual = ServiceLocator.getHechosService().
                cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre(Date.valueOf(date), 2);
        LinkedList<ResumenModels> resumenModelsPextAnterrior = ServiceLocator.getHechosService().
                cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre(Date.valueOf(date.minusYears(1)), 1);
        LinkedList<ResumenModels> resumenModelsTPubAnterrior = ServiceLocator.getHechosService().
                cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre(Date.valueOf(date.minusYears(1)), 2);

        worksheet.getCells().getSubrange("D4:D38").getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);
        worksheet.getCells().getSubrange("G4:G38").getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);

        //TODO:Usar datos booleanos despues
        boolean completeLlenadoPextAnnoAnterior = this.llenarCamposPextTpu(resumenModelsPextAnterrior, worksheet, letrasListPextAnnoAnterior);
        boolean completeLlenadoPextAnnoActual = this.llenarCamposPextTpu(resumenModelsPextActual, worksheet, letrasListPextAnnoActual);
        boolean completeLlenadoTpubAnnoAnterior = this.llenarCamposPextTpu(resumenModelsTPubAnterrior, worksheet, letrasListTpubAnnoAnterior);
        boolean completeLlenadoTpubAnnoActual = this.llenarCamposPextTpu(resumenModelsTPubActual, worksheet, letrasListTpubAnnoActual);


        this.tablaMateriales(worksheet, date);
        this.tablaAfectaciones(worksheet, date);
        this.tableCantByAnnos(worksheet, date);
        this.tableTelefoniaPublica(worksheet, date);

        for (int column = 0; column < 7; column++) {
            worksheet.getColumn(column).setWidth(11.50, LengthUnit.ZERO_CHARACTER_WIDTH);
        }

        return true;
    }

    private void modificarCell(String value, CellRange rango) {
        rango.setMerged(true);
        rango.setStyle(generarStilo());
        rango.setValue(value);


    }

    private void modificarCell(Object value, ExcelCell cell) {
        cell.setValue(value);
        cell.setStyle(generarStilo());

    }

    private boolean llenarEsclarecimiento(LinkedList<EsclarecimientoHechos> esclarecimientoHechos, ExcelWorksheet sheet, ArrayList<String> letras) {
        boolean result = false;
        if (esclarecimientoHechos != null) {
            result = true;
            esclarecimientoHechos.forEach(esclarecimientoHecho -> {
                if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("DTPR")) {
                    sheet.getCell(letras.get(0)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(1)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(2)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtar")) {
                    sheet.getCell(letras.get(3)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(4)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(5)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtmy")) {
                    sheet.getCell(letras.get(6)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(7)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(8)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtsr")
                        || esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtno")
                        || esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtoe")
                        || esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtes")) {
                    sheet.getCell(letras.get(9)).setValue(
                            esclarecimientoHecho.getTotalConciliados() + (Integer) sheet.getCell(letras.get(9)).getValue()
                    );
                    sheet.getCell(letras.get(10)).setValue(
                            esclarecimientoHecho.getCantPext() + (Integer) sheet.getCell(letras.get(10)).getValue()
                    );
                    sheet.getCell(letras.get(11)).setValue(
                            esclarecimientoHecho.getCantTpub() + (Integer) sheet.getCell(letras.get(11)).getValue()
                    );
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("DTIJ")) {
                    sheet.getCell(letras.get(12)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(13)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(14)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtmz")) {
                    sheet.getCell(letras.get(15)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(16)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(17)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtcf")) {
                    sheet.getCell(letras.get(18)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(19)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(20)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtvc")) {
                    sheet.getCell(letras.get(21)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(22)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(23)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtss")) {
                    sheet.getCell(letras.get(24)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(25)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(26)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtca")) {
                    sheet.getCell(letras.get(27)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(28)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(29)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtcm")) {
                    sheet.getCell(letras.get(30)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(31)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(32)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtlt")) {
                    sheet.getCell(letras.get(33)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(34)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(35)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtho")) {
                    sheet.getCell(letras.get(36)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(37)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(38)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtgr")) {
                    sheet.getCell(letras.get(39)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(40)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(41)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtsc")) {
                    sheet.getCell(letras.get(42)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(43)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(44)).setValue(esclarecimientoHecho.getCantTpub());
                } else if (esclarecimientoHecho.getDireccionTerritorial().equalsIgnoreCase("dtgt")) {
                    sheet.getCell(letras.get(45)).setValue(esclarecimientoHecho.getTotalConciliados());
                    sheet.getCell(letras.get(46)).setValue(esclarecimientoHecho.getCantPext());
                    sheet.getCell(letras.get(47)).setValue(esclarecimientoHecho.getCantTpub());
                }
            });
        }
        return result;
    }

    private boolean llenarCamposPextTpu(LinkedList<ResumenModels> modelosResumenList, ExcelWorksheet sheet, ArrayList<String> listaLetras) {
        boolean result = false;
        if (modelosResumenList != null) {
            result = true;
            modelosResumenList.forEach(resumenModels -> {
                if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtpr")) {
                    sheet.getCell(listaLetras.get(0)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(1)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(2)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtar")) {
                    sheet.getCell(listaLetras.get(3)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(4)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(5)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtmy")) {
                    sheet.getCell(listaLetras.get(6)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(7)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(8)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtes") ||
                        resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtoe") ||
                        resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtsr") ||
                        resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtno")) {
                    sheet.getCell(listaLetras.get(9)).setValue(
                            sheet.getCell(listaLetras.get(9)).getIntValue() + resumenModels.getCantHechos()
                    );
                    sheet.getCell(listaLetras.get(10)).setValue(
                            sheet.getCell(listaLetras.get(10)).getDoubleValue() + resumenModels.getServiciosAfectados()
                    );
                    sheet.getCell(listaLetras.get(11)).setValue(
                            sheet.getCell(listaLetras.get(11)).getDoubleValue() + resumenModels.getAfectacionMlc()
                    );
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtij")) {
                    sheet.getCell(listaLetras.get(12)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(13)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(14)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtmz")) {
                    sheet.getCell(listaLetras.get(15)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(16)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(17)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtcf")) {
                    sheet.getCell(listaLetras.get(18)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(19)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(20)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtvc")) {
                    sheet.getCell(listaLetras.get(21)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(22)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(23)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtss")) {
                    sheet.getCell(listaLetras.get(24)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(25)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(26)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtca")) {
                    sheet.getCell(listaLetras.get(27)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(28)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(29)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtcm")) {
                    sheet.getCell(listaLetras.get(30)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(31)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(32)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtlt")) {
                    sheet.getCell(listaLetras.get(33)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(34)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(35)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtho")) {
                    sheet.getCell(listaLetras.get(36)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(37)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(38)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtgr")) {
                    sheet.getCell(listaLetras.get(39)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(40)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(41)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtsc")) {
                    sheet.getCell(listaLetras.get(42)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(43)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(44)).setValue(resumenModels.getAfectacionMlc());
                } else if (resumenModels.getUnidadOrganizativa().equalsIgnoreCase("dtgt")) {
                    sheet.getCell(listaLetras.get(45)).setValue(resumenModels.getCantHechos());
                    sheet.getCell(listaLetras.get(46)).setValue(resumenModels.getServiciosAfectados());
                    sheet.getCell(listaLetras.get(47)).setValue(resumenModels.getAfectacionMlc());
                }
            });
        }
        return result;
    }

    private boolean tablaMateriales(ExcelWorksheet worksheet, LocalDate date) {
        boolean result = false;

        CellRange rangoA40B41 = worksheet.getCells().getSubrange("A40:B41");
        rangoA40B41.setStyle(generarStilo());
        rangoA40B41.setMerged(true);
        rangoA40B41.setValue("Material afectado");

        CellRange rangoC40D40 = worksheet.getCells().getSubrange("C40:D40");
        rangoC40D40.setStyle(generarStilo());
        rangoC40D40.setMerged(true);
        rangoC40D40.setValue(date.minusYears(1).getYear());

        CellRange rangoE40F40 = worksheet.getCells().getSubrange("E40:F40");
        rangoE40F40.setStyle(generarStilo());
        rangoE40F40.setMerged(true);
        rangoE40F40.setValue(date.getYear());

        worksheet.getCell("C41").setStyle(generarStilo());
        worksheet.getCell("C41").setValue("Cant. Hechos");
        worksheet.getCell("D41").setStyle(generarStilo());
        worksheet.getCell("D41").setValue("Cant. Material");

        worksheet.getCell("E41").setValue("Cant. Hechos");
        worksheet.getCell("E41").setStyle(generarStilo());
        worksheet.getCell("F41").setValue("Cant. Material");
        worksheet.getCell("F41").setStyle(generarStilo());

        LinkedList<String> listaMateriales = new LinkedList<>();
        ServiceLocator.getTipoMaterialesService().fetchAll().forEach(
                materiales -> listaMateriales.add(materiales.getMateriales())
        );

        for (int row = 41; row < 59; row++) {
            worksheet.getCell(row, 0).setValue(listaMateriales.removeFirst());
        }

        for (int row = 42; row < 60; row++) {
            CellRange range = worksheet.getCells().getSubrange("A" + row, "B" + row);
            range.setStyle(generarBordes());
            range.setMerged(true);
            range.getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.LEFT);
        }

        for (int row = 41; row < 59; row++) {
            for (int column = 2; column < 6; column++) {
                worksheet.getCell(row, column).setValue(0);
                if (column < 4) {
                    worksheet.getCell(row, column).setStyle(generarBordes());
                } else {
                    worksheet.getCell(row, column).setStyle(estiloColumnasHojasTotales());
                }
            }
        }


        ArrayList<String> letrasToBeforeYear = new ArrayList<>();
        ArrayList<String> letrasToActualYear = new ArrayList<>();

        for (int x = 42; x < 60; x++) {
            letrasToBeforeYear.add("C" + x);
            letrasToBeforeYear.add("D" + x);
        }

        for (int x = 42; x < 60; x++) {
            letrasToActualYear.add("E" + x);
            letrasToActualYear.add("F" + x);
        }

        LinkedList<MaterialsFiscaliaModels> modelsActual = ServiceLocator.getTipoMaterialesService()
                .materialesPorAnno(Date.valueOf(date));
        LinkedList<MaterialsFiscaliaModels> modelsPast = ServiceLocator.getTipoMaterialesService()
                .materialesPorAnno(Date.valueOf(date.minusYears(1)));
        this.llenarCamposMaterials(modelsPast, worksheet, letrasToBeforeYear);
        this.llenarCamposMaterials(modelsActual, worksheet, letrasToActualYear);

        return result;
    }

    private boolean tablaAfectaciones(ExcelWorksheet worksheet, LocalDate date) {

        CellRange rangoA61C62 = worksheet.getCells().getSubrange("A61:C62");
        rangoA61C62.setMerged(true);
        rangoA61C62.setStyle(generarStilo());
        rangoA61C62.setValue("Tipo de Afectación");

        CellRange rangoD61E61 = worksheet.getCells().getSubrange("D61:E61");
        rangoD61E61.setMerged(true);
        rangoD61E61.setStyle(generarStilo());
        rangoD61E61.setValue("Cant. Hechos");

        worksheet.getCell("D62").setValue(date.minusYears(1).getYear());
        worksheet.getCell("D62").setStyle(generarStilo());

        worksheet.getCell("E62").setValue(date.getYear());
        worksheet.getCell("E62").setStyle(generarStilo());

        for (int row = 62; row < 76; row++) {
            for (int column = 3; column < 5; column++) {
                worksheet.getCell(row, column).setValue(0);
                if (column != 4) {
                    worksheet.getCell(row, column).setStyle(generarBordes());
                } else {
                    worksheet.getCell(row, column).setStyle(estiloColumnasHojasTotales());
                }
            }
        }

        LinkedList<TipoVandalismo> vandalismos = new LinkedList<>();
        ServiceLocator.getTipoVandalismoService().fetchAll().stream().forEach(
                vandalismo -> vandalismos.add(vandalismo)

        );

        for (int row = 63; row < 77; row++) {
            CellRange range = worksheet.getCells().getSubrange("A" + row, "C" + row);
            range.setStyle(generarBordes());
            range.setMerged(true);
            range.getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.LEFT);
            range.setValue(vandalismos.removeFirst().getAfect_tpublica());
        }

        this.llenarCamposAfectacion(
                ServiceLocator.getHechosService().cantidadAfectacionesHechosFiscalia(Date.valueOf(date.minusYears(1))),
                worksheet,
                "D"
        );

        this.llenarCamposAfectacion(
                ServiceLocator.getHechosService().cantidadAfectacionesHechosFiscalia(Date.valueOf(date)),
                worksheet,
                "E"
        );

        return true;
    }

    private boolean tableCantByAnnos(ExcelWorksheet worksheet, LocalDate date) {

        CellRange rangoA78A79 = worksheet.getCells().getSubrange("A78:A79");
        rangoA78A79.setStyle(generarStilo());
        rangoA78A79.setMerged(true);
        rangoA78A79.setValue("Año");

        CellRange rangoB78B79 = worksheet.getCells().getSubrange("B78:B79");
        rangoB78B79.setStyle(generarStilo());
        rangoB78B79.setMerged(true);
        rangoB78B79.setValue("Mes");

        CellRange rangoC78D78 = worksheet.getCells().getSubrange("C78:D78");
        rangoC78D78.setStyle(generarStilo());
        rangoC78D78.setMerged(true);
        rangoC78D78.setValue("Cant. Hechos");

        worksheet.getCell("C79").setValue("PExt");
        worksheet.getCell("C79").setStyle(generarStilo());

        worksheet.getCell("D79").setValue("TPúb");
        worksheet.getCell("D79").setStyle(generarStilo());

        CellRange rangoA80A91 = worksheet.getCells().getSubrange("A80:A91");
        rangoA80A91.setStyle(generarBordes());
        rangoA80A91.setMerged(true);
        rangoA80A91.setValue(date.minusYears(1).getYear());

        CellRange rangoA92A103 = worksheet.getCells().getSubrange("A92:A103");
        rangoA92A103.setValue(date.getYear());
        rangoA92A103.setStyle(generarBordes());
        rangoA92A103.setMerged(true);

        for (int row = 79; row < 103; row++) {
            for (int column = 1; column < 4; column++) {
                worksheet.getCell(row, column).setValue(0);
                if (column < 3) {
                    worksheet.getCell(row, column).setStyle(generarBordes());
                } else {
                    worksheet.getCell(row, column).setStyle(estiloColumnasHojasTotales());
                }
            }
        }

        int valorLetra = 80;
        for (int it = 0; it < 2; it++) {
            for (int i = 0; i < 12; i++) {
                worksheet.getCell("B" + valorLetra).setValue(Util.meses[i]);
                valorLetra++;
            }
        }

        ArrayList<String> listaLetrasAnnoAnterior = new ArrayList<>();
        for (int x = 80; x < 92; x++) {
            listaLetrasAnnoAnterior.add("C" + x);
            listaLetrasAnnoAnterior.add("D" + x);
        }
        ArrayList<String> letrasAnnoActual = new ArrayList<>();
        for (int x = 92; x < 104; x++) {
            letrasAnnoActual.add("C" + x);
            letrasAnnoActual.add("D" + x);
        }

        UtilToGenerateTables.llenarCamposCantHechosByAnno(
                ServiceLocator.getHechosService().cantidadHechosByAnno(date.minusYears(1).getYear()),
                worksheet,
                listaLetrasAnnoAnterior
        );
        UtilToGenerateTables.llenarCamposCantHechosByAnno(
                ServiceLocator.getHechosService().cantidadHechosByAnno(date.getYear()),
                worksheet,
                letrasAnnoActual
        );

        return true;
    }

    private boolean tableTelefoniaPublica(ExcelWorksheet worksheet, LocalDate date) {

        CellRange rangoN1R1 = worksheet.getCells().getSubrange("N1:R1");
        rangoN1R1.setMerged(true);
        rangoN1R1.setStyle(generarStilo());
        rangoN1R1.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromName(ColorName.DARK_RED));
        rangoN1R1.setValue("Telefonía Pública (" + date.getYear() + ")");

        worksheet.getCell("N2").setValue("Div Territ.");
        worksheet.getCell("N2").setStyle(generarStilo());

        worksheet.getCell("O2").setValue("Cant. Hechos");
        worksheet.getCell("O2").setStyle(generarStilo());

        worksheet.getCell("P2").setValue("Serv. Afect.");
        worksheet.getCell("P2").setStyle(generarStilo());

        worksheet.getCell("Q2").setValue("EP Instaladas");
        worksheet.getCell("Q2").setStyle(generarStilo());

        worksheet.getCell("R2").setValue("SA/EP");
        worksheet.getCell("R2").setStyle(generarStilo());

        for (int row = 2; row < 18; row++) {
            for (int column = 13; column < 18; column++) {
                worksheet.getCell(row, column).setValue(0);
                if (column % 2 == 0) {
                    worksheet.getCell(row, column).setStyle(estiloColumnasHojasTotales());
                } else {
                    worksheet.getCell(row, column).setStyle(generarBordes());
                }
            }
        }

        worksheet.getCell("N19").setValue("Total:");
        worksheet.getCell("N19").setStyle(generarStilo());

        worksheet.getCell("O19").setFormula("=SUM(O3:O18)");
        worksheet.getCell("O19").calculate();

        worksheet.getCell("P19").setFormula("=SUM(P3:P18)");
        worksheet.getCell("P19").calculate();

        worksheet.getCell("Q19").setFormula("=SUM(Q3:Q18)");
        worksheet.getCell("Q19").calculate();

        int value = 3;

        //col = 17
        for (int row = 2; row < 19; row++) {
            worksheet.getCell(row, 17).setFormula("=100*P" + value + "/Q" + value);
            worksheet.getCell(row, 17).calculate();
            worksheet.getCell(row, 17).getStyle().setNumberFormat("0.00");
            value++;
        }

        for (int column = 13; column < 18; column++)
            worksheet.getCell(18, column).setStyle(generarStilo());

        worksheet.getCell("N3").setValue("DTPR");
        worksheet.getCell("N4").setValue("DTAR");
        worksheet.getCell("N5").setValue("DTMY");
        worksheet.getCell("N6").setValue("DVLH");
        worksheet.getCell("N7").setValue("DTIJ");
        worksheet.getCell("N8").setValue("DTMZ");
        worksheet.getCell("N9").setValue("DTCF");
        worksheet.getCell("N10").setValue("DTVC");
        worksheet.getCell("N11").setValue("DTSS");
        worksheet.getCell("N12").setValue("DTCA");
        worksheet.getCell("N13").setValue("DTCM");
        worksheet.getCell("N14").setValue("DTLT");
        worksheet.getCell("N15").setValue("DTHO");
        worksheet.getCell("N16").setValue("DTGR");
        worksheet.getCell("N17").setValue("DTSC");
        worksheet.getCell("N18").setValue("DTGT");

        //=E22 hasta =E37
        value = 22;
        for (int row = 2; row < 19; row++) {
            worksheet.getCell(row, 14).setFormula("=E" + value);
            value++;
        }

        value = 22;
        for (int row = 2; row < 19; row++) {
            worksheet.getCell(row, 15).setFormula("=F" + value);
            value++;
        }

        worksheet.getCell("R19").getStyle().setNumberFormat("0.00");


        this.llenarCamposTelefoniaPublica(ServiceLocator.getHechosService().getEstacionesPublicasCant(), worksheet);
        return true;
    }

    private boolean llenarCamposTelefoniaPublica(LinkedList<EstacionesPublicas> estacionesPublicas, ExcelWorksheet sheet) {
        boolean result = false;
        if (estacionesPublicas != null) {
            result = true;
            estacionesPublicas.forEach(estacionesPublica -> {
                if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTPR")) {
                    sheet.getCell("Q3").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTAR")) {
                    sheet.getCell("Q4").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTMY")) {
                    sheet.getCell("Q5").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DVLH")) {
                    sheet.getCell("Q6").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTIJ")) {
                    sheet.getCell("Q7").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTMZ")) {
                    sheet.getCell("Q8").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTCF")) {
                    sheet.getCell("Q9").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTVC")) {
                    sheet.getCell("Q10").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTSS")) {
                    sheet.getCell("Q11").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTCA")) {
                    sheet.getCell("Q12").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTCM")) {
                    sheet.getCell("Q13").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTLT")) {
                    sheet.getCell("Q14").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTHO")) {
                    sheet.getCell("Q15").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTGR")) {
                    sheet.getCell("Q16").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTSC")) {
                    sheet.getCell("Q17").setValue(estacionesPublica.getCantEstaciones());
                } else if (estacionesPublica.getUnidadOrganizativa().equalsIgnoreCase("DTGT")) {
                    sheet.getCell("Q18").setValue(estacionesPublica.getCantEstaciones());
                }
            });
        }

        return result;
    }


    private boolean llenarCamposMaterials(LinkedList<MaterialsFiscaliaModels> materialsFiscaliaModels, ExcelWorksheet sheet, ArrayList<String> listaLetras) {
        boolean value = false;
        if (materialsFiscaliaModels != null) {
            value = true;
            materialsFiscaliaModels.stream().forEach(materialsFiscalia -> {
                if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("alambre de coser")) {
                    sheet.getCell(listaLetras.get(0)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(1)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("cable telefónico")) {
                    sheet.getCell(listaLetras.get(2)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(3)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("tensor")) {
                    sheet.getCell(listaLetras.get(4)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(5)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Bajante telefónico")) {
                    sheet.getCell(listaLetras.get(6)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(7)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Fibra óptica")) {
                    sheet.getCell(listaLetras.get(8)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(9)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Alambre de cobre")) {
                    sheet.getCell(listaLetras.get(10)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(11)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Postes")) {
                    sheet.getCell(listaLetras.get(12)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(13)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Gabinetes")) {
                    sheet.getCell(listaLetras.get(14)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(15)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Cable coaxial")) {
                    sheet.getCell(listaLetras.get(16)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(17)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Cable simétrico")) {
                    sheet.getCell(listaLetras.get(18)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(19)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Angulares de torres")) {
                    sheet.getCell(listaLetras.get(20)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(21)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Crucetas")) {
                    sheet.getCell(listaLetras.get(22)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(23)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Cable de aterramiento")) {
                    sheet.getCell(listaLetras.get(24)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(25)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Campana canal lateral")) {
                    sheet.getCell(listaLetras.get(26)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(27)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Ganador de pares")) {
                    sheet.getCell(listaLetras.get(28)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(29)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Mondragón")) {
                    sheet.getCell(listaLetras.get(30)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(31)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Mordazas")) {
                    sheet.getCell(listaLetras.get(32)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(33)).setValue(materialsFiscalia.getCantMaterial());
                } else if (materialsFiscalia.getMaterialAfectado().equalsIgnoreCase("Tensor de anclaje")) {
                    sheet.getCell(listaLetras.get(34)).setValue(materialsFiscalia.getCantHechos());
                    sheet.getCell(listaLetras.get(35)).setValue(materialsFiscalia.getCantMaterial());
                }
            });
        }
        return value;
    }

    private boolean llenarCamposAfectacion(LinkedList<AfectacionFiscaliaModels> afectacionFiscaliaModels, ExcelWorksheet sheet, String letra) {
        boolean result = false;
        if (afectacionFiscaliaModels != null) {
            result = true;
            afectacionFiscaliaModels.stream().forEach(afectacionFiscalia -> {
                if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Robo del microteléfono")) {
                    sheet.getCell(letra + "63").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Microteléfono partido")) {
                    sheet.getCell(letra + "64").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Robo equipo completo")) {
                    sheet.getCell(letra + "65").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Display roto o dañado")) {
                    sheet.getCell(letra + "66").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Canaleta violentada")) {
                    sheet.getCell(letra + "67").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Cabina rota")) {
                    sheet.getCell(letra + "68").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Robo de la cabina")) {
                    sheet.getCell(letra + "69").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Vandalismo múltiple")) {
                    sheet.getCell(letra + "70").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Robo de cápsulas")) {
                    sheet.getCell(letra + "71").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Robo de la hucha")) {
                    sheet.getCell(letra + "72").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Corte del bajante")) {
                    sheet.getCell(letra + "73").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Corte de la anaconda")) {
                    sheet.getCell(letra + "74").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Robo de varias partes")) {
                    sheet.getCell(letra + "75").setValue(afectacionFiscalia.getCantHechos());
                } else if (afectacionFiscalia.getTipoAfectacion().equalsIgnoreCase("Obstrucción de ranura")) {
                    sheet.getCell(letra + "77").setValue(afectacionFiscalia.getCantHechos());
                }
            });
        }
        return result;
    }

}
