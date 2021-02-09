package com.example.readapplication.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.readapplication.Service.AppService;
import com.example.readapplication.R;

import java.util.Objects;

public class Menu_Fragment extends Fragment {
    private Button menu_BTN_Start;
    private Button menu_BTN_Finish;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        findView(view);
        initButton();

        menu_BTN_Finish.setEnabled(false);
    }

    private void findView(View view) {
        menu_BTN_Start = (Button) view.findViewById(R.id.Menu_BTN_Start);
        menu_BTN_Finish = (Button) view.findViewById(R.id.Menu_BTN_Finish);
    }

    // Set click listener to turn on or turn off the background service
    private void initButton() {
        menu_BTN_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionToService(AppService.START_FOREGROUND_SERVICE);

                menu_BTN_Finish.setEnabled(true);
                menu_BTN_Start.setEnabled(false);
            }
        });

        menu_BTN_Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionToService(AppService.STOP_FOREGROUND_SERVICE);

                menu_BTN_Finish.setEnabled(false);
                menu_BTN_Start.setEnabled(true);
            }
        });
    }

    private void actionToService(String action) {
        // Set service
        Intent startIntent = new Intent(getContext(), AppService.class);
        startIntent.setAction(action);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Objects.requireNonNull(getActivity()).startForegroundService(startIntent);
        } else {
            Objects.requireNonNull(getActivity()).startService(startIntent);
        }
    }
}
