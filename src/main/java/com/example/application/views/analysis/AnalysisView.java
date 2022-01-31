package com.example.application.views.analysis;

import com.example.application.data.entity.Expense;
import com.example.application.data.entity.Income;
import com.example.application.data.service.ExpenseService;
import com.example.application.data.service.IncomeService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
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
                        Collectors.summingDouble(foo->foo.getAmount())));


        for (Map.Entry<Object, Double> entry : groupedExpenses.entrySet()) {
        series.add(new DataSeriesItem((String) entry.getKey(), entry.getValue()));
        }


        conf.addSeries(series);
        add(chart);

    }
}
