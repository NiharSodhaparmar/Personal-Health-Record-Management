package com.example.phrm;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * A simple {@link Fragment} subclass.
 */
public class Overview extends Fragment {

    private TextView dob,gender,bloodgroup,nationality,relationship,address,mobile,email;
    private String familymemberid,full_address;
    private FamilyMember fm;
    public Overview() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dob = getView().findViewById(R.id.p_dob);
        gender = getView().findViewById(R.id.p_gender);
        bloodgroup = getView().findViewById(R.id.p_bloodgroup);
        nationality = getView().findViewById(R.id.p_nationality);
        relationship = getView().findViewById(R.id.p_relationship);
        address = getView().findViewById(R.id.p_address);
        mobile = getView().findViewById(R.id.p_mobile);
        email = getView().findViewById(R.id.p_email);

        Bundle extras =getActivity().getIntent().getExtras();
        familymemberid = extras.getString("key");

        final DocumentReference f = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("familyMembers").document(familymemberid);

        f.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("tag", "DocumentSnapshot data: " + document.getData());
                        fm=document.toObject(FamilyMember.class);
                        if(fm!=null) {
                            Log.d("Family Member","Data arrived");
                            f.collection("PersonalInfo").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("tag", document.getId() + " => " + document.getData());
                                                PersonalInfo info = document.toObject(PersonalInfo.class);
                                                dob.setText(info.getmDisplayDate());
                                                gender.setText(info.getMySpinner());
                                                bloodgroup.setText(info.getMySpinner1());
                                                relationship.setText(info.getMySpinner2());
                                                nationality.setText(info.getNationality());
                                                full_address = info.getHouse_no()+" "+info.getSociety_name()+"\n"+info.getStreet()+" "+info.getTown()+"\nPinCode : "+info.getPincode();
                                                address.setText(full_address);
                                                mobile.setText(info.getMobile());
                                                email.setText(info.getEmail());
                                            }
                                        }
                                    });
                        }
                    }
                    else {
                        Log.d("tag", "No such document");
                    }
                } else {
                    Log.d("tag", "get failed with ", task.getException());
                }
            }
        });
    }
}
