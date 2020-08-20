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

public class BodyMeasurementsDetails extends AppCompatActivity {

    EditText weight,height,chest,waist,hips;
    TextView measurement_date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button edit,delete;
    private  String familymemberid,measurementid;
    DocumentReference measurements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_measurements_details);

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("familymemberid");
        measurementid = extras.getString("measurement_key");

        weight = findViewById(R.id.weight1);
        height = findViewById(R.id.height1);
        chest = findViewById(R.id.chest1);
        waist = findViewById(R.id.waist1);
        hips = findViewById(R.id.hips1);
        measurement_date = findViewById(R.id.measurement_date1);
        edit = findViewById(R.id.btn_edit_body_measurements);
        delete = findViewById(R.id.btn_delete_body_measurements);

        measurement_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BodyMeasurementsDetails.this,
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

        measurements = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("familyMembers").document(familymemberid)
                .collection("BodyMeasurements").document(measurementid);

        measurements.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                BodyMeasurementModel document = doc.toObject(BodyMeasurementModel.class);
                measurement_date.setText(document.getMeasurement_date());
                weight.setText(document.getWeight());
                height.setText(document.getHeight());
                chest.setText(document.getChest());
                waist.setText(document.getWaist());
                hips.setText(document.getHips());
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docref = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("familyMembers").document(familymemberid)
                        .collection("BodyMeasurements").document(measurementid);

                Map<String, Object> map = new HashMap<>();
                map.put("measurement_date",measurement_date.getText().toString());
                map.put("weight",weight.getText().toString());
                map.put("height",height.getText().toString());
                map.put("chest",chest.getText().toString());
                map.put("waist",waist.getText().toString());
                map.put("hips",hips.getText().toString());

                docref.update(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(BodyMeasurementsDetails.this,"Data Updated Successfully",Toast.LENGTH_SHORT).show();
                                Intent i =new Intent(BodyMeasurementsDetails.this,BodyMeasurementsList.class);
                                i.putExtra("key",familymemberid);
                                startActivity(i);
                            }
                        });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                measurements.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(BodyMeasurementsDetails.this,"Data Deleted Successfully",Toast.LENGTH_SHORT).show();
                                Intent i =new Intent(BodyMeasurementsDetails.this,BodyMeasurementsList.class);
                                i.putExtra("key",familymemberid);
                                startActivity(i);
                            }
                        });
            }
        });
    }
}
