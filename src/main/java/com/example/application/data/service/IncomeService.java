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
            if (document.getData().get("amount").getClass().getSimpleName() == "Double") {
                amount = (Double) document.getData().get("amount");
            } else {
                amount =  ((Number)document.getData().get("amount")).doubleValue();
            }

            System.out.println(document.getId());

            String name = (String) document.getData().get("name");
            all_incomes.add(new Income(amount, name, document.getId()));
        }

        return all_incomes;
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

        System.out.println("Update time : " + collectionsApiFuture.get().getUpdateTime());

        return collectionsApiFuture.get();
    }

   /* public Income getIncome(String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("users").document(email).collection("incomes").document("Y6fqIAZzk50wFqbVCjL7");
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        if(document.exists()) {
            Double amount = (Double) document.getData().get("amount");
            String name = (String) document.getData().get("name");

            Income income = new Income(amount, name);

            return income;
        } else {
            System.out.println("NEMA DOC");
            return null;
        }
    }*/

}
