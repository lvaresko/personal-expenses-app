package com.example.application.data.service;

import com.example.application.data.entity.Expense;
import com.example.application.data.entity.Income;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExpenseService {

    public List<Expense> getExpenses(String email) throws ExecutionException, InterruptedException, NullPointerException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("users").document(email).collection("expenses").orderBy("date", Query.Direction.DESCENDING).get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Expense> all_expensess = new ArrayList<>();

        Double amount;

        for (DocumentSnapshot document : documents) {
            if (document.getData().get("amount").getClass().getSimpleName().equals("Double")) {
                amount = (Double) document.getData().get("amount");
            } else {
                amount = ((Number) document.getData().get("amount")).doubleValue();
            }

            String category = (String) document.getData().get("category");
            String title = (String) document.getData().get("title");
            String payedWith = (String) document.getData().get("payed_with");
            Timestamp date = (Timestamp) document.getData().get("date");
            all_expensess.add(new Expense(category, title, amount, payedWith, date));

        }

        return all_expensess;
    }

    public WriteResult saveExpense(String email, String category, String title, Double amount, String payedWith, Timestamp date) throws InterruptedException, ExecutionException {
        IncomeService incomeService = new IncomeService();
        Expense expense = new Expense(category, title, amount, payedWith, date);

        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("users").document(email).collection("expenses").document().set(expense);

        Income oldIncome = incomeService.getIncomeByType(email, payedWith);
        incomeService.editIncome(email, payedWith, oldIncome.getId(), oldIncome.getAmount() - amount);

        return collectionsApiFuture.get();
    }
}
