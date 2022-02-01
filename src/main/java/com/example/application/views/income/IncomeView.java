package com.example.application.views.income;

import com.example.application.data.entity.Income;
import com.example.application.data.service.FirebaseInitialize;
import com.example.application.data.service.IncomeService;
import com.example.application.views.MainLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@PageTitle("Income")
@Route(value = "income", layout = MainLayout.class)

public class IncomeView extends Div implements AfterNavigationObserver {

    Grid<Income> grid = new Grid<>();
    Double total_income = 0.0;

    public IncomeView() {

        System.out.println(FirebaseApp.getApps());
        if (!FirebaseApp.getApps().isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }



        addClassName("income-view");

        H2 headline = new H2("Total income: " + getTotal_income() + " $");
        headline.getStyle().set("margin-top", "0").set("padding", "20px 0 0 20px");

        if (total_income < 0) {
            headline.getElement().getStyle().set("color", "red");
        } else {
            headline.getElement().getStyle().set("color", "green");
        }

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(income -> createCard(income));
        grid.setAllRowsVisible(true);

        Dialog dialog1 = new Dialog();
        dialog1.getElement().setAttribute("aria-label", "Edit income");

        VerticalLayout dialogLayout_2 = createDialog2(dialog1);
        dialog1.add(dialogLayout_2);

        Button addButton = new Button("Add", event -> dialog1.open());
        addButton.addClassName("income_add_btn");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(headline, grid, dialog1, addButton);
    }

    // -- EDIT INCOME DIALOG --
    private static VerticalLayout createDialog1(Dialog dialog, Double amount, String id, String incomeName) {
        IncomeService incomeService = new IncomeService();

        H2 headline = new H2("Edit income");
        headline.addClassName("dialog_header");
        headline.getStyle().set("text-align", "center");

        NumberField incomeAmount = new NumberField("Current income");
        incomeAmount.setValue(amount);
        Div dollarPrefix = new Div();
        dollarPrefix.setText("$");
        incomeAmount.setPrefixComponent(dollarPrefix);
        incomeAmount.getStyle().set("margin", "0 0 0 0");

        Double initial_val = incomeAmount.getValue();

        Button cancelButton = new Button("Cancel", event -> {
            incomeAmount.setValue(initial_val);
            dialog.close();
        });
        Button saveButton = new Button("Save", event -> {
            try {
                String userEmail = VaadinSession.getCurrent().getSession().getAttribute("email").toString();
                incomeService.editIncome(userEmail, incomeName, id, incomeAmount.getValue()); //staticki za sad
            } catch (ExecutionException | InterruptedException | NullPointerException e) {
                e.printStackTrace();
            }
            dialog.close();
            UI.getCurrent().getPage().reload();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        VerticalLayout dialogLayout = new VerticalLayout(headline, incomeAmount, buttonLayout);
        dialogLayout.addClassName("income_dialog");
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        return dialogLayout;
    }

    // -- ADD NEW INCOME DIALOG --
    private static VerticalLayout createDialog2(Dialog dialog) {
        IncomeService incomeService = new IncomeService();

        H2 headline = new H2("Add a new income");
        headline.addClassName("dialog_header");

        TextField incomeName = new TextField("Income name");
        incomeName.getStyle().set("margin", "0 0 0 0");

        NumberField amount = new NumberField("Amount");
        amount.setPrefixComponent(VaadinIcon.DOLLAR.create());
        amount.getStyle().set("margin", "0 0 0 0");

        VerticalLayout fieldLayout = new VerticalLayout(incomeName, amount);
        fieldLayout.setPadding(false);

        Button cancelButton = new Button("Cancel", event -> {
            dialog.close();
            amount.clear();
            incomeName.clear();
        });
        Button saveButton = new Button("Add", event -> {
            try {
                String userEmail = VaadinSession.getCurrent().getSession().getAttribute("email").toString();
                incomeService.saveIncome(userEmail, incomeName.getValue(), amount.getValue()); //staticki za sad
            } catch (ExecutionException | InterruptedException | NullPointerException e) {
                e.printStackTrace();
            }

            dialog.close();
            amount.clear();
            incomeName.clear();
            UI.getCurrent().getPage().reload();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout, buttonLayout);
        dialogLayout.addClassName("income_dialog");
        dialogLayout.setPadding(false);

        return dialogLayout;
    }

    private HorizontalLayout createCard(Income income) {
        IncomeService incomeService = new IncomeService();

        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        // -- BADGE --
        Span badge = new Span("");
        badge.addClassName("badge");
        badge.setWidth("5px");
        if (income.getAmount() < 0) {
            badge.getElement().getStyle().set("background-color", "red");
        } else {
            badge.getElement().getStyle().set("background-color", "green");
        }

        // -- TEXT --
        VerticalLayout incomeDetails = new VerticalLayout();
        Span incomeName = new Span(income.getName());
        incomeName.getStyle().set("font-weight", "bold");

        Span amount = new Span(String.valueOf(income.getAmount()) + " $");
        if (income.getAmount() < 0) {
            amount.getElement().getStyle().set("color", "red");
        } else {
            amount.getElement().getStyle().set("color", "green");
        }
        incomeDetails.add(incomeName, amount);

        incomeDetails.setSpacing(false);
        incomeDetails.setPadding(false);
        incomeDetails.setMargin(false);

        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Edit income");

        VerticalLayout dialogLayout = createDialog1(dialog, income.getAmount(), income.getId(), income.getName());
        dialog.add(dialogLayout);

        // -- BUTTONS --
        Button editButton = new Button(new Icon(VaadinIcon.EDIT), event -> dialog.open());
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        editButton.addClassName("income_button");

        Button trashButton = new Button(new Icon(VaadinIcon.TRASH), event -> {
            try {
                String userEmail = VaadinSession.getCurrent().getSession().getAttribute("email").toString();
                incomeService.deleteIncome(userEmail, income.getId()); //staticki za sad
            } catch (ExecutionException | InterruptedException | NullPointerException e) {
                e.printStackTrace();
            }
            UI.getCurrent().getPage().reload();
        });
        trashButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        trashButton.addClassName("income_button");

        card.add(badge, incomeDetails, editButton, trashButton);
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        IncomeService incomeService = new IncomeService();

        List<Income> incomes = new ArrayList<>();

        try {
            String userEmail = VaadinSession.getCurrent().getSession().getAttribute("email").toString();
            incomes = incomeService.getIncome(userEmail);
        } catch (ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }

        grid.setItems(incomes);
    }

    public Double getTotal_income() {
        IncomeService incomeService = new IncomeService();
        List<Income> incomes = new ArrayList<>();
        Double total_income = 0.0;

        try {
            String userEmail = VaadinSession.getCurrent().getSession().getAttribute("email").toString();
            incomes = incomeService.getIncome(userEmail);
        } catch (ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
        }

        for (Income income : incomes) {
            total_income += income.getAmount();
        }

        return total_income;
    }
}
