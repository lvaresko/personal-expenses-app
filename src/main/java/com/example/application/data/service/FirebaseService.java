package com.example.application.data.service;

import com.example.application.data.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {
    private FirebaseAuth mAuth;

    public WriteResult createUser(String email, String password) throws ExecutionException, InterruptedException {
        Map<String, Object> user = new HashMap<>();
        user.put("status", "active");

        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore
                .collection("users")
                .document(email)
                .set(user);


        mAuth = FirebaseAuth.getInstance();



        
        
        
        


        return collectionsApiFuture.get();
    }

    public boolean getUserStatus(String email) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("users").document(email);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        boolean value = false;
        return value;


        /*if(document.exists()) {
            // treba toObject(klasa.class)
            value = document.toObject(Expenses.class);
            return value;
        } else {
            return false;
        }*/
    }


}
