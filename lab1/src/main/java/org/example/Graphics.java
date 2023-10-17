package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Graphics {
    private class FunctionPlotter extends JFrame {
        public FunctionPlotter(Params params) {
            super("plot");
            XYSeries series = new XYSeries("func");
            for (double x = -100; x <= 100; x += 0.1) {
                double y = Math.pow(x, 3) + params.getA() * Math.pow(x, 2) + params.getB() * x + params.getC();
                series.add(x, y);
            }
            XYSeriesCollection dataset = new XYSeriesCollection(series);
            JFreeChart chart = ChartFactory.createXYLineChart(
                    null,
                    "x",
                    "y",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );
            chart.setBackgroundPaint(Color.white);
            XYPlot plot = chart.getXYPlot();
            plot.setDomainPannable(true);
            plot.setRangePannable(true);
            plot.setDomainZeroBaselineVisible(true);
            plot.setRangeZeroBaselineVisible(true);

            // Настройка рендерера
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
            plot.setRenderer(renderer);

            // Создаем панель для отображения графика
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 600));
            setContentPane(chartPanel);        }
    }
    private final Params params;
    public Graphics(Params params) {
        this.params = params;
    }
    public void draw() {
        SwingUtilities.invokeLater(() -> {
            FunctionPlotter plotter = new FunctionPlotter(params);
            plotter.pack();
            plotter.setLocationRelativeTo(null);
            plotter.setVisible(true);
        });
//        try {
//            Runtime.getRuntime().exec(String.format("python3 draw.py %f %f %f", params.getA(), params.getB(), params.getC()));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
