package com.example.clinicappointmentapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentSlotsFragment extends Fragment {
    private static final String TAG = "AppointmentSlotsFragment";

    private DatabaseReference bookedAppointmentsRef;
    private DatabaseReference appointmentsRef;

    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_slots, container, false);

        // Initialize bookedAppointmentsRef and appointmentsRef
        bookedAppointmentsRef = FirebaseDatabase.getInstance().getReference("booked_appointments");
        appointmentsRef = FirebaseDatabase.getInstance().getReference("appointment_slots");

        // Retrieve and display data from Firebase
        retrieveAllAppointmentsFromFirebase();

        return view;
    }

    private void retrieveAllAppointmentsFromFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userAppointmentsRef = FirebaseDatabase.getInstance().getReference("booked_appointments");

            userAppointmentsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User has a pending appointment, show details
                        showPendingAppointmentDetails(dataSnapshot);
                    } else {
                        // No pending appointment, proceed to retrieve and display slots
                        retrieveAndDisplaySlots();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });
        } else {
            // User is not authenticated, handle accordingly
        }
    }

    private void retrieveAndDisplaySlots() {
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
        headerRow.addView(createTextView("", true)); // Empty cell for the "BOOK" button
        tableLayout.addView(headerRow);

        // Add data rows
        for (Appointment appointment : appointmentsList) {
            TableRow dataRow = new TableRow(requireContext());
            dataRow.addView(createTextView(appointment.getDateOfAppointment(), false));
            dataRow.addView(createTextView(appointment.getAppointmentTime(), false));
            dataRow.addView(createTextView(appointment.getAppointmentDuration(), false));
            dataRow.addView(createTextView(appointment.getDoctorAssigned(), false));

            // Add "BOOK" button
            Button bookButton = createBookButton(appointment);
            dataRow.addView(bookButton);

            tableLayout.addView(dataRow);
        }
    }

    private Button createBookButton(Appointment appointment) {
        Button bookButton = new Button(requireContext());
        bookButton.setText("BOOK");
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDialog(appointment);
            }
        });
        return bookButton;
    }

    private void showBookingDialog(Appointment appointment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Enter Booking Details");

        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_booking, null);
        builder.setView(view);

        EditText etFirstName = view.findViewById(R.id.etFirstName);
        EditText etLastName = view.findViewById(R.id.etLastName);
        EditText etAge = view.findViewById(R.id.etAge);
        EditText etSex = view.findViewById(R.id.etSex);
        EditText etDistrict = view.findViewById(R.id.etDistrict);
        EditText etVillage = view.findViewById(R.id.etVillage);

        builder.setPositiveButton("Book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String age = etAge.getText().toString();
                String sex = etSex.getText().toString();
                String district = etDistrict.getText().toString();
                String village = etVillage.getText().toString();

                // Handle booking logic and save to Firebase
                saveBookingToFirebase(appointment, firstName, lastName, age, sex, district, village);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog bookingDialog = builder.create();
        bookingDialog.show();
    }

    private void saveBookingToFirebase(Appointment appointment, String firstName, String lastName, String age, String sex, String district, String village) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Create a new node for the booked appointment
            String bookingKey = bookedAppointmentsRef.push().getKey();

            // Build a data object with all the details
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("userId", userId);
            bookingData.put("date", appointment.getDateOfAppointment());
            bookingData.put("time", appointment.getAppointmentTime());
            bookingData.put("duration", appointment.getAppointmentDuration());
            bookingData.put("doctor", appointment.getDoctorAssigned());

            // Add additional details from the dialog
            bookingData.put("firstName", firstName);
            bookingData.put("lastName", lastName);
            bookingData.put("age", age);
            bookingData.put("sex", sex);
            bookingData.put("district", district);
            bookingData.put("village", village);

            // Save data to Firebase
            bookedAppointmentsRef.child(bookingKey).setValue(bookingData);

            // Optionally, you may want to remove the booked appointment from the original node
            removeAppointmentFromSlots(appointment);
        } else {
            // User is not authenticated, handle accordingly
        }
    }

    private void removeAppointmentFromSlots(Appointment appointment) {
        appointmentsRef.orderByChild("Date_of_appointment").equalTo(appointment.getDateOfAppointment())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot slotSnapshot : dataSnapshot.getChildren()) {
                            // Compare other fields if needed (e.g., appointment time, duration, etc.)
                            if (slotSnapshot.child("Appointment_Time").getValue(String.class).equals(appointment.getAppointmentTime())) {
                                // Remove the booked appointment from appointment_slots
                                slotSnapshot.getRef().removeValue();
                                break; // Exit loop after removing the appointment
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }

    private void showPendingAppointmentDetails(DataSnapshot dataSnapshot) {
        DataSnapshot appointmentSnapshot = dataSnapshot.getChildren().iterator().next();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_pending_appointment, null);
        builder.setView(view);

        // Retrieve appointment details from the dataSnapshot
        String date = appointmentSnapshot.child("date").getValue(String.class);
        String time = appointmentSnapshot.child("time").getValue(String.class);
        String duration = appointmentSnapshot.child("duration").getValue(String.class);
        String doctor = appointmentSnapshot.child("doctor").getValue(String.class);

        // Find TextViews in the custom dialog layout
        TextView tvHeading = view.findViewById(R.id.tvHeading);
        TextView tvDate = view.findViewById(R.id.tvDate);
        TextView tvTime = view.findViewById(R.id.tvTime);
        TextView tvDuration = view.findViewById(R.id.tvDuration);
        TextView tvDoctor = view.findViewById(R.id.tvDoctor);

        // Set heading and appointment details to the TextViews
        tvHeading.setText("YOUR PENDING APPOINTMENT");
        tvDate.setText("Date: " + date);
        tvTime.setText("Time: " + time);
        tvDuration.setText("Duration: " + duration);
        tvDoctor.setText("Doctor: " + doctor);

        // Find buttons in the custom dialog layout
        Button btnCancelAppointment = view.findViewById(R.id.btnCancelAppointment);
        Button btnClose = view.findViewById(R.id.btnClose);

        btnCancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAppointment(appointmentSnapshot.getKey());
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private void cancelAppointment(String appointmentKey) {
        bookedAppointmentsRef.child(appointmentKey).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        retrieveAndDisplaySlots();
                        addAppointmentToSlots(appointmentKey);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to cancel the appointment
                    }
                });
    }

    private void addAppointmentToSlots(String canceledAppointmentKey) {
        bookedAppointmentsRef.child(canceledAppointmentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String appointmentDuration = dataSnapshot.child("duration").getValue(String.class);
                    String appointmentTime = dataSnapshot.child("time").getValue(String.class);
                    String dateOfAppointment = dataSnapshot.child("date").getValue(String.class);
                    String doctorAssigned = dataSnapshot.child("doctor").getValue(String.class);

                    Appointment canceledAppointment = new Appointment(appointmentDuration, appointmentTime, dateOfAppointment, doctorAssigned);

                    // Add the canceled appointment back to the appointment_slots node
                    appointmentsRef.push().setValue(canceledAppointment);

                    // Optionally, you may want to remove the canceled appointment from the booked_appointments node
                    bookedAppointmentsRef.child(canceledAppointmentKey).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
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
}
