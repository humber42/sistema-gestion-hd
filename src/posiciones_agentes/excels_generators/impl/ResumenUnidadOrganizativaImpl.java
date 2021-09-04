package posiciones_agentes.excels_generators.impl;

import com.gembox.spreadsheet.*;
import models.UnidadOrganizativa;
import posiciones_agentes.excels_generators.ResumenUnidadOrganizativa;
import posiciones_agentes.models.RegistroPosicionesAgentes;
import posiciones_agentes.utils.CalculoTarifas;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ResumenUnidadOrganizativaImpl implements ResumenUnidadOrganizativa {

    @Override
    public boolean generarResumenUnidadOrganizativa(String path, UnidadOrganizativa unidadOrganizativa) {
        ExcelFile workBook = new ExcelFile();
        boolean result = false;
        if(this.listadoRelacion(workBook, unidadOrganizativa)){
            try{
                workBook.save(path);
                result = true;
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        return result;
    }

    private boolean listadoRelacion(ExcelFile excel, UnidadOrganizativa unidadOrganizativa){
        ExcelWorksheet worksheet = excel.addWorksheet("Relación");

        generarEncabezado("A1:A2", "No.", worksheet);
        generarEncabezado("B1:B2", "Posición de ASP (Instalación que Protege)", worksheet);
        generarEncabezado("C1:C2", "Proveedor de servicio", worksheet);
        generarEncabezado("D1:D2", "Cantidad Efectivos", worksheet);
        generarEncabezado("E1:E2", "Horas en Días Laborales", worksheet);
        generarEncabezado("F1:F2", "Horas en Días No Laborales", worksheet);
        generarEncabezado("G1:G2", "Gasto Anual", worksheet);

        List<RegistroPosicionesAgentes> posicionesList = ServiceLocator.getRegistroPosicionesAgentesService()
                .getAllRegistrosByUOrg(unidadOrganizativa.getId_unidad_organizativa());

        int size = posicionesList.size();
        int pages = size%150==1?2:1;

        worksheet.setPanes(new WorksheetPanes(PanesState.FROZEN_SPLIT, 0, 2, "A3", PanePosition.BOTTOM_LEFT));

        while (pages > 0){
            int row = 0;
            while (row < size){
                RegistroPosicionesAgentes rpa = posicionesList.get(row);
                writeOnCell(row++, worksheet, rpa);
            }
            pages--;
        }
        return true;
    }

    private void writeOnCell(int record, ExcelWorksheet sheet, RegistroPosicionesAgentes rpa){
        sheet.getCells().getSubrange("A"+record+":G"+record).forEach(cell ->
                cell.setStyle(Util.generarBordes())
        );
        sheet.getCell("A"+record).setValue(record-2);
        sheet.getCell("B"+record).setValue(rpa.getInstalacion());
        sheet.getCell("C"+record).setValue(rpa.getProveedorServicio().getProveedorServicio());
        sheet.getCell("D"+record).setValue(rpa.getCantidadEfectivos());
        sheet.getCell("E"+record).setValue(rpa.getHorasDiasLaborables());
        sheet.getCell("F"+record).setValue(rpa.getHorasDiasNoLaborables());
        sheet.getCell("G"+record).setValue(CalculoTarifas.calculateByProviderOnAYear(rpa, LocalDate.now().getYear()));
    }

    private void generarEncabezado(String range, String title, ExcelWorksheet worksheet){
        CellRange genericCellRange = worksheet.getCells().getSubrange(range);
        genericCellRange.setValue(title);
        genericCellRange.setStyle(Util.generarStilo());
        genericCellRange.setMerged(true);
    }
}
