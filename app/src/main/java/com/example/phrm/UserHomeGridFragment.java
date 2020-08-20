package com.example.phrm;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserHomeGridFragment extends Fragment {
    private String familymemberid;

    GridView gridView;
    String[] values = {
            "Personal Info",
            "Appointments & Visits",
            "Allergies",
            "Medical Documents",
            "Medical History",
            "Blood Report & Other Tests",
            "Emergency Contacts",
            "Life Habits & Risks ",
            "Body Measurements",
            "Immunizations",
            "Drugs",
            "Insurance Details"
    };

    int[] images = {
            R.mipmap.ic_admindata_foreground,
            R.mipmap.ic_appointments_foreground,
            R.mipmap.ic_allergies_foreground,
            R.mipmap.ic_documents_foreground,
            R.mipmap.ic_medicalhistory_foreground,
            R.mipmap.ic_test_foreground,
            R.mipmap.ic_contact_foreground,
            R.mipmap.ic_risk_foreground,
            R.mipmap.ic_body_foreground,
            R.mipmap.ic_immunization_foreground,
            R.mipmap.ic_drugs_foreground,
            R.mipmap.ic_log_foreground,
    };


    public UserHomeGridFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        gridView = getActivity().findViewById(R.id.gridView1);
//        CustomGridAdapter gridAdapter = new CustomGridAdapter(this.getActivity(), values, images);
//        gridView.setAdapter(gridAdapter);
        return inflater.inflate(R.layout.fragment_user_home_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //for key
        Bundle extras =getActivity().getIntent().getExtras();
        familymemberid = extras.getString("key");

        gridView = getActivity().findViewById(R.id.gridView1);
        CustomGridAdapter gridAdapter = new CustomGridAdapter(this.getActivity(), values, images);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(values[position] == "Personal Info"){
                    Intent intent = new Intent(getContext(),Personal_Info.class);
                    intent.putExtra("key",familymemberid);
                    startActivity(intent);
                }
                else if(values[position] == "Appointments & Visits"){
                    Intent intent = new Intent(getContext(),AppointmentsVisitList.class);
                    intent.putExtra("key",familymemberid);
                    startActivity(intent);
                }
                else if(values[position] == "Allergies"){
                    Intent intent = new Intent(getContext(),AllergiesList.class);
                    intent.putExtra("key",familymemberid);
                    startActivity(intent);
                }
                else if(values[position] == "Emergency Contacts"){
                    Intent intent = new Intent(getContext(),EmergencyContacts.class);
                    intent.putExtra("key",familymemberid);
                    startActivity(intent);
                }
                else if(values[position] == "Body Measurements"){
                    Intent intent = new Intent(getContext(),BodyMeasurementsList.class);
                    intent.putExtra("key",familymemberid);
                    startActivity(intent);
                }
                else if(values[position] == "Insurance Details"){
                    Intent intent = new Intent(getContext(),insurance_details.class);
                    intent.putExtra("key",familymemberid);
                    startActivity(intent);
                }
                else if(values[position] == "Medical Documents"){
                    Intent intent = new Intent(getContext(),MedicalDocumentList.class);
                    intent.putExtra("key",familymemberid);
                    startActivity(intent);
                }
                else if(values[position] == "Immunizations"){
                    Intent intent = new Intent(getContext(),Immunizations.class);
                    intent.putExtra("key",familymemberid);
                    startActivity(intent);
                }
                else if(values[position] == "Drugs"){
//                    Intent intent = new Intent(getContext(),Drugs.class);
//                    intent.putExtra("key",familymemberid);
//                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(),values[position],Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
