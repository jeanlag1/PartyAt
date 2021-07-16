package com.codepath.partyat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.codepath.partyat.Fragments.FeedFragment;
import com.codepath.partyat.Fragments.MapFragment;
import com.codepath.partyat.Fragments.ProfileFragment;
import com.codepath.partyat.Fragments.WishlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    private Button mLogout;
    private BottomNavigationView mNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavBar = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // handle navigation selection
        mNavBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.profile_item:
                                fragment = new ProfileFragment();
                                break;
                            case R.id.map_item:
                                fragment = new MapFragment();
                                break;
                            case R.id.wishlist_item:
                                fragment = new WishlistFragment();
                                break;
                            default:
                                fragment = new FeedFragment();
                                break;
                        }
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                        return true;
                    }
                });
        // Set default selection
        mNavBar.setSelectedItemId(R.id.home_iem);

        mLogout = findViewById(R.id.btnLogout);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}