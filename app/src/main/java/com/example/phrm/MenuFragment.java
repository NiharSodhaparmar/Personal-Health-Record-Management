package com.example.phrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MenuFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_menu, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationViewListener();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.logout: {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                Intent i = new Intent(getActivity(),MainActivity.class);
                startActivity(i);
                break;
            }

            case R.id.nav_info: {
                Intent i = new Intent(getActivity(),PersonalInfoDetails.class);
                startActivity(i);
                break;
            }
        }
        return false;
    }
    private void setNavigationViewListener() {
        NavigationView navigationView = getView().findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
    }
}
