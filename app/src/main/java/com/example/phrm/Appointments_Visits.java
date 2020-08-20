package com.example.phrm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class Appointments_Visits extends AppCompatActivity {


    private TextView appointment_date,appointment_time;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button btn_document,save;
    Intent myFileIntent;
    Spinner subject_matter;
    private String familymemberid;
    String amPm;
    ArrayList<String> keys;
    private int hours,minute;

    EditText doctor_name,clinic_name,street,area,city,state;

    private int notificationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments__visits);

        Bundle extras = getIntent().getExtras();
        //keys = extras.getStringArrayList("keys");
        familymemberid = extras.getString("key");
        //appointmentid = extras.getString("appointment_key");



        doctor_name = findViewById(R.id.doctor_name);
        clinic_name = findViewById(R.id.clinic_name);
        street = findViewById(R.id.street1);
        area = findViewById(R.id.area);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state1);
        save = findViewById(R.id.btn_save_app);

        appointment_date = findViewById(R.id.appointment_date);

        //Toast.makeText(Appointments_Visits.this,"app "+appointmentid,Toast.LENGTH_SHORT).show();
        //Toast.makeText(Appointments_Visits.this,"fam "+familymemberid,Toast.LENGTH_SHORT).show();


        appointment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Appointments_Visits.this,
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

        appointment_time = findViewById(R.id.appointment_time);
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

                timePickerDialog = new TimePickerDialog(Appointments_Visits.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        hours = hourOfDay;
                        minute = minutes;
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


        //spinner of subject MAtter


        subject_matter = findViewById(R.id.subject_matter);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(Appointments_Visits.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.subject_matter));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject_matter.setAdapter(myAdapter);

        //document

        btn_document = findViewById(R.id.btn_document);

        btn_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                startActivityForResult(myFileIntent,10);
            }
        });


        //database

        final DocumentReference f = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentsAndVisits appointmentsAndVisits = new AppointmentsAndVisits(appointment_date.getText().toString(),appointment_time.getText().toString(),subject_matter.getSelectedItem().toString(),doctor_name.getText().toString(),clinic_name.getText().toString(),street.getText().toString(),area.getText().toString(),city.getText().toString(),state.getText().toString());
                CollectionReference appointmentsandvisits = f.collection("Appointments&Visits");
                appointmentsandvisits.add(appointmentsAndVisits);
                Toast.makeText(Appointments_Visits.this,"Data Uploaded Successfully",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Appointments_Visits.this,AlarmReceiver.class);
                intent.putExtra("notificationId",notificationId);
                intent.putExtra("todo",doctor_name.getText().toString());
                intent.putExtra("app_name",R.string.app_name);

                PendingIntent alarmIntent = PendingIntent.getBroadcast(Appointments_Visits.this, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

                switch (view.getId()){
                    case R.id.btn_save_app:
                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.HOUR_OF_DAY,hours);
                        startTime.set(Calendar.MINUTE,minute);
                        startTime.set(Calendar.SECOND,0);
                        long alarmStartTime = startTime.getTimeInMillis();

                        alarm.set(AlarmManager.RTC_WAKEUP,alarmStartTime,alarmIntent);
                        Log.d("tag",startTime.toString());
                        Toast.makeText(Appointments_Visits.this,"Done!!",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });

//        Intent i =new Intent(Appointments_Visits.this,AppointmentsVisitList.class);
//        i.putExtra("key",familymemberid);
//        startActivity(i);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode)
        {
            case 10:
                if(resultCode == RESULT_OK)
                {
                    String path = data.getData().getPath();
                    Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

class  AppointmentsAndVisits
{
    private  String appointment_date,appointment_time,subject_matter,doctor_name,clinic_name,street,area,city,state;

    public AppointmentsAndVisits() {
    }

    public AppointmentsAndVisits(String appointment_date,String appointment_time, String subject_matter, String doctor_name, String clinic_name, String street, String area, String city, String state) {
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.subject_matter = subject_matter;
        this.doctor_name = doctor_name;
        this.clinic_name = clinic_name;
        this.street = street;
        this.area = area;
        this.city = city;
        this.state = state;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getSubject_matter() {
        return subject_matter;
    }

    public void setSubject_matter(String subject_matter) {
        this.subject_matter = subject_matter;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
