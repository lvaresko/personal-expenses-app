package com.example.application.views.login;

import com.example.application.data.service.AuthService;
import com.example.application.views.register.RegisterView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.io.IOException;


@PageTitle("Login")
@Route(value = "login")
@RouteAlias("")
public class LoginView extends VerticalLayout {

    public LoginView(AuthService authService) {
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Password");
        Span denied = new Span("Denied");
        denied.getElement().getThemeList().add("badge error");
        denied.setVisible(false);
        Button login = new Button("Login", event -> {
            denied.setVisible(false);
            try {
                authService.authenticate(email.getValue(), password.getValue());
                UI.getCurrent().navigate("income");
            } catch (AuthService.AuthException e) {
                e.printStackTrace();
                denied.getElement().setText("Wrong email or password input");
                denied.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        login.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

        HorizontalLayout layout = new HorizontalLayout();

        FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("400px");
        formLayout.setMaxHeight("200px");
        formLayout.add(email, password, login);

        HorizontalLayout footer = new HorizontalLayout();
        Span newUser = new Span("Don't have an account?");
        Button register = new Button("Register here", e -> {
            UI.getCurrent().navigate(RegisterView.class);
        });
        register.getStyle().set("height", "30px");
        register.getStyle().set("font-size", "16px");
        register.getStyle().set("background", "white");
        register.getStyle().set("vertical-align", "center");
        register.getStyle().set("margin", "0px");


        footer.add(newUser, register);

        layout.setSizeFull();
        layout.setSizeFull();
        layout.add(formLayout);
        layout.setMaxHeight("250px");
        layout.setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H1("Login"), layout, footer, denied);

    }

}
