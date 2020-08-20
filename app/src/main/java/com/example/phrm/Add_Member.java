package com.example.phrm;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class Add_Member extends Activity {
    String new_fname,new_lname;
    private EditText fname,lname;
    private TextView confirm;
    DocumentReference user;
    FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add__member);

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            int width = dm.widthPixels;
            int height = dm.widthPixels;

            getWindow().setLayout((int)(width*.9),(int)(height*.7));

            TextView cancelBtn = findViewById(R.id.cancel);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        fname = findViewById(R.id.new_fname);
        lname = findViewById(R.id.new_lname);
        confirm = findViewById(R.id.confirm);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userSendData();
                    finish();
                }
            });
    }
    private void userSendData(){
        new_fname = fname.getText().toString();
        new_lname = lname.getText().toString();
        Log.d("registration","UsersendData");
        db = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        user= db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        FamilyMember familyMember = new FamilyMember(new_fname,new_lname);
        Timestamp timestamp=new Timestamp(new Date());
        familyMember.setTimestamp(timestamp);
        CollectionReference familyMembers = user.collection("familyMembers");
        familyMembers.add(familyMember);
    }
}
