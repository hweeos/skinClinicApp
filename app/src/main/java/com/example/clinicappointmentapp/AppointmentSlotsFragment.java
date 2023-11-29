package com.example.clinicappointmentapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppointmentSlotsFragment extends Fragment {
    private static final String TAG = "AppointmentSlotsFragment";

    // ...

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_slots, container, false);

        // Retrieve and display data from Firebase
        retrieveAllAppointmentsFromFirebase();

        return view;
    }

//    private void retrieveAvailableAppointmentsFromFirebase() {
//        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointment_slots");
//
////         Query appointments with status = Available
//        appointmentsRef.orderByChild("appointment_status").equalTo("Available").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Appointment> appointmentsList = new ArrayList<>();
//
//                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
//                    // Only retrieve specific fields
//                    String appointmentDuration = appointmentSnapshot.child("Appointment_Duration").getValue(String.class);
//                    String appointmentTime = appointmentSnapshot.child("Appointment_Time").getValue(String.class);
//                    String dateOfAppointment = appointmentSnapshot.child("Date_of_appointment").getValue(String.class);
//                    String doctorAssigned = appointmentSnapshot.child("Doctor_Assigned").getValue(String.class);
//
//                    // Create Appointment object
//                    Appointment appointment = new Appointment(appointmentDuration, appointmentTime, dateOfAppointment, doctorAssigned);
//                    appointmentsList.add(appointment);
//                }
//
//                // Populate the table with retrieved data
//                populateTable(appointmentsList);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", databaseError.toException());
//            }
//        });
//    }
    private void retrieveAllAppointmentsFromFirebase() {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointment_slots");

        appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Appointment> appointmentsList = new ArrayList<>();

                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve all fields
                    String appointmentDuration = appointmentSnapshot.child("Appointment_Duration").getValue(String.class);
                    String appointmentTime = appointmentSnapshot.child("Appointment_Time").getValue(String.class);
                    String dateOfAppointment = appointmentSnapshot.child("Date_of_appointment").getValue(String.class);
                    String doctorAssigned = appointmentSnapshot.child("Doctor_Assigned").getValue(String.class);
                    // Add other fields as needed

                    // Create Appointment object
                    Appointment appointment = new Appointment(appointmentDuration, appointmentTime, dateOfAppointment, doctorAssigned);
                    appointmentsList.add(appointment);
                }

                // Populate the table with retrieved data
                populateTable(appointmentsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }


    private void populateTable(List<Appointment> appointmentsList) {
        TableLayout tableLayout = requireView().findViewById(R.id.tableLayout);

        // Clear existing rows
        tableLayout.removeAllViews();

        // Add header row
        TableRow headerRow = new TableRow(requireContext());
        headerRow.addView(createTextView("Date", true));
        headerRow.addView(createTextView("Time", true));
        headerRow.addView(createTextView("Duration", true));
        headerRow.addView(createTextView("Doctor", true));
        tableLayout.addView(headerRow);

        // Add data rows
        for (Appointment appointment : appointmentsList) {
            TableRow dataRow = new TableRow(requireContext());
            dataRow.addView(createTextView(appointment.getDateOfAppointment(), false));
            dataRow.addView(createTextView(appointment.getAppointmentTime(), false));
            dataRow.addView(createTextView(appointment.getAppointmentDuration(), false));
            dataRow.addView(createTextView(appointment.getDoctorAssigned(), false));
            tableLayout.addView(dataRow);
        }
    }

    private TextView createTextView(String text, boolean isHeader) {
        TextView textView = new TextView(requireContext());
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                isHeader ? 0.5f : 1.0f
        ));
        return textView;
    }

    // ...
}
