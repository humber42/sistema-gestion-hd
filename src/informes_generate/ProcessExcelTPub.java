package informes_generate;

import models.TpubCentroAgenteFromExcel;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProcessExcelTPub {

    public static List<TpubCentroAgenteFromExcel> getDataFromExcel(File excelFile,boolean excelsHigh2003){
        List<TpubCentroAgenteFromExcel> data = new LinkedList<>();
        try{
            FileInputStream fileInputStream = new FileInputStream(excelFile);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            int cantRows = 0;
            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                if (row.getRowNum() > 2 && !row.getCell(1).getStringCellValue().contains("TOTAL") && row.getRowNum() < 186) {
                    String municipio = row.getCell(1).getStringCellValue();
                    int tpub = Double.valueOf((row.getCell(13)).getNumericCellValue()).intValue();
                    int centroAgente = Double.valueOf(row.getCell(12).getNumericCellValue()).intValue();

                    System.out.println(municipio + " " + tpub + " " + centroAgente);
                    System.out.println("-----------");
                    data.add(new TpubCentroAgenteFromExcel(
                            municipio,
                            tpub,
                            centroAgente
                    ));
                }
                cantRows++;
            }
            System.out.println(cantRows);
        }catch (Exception e){
            e.printStackTrace();
        }

        return data;
    }
    public static List<TpubCentroAgenteFromExcel> getDataFromExcel(File excelFile){
        List<TpubCentroAgenteFromExcel> data = new LinkedList<>();
        try{
            FileInputStream fileInputStream = new FileInputStream(excelFile);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            int cantSanLuis = 0;
            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                if (row.getRowNum() > 2 && !row.getCell(1).getStringCellValue().contains("TOTAL") && row.getRowNum() < 186) {
                    String municipio = row.getCell(1).getStringCellValue();
                    if (municipio.equalsIgnoreCase("San Luis") && cantSanLuis < 1) {
                        cantSanLuis++;
                        municipio = "San Luis (PR)";
                    } else if (municipio.equalsIgnoreCase("San Luis") && cantSanLuis > 0) {
                        cantSanLuis = 0;
                        municipio = "San Luis (SC)";
                    } else if (municipio.equalsIgnoreCase("San Juan y Mtnez")) {
                        municipio = "San Juan y Mart??nez";
                    } else if (municipio.equalsIgnoreCase("Alquizar")) {
                        municipio = "Alqu??zar";
                    } else if (municipio.equalsIgnoreCase("Guira de Melena")) {
                        municipio = "G??ira de Melena";
                    } else if (municipio.equalsIgnoreCase("San Cristobal")) {
                        municipio = "San Crist??bal";
                    } else if (municipio.equalsIgnoreCase("Guines")) {
                        municipio = "G??ines";
                    } else if (municipio.equalsIgnoreCase("10 de Octubre")) {
                        municipio = "Diez de Octubre";
                    } else if (municipio.equalsIgnoreCase("Jaguey Grande")) {
                        municipio = "Jag??ey Grande";
                    } else if (municipio.equalsIgnoreCase("Quemado de Guines")) {
                        municipio = "Quemado de G??ines";
                    } else if (municipio.equalsIgnoreCase("Cabaiguan")) {
                        municipio = "Cabaigu??n";
                    } else if (municipio.equalsIgnoreCase("Baragua ")) {
                        municipio = "Baragu??";
                    } else if (municipio.equalsIgnoreCase("1ro de Enero")) {
                        municipio = "Primero de Enero";
                    } else if (municipio.equalsIgnoreCase("Ciego de Avila")) {
                        municipio = "Ciego de ??vila";
                    } else if (municipio.equalsIgnoreCase("Camaguey")) {
                        municipio = "Camag??ey";
                    } else if (municipio.equalsIgnoreCase("C??spedes")) {
                        municipio = "Carlos Manuel de C??spedes";
                    } else if (municipio.equalsIgnoreCase("Guaimaro")) {
                        municipio = "Gu??imaro";
                    } else if (municipio.equalsIgnoreCase("G??bara")) {
                        municipio = "Gibara";
                    } else if (municipio.equalsIgnoreCase("Pilon")) {
                        municipio = "Pil??n";
                    } else if (municipio.equalsIgnoreCase("Rio Cauto")) {
                        municipio = "R??o Cauto";
                    } else if (municipio.equalsIgnoreCase("II Frente")) {
                        municipio = "Segundo Frente";
                    } else if (municipio.equalsIgnoreCase("Songo La Maya ")) {
                        municipio = "Songo - La Maya";
                    } else if (municipio.equalsIgnoreCase("III Frente")) {
                        municipio = "Tercer Frente";
                    } else if (municipio.equalsIgnoreCase("Imias")) {
                        municipio = "Im??as";
                    } else if (municipio.equalsIgnoreCase("San Antonio de las Ba??os")) {
                        municipio = "San Antonio de los Ba??os";
                    } else if (municipio.equalsIgnoreCase("Bahia Honda")) {
                        municipio = "Bah??a Honda";
                    } else if (municipio.equalsIgnoreCase("Sagua La Grande")) {
                        municipio = "Sagua la Grande";
                    } else if (municipio.equalsIgnoreCase("Mor??n ")) {
                        municipio = "Mor??n";
                    } else if (municipio.equalsIgnoreCase("Antillas")) {
                        municipio = "Antilla";
                    }
                    int tpub = Double.valueOf((row.getCell(13)).getNumericCellValue()).intValue();
                    int centroAgente = Double.valueOf(row.getCell(12).getNumericCellValue()).intValue();

                    System.out.println(municipio + " " + tpub + " " + centroAgente);
                    System.out.println("-----------");
                    data.add( new TpubCentroAgenteFromExcel(
                           municipio,
                            tpub,
                            centroAgente
                    ));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return data;
    }
}
