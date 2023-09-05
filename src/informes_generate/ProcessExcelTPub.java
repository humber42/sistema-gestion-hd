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
            int cantSanLuis = 0;
            int cantRows = 0;
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
                        municipio = "San Juan y Martínez";
                    } else if (municipio.equalsIgnoreCase("Alquizar")) {
                        municipio = "Alquízar";
                    } else if (municipio.equalsIgnoreCase("Guira de Melena")) {
                        municipio = "Güira de Melena";
                    } else if (municipio.equalsIgnoreCase("San Cristobal")) {
                        municipio = "San Cristóbal";
                    } else if (municipio.equalsIgnoreCase("Guines")) {
                        municipio = "Güines";
                    } else if (municipio.equalsIgnoreCase("10 de Octubre")) {
                        municipio = "Díez de Octubre";
                    } else if (municipio.equalsIgnoreCase("Jaguey Grande")) {
                        municipio = "Jagüey Grande";
                    } else if (municipio.equalsIgnoreCase("Quemado de Guines")) {
                        municipio = "Quemado de Güines";
                    } else if (municipio.equalsIgnoreCase("Cabaiguan")) {
                        municipio = "Cabaiguán";
                    } else if (municipio.equalsIgnoreCase("Baragua ")) {
                        municipio = "Baraguá";
                    } else if (municipio.equalsIgnoreCase("1ro de Enero")) {
                        municipio = "Primero de Enero";
                    } else if (municipio.equalsIgnoreCase("Ciego de Avila")) {
                        municipio = "Ciego de Ávila";
                    } else if (municipio.equalsIgnoreCase("Camaguey")) {
                        municipio = "Camagüey";
                    } else if (municipio.equalsIgnoreCase("Céspedes")) {
                        municipio = "Carlos Manuel de Céspedes";
                    } else if (municipio.equalsIgnoreCase("Guaimaro")) {
                        municipio = "Guáimaro";
                    } else if (municipio.equalsIgnoreCase("Gíbara")) {
                        municipio = "Gibara";
                    } else if (municipio.equalsIgnoreCase("Pilon")) {
                        municipio = "Pilón";
                    } else if (municipio.equalsIgnoreCase("Rio Cauto")) {
                        municipio = "Río Cauto";
                    } else if (municipio.equalsIgnoreCase("II Frente")) {
                        municipio = "Segundo Frente";
                    } else if (municipio.equalsIgnoreCase("Songo La Maya ")) {
                        municipio = "Songo - La Maya";
                    } else if (municipio.equalsIgnoreCase("III Frente")) {
                        municipio = "Tercer Frente";
                    } else if (municipio.equalsIgnoreCase("Imias")) {
                        municipio = "Imías";
                    } else if (municipio.equalsIgnoreCase("San Antonio de las Baños")) {
                        municipio = "San Antonio de los Baños";
                    } else if (municipio.equalsIgnoreCase("Bahia Honda")) {
                        municipio = "Bahía Honda";
                    } else if (municipio.equalsIgnoreCase("Sagua La Grande")) {
                        municipio = "Sagua la Grande";
                    } else if (municipio.equalsIgnoreCase("Morón ")) {
                        municipio = "Morón";
                    } else if (municipio.equalsIgnoreCase("Antillas")) {
                        municipio = "Antilla";
                    } else if (municipio.equalsIgnoreCase("Plaza de la Revolución")) {
                        municipio = "Plaza";
                    } else if (municipio.equalsIgnoreCase("San Nicolás de Bari")) {
                        municipio = "San Nicolás";
                    }
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
                        municipio = "San Juan y Martínez";
                    } else if (municipio.equalsIgnoreCase("Alquizar")) {
                        municipio = "Alquízar";
                    } else if (municipio.equalsIgnoreCase("Guira de Melena")) {
                        municipio = "Güira de Melena";
                    } else if (municipio.equalsIgnoreCase("San Cristobal")) {
                        municipio = "San Cristóbal";
                    } else if (municipio.equalsIgnoreCase("Guines")) {
                        municipio = "Güines";
                    } else if (municipio.equalsIgnoreCase("10 de Octubre")) {
                        municipio = "Díez de Octubre";
                    } else if (municipio.equalsIgnoreCase("Jaguey Grande")) {
                        municipio = "Jagüey Grande";
                    } else if (municipio.equalsIgnoreCase("Quemado de Guines")) {
                        municipio = "Quemado de Güines";
                    } else if (municipio.equalsIgnoreCase("Cabaiguan")) {
                        municipio = "Cabaiguán";
                    } else if (municipio.equalsIgnoreCase("Baragua ")) {
                        municipio = "Baraguá";
                    } else if (municipio.equalsIgnoreCase("1ro de Enero")) {
                        municipio = "Primero de Enero";
                    } else if (municipio.equalsIgnoreCase("Ciego de Avila")) {
                        municipio = "Ciego de Ávila";
                    } else if (municipio.equalsIgnoreCase("Camaguey")) {
                        municipio = "Camagüey";
                    } else if (municipio.equalsIgnoreCase("Céspedes")) {
                        municipio = "Carlos Manuel de Céspedes";
                    } else if (municipio.equalsIgnoreCase("Guaimaro")) {
                        municipio = "Guáimaro";
                    } else if (municipio.equalsIgnoreCase("Gíbara")) {
                        municipio = "Gibara";
                    } else if (municipio.equalsIgnoreCase("Pilon")) {
                        municipio = "Pilón";
                    } else if (municipio.equalsIgnoreCase("Rio Cauto")) {
                        municipio = "Río Cauto";
                    } else if (municipio.equalsIgnoreCase("II Frente")) {
                        municipio = "Segundo Frente";
                    } else if (municipio.equalsIgnoreCase("Songo La Maya ")) {
                        municipio = "Songo - La Maya";
                    } else if (municipio.equalsIgnoreCase("III Frente")) {
                        municipio = "Tercer Frente";
                    } else if (municipio.equalsIgnoreCase("Imias")) {
                        municipio = "Imías";
                    } else if (municipio.equalsIgnoreCase("San Antonio de las Baños")) {
                        municipio = "San Antonio de los Baños";
                    } else if (municipio.equalsIgnoreCase("Bahia Honda")) {
                        municipio = "Bahía Honda";
                    } else if (municipio.equalsIgnoreCase("Sagua La Grande")) {
                        municipio = "Sagua la Grande";
                    } else if (municipio.equalsIgnoreCase("Morón ")) {
                        municipio = "Morón";
                    } else if (municipio.equalsIgnoreCase("Antillas")) {
                        municipio = "Antilla";
                    } else if (municipio.equalsIgnoreCase("Plaza de la Revolución")) {
                        municipio = "Plaza";
                    } else if (municipio.equalsIgnoreCase("San Nicolás de Bari")) {
                        municipio = "San Nicolás";
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
