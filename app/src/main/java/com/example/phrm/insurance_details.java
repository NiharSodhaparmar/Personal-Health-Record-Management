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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class insurance_details extends AppCompatActivity {

    Button btn_document,save;
    Intent myFileIntent;
    private String familymemberid;
    private DatePickerDialog.OnDateSetListener mDateSetListener,mDateSetListener2;

    EditText policy_no,company_name,policy_amt,premium_amt,short_description;
    TextView start_date,end_date;

    Button save_ins_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_details);

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("key");

        policy_no = findViewById(R.id.policy_no);
        company_name = findViewById(R.id.company_name);
        policy_amt = findViewById(R.id.policy_amount);
        premium_amt = findViewById(R.id.premium_amount);
        short_description = findViewById(R.id.short_description);

        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);


        save_ins_details = findViewById(R.id.btn_save_app);

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        insurance_details.this,
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
                start_date.setText(date);
            }
        };


        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        insurance_details.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });



        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                // Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                end_date.setText(date);
            }
        };


        final DocumentReference f = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);


        save_ins_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Insurancemodel ins_details = new Insurancemodel(policy_no.getText().toString(),company_name.getText().toString(),start_date.getText().toString(),end_date.getText().toString(),policy_amt.getText().toString(),premium_amt.getText().toString(),short_description.getText().toString(),new Timestamp(new Date()));
                CollectionReference ins_details_collections = f.collection("InsuranceDetails");
                ins_details_collections.add(ins_details);
                Toast.makeText(insurance_details.this,"Data Uploaded Successfully",Toast.LENGTH_SHORT).show();

            }
        });







    }
}




class Insurancemodel{
    private String policy_no,comapany_name, ins_start_date, ins_expiry_date, policy_amount, premium_amount, short_descrption;
    Timestamp timestamp;

    public String getPolicy_no() {
        return policy_no;
    }

    public void setPolicy_no(String policy_no) {
        this.policy_no = policy_no;
    }

    public String getComapany_name() {
        return comapany_name;
    }

    public void setComapany_name(String comapany_name) {
        this.comapany_name = comapany_name;
    }

    public String getIns_start_date() {
        return ins_start_date;
    }

    public void setIns_start_date(String ins_start_date) {
        this.ins_start_date = ins_start_date;
    }

    public String getIns_expiry_date() {
        return ins_expiry_date;
    }

    public void setIns_expiry_date(String ins_expiry_date) {
        this.ins_expiry_date = ins_expiry_date;
    }

    public String getPolicy_amount() {
        return policy_amount;
    }

    public void setPolicy_amount(String policy_amount) {
        this.policy_amount = policy_amount;
    }

    public String getPremium_amount() {
        return premium_amount;
    }

    public void setPremium_amount(String premium_amount) {
        this.premium_amount = premium_amount;
    }

    public String getShort_descrption() {
        return short_descrption;
    }

    public void setShort_descrption(String short_descrption) {
        this.short_descrption = short_descrption;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Insurancemodel(String policy_no, String comapany_name, String ins_start_date, String ins_expiry_date, String policy_amount, String premium_amount, String short_descrption, Timestamp timestamp) {
        this.policy_no = policy_no;
        this.comapany_name = comapany_name;
        this.ins_start_date = ins_start_date;
        this.ins_expiry_date = ins_expiry_date;
        this.policy_amount = policy_amount;
        this.premium_amount = premium_amount;
        this.short_descrption = short_descrption;
        this.timestamp = timestamp;
    }

    public Insurancemodel(String policy_no, String comapany_name, String ins_start_date, String ins_expiry_date, String policy_amount, String premium_amount, String short_descrption) {
        this.policy_no = policy_no;
        this.comapany_name = comapany_name;
        this.ins_start_date = ins_start_date;
        this.ins_expiry_date = ins_expiry_date;
        this.policy_amount = policy_amount;
        this.premium_amount = premium_amount;
        this.short_descrption = short_descrption;
    }

    public Insurancemodel() {
    }
}
