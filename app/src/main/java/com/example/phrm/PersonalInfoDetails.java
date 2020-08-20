package com.example.phrm;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Map;

public class PersonalInfoDetails extends AppCompatActivity {

    //Button image,edit;
    ImageView imageView;
    TextView birthDate;
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private String familymemberid,personalInfoId;
    public String imageUrl;
    EditText fname,lname,nationality,house_no,society_name,street,pincode,town,state,mobile,email;
    Map<String, String> updateMap;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DocumentReference personalInfo,familyMember;


    private String[] gender = {
            "Gender",
            "Male",
            "Female"
    };

    private String[] relation = {
            "Select",
            "Yourself",
            "Son",
            "Wife",
            "Father",
            "Mother",
            "Brother",
            "Sister",
            "GrandFather",
            "GrandMother",
            "Uncle",
            "Aunty"
    };

    private  String[] blood_group = {
            "Blood Group",
            "A+",
            "A-",
            "AB+",
            "B+",
            "B-",
            "O+",
            "O-"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_details);

        fname = findViewById(R.id.first_name1);
        lname = findViewById(R.id.last_name1);
        nationality = findViewById(R.id.nationality1);
        house_no = findViewById(R.id.house_no1);
        society_name = findViewById(R.id.society1);
        street = findViewById(R.id.street1);
        pincode = findViewById(R.id.postal_code1);
        town = findViewById(R.id.town1);
        state = findViewById(R.id.state1);
        mobile = findViewById(R.id.mobile1);
        email = findViewById(R.id.email1);
        //edit = findViewById(R.id.btn_edit_info);
        //image =  findViewById(R.id.btn_image1);
        imageView = findViewById(R.id.img1);
        birthDate =  findViewById(R.id.birth_date1);

        final Spinner mySpinner = findViewById(R.id.gender1);
        final Spinner mySpinner1 = findViewById(R.id.blood_type1);
        final Spinner mySpinner2 = findViewById(R.id.relationship1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(PersonalInfoDetails.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.gender));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<>(PersonalInfoDetails.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.blood_type));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner1.setAdapter(myAdapter1);

        final ArrayAdapter<String> myAdapter2 = new ArrayAdapter<>(PersonalInfoDetails.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.relation));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter2);


        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        PersonalInfoDetails.this,
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
                String date = day + "/" + month + "/" + year;
                birthDate.setText(date);
            }
        };

        personalInfo = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("familyMembers").document("8TDsYPfaOQ6T6nPL0xrM")
                .collection("PersonalInfo").document("VJeJm9DBwBskqyLjU8xZ");

        personalInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                PersonalInfo document = doc.toObject(PersonalInfo.class);
                //fname.setText(document);
                //lname.setText(document);
                birthDate.setText(document.getmDisplayDate());
                mySpinner.setSelection(getPos(document.getMySpinner(),gender));
                mySpinner1.setSelection(getPos(document.getMySpinner1(),blood_group));
                mySpinner2.setSelection(getPos(document.getMySpinner2(),relation));
                nationality.setText(document.getNationality());
                house_no.setText(document.getHouse_no());
                society_name.setText(document.getSociety_name());
                street.setText(document.getStreet());
                pincode.setText(document.getPincode());
                town.setText(document.getTown());
                state.setText(document.getState());
                mobile.setText(document.getMobile());
                email.setText(document.getEmail());
                Picasso.get().load(document.getImageUrl()).into(imageView);
            }
        });

        familyMember = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("familyMembers").document("8TDsYPfaOQ6T6nPL0xrM");

        familyMember.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                FamilyMember document = doc.toObject(FamilyMember.class);
                fname.setText(document.getFirst_name());
                lname.setText(document.getLast_name());
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
