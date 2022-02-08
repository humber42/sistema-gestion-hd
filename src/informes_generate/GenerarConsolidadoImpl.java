package informes_generate;

import com.gembox.spreadsheet.*;
import models.resumen_esclarecimiento.HechosEsclarecimientoPExtTPub;
import models.resumen_esclarecimiento.HechosEsclarecimientoResumen;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import services.ServiceLocator;
import util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class GenerarConsolidadoImpl implements GenerarConsolidado {

    @Override
    public boolean generarConsolidado(LocalDate date, String path) {
        ExcelFile workbook = new ExcelFile();
        boolean result = false;
        if (this.generarEncabezados(workbook) && this.generarResumen(workbook, date)) {
            try {
                workbook.save(path);
                result = true;
                this.generarDataForPextTpub(path, date);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean generarEncabezados(ExcelFile workbook) {
        this.encabezados(workbook.addWorksheet("PExt"), false);
        this.encabezados(workbook.addWorksheet("TPub"), true);
        return true;
    }

    public void encabezados(ExcelWorksheet sheet, boolean tpub) {
        this.generateEncabezado("A1:A2", "U/O", sheet, true);
        this.generateEncabezado("B1:B2", "Fecha Ocurrido", sheet, true);
        this.generateEncabezado("C1:C2", "Sintesis y lugar del hecho", sheet, true);
        this.generateEncabezado("D1:D2", "Municipio", sheet, true);
        this.generateEncabezado("E1:E2", "Pérdidas", sheet, true);
        this.generateEncabezado("F1:F2", tpub ? "Est. Púb Afect." : "Afec. Servicios", sheet, true);
        this.generateEncabezado("G1:G2", tpub ? "Tipo Vandalismo" : "Material Afectado", sheet, true);
        this.generateEncabezado("H1:H2", "No. Denuncia", sheet, true);
        this.generateEncabezado("I1:J1", "Sin Esclarecer", sheet, true);
        this.generateEncabezado("I2", "Exp. Inv. Sin Autor Conocido", sheet, false);
        this.generateEncabezado("J2", "Sin Denuncia", sheet, false);
        this.generateEncabezado("K1:T1", "Esclarecidos", sheet, true);
        this.generateEncabezado("K2", "Cód Penal Art. 8.3", sheet, false);
        this.generateEncabezado("L2", "Cód Penal Art. 8.2", sheet, false);
        this.generateEncabezado("M2", "Med. Administ", sheet, false);
        this.generateEncabezado("N2", "Menor/ Enajen.", sheet, false);
        this.generateEncabezado("O2", "Exp. en Fase Peparat.", sheet, false);
        this.generateEncabezado("P2", "Pend. Despacho", sheet, false);
        this.generateEncabezado("Q2", "Pend. Juicio", sheet, false);
        this.generateEncabezado("R2", "Priv. Libertad", sheet, false);
        this.generateEncabezado("S2", "Cant. Sanc.", sheet, false);
        this.generateEncabezado("T2", "Sentencia.", sheet, false);

        //Frozen rows
        sheet.setPanes(new WorksheetPanes(PanesState.FROZEN_SPLIT, 0, 2, "A3", PanePosition.BOTTOM_LEFT));

        //Assign column sizes
        sheet.getColumn("A").setWidth(61, LengthUnit.PIXEL);
        sheet.getColumn("B").setWidth(75, LengthUnit.PIXEL);
        sheet.getColumn("C").setWidth(166, LengthUnit.PIXEL);
        sheet.getColumn("D").setWidth(103, LengthUnit.PIXEL);
        sheet.getColumn("E").setWidth(76, LengthUnit.PIXEL);
        sheet.getColumn("F").setWidth(66, LengthUnit.PIXEL);
        sheet.getColumn("G").setWidth(117, LengthUnit.PIXEL);
        sheet.getColumn("H").setWidth(69, LengthUnit.PIXEL);
        sheet.getColumn("I").setWidth(79, LengthUnit.PIXEL);
        sheet.getColumn("J").setWidth(73, LengthUnit.PIXEL);
        sheet.getColumn("K").setWidth(69, LengthUnit.PIXEL);
        sheet.getColumn("L").setWidth(69, LengthUnit.PIXEL);
        sheet.getColumn("M").setWidth(69, LengthUnit.PIXEL);
        sheet.getColumn("N").setWidth(69, LengthUnit.PIXEL);
        sheet.getColumn("O").setWidth(69, LengthUnit.PIXEL);
        sheet.getColumn("P").setWidth(75, LengthUnit.PIXEL);
        sheet.getColumn("Q").setWidth(69, LengthUnit.PIXEL);
        sheet.getColumn("R").setWidth(69, LengthUnit.PIXEL);
        sheet.getColumn("S").setWidth(69, LengthUnit.PIXEL);
        sheet.getColumn("T").setWidth(131, LengthUnit.PIXEL);
    }

    private void generateEncabezado(String range, String label, ExcelWorksheet sheet, boolean merged) {
        CellRange cell = sheet.getCells().getSubrange(range);
        cell.setValue(label);
        if (!merged) {
            cell.forEach(cells -> cells.setStyle(Util.generarStilo()));
        } else {
            cell.setStyle(Util.generarStilo());
        }
        cell.setMerged(merged);
    }


    public boolean generarResumen(ExcelFile workbook, LocalDate date) {
        ExcelWorksheet sheet = workbook.addWorksheet("Resumen");
        this.generateEncabezado("A1:A3", "U/O", sheet, true);
        this.generateEncabezado("B1:D1", "Hechos Conciliados", sheet, true);
        this.generateEncabezado("B2:B3", "Total", sheet, true);
        this.generateEncabezado("C2:C3", "PExt", sheet, true);
        this.generateEncabezado("D2:D3", "TPúb", sheet, true);
        this.generateEncabezado("E1:I1", "Sin esclarecer", sheet, true);
        this.generateEncabezado("E2:E3", "Total", sheet, true);
        this.generateEncabezado("F2:F3", "PExt", sheet, true);
        this.generateEncabezado("G2:G3", "TPúb", sheet, true);
        this.generateEncabezado("H2:H3", "Exp. Inv. SAC", sheet, true);
        this.generateEncabezado("I2:I3", "Sin Denuncia", sheet, true);
        this.generateEncabezado("J1:V1", "Esclarecidos", sheet, true);
        this.generateEncabezado("J2:J3", "Total", sheet, true);
        this.generateEncabezado("K2:K3", "PExt", sheet, true);
        this.generateEncabezado("L2:L3", "TPúb", sheet, true);
        this.generateEncabezado("M2:M3", "CP Art. 8.3", sheet, true);
        this.generateEncabezado("N2:N3", "CP Art. 8.2", sheet, true);
        this.generateEncabezado("O2:O3", "Med. Admin.", sheet, true);
        this.generateEncabezado("P2:P3", "Menor/ Enajen.", sheet, true);
        this.generateEncabezado("Q2:Q3", "Fase Prep.", sheet, true);
        this.generateEncabezado("R2:R3", "Pend. Desp.", sheet, true);
        this.generateEncabezado("S2:S3", "Pend. Juicio", sheet, true);
        this.generateEncabezado("T2:V2", "Priv. de Libertad", sheet, true);
        this.generateEncabezado("T3", "Cant. Casos", sheet, false);
        this.generateEncabezado("U3", "Cant. Sanc.", sheet, false);
        this.generateEncabezado("V3", "Sanciones", sheet, false);
        sheet.getColumn("V").setWidth(300, LengthUnit.PIXEL);

        this.generateEncabezado("A20:V20", "0", sheet, false);
        sheet.getCell("V20").setValue("");
        sheet.getCell("A20").setValue("Total");
        sheet.getCell("A20").getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);

        String[] arrayLettersWithoutV = {"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U"};
        int i = 0;

        while (i < arrayLettersWithoutV.length) {
            String letter = arrayLettersWithoutV[i];
            sheet.getCell(letter + "20").setFormula("=SUM(" + letter + "4:" + letter + "19)");
            i++;
        }

        sheet.getCells().getSubrange("A4:V19").forEach(cell -> {
            cell.setStyle(Util.generarBordes());
            if (!cell.getColumn().getName().equalsIgnoreCase("V"))
                cell.setValue(0);
        });

        CellStyle style1 = Util.generarBordes();
        CellStyle style2 = Util.generarBordes();
        style2.getFillPattern().setSolid(SpreadsheetColor.fromArgb(130, 194, 208));
        style1.getFillPattern().setSolid(SpreadsheetColor.fromArgb(72, 138, 154));
        this.cambiarStyleCell(style1, "B", sheet);
        this.cambiarStyleCell(style1, "E", sheet);
        this.cambiarStyleCell(style1, "J", sheet);
        this.cambiarStyleCell(style2, "C", sheet);
        this.cambiarStyleCell(style2, "D", sheet);
        this.cambiarStyleCell(style2, "F", sheet);
        this.cambiarStyleCell(style2, "G", sheet);
        this.cambiarStyleCell(style2, "K", sheet);
        this.cambiarStyleCell(style2, "L", sheet);
        this.llenarDataResumen(date, sheet);

        return true;
    }

    public void cambiarStyleCell(CellStyle style, String letter, ExcelWorksheet sheet) {

        sheet.getCells().getSubrange(letter + "4:" + letter + "19").forEach(cell ->
                cell.setStyle(style)
        );
    }

    public void generarDataForPextTpub(String path, LocalDate date) {
        List<HechosEsclarecimientoPExtTPub> pext = ServiceLocator.getHechosService()
                .obtenerEsclarecimientoPExtDateRange(Date.valueOf(date.getYear() + "-01-01"), Date.valueOf(date));
        List<HechosEsclarecimientoPExtTPub> tpub = ServiceLocator.getHechosService()
                .obtenerEsclarecimientoTPubDateRange(Date.valueOf(date.getYear() + "-01-01"), Date.valueOf(date));
        try {
            File file = new File(path);
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
                XSSFSheet sheetPext = workbook.getSheetAt(0);
                XSSFSheet sheetTpub = workbook.getSheetAt(1);
                org.apache.poi.ss.usermodel.CellStyle cellStyle = workbook.createCellStyle();
                CreationHelper helper = workbook.getCreationHelper();
                cellStyle.setDataFormat(helper.createDataFormat().getFormat("m/d/yy"));

                this.llenarPextTpub(pext, sheetPext, cellStyle);
                this.llenarPextTpub(tpub, sheetTpub, cellStyle);

                FileOutputStream archivo = new FileOutputStream(file);
                workbook.write(archivo);
                archivo.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void llenarPextTpub(List<HechosEsclarecimientoPExtTPub> list, XSSFSheet sheet, org.apache.poi.ss.usermodel.CellStyle cellStyle) {

        Iterator<HechosEsclarecimientoPExtTPub> iterator = list.iterator();
        int row = 2;
        while (iterator.hasNext()) {
            HechosEsclarecimientoPExtTPub hecho = iterator.next();
            XSSFRow xssfRow = sheet.createRow(row);
            xssfRow.createCell(0).setCellValue(hecho.getUnidad_organizativa());
            var cell = xssfRow.createCell(1);
            cell.setCellValue(hecho.getFecha_ocurrencia());
            cell.setCellStyle(cellStyle);

            xssfRow.createCell(2).setCellValue(hecho.getSintesis());
            xssfRow.createCell(3).setCellValue(hecho.getMunicipio());
            xssfRow.createCell(4).setCellValue(hecho.getAfectacion_usd());
            xssfRow.createCell(5).setCellValue(hecho.getAfectacion_servicio());

            xssfRow.createCell(6).setCellValue(hecho.getMaterial_afectado_o_vandalismo());
            xssfRow.createCell(7).setCellValue(hecho.getNum_denuncia());
            xssfRow.createCell(8).setCellValue(hecho.isExp_sac() ? "X" : "");
            xssfRow.createCell(9).setCellValue(hecho.isSin_denuncia() ? "X" : "");
            xssfRow.createCell(10).setCellValue(hecho.isArticulo83() ? "X" : "");
            xssfRow.createCell(11).setCellValue(hecho.isArticulo82() ? "X" : "");
            xssfRow.createCell(12).setCellValue(hecho.isMedida_administrativa() ? "X" : "");
            xssfRow.createCell(13).setCellValue(hecho.isMenor_edad() ? "X" : "");
            xssfRow.createCell(14).setCellValue(hecho.isExpfp() ? "X" : "");
            xssfRow.createCell(15).setCellValue(hecho.isPendiente_despacho() ? "X" : "");
            xssfRow.createCell(16).setCellValue(hecho.isPendiente_juicio() ? "X" : "");
            xssfRow.createCell(17).setCellValue(hecho.isPriv_lib() ? "X" : "");
            xssfRow.createCell(18).setCellValue(hecho.getCant_sancionados());
            xssfRow.createCell(19).setCellValue(hecho.getSentencia());
            row++;
        }

    }

    private void llenarDataResumen(LocalDate date, ExcelWorksheet sheet) {
        Date date2 = Date.valueOf(date.getYear() + "-01-01");
        List<HechosEsclarecimientoResumen> hechosEsclarecimientoResumen = ServiceLocator
                .getHechosService().obtenerEsclarecimientoResumenDateRange(date2, Date.valueOf(date));

        int row = 4;
        for (HechosEsclarecimientoResumen resumen : hechosEsclarecimientoResumen) {
            sheet.getCell("A" + row).setValue(resumen.getUnidad_organizativa());
            sheet.getCell("B" + row).setValue(resumen.getTotal_conciliados());
            sheet.getCell("C" + row).setValue(resumen.getCant_pext());
            sheet.getCell("D" + row).setValue(resumen.getCant_tpub());
            sheet.getCell("E" + row).setValue(resumen.getTotal_no_esclarecidos());
            sheet.getCell("F" + row).setValue(resumen.getCant_pext_no_esc());
            sheet.getCell("G" + row).setValue(resumen.getCant_tpub_no_esc());
            sheet.getCell("H" + row).setValue(resumen.getCant_exp_sac());
            sheet.getCell("I" + row).setValue(resumen.getCant_sin_denuncia());
            sheet.getCell("J" + row).setValue(resumen.getTotal_esclarecidos());
            sheet.getCell("K" + row).setValue(resumen.getCant_pext_esc());
            sheet.getCell("L" + row).setValue(resumen.getCant_tpub_esc());
            sheet.getCell("M" + row).setValue(resumen.getCant_art_83());
            sheet.getCell("N" + row).setValue(resumen.getCant_art_82());
            sheet.getCell("O" + row).setValue(resumen.getCant_med_admin());
            sheet.getCell("P" + row).setValue(resumen.getCant_menor());
            sheet.getCell("Q" + row).setValue(resumen.getCant_fase_prep());
            sheet.getCell("R" + row).setValue(resumen.getCant_pend_desp());
            sheet.getCell("S" + row).setValue(resumen.getCant_pend_juicio());
            sheet.getCell("T" + row).setValue(resumen.getCant_casos());
            sheet.getCell("U" + row).setValue(resumen.getCant_sanciones());
            sheet.getCell("V" + row).setValue(resumen.getSentencias());

            System.out.println(resumen);

            row++;
        }


    }
}
