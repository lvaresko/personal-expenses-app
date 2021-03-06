package com.example.application.views.expenses;

import com.example.application.data.entity.Expense;
import com.example.application.data.entity.Income;
import com.example.application.data.service.ExpenseService;
import com.example.application.data.service.IncomeService;
import com.example.application.views.MainLayout;
import com.google.cloud.Timestamp;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@PageTitle("Expenses")
@Route(value = "expenses", layout= MainLayout.class)
public class ExpensesView extends Div implements AfterNavigationObserver {

    public ExpensesView() {
        addClassNames("expenses-view", "flex", "flex-col", "h-full");
        VerticalLayout expensesLayout = new VerticalLayout();
        expensesLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add a new expense");

        VerticalLayout dialogLayout = createDialogLayout(dialog);
        dialog.add(dialogLayout);

        Button add = new Button("Add", event -> dialog.open());
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // add to expensesLayout
        add(expensesLayout);
        expensesLayout.add(dialog, add);
        expensesLayout.add(createTableLayout());

    }

    private static VerticalLayout createDialogLayout(Dialog dialog) {
        IncomeService incomeService = new IncomeService();
        ExpenseService expenseService = new ExpenseService();
        List<String> incomes_list = new ArrayList<>();

        try {
            String userEmail = VaadinSession.getCurrent().getSession().getAttribute("email").toString();
            List<Income> incomes = incomeService.getIncome(userEmail);
            for (Income income : incomes) {
                incomes_list.add(income.getName());
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        H2 headline = new H2("Add a new expense");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0").set("font-size", "1.5em").set("font-weight", "bold");

        Select<String> selectCategory = new Select<>();
        selectCategory.setLabel("Choose category");
        selectCategory.setItems("Food and drinks", "Groceries", "Health", "Shopping", "Services", "Training", "Other");

        TextField title = new TextField("Title");

        NumberField amount = new NumberField("Amount");
        amount.setPrefixComponent(VaadinIcon.DOLLAR.create());

        Select<String> selectPayment = new Select<>();
        selectPayment.setLabel("Payed with");
        selectPayment.setItems(incomes_list);

        VerticalLayout fieldLayout = new VerticalLayout(selectCategory, title, amount, selectPayment);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        Button cancelButton = new Button("Cancel", event -> {
            selectCategory.clear();
            title.clear();
            amount.clear();
            selectPayment.clear();
            dialog.close();
        });

        Button saveButton = new Button("Add", event -> {
            try {
                String userEmail = VaadinSession.getCurrent().getSession().getAttribute("email").toString();
                Timestamp dateNow;
                dateNow = Timestamp.now();
                expenseService.saveExpense(userEmail, selectCategory.getValue(), title.getValue(), amount.getValue(), selectPayment.getValue(), dateNow);
            } catch (ExecutionException | InterruptedException | NullPointerException e) {
                e.printStackTrace();
            }
            dialog.close();
            UI.getCurrent().getPage().reload();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return dialogLayout;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

    }

    private Component createTableLayout() {
        List<Expense> expenses = new ArrayList<>();
        ExpenseService expenseService = new ExpenseService();

        try {
            String userEmail = VaadinSession.getCurrent().getSession().getAttribute("email").toString();
            expenses = expenseService.getExpenses(userEmail);

        } catch (ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }


        HorizontalLayout tableLayout = new HorizontalLayout();
        TreeGrid<Expense> treeGrid = new TreeGrid<>();

        treeGrid.setItems(expenses);
        treeGrid.addColumn(Expense::getCategory).setHeader("Category");
        treeGrid.addColumn(Expense::getTitle).setHeader("Title");
        treeGrid.addColumn(Expense::getAmount).setHeader("Amount");
        treeGrid.addHierarchyColumn(Expense::getDateString).setHeader("Date");
        treeGrid.setWidth("1000px");
        treeGrid.setHeight("500px");
        treeGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER); // no border

        tableLayout.add(treeGrid);
        return tableLayout;
    }

}


