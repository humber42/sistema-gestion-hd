package informes_generate;

import com.gembox.spreadsheet.CellRange;
import com.gembox.spreadsheet.ExcelFile;
import com.gembox.spreadsheet.ExcelWorksheet;
import models.HechosPrevenidos;
import services.ServiceLocator;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import static util.Util.generarBordes;
import static util.Util.generarStilo;

public class GenerarHechosPrevenidosImpl implements GenerarHechosPrevenidos {

    @Override
    public boolean generarHechosPrevenidos(int year, String path) {
        String DIRECCION_ARCHIVO = path+"/HechosPrevenidos.xlsx";
        boolean result = false;
        ExcelFile workbook = new ExcelFile();
        if (this.generartabla(year, workbook)) {
            try {
                workbook.save(DIRECCION_ARCHIVO);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean generartabla(int year, ExcelFile workbook) {
        ExcelWorksheet sheet = workbook.addWorksheet("Prevenidos");
        CellRange rangeA1A2 = sheet.getCells().getSubrange("A1:A2");
        rangeA1A2.setMerged(true);
        rangeA1A2.setValue("U/O");
        rangeA1A2.setStyle(generarStilo());

        CellRange rangeB1B2 = sheet.getCells().getSubrange("B1:B2");
        rangeB1B2.setMerged(true);
        rangeB1B2.setValue("Cant. Hechos Delictivos");
        rangeB1B2.setStyle(generarStilo());

        CellRange rangeC1I1 = sheet.getCells().getSubrange("C1:I1");
        rangeC1I1.setMerged(true);
        rangeC1I1.setStyle(generarStilo());
        rangeC1I1.setValue("Delitos Prevenidos");

        CellRange rangeJ1J2 = sheet.getCells().getSubrange("J1:J2");
        rangeJ1J2.setMerged(true);
        rangeJ1J2.setStyle(generarStilo());
        rangeJ1J2.setValue("Total Hechos Prevenidos");

        sheet.getCell("C2").setValue("PExt");
        sheet.getCell("D2").setValue("TPúb");
        sheet.getCell("E2").setValue("Robos");
        sheet.getCell("F2").setValue("Hurtos");
        sheet.getCell("G2").setValue("Fraudes");
        sheet.getCell("H2").setValue("Acciones C/R");
        sheet.getCell("I2").setValue("Otros Delitos");

        sheet.getCells().getSubrange("C2:I2").forEach(
                cell ->
                        cell.setStyle(generarStilo())

        );

        int cant = ServiceLocator.getUnidadOrganizativaService().countAll();
        System.out.println(cant);

        for (int i = 3; i < cant - 1; i++) {
            sheet.getCell("J" + i).setFormula("=SUM(C" + i + ":I" + i + ")");
        }

        sheet.getCells().getSubrange("A3:J" + (cant - 2)).forEach(cell -> cell.setStyle(generarBordes()));

        sheet.getCell("A" + (cant - 1)).setValue("Total");
        sheet.getCell("B" + (cant - 1)).setFormula("=SUM(B3:B" + (cant - 3) + ")");
        sheet.getCell("C" + (cant - 1)).setFormula("=SUM(C3:C" + (cant - 3) + ")");
        sheet.getCell("D" + (cant - 1)).setFormula("=SUM(D3:D" + (cant - 3) + ")");
        sheet.getCell("E" + (cant - 1)).setFormula("=SUM(E3:E" + (cant - 3) + ")");
        sheet.getCell("F" + (cant - 1)).setFormula("=SUM(F3:F" + (cant - 3) + ")");
        sheet.getCell("G" + (cant - 1)).setFormula("=SUM(G3:G" + (cant - 3) + ")");
        sheet.getCell("H" + (cant - 1)).setFormula("=SUM(H3:H" + (cant - 3) + ")");
        sheet.getCell("I" + (cant - 1)).setFormula("=SUM(I3:I" + (cant - 3) + ")");
        sheet.getCell("J" + (cant - 1)).setFormula("=SUM(J3:J" + (cant - 3) + ")");

        sheet.getCells().getSubrange("A" + (cant - 1) + ":J" + (cant - 1)).forEach(cell -> cell.setStyle(generarStilo()));

        return escribirData(
                sheet,
                preprocesarHechos(ServiceLocator.getHechosService().obtenerHechosPrevenidos(year))
        );
    }

    private boolean escribirData(ExcelWorksheet sheet, LinkedList<HechosPrevenidos> prevenidos) {
        int i = 3;
        for (HechosPrevenidos hecho : prevenidos) {
            sheet.getCell("A" + i).setValue(hecho.getUnidadOrganizativa());
            sheet.getCell("B" + i).setValue(hecho.getCantHechosDelictivos());
            sheet.getCell("C" + i).setValue(hecho.getCantPext());
            sheet.getCell("D" + i).setValue(hecho.getCantTpub());
            sheet.getCell("E" + i).setValue(hecho.getCantRobos());
            sheet.getCell("F" + i).setValue(hecho.getCantHurtos());
            sheet.getCell("G" + i).setValue(hecho.getCantFraudes());
            sheet.getCell("H" + i).setValue(hecho.getCantAccionesCR());
            sheet.getCell("I" + i).setValue(hecho.getCantOtros());
            i++;
        }
        return true;
    }


    private LinkedList<HechosPrevenidos> preprocesarHechos(LinkedList<HechosPrevenidos> hechosPrevenidos) {

        Iterator<HechosPrevenidos> iterator = hechosPrevenidos.iterator();
        HechosPrevenidos instant = new HechosPrevenidos();
        while (iterator.hasNext()) {
            HechosPrevenidos hecho = iterator.next();
            if (hecho.getUnidadOrganizativa().equalsIgnoreCase("DTNO")
                    || hecho.getUnidadOrganizativa().equalsIgnoreCase("DTES")
                    || hecho.getUnidadOrganizativa().equalsIgnoreCase("DTSR")
                    || hecho.getUnidadOrganizativa().equalsIgnoreCase("DTOE")) {

                instant.setCantAccionesCR(instant.getCantAccionesCR() + hecho.getCantAccionesCR());
                instant.setCantFraudes(instant.getCantFraudes() + hecho.getCantFraudes());
                instant.setCantHechosDelictivos(instant.getCantHechosDelictivos() + hecho.getCantHechosDelictivos());
                instant.setCantHurtos(instant.getCantHurtos() + hecho.getCantHurtos());
                instant.setCantOtros(instant.getCantOtros() + hecho.getCantOtros());
                instant.setCantPext(instant.getCantPext() + hecho.getCantPext());
                instant.setCantRobos(instant.getCantRobos() + hecho.getCantRobos());
                instant.setCantTpub(instant.getCantTpub() + hecho.getCantTpub());
                iterator.remove();
            }

            hechosPrevenidos.stream().filter(p -> p.getUnidadOrganizativa().equalsIgnoreCase("DVLH")).forEach(
                    p -> {
                        p.setCantTpub(instant.getCantTpub());
                        p.setCantRobos(instant.getCantRobos());
                        p.setCantPext(instant.getCantPext());
                        p.setCantOtros(instant.getCantOtros());
                        p.setCantHurtos(instant.getCantHurtos());
                        p.setCantHechosDelictivos(instant.getCantHechosDelictivos());
                        p.setCantFraudes(instant.getCantFraudes());
                        p.setCantAccionesCR(instant.getCantAccionesCR());
                    }
            );
        }
        return hechosPrevenidos;
    }


}
