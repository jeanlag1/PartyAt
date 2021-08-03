package com.codepath.partyat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.partyat.Fragments.FeedFragment;
import com.codepath.partyat.Fragments.MapFragment;
import com.codepath.partyat.Fragments.ProfileFragment;
import com.codepath.partyat.Fragments.WishlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setIcon(R.drawable.nav_logo_whiteout);
        MenuItem searchItem = menu.findItem(R.id.btnSearchParty);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if (item.getItemId() == R.id.btnCreate) {
            Intent i = new Intent(MainActivity.this, CreateActivity.class);
            startActivity(i);
        }

        if (item.getItemId() == R.id.btnSearchParty) {
            // TODO: Search party
        }

        if (item.getItemId() == R.id.btnSearchUser) {
            Intent intent = new Intent(this, UserSearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}