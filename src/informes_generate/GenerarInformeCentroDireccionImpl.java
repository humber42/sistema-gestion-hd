package informes_generate;

import com.gembox.spreadsheet.*;
import models.HechosMesAnno;
import models.HechosUOrgAnno;
import models.HechosUorgMesAnno;
import models.UnidadOrganizativaCantHechos;
import services.ServiceLocator;
import util.Util;
import util.UtilToGenerateTables;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static informes_generate.GraphicGenerator.*;
import static util.Util.*;


/**
 * @author Humberto Cabrera Dominguez
 * Generador de informe de centro de direccion
 * @version 1.0
 */
public class GenerarInformeCentroDireccionImpl implements GenerarInformeCentroDireccion {

    private static String DIRECCION_ARCHIVO = "/ResumenCDNT.xlsx";


    @Override
    public boolean generarInformeCentroDireccion(Date date, String path) {
        boolean result = false;
        ExcelFile workbook = new ExcelFile();
        if (generararHojaResumenMensual(date, workbook)
                && generarResumen(date, workbook)
                && generateHistoricalData(workbook, date)
                && generateGraphicalInformation(workbook, date)
        ) {

            try {
                workbook.save(path+DIRECCION_ARCHIVO);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Genera la hoja de resumen mensual
     *
     * @param date     fecha
     * @param workbook libro
     * @return tarea cumplida o no
     */
    private boolean generararHojaResumenMensual(Date date, ExcelFile workbook) {
        ExcelWorksheet sheet = workbook.addWorksheet("Resumen Mensual");
        sheet.getCell("A1").setValue("Mes");
        sheet.getCell("A1").setStyle(Util.generarStilo());
        sheet.getColumn("A").setWidth(11.50, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getCell("B1").setValue("PExt");
        sheet.getCell("B1").setStyle(Util.generarStilo());
        sheet.getCell("C1").setValue("TPub");
        sheet.getCell("C1").setStyle(Util.generarStilo());
        sheet.getCell("D1").setStyle(Util.generarStilo());
        sheet.getCell("D1").setValue("Robo");
        sheet.getCell("E1").setValue("Hurto");
        sheet.getCell("E1").setStyle(Util.generarStilo());
        sheet.getCell("F1").setValue("Fraude");
        sheet.getCell("F1").setStyle(Util.generarStilo());
        sheet.getCell("G1").setValue("Otros Delitos");
        sheet.getCell("G1").setStyle(Util.generarStilo());
        sheet.getCell("H1").setValue("Total");
        sheet.getCell("H1").setStyle(Util.generarStilo());
        CellRange range = sheet.getCells().getSubrange("A2:H2");
        range.setValue(date.toLocalDate().getYear());
        range.setMerged(true);
        range.setStyle(Util.estiloColumnasHojasTotales());
        range.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(47, 163, 255));
        range.getStyle().getFont().setColor(SpreadsheetColor.fromName(ColorName.WHITE));

        CellRange cells = sheet.getCells().getSubrange("A3:A14");
        int number = 0;
        for (ExcelCell cell : cells) {
            cell.setValue(Util.meses2[number]);
            cell.setStyle(Util.generarBordes());
            cell.getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.LEFT);
            number++;
        }

        CellRange values1 = sheet.getCells().getSubrange("B3:G14");
        for (ExcelCell cell : values1) {
            cell.setValue(0);
            cell.setStyle(Util.generarBordes());
        }

        CellRange rangoPaFormulas = sheet.getCells().getSubrange("H3:H14");
        int fila = 3;
        for (ExcelCell cell : rangoPaFormulas) {
            cell.setFormula("=SUM(B" + fila + ":G" + fila + ")");
            cell.calculate();
            cell.setStyle(Util.estiloColumnasHojasTotales());
            cell.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(145, 163, 255));
            fila++;
        }

        String[] letras = {"B", "C", "D", "E", "F", "G", "H"};

        sheet.getCells().getSubrange("A15:H15").forEach(p -> p.setStyle(Util.generarStilo()));
        sheet.getCell("A15").setValue("Total:");
        sheet.getCell("A15").getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);
        fila = 0;
        CellRange rangeToForms = sheet.getCells().getSubrange("B15:H15");
        for (ExcelCell cell : rangeToForms) {
            cell.setFormula("=SUM(" + letras[fila] + "3:" + letras[fila] + "14)");
            cell.calculate();
            fila++;
        }

        CellRange range2 = sheet.getCells().getSubrange("A16:H16");
        range2.setValue(date.toLocalDate().minusYears(1).getYear());
        range2.setMerged(true);
        range2.setStyle(Util.estiloColumnasHojasTotales());
        range2.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(47, 163, 255));
        range2.getStyle().getFont().setColor(SpreadsheetColor.fromName(ColorName.WHITE));

        CellRange meses2 = sheet.getCells().getSubrange("A17:A28");
        fila = 0;
        for (ExcelCell cell : meses2) {
            cell.setValue(Util.meses2[fila]);
            fila++;
            cell.setStyle(Util.generarBordes());
        }

        sheet.getCells().getSubrange("B17:H28").forEach(excelCell -> {
            excelCell.setValue(0);
            excelCell.setStyle(Util.generarBordes());
        });

        fila = 17;
        CellRange rangoH17H28 = sheet.getCells().getSubrange("H17:H28");
        for (ExcelCell cell : rangoH17H28) {
            cell.setFormula("=SUM(B" + fila + ":G" + fila + ")");
            cell.calculate();
            cell.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(145, 163, 255));
            fila++;
        }
        sheet.getCell("A29").setValue("Total:");
        sheet.getCell("A29").setStyle(Util.generarStilo());
        sheet.getCell("A29").getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);

        fila = 0;
        CellRange filaFinal = sheet.getCells().getSubrange("B29:H29");
        for (ExcelCell cell : filaFinal) {
            cell.setFormula("=SUM(" + letras[fila] + "17:" + letras[fila] + "28)");
            cell.calculate();
            cell.setStyle(Util.generarStilo());
            fila++;
        }

        ArrayList<String> positions1 = this.devolverListaLetras(3, 15);
        ArrayList<String> positions2 = this.devolverListaLetras(17, 29);


        this.dataWritted(ServiceLocator.getHechosService().hechosMesesAnno(date), positions1, sheet);
        this.dataWritted(ServiceLocator.getHechosService().hechosMesesAnno(Date.valueOf(date.toLocalDate().minusYears(1))), positions2, sheet);


        return true;
    }

    private boolean generarResumen(Date date, ExcelFile workbook) {
        ExcelWorksheet worksheet = workbook.addWorksheet("Resumen");
        GeneradoresTablas.generarTotales(date.toLocalDate(), worksheet, GenerateInformeFiscaliaImpl.executingConsult(date.toLocalDate()));
        GeneradoresTablas.generarTotalesHurtosRobos(date.toLocalDate(), worksheet);
        return generarTablaResumen2(date.toLocalDate(), worksheet);
    }


    private boolean generarTablaResumen2(LocalDate date, ExcelWorksheet sheet) {

        CellRange rangeA8A9 = sheet.getCells().getSubrange("A8:A9");
        rangeA8A9.setValue("División Territorial");
        rangeA8A9.setStyle(Util.generarStilo());
        rangeA8A9.setMerged(true);

        CellRange rangeB1D1 = sheet.getCells().getSubrange("B8:D8");
        rangeB1D1.setValue("PExt");
        rangeB1D1.setStyle(Util.generarStilo());
        rangeB1D1.setMerged(true);

        CellRange rangeE1G1 = sheet.getCells().getSubrange("E8:G8");
        rangeE1G1.setValue("TPúb");
        rangeE1G1.setStyle(Util.generarStilo());
        rangeE1G1.setMerged(true);

        ExcelCell cell = sheet.getCell("B9");
        cell.setValue(date.minusYears(1).getYear());
        cell.setStyle(Util.generarStilo());

        cell = sheet.getCell("C9");
        cell.setStyle(Util.generarStilo());
        cell.setValue(date.getYear());

        cell = sheet.getCell("D9");
        cell.setStyle(Util.generarStilo());
        cell.setValue("Dif");


        cell = sheet.getCell("E9");
        cell.setValue(date.minusYears(1).getYear());
        cell.setStyle(Util.generarStilo());

        cell = sheet.getCell("F9");
        cell.setStyle(Util.generarStilo());
        cell.setValue(date.getYear());

        cell = sheet.getCell("G9");
        cell.setValue("Dif");
        cell.setStyle(Util.generarStilo());

        CellRange rangeUOrg = sheet.getCells().getSubrange("A10:A25");
        int fila = 0;
        for (ExcelCell celda : rangeUOrg) {
            celda.setValue(unidades_organizativa[fila]);
            celda.setStyle(generarBordes());
            fila++;
        }

        sheet.getCells().getSubrange("B10:G25").
                forEach(celda -> {
                    celda.setStyle(generarBordes());
                    celda.setValue(0);
                });

        fila = 10;
        CellRange rangeForms = sheet.getCells().getSubrange("D10:D25");
        for (ExcelCell celda : rangeForms) {
            celda.setFormula("=C" + fila + "-B" + fila);
            fila++;
        }

        rangeForms = sheet.getCells().getSubrange("G10:G25");
        fila = 10;
        for (ExcelCell celda : rangeForms) {
            celda.setFormula("=F" + fila + "-E" + fila);
            celda.calculate();
            fila++;
        }

        cell = sheet.getCell("A26");
        cell.setStyle(generarStilo());
        cell.setValue("Totales");
        cell.getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);

        rangeForms = sheet.getCells().getSubrange("B26:G26");
        rangeForms.forEach(p -> p.setStyle(generarStilo()));

        String[] letras = {"B", "C", "D", "E", "F", "G"};

        fila = 0;
        for (ExcelCell celda : rangeForms) {
            celda.setFormula("=SUM(" + letras[fila] + "10:" + letras[fila] + "25)");
            fila++;
        }

        boolean writingDataAnnoAnterior = this.writeDataResumen2(sheet, devolverListaLetras("B", "E"),
                ServiceLocator.getHechosService().hechosUnidadOranizativaList(Date.valueOf(date.minusYears(1))));
        boolean writingDataAnnoActual = this.writeDataResumen2(sheet, devolverListaLetras("C", "F"),
                ServiceLocator.getHechosService().hechosUnidadOranizativaList(Date.valueOf(date)));

        this.aplicarStilo(sheet.getCells().getSubrange("D10:D25"));
        this.aplicarStilo(sheet.getCells().getSubrange("G10:G25"));

        this.generarTablasAuxiliaresResumen2(sheet.getCells().getSubrange("D10:D25"),
                sheet, "PExt", "I", "J");
        this.generarTablasAuxiliaresResumen2(sheet.getCells().getSubrange("G10:G25"), sheet, "TPúb", "L", "M");

        return true;
    }

    private boolean generarTablasAuxiliaresResumen2(CellRange range, ExcelWorksheet sheet, String type, String... ubicacion) {
        LinkedList<UnidadOrganizativaCantHechos> unidadOrganizativaCantHechos = new LinkedList<>();

        int value = 0;
        for (ExcelCell cell : range) {
            cell.calculate();
            unidadOrganizativaCantHechos.add(new UnidadOrganizativaCantHechos(unidades_organizativa[value], cell.getIntValue()));
            value++;
        }

        unidadOrganizativaCantHechos.sort(Comparator.comparingInt(UnidadOrganizativaCantHechos::getCantHechos));
        Collections.reverse(unidadOrganizativaCantHechos);
        int row = 9;

        sheet.getCell(ubicacion[0] + 8).setValue("U/O");
        sheet.getCell(ubicacion[0] + 8).setStyle(generarStilo());
        sheet.getCell(ubicacion[1] + 8).setValue(type);
        sheet.getCell(ubicacion[1] + 8).setStyle(generarStilo());

        for (UnidadOrganizativaCantHechos unidadOrganizativa : unidadOrganizativaCantHechos) {
            sheet.getCell(ubicacion[0] + row).setValue(unidadOrganizativa.getUnidadOrganizativa());
            sheet.getCell(ubicacion[0] + row).setStyle(generarBordes());
            sheet.getCell(ubicacion[1] + row).setValue(unidadOrganizativa.getCantHechos());
            sheet.getCell(ubicacion[1] + row).setStyle(generarBordes());
            if (sheet.getCell(ubicacion[1] + row).getIntValue() > 0) {
                sheet.getCell(ubicacion[1] + row).getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(214, 0, 36));
                sheet.getCell(ubicacion[1] + row).getStyle().getFont().setColor(SpreadsheetColor.fromName(ColorName.WHITE));
            } else {
                sheet.getCell(ubicacion[1] + row).getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(161, 214, 214));
            }
            row++;
        }
        return true;
    }

    /**
     * Genera y escribe la hoja de datos historicos
     *
     * @param workbook libro
     * @param date     fecha
     * @return tarea completada
     */
    private boolean generateHistoricalData(ExcelFile workbook, Date date) {
        ExcelWorksheet sheet = workbook.addWorksheet("Historico");
        CellRange rangeA1A2 = sheet.getCells().getSubrange("A1:A2");
        rangeA1A2.setValue("Direccion Territorial");
        rangeA1A2.setStyle(Util.generarStilo());
        rangeA1A2.setMerged(true);

        CellRange rangeB1N1 = sheet.getCells().getSubrange("B1:N1");
        rangeB1N1.setValue(date.toLocalDate().minusYears(1).getYear());
        rangeB1N1.setStyle(Util.generarStilo());
        rangeB1N1.setMerged(true);

        CellRange rangeO1AA1 = sheet.getCells().getSubrange("O1:AA1");
        rangeO1AA1.setValue(date.toLocalDate().getYear());
        rangeO1AA1.setStyle(Util.generarStilo());
        rangeO1AA1.setMerged(true);

        ExcelCell celdaTotal = sheet.getCell("B2");
        celdaTotal.setValue("Total");
        celdaTotal.setStyle(Util.generarStilo());
        celdaTotal = sheet.getCell("O2");
        celdaTotal.setValue("Total");
        celdaTotal.setStyle(Util.generarStilo());

        GeneradoresTablas.writeMonthOnHistorical(sheet.getCells().getSubrange("C2:N2"), Util.generarStilo());
        GeneradoresTablas.writeMonthOnHistorical(sheet.getCells().getSubrange("P2:AA2"), Util.generarStilo());

        //Freezing row 1 and 2
        sheet.setPanes(new WorksheetPanes(PanesState.FROZEN_SPLIT, 0, 2, "A3", PanePosition.BOTTOM_LEFT));

        CellRange rangeA3AA3 = sheet.getCells().getSubrange("A3:AA3");
        rangeA3AA3.setMerged(true);
        rangeA3AA3.setStyle(Util.generarStilo());
        rangeA3AA3.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(214, 51, 59));
        rangeA3AA3.setValue("Planta Exterior");

        CellRange rangeA21AA21 = sheet.getCells().getSubrange("A21:AA21");
        rangeA21AA21.setMerged(true);
        rangeA21AA21.setStyle(Util.generarStilo());
        rangeA21AA21.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(214, 51, 59));
        rangeA21AA21.setValue("Telefonía Pública");

        //Writing names and styling UO's
        writeUOsOnHistorical(sheet.getCells().getSubrange("A4:A19"));
        writeUOsOnHistorical(sheet.getCells().getSubrange("A22:A37"));

        celdaTotal = sheet.getCell("A20");
        celdaTotal.setValue("Total:");
        celdaTotal.setStyle(Util.generarStilo());
        celdaTotal.getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);

        celdaTotal = sheet.getCell("A38");
        celdaTotal.setValue("Total:");
        celdaTotal.setStyle(Util.generarStilo());
        celdaTotal.getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);

        this.writeZeerosAndStylingOnHistorical(sheet.getCells().getSubrange("C4:N19"));
        this.writeZeerosAndStylingOnHistorical(sheet.getCells().getSubrange("P4:AA19"));
        this.writeZeerosAndStylingOnHistorical(sheet.getCells().getSubrange("C22:N37"));
        this.writeZeerosAndStylingOnHistorical(sheet.getCells().getSubrange("P22:AA37"));

        //writing formulas
        writeFormulasTotalHorizontalsonHistorical(sheet.getCells().getSubrange("B4:B19"), "C", "N");
        writeFormulasTotalHorizontalsonHistorical(sheet.getCells().getSubrange("O4:O19"), "P", "AA");
        writeFormulasTotalHorizontalsonHistorical(sheet.getCells().getSubrange("B22:B37"), "C", "N");
        writeFormulasTotalHorizontalsonHistorical(sheet.getCells().getSubrange("O22:O37"), "P", "AA");

        writeFormulasTotalVerticalsonHistorical(sheet.getCells().getSubrange("B20:AA20"), 4, 19);
        writeFormulasTotalVerticalsonHistorical(sheet.getCells().getSubrange("B38:AA38"), 22, 37);
        this.writeHistorical2(sheet, date);

        //Setting data
        LinkedList<HechosUorgMesAnno> hechosUorgMesAnnosAnteriorPlantaExterior = ServiceLocator.getHechosService().obtenerCantidadHechosUOrgPorMes(date.toLocalDate().minusYears(1).getYear(), 1);
        LinkedList<HechosUorgMesAnno> hechosUorgMesAnnosActualPlantaExterior = ServiceLocator.getHechosService().obtenerCantidadHechosUOrgPorMes(date.toLocalDate().getYear(), 1);
        LinkedList<HechosUorgMesAnno> hechosUorgMesAnnosAnteriorTPub = ServiceLocator.getHechosService().obtenerCantidadHechosUOrgPorMes(date.toLocalDate().minusYears(1).getYear(), 2);
        LinkedList<HechosUorgMesAnno> hechosUorgMesAnnosActualTPub = ServiceLocator.getHechosService().obtenerCantidadHechosUOrgPorMes(date.toLocalDate().getYear(), 2);

        this.writeDataOnHistorical(sheet, hechosUorgMesAnnosAnteriorPlantaExterior, "PextAnterior");
        this.writeDataOnHistorical(sheet, hechosUorgMesAnnosActualPlantaExterior, "PextActual");
        this.writeDataOnHistorical(sheet, hechosUorgMesAnnosAnteriorTPub, "TpubAnterior");
        this.writeDataOnHistorical(sheet, hechosUorgMesAnnosActualTPub, "TpubActual");

        return true;
    }

    /**
     * Escribir datos en la hoja
     *
     * @param sheet
     * @param data
     * @param llave
     * @return exito
     */
    private boolean writeDataOnHistorical(ExcelWorksheet sheet, LinkedList<HechosUorgMesAnno> data, String llave) {
        Map<String, String[]> posiciones = this.generarPosicionesParaHistorico().get(llave);
        int whereIM = 0;
        try {
            for (HechosUorgMesAnno hecho : data) {

                if (
                        hecho.getUnidad().equalsIgnoreCase("DTNO") ||
                                hecho.getUnidad().equalsIgnoreCase("DTES") ||
                                hecho.getUnidad().equalsIgnoreCase("DTOE") ||
                                hecho.getUnidad().equalsIgnoreCase("DTSR")) {
                    this.writeOnCellHistoricData(sheet, hecho, posiciones.get("DVLH"));
                } else {
                    this.writeOnCellHistoricData(sheet, hecho, posiciones.get(hecho.getUnidad()));
                }
                whereIM++;

            }
        } catch (Exception e) {
            System.out.println(whereIM);
        }

        return true;
    }

    /**
     * Llenar valores en las celdas
     *
     * @param sheet  Hoja
     * @param hecho  Objeto a insertar
     * @param celdas listado de las celdas
     */
    private void writeOnCellHistoricData(ExcelWorksheet sheet, HechosUorgMesAnno hecho, String[] celdas) {
        if (hecho.getMes().equalsIgnoreCase("enero"))
            sheet.getCell(celdas[0]).setValue(sheet.getCell(celdas[0]).getIntValue() + hecho.getCantHechos());
        else if (hecho.getMes().equalsIgnoreCase("febrero"))
            sheet.getCell(celdas[1]).setValue(sheet.getCell(celdas[1]).getIntValue() + hecho.getCantHechos());
        else if (hecho.getMes().equalsIgnoreCase("marzo"))
            sheet.getCell(celdas[2]).setValue(sheet.getCell(celdas[2]).getIntValue() + hecho.getCantHechos());
        else if (hecho.getMes().equalsIgnoreCase("abril"))
            sheet.getCell(celdas[3]).setValue(sheet.getCell(celdas[3]).getIntValue() + hecho.getCantHechos());
        else if (hecho.getMes().equalsIgnoreCase("mayo"))
            sheet.getCell(celdas[4]).setValue(sheet.getCell(celdas[4]).getIntValue() + hecho.getCantHechos());
        else if (hecho.getMes().equalsIgnoreCase("junio"))
            sheet.getCell(celdas[5]).setValue(sheet.getCell(celdas[5]).getIntValue() + hecho.getCantHechos());
        else if (hecho.getMes().equalsIgnoreCase("julio"))
            sheet.getCell(celdas[6]).setValue(sheet.getCell(celdas[6]).getIntValue() + hecho.getCantHechos());
        else if (hecho.getMes().equalsIgnoreCase("agosto"))
            sheet.getCell(celdas[7]).setValue(sheet.getCell(celdas[7]).getIntValue() + hecho.getCantHechos());
        else if (hecho.getMes().equalsIgnoreCase("septiembre"))
            sheet.getCell(celdas[8]).setValue(sheet.getCell(celdas[8]).getIntValue() + hecho.getCantHechos());
        else if (hecho.getMes().equalsIgnoreCase("octubre"))
            sheet.getCell(celdas[9]).setValue(sheet.getCell(celdas[9]).getIntValue() + hecho.getCantHechos());
        else if (hecho.getMes().equalsIgnoreCase("noviembre"))
            sheet.getCell(celdas[10]).setValue(sheet.getCell(celdas[10]).getIntValue() + hecho.getCantHechos());
        else if (hecho.getMes().equalsIgnoreCase("diciembre"))
            sheet.getCell(celdas[11]).setValue(sheet.getCell(celdas[11]).getIntValue() + hecho.getCantHechos());
    }


    /**
     * Metodo para escribir la tabla debajo en la hoja de historico
     *
     * @param worksheet hoja
     * @param date      fecha
     */
    private void writeHistorical2(ExcelWorksheet worksheet, Date date) {
        ExcelCell cell = worksheet.getCell("B41");
        cell.setStyle(Util.generarStilo());
        cell.setValue("Año");

        cell = worksheet.getCell("C41");
        cell.setStyle(Util.generarStilo());
        cell.setValue("Mes");

        cell = worksheet.getCell("D41");
        cell.setStyle(Util.generarStilo());
        cell.setValue("PExt");

        cell = worksheet.getCell("E41");
        cell.setStyle(Util.generarStilo());
        cell.setValue("TPúb");

        CellRange rangeB42B53 = worksheet.getCells().getSubrange("B42:B53");
        rangeB42B53.setMerged(true);
        rangeB42B53.setStyle(Util.generarBordes());
        rangeB42B53.setValue(date.toLocalDate().minusYears(1).getYear());

        CellRange rangeB54B65 = worksheet.getCells().getSubrange("B54:B65");
        rangeB54B65.setMerged(true);
        rangeB54B65.setStyle(Util.generarBordes());
        rangeB54B65.setValue(date.toLocalDate().getYear());

        GeneradoresTablas.writeMonthOnHistorical(worksheet.getCells().getSubrange("C42:C53"), Util.generarBordes());
        GeneradoresTablas.writeMonthOnHistorical(worksheet.getCells().getSubrange("C54:C65"), Util.generarBordes());

        worksheet.getCells().getSubrange("D42:E65").forEach(excelCell -> {
            excelCell.setValue(0);
            excelCell.setStyle(Util.generarBordes());
            if (excelCell.getColumn().getName().equalsIgnoreCase("e")) {
                excelCell.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(180, 214, 214));
            }
        });

        ArrayList<String> letrasAnnoAnterior = new ArrayList<>();
        for (int i = 42; i < 54; i++) {
            letrasAnnoAnterior.add("D" + i);
            letrasAnnoAnterior.add("E" + i);
        }

        ArrayList<String> letrasAnnoActual = new ArrayList<>();
        for (int i = 54; i < 66; i++) {
            letrasAnnoActual.add("D" + i);
            letrasAnnoActual.add("E" + i);
        }

        UtilToGenerateTables.llenarCamposCantHechosByAnno(
                ServiceLocator.getHechosService().cantidadHechosByAnno(date.toLocalDate().minusYears(1).getYear()),
                worksheet,
                letrasAnnoAnterior
        );
        UtilToGenerateTables.llenarCamposCantHechosByAnno(
                ServiceLocator.getHechosService().cantidadHechosByAnno(date.toLocalDate().getYear()),
                worksheet,
                letrasAnnoActual
        );


    }

    /**
     * Escribe las formulas horizontales en la hoja historico
     *
     * @param range   rango de celdas
     * @param letter1 letra con que empieza
     * @param letter2 letra con que acaba
     */
    private void writeFormulasTotalHorizontalsonHistorical(CellRange range, String letter1, String letter2) {
        range.forEach(
                excelCell -> {
                    int position = excelCell.getRow().getIndex() + 1;
                    excelCell.setFormula("=SUM(" + letter1 + position + ":" + letter2 + position + ")");
                    excelCell.calculate();
                    excelCell.setStyle(Util.generarStilo());
                }
        );
    }

    private boolean generateGraphicalInformation(ExcelFile workbook, Date date) {

        LinkedList<CellRange> cellRange = GeneradoresTablas.generarTablasParaGraficos(workbook.addWorksheet("Datos para graficos"), date.toLocalDate(), 1, true);
        ExcelWorksheet sheet = workbook.addWorksheet("Graficos");

        workbook.getWorksheet(0).getCell("H15").calculate();
        workbook.getWorksheet(0).getCell("H29").calculate();
        int value1 = workbook.getWorksheet(0).getCell("H15").getIntValue();
        int value2 = workbook.getWorksheet(0).getCell("H29").getIntValue();

        //Generar grafico de barras comportamiento del delito
        GraphicGenerator.generarGraficoBarrasDobles(
                "Comportamiento del Delito",
                "B2",
                "M16",
                sheet,
                date.toLocalDate(),
                workbook.getWorksheet(0).getCells().getSubrange("B29:G29"),
                workbook.getWorksheet(0).getCells().getSubrange("B15:G15"),
                workbook.getWorksheet(0).getCells().getSubrange("B1:G1"),
                value1, value2
        );


        //Generar grafico de barras PExt Comportamiento Territorial
        GraphicGenerator.generarGraficoBarrasDobles(
                "Planta Exterior - Comportamiento Territorial",
                "B18",
                "M32",
                sheet,
                date.toLocalDate(),
                workbook.getWorksheet(1).getCells().getSubrange("B10:B25"),
                workbook.getWorksheet(1).getCells().getSubrange("C10:C25"),
                workbook.getWorksheet(1).getCells().getSubrange("A10:A25"),
                Util.
                        cantPextTpubTotal
                                (ServiceLocator.getHechosService().cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre
                                        (Date.valueOf(date.toLocalDate().minusYears(1)), 1)),
                Util.cantPextTpubTotal(
                        ServiceLocator.getHechosService().cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre(Date.valueOf(date.toLocalDate()), 1)
                )
        );

        //Generar grafico de barras TPub Comportamiento territorial
        GraphicGenerator.generarGraficoBarrasDobles(
                "Telefonía Pública - Comportamiento Territorial\n",
                "B34",
                "M48",
                sheet,
                date.toLocalDate(),
                workbook.getWorksheet(1).getCells().getSubrange("E10:E25"),
                workbook.getWorksheet(1).getCells().getSubrange("F10:F25"),
                workbook.getWorksheet(1).getCells().getSubrange("A10:A25"),
                Util.
                        cantPextTpubTotal
                                (ServiceLocator.getHechosService().cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre
                                        (Date.valueOf(date.toLocalDate().minusYears(1)), 2)),
                Util.cantPextTpubTotal(
                        ServiceLocator.getHechosService().cantidadHechosPextPorUnidadOrganizativaAnnosFiscaliaCierre(Date.valueOf(date.toLocalDate()), 2)
                )
        );

        GraphicGenerator.generarGraficoBarrasDoblesParaRobosHurtos(
                "Comportamiento Territorial: Robos y hurtos",
                "U88",
                "AC102",
                sheet
                , date.toLocalDate()
                , cellRange.get(13)
                , cellRange.get(14)
                , cellRange.get(15)
                , cellRange.get(12)
        );


        //Generar grafico pext Municipios mas afectados

        generarGraficoBarras(
                cellRange.get(3),
                sheet,
                "B50",
                "I70",
                "Planta Exterior - Municipios Más Afectados",
                false);

        //Generar grafico tpub municipios mas afectados

        generarGraficoBarras(
                cellRange.get(8),
                sheet,
                "K50",
                "Q70",
                "Telefonía Pública - Municipios Más Afectados",
                false);

        //Generar grafico de pastel pext material mas afectado
        generarGraficoPastel(
                cellRange.get(1),
                sheet,
                "B72",
                "I92",
                "Planta Exterior - Material Afectado",
                1);

        //generar grafico de pastel tpub tipos de vandalismo
        generarGraficoPastel(
                cellRange.get(5),
                sheet,
                "K72",
                "S92",
                "Telefonía Pública - Tipos de Vandalismos",
                1);


        //generar grafico de puntos pext comportamiento mensual
        generarGraficoLineas(
                cellRange.get(2),
                sheet,
                "O2",
                "Y22",
                "Planta Exterior - Comportamiento Mensual",
                false);

        //generar grafico de puntos tpub comportamiento mensual
        generarGraficoLineas(
                cellRange.get(6),
                sheet,
                "O25",
                "Y45",
                "Telefonía Pública - Comportamiento Mensual",
                false);

        generarGraficoLineas(
                cellRange.get(10),
                sheet,
                "S50",
                "Z62",
                "Robos - Comportamiento Mensual",
                false);

        generarGraficoLineas(
                cellRange.get(11),
                sheet,
                "U72",
                "AB85",
                "Hurtos - Comportamiento Mensual",
                false
        );


        return true;
    }

    /**
     * Escribe las formulas verticales en la hoja historico
     *
     * @param range rango de celdas
     * @param first numero con que empieza
     * @param last  numero con que acaba
     */
    private void writeFormulasTotalVerticalsonHistorical(CellRange range, int first, int last) {
        range.forEach(
                excelCell -> {
                    String letter = excelCell.getColumn().getName();
                    excelCell.setFormula("=SUM(" + letter + first + ":" + letter + last + ")");
                    excelCell.calculate();
                    excelCell.setStyle(Util.generarStilo());
                }
        );
    }

    /**
     * Escribe los ceros en la tabla y annade los bordes en la hoja historico
     *
     * @param range rango de celdas
     */
    private void writeZeerosAndStylingOnHistorical(CellRange range) {
        range.forEach(excelCell -> {
            excelCell.setStyle(Util.generarBordes());
            excelCell.setValue(0);
        });
    }

    /**
     * Escribe las unidades organizativas en la hoja historico
     *
     * @param range rango de celdas
     */
    private void writeUOsOnHistorical(CellRange range) {
        int valor = 0;
        for (ExcelCell cell : range) {
            cell.setStyle(Util.generarBordes());
            cell.setValue(Util.unidades_organizativa[valor]);
            valor++;
        }
    }


    private void aplicarStilo(CellRange range) {
        range.forEach(excelCell -> {
            excelCell.calculate();
            if (excelCell.getIntValue() <= 0) {
                excelCell.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(161, 214, 214));
            } else {
                excelCell.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(214, 0, 36));
                excelCell.getStyle().getFont().setColor(SpreadsheetColor.fromName(ColorName.WHITE));
            }
        });
    }


    private ArrayList<String> devolverListaLetras(int positionInit, int positionEnd) {
        ArrayList<String> position = new ArrayList<>();
        for (int i = positionInit; i < positionEnd; i++) {
            position.add("B" + i);
            position.add("C" + i);
            position.add("D" + i);
            position.add("E" + i);
            position.add("F" + i);
            position.add("G" + i);
        }
        return position;
    }

    private ArrayList<String> devolverListaLetras(String letra1, String letra2) {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 10; i < 26; i++) {
            strings.add(letra1 + i);
            strings.add(letra2 + i);
        }
        return strings;
    }

    /**
     * Escribe los datos en el resumen mensual
     *
     * @param hechosMesAnnos arreglo de hechos
     * @param positions      posiciones de las celdas
     * @param sheet          hoja a escribir
     * @return si la tarea fue completada
     */
    private boolean dataWritted(LinkedList<HechosMesAnno> hechosMesAnnos, ArrayList<String> positions, ExcelWorksheet sheet) {
        int choosePosition = 0;
        for (HechosMesAnno hechosMesAnno : hechosMesAnnos
        ) {
            if (hechosMesAnno.getMes().equalsIgnoreCase("enero")) {
                choosePosition = aplicarDatos(sheet, positions, hechosMesAnno, choosePosition);
            } else if (hechosMesAnno.getMes().equalsIgnoreCase("febrero")) {
                choosePosition = aplicarDatos(sheet, positions, hechosMesAnno, choosePosition);
            } else if (hechosMesAnno.getMes().equalsIgnoreCase("marzo")) {
                choosePosition = aplicarDatos(sheet, positions, hechosMesAnno, choosePosition);
            } else if (hechosMesAnno.getMes().equalsIgnoreCase("abril")) {
                choosePosition = aplicarDatos(sheet, positions, hechosMesAnno, choosePosition);
            } else if (hechosMesAnno.getMes().equalsIgnoreCase("mayo")) {
                choosePosition = aplicarDatos(sheet, positions, hechosMesAnno, choosePosition);
            } else if (hechosMesAnno.getMes().equalsIgnoreCase("junio")) {
                choosePosition = aplicarDatos(sheet, positions, hechosMesAnno, choosePosition);
            } else if (hechosMesAnno.getMes().equalsIgnoreCase("julio")) {
                choosePosition = aplicarDatos(sheet, positions, hechosMesAnno, choosePosition);
            } else if (hechosMesAnno.getMes().equalsIgnoreCase("agosto")) {
                choosePosition = aplicarDatos(sheet, positions, hechosMesAnno, choosePosition);
            } else if (hechosMesAnno.getMes().equalsIgnoreCase("septiembre")) {
                choosePosition = aplicarDatos(sheet, positions, hechosMesAnno, choosePosition);
            } else if (hechosMesAnno.getMes().equalsIgnoreCase("octubre")) {
                choosePosition = aplicarDatos(sheet, positions, hechosMesAnno, choosePosition);
            } else if (hechosMesAnno.getMes().equalsIgnoreCase("noviembre")) {
                choosePosition = aplicarDatos(sheet, positions, hechosMesAnno, choosePosition);
            } else if (hechosMesAnno.getMes().equalsIgnoreCase("diciembre")) {
                choosePosition = aplicarDatos(sheet, positions, hechosMesAnno, choosePosition);
            }

        }

        return true;
    }

    /**
     * Escribe los datos correspondientes a su hoja
     *
     * @param sheet          hoja a escribir
     * @param positions      posiciones o letras
     * @param hechosMesAnno  Objeto a escribir
     * @param choosePosition posicion actual en el arreglo de positions
     * @return indice de la posicion
     */
    private int aplicarDatos(ExcelWorksheet sheet, ArrayList<String> positions, HechosMesAnno hechosMesAnno, int choosePosition) {
        sheet.getCell(positions.get(choosePosition++)).setValue(hechosMesAnno.getPext());
        sheet.getCell(positions.get(choosePosition++)).setValue(hechosMesAnno.getTpub());
        sheet.getCell(positions.get(choosePosition++)).setValue(hechosMesAnno.getRobo());
        sheet.getCell(positions.get(choosePosition++)).setValue(hechosMesAnno.getHurto());
        sheet.getCell(positions.get(choosePosition++)).setValue(hechosMesAnno.getFraude());
        sheet.getCell(positions.get(choosePosition++)).setValue(hechosMesAnno.getOtros_delitos());
        return choosePosition;
    }

    private boolean writeDataResumen2(ExcelWorksheet sheet, ArrayList<String> positions, LinkedList<HechosUOrgAnno> data) {
        boolean completed = false;
        if (data != null) {
            completed = true;
            data.forEach(hechosUOrgAnno -> {
                if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtpr")) {
                    sheet.getCell(positions.get(0)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(1)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtar")) {
                    sheet.getCell(positions.get(2)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(3)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtmy")) {
                    sheet.getCell(positions.get(4)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(5)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (
                        hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtno")
                                || hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtsr")
                                || hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtoe")
                                || hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtes")
                ) {
                    sheet.getCell(positions.get(6)).setValue(sheet.getCell(positions.get(6)).getIntValue() + hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(7)).setValue(sheet.getCell(positions.get(7)).getIntValue() + hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtij")) {
                    sheet.getCell(positions.get(8)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(9)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtmz")) {
                    sheet.getCell(positions.get(10)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(11)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtcf")) {
                    sheet.getCell(positions.get(12)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(13)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtvc")) {
                    sheet.getCell(positions.get(14)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(15)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtss")) {
                    sheet.getCell(positions.get(16)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(17)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtca")) {
                    sheet.getCell(positions.get(18)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(19)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtcm")) {
                    sheet.getCell(positions.get(20)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(21)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtlt")) {
                    sheet.getCell(positions.get(22)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(23)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtho")) {
                    sheet.getCell(positions.get(24)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(25)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtgr")) {
                    sheet.getCell(positions.get(26)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(27)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtsc")) {
                    sheet.getCell(positions.get(28)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(29)).setValue(hechosUOrgAnno.getCant_tpub());
                } else if (hechosUOrgAnno.getUnidad().equalsIgnoreCase("dtgt")) {
                    sheet.getCell(positions.get(30)).setValue(hechosUOrgAnno.getCant_pext());
                    sheet.getCell(positions.get(31)).setValue(hechosUOrgAnno.getCant_tpub());
                }
            });
        }
        return completed;
    }

    /**
     * Genera las posiciones para el Historico
     *
     * @return mapa segun el anno que contiene la direccion territorial y un arreglo de posiciones
     */
    private Map<String, Map<String, String[]>> generarPosicionesParaHistorico() {
        Map<String, Map<String, String[]>> posiciones = new LinkedHashMap<>();

        Map<String, String[]> posicionesAnteriorPext = new LinkedHashMap<>();
        for (int i = 0; i < Util.unidades_organizativa.length; i++) {
            posicionesAnteriorPext.put(Util.unidades_organizativa[i], this.obtenerLetrasCN(i + 4));
        }
        posiciones.put("PextAnterior", posicionesAnteriorPext);

        Map<String, String[]> posicionesActualPext = new LinkedHashMap<>();
        for (int i = 0; i < Util.unidades_organizativa.length; i++) {
            posicionesActualPext.put(Util.unidades_organizativa[i], this.obtenerLetrasPAA(i + 4));
        }
        posiciones.put("PextActual", posicionesActualPext);

        Map<String, String[]> posicionesTPubAnterior = new LinkedHashMap<>();
        for (int i = 0; i < Util.unidades_organizativa.length; i++) {
            posicionesTPubAnterior.put(Util.unidades_organizativa[i], this.obtenerLetrasCN(i + 22));
        }
        posiciones.put("TpubAnterior", posicionesTPubAnterior);

        Map<String, String[]> posicionesTpubActual = new LinkedHashMap<>();
        for (int i = 0; i < Util.unidades_organizativa.length; i++) {
            posicionesTpubActual.put(Util.unidades_organizativa[i], this.obtenerLetrasPAA(i + 22));
        }
        posiciones.put("TpubActual", posicionesTpubActual);

        return posiciones;
    }

    private String[] obtenerLetrasCN(int numero) {
        String[] letras = {"C" + numero, "D" + numero, "E" + numero, "F" + numero, "G" + numero, "H" + numero
                , "I" + numero, "J" + numero, "K" + numero, "L" + numero, "M" + numero, "N" + numero};
        return letras;
    }

    private String[] obtenerLetrasPAA(int numero) {
        String[] letras = {"P" + numero, "Q" + numero, "R" + numero, "S" + numero, "T" + numero, "U" + numero, "V" + numero, "W" + numero,
                "X" + numero, "Y" + numero, "Z" + numero, "AA" + numero};
        return letras;
    }


}
