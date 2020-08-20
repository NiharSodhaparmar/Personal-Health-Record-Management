package com.example.phrm;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragmnet = null;

                    switch (menuItem.getItemId()){

                        case R.id.nav_home:
                            selectedFragmnet = new HomeFragment();
                            break;
                        case R.id.nav_member:
                            selectedFragmnet = new FavoritesFragment();
                            break;
                        case R.id.nav_menu:
                            selectedFragmnet = new MenuFragment();
                            break;
                        case R.id.nav_notification:
                            selectedFragmnet = new NotificationsFragment();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragmnet).commit();
                    return true;
                }
            };
}
