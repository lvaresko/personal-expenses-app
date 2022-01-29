package com.example.application.views.income;

import com.example.application.views.MainLayout;
import com.example.application.views.expenses.ExpenseItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Arrays;
import java.util.List;

@PageTitle("Income")
@Route(value = "income", layout = MainLayout.class)
public class IncomeView extends Div implements AfterNavigationObserver {

    Grid<Income> grid = new Grid<>();

    public IncomeView() {
        addClassName("income-view");
        setSizeFull();

        grid.setHeight("70%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(income -> createCard(income));;

        Dialog addDialog = new Dialog();
        addDialog.getElement().setAttribute("aria-label", "Edit income");

        VerticalLayout dialogLayout_2 = createDialogLayout_2(addDialog);
        addDialog.add(dialogLayout_2);

        Button addButton = new Button("Add", e -> addDialog.open());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(grid, addDialog, addButton);
    }

    private HorizontalLayout createCard(Income income) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.setHeight("30%");
        card.setWidth("70%");
        card.getThemeList().add("spacing-s");
        card.getElement().getStyle().set("padding", "0 0 10 0").set("margin", "0 0 0 0");
        card.setAlignItems(FlexComponent.Alignment.CENTER);

        // negdje je otiso haha ja bi da se vrati
        Span badge = new Span("");
        badge.addClassName("badge");
        badge.getElement().getStyle().set("background-color", "red");
        badge.getElement().getStyle().set("margin", "0 0 0 8");
        badge.setWidth("5px");
        badge.setHeight("100%");

        VerticalLayout incomeDetails = new VerticalLayout();
        Span incomeName = new Span(income.getIncomeName());
        incomeName.getStyle().set("font-weight", "bold");

        Span amount = new Span(String.valueOf(income.getAmount()));
        incomeDetails.add(incomeName, amount);

        incomeDetails.setSpacing(false);
        incomeDetails.setPadding(false);
        incomeDetails.setMargin(false);

        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Edit income");

        VerticalLayout dialogLayout = createDialogLayout(dialog, income.getAmount());
        dialog.add(dialogLayout);

        Button editButton = new Button(new Icon(VaadinIcon.EDIT), e -> dialog.open());
        Button trashButton = new Button(new Icon(VaadinIcon.TRASH));

        card.add(badge, incomeDetails, editButton, trashButton);
        return card;
    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        // Set some data when this view is displayed.
        List<Income> incomes = Arrays.asList( //
                createIncome("Cash", 500.0),
                createIncome("Mastercard credit card",  7000.0),
                createIncome("Dainers credit card", 400.50)
        );

        grid.setItems(incomes);
    }

    private static Income createIncome(String incomeName, Double amount) {
        Income i = new Income();
        i.setIncomeName(incomeName);
        i.setAmount(amount);

        return i;
    }

    private static VerticalLayout createDialogLayout(Dialog dialog, Double amount) {

        H2 headline = new H2("Edit income");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        NumberField incomeAmount = new NumberField("Current income");
        incomeAmount.setValue(amount);
        Div dollarPrefix = new Div();
        dollarPrefix.setText("$");
        incomeAmount.setPrefixComponent(dollarPrefix);
        incomeAmount.getStyle().set("margin", "0 0 0 0");

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        Button saveButton = new Button("Save", e -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        VerticalLayout dialogLayout = new VerticalLayout(headline, incomeAmount, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return dialogLayout;
    }

    private static VerticalLayout createDialogLayout_2(Dialog dialog) {

        H2 headline = new H2("Add a new income");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        TextField incomeName = new TextField("Income");

        NumberField amount = new NumberField("Amount");
        amount.setPrefixComponent(VaadinIcon.DOLLAR.create());

        VerticalLayout fieldLayout = new VerticalLayout(incomeName,amount);
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
