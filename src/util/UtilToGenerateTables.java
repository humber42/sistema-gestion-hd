package util;

import com.gembox.spreadsheet.ExcelWorksheet;
import models.HechosByAnno;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Humberto Cabrera Dominguez
 * Generadores de tablas y encargados de llenar los datos
 * @version 1.0
 */
public class UtilToGenerateTables {

    /**
     * Metodo para llenar las tablas que recojen un resumen de los hechos sucedidos en annos diferentes
     *
     * @param hechosByAnnos listado de hechos por annos
     * @param sheet         hoja
     * @param listaLetras   listado de posiciones
     * @return resultado de la operacion
     */
    public static boolean llenarCamposCantHechosByAnno(LinkedList<HechosByAnno> hechosByAnnos, ExcelWorksheet sheet, ArrayList<String> listaLetras) {
        boolean result = false;
        if (hechosByAnnos != null) {
            result = true;
            hechosByAnnos.stream().forEach(hechosByAnno -> {
                if ((hechosByAnno.getMes() == 1)) {
                    sheet.getCell(listaLetras.get(0)).setValue(hechosByAnno.getCantPext());
                    sheet.getCell(listaLetras.get(1)).setValue(hechosByAnno.getCantTpub());
                } else if ((hechosByAnno.getMes() == 2)) {
                    sheet.getCell(listaLetras.get(2)).setValue(hechosByAnno.getCantPext());
                    sheet.getCell(listaLetras.get(3)).setValue(hechosByAnno.getCantTpub());
                } else if ((hechosByAnno.getMes() == 3)) {
                    sheet.getCell(listaLetras.get(4)).setValue(hechosByAnno.getCantPext());
                    sheet.getCell(listaLetras.get(5)).setValue(hechosByAnno.getCantTpub());
                } else if ((hechosByAnno.getMes() == 4)) {
                    sheet.getCell(listaLetras.get(6)).setValue(hechosByAnno.getCantPext());
                    sheet.getCell(listaLetras.get(7)).setValue(hechosByAnno.getCantTpub());
                } else if ((hechosByAnno.getMes() == 5)) {
                    sheet.getCell(listaLetras.get(8)).setValue(hechosByAnno.getCantPext());
                    sheet.getCell(listaLetras.get(9)).setValue(hechosByAnno.getCantTpub());
                } else if ((hechosByAnno.getMes() == 6)) {
                    sheet.getCell(listaLetras.get(10)).setValue(hechosByAnno.getCantPext());
                    sheet.getCell(listaLetras.get(11)).setValue(hechosByAnno.getCantTpub());
                } else if ((hechosByAnno.getMes() == 7)) {
                    sheet.getCell(listaLetras.get(12)).setValue(hechosByAnno.getCantPext());
                    sheet.getCell(listaLetras.get(13)).setValue(hechosByAnno.getCantTpub());
                } else if ((hechosByAnno.getMes() == 8)) {
                    sheet.getCell(listaLetras.get(14)).setValue(hechosByAnno.getCantPext());
                    sheet.getCell(listaLetras.get(15)).setValue(hechosByAnno.getCantTpub());
                } else if ((hechosByAnno.getMes() == 9)) {
                    sheet.getCell(listaLetras.get(16)).setValue(hechosByAnno.getCantPext());
                    sheet.getCell(listaLetras.get(17)).setValue(hechosByAnno.getCantTpub());
                } else if ((hechosByAnno.getMes() == 10)) {
                    sheet.getCell(listaLetras.get(18)).setValue(hechosByAnno.getCantPext());
                    sheet.getCell(listaLetras.get(19)).setValue(hechosByAnno.getCantTpub());
                } else if ((hechosByAnno.getMes() == 11)) {
                    sheet.getCell(listaLetras.get(20)).setValue(hechosByAnno.getCantPext());
                    sheet.getCell(listaLetras.get(21)).setValue(hechosByAnno.getCantTpub());
                } else if ((hechosByAnno.getMes() == 12)) {
                    sheet.getCell(listaLetras.get(22)).setValue(hechosByAnno.getCantPext());
                    sheet.getCell(listaLetras.get(23)).setValue(hechosByAnno.getCantTpub());
                }


            });

        }
        return result;
    }
}
