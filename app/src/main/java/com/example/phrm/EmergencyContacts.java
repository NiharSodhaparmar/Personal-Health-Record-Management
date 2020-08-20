package com.example.phrm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class EmergencyContacts extends AppCompatActivity {
    EditText relative_name,relative_contact,doctor_name,doctor_contact;
    Button save;
    private String familymemberid;
    DocumentReference c;
    Timestamp timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("key");

        relative_name = findViewById(R.id.rel_name);
        relative_contact = findViewById(R.id.rel_contact_number);
        doctor_name = findViewById(R.id.doc_name);
        doctor_contact = findViewById(R.id.doc_contact_number);
        save = findViewById(R.id.btn_save_contact);
        timestamp=new Timestamp(new Date());

        c = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmergencyContactModel emergencyContactModel = new EmergencyContactModel(timestamp,relative_name.getText().toString(),relative_contact.getText().toString(),doctor_name.getText().toString(),doctor_contact.getText().toString());
                CollectionReference emergencyContacts = c.collection("EmergencyContacts");
                emergencyContacts.add(emergencyContactModel);
                Toast.makeText(EmergencyContacts.this,"Data Uploaded Successfully",Toast.LENGTH_SHORT).show();
//                Intent i =new Intent(EmergencyContacts.this,AllergiesList.class);
//                i.putExtra("key",familymemberid);
//                startActivity(i);

            }
        });
    }
}

class EmergencyContactModel
{
    String relative_name,relative_contact_number,doctor_name,doctor_contact_number;
    private Timestamp timestamp;

    public EmergencyContactModel() {
    }

    public EmergencyContactModel(Timestamp timestamp,String relative_name, String relative_contact_number, String doctor_name, String doctor_contact_number) {
        this.timestamp = timestamp;
        this.relative_name = relative_name;
        this.relative_contact_number = relative_contact_number;
        this.doctor_name = doctor_name;
        this.doctor_contact_number = doctor_contact_number;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getRelative_name() {
        return relative_name;
    }

    public void setRelative_name(String relative_name) {
        this.relative_name = relative_name;
    }

    public String getRelative_contact_number() {
        return relative_contact_number;
    }

    public void setRelative_contact_number(String relative_contact_number) {
        this.relative_contact_number = relative_contact_number;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_contact_number() {
        return doctor_contact_number;
    }

    public void setDoctor_contact_number(String doctor_contact_number) {
        this.doctor_contact_number = doctor_contact_number;
    }
}