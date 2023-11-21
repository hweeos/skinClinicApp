package com.example.clinicappointmentapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentFragment extends Fragment {

    private Spinner spinnerAvailableSlots;
    private Button btnBookAppointment;

    // Additional logic for managing available slots (you might use a List or an array of available slots)
    private List<String> availableSlots = Arrays.asList("Slot 1", "Slot 2", "Slot 3");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        // Existing code

        spinnerAvailableSlots = view.findViewById(R.id.spinnerAvailableSlots);
        btnBookAppointment = view.findViewById(R.id.btnBookAppointment);

        // Populate the spinner with available slots
        populateAvailableSlotsSpinner();

        // Set click listener for the book appointment button
        btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle booking logic (update database, show confirmation, etc.)
                bookAppointment();
            }
        });

        return view;
    }

    private void populateAvailableSlotsSpinner() {
        // Use an ArrayAdapter to populate the spinner with available slots
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                availableSlots
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAvailableSlots.setAdapter(adapter);
    }

    private void bookAppointment() {
        // Get the selected slot from the spinner
        String selectedSlot = spinnerAvailableSlots.getSelectedItem().toString();

        // Perform booking logic (update database, show confirmation, etc.)
        // ...

        // Remove the booked slot from the list of available slots
        availableSlots.remove(selectedSlot);

        // Update the spinner to reflect the changes
        populateAvailableSlotsSpinner();

        // Optionally, show a confirmation message to the user
        Toast.makeText(requireContext(), "Appointment booked for " + selectedSlot, Toast.LENGTH_SHORT).show();
    }

}
