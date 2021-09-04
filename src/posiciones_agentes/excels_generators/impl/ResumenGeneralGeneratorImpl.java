package posiciones_agentes.excels_generators.impl;

import com.gembox.spreadsheet.*;
import javafx.scene.control.Alert;
import posiciones_agentes.excels_generators.ResumenGeneralGenerator;
import posiciones_agentes.models.RegistroPosicionesAgentes;
import posiciones_agentes.utils.CalculoTarifas;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ResumenGeneralGeneratorImpl implements ResumenGeneralGenerator {

    @Override
    public boolean generarResumen(String path) {
        ExcelFile workbook = new ExcelFile();
        boolean result = false;
        if(this.generarListado(workbook)&&this.generarResumenXUO(workbook)){
            try{
                workbook.save(path);
                result=true;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean generarListado(ExcelFile workbook){
        int hojas = 0;
        List<RegistroPosicionesAgentes> posicionesAgentes = ServiceLocator.getRegistroPosicionesAgentesService().getAllRegistroPosicionesAgentes();
        int posOnAgents = 0;
        while(hojas < 5 && posOnAgents<posicionesAgentes.size()){
            //Creo el encabezado
            ExcelWorksheet sheet = workbook.addWorksheet("Listado "+ (hojas==0?"":hojas));

            this.generateEncabezado("A1:A2","No",sheet);
            this.generateEncabezado("B1:B2","U/O",sheet);
            this.generateEncabezado("C1:C2","Posición de ASP\n" +
                    "(Instalación que Protege)",sheet);
            this.generateEncabezado("D1:D2","Proveedor de servicio",sheet);
            this.generateEncabezado("E1:E2","Cantidad de efectivos",sheet);
            this.generateEncabezado("F1:F2","Horas en Días Laborables",sheet);
            this.generateEncabezado("G1:G2", "Horas en Días No Laborables",sheet);
            this.generateEncabezado("H1:H2","Gasto anual",sheet);

            //freezing rows 1 and 2
            sheet.setPanes(new WorksheetPanes(PanesState.FROZEN_SPLIT,0,2,"A3", PanePosition.BOTTOM_LEFT));
            //comienzo con las tuplas
            int tuplas =3;
            while(tuplas<150&&posOnAgents<posicionesAgentes.size()){
                writeCellOnListado(tuplas,sheet,posicionesAgentes.get(posOnAgents));
                tuplas++;
                posOnAgents++;
            }
            hojas++;
        }
        return true;
    }

    private boolean generarResumenXUO(ExcelFile workbook){
        if(workbook.getWorksheets().size()<5) {
            ExcelWorksheet sheet = workbook.addWorksheet("Resumen X UO");

        }else{
            Util.dialogResult("No se pudo generar el resumen por Unidad Organizativa las posiciones exceden 740, genere el resumen por Unidad organizativa individualmente",
                    Alert.AlertType.WARNING);
        }
        return true;
    }


    private void generateEncabezado(String range, String label,ExcelWorksheet sheet){
        CellRange cell = sheet.getCells().getSubrange(range);
        cell.setMerged(true);
        cell.setValue(label);
        cell.setStyle(Util.generarStilo());
    }

    private void writeCellOnListado(int tupla,ExcelWorksheet sheet, RegistroPosicionesAgentes posicionAgente){
        sheet.getCells().getSubrange("A"+tupla+":H"+tupla).forEach(cell->{
            cell.setStyle(Util.generarBordes());
        });
        sheet.getCell("A"+tupla).setValue(tupla-2);
        sheet.getCell("B"+tupla).setValue(posicionAgente.getUnidadOrganizativa().getUnidad_organizativa());
        sheet.getCell("C"+tupla).setValue(posicionAgente.getInstalacion());
        sheet.getCell("D"+tupla).setValue(posicionAgente.getProveedorServicio().getProveedorServicio());
        sheet.getCell("E"+tupla).setValue(posicionAgente.getCantidadEfectivos());
        sheet.getCell("F"+tupla).setValue(posicionAgente.getHorasDiasLaborables());
        sheet.getCell("G"+tupla).setValue(posicionAgente.getHorasDiasNoLaborables());
        sheet.getCell("H"+tupla).setValue(CalculoTarifas.calculateByProviderOnAYear(posicionAgente, LocalDate.now().getYear()));
    }
}
