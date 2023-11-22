package com.example.clinicappointmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AppointmentFragment extends Fragment {

    private EditText etName, etAge, etSex, etPhone, etResidence, etReason;
    private Button btnBookAppointment;

    // Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("appointments");

        // Initialize UI elements
        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etSex = view.findViewById(R.id.etSex);
        etPhone = view.findViewById(R.id.etPhone);
        etResidence = view.findViewById(R.id.etResidence);
        etReason = view.findViewById(R.id.etReason);

        btnBookAppointment = view.findViewById(R.id.btnBookAppointment);
        btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to submit data
                bookAppointment();
            }
        });

        return view;
    }

    private void bookAppointment() {
        String name = etName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String sex = etSex.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String residence = etResidence.getText().toString().trim();
        String reason = etReason.getText().toString().trim();

        // Check if any of the fields are empty
        if (name.isEmpty() || age.isEmpty() || sex.isEmpty() || phone.isEmpty() || residence.isEmpty() || reason.isEmpty()) {
            // Handle empty fields as needed
            return;
        }

        // Get the current time
        String submissionTime = getCurrentTime();

        // Create a new appointment object
        Appointment appointment = new Appointment(name, age, sex, phone, residence, reason, submissionTime);

        // Push the appointment data to Firebase
        databaseReference.push().setValue(appointment);

        // Clear the input fields
        etName.setText("");
        etAge.setText("");
        etSex.setText("");
        etPhone.setText("");
        etResidence.setText("");
        etReason.setText("");

        // Show the success fragment with reason and submission time
        showAppointmentSuccessFragment(reason, submissionTime);
    }

    private void showAppointmentSuccessFragment(String reason, String submissionTime) {
        AppointmentSuccessFragment successFragment = AppointmentSuccessFragment.newInstance(reason, submissionTime);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, successFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Method to get the current time in a specific format
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(Calendar.getInstance().getTime());
    }
}
