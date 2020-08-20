package com.example.phrm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    String first_name,last_name,email,password,mobile;
    private EditText fname, lname, r_email, r_password, phone;
    private Button register;
    private TextView login;
    DocumentReference user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    final String email = r_email.getText().toString().trim();
                    String password = r_password.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                               userSendData();


//                                UserProfile userProfile = new UserProfile(first_name,last_name,email,mobile);
//                                FirebaseFirestore db= FirebaseFirestore.getInstance();
//                                user= db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
//                                CollectionReference familyMembers = user.collection("familyMembers");
//                                familyMembers.add(userProfile);

                               Toast.makeText(RegisterActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                            }
                            else {
                                Toast.makeText(RegisterActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }

    private void setupUIViews() {
        fname = findViewById (R.id.fname);
        lname =  findViewById (R.id.lname);
        r_email =  findViewById (R.id.email);
        r_password =  findViewById (R.id.password);
        phone =  findViewById (R.id.phone);
        register =  findViewById (R.id.btn_register);
        login =  findViewById (R.id.login_link);
    }

    private boolean validate() {

        boolean result = false;

        first_name = fname.getText().toString();
        last_name = lname.getText().toString();
        email = r_email.getText().toString();
        password = r_password.getText().toString();
        mobile = phone.getText().toString();

        if (first_name.isEmpty() && last_name.isEmpty() && email.isEmpty() && password.isEmpty() && mobile.isEmpty()) {
            Toast.makeText(this,"Please Enter All The Details",Toast.LENGTH_SHORT).show();
        }
        else {
            result = true;
        }
        return result;
    }

    private void userSendData(){
        Log.d("registration","UsersendData");
        UserProfile userProfile = new UserProfile(first_name,last_name,email,mobile);

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        CollectionReference users=db.collection("users");
        users.document(firebaseAuth.getCurrentUser().getUid()).set(userProfile);
        user= db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        CollectionReference familyMembers = user.collection("familyMembers");
        FamilyMember fm=new FamilyMember(userProfile.first_name,userProfile.last_name);
        Timestamp timestamp=new Timestamp(new Date());
        fm.setTimestamp(timestamp);
        familyMembers.add(fm);

    }
}
