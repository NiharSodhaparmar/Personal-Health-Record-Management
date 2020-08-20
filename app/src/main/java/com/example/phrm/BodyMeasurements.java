package com.example.phrm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class BodyMeasurements extends AppCompatActivity {

    EditText weight,height,chest,waist,hips;
    TextView measurement_date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button save;
    DocumentReference f;
    private String familymemberid;
    Timestamp timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_measurements);

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("key");

        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        chest = findViewById(R.id.chest);
        waist = findViewById(R.id.waist);
        hips = findViewById(R.id.hips);
        measurement_date = findViewById(R.id.measurement_date);
        save = findViewById(R.id.btn_save_body_measurements);
        timestamp=new Timestamp(new Date());

        measurement_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BodyMeasurements.this,
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
                measurement_date.setText(date);
            }
        };

        f = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BodyMeasurementModel bodyMeasurementModel = new BodyMeasurementModel(measurement_date.getText().toString(),weight.getText().toString(),height.getText().toString(),chest.getText().toString(),waist.getText().toString(),hips.getText().toString(),timestamp);
                CollectionReference bodyMeasurements = f.collection("BodyMeasurements");
                bodyMeasurements.add(bodyMeasurementModel);
                Toast.makeText(BodyMeasurements.this,"Data Uploaded Successfully",Toast.LENGTH_SHORT).show();
                Intent i =new Intent(BodyMeasurements.this,BodyMeasurementsList.class);
                i.putExtra("key",familymemberid);
                startActivity(i);
            }
        });
    }
}

class BodyMeasurementModel
{
    private  String measurement_date,weight,height,chest,waist,hips;
    private Timestamp timestamp;

    public BodyMeasurementModel(String measurement_date, String weight, String height, String chest, String waist, String hips, Timestamp timestamp) {
        this.measurement_date = measurement_date;
        this.weight = weight;
        this.height = height;
        this.chest = chest;
        this.waist = waist;
        this.hips = hips;
        this.timestamp = timestamp;
    }

    public BodyMeasurementModel() {
    }

    public String getMeasurement_date() {
        return measurement_date;
    }

    public void setMeasurement_date(String measurement_date) {
        this.measurement_date = measurement_date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getHips() {
        return hips;
    }

    public void setHips(String hips) {
        this.hips = hips;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
