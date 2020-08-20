package com.example.phrm;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Personal_Info extends AppCompatActivity  {
    Button image,save;
    ImageView imageView;
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private String familymemberid;
    public String imageUrl;
    EditText fname,lname,nationality,house_no,society_name,street,pincode,town,state,mobile,email;
    Map<String, String> updateMap;

    private FamilyMember fm;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private static final String TAG = "UploadActivity";

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal__info);

        fname = findViewById(R.id.first_name);
        lname = findViewById(R.id.last_name);
        nationality = findViewById(R.id.nationality);
        house_no = findViewById(R.id.house_no);
        society_name = findViewById(R.id.society);
        street = findViewById(R.id.street);
        pincode = findViewById(R.id.postal_code);
        town = findViewById(R.id.town);
        state = findViewById(R.id.state);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        save = findViewById(R.id.btn_save);

        final Spinner mySpinner = findViewById(R.id.gender);
        final Spinner mySpinner1 = findViewById(R.id.blood_type);
        final Spinner mySpinner2 = findViewById(R.id.relationship);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(Personal_Info.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.gender));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<>(Personal_Info.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.blood_type));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner1.setAdapter(myAdapter1);

        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<>(Personal_Info.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.relation));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter2);

        image =  findViewById(R.id.btn_image);
        imageView = findViewById(R.id.img);
        image.setOnClickListener(new  View.OnClickListener(){
            @Override
            public  void onClick(View v){
                openGallery();
            }
        });

        mDisplayDate =  findViewById(R.id.birth_date);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Personal_Info.this,
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
                mDisplayDate.setText(date);
            }
        };

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("key");


        final DocumentReference f = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        imageUrl = updateMap.get("imageUrl");
                        PersonalInfo personalInfo = new PersonalInfo(imageUrl,nationality.getText().toString(), house_no.getText().toString(), society_name.getText().toString(), street.getText().toString(), pincode.getText().toString(), town.getText().toString(), state.getText().toString(), mobile.getText().toString(), email.getText().toString(),mDisplayDate.getText().toString(),mySpinner.getSelectedItem().toString(),mySpinner1.getSelectedItem().toString(),mySpinner2.getSelectedItem().toString());
                        CollectionReference personalinfo = f.collection("PersonalInfo");
                        personalinfo.add(personalInfo);
                    }
                }, 40000);

                Toast.makeText(Personal_Info.this,"Data Uploaded Successfully",Toast.LENGTH_SHORT).show();
            }
        });

        f.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                fm = document.toObject(FamilyMember.class);
                fname.setText(fm.getFirst_name());
                lname.setText(fm.getLast_name());
            }
        });
    }

    private String getExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null && data.getData() != null ){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadFile() {
        //progressDialog();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://phrm-22e27.appspot.com/");
        StorageReference storageReference = firebaseStorage.getReference();
        final StorageReference riversRef = storageReference.child("Users/"+ familymemberid+"/images/"+imageUri.getLastPathSegment());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] data = baos.toByteArray();
        final UploadTask uploadTask = riversRef.putBytes(data);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.i("problem", task.getException().toString());
                                }

                                return riversRef.getDownloadUrl();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    imageUrl = downloadUri.toString();
                                    Log.d("seeThisUri", downloadUri.toString());


                                    updateMap = new HashMap();
                                    updateMap.put("imageUrl",imageUrl);

//                                    final DocumentReference f = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);
//                                    f.collection("PersonalInfo").add(updateMap);

                                }else {
                                    Log.i("wentWrong","downloadUri failure");
                                }
                            }
                        });
                    }
                });
    }
}

class PersonalInfo {

    private String imageUrl, nationality, house_no, society_name, street, pincode, town, state, mobile, email, mDisplayDate, mySpinner, mySpinner1, mySpinner2;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getmDisplayDate() {
        return mDisplayDate;
    }

    public void setmDisplayDate(String mDisplayDate) {
        this.mDisplayDate = mDisplayDate;
    }

    public String getMySpinner() {
        return mySpinner;
    }

    public void setMySpinner(String mySpinner) {
        this.mySpinner = mySpinner;
    }

    public String getMySpinner1() {
        return mySpinner1;
    }

    public void setMySpinner1(String mySpinner1) {
        this.mySpinner1 = mySpinner1;
    }

    public String getMySpinner2() {
        return mySpinner2;
    }

    public void setMySpinner2(String mySpinner2) {
        this.mySpinner2 = mySpinner2;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getSociety_name() {
        return society_name;
    }

    public void setSociety_name(String society_no) {
        this.society_name = society_no;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PersonalInfo(String imageUrl, String nationality, String house_no, String society_name, String street, String pincode, String town, String state, String mobile, String email, String mDisplayDate, String mySpinner, String mySpinner1, String mySpinner2) {
        this.imageUrl = imageUrl;
        this.nationality = nationality;
        this.house_no = house_no;
        this.society_name = society_name;
        this.street = street;
        this.pincode = pincode;
        this.town = town;
        this.state = state;
        this.mobile = mobile;
        this.email = email;
        this.mDisplayDate = mDisplayDate;
        this.mySpinner = mySpinner;
        this.mySpinner1 = mySpinner1;
        this.mySpinner2 = mySpinner2;
    }

    public PersonalInfo() {
    }
}
