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

public class AllergyDetails extends AppCompatActivity {

    private TextView startDate,verificationDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener,mDateSetListener1;
    Spinner reaction_severity,verification_status,criticality;
    private String familymemberid,allergyid;
    EditText symptoms,personal_allergy;
    Button edit,delete;
    DocumentReference allergy;
    String[] reac_sev = {
            "Reaction Severity",
            "Minor",
            "Average",
            "Serious",
            "Very Serious"
    };
    String[] ver_status = {
            "Verification Status",
            "Confirmed",
            "Unconfirmed",
            "Refuted"
    };
    String[] critic = {
            "Criticality",
            "Low",
            "High",
            "Unable to assess"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy_details);

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("familymemberid");
        allergyid = extras.getString("allergy_key");

        symptoms = findViewById(R.id.symptoms1);
        personal_allergy = findViewById(R.id.personal_allergy1);
        startDate = findViewById(R.id.allergy_start1);
        reaction_severity = findViewById(R.id.reactions_severity1);
        verification_status = findViewById(R.id.verification_status1);
        verificationDate = findViewById(R.id.verification_date1);
        criticality = findViewById(R.id.crticality1);
        edit = findViewById(R.id.btn_edit_allergy);
        delete = findViewById(R.id.btn_delete_allergy);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AllergyDetails.this,
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

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(AllergyDetails.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.reactions_severity));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reaction_severity.setAdapter(myAdapter);

        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<>(AllergyDetails.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.verification_status));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        verification_status.setAdapter(myAdapter1);

        verificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AllergyDetails.this,
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

        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<>(AllergyDetails.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.criticality));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        criticality.setAdapter(myAdapter2);

        allergy = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("familyMembers").document(familymemberid)
                .collection("Allergies").document(allergyid);

        allergy.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                AllergyModel document = doc.toObject(AllergyModel.class);
                personal_allergy.setText(document.getPersonal_allergy());
                startDate.setText(document.getStartDate());
                reaction_severity.setSelection(getPos(document.getReaction_severity(),reac_sev));
                verification_status.setSelection(getPos(document.getVerification_status(),ver_status));
                verificationDate.setText(document.getVerificationDate());
                criticality.setSelection(getPos(document.getCriticality(),critic));
                symptoms.setText(document.getSymptoms());
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> map = new HashMap<>();
                map.put("personal_allergy",personal_allergy.getText().toString());
                map.put("startDate",startDate.getText().toString());
                map.put("reaction_severity",reaction_severity.getSelectedItem().toString());
                map.put("verification_status",verification_status.getSelectedItem().toString());
                map.put("verificationDate",verificationDate.getText().toString());
                map.put("criticality",criticality.getSelectedItem().toString());
                map.put("symptoms",symptoms.getText().toString());

                allergy.update(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AllergyDetails.this,"Data Updated Successfully",Toast.LENGTH_SHORT).show();
                                Intent i =new Intent(AllergyDetails.this,AllergiesList.class);
                                i.putExtra("key",familymemberid);
                                startActivity(i);
                            }
                        });

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allergy.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AllergyDetails.this,"Data Deleted Successfully",Toast.LENGTH_SHORT).show();
                                Intent i =new Intent(AllergyDetails.this,AllergiesList.class);
                                i.putExtra("key",familymemberid);
                                startActivity(i);
                            }
                        });
            }
        });
    }

    public int getPos(String str,String[] array)
    {
        int i;
        for (i=0;i<=array.length;i++)
        {
            if(array[i].equals(str))
            {
                return i;
            }
        }
        return 0;
    }
}
