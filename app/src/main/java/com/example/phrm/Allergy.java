package com.example.phrm;

import android.app.DatePickerDialog;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class Allergy extends AppCompatActivity {

    private TextView startDate,verificationDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener,mDateSetListener1;
    Spinner reaction_severity,verification_status,criticality;
    private String familymemberid;
    EditText symptoms,personal_allergy;
    Button save;
    DocumentReference f;
    Timestamp timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy);

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("key");

        symptoms = findViewById(R.id.symptoms);
        personal_allergy = findViewById(R.id.personal_allergy);
        save = findViewById(R.id.btn_save_allergy);
        timestamp=new Timestamp(new Date());

        //code for date

        startDate = findViewById(R.id.allergy_start);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Allergy.this,
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
                startDate.setText(date);
            }
        };


        reaction_severity = findViewById(R.id.reactions_severity);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(Allergy.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.reactions_severity));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reaction_severity.setAdapter(myAdapter);

        verification_status = findViewById(R.id.verification_status);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<>(Allergy.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.verification_status));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        verification_status.setAdapter(myAdapter1);

        verificationDate = findViewById(R.id.verification_date);
        verificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Allergy.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener1,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                // Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                verificationDate.setText(date);
            }
        };

        criticality = findViewById(R.id.crticality);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<>(Allergy.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.criticality));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        criticality.setAdapter(myAdapter2);

        f = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllergyModel allergyModel = new AllergyModel(timestamp,personal_allergy.getText().toString(),startDate.getText().toString(),verificationDate.getText().toString(),reaction_severity.getSelectedItem().toString(),verification_status.getSelectedItem().toString(),criticality.getSelectedItem().toString(),symptoms.getText().toString());
                CollectionReference allergy = f.collection("Allergies");
                allergy.add(allergyModel);
                Toast.makeText(Allergy.this,"Data Uploaded Successfully",Toast.LENGTH_SHORT).show();
                Intent i =new Intent(Allergy.this,AllergiesList.class);
                i.putExtra("key",familymemberid);
                startActivity(i);

            }
        });

    }
}

class  AllergyModel
{
    private  String startDate,verificationDate,reaction_severity,verification_status,criticality,symptoms,personal_allergy;
    private Timestamp timestamp;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getPersonal_allergy() {
        return personal_allergy;
    }

    public void setPersonal_allergy(String personal_allergy) {
        this.personal_allergy = personal_allergy;
    }

    public AllergyModel() {
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getReaction_severity() {
        return reaction_severity;
    }

    public void setReaction_severity(String reaction_severity) {
        this.reaction_severity = reaction_severity;
    }

    public String getVerification_status() {
        return verification_status;
    }

    public void setVerification_status(String verification_status) {
        this.verification_status = verification_status;
    }

    public String getCriticality() {
        return criticality;
    }

    public void setCriticality(String criticality) {
        this.criticality = criticality;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public AllergyModel(Timestamp timestamp,String personal_allergy,String startDate, String verificationDate, String reaction_severity, String verification_status, String criticality, String symptoms) {
        this.timestamp = timestamp;
        this.personal_allergy = personal_allergy;
        this.startDate = startDate;
        this.verificationDate = verificationDate;
        this.reaction_severity = reaction_severity;
        this.verification_status = verification_status;
        this.criticality = criticality;
        this.symptoms = symptoms;
    }
}
