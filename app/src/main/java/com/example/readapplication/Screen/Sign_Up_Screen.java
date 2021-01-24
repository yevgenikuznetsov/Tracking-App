package com.example.readapplication.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.readapplication.Aid.CheckInputValue;
import com.example.readapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_Up_Screen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private CheckInputValue checkInputValue;

    private TextInputLayout sign_Up_LBL_Full_Name;
    private TextInputLayout sign_Up_LBL_Emil;
    private TextInputLayout sign_Up_LBL_Password;
    private ImageView sign_Up_LBL_Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up__screen);

        mAuth = FirebaseAuth.getInstance();
        checkInputValue = new CheckInputValue();

        findView();
        initButton();

    }

    private void initButton() {
        sign_Up_LBL_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValueAndStore();
            }
        });
    }

    private void checkValueAndStore() {
        Log.d("pttt", "enter to check");
        if (!checkInputValue.validateName(sign_Up_LBL_Full_Name) |
                !checkInputValue.validateEmail(sign_Up_LBL_Emil) |
                !checkInputValue.validatePassword(sign_Up_LBL_Password)) {
            return;
        }

        String email = sign_Up_LBL_Emil.getEditText().getText().toString().trim();
        String password = sign_Up_LBL_Password.getEditText().getText().toString().trim();

        Log.d("pttt", "after check");
        saveUserInDB(email, password);
    }

    private void saveUserInDB(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("pttt", "save in DB");
                            FirebaseUser user = mAuth.getCurrentUser();
                            newActivity();
                        } else {
                            if (task.getException().getMessage().contains("email")) {
                                Log.d("pttt", "not save in DB");
                                sign_Up_LBL_Emil.setError(task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    private void newActivity() {
        Log.d("pttt", "Close activity");
        finish();
    }

    private void findView() {
        sign_Up_LBL_Full_Name = findViewById(R.id.Sign_Up_LBL_Full_Name);
        sign_Up_LBL_Emil = findViewById(R.id.Sign_Up_LBL_Emil);
        sign_Up_LBL_Password = findViewById(R.id.Sign_Up_LBL_Password);
        sign_Up_LBL_Submit = findViewById(R.id.Sign_Up_LBL_Submit);
    }
}