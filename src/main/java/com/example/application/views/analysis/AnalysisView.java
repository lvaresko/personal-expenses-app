package com.example.application.views.analysis;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Analysis")
@Route(value = "analysis", layout = MainLayout.class)
public class AnalysisView extends HorizontalLayout {

    public AnalysisView() {
        addClassName("analysis-view");

        RangeSeries series =
                new RangeSeries("Temperature Ranges",
                        new Double[]{-51.5,10.9},
                        new Double[]{-49.0,11.8},
                        new Double[]{-47.0,10.8});

        // RANDOM CHART
        Chart chart = new Chart(ChartType.COLUMN);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Reindeer Kills by Predators");
        conf.setSubTitle("Kills Grouped by Counties");
        conf.addSeries(series);
        add(chart);

    }
}
