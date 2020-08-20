package com.example.phrm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AppointmentsVisitList extends AppCompatActivity {

    private ImageView add_appointment;
    private String familymemberid;
    private ListView appointmentslist;
    FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    ArrayList<String> listItem;
    ArrayList<String> keys;
    DocumentReference appointments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_visit_list);

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("key");

        add_appointment = findViewById(R.id.add_appointment);
        appointmentslist = findViewById(R.id.listview_appointment);

        add_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppointmentsVisitList.this, Appointments_Visits.class);
//                ArrayList<String> keys1 = new ArrayList<>();
//                keys1.add(familymemberid);
//
//                keys1.add("01");
                //i.putExtra("appointment_key","01");
                i.putExtra("key",familymemberid);
                startActivity(i);
            }
        });

        //list view

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        listItem =new ArrayList();
        appointments = db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);
        keys=new ArrayList();

        appointments.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("tag", "DocumentSnapshot data: " + document.getData());
                        appointments.collection("Appointments&Visits").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("tag", document.getId() + " => " + document.getData());
                                                AppointmentsAndVisits a = document.toObject(AppointmentsAndVisits.class);
                                                keys.add(document.getId());

                                                listItem.add("Date : " + a.getAppointment_date() + ", Time : " + a.getAppointment_time());
                                                Log.d("list item Size",Integer.toString(listItem.size()));
                                            }
                                            if(!listItem.isEmpty()){
                                                final ArrayAdapter<String> adapter = new ArrayAdapter<>(AppointmentsVisitList.this, android.R.layout.simple_list_item_1,android.R.id.text1 ,listItem);
                                                appointmentslist.setAdapter(adapter);
                                                appointmentslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                        // TODO Auto-generated method stub
                                                        String appointment_key = keys.get(position);
                                                        Toast.makeText(AppointmentsVisitList.this,appointment_key,Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(AppointmentsVisitList.this,AppointmentDetails.class);
                                                        //i.putExtra("key","01");
//                                                        ArrayList<String> keys1 = new ArrayList<>();
//                                                        keys1.add("01");
//                                                        keys1.add(appointment_key);
                                                        i.putExtra("familymemberid",familymemberid);
                                                        i.putExtra("appointment_key",appointment_key);
                                                        startActivity(i);
                                                    }
                                                });
                                            }


                                        } else {
                                            Log.d("tag", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                    } else {
                        Log.d("tag", "No such document");
                    }
                } else {
                    Log.d("tag", "get failed with ", task.getException());
                }
            }
        });
    }


    }



