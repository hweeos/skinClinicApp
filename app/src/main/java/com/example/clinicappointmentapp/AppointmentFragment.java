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

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppointmentFragment extends Fragment {

    private EditText etName, etAge, etSex, etPhone;
    private Spinner spinnerAppointmentTimes;
    private TextView tvSelectedDateTime;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Add your logic here
        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etSex = view.findViewById(R.id.etSex);
        etPhone = view.findViewById(R.id.etPhone);
        spinnerAppointmentTimes = view.findViewById(R.id.spinnerAppointmentTimes);
        Button btnSelectDateTime = view.findViewById(R.id.btnSelectDateTime);
        tvSelectedDateTime = view.findViewById(R.id.tvSelectedDateTime);

        btnSelectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        // Populate the spinner with available appointment times
        populateAppointmentTimesSpinner();

        return view;
    }

    private void showDateTimePicker() {
        // Implement date and time picker logic here
        // You can use DatePickerDialog and TimePickerDialog
        // to allow users to choose the date and time.

        // For simplicity, let's assume you have a method to show a date picker
        showDatePicker();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Handle the selected date
                        showTimePicker();
                    }
                },
                // Set initial date values if needed
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showTimePicker() {
        // Implement TimePickerDialog logic here
        // Set the selected date and time to tvSelectedDateTime TextView
        // You can use a library or a custom method to show a time picker
        // For simplicity, let's assume you have a method called showTimePickerDialog
        showTimePickerDialog();
    }

    private void showTimePickerDialog() {
        // Implement TimePickerDialog logic here
        // Set the selected date and time to tvSelectedDateTime TextView

        // Example code for a simple time picker
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the selected time
                        String selectedDateTime = getSelectedDateTime(hourOfDay, minute);
                        tvSelectedDateTime.setText("Selected Date and Time: " + selectedDateTime);
                    }
                },
                // Set initial time values if needed
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
        );

        timePickerDialog.show();
    }

    private String getSelectedDateTime(int hourOfDay, int minute) {
        // Process the selected date and time
        // You might want to format it as needed

        // Example: Format as "YYYY-MM-DD HH:mm"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = new Date(); // Replace this with the actual date selected
        date.setHours(hourOfDay);
        date.setMinutes(minute);
        return dateFormat.format(date);
    }

    private void populateAppointmentTimesSpinner() {
        // Implement logic to populate the spinner with available appointment times
        // You can use an array adapter to set the available times
        // For simplicity, let's assume you have an array of times
        String[] times = {"10:00 AM", "02:00 PM", "04:30 PM"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                times
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerAppointmentTimes.setAdapter(adapter);
    }

}
