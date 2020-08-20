package com.example.phrm;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Medical_Documents extends AppCompatActivity {

    Map<String, String> updateMap;

    Button image,save;
    ImageView imageView;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    Timestamp tm ;
    String imageUrl;



    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String familymemberid;

    private EditText doc_date,short_description;
    private Spinner medical_docs;


    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical__documents);

        Bundle extras = getIntent().getExtras();
        familymemberid = extras.getString("key");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Images");

        tm = new Timestamp(new Date());

        doc_date = findViewById(R.id.doc_date);
        medical_docs = findViewById(R.id.medical_documents);
        short_description = findViewById(R.id.short_description);

        save = findViewById(R.id.btn_save_medical_doc);
        imageView = findViewById(R.id.img_medical_doc);
        image = findViewById(R.id.btn_medical_doc);


        final Spinner mySpinner = findViewById(R.id.medical_documents);
        final ArrayAdapter<String> myAdapter = new ArrayAdapter<>(Medical_Documents.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.medical_documents));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);


        image.setOnClickListener(new  View.OnClickListener(){
            @Override
            public  void onClick(View v){
                openGallery();
            }
        });

        doc_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Medical_Documents.this,
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
                doc_date.setText(date);
            }
        };

        final DocumentReference f = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        imageUrl = updateMap.get("imageUrl");
                        Medicalmodel md = new Medicalmodel(doc_date.getText().toString(),medical_docs.getSelectedItem().toString(),imageUrl, short_description.getText().toString(),new Timestamp(new Date()));
                        CollectionReference mymedical = f.collection("Medical Documents");
                        mymedical.add(md);
                    }
                }, 30000);

                Toast.makeText(Medical_Documents.this,"Data Uploaded Successfully",Toast.LENGTH_SHORT).show();
                Intent i =new Intent(Medical_Documents.this,MedicalDocumentList.class);
                i.putExtra("key",familymemberid);
                startActivity(i);
            }
        });


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

                                    //final DocumentReference f = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);
                                    //f.collection("PersonalInfo").add(updateMap);

                                }else {
                                    Log.i("wentWrong","downloadUri failure");
                                }
                            }
                        });
                    }
                });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);

    }

}
class Medicalmodel{

    private String doc_date,medical_documents,imageUrl,short_description;
    private Timestamp tm;

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public Timestamp getTm() {
        return tm;
    }

    public void setTm(Timestamp tm) {
        this.tm = tm;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDoc_date() {
        return doc_date;
    }

    public void setDoc_date(String doc_date) {
        this.doc_date = doc_date;
    }

    public String getMedical_documents() {
        return medical_documents;
    }

    public void setMedical_documents(String medical_documents) {
        this.medical_documents = medical_documents;
    }

    public Medicalmodel(String doc_date, String medical_documents, String imageUrl, String short_description, Timestamp tm) {
        this.doc_date = doc_date;
        this.medical_documents = medical_documents;
        this.imageUrl = imageUrl;
        this.short_description = short_description;
        this.tm = tm;
    }

    public Medicalmodel() {
    }
}

