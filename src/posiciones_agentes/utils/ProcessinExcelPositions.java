package posiciones_agentes.utils;

import models.UnidadOrganizativa;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import posiciones_agentes.models.ProveedorServicio;
import posiciones_agentes.models.RegistroPosicionesAgentes;
import services.ServiceLocator;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProcessinExcelPositions {

    public static List<RegistroPosicionesAgentes> getDataFromExcel(File excelFile, UnidadOrganizativa unidadOrganizativa) {
        List<RegistroPosicionesAgentes> data = new LinkedList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(excelFile);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() > 1) {
                    String instalacion = row.getCell(1).getStringCellValue();
                    if (!instalacion.isEmpty()) {
                        String proveedorName = row.getCell(2).getStringCellValue();
                        if (proveedorName.equalsIgnoreCase("otro")) {
                            proveedorName = "Otros";
                        }
                        int cantEfectivos = Double.valueOf(row.getCell(3).getNumericCellValue()).intValue();
                        int cantHorasLab = Double.valueOf(row.getCell(4).getNumericCellValue()).intValue();
                        int cantHorasNoLab = Double.valueOf(row.getCell(5).getNumericCellValue()).intValue();
                        ProveedorServicio proveedorServicio = ServiceLocator.getProveedorServicioService().getByName(proveedorName);
                        data.add(new
                                RegistroPosicionesAgentes(0, instalacion,
                                unidadOrganizativa, proveedorServicio, cantHorasNoLab, cantHorasLab, cantEfectivos));

                        System.out.println(instalacion
                                + "-" + proveedorName + "-" + cantEfectivos + "-" + cantHorasLab + "-" + cantHorasNoLab);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}
