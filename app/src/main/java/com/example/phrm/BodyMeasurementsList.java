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

public class BodyMeasurementsList extends AppCompatActivity {

    private ImageView add_mesurements;
    private String familymemberid;
    private ListView measurementslist;
    FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    ArrayList<String> listItem;
    ArrayList<String> keys;
    DocumentReference measurements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_measurements_list);

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("key");

        add_mesurements = findViewById(R.id.add_measurements);
        measurementslist = findViewById(R.id.listview_measurements);

        add_mesurements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BodyMeasurementsList.this, BodyMeasurements.class);
                i.putExtra("key",familymemberid);
                startActivity(i);
            }
        });

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        listItem =new ArrayList();
        measurements = db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);
        keys=new ArrayList();

        measurements.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("tag", "DocumentSnapshot data: " + document.getData());
                        measurements.collection("BodyMeasurements").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("tag", document.getId() + " => " + document.getData());
                                                BodyMeasurementModel a = document.toObject(BodyMeasurementModel.class);
                                                keys.add(document.getId());

                                                listItem.add("Measurement Date : " + a.getMeasurement_date() );
                                                Log.d("list item Size",Integer.toString(listItem.size()));
                                            }
                                            if(!listItem.isEmpty()){
                                                final ArrayAdapter<String> adapter = new ArrayAdapter<>(BodyMeasurementsList.this, android.R.layout.simple_list_item_1,android.R.id.text1 ,listItem);
                                                measurementslist.setAdapter(adapter);
                                                measurementslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                        // TODO Auto-generated method stub
                                                        String measurement_key = keys.get(position);
                                                        Toast.makeText(BodyMeasurementsList.this,measurement_key,Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(BodyMeasurementsList.this,BodyMeasurementsDetails.class);
                                                        i.putExtra("familymemberid",familymemberid);
                                                        i.putExtra("measurement_key",measurement_key);
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
