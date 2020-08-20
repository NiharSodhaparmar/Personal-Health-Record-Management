package com.example.phrm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AppointmentDetails extends AppCompatActivity {

    private TextView appointment_date,appointment_time;
    EditText doctor_name,clinic_name,street,area,city,state;
    Button btn_edit,btn_delete;
    private String familymemberid,appointmentid;
    DocumentReference appointment;
    Spinner subject_mat;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String amPm;
    String[] sub_mat = {"Subject Matter",
            "Cardiology",
            "Pneumology",
            "Neurology",
            "Radiotherapy",
            "Immunology",
            "Dermatology",
            "Urology",
            "Opthalmology",
            "Accupuncture",
            "Dentistry",
            "Homeopathy",
            "Orthopaedics Surgery",
            "Psychology"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("familymemberid");
        appointmentid = extras.getString("appointment_key");

        appointment_date = findViewById(R.id.appointment_date1);
        appointment_time = findViewById(R.id.appointment_time1);
        subject_mat = findViewById(R.id.subject_matter1);
        doctor_name = findViewById(R.id.doctor_name1);
        clinic_name = findViewById(R.id.clinic_name1);
        street = findViewById(R.id.street11);
        area = findViewById(R.id.area1);
        city = findViewById(R.id.city1);
        state = findViewById(R.id.state11);
        btn_edit = findViewById(R.id.btn_edit_app);
        btn_delete = findViewById(R.id.btn_delete_app);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(AppointmentDetails.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.subject_matter));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject_mat.setAdapter(myAdapter);

        appointment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AppointmentDetails.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });



        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                // Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                appointment_date.setText(date);
            }
        };

        //timepicker

        appointment_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog;
                Calendar calendar;
                int currentHour;
                int currentMinute;

                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AppointmentDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        appointment_time.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        appointment = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("familyMembers").document(familymemberid)
                .collection("Appointments&Visits").document(appointmentid);


        appointment.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                AppointmentsAndVisits document = doc.toObject(AppointmentsAndVisits.class);
                appointment_date.setText(document.getAppointment_date());
                appointment_time.setText(document.getAppointment_time());
                subject_mat.setSelection(getPos(document.getSubject_matter())); //getPos(document.getSubject_matter())
                doctor_name.setText(document.getDoctor_name());
                clinic_name.setText(document.getClinic_name());
                street.setText(document.getStreet());
                area.setText(document.getArea());
                city.setText(document.getCity());
                state.setText(document.getState());
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("appointment_date",appointment_date.getText().toString());
                                            map.put("appointment_time",appointment_time.getText().toString());
                                            map.put("subject_matter",subject_mat.getSelectedItem().toString());
                                            map.put("doctor_name",doctor_name.getText().toString());
                                            map.put("clinic_name",clinic_name.getText().toString());
                                            map.put("street",street.getText().toString());
                                            map.put("area",area.getText().toString());
                                            map.put("city",city.getText().toString());
                                            map.put("state",state.getText().toString());

                                            appointment.update(map)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(AppointmentDetails.this,"Data Updated Successfully",Toast.LENGTH_SHORT).show();
                                                            Intent i =new Intent(AppointmentDetails.this,AppointmentsVisitList.class);
                                                            i.putExtra("key",familymemberid);
                                                            startActivity(i);
                                                        }
                                                    });

                                        }
                                    }

        );

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appointment.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AppointmentDetails.this,"Data Deleted Successfully",Toast.LENGTH_SHORT).show();
                                Intent i =new Intent(AppointmentDetails.this,AppointmentsVisitList.class);
                                i.putExtra("key",familymemberid);
                                startActivity(i);
                            }
                        });
            }
        });

    }

    public int getPos(String str)
    {
        int i;
        for (i=0;i<=sub_mat.length;i++)
        {
            if(sub_mat[i].equals(str))
            {
                return i;
            }
        }
        return 0;
    }
}
