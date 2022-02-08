package posiciones_agentes.excels_generators.impl;

import com.gembox.spreadsheet.*;
import javafx.scene.control.Alert;
import posiciones_agentes.excels_generators.ResumenGeneralGenerator;
import posiciones_agentes.models.PosicionAgente;
import posiciones_agentes.models.ProveedorGasto;
import posiciones_agentes.models.UOrgPosiciones;
import posiciones_agentes.utils.CalculoByOurg;
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
        if (this.generarListado(workbook) && this.generarResumenXUO(workbook)
                && this.generarResumenXPS(workbook, path)) {
            try {
                workbook.save(path);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean generarListado(ExcelFile workbook) {
        int hojas = 0;
        List<PosicionAgente> posicionesAgentes = ServiceLocator.getRegistroPosicionesAgentesService().getAll();
        int posOnAgents = 0;
        while (hojas < 5 && posOnAgents < posicionesAgentes.size()) {
            //Creo el encabezado
            ExcelWorksheet sheet = workbook.addWorksheet("Listado " + (hojas == 0 ? "" : hojas));

            this.generateEncabezado("A1:A2", "No", sheet, true);
            this.generateEncabezado("B1:B2", "U/O", sheet, true);
            this.generateEncabezado("C1:C2", "Posición de ASP\n" +
                    "(Instalación que Protege)", sheet, true);
            sheet.getColumn("C").setWidth(190, LengthUnit.PIXEL);
            this.generateEncabezado("D1:D2", "Proveedor de servicio", sheet, true);
            sheet.getColumn("D").setWidth(105, LengthUnit.PIXEL);
            this.generateEncabezado("E1:E2", "Cantidad de efectivos", sheet, true);
            sheet.getColumn("E").setWidth(120, LengthUnit.PIXEL);
            this.generateEncabezado("F1:F2", "Horas en Días Laborables", sheet, true);
            sheet.getColumn("F").setWidth(121, LengthUnit.PIXEL);
            this.generateEncabezado("G1:G2", "Horas en Días No Laborables", sheet, true);
            sheet.getColumn("G").setWidth(123, LengthUnit.PIXEL);
            this.generateEncabezado("H1:H2", "Gasto anual", sheet, true);


            //freezing rows 1 and 2
            sheet.setPanes(new WorksheetPanes(PanesState.FROZEN_SPLIT, 0, 2, "A3", PanePosition.BOTTOM_LEFT));
            //comienzo con las tuplas
            int tuplas = 3;
            while (tuplas < 150 && posOnAgents < posicionesAgentes.size()) {
                writeCellOnListado(tuplas, sheet, posicionesAgentes.get(posOnAgents));
                tuplas++;
                posOnAgents++;
            }
            hojas++;
        }
        return true;
    }

    private boolean generarResumenXUO(ExcelFile workbook) {
        if (workbook.getWorksheets().size() < 5) {
            ExcelWorksheet sheet = workbook.addWorksheet("Resumen X UO");
            generateEncabezado("A1:A2", "U/O", sheet, true);
            generateEncabezado("B1:B2", "Cantidad Posiciones", sheet, true);
            generateEncabezado("C1:G1", "Proveedor de Servicio", sheet, true);
            generateEncabezado("C2", "SEPSA", sheet, false);
            generateEncabezado("D2", "SEPCOM", sheet, false);
            generateEncabezado("E2", "CAP", sheet, false);
            generateEncabezado("F2", "AGESP", sheet, false);
            generateEncabezado("G2", "Otros", sheet, false);
            generateEncabezado("H1:H2", "Gasto Anual", sheet, true);

            sheet.setPanes(new WorksheetPanes(PanesState.FROZEN_SPLIT, 0, 2, "A3", PanePosition.BOTTOM_LEFT));

            List<UOrgPosiciones> uOrgPosicionesList = CalculoByOurg.obtenerUOrgByPosicionesList();

            int row = 3;

            while ((row-3) < uOrgPosicionesList.size()) {
                UOrgPosiciones uOrgPosiciones = uOrgPosicionesList.get(row-3);
                writeCellForResumenUO(row, sheet, uOrgPosiciones);
                row++;
            }

            generateFooter(row,sheet);
        } else {
            Util.dialogResult("No se pudo generar el resumen por Unidad Organizativa las posiciones exceden 740, genere el resumen por Unidad organizativa individualmente",
                    Alert.AlertType.WARNING);
        }
        return true;
    }

    private void writeCellForResumenUO(int record, ExcelWorksheet sheet, UOrgPosiciones uOrgPosiciones) {
        sheet.getCells().getSubrange("A" + record + ":H" + record).forEach(cell ->
                cell.setStyle(Util.generarBordes())
        );

        sheet.getCell("A" + record).setValue(uOrgPosiciones.getUorg());
        sheet.getCell("B" + record).setValue(uOrgPosiciones.getCantPosiciones());
        sheet.getCell("C" + record).setValue(uOrgPosiciones.getSepsa());
        sheet.getCell("D" + record).setValue(uOrgPosiciones.getSepcom());
        sheet.getCell("E" + record).setValue(uOrgPosiciones.getCap());
        sheet.getCell("F" + record).setValue(uOrgPosiciones.getAgesp());
        sheet.getCell("G" + record).setValue(uOrgPosiciones.getOtros());
        sheet.getCell("H" + record).setValue(uOrgPosiciones.getGasto());

    }

    private void generateEncabezado(String range, String label, ExcelWorksheet sheet, boolean merged) {
        CellRange cell = sheet.getCells().getSubrange(range);
        cell.setValue(label);
        cell.setStyle(Util.generarStilo());
        if (merged)
            cell.setMerged(true);
    }

    private void writeCellOnListado(int tupla, ExcelWorksheet sheet, PosicionAgente posicionAgente) {
        sheet.getCells().getSubrange("A" + tupla + ":H" + tupla).forEach(cell ->
            cell.setStyle(Util.generarBordes())
        );

        sheet.getCell("A" + tupla).setValue(tupla - 2);
        sheet.getCell("B" + tupla).setValue(posicionAgente.getUnidadOrganizativa());
        sheet.getCell("C" + tupla).setValue(posicionAgente.getInstalacion());
        sheet.getCell("C" + tupla).getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.JUSTIFY);
        sheet.getCell("D" + tupla).setValue(posicionAgente.getProveedor());
        sheet.getCell("E" + tupla).setValue(posicionAgente.getCantEfectivos());
        sheet.getCell("F" + tupla).setValue(posicionAgente.getHorasDL());
        sheet.getCell("G" + tupla).setValue(posicionAgente.getHorasDNL());
        sheet.getCell("H" + tupla).setValue(CalculoTarifas.calculateByProviderOnAYear(posicionAgente, LocalDate.now().getYear()));
    }

    private void generateFooter(int finalRecord, ExcelWorksheet sheet){
        sheet.getCells().getSubrange("A" + finalRecord + ":H" + finalRecord).forEach(cell -> {
                    cell.setStyle(Util.generarBordes());
                    cell.setStyle(Util.generarStilo());
        }
        );
        sheet.getCell("A"+finalRecord).setValue("Total:");
        sheet.getCell("B"+finalRecord).setFormula("=SUM(B3:B"+(finalRecord-1)+")");
        sheet.getCell("C" + finalRecord).setFormula("=SUM(C3:C" + (finalRecord - 1) + ")");
        sheet.getCell("D" + finalRecord).setFormula("=SUM(D3:D" + (finalRecord - 1) + ")");
        sheet.getCell("E" + finalRecord).setFormula("=SUM(E3:E" + (finalRecord - 1) + ")");
        sheet.getCell("F" + finalRecord).setFormula("=SUM(F3:F" + (finalRecord - 1) + ")");
        sheet.getCell("G" + finalRecord).setFormula("=SUM(G3:G" + (finalRecord - 1) + ")");
        sheet.getCell("H" + finalRecord).setFormula("=SUM(H3:H" + (finalRecord - 1) + ")");
    }

    private boolean generarResumenXPS(ExcelFile workbook, String path) {
        if (workbook.getWorksheets().size() < 5) {
            this.metodoResumidoResumenXPS(workbook);
        } else {
            ExcelFile workbook2 = new ExcelFile();
            String path2 = path.replace("ResumenGeneralPosiciones.xlsx", "ResumenGeneralPosicionesXPS.xlsx");
            this.metodoResumidoResumenXPS(workbook2);
            try {
                workbook2.save(path2);
                Runtime.getRuntime().exec("cmd /c start " + path2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void metodoResumidoResumenXPS(ExcelFile workbook) {
        ExcelWorksheet sheet = workbook.addWorksheet("Resumen x PS");
        generateEncabezado("A1:A2", "Proveedor de Servicio", sheet, true);
        sheet.getColumn("A").setWidth(105, LengthUnit.PIXEL);
        generateEncabezado("B1:B2", "Cantidad Posiciones", sheet, true);
        sheet.getColumn("B").setWidth(75, LengthUnit.PIXEL);
        generateEncabezado("C1:C2", "Gasto Anual", sheet, true);

        List<ProveedorGasto> proveedorGastoList = CalculoByOurg.calculoProveedorGasto();

        int row = 3;
        while ((row - 3) < proveedorGastoList.size()) {
            writeCellOnResumenXPS(row, sheet, proveedorGastoList.get(row - 3));
            row++;
        }

        generateFooterOnResumenXPS(row, sheet);
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
}
