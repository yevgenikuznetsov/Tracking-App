package com.example.readapplication.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.readapplication.Fragment.Call_Fragment;
import com.example.readapplication.Fragment.Menu_Fragment;
import com.example.readapplication.Fragment.SMS_Fragment;
import com.example.readapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Main_Screen extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private ImageView main_IMG_Log_Out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__screen);

        findView();
        initButton();
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.Main_FRG_Content, new Menu_Fragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.Nav_Menu:
                        selectedFragment = new Menu_Fragment();
                        break;
                    case R.id.Nav_SMSHistory:
                        selectedFragment = new SMS_Fragment();
                        break;
                    case R.id.Nav_CallHistory:
                        selectedFragment = new Call_Fragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.Main_FRG_Content, selectedFragment).commit();

                return true;
            }
        };

    private void initButton() {
        main_IMG_Log_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;

                FirebaseAuth.getInstance().signOut();

                intent = new Intent(getApplicationContext(), Open_Screen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findView() {
        bottomNavigation = findViewById(R.id.Main_BTN_Navigation);
        main_IMG_Log_Out = findViewById(R.id.Main_IMG_Log_Out);
    }
}