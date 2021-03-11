package informes_generate;

import com.gembox.spreadsheet.*;
import services.ServiceLocator;
import util.Util;
import util.UtilToGenerateTables;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import static util.Util.generarBordes;
import static util.Util.generarStilo;

public class GenerarPrevencionImpl implements GenerarPrevencion {

    @Override
    public boolean generarInformePrevencion(Date fechaInicio, Date fechaFin) {
        String DIRECCION_ARCHIVO = "src/informesGenerados/InformePrevencion.xlsx";
        boolean result = false;
        ExcelFile workbook = new ExcelFile();
        if (this.generarTablaResumen(null, null, workbook)
                && this.generarPerdidas(null, null, workbook)
                && this.writeGraficos(workbook.addWorksheet("Graficos"), Date.valueOf("2021-03-03"))) {
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

        //TODO: EXTRAER INFORMACION PARA IMPRIMIR
        return true;
    }

    private boolean generarTablaResumen(Date fechaInicio, Date fechaFin, ExcelFile workbook) {
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

        sheet.getCells().getSubrange("A2:I2").setValue(//fechaInicio.toLocalDate().getYear()
                2021);
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
                //fechaInicio.toLocalDate().minusYears(1).getYear()
                2020);
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


        //TODO: Extraer informacion para imprimir en el EXCEL
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
        return true;
    }
}
