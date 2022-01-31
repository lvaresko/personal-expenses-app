package com.example.application.data.service;

import com.example.application.views.logout.LogoutView;
import com.example.application.views.MainLayout;
import com.example.application.views.analysis.AnalysisView;
import com.example.application.views.expenses.ExpensesView;
import com.example.application.views.income.IncomeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
public class AuthService {
    private static final String POST_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";
    String API_KEY = "AIzaSyC_0Y8THB9o7G9L2oi5JQwyDvJALL9YATg";

    public void authenticate(String email, String password) throws AuthException, IOException {

        URL obj = new URL(POST_URL + API_KEY);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("POST");

        String POST_PARAMS = "email=" + email + "&password=" + password;
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();

        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode != 200) {
            throw new AuthException();
        } else {
            createRoutes();
            VaadinSession.getCurrent().getSession().setAttribute("email", email);
        }
    }

    private void createRoutes() {
        getAuthorizedRoutes().stream().forEach(route -> {
            if (route.name.equals("Logout")) {
                RouteConfiguration.forSessionScope().setRoute(route.route, route.view);
            } else {
                RouteConfiguration.forSessionScope().setRoute(route.route, route.view, MainLayout.class);
            }
        });
    }

    public List<AuthorizedRoute> getAuthorizedRoutes() {
        ArrayList<AuthorizedRoute> routes = new ArrayList<>();
        routes.add(new AuthorizedRoute("income", "Income", IncomeView.class));
        routes.add(new AuthorizedRoute("expenses", "Expenses", ExpensesView.class));
        routes.add(new AuthorizedRoute("analysis", "Analysis", AnalysisView.class));
        routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));


        return routes;
    }

    public UserRecord createUser(String email, String password) throws ExecutionException, InterruptedException, FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest();
        request.setEmail(email);
        request.setPassword(password);
        UserRecord result = FirebaseAuth.getInstance().createUser(request);
        createRoutes();
        VaadinSession.getCurrent().getSession().setAttribute("email", email);
        return result;
    }

    public record AuthorizedRoute(String route, String name, Class view) {

    }

    public class AuthException extends Exception {

    }
}
