package com.example.clinicappointmentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;

                if (menuItem.getItemId() == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();
                } else if (menuItem.getItemId() == R.id.navigation_appointment) {
                    selectedFragment = new AppointmentSlotsFragment();
                }else if (menuItem.getItemId() == R.id.navigation_profile) {
                    selectedFragment = new ProfileFragment();
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .addToBackStack(null) // Add this line to handle back stack
                        .commit();

                return true;
            }
        });

    }
}