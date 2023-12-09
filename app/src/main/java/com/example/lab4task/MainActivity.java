package com.example.lab4task;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    TextView fullname, email, phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone = findViewById(R.id.profilePhone);
        fullname = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);

        // Implementing Firebase
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        // Getting the userID
        userID = fAuth.getCurrentUser().getUid();

        // Retrieving data from Firebase database and applying the data
        // in fields in MainActivity
        DocumentReference documentReference =
                fstore.collection("users").document(userID);
        // Snap shot listener
        documentReference.addSnapshotListener(this,
                new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        phone.setText(documentSnapshot.getString("phone"));
                        fullname.setText(documentSnapshot.getString("fullname"));
                        email.setText(documentSnapshot.getString("email"));
                    }
                });
        }
        // Using get methods
        public void logout(View view){
            FirebaseAuth.getInstance().signOut();
            StartActivity(new Intent(getApplicationContext(), login.class));
    }

    private void StartActivity(Intent intent) {
    }

}