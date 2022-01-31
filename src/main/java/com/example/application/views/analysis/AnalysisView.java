package com.example.application.views.analysis;

import com.example.application.data.entity.Expense;
import com.example.application.data.service.ExpenseService;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@PageTitle("Analysis")

public class AnalysisView extends HorizontalLayout {

    public AnalysisView() throws ExecutionException, InterruptedException {
        ExpenseService incomeService = new ExpenseService();
        var userEmail = VaadinSession.getCurrent().getSession().getAttribute("email").toString();

        addClassName("analysis-view");

        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();

        DataSeries series = new DataSeries();

        List<Expense> expenses = incomeService.getExpenses(userEmail);
        Map<Object, Double> groupedExpenses = expenses.stream()
                .collect(Collectors.groupingBy(foo -> foo.getCategory(),
                        Collectors.summingDouble(foo -> foo.getAmount())));


        for (Map.Entry<Object, Double> entry : groupedExpenses.entrySet()) {
            series.add(new DataSeriesItem((String) entry.getKey(), entry.getValue()));
        }

        H2 headline = new H2("Analysis on total spending by Category");
        headline.getStyle().set("margin-top", "0").set("padding", "20px 0 0 20px");

        conf.addSeries(series);
        add(headline, chart);

    }
}
