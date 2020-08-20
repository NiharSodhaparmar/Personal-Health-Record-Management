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

public class MedicalDocumentList extends AppCompatActivity {

    private ImageView add_document;
    private String familymemberid;
    private ListView documentslist;
    FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    ArrayList<String> listItem;
    ArrayList<String> keys;
    DocumentReference documents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_document_list);

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("key");

        add_document = findViewById(R.id.add_document);
        documentslist = findViewById(R.id.listview_documents);

        add_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MedicalDocumentList.this, Medical_Documents.class);
                i.putExtra("key",familymemberid);
                startActivity(i);
            }
        });

        //listview

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        listItem =new ArrayList();
        documents = db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);
        keys=new ArrayList();

        documents.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("tag", "DocumentSnapshot data: " + document.getData());
                        documents.collection("Medical Documents").get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("tag", document.getId() + " => " + document.getData());
                                                Medicalmodel a = document.toObject(Medicalmodel.class);
                                                keys.add(document.getId());

                                                listItem.add("Date : " + a.getDoc_date() + ", Medical Doc : " + a.getMedical_documents());
                                                Log.d("list item Size",Integer.toString(listItem.size()));
                                            }
                                            if(!listItem.isEmpty()){
                                                final ArrayAdapter<String> adapter = new ArrayAdapter<>(MedicalDocumentList.this, android.R.layout.simple_list_item_1,android.R.id.text1 ,listItem);
                                                documentslist.setAdapter(adapter);
                                                documentslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                        // TODO Auto-generated method stub
                                                        String document_key = keys.get(position);
                                                        Toast.makeText(MedicalDocumentList.this,document_key,Toast.LENGTH_SHORT).show();
                                                        //Intent i = new Intent(MedicalDocumentList.this,MedicalDocumentDetails.class);
                                                        //i.putExtra("familymemberid",familymemberid);
                                                        //i.putExtra("document_key",document_key);
                                                        //startActivity(i);
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
