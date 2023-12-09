package com.example.clinicappointmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.clinicappointmentapp.R;


import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    public void onItemClick(View v) {
        // Perform different actions based on the clicked square
        if (v.getId() == R.id.upcomingEventsSquare) {
            // Action for UPCOMING EVENTS square
            showToast("Upcoming Events Clicked");
        } else if (v.getId() == R.id.medicalHistorySquare) {
            // Action for MEDICAL HISTORY square
            showToast("Medical History Clicked");
        } else if (v.getId() == R.id.notificationsSquare) {
            // Action for NOTIFICATIONS square
            showToast("Notifications Clicked");
        } else if (v.getId() == R.id.reviewDateSquare) {
            // Action for REVIEW DATE square
            showToast("Review Date Clicked");
        } else if (v.getId() == R.id.bookAppointmentSquare) {
            // Action for BOOK APPOINTMENT square
            showToast("Book Appointment Clicked");
        } else if (v.getId() == R.id.contactUsSquare) {
            // Action for CONTACT US square
            showToast("Contact Us Clicked");
        }
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}


