package informes_generate;

import models.TpubCentroAgenteFromExcel;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
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
            while(rowIterator.hasNext()){
                Row row = rowIterator.next();
                if(row.getRowNum()>3&&!row.getCell(1).getStringCellValue().equalsIgnoreCase("Total")){
                    System.out.println(row.getCell(1).getStringCellValue());
                }
            }
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
            HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(sheet,workbook);
            Iterator<HSSFRow> rowIterator = sheet.rowIterator();
            while(rowIterator.hasNext()){
                HSSFRow row = rowIterator.next();
                String municipio =  row.getCell(Short.parseShort("1")).getStringCellValue();
                int tpub = Double.valueOf((row.getCell(Short.parseShort("13"))).getNumericCellValue()).intValue();
                int centroAgente = Double.valueOf(evaluator.evaluate(row.getCell(Short.parseShort("12"))).getNumberValue()).intValue();
                if(row.getRowNum()>2&&!row.getCell(Short.parseShort("1")).getStringCellValue().equalsIgnoreCase("Total")){
                    data.add( new TpubCentroAgenteFromExcel(
                           municipio,
                            tpub,
                            centroAgente
                    ));
                }
            }
            for (TpubCentroAgenteFromExcel pp:
                 data) {
                System.out.println(pp.getMunicipio()+" "+pp.getCentroAgentes()+" "+pp.getTpubs());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return data;
    }
}
