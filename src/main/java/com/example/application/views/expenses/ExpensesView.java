package com.example.application.views.expenses;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

@PageTitle("Expenses")
@Route(value = "expenses", layout = MainLayout.class)
@Tag("expenses-view")
@JsModule("./views/expenses/expenses-view.ts")
public class ExpensesView extends LitTemplate implements HasComponents, HasStyle {

    //@Id
   // private Select<String> sortBy;

    List<ExpenseItem> expense_items = Arrays.asList(
            new ExpenseItem(1, "Groceries", "vegetables", 50.0),
            new ExpenseItem(6, "Groceries", "vegetables", 50.0),
            new ExpenseItem(2, "Shopping", "winter clothes", 400.08),
            new ExpenseItem(3, "Food and drinks", "Pizzeria Jupiter", 355.89),
            new ExpenseItem(4, "Services", "cleaning", 170.0));


    public ExpensesView() {
        // GRID
        /*
        List<Expense> expenses = Arrays.asList(
                new Expense("Groceries", 1000.08),
                new Expense("Shopping", 400.08),
                new Expense("Food and drinks", 355.89),
                new Expense("Services", 270.0));

        // Create a grid bound to the list
            Grid<Expense> grid = new Grid<>();
            grid.setAllRowsVisible(true); // dynamic height of the table
            grid.addThemeVariants(GridVariant.LUMO_NO_BORDER); // no border
            grid.setItems(expenses);
            grid.addColumn(Expense::getCategory).setHeader("Category");
            grid.addColumn(Expense::getAmount).setHeader("Amount");

            add(grid);*/

        addClassNames("expenses-view", "flex", "flex-col", "h-full");
        VerticalLayout expensesLayout = new VerticalLayout();

        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add a new expense");

        VerticalLayout dialogLayout = createDialogLayout(dialog);
        dialog.add(dialogLayout);

        Button add = new Button("Add", e -> dialog.open());
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // add to expensesLayout
        add(expensesLayout);
        expensesLayout.add(createTableLayout());
        expensesLayout.add(dialog, add);
    }

    private Component createTableLayout() {

        HorizontalLayout tableLayout = new HorizontalLayout();
        TreeGrid<ExpenseItem> treeGrid = new TreeGrid<>();

        treeGrid.setItems(expense_items); //ovdje nes ne valja zato ih ne grupira
        treeGrid.addHierarchyColumn(ExpenseItem::getCategory).setHeader("Category");
        treeGrid.addColumn(ExpenseItem::getTitle).setHeader("Title");
        treeGrid.addColumn(ExpenseItem::getAmount).setHeader("Amount $");

        treeGrid.setWidth("1000px");
        treeGrid.setHeight("350px");
        treeGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER); // no border


        tableLayout.add(treeGrid);
        return tableLayout;
    }


    private static VerticalLayout createDialogLayout(Dialog dialog) {

        H2 headline = new H2("Add a new expense");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        Select<String> selectCategory = new Select<>();
        selectCategory.setLabel("Choose category");
        selectCategory.setItems("Food and drinks", "Groceries", "Health", "Shopping", "Services", "Training", "Other");

        NumberField amount = new NumberField("Amount");
        amount.setPrefixComponent(VaadinIcon.DOLLAR.create());

        Select<String> selectPayment = new Select<>();
        selectPayment.setLabel("Payed with");
        selectPayment.setItems("Cash", "Maesto credit card", "Dainers credit card");

        VerticalLayout fieldLayout = new VerticalLayout(selectCategory,amount, selectPayment);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        Button saveButton = new Button("Add", e -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return dialogLayout;
    }

}


