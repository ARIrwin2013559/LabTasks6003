package com.example.lab4task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginButton;
    TextView mCreateButton;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();

        mLoginButton = findViewById(R.id.logButton);

        mCreateButton = findViewById(R.id.logReg);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                //if (TestUtils.isEmpty(email)){
                //    mEmail.setError("An Email address is required!");
                //    return;
                //}

                // Authenticate User
                fAuth.signInWithEmailAndPassword(email,
                        password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(login.this, "Logged in Successfully!",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),
                                    MainActivity.class));
                        }
                        else {
                            Toast.makeText(login.this, "Error!" +
                                    task.getException(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }

    });
        mCreateButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(),
                register.class));
        }
    });

    }
    public void linkToSignIn (View view) {
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}