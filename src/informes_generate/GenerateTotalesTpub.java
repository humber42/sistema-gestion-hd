package informes_generate;

import com.gembox.spreadsheet.ExcelCell;
import com.gembox.spreadsheet.ExcelFile;
import com.gembox.spreadsheet.ExcelWorksheet;
import models.EstacionPublicaCentroAgente;
import util.Util;

import java.util.List;

public class GenerateTotalesTpub {
    public boolean generarTotales(String path, List<EstacionPublicaCentroAgente> estaciones) {
        String direccion = path + "/TotalesDesnsidad.xlsx";
        ExcelFile workbook = new ExcelFile();
        boolean result = false;
        if (generarData(estaciones, workbook)) {
            try {
                workbook.save(direccion);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean generarData(List<EstacionPublicaCentroAgente> estaciones, ExcelFile workbook) {
        ExcelWorksheet sheet = workbook.addWorksheet("Totales");
        ExcelCell cellA1 = sheet.getCell("A1");
        cellA1.setStyle(Util.generarStilo());
        cellA1.setValue("U/O");
        ExcelCell cellB1 = sheet.getCell("B1");
        cellB1.setStyle(Util.generarStilo());
        cellB1.setValue("Centro Agente");
        ExcelCell cellC1 = sheet.getCell("C1");
        cellC1.setStyle(Util.generarStilo());
        cellC1.setValue("Est. Públ.");
        ExcelCell cellD1 = sheet.getCell("D1");
        cellD1.setStyle(Util.generarStilo());
        cellD1.setValue("Totales:");

        int row = 2;
        for (EstacionPublicaCentroAgente estacion : estaciones) {
            sheet.getCell("A" + row).setValue(estacion.getUnidadOrganizativa());
            sheet.getCell("A" + row).setStyle(Util.generarBordes());
            sheet.getCell("B" + row).setValue(estacion.getCentroAgente());
            sheet.getCell("B" + row).setStyle(Util.generarBordes());
            sheet.getCell("C" + row).setValue(estacion.getEstacionPublica());
            sheet.getCell("C" + row).setStyle(Util.generarBordes());
            sheet.getCell("D" + row).setFormula("=SUM(B" + row + ":C" + row + ")");
            sheet.getCell("D" + row).setStyle(Util.generarBordes());
            row++;
        }

        ExcelCell cellAForm = sheet.getCell("A" + row);
        cellAForm.setStyle(Util.generarStilo());
        cellAForm.setValue("Totales:");

        ExcelCell cellBForm = sheet.getCell("B" + row);
        cellBForm.setStyle(Util.generarStilo());
        cellBForm.setFormula("=SUM(B1:B" + (row - 1) + ")");
        ExcelCell cellCForm = sheet.getCell("C" + row);
        cellCForm.setStyle(Util.generarStilo());
        cellCForm.setFormula("=SUM(C1:C" + (row - 1) + ")");
        ExcelCell cellDForm = sheet.getCell("D" + row);
        cellDForm.setStyle(Util.generarStilo());
        cellDForm.setFormula("=SUM(D1:D" + (row - 1) + ")");
        return true;
    }
}
