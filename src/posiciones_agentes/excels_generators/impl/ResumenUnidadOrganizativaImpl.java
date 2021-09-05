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
        generarEncabezado("B1:B2", "Posición de ASP\n" +
                "(Instalación que Protege)", worksheet);
        worksheet.getColumn("B").setWidth(194,LengthUnit.PIXEL);
        generarEncabezado("C1:C2", "Proveedor de servicio", worksheet);
        worksheet.getColumn("C").setWidth(130,LengthUnit.PIXEL);
        generarEncabezado("D1:D2", "Cantidad Efectivos", worksheet);
        generarEncabezado("E1:E2", "Horas en Días Laborales", worksheet);
        worksheet.getColumn("E").setWidth(100,LengthUnit.PIXEL);
        generarEncabezado("F1:F2", "Horas en Días No Laborales", worksheet);
        worksheet.getColumn("F").setWidth(100,LengthUnit.PIXEL);
        generarEncabezado("G1:G2", "Gasto Anual", worksheet);

        List<RegistroPosicionesAgentes> posicionesList = ServiceLocator.getRegistroPosicionesAgentesService()
                .getAllRegistrosByUOrg(unidadOrganizativa.getId_unidad_organizativa());

        int size = posicionesList.size();
        int pages = size%150==1?2:1;

        worksheet.setPanes(new WorksheetPanes(PanesState.FROZEN_SPLIT, 0, 2, "A3", PanePosition.BOTTOM_LEFT));
        int posAgent = 0;
        while (pages > 0&&posAgent<size){
            int row = 3;
            while (row < 150&&posAgent<size){
                RegistroPosicionesAgentes rpa = posicionesList.get(posAgent);
                writeOnCell(row, worksheet, rpa);
                posAgent++;
                row++;
            }
            pages--;
        }
        return true;
    }

    private void writeOnCell(int record, ExcelWorksheet sheet, RegistroPosicionesAgentes rpa){
        sheet.getCells().getSubrange("A"+record+":G"+record).forEach(cell->{
            cell.setStyle(Util.generarBordes());
        });
        sheet.getCell("A"+record).setValue(record-2);
        sheet.getCell("B"+record).setValue(rpa.getInstalacion());
        sheet.getCell("B"+record).getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.JUSTIFY);
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
