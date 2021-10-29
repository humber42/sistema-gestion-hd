package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;

import java.util.HashMap;
import java.util.Map;

public class PieChartUtils {
    private PieChart pieChart;
    private ObservableList<PieChart.Data> pieChartData;

    public PieChartUtils(PieChart pieChart)
    {
        pieChartData = FXCollections.observableArrayList();
        this.pieChart = pieChart;
    }
    public void addData(String name, double value)
    {
        pieChartData.add(new PieChart.Data(name, value));
    }
    public void showChart()
    {
        pieChart.setData(pieChartData);
    }
    public void setDataColor(int index, String color)
    {
        pieChartData.get(index).getNode().setStyle("-fx-background-color: "+ color);
    }
    public void setMarkVisible(boolean b)
    {
        pieChart.setLabelsVisible(b);
    }
    public void setLegendColor(HashMap<Integer, String> colors)
    {
        String setColor = "";
        for(Map.Entry<Integer, String> entry:colors.entrySet())
        {
            int index = entry.getKey();
            String color = entry.getValue();
            setColor = setColor.concat("CHART_COLOR_" + (index+1) + ":" + color+";");
        }
        pieChart.setStyle(setColor);
    }
    public void setLegendSide(String side)
    {
        if(side.equalsIgnoreCase("top"))
            pieChart.setLegendSide(Side.TOP);
        else if(side.equalsIgnoreCase("bottom"))
            pieChart.setLegendSide(Side.BOTTOM);
        else if(side.equalsIgnoreCase("left"))
            pieChart.setLegendSide(Side.LEFT);
        else{
            pieChart.setLegendSide(Side.RIGHT);
        }
    }
    public void setTitle(String title)
    {
        pieChart.setTitle(title);
    }
   /* public void operatePieChart()
    {
        PieChartUtils pieChartUtils = new PieChartUtils(pieChart);
        pieChartUtils.addData("R", 0.5);
        pieChartUtils.addData("G", 0.3);
        pieChartUtils.addData("B", 0.2);
        // Mostrar gráfico
        pieChartUtils.showChart();
        // El color del área de datos debe modificarse después de que se muestre el icono
        pieChartUtils.setDataColor(0, "red");
        pieChartUtils.setDataColor(1, "green");
        pieChartUtils.setDataColor(2, "blue");
        // Desmarcar
        pieChartUtils.setMarkVisible(false);
        // Establecer el color de la serie de datos del gráfico de abanico
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(0, "red");
        hashMap.put(1, "green");
        hashMap.put(2, "blue");
        // Establecer el color y la orientación de la leyenda
        pieChartUtils.setLegendColor(hashMap);
        pieChartUtils.setLegendSide("Bottom");
        // Establecer el título del gráfico
        //pieChartUtils.setTitle("Comportamiento de los hechos en " + LocalDate.now().getYear());
    }*/
}
