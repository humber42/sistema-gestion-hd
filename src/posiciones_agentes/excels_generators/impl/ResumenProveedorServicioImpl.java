package posiciones_agentes.excels_generators.impl;

import com.gembox.spreadsheet.CellRange;
import com.gembox.spreadsheet.ExcelFile;
import com.gembox.spreadsheet.ExcelWorksheet;
import posiciones_agentes.excels_generators.ResumenProveedorServicio;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;

public class ResumenProveedorServicioImpl implements ResumenProveedorServicio {

    @Override
    public boolean generarResumenProveedorServicio(String path) {
        ExcelFile workBook = new ExcelFile();
        boolean result = false;
        if(this.generarListadoProveedores(workBook)){
            try {
                workBook.save(path);
                result = true;
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean generarListadoProveedores(ExcelFile excel){
        ExcelWorksheet worksheet = excel.addWorksheet("Resumen por U/O");

        generarEncabezado("A1:A2", "U/O", worksheet, true);
        generarEncabezado("B1:B2", "Cantidad Posiciones", worksheet, true);
        generarEncabezado("C1:G1", "Proveedor de Servicio", worksheet, true);
        generarEncabezado("C2", "SEPSA", worksheet, false);
        generarEncabezado("D2", "SEPCOM", worksheet, false);
        generarEncabezado("E2", "CAP", worksheet, false);
        generarEncabezado("F2", "AGESP", worksheet, false);
        generarEncabezado("G2", "Otros", worksheet, false);
        generarEncabezado("H1:H2", "Gasto Anual", worksheet, true);

        int pages = 1;
        int size = ServiceLocator.getRegistroPosicionesAgentesService().getAllUorgNames().size();

        while (pages > 0){
            int row = 3;
            while (row < size){
                row++;
            }
            pages--;
        }

        return true;
    }

    private void writeOnCell(int record, ExcelWorksheet sheet){

    }

    private void generarEncabezado(String range, String title, ExcelWorksheet sheet, boolean merged){
        CellRange genericCellRange = sheet.getCells().getSubrange(range);
        genericCellRange.setValue(title);
        genericCellRange.setStyle(Util.generarStilo());
        if(merged)
            genericCellRange.setMerged(true);
    }
}
