package com.example.speechful;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText passwordField, repeatPasswordField, emailAddressField, firstNameField, lastNameField;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();


        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        firstNameField = (EditText) findViewById(R.id.firstNameField);
        lastNameField = (EditText) findViewById(R.id.lastNameField);
        emailAddressField = (EditText) findViewById(R.id.emailAddressField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        repeatPasswordField = (EditText) findViewById(R.id.repeatPasswordField);

        //perhaps add progress bar

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.registerButton:
                registerUser();
                break;
        }
    }

    private void registerUser() {

        String firstName = firstNameField.getText().toString().trim();
        String lastName = lastNameField.getText().toString().trim();
        String email = emailAddressField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String repeatPassword = repeatPasswordField.getText().toString().trim();


        if (firstName.isEmpty()) {
            firstNameField.setError("First name is required");
            firstNameField.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            lastNameField.setError("Last name is required");
            lastNameField.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            emailAddressField.setError("Email address is required");
            emailAddressField.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddressField.setError("Provide valid email address");
            emailAddressField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordField.setError("Password is required");
            passwordField.requestFocus();
            return;
        }
        if (repeatPassword.isEmpty()) {
            repeatPasswordField.setError("Repeating the password is required");
            repeatPasswordField.requestFocus();
            return;
        }
        if (!password.matches(repeatPassword)) {
            passwordField.setError("Passwords should match");
            passwordField.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordField.setError("Min password length is 6");
            passwordField.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(firstName, lastName, email);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "user has been registered succsessfully", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(RegisterUser.this, "not successful registration", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(RegisterUser.this, "not successful registration", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}