package com.example.application.data.service;

import com.example.application.data.entity.User;
import com.example.application.views.MainLayout;
import com.example.application.views.analysis.AnalysisView;
import com.example.application.views.expenses.ExpensesView;
import com.example.application.views.income.IncomeView;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.vaadin.flow.router.RouteConfiguration;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
public class AuthService {
    String API_KEY = "AIzaSyC_0Y8THB9o7G9L2oi5JQwyDvJALL9YATg";
    private static final String POST_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";
    public record AuthorizedRoute(String route, String name, Class view){

    }

    public void authenticate(String email, String password) throws AuthException, IOException {

        URL obj = new URL(POST_URL+API_KEY);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("POST");

        String POST_PARAMS = "email="+email+"&password="+password;
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();

        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if(responseCode != 200){
            throw new AuthException();
        } else{
            createRoutes();
        }
    }

    private void createRoutes() {
        getAuthorizedRoutes().stream().forEach(route -> RouteConfiguration.forSessionScope().setRoute(
                route.route, route.view, MainLayout.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(){
        ArrayList<AuthorizedRoute> routes = new ArrayList<>();
        routes.add(new AuthorizedRoute("income", "Income", IncomeView.class));
        routes.add(new AuthorizedRoute("expenses", "Expenses", ExpensesView.class));
        routes.add(new AuthorizedRoute("analysis", "Analysis", AnalysisView.class));


        return routes;
    }

    public UserRecord createUser(String email, String password) throws ExecutionException, InterruptedException, FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest();
        request.setEmail(email);
        request.setPassword(password);
        UserRecord result = FirebaseAuth.getInstance().createUser(request);
        createRoutes();
        return result;
    }

    public class AuthException extends Exception {

    }
}
