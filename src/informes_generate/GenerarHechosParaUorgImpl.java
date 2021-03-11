package informes_generate;

import com.gembox.spreadsheet.*;
import models.Hechos;
import util.Util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class GenerarHechosParaUorgImpl implements GenerarHechosParaUOrg {

    @Override
    public boolean generarHechosParaUorg(LinkedList<Hechos> hechos) {
        String DIRECCION_ARCHIVO = "src/informesGenerados/HechosParaUnidadOrganizativa.xlsx";
        boolean result = false;
        ExcelFile workbook = new ExcelFile();
        if (generarTabla(workbook, hechos)) {
            try {
                workbook.save(DIRECCION_ARCHIVO);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean generarTabla(ExcelFile workbook, LinkedList<Hechos> hechos) {
        ExcelWorksheet sheet = workbook.addWorksheet("Hechos");
        sheet.getCell("A1").setValue("UOrg");
        sheet.getCell("B1").setValue("Tipo");
        sheet.getCell("C1").setValue("Fecha Ocurrencia");
        ExcelCell cell = sheet.getCell("D1");
        cell.setValue("Sintesis");
        sheet.getCell("E1").setValue("Municipio");
        sheet.getCell("F1").setValue("Afectacion USD");
        sheet.getCell("G1").setValue("Afectacion MN");
        sheet.getCell("H1").setValue("Afectacion Servicios");
        sheet.getCell("I1").setValue("TAfect");
        sheet.getCell("J1").setValue("Causa");
        sheet.getCell("K1").setValue("Imputable");
        sheet.getCell("L1").setValue("Incidencia");
        sheet.getCell("M1").setValue("Incendio");
        sheet.getCell("N1").setValue("CDNT");
        this.writeData(hechos, sheet);
        this.cambiarTamannoColumns(sheet);
        sheet.getCells().getSubrange("A1:N1").forEach(p -> p.setStyle(Util.generarStilo()));
        return true;
    }

    private boolean cambiarTamannoColumns(ExcelWorksheet sheet) {
        sheet.getColumn("A").setWidth(10.56, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("B").setWidth(13.65, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("C").setWidth(13.22, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("D").setWidth(42.22, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("E").setWidth(16.44, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("F").setWidth(11.11, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("G").setWidth(11.11, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("H").setWidth(11.11, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("I").setWidth(20, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("J").setWidth(20, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("K").setWidth(16.44, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("L").setWidth(16.44, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("M").setWidth(16.44, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getColumn("N").setWidth(15.78, LengthUnit.ZERO_CHARACTER_WIDTH);

        return true;
    }

    private boolean writeData(LinkedList<Hechos> hechos, ExcelWorksheet sheet) {
        int place = 2;
        for (Hechos hecho : hechos) {
            sheet.getCell("A" + place).setValue(hecho.getUnidadOrganizativa().getUnidad_organizativa());
            sheet.getCell("B" + place).setValue(hecho.getTipoHecho().getTipo_hecho());
            sheet.getCell("C" + place).setValue(hecho.getFecha_ocurrencia().toString());
            sheet.getCell("D" + place).setValue(hecho.getTitulo() + ". " + hecho.getLugar());
            sheet.getCell("E" + place).setValue(hecho.getMunicipio().getMunicipio());
            sheet.getCell("F" + place).setValue(hecho.getAfectacion_usd());
            sheet.getCell("G" + place).setValue(hecho.getAfectacion_mn());
            sheet.getCell("H" + place).setValue(hecho.getAfectacion_servicio());
            sheet.getCell("I" + place).setValue(Objects.isNull(hecho.getMateriales().getMateriales()) ? "" : hecho.getMateriales().getMateriales() + "-" + hecho.getCantidad());
            sheet.getCell("J" + place).setValue(hecho.getAveriasPext().getCausa());
            sheet.getCell("K" + place).setValue(hecho.isImputable() ? "Si" : "No");
            sheet.getCell("L" + place).setValue(hecho.isIncidencias() ? "Si" : "No");
            sheet.getCell("M" + place).setValue(hecho.getTipoHecho().getTipo_hecho().contains("Inc") ? "Si" : "No");
            sheet.getCell("N" + place).setValue(hecho.getCod_cdnt().toUpperCase());
            sheet.getCells().getSubrange("A" + place + ":" + "N" + place).forEach(p -> p.setStyle(Util.generarBordes()));
            sheet.getCell("D" + place).getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.JUSTIFY);
            place++;
        }
        return true;
    }


}
