package informes_generate;

import com.gembox.spreadsheet.*;
import com.gembox.spreadsheet.charts.ChartLegendPosition;
import com.gembox.spreadsheet.charts.ChartType;
import com.gembox.spreadsheet.charts.DataLabelPosition;
import com.gembox.spreadsheet.charts.LineChart;
import models.AfectacionesServiciosAfectados;
import models.CantidadDelitoRangoFecha;
import services.ServiceLocator;
import util.Util;
import util.UtilToGenerateTables;

import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static util.Util.generarBordes;
import static util.Util.generarStilo;

public class GenerarPrevencionImpl implements GenerarPrevencion {

    @Override
    public boolean generarInformePrevencion(Date fechaInicio, Date fechaFin, String path) {
        String DIRECCION_ARCHIVO = path+"/InformePrevencion.xlsx";
        boolean result = false;
        ExcelFile workbook = new ExcelFile();
        if (this.generarTablaResumen(fechaInicio, Date.valueOf(fechaFin.toLocalDate().minusDays(1)), workbook)
                && this.generarPerdidas(fechaInicio, fechaFin, workbook)
                && this.writeGraficos(workbook.addWorksheet("Graficos"), fechaFin)) {
            try {
                workbook.save(DIRECCION_ARCHIVO);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean generarPerdidas(Date fechaInicio, Date fechaFin, ExcelFile workbook) {


        AfectacionesServiciosAfectados tpub = ServiceLocator.getHechosService().obtenerAfectacionServicio(
                fechaInicio, fechaFin, 2);
        AfectacionesServiciosAfectados pext = ServiceLocator.getHechosService().obtenerAfectacionServicio(
                fechaInicio, fechaFin, 1);
        ExcelWorksheet sheet = workbook.addWorksheet("Pérdidas");
        CellRange rangeA1A2 = sheet.getCells().getSubrange("A1:A2");
        rangeA1A2.setMerged(true);
        rangeA1A2.setStyle(generarStilo());
        rangeA1A2.setValue("Tipo de Hecho");


        CellRange rangeB1C1 = sheet.getCells().getSubrange("B1:C1");
        rangeB1C1.setMerged(true);
        rangeB1C1.setStyle(generarStilo());
        rangeB1C1.setValue("Pérdidas Económicas");

        ExcelCell cellB2 = sheet.getCell("B2");
        cellB2.setStyle(generarStilo());
        cellB2.setValue("USD");
        ExcelCell cellC2 = sheet.getCell("C2");
        cellC2.setStyle(generarStilo());
        cellC2.setValue("MN");

        CellRange rangeD1D2 = sheet.getCells().getSubrange("D1:D2");
        rangeD1D2.setValue("Servicios Afectados");
        rangeD1D2.setMerged(true);
        rangeD1D2.setStyle(generarStilo());

        sheet.getCell("A3").setValue("Planta Exterior");
        sheet.getCell("A4").setValue("Telefonía Pública");

        sheet.getCell("A5").setValue("Total:");
        sheet.getCells().getSubrange("A3:D4").forEach(p -> p.setStyle(generarBordes()));
        sheet.getCells().getSubrange("A5:D5").forEach(p -> p.setStyle(generarStilo()));

        sheet.getCell("A5").getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);

        sheet.getCell("B5").setFormula("=SUM(B3:B4)");
        sheet.getCell("C5").setFormula("=SUM(C3:C4)");
        sheet.getCell("D5").setFormula("=SUM(D3:D4)");

        sheet.getColumn("A").setWidth(18, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("B").setWidth(13, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("C").setWidth(13, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("D").setWidth(13, LengthUnit.ZERO_CHARACTER_WIDTH);

        DecimalFormat df = new DecimalFormat("#.00");
        sheet.getCell("B3").setValue(Double.valueOf(df.format(pext.getUsd())));
        sheet.getCell("C3").setValue(Double.valueOf(df.format(pext.getMn())));
        sheet.getCell("D3").setValue(pext.getServ_afectados());

        sheet.getCell("B4").setValue(Double.valueOf(df.format(tpub.getUsd())));
        sheet.getCell("C4").setValue(Double.valueOf(df.format(tpub.getMn())));
        sheet.getCell("D4").setValue(tpub.getServ_afectados());

        return true;
    }

    private boolean generarTablaResumen(Date fechaInicio, Date fechaFin, ExcelFile workbook) {

        LinkedList<CantidadDelitoRangoFecha> rangoFechasActual = ServiceLocator.getHechosService()
                .obtenerCantidadDelitoRangoFecha(fechaInicio, fechaFin);
        LinkedList<CantidadDelitoRangoFecha> rangoFechasAnterior = ServiceLocator.getHechosService()
                .obtenerCantidadDelitoRangoFecha(Date.valueOf(fechaInicio.toLocalDate().minusYears(1)), Date.valueOf(fechaFin.toLocalDate().minusYears(1)));

        ExcelWorksheet sheet = workbook.addWorksheet("Resumen");
        sheet.getColumn("A").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getCell("A1").setValue("Mes");
        sheet.getCell("B1").setValue("PExt");
        sheet.getCell("C1").setValue("TPúb");
        sheet.getCell("D1").setValue("Robo");
        sheet.getCell("E1").setValue("Hurto");
        sheet.getCell("F1").setValue("Fraude");
        sheet.getCell("G1").setValue("Acción C/R");
        sheet.getCell("H1").setValue("Otros Delitos");
        sheet.getCell("I1").setValue("Total");

        sheet.getCells().getSubrange("A1:I1").forEach(cell -> cell.setStyle(generarStilo()));

        sheet.getCells().getSubrange("A2:I2").setValue(fechaInicio.toLocalDate().getYear()
        );
        sheet.getCells().getSubrange("A2:I2").setStyle(generarBordes());
        sheet.getCells().getSubrange("A2:I2").setMerged(true);
        sheet.getCells().getSubrange("A2:I2").getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(8, 199, 226));

        sheet.getCells().getSubrange("A3:I14").forEach(p -> p.setStyle(generarBordes()));

        sheet.getCells().getSubrange("I3:I14").forEach(p -> p.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(100, 226, 188)));
        for (int i = 3; i < 15; i++) {
            sheet.getCell("A" + i).setValue(Util.meses2[i - 3]);
            sheet.getCell("I" + i).setFormula("=SUM(B" + i + ":" + "H" + i + ")");
            sheet.getCell("I" + i).getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(68, 125, 226));
        }

        sheet.getCell("A15").setValue("Total:");
        sheet.getCells().getSubrange("A15:I15").forEach(p -> p.setStyle(generarStilo()));
        sheet.getCell("A15").getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);

        sheet.getCell("B15").setFormula("=SUM(B3:B14)");
        sheet.getCell("C15").setFormula("=SUM(C3:C14)");
        sheet.getCell("D15").setFormula("=SUM(D3:D14)");
        sheet.getCell("E15").setFormula("=SUM(E3:E14)");
        sheet.getCell("F15").setFormula("=SUM(F3:F14)");
        sheet.getCell("G15").setFormula("=SUM(G3:G14)");
        sheet.getCell("H15").setFormula("=SUM(H3:H14)");
        sheet.getCell("I15").setFormula("=SUM(I3:I14)");

        sheet.getCells().getSubrange("A16:I16").setValue(
                fechaInicio.toLocalDate().minusYears(1).getYear()
        );
        sheet.getCells().getSubrange("A16:I16").setStyle(generarBordes());
        sheet.getCells().getSubrange("A16:I16").setMerged(true);
        sheet.getCells().getSubrange("A16:I16").getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(8, 199, 226));

        sheet.getCells().getSubrange("A17:I28").forEach(p -> p.setStyle(generarBordes()));

        for (int i = 17; i < 29; i++) {
            sheet.getCell("A" + i).setValue(Util.meses2[i - 17]);
            sheet.getCell("I" + i).setFormula("=SUM(B" + i + ":" + "H" + i + ")");
            sheet.getCell("I" + i).getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(68, 125, 226));
        }

        sheet.getCell("A29").setValue("Total:");
        sheet.getCells().getSubrange("A29:I29").forEach(p -> p.setStyle(generarStilo()));
        sheet.getCell("A29").getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);

        sheet.getCell("B29").setFormula("=SUM(B17:B28)");
        sheet.getCell("C29").setFormula("=SUM(C17:C28)");
        sheet.getCell("D29").setFormula("=SUM(D17:D28)");
        sheet.getCell("E29").setFormula("=SUM(E17:E28)");
        sheet.getCell("F29").setFormula("=SUM(F17:F28)");
        sheet.getCell("G29").setFormula("=SUM(G17:G28)");
        sheet.getCell("H29").setFormula("=SUM(H17:H28)");
        sheet.getCell("I29").setFormula("=SUM(I17:I28)");

        sheet.getCells().getSubrange("B3:H14").forEach(p -> p.setValue(0));
        sheet.getCells().getSubrange("B17:H28").forEach(p -> p.setValue(0));


        List<String> posicionesActual = this.generarLetrasBH(3, 15);
        List<String> posicionesAnteriores = this.generarLetrasBH(17, 29);

        //actual
        this.writeDataOnResumen(rangoFechasActual, posicionesActual, sheet);
        //anterior
        this.writeDataOnResumen(rangoFechasAnterior, posicionesAnteriores, sheet);

        return true;
    }


    /**
     * Metodo para escribir la tabla debajo en la hoja de historico
     *
     * @param worksheet hoja
     * @param date      fecha
     */
    private boolean writeGraficos(ExcelWorksheet worksheet, Date date) {
        ExcelCell cell = worksheet.getCell("A1");
        cell.setStyle(Util.generarStilo());
        cell.setValue("Año");

        cell = worksheet.getCell("B1");
        cell.setStyle(Util.generarStilo());
        cell.setValue("Mes");

        cell = worksheet.getCell("C1");
        cell.setStyle(Util.generarStilo());
        cell.setValue("PExt");

        cell = worksheet.getCell("D1");
        cell.setStyle(Util.generarStilo());
        cell.setValue("TPúb");

        CellRange rangeB42B53 = worksheet.getCells().getSubrange("A2:A13");
        rangeB42B53.setMerged(true);
        rangeB42B53.setStyle(Util.generarBordes());
        rangeB42B53.setValue(date.toLocalDate().minusYears(1).getYear());

        CellRange rangeB54B65 = worksheet.getCells().getSubrange("A14:A25");
        rangeB54B65.setMerged(true);
        rangeB54B65.setStyle(Util.generarBordes());
        rangeB54B65.setValue(date.toLocalDate().getYear());

        GeneradoresTablas.writeMonthOnHistorical(worksheet.getCells().getSubrange("B2:B13"), Util.generarStilo());
        GeneradoresTablas.writeMonthOnHistorical(worksheet.getCells().getSubrange("B14:B25"), Util.generarStilo());

        worksheet.getRow("1").setHeight(5, LengthUnit.ZERO_CHARACTER_WIDTH);
        worksheet.getCells().getSubrange("C2:D25").forEach(excelCell -> {
            excelCell.setValue(0);
            excelCell.setStyle(Util.generarBordes());
            if (excelCell.getColumn().getName().equalsIgnoreCase("d")) {
                excelCell.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromArgb(180, 214, 214));
            }
        });

        ArrayList<String> letrasAnnoAnterior = new ArrayList<>();
        for (int i = 2; i < 14; i++) {
            letrasAnnoAnterior.add("C" + i);
            letrasAnnoAnterior.add("D" + i);
        }

        ArrayList<String> letrasAnnoActual = new ArrayList<>();
        for (int i = 14; i < 26; i++) {
            letrasAnnoActual.add("C" + i);
            letrasAnnoActual.add("D" + i);
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


        //TODO: Agregar graficos de puntos
        this.generarGraficoLineas(worksheet.getCells().getSubrange("C2:C25"), worksheet, "F2", "P17"
                , "Planta Exterior");
        this.generarGraficoLineas(worksheet.getCells().getSubrange("D2:D25"), worksheet, "F19", "P34",
                "Telefonía Pública");
        return true;
    }

    /**
     * Escribe los datos dentro de la tabla de resumen
     *
     * @param delitos    listado de los meses con sus delitos
     * @param posiciones lista de posiciones
     * @param sheet      hoja
     * @return valor true
     */
    private boolean writeDataOnResumen(LinkedList<CantidadDelitoRangoFecha> delitos, List<String> posiciones, ExcelWorksheet sheet) {

        delitos.forEach(delito -> {
            if (delito.getMes().equalsIgnoreCase("enero")) {
                sheet.getCell(posiciones.get(0)).setValue(delito.getPext());
                sheet.getCell(posiciones.get(1)).setValue(delito.getTpub());
                sheet.getCell(posiciones.get(2)).setValue(delito.getRobo());
                sheet.getCell(posiciones.get(3)).setValue(delito.getHurto());
                sheet.getCell(posiciones.get(4)).setValue(delito.getFraude());
                sheet.getCell(posiciones.get(5)).setValue(delito.getAccioncr());
                sheet.getCell(posiciones.get(6)).setValue(delito.getOtros());
            } else if (delito.getMes().equalsIgnoreCase("febrero")) {
                sheet.getCell(posiciones.get(7)).setValue(delito.getPext());
                sheet.getCell(posiciones.get(8)).setValue(delito.getTpub());
                sheet.getCell(posiciones.get(9)).setValue(delito.getRobo());
                sheet.getCell(posiciones.get(10)).setValue(delito.getHurto());
                sheet.getCell(posiciones.get(11)).setValue(delito.getFraude());
                sheet.getCell(posiciones.get(12)).setValue(delito.getAccioncr());
                sheet.getCell(posiciones.get(13)).setValue(delito.getOtros());
            } else if (delito.getMes().equalsIgnoreCase("marzo")) {
                sheet.getCell(posiciones.get(14)).setValue(delito.getPext());
                sheet.getCell(posiciones.get(15)).setValue(delito.getTpub());
                sheet.getCell(posiciones.get(16)).setValue(delito.getRobo());
                sheet.getCell(posiciones.get(17)).setValue(delito.getHurto());
                sheet.getCell(posiciones.get(18)).setValue(delito.getFraude());
                sheet.getCell(posiciones.get(19)).setValue(delito.getAccioncr());
                sheet.getCell(posiciones.get(20)).setValue(delito.getOtros());
            } else if (delito.getMes().equalsIgnoreCase("abril")) {
                sheet.getCell(posiciones.get(21)).setValue(delito.getPext());
                sheet.getCell(posiciones.get(22)).setValue(delito.getTpub());
                sheet.getCell(posiciones.get(23)).setValue(delito.getRobo());
                sheet.getCell(posiciones.get(24)).setValue(delito.getHurto());
                sheet.getCell(posiciones.get(25)).setValue(delito.getFraude());
                sheet.getCell(posiciones.get(26)).setValue(delito.getAccioncr());
                sheet.getCell(posiciones.get(27)).setValue(delito.getOtros());
            } else if (delito.getMes().equalsIgnoreCase("mayo")) {
                sheet.getCell(posiciones.get(28)).setValue(delito.getPext());
                sheet.getCell(posiciones.get(29)).setValue(delito.getTpub());
                sheet.getCell(posiciones.get(30)).setValue(delito.getRobo());
                sheet.getCell(posiciones.get(31)).setValue(delito.getHurto());
                sheet.getCell(posiciones.get(32)).setValue(delito.getFraude());
                sheet.getCell(posiciones.get(33)).setValue(delito.getAccioncr());
                sheet.getCell(posiciones.get(34)).setValue(delito.getOtros());
            } else if (delito.getMes().equalsIgnoreCase("junio")) {
                sheet.getCell(posiciones.get(35)).setValue(delito.getPext());
                sheet.getCell(posiciones.get(36)).setValue(delito.getTpub());
                sheet.getCell(posiciones.get(37)).setValue(delito.getRobo());
                sheet.getCell(posiciones.get(38)).setValue(delito.getHurto());
                sheet.getCell(posiciones.get(39)).setValue(delito.getFraude());
                sheet.getCell(posiciones.get(40)).setValue(delito.getAccioncr());
                sheet.getCell(posiciones.get(41)).setValue(delito.getOtros());
            } else if (delito.getMes().equalsIgnoreCase("julio")) {
                sheet.getCell(posiciones.get(42)).setValue(delito.getPext());
                sheet.getCell(posiciones.get(43)).setValue(delito.getTpub());
                sheet.getCell(posiciones.get(44)).setValue(delito.getRobo());
                sheet.getCell(posiciones.get(45)).setValue(delito.getHurto());
                sheet.getCell(posiciones.get(46)).setValue(delito.getFraude());
                sheet.getCell(posiciones.get(47)).setValue(delito.getAccioncr());
                sheet.getCell(posiciones.get(48)).setValue(delito.getOtros());
            } else if (delito.getMes().equalsIgnoreCase("agosto")) {
                sheet.getCell(posiciones.get(49)).setValue(delito.getPext());
                sheet.getCell(posiciones.get(50)).setValue(delito.getTpub());
                sheet.getCell(posiciones.get(51)).setValue(delito.getRobo());
                sheet.getCell(posiciones.get(52)).setValue(delito.getHurto());
                sheet.getCell(posiciones.get(53)).setValue(delito.getFraude());
                sheet.getCell(posiciones.get(54)).setValue(delito.getAccioncr());
                sheet.getCell(posiciones.get(55)).setValue(delito.getOtros());
            } else if (delito.getMes().equalsIgnoreCase("septiembre")) {
                sheet.getCell(posiciones.get(56)).setValue(delito.getPext());
                sheet.getCell(posiciones.get(57)).setValue(delito.getTpub());
                sheet.getCell(posiciones.get(58)).setValue(delito.getRobo());
                sheet.getCell(posiciones.get(59)).setValue(delito.getHurto());
                sheet.getCell(posiciones.get(60)).setValue(delito.getFraude());
                sheet.getCell(posiciones.get(61)).setValue(delito.getAccioncr());
                sheet.getCell(posiciones.get(62)).setValue(delito.getOtros());
            } else if (delito.getMes().equalsIgnoreCase("octubre")) {
                sheet.getCell(posiciones.get(63)).setValue(delito.getPext());
                sheet.getCell(posiciones.get(64)).setValue(delito.getTpub());
                sheet.getCell(posiciones.get(65)).setValue(delito.getRobo());
                sheet.getCell(posiciones.get(66)).setValue(delito.getHurto());
                sheet.getCell(posiciones.get(67)).setValue(delito.getFraude());
                sheet.getCell(posiciones.get(68)).setValue(delito.getAccioncr());
                sheet.getCell(posiciones.get(69)).setValue(delito.getOtros());
            } else if (delito.getMes().equalsIgnoreCase("noviembre")) {
                sheet.getCell(posiciones.get(70)).setValue(delito.getPext());
                sheet.getCell(posiciones.get(71)).setValue(delito.getTpub());
                sheet.getCell(posiciones.get(72)).setValue(delito.getRobo());
                sheet.getCell(posiciones.get(73)).setValue(delito.getHurto());
                sheet.getCell(posiciones.get(74)).setValue(delito.getFraude());
                sheet.getCell(posiciones.get(75)).setValue(delito.getAccioncr());
                sheet.getCell(posiciones.get(76)).setValue(delito.getOtros());
            } else if (delito.getMes().equalsIgnoreCase("diciembre")) {
                sheet.getCell(posiciones.get(77)).setValue(delito.getPext());
                sheet.getCell(posiciones.get(78)).setValue(delito.getTpub());
                sheet.getCell(posiciones.get(79)).setValue(delito.getRobo());
                sheet.getCell(posiciones.get(80)).setValue(delito.getHurto());
                sheet.getCell(posiciones.get(81)).setValue(delito.getFraude());
                sheet.getCell(posiciones.get(82)).setValue(delito.getAccioncr());
                sheet.getCell(posiciones.get(83)).setValue(delito.getOtros());
            }
        });


        return true;
    }


    private LinkedList<String> generarLetrasBH(int inicio, int fin) {
        LinkedList<String> posicionesAnteriores = new LinkedList<>();

        for (int i = inicio; i < fin; i++) {
            posicionesAnteriores.add("B" + i);
            posicionesAnteriores.add("C" + i);
            posicionesAnteriores.add("D" + i);
            posicionesAnteriores.add("E" + i);
            posicionesAnteriores.add("F" + i);
            posicionesAnteriores.add("G" + i);
            posicionesAnteriores.add("H" + i);
        }
        return posicionesAnteriores;
    }

    private boolean generarGraficoLineas(CellRange range, ExcelWorksheet worksheet,
                                         String positionInit, String positionFinal, String title) {
        LineChart chart = (LineChart) worksheet.getCharts().add(ChartType.LINE, positionInit, positionFinal);
        //'Tablas Graficos'!$A$25:$A$43
        //'Tablas Graficos'!$A$25:$A$43'
        chart.getAxes().getVertical().setVisible(false);
        String reference = Util.formatedLetter(range);
        chart.getSeries().add("Cant-Hechos", reference);
        chart.getDataLabels().setLabelContainsValue(true);
        chart.getDataLabels().setLabelPosition(DataLabelPosition.TOP);
        chart.setCategoryLabelsReference("Graficos!$A$2:$B$25");
        chart.getTitle().setText(title);
//        if (fiscalia) {
//            chart.setCategoryLabelsReference("Resumen!$A$80:$B$103");
//            chart.getTitle().setVisible(false);
//        } else {
//            chart.setCategoryLabelsReference("Historico!$B$42:$C$65");
//            chart.getTitle().setText(title);
//
//        }
        chart.getLegend().setPosition(ChartLegendPosition.BOTTOM);

        return true;

    }
}
