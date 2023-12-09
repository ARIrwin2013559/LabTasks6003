package com.example.lab4task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class register extends AppCompatActivity {
    EditText mFLName, mEmail, mPass, mPhone;
    Button mRegisterButton;
    TextView mLoginButton;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    // creating an instance of FirebaseFirestore
    FirebaseFirestore fstore;  // May not be compatible with latest version of FB?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFLName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.regEmail);
        mPass = findViewById(R.id.regPassword);
        mPhone = findViewById(R.id.regPhone);
        mRegisterButton = findViewById(R.id.regButton);
        fAuth = FirebaseAuth.getInstance();

        //instantiate the Firestore object
        fstore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBarReg);

        // Register button clicked

        mRegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPass.getText().toString().trim();

                // Retrieving Full Name and phone number
                String fullname = mFLName.getText().toString();
                String phone = mPhone.getText().toString();

                // Checking if values are entered
                // If not entered, user can't login to app
                // And an error is displayed
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("An Email is Required to log in");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPass.setError("An Password is Required to log in");
                    return;
                }
                if (password.length()<5){
                    mPass.setError("Password is too short! Please enter a password longer than 5 characters");
                    return;
                }
                if (TextUtils.isEmpty(fullname)){
                    mFLName.setError("You must enter your full name to log in");
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    mPhone.setError("You must enter a phone number to log in");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                // Checks if the user is registered
                if (fAuth.getCurrentUser() !=null){
                    startActivity(new Intent(getApplicationContext(),
                            MainActivity.class));
                    finish();
                }

                //Registering the user
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(register.this, "User Created!",
                                    Toast.LENGTH_SHORT).show();

                            // retrieving the userID of the currently logged in user
                            String userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference =
                                    fstore.collection("users").document(userID);

                            // Creating a map
                            Map<String, Object> user = new HashMap<>();

                            // Inserting data
                            user.put("fName", fullname);
                            user.put("email", email);
                            user.put("phone", phone);

                            // Store Data on Firebase and checks if successful
                            // or not
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {
                                    Log.d( "TAG", "OnSuccess: user profile is created for " +
                                            userID);
                                }

                            });
                            // Creates Documents
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            FirebaseUser usr = fAuth.getCurrentUser();

                            usr.sendEmailVerification().addOnSuccessListener(new OnSuccessListener() {

                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(register.this,
                                            "Verification Email has been sent",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener(){
                                @Override
                                public void onFailure(@NonNull Exception e){
                                    Log.d("TAG", "OFailure Email not sent "
                                    + e.getMessage());
                                }
                            });
                            Toast.makeText(register.this, "User Created",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(register.this, "Error!" + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });

    }
}