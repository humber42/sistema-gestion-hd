package informes_generate;

import com.gembox.spreadsheet.ExcelFile;
import com.gembox.spreadsheet.ExcelWorksheet;
import com.gembox.spreadsheet.LengthUnit;
import models.Hechos;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import static util.Util.generarStilo;

public class GenerarHechosPendientesImpl implements GenerarHechosPendientes {

    @Override
    public boolean generarHechosPendientes(LinkedList<Hechos> pendientes, String path) {
        String DIRECCION_ARCHIVO = path+"/HechosPendientes.xlsx";
        boolean result = false;
        ExcelFile workbook = new ExcelFile();
        if (generarTabla(workbook, pendientes))
            try {
                workbook.save(DIRECCION_ARCHIVO);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        return result;
    }

    private boolean generarTabla(ExcelFile workbook, LinkedList<Hechos> pendientes) {
        ExcelWorksheet sheet = workbook.addWorksheet("Pendientes");
        sheet.getCell("A1").setValue("Tipo");
        sheet.getColumn("A").setWidth(15, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getCell("B1").setValue("UOrg");
        sheet.getCell("C1").setValue("Fecha Ocurrencia");
        sheet.getColumn("C").setWidth(16, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getCell("D1").setValue("Lugar");
        sheet.getColumn("D").setWidth(48, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getCell("E1").setValue("Municipio");
        sheet.getColumn("E").setWidth(15, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getCell("F1").setValue("No. Denuncia");
        sheet.getColumn("F").setWidth(15, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getCell("G1").setValue("Afectación");
        sheet.getColumn("G").setWidth(13, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getCell("H1").setValue("Cod. CDNT");
        sheet.getColumn("H").setWidth(17, LengthUnit.ZERO_CHARACTER_WIDTH);
        sheet.getCells().getSubrange("A1:H1").forEach(p -> p.setStyle(generarStilo()));

        int i = 2;
        Iterator<Hechos> iterator = pendientes.iterator();
        while (iterator.hasNext()) {
            Hechos hecho = iterator.next();
            sheet.getCell("A" + i).setValue(hecho.getTipoHecho().getTipo_hecho());
            sheet.getCell("B" + i).setValue(hecho.getUnidadOrganizativa().getUnidad_organizativa());
            sheet.getCell("C" + i).setValue(hecho.getFecha_ocurrencia().toLocalDate().toString());
            sheet.getCell("D" + i).setValue(hecho.getLugar());
            sheet.getCell("E" + i).setValue(hecho.getMunicipio().getMunicipio());
            sheet.getCell("F" + i).setValue(hecho.getNumero_denuncia());
            sheet.getCell("G" + i).setValue(hecho.getAfectacion_usd() * 24 + hecho.getAfectacion_mn());
            sheet.getCell("H" + i).setValue(hecho.getCod_cdnt().toUpperCase());
            i++;
        }

        return true;
    }
}
