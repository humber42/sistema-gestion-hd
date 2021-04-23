package informes_generate;

import com.gembox.spreadsheet.ExcelFile;
import com.gembox.spreadsheet.ExcelWorksheet;
import com.gembox.spreadsheet.HorizontalAlignmentStyle;
import com.gembox.spreadsheet.LengthUnit;
import models.Hechos;
import util.Util;

import java.io.IOException;
import java.util.LinkedList;

public class GenerarListadosImpl implements GenerarListados {

    @Override
    public boolean generarListado(LinkedList<Hechos> hechos, String fileAddres) {
        boolean result = false;
        ExcelFile workbook = new ExcelFile();
        if (writeData(workbook, hechos)) {
            try {
                workbook.save(fileAddres);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean generarListado(LinkedList<Hechos> hechos, String fileAddres, int tipoHechos) {
        boolean result = false;
        ExcelFile workbook = new ExcelFile();
        switch (tipoHechos) {
            case 8:
                result = generarAveria(hechos, workbook);
                break;
            case 9:
                result = generarAccTransito(hechos, workbook);
                break;
            case 10:
                result = generarSegInformatica(hechos, workbook);
                break;
            case 11:
                result = generarIncendios(hechos, workbook);
                break;
            case 13:
                result = generarDiferOrigen(hechos, workbook);
                break;
            case 14:
                result = generarOtrosHechos(hechos, workbook);
                break;
        }

        if (result) {
            try {
                workbook.save(fileAddres);
            } catch (IOException e) {
                result = false;
            }
        }

        return result;
    }

    private boolean generarAveria(LinkedList<Hechos> hechos, ExcelFile workbook) {

        int listadoNumero = 0;
        int numeroHecho = 0;

        while (numeroHecho < hechos.size()) {
            ExcelWorksheet sheet = workbook.addWorksheet("Listado" + (listadoNumero == 1 ? "" : listadoNumero));
            sheet.getCell("A1").setValue("Unidad Organizativa");
            sheet.getCell("B1").setValue("Fecha Ocurrido");
            sheet.getCell("C1").setValue("Sintesis del Hecho");
            sheet.getCell("D1").setValue("Municipio");
            sheet.getCell("E1").setValue("Afectación USD");
            sheet.getCell("F1").setValue("Afectación MN");
            sheet.getCell("G1").setValue("Afectación Servicio");
            sheet.getCell("H1").setValue("CDNT");

            sheet.getCells().getSubrange("A1:H1").forEach(p -> p.setStyle(Util.generarStilo()));
            sheet.getColumn("A").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("B").setWidth(11, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("C").setWidth(41, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("D").setWidth(25, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("E").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("F").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("G").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("H").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);

            //writing data on cells
            int fila = 2;
            while (numeroHecho < hechos.size() && fila < 150) {
                sheet.getCell("A" + fila).setValue(hechos.get(numeroHecho).getUnidadOrganizativa().getUnidad_organizativa());
                sheet.getCell("B" + fila).setValue(hechos.get(numeroHecho).getFecha_ocurrencia().toString());
                sheet.getCell("C" + fila).setValue(hechos.get(numeroHecho).getTitulo() + " " + hechos.get(numeroHecho).getLugar());
                sheet.getCell("D" + fila).setValue(hechos.get(numeroHecho).getMunicipio().getMunicipio());
                sheet.getCell("E" + fila).setValue(hechos.get(numeroHecho).getAfectacion_usd());
                sheet.getCell("F" + fila).setValue(hechos.get(numeroHecho).getAfectacion_mn());
                sheet.getCell("G" + fila).setValue(hechos.get(numeroHecho).getAfectacion_servicio());
                sheet.getCell("H" + fila).setValue(hechos.get(numeroHecho).getCod_cdnt().toUpperCase());

                sheet.getCell("A" + fila).setStyle(Util.generarBordes());
                sheet.getCell("B" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.JUSTIFY);
                sheet.getCell("D" + fila).setStyle(Util.generarBordes());
                sheet.getCell("E" + fila).setStyle(Util.generarBordes());
                sheet.getCell("F" + fila).setStyle(Util.generarBordes());
                sheet.getCell("G" + fila).setStyle(Util.generarBordes());
                sheet.getCell("H" + fila).setStyle(Util.generarBordes());

                fila++;
                numeroHecho++;
            }
            listadoNumero++;
        }
        return true;
    }

    private boolean generarAccTransito(LinkedList<Hechos> hechos, ExcelFile workbook) {
        int listadoNumero = 0;
        int numeroHecho = 0;

        while (numeroHecho < hechos.size()) {
            ExcelWorksheet sheet = workbook.addWorksheet("Listado" + (listadoNumero == 1 ? "" : listadoNumero));
            sheet.getCell("A1").setValue("Unidad Organizativa");
            sheet.getCell("B1").setValue("Fecha Ocurrido");
            sheet.getCell("C1").setValue("Sintesis del Hecho");
            sheet.getCell("D1").setValue("Municipio");
            sheet.getCell("E1").setValue("Afectación USD");
            sheet.getCell("F1").setValue("Afectación MN");
            sheet.getCell("G1").setValue("Imputable");
            sheet.getCell("H1").setValue("Incidente");
            sheet.getCell("I1").setValue("CDNT");

            sheet.getCells().getSubrange("A1:I1").forEach(p -> p.setStyle(Util.generarStilo()));
            sheet.getColumn("A").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("B").setWidth(11, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("C").setWidth(41, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("D").setWidth(25, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("E").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("F").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("G").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("H").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("I").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);

            //writing data on cells
            int fila = 2;
            while (numeroHecho < hechos.size() && fila < 150) {
                sheet.getCell("A" + fila).setValue(hechos.get(numeroHecho).getUnidadOrganizativa().getUnidad_organizativa());
                sheet.getCell("B" + fila).setValue(hechos.get(numeroHecho).getFecha_ocurrencia().toString());
                sheet.getCell("C" + fila).setValue(hechos.get(numeroHecho).getTitulo() + " " + hechos.get(numeroHecho).getLugar());
                sheet.getCell("D" + fila).setValue(hechos.get(numeroHecho).getMunicipio().getMunicipio());
                sheet.getCell("E" + fila).setValue(hechos.get(numeroHecho).getAfectacion_usd());
                sheet.getCell("F" + fila).setValue(hechos.get(numeroHecho).getAfectacion_mn());
                sheet.getCell("G" + fila).setValue(hechos.get(numeroHecho).isImputable() ? "Si" : "No");
                sheet.getCell("H" + fila).setValue(hechos.get(numeroHecho).isIncidencias() ? "Si" : "No");
                sheet.getCell("I" + fila).setValue(hechos.get(numeroHecho).getCod_cdnt().toUpperCase());

                sheet.getCell("A" + fila).setStyle(Util.generarBordes());
                sheet.getCell("B" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.JUSTIFY);
                sheet.getCell("D" + fila).setStyle(Util.generarBordes());
                sheet.getCell("E" + fila).setStyle(Util.generarBordes());
                sheet.getCell("F" + fila).setStyle(Util.generarBordes());
                sheet.getCell("G" + fila).setStyle(Util.generarBordes());
                sheet.getCell("H" + fila).setStyle(Util.generarBordes());
                sheet.getCell("I" + fila).setStyle(Util.generarBordes());

                fila++;
                numeroHecho++;
            }
            listadoNumero++;
        }
        return true;
    }

    private boolean generarSegInformatica(LinkedList<Hechos> hechos, ExcelFile workbook) {
        int listadoNumero = 0;
        int numeroHecho = 0;

        while (numeroHecho < hechos.size()) {
            ExcelWorksheet sheet = workbook.addWorksheet("Listado" + (listadoNumero == 1 ? "" : listadoNumero));
            sheet.getCell("A1").setValue("Unidad Organizativa");
            sheet.getCell("B1").setValue("Fecha Ocurrido");
            sheet.getCell("C1").setValue("Sintesis del Hecho");
            sheet.getCell("D1").setValue("Municipio");
            sheet.getCell("E1").setValue("CDNT");

            sheet.getCells().getSubrange("A1:E1").forEach(p -> p.setStyle(Util.generarStilo()));
            sheet.getColumn("A").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("B").setWidth(11, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("C").setWidth(41, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("D").setWidth(25, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("E").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);

            //writing data on cells
            int fila = 2;
            while (numeroHecho < hechos.size() && fila < 150) {
                sheet.getCell("A" + fila).setValue(hechos.get(numeroHecho).getUnidadOrganizativa().getUnidad_organizativa());
                sheet.getCell("B" + fila).setValue(hechos.get(numeroHecho).getFecha_ocurrencia().toString());
                sheet.getCell("C" + fila).setValue(hechos.get(numeroHecho).getTitulo() + " " + hechos.get(numeroHecho).getLugar());
                sheet.getCell("D" + fila).setValue(hechos.get(numeroHecho).getMunicipio().getMunicipio());
                sheet.getCell("E" + fila).setValue(hechos.get(numeroHecho).getCod_cdnt().toUpperCase());

                sheet.getCell("A" + fila).setStyle(Util.generarBordes());
                sheet.getCell("B" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.JUSTIFY);
                sheet.getCell("D" + fila).setStyle(Util.generarBordes());
                sheet.getCell("E" + fila).setStyle(Util.generarBordes());

                fila++;
                numeroHecho++;
            }
            listadoNumero++;
        }
        return true;
    }

    private boolean generarIncendios(LinkedList<Hechos> hechos, ExcelFile workbook) {
        int listadoNumero = 0;
        int numeroHecho = 0;

        while (numeroHecho < hechos.size()) {
            ExcelWorksheet sheet = workbook.addWorksheet("Listado" + (listadoNumero == 1 ? "" : listadoNumero));
            sheet.getCell("A1").setValue("Unidad Organizativa");
            sheet.getCell("B1").setValue("Fecha Ocurrido");
            sheet.getCell("C1").setValue("Sintesis del Hecho");
            sheet.getCell("D1").setValue("Municipio");
            sheet.getCell("E1").setValue("Afectación USD");
            sheet.getCell("F1").setValue("Afectación MN");
            sheet.getCell("G1").setValue("Afectacion Servicio");
            sheet.getCell("H1").setValue("Tipo");
            sheet.getCell("I1").setValue("CDNT");

            sheet.getCells().getSubrange("A1:I1").forEach(p -> p.setStyle(Util.generarStilo()));
            sheet.getColumn("A").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("B").setWidth(11, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("C").setWidth(41, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("D").setWidth(25, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("E").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("F").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("G").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("H").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("I").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);

            //writing data on cells
            int fila = 2;
            while (numeroHecho < hechos.size() && fila < 150) {
                sheet.getCell("A" + fila).setValue(hechos.get(numeroHecho).getUnidadOrganizativa().getUnidad_organizativa());
                sheet.getCell("B" + fila).setValue(hechos.get(numeroHecho).getFecha_ocurrencia().toString());
                sheet.getCell("C" + fila).setValue(hechos.get(numeroHecho).getTitulo() + " " + hechos.get(numeroHecho).getLugar());
                sheet.getCell("D" + fila).setValue(hechos.get(numeroHecho).getMunicipio().getMunicipio());
                sheet.getCell("E" + fila).setValue(hechos.get(numeroHecho).getAfectacion_usd());
                sheet.getCell("F" + fila).setValue(hechos.get(numeroHecho).getAfectacion_mn());
                sheet.getCell("G" + fila).setValue(hechos.get(numeroHecho).getAfectacion_servicio());
                sheet.getCell("H" + fila).setValue(hechos.get(numeroHecho).getTipoHecho().getTipo_hecho().equalsIgnoreCase("Incendio Int.") ? "Interior" : "Exterior");
                sheet.getCell("I" + fila).setValue(hechos.get(numeroHecho).getCod_cdnt().toUpperCase());

                sheet.getCell("A" + fila).setStyle(Util.generarBordes());
                sheet.getCell("B" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.JUSTIFY);
                sheet.getCell("D" + fila).setStyle(Util.generarBordes());
                sheet.getCell("E" + fila).setStyle(Util.generarBordes());
                sheet.getCell("F" + fila).setStyle(Util.generarBordes());
                sheet.getCell("G" + fila).setStyle(Util.generarBordes());
                sheet.getCell("H" + fila).setStyle(Util.generarBordes());
                sheet.getCell("I" + fila).setStyle(Util.generarBordes());

                fila++;
                numeroHecho++;
            }
            listadoNumero++;
        }
        return true;
    }

    private boolean generarDiferOrigen(LinkedList<Hechos> hechos, ExcelFile workbook) {
        int listadoNumero = 0;
        int numeroHecho = 0;

        while (numeroHecho < hechos.size()) {
            ExcelWorksheet sheet = workbook.addWorksheet("Listado" + (listadoNumero == 1 ? "" : listadoNumero));
            sheet.getCell("A1").setValue("Unidad Organizativa");
            sheet.getCell("B1").setValue("Fecha Ocurrido");
            sheet.getCell("C1").setValue("Sintesis del Hecho");
            sheet.getCell("D1").setValue("Municipio");
            sheet.getCell("E1").setValue("Afectación USD");
            sheet.getCell("F1").setValue("Afectación MN");
            sheet.getCell("G1").setValue("CDNT");

            sheet.getCells().getSubrange("A1:G1").forEach(p -> p.setStyle(Util.generarStilo()));
            sheet.getColumn("A").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("B").setWidth(11, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("C").setWidth(41, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("D").setWidth(25, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("E").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("F").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("G").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);

            //writing data on cells
            int fila = 2;
            while (numeroHecho < hechos.size() && fila < 150) {
                sheet.getCell("A" + fila).setValue(hechos.get(numeroHecho).getUnidadOrganizativa().getUnidad_organizativa());
                sheet.getCell("B" + fila).setValue(hechos.get(numeroHecho).getFecha_ocurrencia().toString());
                sheet.getCell("C" + fila).setValue(hechos.get(numeroHecho).getTitulo() + " " + hechos.get(numeroHecho).getLugar());
                sheet.getCell("D" + fila).setValue(hechos.get(numeroHecho).getMunicipio().getMunicipio());
                sheet.getCell("E" + fila).setValue(hechos.get(numeroHecho).getAfectacion_usd());
                sheet.getCell("F" + fila).setValue(hechos.get(numeroHecho).getAfectacion_mn());
                sheet.getCell("G" + fila).setValue(hechos.get(numeroHecho).getCod_cdnt().toUpperCase());

                sheet.getCell("A" + fila).setStyle(Util.generarBordes());
                sheet.getCell("B" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.JUSTIFY);
                sheet.getCell("D" + fila).setStyle(Util.generarBordes());
                sheet.getCell("E" + fila).setStyle(Util.generarBordes());
                sheet.getCell("F" + fila).setStyle(Util.generarBordes());
                sheet.getCell("G" + fila).setStyle(Util.generarBordes());

                fila++;
                numeroHecho++;
            }
            listadoNumero++;
        }
        return true;
    }

    private boolean generarOtrosHechos(LinkedList<Hechos> hechos, ExcelFile workbook) {
        int listadoNumero = 0;
        int numeroHecho = 0;

        while (numeroHecho < hechos.size()) {
            ExcelWorksheet sheet = workbook.addWorksheet("Listado" + (listadoNumero == 1 ? "" : listadoNumero));
            sheet.getCell("A1").setValue("Unidad Organizativa");
            sheet.getCell("B1").setValue("Fecha Ocurrido");
            sheet.getCell("C1").setValue("Sintesis del Hecho");
            sheet.getCell("D1").setValue("Municipio");
            sheet.getCell("E1").setValue("CDNT");

            sheet.getCells().getSubrange("A1:E1").forEach(p -> p.setStyle(Util.generarStilo()));
            sheet.getColumn("A").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("B").setWidth(11, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("C").setWidth(41, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("D").setWidth(25, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("E").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);

            //writing data on cells
            int fila = 2;
            while (numeroHecho < hechos.size() && fila < 150) {
                sheet.getCell("A" + fila).setValue(hechos.get(numeroHecho).getUnidadOrganizativa().getUnidad_organizativa());
                sheet.getCell("B" + fila).setValue(hechos.get(numeroHecho).getFecha_ocurrencia().toString());
                sheet.getCell("C" + fila).setValue(hechos.get(numeroHecho).getTitulo() + " " + hechos.get(numeroHecho).getLugar());
                sheet.getCell("D" + fila).setValue(hechos.get(numeroHecho).getMunicipio().getMunicipio());
                sheet.getCell("E" + fila).setValue(hechos.get(numeroHecho).getCod_cdnt().toUpperCase());

                sheet.getCell("A" + fila).setStyle(Util.generarBordes());
                sheet.getCell("B" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.JUSTIFY);
                sheet.getCell("D" + fila).setStyle(Util.generarBordes());
                sheet.getCell("E" + fila).setStyle(Util.generarBordes());

                fila++;
                numeroHecho++;
            }
            listadoNumero++;
        }
        return true;
    }

    private boolean writeData(ExcelFile workbook, LinkedList<Hechos> hechos) {
        int listadoNumero = 0;
        int numeroHecho = 0;

        while (numeroHecho < hechos.size()) {
            ExcelWorksheet sheet = workbook.addWorksheet("Listado" + (listadoNumero == 1 ? "" : listadoNumero));
            sheet.getCell("A1").setValue("Unidad Organizativa");
            sheet.getCell("B1").setValue("Fecha Ocurrido");
            sheet.getCell("C1").setValue("Sintesis del Hecho");
            sheet.getCell("D1").setValue("Municipio");
            sheet.getCell("E1").setValue("No Denuncia");
            sheet.getCell("F1").setValue("Afectación USD");
            sheet.getCell("G1").setValue("Afectación MN");
            sheet.getCell("H1").setValue("Afectación Servicio");
            sheet.getCell("I1").setValue("Materiales");
            sheet.getCell("J1").setValue("Cantidad");
            sheet.getCell("K1").setValue("CDNT");

            sheet.getCells().getSubrange("A1:K1").forEach(p -> p.setStyle(Util.generarStilo()));
            sheet.getColumn("A").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("B").setWidth(11, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("C").setWidth(41, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("D").setWidth(25, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("E").setWidth(13, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("F").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("G").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("H").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("I").setWidth(23, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("J").setWidth(10, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("K").setWidth(14, LengthUnit.ZERO_CHARACTER_WIDTH);

            //writing data on cells
            int fila = 2;
            while (numeroHecho < hechos.size() && fila < 150) {
                sheet.getCell("A" + fila).setValue(hechos.get(numeroHecho).getUnidadOrganizativa().getUnidad_organizativa());
                sheet.getCell("B" + fila).setValue(hechos.get(numeroHecho).getFecha_ocurrencia().toString());
                sheet.getCell("C" + fila).setValue(hechos.get(numeroHecho).getTitulo() + " " + hechos.get(numeroHecho).getLugar());

                sheet.getCell("D" + fila).setValue(hechos.get(numeroHecho).getMunicipio().getMunicipio());
                sheet.getCell("E" + fila).setValue(hechos.get(numeroHecho).getNumero_denuncia());
                sheet.getCell("F" + fila).setValue(hechos.get(numeroHecho).getAfectacion_usd());
                sheet.getCell("G" + fila).setValue(hechos.get(numeroHecho).getAfectacion_mn());
                sheet.getCell("H" + fila).setValue(hechos.get(numeroHecho).getAfectacion_servicio());
                sheet.getCell("I" + fila).setValue(hechos.get(numeroHecho).getMateriales().getMateriales());
                sheet.getCell("J" + fila).setValue(hechos.get(numeroHecho).getCantidad());
                sheet.getCell("K" + fila).setValue(hechos.get(numeroHecho).getCod_cdnt().toUpperCase());

                sheet.getCell("A" + fila).setStyle(Util.generarBordes());
                sheet.getCell("B" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.JUSTIFY);
                sheet.getCell("D" + fila).setStyle(Util.generarBordes());
                sheet.getCell("E" + fila).setStyle(Util.generarBordes());
                sheet.getCell("F" + fila).setStyle(Util.generarBordes());
                sheet.getCell("G" + fila).setStyle(Util.generarBordes());
                sheet.getCell("H" + fila).setStyle(Util.generarBordes());
                sheet.getCell("I" + fila).setStyle(Util.generarBordes());
                sheet.getCell("J" + fila).setStyle(Util.generarBordes());
                sheet.getCell("K" + fila).setStyle(Util.generarBordes());

                fila++;
                numeroHecho++;
            }
            listadoNumero++;
        }

        return true;
    }
}
