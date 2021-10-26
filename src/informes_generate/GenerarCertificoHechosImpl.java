package informes_generate;

import com.gembox.spreadsheet.CellRange;
import com.gembox.spreadsheet.ExcelFile;
import com.gembox.spreadsheet.ExcelWorksheet;
import models.HechosCertifico;
import services.ServiceLocator;
import util.Util;

import java.io.IOException;
import java.util.LinkedList;

public class GenerarCertificoHechosImpl implements GenerarCertificoHechos {


    @Override
    public boolean generarCertificoHechos(int anno, String mes, String path) {
        String DIRECCION_ARCHIVO = path+"/CertificoHechos.xlsx";
        boolean result = false;
        ExcelFile workbook = new ExcelFile();
        if (crearCertifico(anno, mes, workbook))
            try {
                workbook.save(DIRECCION_ARCHIVO);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        return result;
    }



    private boolean crearCertifico(int anno, String mes, ExcelFile workbook) {

        ExcelWorksheet worksheet = workbook.addWorksheet("Certifico");
        CellRange rangeA1G1 = worksheet.getCells().getSubrange("A1:G1");
        rangeA1G1.forEach(p -> p.setStyle(Util.generarStilo()));
        worksheet.getCell("A1").setValue("U/O");
        worksheet.getCell("B1").setValue("Planta Exterior");
        worksheet.getCell("C1").setValue("Telefonía Pública");
        worksheet.getCell("D1").setValue("Robo");
        worksheet.getCell("E1").setValue("Hurto");
        worksheet.getCell("F1").setValue("Fraude");
        worksheet.getCell("G1").setValue("Otros delitos");

        int month = 0;
        if (!mes.equalsIgnoreCase("<<Todos>>")) {
            month = determinateNumberOfMonth(mes);
        }
        return this.writeData(
                ServiceLocator.getHechosService().obtenerHechosParaCertifico(anno, month),
                worksheet);


    }

    private boolean writeData(LinkedList<HechosCertifico> hechos, ExcelWorksheet sheet) {
        int valorFila = 2;
        for (HechosCertifico hecho : hechos) {
            if (notZeerosValues(hecho)) {
                sheet.getCell("A" + valorFila).setValue(hecho.getUorg());
                sheet.getCell("B" + valorFila).setValue(hecho.getPext());
                sheet.getCell("C" + valorFila).setValue(hecho.getTpub());
                sheet.getCell("D" + valorFila).setValue(hecho.getRobo());
                sheet.getCell("E" + valorFila).setValue(hecho.getHurto());
                sheet.getCell("F" + valorFila).setValue(hecho.getFraude());
                sheet.getCell("G" + valorFila).setValue(hecho.getOtros());
                sheet.getCells().getSubrange("A" + valorFila + ":G" + valorFila).forEach(cell -> cell.setStyle(Util.generarBordes()));
                valorFila++;
            }
        }
        return true;
    }

    private boolean notZeerosValues(HechosCertifico hechosCertifico) {
        boolean noZeeroValues = true;
        int cantZeeros = 0;

        if (hechosCertifico.getFraude() == 0) {
            cantZeeros++;
        }
        if (hechosCertifico.getHurto() == 0) {
            cantZeeros++;
        }
        if (hechosCertifico.getOtros() == 0) {
            cantZeeros++;
        }
        if (hechosCertifico.getPext() == 0) {
            cantZeeros++;
        }
        if (hechosCertifico.getRobo() == 0) {
            cantZeeros++;
        }
        if (hechosCertifico.getTpub() == 0) {
            cantZeeros++;
        }

        if (cantZeeros == 6) {
            noZeeroValues = false;
        }
        return noZeeroValues;
    }

    private int determinateNumberOfMonth(String name) {
        int mes = 0;
        boolean notFound = true;
        for (int i = 0; i < Util.meses2.length && notFound; i++) {
            if (Util.meses2[i].equalsIgnoreCase(name)) {
                notFound = false;
                mes = i + 1;
            }
        }
        return mes;
    }
}
