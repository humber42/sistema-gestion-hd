package informes_generate;

import models.HechosRegistrados;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportarExcel {

    private final String EXCEL_NAME = "/Hechos.xlsx";

    public String generarExcel(String url, List<HechosRegistrados> hechos) throws IOException {
        String address = url + EXCEL_NAME;
        File archivoXLSX = new File(address);
        if (archivoXLSX.exists()) archivoXLSX.delete();
        archivoXLSX.createNewFile();
        this.buildExcel(address, hechos);
        return address;
    }

    private boolean buildExcel(String file, List<HechosRegistrados> hechos) throws IOException {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet("Hechos");

            XSSFRow xssfRow = sheet.createRow(0);
            xssfRow.createCell(0).setCellValue("Cod CDNT");
            xssfRow.createCell(1).setCellValue("U/O");
            xssfRow.createCell(2).setCellValue("Tipo hecho");
            xssfRow.createCell(3).setCellValue("Fecha");
            xssfRow.createCell(4).setCellValue("Titulo");
            xssfRow.createCell(5).setCellValue("Municipio");
            int hechoIndex = 0;

            for (int row = 1; hechoIndex < hechos.size(); row++) {
                XSSFRow xssfRowVariable = sheet.createRow(row);
                xssfRowVariable.createCell(0).setCellValue(hechos.get(hechoIndex).getCodCDNT() == null ? "" : hechos.get(hechoIndex).getCodCDNT().toUpperCase());
                xssfRowVariable.createCell(1).setCellValue(hechos.get(hechoIndex).getUo());
                xssfRowVariable.createCell(2).setCellValue(hechos.get(hechoIndex).getTipoHecho());
                var cell = xssfRowVariable.createCell(3);
                cell.setCellValue(hechos.get(hechoIndex).getFechaOcurre());
                CellStyle cellStyle = workbook.createCellStyle();
                CreationHelper helper = workbook.getCreationHelper();
                cellStyle.setDataFormat(helper.createDataFormat().getFormat("m/d/yy"));
                cell.setCellStyle(cellStyle);
                xssfRowVariable.createCell(4).setCellValue(hechos.get(hechoIndex).getTitulo());
                xssfRowVariable.createCell(5).setCellValue(hechos.get(hechoIndex).getMunicipio());
                hechoIndex++;
            }
            sheet.setColumnWidth(0, 16 * 256);
            sheet.setColumnWidth(2, 18 * 256);
            sheet.setColumnWidth(3, 12 * 256);
            sheet.setColumnWidth(4, 85 * 256);
            sheet.setColumnWidth(5, 20 * 256);
//           CellStyle dateStyle = workbook.createCellStyle();
//           var dataFormat = workbook.createDataFormat();
//           dateStyle.setDataFormat(dataFormat.getFormat("@"));
//           sheet.setDefaultColumnStyle(3,dateStyle);

            FileOutputStream archivo = new FileOutputStream(file);
            workbook.write(archivo);
            archivo.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
