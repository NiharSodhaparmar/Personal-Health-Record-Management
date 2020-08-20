package com.example.phrm;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        UserHomeUpperFragment userHomeUpperFragment = new UserHomeUpperFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.firstLayout,userHomeUpperFragment,userHomeUpperFragment.getTag()).commit();

        UserHomeGridFragment userHomeGridFragment = new UserHomeGridFragment();
        manager.beginTransaction().replace(R.id.secondLayout,userHomeGridFragment,userHomeGridFragment.getTag()).commit();

        BottomNavigationView bottomNav = findViewById(R.id.user_home_navigation);

        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragmnet = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_grid:
                            selectedFragmnet = new UserHomeGridFragment();
                            break;
                        case R.id.nav_overview:
                            selectedFragmnet = new Overview();
                            break;
                        case R.id.nav_timeline:
                            selectedFragmnet = new TimeLine();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.secondLayout,selectedFragmnet,selectedFragmnet.getTag()).commit();
                    return true;
                }
            };
}