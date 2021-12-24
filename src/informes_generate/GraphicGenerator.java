package informes_generate;

import com.gembox.spreadsheet.CellRange;
import com.gembox.spreadsheet.ExcelWorksheet;
import com.gembox.spreadsheet.charts.*;
import models.Afectaciones;
import services.ServiceLocator;
import util.Util;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Humberto Cabrera Dominguez
 * Class that contains methods to generate Graphic Information
 */
public class GraphicGenerator {

    public static void generarGraficoBarrasDobles(String title, String position1, String position2, ExcelWorksheet sheet, LocalDate date, CellRange rango1, CellRange rango2, CellRange categorias, int... cant) {
//        int cantAnnos = 2;
//        int cantUnidadesOrganizativas = 16;

        //Create chart and select data for it
        ColumnChart chart = (ColumnChart) sheet.getCharts().add(ChartType.COLUMN, position1, position2);

        //Set Chart title
        chart.getTitle().setText(title);

        //Set Axis titles
        chart.getAxes().getVertical().setVisible(false);
        chart.getAxes().getHorizontal().getTitle().setText("Unidades organizativas");

        //For all charts except (Pie and Bar ) value axis is vertical
        ValueAxis valueAxis = chart.getAxes().getVerticalValue();

        //Adding data
        chart.getSeries().add(
                String.valueOf(date.minusYears(1).getYear()) + ": " + cant[0] + " hechos",
                Util.formatedLetter(rango1));
        chart.getSeries().add(
                String.valueOf(date.getYear()) + ": " + cant[1] + " hechos",
                Util.formatedLetter(rango2)
        );

        chart.setCategoryLabelsReference(Util.formatedLetter(categorias));
        chart.getDataLabels().setLabelContainsValue(true);

        //Set value axis scaling, unit, gridlines and tick marks
        valueAxis.getMajorGridlines().setVisible(true);
        valueAxis.getMinorGridlines().setVisible(false);
//        valueAxis.setMajorTickMarkType(TickMarkType.OUTSIDE);
//        valueAxis.setMinorTickMarkType(TickMarkType.CROSS);
        chart.getLegend().setPosition(ChartLegendPosition.BOTTOM);
    }

    public static void generarGraficoBarrasDoblesParaRobosHurtos(String title, String position1, String position2, ExcelWorksheet sheet, LocalDate date, CellRange rango1, CellRange rango2, CellRange rango3, CellRange categorias) {
//        int cantAnnos = 2;
//        int cantUnidadesOrganizativas = 16;

        //Create chart and select data for it
        ColumnChart chart = (ColumnChart) sheet.getCharts().add(ChartType.COLUMN, position1, position2);

        //Set Chart title
        chart.getTitle().setText(title);

        //Set Axis titles
        chart.getAxes().getVertical().setVisible(false);
        chart.getAxes().getHorizontal().getTitle().setText("Unidades organizativas");

        //For all charts except (Pie and Bar ) value axis is vertical
        ValueAxis valueAxis = chart.getAxes().getVerticalValue();

        //Adding data
        chart.getSeries().add(
                "Robos",
                Util.formatedLetter(rango1));
        chart.getSeries().add(
                "Hurtos",
                Util.formatedLetter(rango2)
        );
        chart.getSeries().add(
                "Intentos de robo",
                Util.formatedLetter(rango3)
        );

        chart.setCategoryLabelsReference(Util.formatedLetter(categorias));
        chart.getDataLabels().setLabelContainsValue(true);

        //Set value axis scaling, unit, gridlines and tick marks
        valueAxis.getMajorGridlines().setVisible(true);
        valueAxis.getMinorGridlines().setVisible(false);
//        valueAxis.setMajorTickMarkType(TickMarkType.OUTSIDE);
//        valueAxis.setMinorTickMarkType(TickMarkType.CROSS);
        chart.getLegend().setPosition(ChartLegendPosition.BOTTOM);
    }

    public static void generarGraficoBarrasNormal(String title, String position1, String position2, ExcelWorksheet sheet, LocalDate date, CellRange rango1) {

        //Create chart and select data for it
        ColumnChart chart = (ColumnChart) sheet.getCharts().add(ChartType.COLUMN, position1, position2);
        //Set Chart title
        chart.getTitle().setText(title);
        //Set Axis titles
        chart.getAxes().getVertical().setVisible(false);

        //For all charts except (Pie and Bar ) value axis is vertical
        ValueAxis valueAxis = chart.getAxes().getVerticalValue();
        //Adding data
        chart.selectData(rango1, true);

        //Set value axis scaling, unit, gridlines and tick marks
        valueAxis.getMajorGridlines().setVisible(true);
        valueAxis.getMinorGridlines().setVisible(false);
//        valueAxis.setMajorTickMarkType(TickMarkType.OUTSIDE);
//        valueAxis.setMinorTickMarkType(TickMarkType.CROSS);
        chart.getLegend().setPosition(ChartLegendPosition.TOP_RIGHT);
        chart.getLegend().setAllowOverlap(true);
        double epInstaladas = 0;
        double serviciosAfectados = 0;
        List<Afectaciones> afectacionesList = ServiceLocator.getHechosService().calculoServiciosAfectadosEstacionesPublicas(Date.valueOf(date));
        for (Afectaciones afectaciones : afectacionesList) {
            epInstaladas += afectaciones.getCantEstacionesPublicas();
            serviciosAfectados += afectaciones.getAfectaciones();
        }
        double calculate = (100 * serviciosAfectados / epInstaladas);

        chart.getSeries().get(0).setName(String.valueOf(calculate).substring(0, 4) + "% Servicios vs Técnica");
        chart.getDataLabels().setLabelContainsValue(true);
        chart.getTitle().setVisible(false);
    }

    public static boolean generarGraficoBarras(CellRange range, ExcelWorksheet worksheet,
                                               String positionInit, String positionFinal, String title, boolean fiscalia
    ) {
        BarChart chart = (BarChart) worksheet.getCharts().add(ChartType.BAR, positionInit, positionFinal);
        chart.selectData(range, true);
        chart.getTitle().setVisible(false);
        chart.getLegend().setPosition(ChartLegendPosition.BOTTOM);
        chart.getAxes().getHorizontal().setVisible(false);
        chart.getAxes().getVertical().setReverseOrder(true);
        chart.getDataLabels().setLabelContainsValue(true);
        if (fiscalia) {
            chart.getTitle().setVisible(false);
        } else {
            chart.getTitle().setText(title);
        }

        return true;
    }


    public static boolean generarGraficoPastel(CellRange range, ExcelWorksheet worksheet,
                                               String positionInit, String positionFinal,
                                               String title, int serieEliminar) {
        ExcelChart chart = worksheet.getCharts().add(ChartType.PIE, positionInit, positionFinal);
        chart.selectData(range, true);
        chart.getTitle().setText(title);
        chart.getSeries().get(serieEliminar).delete();
        chart.getDataLabels().setSeparator("-");
        chart.getLegend().setVisible(false);
        chart.getDataLabels().setLabelContainsCategoryName(true);
        chart.getDataLabels().setShowLeaderLines(true);
        chart.getDataLabels().show();
        return true;
    }


    public static boolean generarGraficoLineas(CellRange range, ExcelWorksheet worksheet,
                                               String positionInit, String positionFinal, String title, boolean fiscalia) {
        LineChart chart = (LineChart) worksheet.getCharts().add(ChartType.LINE, positionInit, positionFinal);
        //'Tablas Graficos'!$A$25:$A$43
        //'Tablas Graficos'!$A$25:$A$43'
        chart.getAxes().getVertical().setVisible(false);
        String reference = Util.formatedLetter(range);
        chart.getSeries().add("Cant-Hechos", reference);
        chart.getDataLabels().setLabelContainsValue(true);
        chart.getDataLabels().setLabelPosition(DataLabelPosition.TOP);
        if (fiscalia) {
            chart.setCategoryLabelsReference("Resumen!$A$80:$B$103");
            chart.getTitle().setVisible(false);
        } else {
            chart.setCategoryLabelsReference("Historico!$B$42:$C$65");
            chart.getTitle().setText(title);

        }
        chart.getLegend().setPosition(ChartLegendPosition.BOTTOM);

        return true;

    }

}
