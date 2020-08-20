package com.example.phrm;

import android.app.DatePickerDialog;
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

public class Immunizations extends AppCompatActivity {

    Button save;
    TextView immunizationDate;
    EditText vaccine_name,target_disease,administration_type,body_site,lot_number,exp_date;
    private DatePickerDialog.OnDateSetListener mDateSetListener,mDateSetListener1;
    DocumentReference f;
    String familymemberid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunizations);

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("key");

        immunizationDate = findViewById(R.id.immunization_date);
        vaccine_name = findViewById(R.id.vaccine_name);
        target_disease = findViewById(R.id.target_disease);
        administration_type = findViewById(R.id.administration_type);
        body_site = findViewById(R.id.body_site);
        lot_number = findViewById(R.id.lot_number);
        exp_date = findViewById(R.id.exp_date);
        save = findViewById(R.id.btn_save_immunizations);


        immunizationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Immunizations.this,
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
                immunizationDate.setText(date);
            }
        };

        f = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AllergyModel allergyModel = new AllergyModel(timestamp,personal_allergy.getText().toString(),startDate.getText().toString(),verificationDate.getText().toString(),reaction_severity.getSelectedItem().toString(),verification_status.getSelectedItem().toString(),criticality.getSelectedItem().toString(),symptoms.getText().toString());
                ImmunizationModel immobj = new ImmunizationModel(immunizationDate.getText().toString(),vaccine_name.getText().toString(),target_disease.getText().toString(),administration_type.getText().toString(),body_site.getText().toString(),lot_number.getText().toString(),exp_date.getText().toString(),new Timestamp(new Date()));
                CollectionReference imm = f.collection("Immunizations");
                imm.add(immobj);
                Toast.makeText(Immunizations.this,"Data Uploaded Successfully",Toast.LENGTH_SHORT).show();
                //Intent i =new Intent(Immunizations.this,ImmunizationsList.class);
                //i.putExtra("key",familymemberid);
                //startActivity(i);

            }
        });

    }
}

class  ImmunizationModel{

    private String immunizationDate,vaccine_name,target_disease,administration_type,body_site,lot_number,exp_date;
    Timestamp timestamp;

    public String getImmunizationDate() {
        return immunizationDate;
    }

    public void setImmunizationDate(String immunizationDate) {
        this.immunizationDate = immunizationDate;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public void setVaccine_name(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }

    public String getTarget_disease() {
        return target_disease;
    }

    public void setTarget_disease(String target_disease) {
        this.target_disease = target_disease;
    }

    public String getAdministration_type() {
        return administration_type;
    }

    public void setAdministration_type(String administration_type) {
        this.administration_type = administration_type;
    }

    public String getBody_site() {
        return body_site;
    }

    public void setBody_site(String body_site) {
        this.body_site = body_site;
    }

    public String getLot_number() {
        return lot_number;
    }

    public void setLot_number(String lot_number) {
        this.lot_number = lot_number;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public ImmunizationModel() {
    }

    public ImmunizationModel(String immunizationDate, String vaccine_name, String target_disease, String administration_type, String body_site, String lot_number, String exp_date, Timestamp timestamp) {
        this.immunizationDate = immunizationDate;
        this.vaccine_name = vaccine_name;
        this.target_disease = target_disease;
        this.administration_type = administration_type;
        this.body_site = body_site;
        this.lot_number = lot_number;
        this.exp_date = exp_date;
        this.timestamp = timestamp;
    }
}
