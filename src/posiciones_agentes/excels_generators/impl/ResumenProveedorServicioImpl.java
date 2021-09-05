package posiciones_agentes.excels_generators.impl;

import com.gembox.spreadsheet.CellRange;
import com.gembox.spreadsheet.ExcelFile;
import com.gembox.spreadsheet.ExcelWorksheet;
import com.gembox.spreadsheet.LengthUnit;
import posiciones_agentes.excels_generators.ResumenProveedorServicio;
import posiciones_agentes.models.ProveedorGasto;
import posiciones_agentes.utils.CalculoByOurg;
import util.Util;

import java.io.IOException;
import java.util.List;

public class ResumenProveedorServicioImpl implements ResumenProveedorServicio {

    @Override
    public boolean generarResumenProveedorServicio(String path) {
        ExcelFile workbook = new ExcelFile();
        boolean result = false;
        if (this.generarResumenPS(workbook)) {
            try {
                workbook.save(path);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean generarResumenPS(ExcelFile workbook){
        ExcelWorksheet sheet = workbook.addWorksheet("Resumen x PS");
        generateEncabezado("A1:A2", "Proveedor de Servicio", sheet, true);
        sheet.getColumn("A").setWidth(105, LengthUnit.PIXEL);
        generateEncabezado("B1:B2", "Cantidad Posiciones", sheet, true);
        sheet.getColumn("B").setWidth(75, LengthUnit.PIXEL);
        generateEncabezado("C1:C2", "Gasto Anual", sheet, true);

        List<ProveedorGasto> proveedorGastoList = CalculoByOurg.calculoProveedorGasto();

        int row = 3;
        while ((row-3) < proveedorGastoList.size()){
            writeCellOnResumenXPS(row, sheet, proveedorGastoList.get(row-3));
            row++;
        }

        generateFooterOnResumenXPS(row, sheet);

        return true;
    }

    private void writeCellOnResumenXPS(int record, ExcelWorksheet sheet, ProveedorGasto pg) {
        sheet.getCells().getSubrange("A" + record + ":C" + record).forEach(cell ->
                cell.setStyle(Util.generarBordes())
        );

        sheet.getCell("A"+record).setValue(pg.getProveedor());
        sheet.getCell("B"+record).setValue(pg.getCantPosiciones());
        sheet.getCell("C"+record).setValue(pg.getGasto());
    }

    private void generateFooterOnResumenXPS(int finalRecord, ExcelWorksheet sheet){
        sheet.getCells().getSubrange("A" + finalRecord + ":C" + finalRecord).forEach(cell -> {
                    cell.setStyle(Util.generarBordes());
                    cell.setStyle(Util.generarStilo());
                }
        );
        sheet.getCell("A"+finalRecord).setValue("Total:");
        sheet.getCell("B"+finalRecord).setFormula("=SUM(B3:B"+(finalRecord-1)+")");
        sheet.getCell("C"+finalRecord).setFormula("=SUM(C3:C"+(finalRecord-1)+")");
    }

    private void generateEncabezado(String range, String label, ExcelWorksheet sheet, boolean merged) {
        CellRange cell = sheet.getCells().getSubrange(range);
        cell.setValue(label);
        cell.setStyle(Util.generarStilo());
        if (merged)
            cell.setMerged(true);
    }
}
