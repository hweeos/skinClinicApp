package com.example.clinicappointmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class AppointmentSuccessFragment extends Fragment {

    private TextView tvReason;
    private TextView tvSubmissionTime;

    public static AppointmentSuccessFragment newInstance(String reason, String submissionTime) {
        AppointmentSuccessFragment fragment = new AppointmentSuccessFragment();
        Bundle args = new Bundle();
        args.putString("reason", reason);
        args.putString("submissionTime", submissionTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_success, container, false);

        // Initialize UI elements
        tvReason = view.findViewById(R.id.tvReason);
        tvSubmissionTime = view.findViewById(R.id.tvSubmissionTime);

        // Retrieve reason and submission time from arguments
        if (getArguments() != null) {
            String reason = getArguments().getString("reason", "");
            String submissionTime = getArguments().getString("submissionTime", "");

            // Set the values to the TextViews
            tvReason.setText("Reason: " + reason);
            tvSubmissionTime.setText("Submission Time: " + submissionTime);
        }

        return view;
    }
}
