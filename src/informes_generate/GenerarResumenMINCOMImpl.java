package informes_generate;

import com.gembox.spreadsheet.CellRange;
import com.gembox.spreadsheet.ColorName;
import com.gembox.spreadsheet.ExcelCell;
import com.gembox.spreadsheet.ExcelFile;
import com.gembox.spreadsheet.ExcelWorksheet;
import com.gembox.spreadsheet.HorizontalAlignmentStyle;
import com.gembox.spreadsheet.LengthUnit;
import com.gembox.spreadsheet.LineStyle;
import com.gembox.spreadsheet.MultipleBorders;
import com.gembox.spreadsheet.SpreadsheetColor;
import models.Hechos;
import util.Util;

import java.awt.*;
import java.io.IOException;
import java.util.EnumSet;
import java.util.LinkedList;

public class GenerarResumenMINCOMImpl implements GenerarResumenMINCOM {

    @Override
    public boolean generarResumenMINCOM(LinkedList<Hechos> hechos, String path) {
        String DIRECCION_ARCHIVO = path+"/ResumenMINCOM.xlsx";
        boolean result = false;
        ExcelFile workbook = new ExcelFile();
        if (generarResumen(workbook, hechos) && generarHechos(hechos, workbook)) {
            try {
                workbook.save(DIRECCION_ARCHIVO);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean generarResumen(ExcelFile workbook, LinkedList<Hechos> hechos) {
        ExcelWorksheet sheet = workbook.addWorksheet("Resumen");
        sheet.getCell("A1").setValue("Tipo");
        sheet.getCell("B1").setValue("Cantidad");
        sheet.getCell("C1").setValue("Perdidas (CUC)");
        sheet.getCell("D1").setValue("Perdidas (MN)");
        sheet.getCells().getSubrange("A1:D1").forEach(
                p -> p.setStyle(Util.generarStilo())
        );

        CellRange rangeA2D2 = sheet.getCells().getSubrange("A2:D2");
        rangeA2D2.setValue("Delictivos");
        rangeA2D2.setMerged(true);
        rangeA2D2.setStyle(Util.generarStilo());
        rangeA2D2.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromName(ColorName.DARK_RED));

        sheet.getCells().getSubrange("A3:D8").forEach(p -> p.setStyle(Util.generarBordes()));
        sheet.getCell("A3").setValue("Delito vs Pext");
        sheet.getCell("A4").setValue("Delito vs TPúb");
        sheet.getCell("A5").setValue("Robos");
        sheet.getCell("A6").setValue("Hurtos");
        sheet.getCell("A7").setValue("Fraudes");
        sheet.getCell("A8").setValue("Otros delitos");
        sheet.getCells().getSubrange("A9:D9").forEach(
                cell -> {
                    cell.setStyle(Util.generarBordes());
                    cell.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromColor(Color.pink));
                }
        );

        sheet.getColumn("A").setWidth(30, LengthUnit.ZERO_CHARACTER_WIDTH);
        ExcelCell cellA9 = sheet.getCell("A9");
        cellA9.setValue("Subtotal Delictivos:");
        cellA9.getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);

        CellRange rangeA10D10 = sheet.getCells().getSubrange("A10:D10");
        rangeA10D10.setValue("No delictivos");
        rangeA10D10.setMerged(true);
        rangeA10D10.setStyle(Util.generarStilo());
        rangeA10D10.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromName(ColorName.DARK_RED));

        sheet.getCells().getSubrange("A11:D17").forEach(p -> p.setStyle(Util.generarBordes()));

        sheet.getCell("A11").setValue("Averías de PExt");
        sheet.getCell("A12").setValue("Accidentes de Tránsito");
        sheet.getCell("A13").setValue("Seguridad Informática");
        sheet.getCell("A14").setValue("Incendios Interiores");
        sheet.getCell("A15").setValue("Incendios Exteriores");
        sheet.getCell("A16").setValue("Diferencias de Origen");
        sheet.getCell("A17").setValue("Otros Hechos");

        sheet.getCells().getSubrange("A18:D18").forEach(
                cell -> {
                    cell.setStyle(Util.generarBordes());
                    cell.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromColor(Color.pink));
                }
        );

        ExcelCell cellA18 = sheet.getCell("A18");
        cellA18.setValue("Subtotal No Delictivos:");
        cellA18.getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);


        sheet.getCells().getSubrange("A19:D19").forEach(cell -> {
            cell.setStyle(Util.generarBordes());
            cell.getStyle().getFillPattern().setSolid(SpreadsheetColor.fromName(ColorName.DARK_BLUE));
            cell.getStyle().getFont().setColor(SpreadsheetColor.fromName(ColorName.WHITE));
            cell.getStyle().getBorders().setBorders(EnumSet.of(MultipleBorders.LEFT)
                    , SpreadsheetColor.fromColor(Color.WHITE), LineStyle.THIN);
        });

        ExcelCell cellA19 = sheet.getCell("A19");
        cellA19.getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.RIGHT);
        cellA19.setValue("Total");

        sheet.getCell("B9").setFormula("=SUM(B3:B8)");
        sheet.getCell("C9").setFormula("=SUM(C3:C8)");
        sheet.getCell("D9").setFormula("=SUM(D3:D8)");
        sheet.getCell("B18").setFormula("=SUM(B11:B17)");
        sheet.getCell("C18").setFormula("=SUM(C11:C17)");
        sheet.getCell("D18").setFormula("=SUM(D11:D17)");

        sheet.getCell("B19").setFormula("=B9+B18");
        sheet.getCell("C19").setFormula("=C9+C18");
        sheet.getCell("D19").setFormula("=D9+D18");

        LinkedList<Cantidades> cantidades = this.calculateData(hechos);
        int valorArray = 0;
        for (int i = 3; i < 18; i++) {
            if (i != 9 && i != 10) {
                sheet.getCell("B" + i).setValue(cantidades.get(valorArray).getCantidad());
                sheet.getCell("C" + i).setValue(cantidades.get(valorArray).getPerdidaMLC());
                sheet.getCell("D" + i).setValue(cantidades.get(valorArray).getPerdidaMN());
                valorArray++;
            }
        }


        return true;
    }

    private boolean generarHechos(LinkedList<Hechos> hechos, ExcelFile workbook) {
        int listadoNumero = 1;

        int numeroHecho = 0;
        while (numeroHecho < hechos.size()) {

            ExcelWorksheet sheet = workbook.addWorksheet("Listado" + (listadoNumero == 1 ? "" : listadoNumero));

            sheet.getCell("A1").setValue("Tipo");
            sheet.getCell("B1").setValue("Fecha Ocurrido");
            sheet.getCell("C1").setValue("U/O");
            sheet.getCell("D1").setValue("Sintesis del Hecho");
            sheet.getCell("E1").setValue("Pérdidas (CUC)");
            sheet.getCell("F1").setValue("Pérdidas (MN)");

            sheet.getCells().getSubrange("A1:F1").forEach(p -> p.setStyle(Util.generarStilo()));
            sheet.getColumn("A").setWidth(20.89, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("B").setWidth(11.89, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("C").setWidth(7, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("D").setWidth(76.89, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("E").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);
            sheet.getColumn("F").setWidth(12, LengthUnit.ZERO_CHARACTER_WIDTH);

            //writing data on cells
            int fila = 2;
            while (numeroHecho < hechos.size() && fila < 150) {
                sheet.getCell("A" + fila).setValue(hechos.get(numeroHecho).getTipoHecho().getTipo_hecho());
                sheet.getCell("B" + fila).setValue(hechos.get(numeroHecho).getFecha_ocurrencia().toString());
                sheet.getCell("C" + fila).setValue(hechos.get(numeroHecho).getUnidadOrganizativa().getUnidad_organizativa());
                sheet.getCell("D" + fila).setValue(hechos.get(numeroHecho).getTitulo() + ". " + hechos.get(numeroHecho).getLugar());
                sheet.getCell("E" + fila).setValue(hechos.get(numeroHecho).getAfectacion_usd());
                sheet.getCell("F" + fila).setValue(hechos.get(numeroHecho).getAfectacion_mn());

                sheet.getCell("A" + fila).setStyle(Util.generarBordes());
                sheet.getCell("B" + fila).setStyle(Util.generarBordes());
                sheet.getCell("C" + fila).setStyle(Util.generarBordes());
                sheet.getCell("D" + fila).setStyle(Util.generarBordes());
                sheet.getCell("D" + fila).getStyle().setHorizontalAlignment(HorizontalAlignmentStyle.JUSTIFY);
                sheet.getCell("E" + fila).setStyle(Util.generarBordes());
                sheet.getCell("F" + fila).setStyle(Util.generarBordes());
                fila++;
                numeroHecho++;
            }
            listadoNumero++;

        }
        return true;
    }

    private LinkedList<Cantidades> calculateData(LinkedList<Hechos> hechos) {

        Cantidades delitoVsPext = new Cantidades();
        Cantidades delitoTpub = new Cantidades();
        Cantidades delitoRobo = new Cantidades();
        Cantidades delitoHurto = new Cantidades();
        Cantidades delitoFraude = new Cantidades();
        Cantidades delitoOtros = new Cantidades();
        Cantidades averiaPext = new Cantidades();
        Cantidades accTransito = new Cantidades();
        Cantidades segInformatica = new Cantidades();
        Cantidades incendioInt = new Cantidades();
        Cantidades incendioExt = new Cantidades();
        Cantidades diferOrig = new Cantidades();
        Cantidades otrosHechos = new Cantidades();

        for (Hechos hecho : hechos
        ) {
            if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Delito vs PExt")) {
                delitoVsPext.setCantidad(delitoVsPext.getCantidad() + 1);
                delitoVsPext.setPerdidaMN(delitoVsPext.getPerdidaMN() + hecho.getAfectacion_mn());
                delitoVsPext.setPerdidaMLC(delitoVsPext.getPerdidaMLC() + hecho.getAfectacion_usd());
            } else if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Delito vs TPúb")) {
                delitoTpub.setCantidad(delitoTpub.getCantidad() + 1);
                delitoTpub.setPerdidaMLC(delitoTpub.getPerdidaMLC() + hecho.getAfectacion_usd());
                delitoTpub.setPerdidaMN(delitoTpub.getPerdidaMN() + hecho.getAfectacion_mn());
            } else if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Robo")) {
                delitoRobo.setCantidad(delitoRobo.getCantidad() + 1);
                delitoRobo.setPerdidaMLC(delitoRobo.getPerdidaMLC() + hecho.getAfectacion_usd());
                delitoRobo.setPerdidaMN(delitoRobo.getPerdidaMN() + hecho.getAfectacion_mn());
            } else if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Hurto")) {
                delitoHurto.setCantidad(delitoHurto.getCantidad() + 1);
                delitoHurto.setPerdidaMN(delitoHurto.getPerdidaMN() + hecho.getAfectacion_mn());
                delitoHurto.setPerdidaMLC(delitoHurto.getPerdidaMLC() + hecho.getAfectacion_usd());
            } else if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Fraude")) {
                delitoFraude.setCantidad(delitoFraude.getCantidad() + 1);
                delitoFraude.setPerdidaMLC(delitoFraude.getPerdidaMLC() + hecho.getAfectacion_usd());
                delitoFraude.setPerdidaMN(delitoFraude.getPerdidaMN() + hecho.getAfectacion_mn());
            } else if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Otros Delitos")) {
                delitoOtros.setCantidad(delitoOtros.getCantidad() + 1);
                delitoOtros.setPerdidaMLC(delitoOtros.getPerdidaMLC() + hecho.getAfectacion_usd());
                delitoOtros.setPerdidaMN(delitoOtros.getPerdidaMN() + hecho.getAfectacion_mn());
            } else if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Avería PExt")) {
                averiaPext.setCantidad(averiaPext.getCantidad() + 1);
                averiaPext.setPerdidaMLC(averiaPext.getPerdidaMLC() + hecho.getAfectacion_usd());
                averiaPext.setPerdidaMN(averiaPext.getPerdidaMN() + hecho.getAfectacion_mn());
            } else if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Acc. Tránsito")) {
                accTransito.setCantidad(accTransito.getCantidad() + 1);
                accTransito.setPerdidaMLC(accTransito.getPerdidaMLC() + hecho.getAfectacion_usd());
                accTransito.setPerdidaMN(accTransito.getPerdidaMN() + hecho.getAfectacion_mn());
            } else if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Seg. Informática")) {
                segInformatica.setCantidad(segInformatica.getCantidad() + 1);
                segInformatica.setPerdidaMN(segInformatica.getPerdidaMN() + hecho.getAfectacion_mn());
                segInformatica.setPerdidaMLC(segInformatica.getPerdidaMLC() + hecho.getAfectacion_usd());
            } else if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Incendio Int.")) {
                incendioInt.setCantidad(incendioInt.getCantidad() + 1);
                incendioInt.setPerdidaMLC(incendioInt.getPerdidaMLC() + hecho.getAfectacion_usd());
                incendioInt.setPerdidaMN(incendioInt.getPerdidaMN() + hecho.getAfectacion_mn());
            } else if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Incendio Ext.")) {
                incendioExt.setCantidad(incendioExt.getCantidad() + 1);
                incendioExt.setPerdidaMLC(incendioExt.getPerdidaMLC() + hecho.getAfectacion_usd());
                incendioExt.setPerdidaMN(incendioExt.getPerdidaMN() + hecho.getAfectacion_mn());
            } else if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Difer. Origen")) {
                diferOrig.setCantidad(diferOrig.getCantidad() + 1);
                diferOrig.setPerdidaMLC(diferOrig.getPerdidaMLC() + hecho.getAfectacion_usd());
                diferOrig.setPerdidaMN(diferOrig.getPerdidaMN() + hecho.getAfectacion_mn());
            } else if (hecho.getTipoHecho().getTipo_hecho().equalsIgnoreCase("Otros Hechos")) {
                otrosHechos.setCantidad(otrosHechos.getCantidad() + 1);
                otrosHechos.setPerdidaMLC(otrosHechos.getPerdidaMLC() + hecho.getAfectacion_usd());
                otrosHechos.setPerdidaMN(otrosHechos.getPerdidaMN() + hecho.getAfectacion_mn());
            }
        }

        LinkedList<Cantidades> cantidades = new LinkedList<>();
        cantidades.add(delitoVsPext);
        cantidades.add(delitoTpub);
        cantidades.add(delitoRobo);
        cantidades.add(delitoHurto);
        cantidades.add(delitoFraude);
        cantidades.add(delitoOtros);
        cantidades.add(averiaPext);
        cantidades.add(accTransito);
        cantidades.add(segInformatica);
        cantidades.add(incendioInt);
        cantidades.add(incendioExt);
        cantidades.add(diferOrig);
        cantidades.add(otrosHechos);
        cantidades.forEach(e -> {
            System.out.println(e);
        });
        return cantidades;
    }

    private class Cantidades {
        private int cantidad;
        private double perdidaMN;
        private double perdidaMLC;

        public Cantidades() {
            cantidad = 0;
            perdidaMN = 0;
            perdidaMLC = 0;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public double getPerdidaMN() {
            return perdidaMN;
        }

        public void setPerdidaMN(double perdidaMN) {
            this.perdidaMN = perdidaMN;
        }

        public double getPerdidaMLC() {
            return perdidaMLC;
        }

        public void setPerdidaMLC(double perdidaMLC) {
            this.perdidaMLC = perdidaMLC;
        }

        @Override
        public String toString() {
            return "Cantidades{" +
                    "cantidad=" + cantidad +
                    ", perdidaMN=" + perdidaMN +
                    ", perdidaMLC=" + perdidaMLC +
                    '}';
        }
    }
}
