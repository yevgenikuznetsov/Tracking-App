package com.example.readapplication.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.readapplication.Aid.CheckInputValue;
import com.example.readapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Open_Screen extends AppCompatActivity {

    private TextView open_LBL_Sign_Up;
    private Button open_BTN_Sign_In;
    private TextInputLayout open_LBL_Email;
    private TextInputLayout open_LBL_Password;

    private FirebaseAuth mAuth;
    private CheckInputValue checkInputValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open__screen);

        checkInputValue = new CheckInputValue();

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            openMainScreen();
        }

        findView();
        initButton();
        askForPermissions();
    }

    private void initButton(){
        open_LBL_Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToSignUpActivity();
            }
        });

        open_BTN_Sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputAndEnterToApp();
            }
        });

    }

    private void checkInputAndEnterToApp() {
        if (!checkInputValue.validateEmail(open_LBL_Email) | !checkInputValue.validatePassword(open_LBL_Password)) {
            return;
        } else {
            isUserExist();
        }
    }

    private void isUserExist() {
        final String userEnteredEmail = open_LBL_Email.getEditText().getText().toString().trim();
        final String userEnteredPassword = open_LBL_Password.getEditText().getText().toString().trim();

        mAuth.signInWithEmailAndPassword(userEnteredEmail, userEnteredPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            openMainScreen();
                        } else {
                            if (task.getException().getMessage().contains("email")) {
                                open_LBL_Email.setError(task.getException().getMessage());
                            } else if (task.getException().getMessage().contains("password")) {
                                open_LBL_Password.setError(task.getException().getMessage());
                            } else if (task.getException().getMessage().contains("no user")) {
                                open_LBL_Email.setError(task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    private void openMainScreen() {
        Intent intent = new Intent(getApplicationContext(), Main_Screen.class);
        startActivity(intent);
        finish();
    }

    private void moveToSignUpActivity() {
        Intent intent = new Intent(getApplicationContext(), Sign_Up_Screen.class);
        startActivity(intent);
    }

    private void askForPermissions(){
        ActivityCompat.requestPermissions(Open_Screen.this, new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_CALL_LOG}, 1);
    }

    private void findView() {
        open_LBL_Sign_Up = findViewById(R.id.Open_LBL_Sign_Up);
        open_BTN_Sign_In = findViewById(R.id.Open_BTN_Sign_In);
        open_LBL_Email = findViewById(R.id.Open_LBL_Email);
        open_LBL_Password = findViewById(R.id.Open_LBL_Password);
    }
}