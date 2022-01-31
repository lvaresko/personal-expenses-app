package com.example.application.data.service;
import com.example.application.data.entity.Income;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class IncomeService {

    public WriteResult saveIncome(String email, String name, Double amount) throws InterruptedException, ExecutionException {
        Income income = new Income(amount, name, "");

        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore
                .collection("users")
                .document(email)
                .collection("incomes")
                .document()
                .set(income);

        return collectionsApiFuture.get();
    }

    public List<Income> getIncome(String email) throws ExecutionException, InterruptedException, NullPointerException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("users").document(email).collection("incomes").get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Income> all_incomes = new ArrayList<>();

        Double amount;

        for (DocumentSnapshot document : documents) {
            if (document.getData().get("amount").getClass().getSimpleName().equals("Double")) {
                amount = (Double) document.getData().get("amount");
            } else {
                amount =  ((Number)document.getData().get("amount")).doubleValue();
            }


            String name = (String) document.getData().get("name");
            all_incomes.add(new Income(amount, name, document.getId()));
        }

        return all_incomes;
    }

    public Income getIncomeByType(String email, String type) throws ExecutionException, InterruptedException, NullPointerException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("users").document(email).collection("incomes").get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (DocumentSnapshot document : documents) {
            String name = (String) document.getData().get("name");
            if(name.equals(type)){
                Double amount;

                if (document.getData().get("amount").getClass().getSimpleName().equals("Double")) {
                    amount = (Double) document.getData().get("amount");
                } else {
                    amount = ((Number)document.getData().get("amount")).doubleValue();
                }

            return new Income(amount, type, document.getId());
            }

        }

        return new Income(0.0, type, "null");
    }

    public WriteResult editIncome(String email, String name, String id, Double amount) throws InterruptedException, ExecutionException {
        Income income = new Income(amount, name, id);

        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore
                .collection("users")
                .document(email)
                .collection("incomes")
                .document(id)
                .set(income);

        return collectionsApiFuture.get();
    }

    public WriteResult deleteIncome(String email, String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore
                .collection("users")
                .document(email)
                .collection("incomes")
                .document(id)
                .delete();

        return collectionsApiFuture.get();
    }
}
