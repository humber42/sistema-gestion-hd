package informes_generate;

import com.gembox.spreadsheet.*;
import models.*;
import services.ServiceLocator;
import util.Util;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static util.Util.*;

public class GeneradoresTablas {

    public static boolean generarTotales(LocalDate localDate, ExcelWorksheet sheet, ArrayList<Integer> datos) {

        ExcelWorksheet hojaTotales = sheet;
        hojaTotales.getCell("A1").setValue("Año");
        hojaTotales.getCell("B1").setValue("PExt");
        hojaTotales.getCell("C1").setValue("TPúb");
        hojaTotales.getCell("D1").setValue("Total");
        for (int i = 0; i < 4; i++) {
            hojaTotales.getCell(0, i).setStyle(generarStilo());
        }
        for (int i = 0; i < 5; i++) {
            hojaTotales.getRow(i).setHeight(5, LengthUnit.ZERO_CHARACTER_WIDTH);
        }
        for (int i = 0; i < 4; i++) {
            if (i == 0)
                hojaTotales.getColumn(i).setWidth(10, LengthUnit.ZERO_CHARACTER_WIDTH);
            else
                hojaTotales.getColumn(i).setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
        }
        for (int row = 1; row < 5; row++) {
            for (int column = 0; column < 4; column++) {
                hojaTotales.getCell(row, column).setStyle(estiloColumnasHojasTotales());
            }
        }

        hojaTotales.getPrintOptions().setFitWorksheetWidthToPages(1);
        ArrayList<Integer> totalesPEXT = datos;

        hojaTotales.getCell("A2").setValue(localDate.getYear() - 1);
        hojaTotales.getCell("A3").setValue(localDate.getYear());
        hojaTotales.getCell("A4").setValue("Dif");
        hojaTotales.getCell("A5").setValue("%");

        hojaTotales.getCell("B2").setValue(totalesPEXT.get(0));
        hojaTotales.getCell("C2").setValue(totalesPEXT.get(2));
        hojaTotales.getCell("B3").setValue(totalesPEXT.get(1));
        hojaTotales.getCell("C3").setValue(totalesPEXT.get(3));

        hojaTotales.getCell("D2").setFormula("=B2+C2");
        hojaTotales.getCell("D3").setFormula("=C3+B3");
        hojaTotales.getCell("D2").calculate();
        hojaTotales.getCell("D3").calculate();

        hojaTotales.getCells().getSubrange("B2:C3").getStyle()
                .getFillPattern().setSolid(SpreadsheetColor.fromName(ColorName.WHITE));
        hojaTotales.getCell("B4").setFormula("=IF(B3>B2,(B3-B2)&\" más\",IF(B2>B3,(B2-B3)&\" menos\",\"Igual\"))");
        hojaTotales.getCell("B4").calculate();

        hojaTotales.getCell("C4").setFormula("=IF(C3>C2,(C3-C2)&\" más\",IF(C2>C3,(C2-C3)&\" menos\",\"Igual\"))");
        hojaTotales.getCell("C4").calculate();

        hojaTotales.getCell("D4").setFormula("=IF(D3>D2,(D3-D2)&\" más\",IF(D2>D3,(D2-D3)&\" menos\",\"Igual\"))");
        hojaTotales.getCell("D4").calculate();


        hojaTotales.getCell("B5").setFormula("=IF(B3<B2,ROUND(100*(B2-B3)/B2,0)" + "  & " + " \"% Red\",IF(B3=B2,0%,ROUND(100*(B3-B2)/B2,0) & \"% Inc\"))");
        hojaTotales.getCell("B5").calculate();

        hojaTotales.getCell("C5").setFormula("=IF(C3<C2,ROUND(100*(C2-C3)/C2,0) & \"% Red\",IF(C3=C2,0%,ROUND(100*(C3-C2)/C2,0) & \"% Inc\"))");
        hojaTotales.getCell("C5").calculate();

        hojaTotales.getCell("D5").setFormula("=IF(D3<D2,ROUND(100*(D2-D3)/D2,0) & \"% Red\",IF(D3=D2,0%,ROUND(100*(D3-D2)/D2,0) & \"% Inc\"))");
        hojaTotales.getCell("D5").calculate();

        return true;
    }

    public static boolean generarTotalesHurtosRobos(LocalDate localDate, ExcelWorksheet sheet) {
        ExcelWorksheet hojaTotales = sheet;
        hojaTotales.getCell("F1").setValue("Año");
        hojaTotales.getCell("G1").setValue("Robo");
        hojaTotales.getCell("H1").setValue("Hurto");
        hojaTotales.getCell("I1").setValue("Total");
        for (int i = 5; i < 9; i++) {
            hojaTotales.getCell(0, i).setStyle(generarStilo());
        }
        for (int i = 0; i < 5; i++) {
            hojaTotales.getRow(i).setHeight(5, LengthUnit.ZERO_CHARACTER_WIDTH);
        }
        for (int i = 5; i < 9; i++) {
            if (i == 5)
                hojaTotales.getColumn(i).setWidth(10, LengthUnit.ZERO_CHARACTER_WIDTH);
            else
                hojaTotales.getColumn(i).setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
        }
        for (int row = 1; row < 5; row++) {
            for (int column = 5; column < 9; column++) {
                hojaTotales.getCell(row, column).setStyle(estiloColumnasHojasTotales());
            }
        }

        hojaTotales.getPrintOptions().setFitWorksheetWidthToPages(1);

        int actualRobo = ServiceLocator.getHechosService().cantHechosPextCierreFiscalia(Date.valueOf(localDate), 3);
        int anteriorRobo = ServiceLocator.getHechosService().cantHechosPextCierreFiscalia(Date.valueOf(localDate.minusYears(1)), 3);
        int actualHurto = ServiceLocator.getHechosService().cantHechosPextCierreFiscalia(Date.valueOf(localDate), 4);
        int anteriorHurto = ServiceLocator.getHechosService().cantHechosPextCierreFiscalia(Date.valueOf(localDate.minusYears(1)), 4);

        hojaTotales.getCell("F2").setValue(localDate.getYear() - 1);
        hojaTotales.getCell("F3").setValue(localDate.getYear());
        hojaTotales.getCell("F4").setValue("Dif");
        hojaTotales.getCell("F5").setValue("%");

        hojaTotales.getCell("G2").setValue(anteriorRobo);
        hojaTotales.getCell("H2").setValue(anteriorHurto);
        hojaTotales.getCell("G3").setValue(actualRobo);
        hojaTotales.getCell("H3").setValue(actualHurto);

        hojaTotales.getCell("I2").setFormula("=G2+H2");
        hojaTotales.getCell("I3").setFormula("=G3+H3");
        hojaTotales.getCell("I2").calculate();
        hojaTotales.getCell("I3").calculate();

        hojaTotales.getCells().getSubrange("G2:H3").getStyle()
                .getFillPattern().setSolid(SpreadsheetColor.fromName(ColorName.WHITE));
        hojaTotales.getCell("G4").setFormula("=IF(G3>G2,(G3-G2)&\" más\",IF(G2>G3,(G2-G3)&\" menos\",\"Igual\"))");
        hojaTotales.getCell("G4").calculate();

        hojaTotales.getCell("H4").setFormula("=IF(H3>H2,(H3-H2)&\" más\",IF(H2>H3,(H2-H3)&\" menos\",\"Igual\"))");
        hojaTotales.getCell("H4").calculate();

        hojaTotales.getCell("I4").setFormula("=IF(I3>I2,(I3-I2)&\" más\",IF(I2>I3,(I2-I3)&\" menos\",\"Igual\"))");
        hojaTotales.getCell("I4").calculate();


        hojaTotales.getCell("G5").setFormula("=IF(G3<G2,ROUND(100*(G2-G3)/G2,0)" + "  & " + " \"% Red\",IF(G3=G2,0%,ROUND(100*(G3-G2)/G2,0) & \"% Inc\"))");
        hojaTotales.getCell("G5").calculate();

        hojaTotales.getCell("H5").setFormula("=IF(H3<H2,ROUND(100*(H2-H3)/H2,0) & \"% Red\",IF(H3=H2,0%,ROUND(100*(H3-H2)/H2,0) & \"% Inc\"))");
        hojaTotales.getCell("H5").calculate();

        hojaTotales.getCell("I5").setFormula("=IF(I3<I2,ROUND(100*(I2-I3)/I2,0) & \"% Red\",IF(I3=I2,0%,ROUND(100*(I3-I2)/I2,0) & \"% Inc\"))");
        hojaTotales.getCell("I5").calculate();

        return true;
    }

    /**
     * Escribe los meses en la hoja de historico
     *
     * @param range rango de celdas
     * @param style estilo de celda
     */
    public static void writeMonthOnHistorical(CellRange range, CellStyle style) {
        int valor = 0;
        for (ExcelCell cell : range) {
            cell.setStyle(style);
            cell.setValue(meses[valor]);
            valor++;
        }
    }


    public static LinkedList<CellRange> generarTablasParaGraficos(ExcelWorksheet worksheet, LocalDate date, int titleRow) {
        LinkedList<CellRange> ranges = new LinkedList<>();
        ExcelWorksheet sheet = worksheet;
        LinkedList<MaterialsFiscaliaModels> materialsFiscaliaListAnnoAnterior = new LinkedList<>();
        LinkedList<MaterialsFiscaliaModels> materialsFiscaliaListAnnoActual = new LinkedList<>();
        ServiceLocator.getTipoMaterialesService().materialesPorAnno(Date.valueOf(date.minusYears(1))).forEach(
                materialsFiscaliaModels -> {
                    if (materialsFiscaliaModels.getCantHechos() != 0) {
                        materialsFiscaliaListAnnoAnterior.add(materialsFiscaliaModels);
                    }
                }
        );
        ServiceLocator.getTipoMaterialesService().materialesPorAnno(Date.valueOf(date)).forEach(
                materialsFiscaliaModels -> {
                    if (materialsFiscaliaModels.getCantHechos() != 0) {
                        materialsFiscaliaListAnnoActual.add(materialsFiscaliaModels);
                    }
                }
        );

        LinkedList<AfectacionFiscaliaModels> afectacionFiscaliaModelsAnnoAnterior = new LinkedList<>();
        LinkedList<AfectacionFiscaliaModels> afectacionFiscaliaModelsAnnoActual = new LinkedList<>();
        ServiceLocator.getHechosService().cantidadAfectacionesHechosFiscalia(Date.valueOf(date.minusYears(1))).forEach(
                afectacionFiscaliaModels -> {
                    if (afectacionFiscaliaModels.getCantHechos() != 0) {
                        afectacionFiscaliaModelsAnnoAnterior.add(afectacionFiscaliaModels);
                    }
                }
        );
        ServiceLocator.getHechosService().cantidadAfectacionesHechosFiscalia(Date.valueOf(date)).forEach(
                afectacionFiscaliaModels -> {
                    if (afectacionFiscaliaModels.getCantHechos() != 0) {
                        afectacionFiscaliaModelsAnnoActual.add(afectacionFiscaliaModels);
                    }
                }
        );


        sheet.getCell("A" + titleRow++).setValue("Datos generados para los graficos, NO TOCAR");

        sheet.getCell("A" + titleRow).setValue("Material Afectado");
        sheet.getCell("B" + titleRow).setValue("Cant Hechos");
        sheet.getCell("C" + titleRow).setValue("Cant Material");

        int row = titleRow;
        int column = 0;
        int rowInitial = row;


        row = writeCellMaterialesGrafico(row, column, materialsFiscaliaListAnnoAnterior, sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial - 1, column, row - 1, 2));
        rowInitial = row;


        row = writeCellMaterialesGrafico(row, column, materialsFiscaliaListAnnoActual, sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial - 1, column, row - 1, 2));
        rowInitial = row;


        sheet.getCell(row++, column).setValue("Cant-Hechos");
        CellRange range = sheet.getParent().getWorksheet(1).getCells().getSubrange("C80:C103");
        for (ExcelCell cell : range) {
            if ((int) cell.getValue() != 0) {
                sheet.getCell(row, column).setValue(cell.getValue());
                row++;
            }
        }
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial + 1, 0, row - 1, 0));
        rowInitial = row;


        row = writeCellHechosByMunicipio(row, column, ServiceLocator.getHechosService().obtenerHechosPorMunicipio(Date.valueOf(date), 1), sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial, 0, row - 1, 1));
        rowInitial = row;


        row = writeCellAfectacionGrafico(row, column, afectacionFiscaliaModelsAnnoAnterior, sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial - 1, column, row - 1, 2));
        rowInitial = row;

        row = writeCellAfectacionGrafico(row, column, afectacionFiscaliaModelsAnnoActual, sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial - 1, column, row - 1, 2));
        rowInitial = row;

        sheet.getCell(row++, column).setValue("Cant-Hechos-Tpub");
        CellRange range2 = sheet.getParent().getWorksheet(1).getCells().getSubrange("D80:D103");
        for (ExcelCell cell : range2) {
            if ((int) cell.getValue() != 0) {
                sheet.getCell(row, column).setValue(cell.getValue());
                row++;
            }
        }
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial+1, 0, row-1, 0));
        //rowInitial = row;


        column = 4;
        row = 8;
        rowInitial = row;
        row = writeCellAfectacionTPubGrafico(row, column,
                ServiceLocator.getHechosService().calculoServiciosAfectadosEstacionesPublicas(Date.valueOf(date))
                , sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial - 1, column, row - 1, column + 1));
        rowInitial = row;


        row = writeCellHechosByMunicipioServiciosAfectados(row, column, ServiceLocator.getHechosService().serviciosAfectadosPorMunicipio(Date.valueOf(date), 2), sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial, column, row - 1, column + 1));
        rowInitial = row;


        row = writeCellAfectaciones(row, column, ServiceLocator.getHechosService().afectacionesTPubMunicipio(Date.valueOf(date)), sheet, date);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial, column, row - 1, column + 1));

        //range12
        sheet.getCell(row++, column).setValue("HurtosRobosPrevenidos");
//        int rowInstant= row;
//        int columnInstant = column;
        LinkedList<HurtosRobosPrevUorg> hurtosRobosPrevUorgs = ServiceLocator.getHechosService().obtenerRobosHurtosPrev(Date.valueOf(date));
        List<HurtosRobosPrevUorg> processedListHurtosRobosPrevUorgs = Util.cleaningAndFormatingList(hurtosRobosPrevUorgs);

        int rowInstant = row;
        for (HurtosRobosPrevUorg hecho : processedListHurtosRobosPrevUorgs) {
            int columnInstant = column;
            sheet.getCell(rowInstant, columnInstant++).setValue(hecho.getUorg());
            sheet.getCell(rowInstant, columnInstant++).setValue(hecho.getCantRobos());
            sheet.getCell(rowInstant, columnInstant++).setValue(hecho.getCantHurtos());
            sheet.getCell(rowInstant, columnInstant++).setValue(hecho.getCantPrevenidos());
            rowInstant++;
        }

        /**
         * Defining cell ranges
         * range 12
         * range 13
         * range 14
         * range 15
         * **/
        //Unidades Organizativas
        ranges.add(sheet.getCells().getSubrangeAbsolute(row, column, rowInstant, column));
        //Cantidad Robos
        ranges.add(sheet.getCells().getSubrangeAbsolute(row, column + 1, rowInstant, column + 1));
        // Cantidad de Hurtos
        ranges.add(sheet.getCells().getSubrangeAbsolute(row, column + 2, rowInstant, column + 2));
        // Cantidad de Prevenidos
        ranges.add(sheet.getCells().getSubrangeAbsolute(row, column + 3, rowInstant, column + 3));


        return ranges;
    }

    public static LinkedList<CellRange> generarTablasParaGraficos(ExcelWorksheet worksheet, LocalDate date, int titleRow, boolean centroDireccion) {
        LinkedList<CellRange> ranges = new LinkedList<>();
        ExcelWorksheet sheet = worksheet;

        LinkedList<MaterialsFiscaliaModels> materialsFiscaliaListAnnoAnterior = new LinkedList<>();
        LinkedList<MaterialsFiscaliaModels> materialsFiscaliaListAnnoActual = new LinkedList<>();
        ServiceLocator.getTipoMaterialesService().materialesPorAnno(Date.valueOf(date.minusYears(1))).forEach(
                materialsFiscaliaModels -> {
                    if (materialsFiscaliaModels.getCantHechos() != 0) {
                        materialsFiscaliaListAnnoAnterior.add(materialsFiscaliaModels);
                    }
                }
        );
        ServiceLocator.getTipoMaterialesService().materialesPorAnno(Date.valueOf(date)).forEach(
                materialsFiscaliaModels -> {
                    if (materialsFiscaliaModels.getCantHechos() != 0) {
                        materialsFiscaliaListAnnoActual.add(materialsFiscaliaModels);
                    }
                }
        );

        LinkedList<AfectacionFiscaliaModels> afectacionFiscaliaModelsAnnoAnterior = new LinkedList<>();
        LinkedList<AfectacionFiscaliaModels> afectacionFiscaliaModelsAnnoActual = new LinkedList<>();
        ServiceLocator.getHechosService().cantidadAfectacionesHechosFiscalia(Date.valueOf(date.minusYears(1))).forEach(
                afectacionFiscaliaModels -> {
                    if (afectacionFiscaliaModels.getCantHechos() != 0) {
                        afectacionFiscaliaModelsAnnoAnterior.add(afectacionFiscaliaModels);
                    }
                }
        );
        ServiceLocator.getHechosService().cantidadAfectacionesHechosFiscalia(Date.valueOf(date)).forEach(
                afectacionFiscaliaModels -> {
                    if (afectacionFiscaliaModels.getCantHechos() != 0) {
                        afectacionFiscaliaModelsAnnoActual.add(afectacionFiscaliaModels);
                    }
                }
        );


        sheet.getCell("A" + titleRow++).setValue("Datos generados para los graficos, NO TOCAR");

        sheet.getCell("A" + titleRow).setValue("Material Afectado");
        sheet.getCell("B" + titleRow).setValue("Cant Hechos");
        sheet.getCell("C" + titleRow).setValue("Cant Material");

        int row = titleRow;
        int column = 0;
        int rowInitial = row;

        // range 0
        row = writeCellMaterialesGrafico(row, column, materialsFiscaliaListAnnoAnterior, sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial - 1, column, row - 1, 2));
        rowInitial = row;


        //range 1
        row = writeCellMaterialesGrafico(row, column, materialsFiscaliaListAnnoActual, sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial - 1, column, row - 1, 2));
        rowInitial = row;

        //range 2
        //Se toma directo de las hojas del libro de calculo para grafico de lineas
        sheet.getCell(row++, column).setValue("Cant-Hechos");
        CellRange range = sheet.getParent().getWorksheet(2).getCells().getSubrange("D42:D65");
        for (ExcelCell cell : range) {
            if ((int) cell.getValue() != 0) {
                sheet.getCell(row, column).setValue(cell.getValue());
                row++;
            }
        }
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial+1, 0, row-1, 0));
        rowInitial = row;

        //range 3
        row = writeCellHechosByMunicipio(row, column, ServiceLocator.getHechosService().obtenerHechosPorMunicipio(Date.valueOf(date), 1), sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial, 0, row - 1, 1));
        rowInitial = row;

        //range 4
        row = writeCellAfectacionGrafico(row, column, afectacionFiscaliaModelsAnnoAnterior, sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial, column, row, 2));
        rowInitial = row;

        //range 5
        row = writeCellAfectacionGrafico(row, column, afectacionFiscaliaModelsAnnoActual, sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial - 1, column, row - 1, 2));
        rowInitial = row;

        //range 6
        //Se toma directo de las hojas del libro de calculo para grafico de lineas
        sheet.getCell(row++, column).setValue("Cant-Hechos-Tpub");
        CellRange range2 = sheet.getParent().getWorksheet(2).getCells().getSubrange("E42:E65");
        for (ExcelCell cell : range2) {
            if ((int) cell.getValue() != 0) {
                sheet.getCell(row, column).setValue(cell.getValue());
                row++;
            }
        }
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial+1, 0, row-1, 0));
        //rowInitial = row;

        //range 7
        column = 4;
        row = titleRow;
        rowInitial = row;
        row = writeCellAfectacionTPubGrafico(row, column,
                ServiceLocator.getHechosService().calculoServiciosAfectadosEstacionesPublicas(Date.valueOf(date))
                , sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial - 1, column, row - 1, column + 1));
        rowInitial = row;


        ///Cambiar
        //range 8
        row = writeCellHechosByMunicipio(row, column, ServiceLocator.getHechosService().obtenerHechosPorMunicipio(Date.valueOf(date), 2), sheet);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial, column, row - 1, column + 1));
        rowInitial = row;

        //range 9
        row = writeCellAfectaciones(row, column, ServiceLocator.getHechosService().afectacionesTPubMunicipio(Date.valueOf(date)), sheet, date);
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial, column, row - 1, column + 1));
        rowInitial = row;

        LinkedList<HechosByAnno> hechosByAnnosActual = ServiceLocator.getHechosService().cantidadRobosHurtosByAnno(date.getYear());
        LinkedList<HechosByAnno> hechosByAnnosAnterior = ServiceLocator.getHechosService().cantidadRobosHurtosByAnno(date.minusYears(1).getYear());
        //range10
        sheet.getCell(row++, column).setValue("Cant-Robos");
        for (HechosByAnno hechos : hechosByAnnosAnterior) {
            sheet.getCell(row, column).setValue(hechos.getCantPext());
            row++;
        }
        for (HechosByAnno hechos : hechosByAnnosActual) {
            sheet.getCell(row, column).setValue(hechos.getCantPext());
            row++;
        }
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial + 1, column, row - 1, column));
        rowInitial = row;

        //range11
        sheet.getCell(row++, column).setValue("Cant-Hurtos");
        for (HechosByAnno hechos : hechosByAnnosAnterior) {
            sheet.getCell(row, column).setValue(hechos.getCantTpub());
            row++;
        }
        for (HechosByAnno hechos : hechosByAnnosActual) {
            sheet.getCell(row, column).setValue(hechos.getCantTpub());
            row++;
        }
        ranges.add(sheet.getCells().getSubrangeAbsolute(rowInitial + 1, column, row - 1, column));
        rowInitial = row;

        //range12
        sheet.getCell(row++, column).setValue("HurtosRobosPrevenidos");
//        int rowInstant= row;
//        int columnInstant = column;
        LinkedList<HurtosRobosPrevUorg> hurtosRobosPrevUorgs = ServiceLocator.getHechosService().obtenerRobosHurtosPrev(Date.valueOf(date));
        List<HurtosRobosPrevUorg> processedListHurtosRobosPrevUorgs = Util.cleaningAndFormatingList(hurtosRobosPrevUorgs);

        int rowInstant = row;
        for (HurtosRobosPrevUorg hecho : processedListHurtosRobosPrevUorgs) {
            int columnInstant = column;
            sheet.getCell(rowInstant, columnInstant++).setValue(hecho.getUorg());
            sheet.getCell(rowInstant, columnInstant++).setValue(hecho.getCantRobos());
            sheet.getCell(rowInstant, columnInstant++).setValue(hecho.getCantHurtos());
            sheet.getCell(rowInstant, columnInstant++).setValue(hecho.getCantPrevenidos());
            rowInstant++;
        }

        /**
         * Defining cell ranges
         * range 12
         * range 13
         * range 14
         * range 15
         * **/
        //Unidades Organizativas
        ranges.add(sheet.getCells().getSubrangeAbsolute(row, column, rowInstant, column));
        //Cantidad Robos
        ranges.add(sheet.getCells().getSubrangeAbsolute(row, column + 1, rowInstant - 1, column + 1));
        // Cantidad de Hurtos
        ranges.add(sheet.getCells().getSubrangeAbsolute(row, column + 2, rowInstant - 1, column + 2));
        // Cantidad de Prevenidos
        ranges.add(sheet.getCells().getSubrangeAbsolute(row, column + 3, rowInstant - 1, column + 3));


        return ranges;
    }


    private static int writeCellAfectacionGrafico(int row, int column, java.util.List<AfectacionFiscaliaModels> afectacionFiscaliaModelsList, ExcelWorksheet sheet) {

        for (AfectacionFiscaliaModels afectacionFiscaliaModels : afectacionFiscaliaModelsList) {
            sheet.getCell(row, column).setValue(afectacionFiscaliaModels.getTipoAfectacion());
            sheet.getCell(row, column + 1).setValue(afectacionFiscaliaModels.getCantHechos());
            row++;
        }
        return row;
    }

    private static int writeCellAfectacionTPubGrafico(int row, int column, java.util.List<Afectaciones> afectaciones, ExcelWorksheet sheet) {
        for (Afectaciones afectacion : afectaciones) {
            sheet.getCell(row, column).setValue(afectacion.getUnidadOrganizativa());
            sheet.getCell(row, column + 1).setValue(afectacion.getServiciosAfectadosVSEstacionesPublicas());
            row++;
        }
        return row;
    }

    private static int writeCellHechosByMunicipio(int row, int column, java.util.LinkedList<HechosPorMunicipio> hechosPorMunicipios, ExcelWorksheet sheet) {
        int cant = Util.countCant(hechosPorMunicipios);
        sheet.getCell(row, column).setValue("Municipio");
        sheet.getCell(row, column + 1).setValue("Cantidad de Hechos: " + cant);
        row++;
        for (HechosPorMunicipio hechosPorMunicipio : Util.ordenandoHechosPorMunicipio(hechosPorMunicipios)) {
            sheet.getCell(row, column).setValue(hechosPorMunicipio.getNombreMunicipio());
            sheet.getCell(row, column + 1).setValue(hechosPorMunicipio.getCantHechos());
            row++;
        }
        return row;
    }

    private static int writeCellHechosByMunicipioServiciosAfectados(int row, int column, java.util.LinkedList<MunicipioServiciosAfectados> hechosPorMunicipios, ExcelWorksheet sheet) {
        int cant = Util.countCantServiciosAfectados(hechosPorMunicipios);
        sheet.getCell(row, column).setValue("Municipio");
        sheet.getCell(row, column + 1).setValue("Servicios Afectados: " + cant);
        row++;
        for (MunicipioServiciosAfectados hechosPorMunicipio : Util.ordenandoHechosPorMunicipioServiciosAfectados(hechosPorMunicipios)) {
            try {
                sheet.getCell(row, column).setValue(hechosPorMunicipio.getMunicipio());
                sheet.getCell(row, column + 1).setValue(hechosPorMunicipio.getCant_servicios());
            } catch (NullPointerException e) {
            }
            ;
            row++;
        }
        return row;
    }

    private static int writeCellAfectaciones(int row, int column, LinkedList<Afectaciones> afectaciones, ExcelWorksheet sheet, LocalDate date) {
        double percentMedio = 0;
        sheet.getCell(row, column).setValue("Municipio");

        double epInstaladas = 0;
        double serviciosAfectados = 0;
        List<Afectaciones> afectacionesList = ServiceLocator.getHechosService().calculoServiciosAfectadosEstacionesPublicas(Date.valueOf(date));
        for (Afectaciones afectacion : afectacionesList) {
            epInstaladas += afectacion.getCantEstacionesPublicas();
            serviciosAfectados += afectacion.getAfectaciones();
        }


        percentMedio = (100 * serviciosAfectados / epInstaladas);
        double roundedValue = Math.round(percentMedio * 100.0) / 100.0;
        sheet.getCell(row, column + 1).setValue(roundedValue + "% Servicios vs Técnica");
        row++;
        for (Afectaciones afectaciones1 : Util.reordenandoAfectaciones(afectaciones)) {
            sheet.getCell(row, column).setValue(afectaciones1.getUnidadOrganizativa());
            sheet.getCell(row, column + 1).setValue(afectaciones1.getServiciosAfectadosVSEstacionesPublicas());
            row++;
        }
        return row;
    }

    private static int writeCellMaterialesGrafico(int row, int column, java.util.List<MaterialsFiscaliaModels> materialsFiscaliaModelsList, ExcelWorksheet sheet) {

        for (MaterialsFiscaliaModels materialsFiscaliaModels : materialsFiscaliaModelsList) {
            sheet.getCell(row, column).setValue(materialsFiscaliaModels.getMaterialAfectado());
            sheet.getCell(row, column + 1).setValue(materialsFiscaliaModels.getCantHechos());
            sheet.getCell(row, column + 2).setValue(materialsFiscaliaModels.getCantMaterial());
            row++;
        }
        return row;
    }
}
