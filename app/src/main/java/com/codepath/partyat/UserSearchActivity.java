package com.codepath.partyat;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserSearchActivity extends AppCompatActivity implements DataManager.UsersQueryCallback {
    private RecyclerView mRvUsers;
    private UserSearchAdapter mAdapter;
    private List<ParseUser> mUsers;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        mDataManager = new DataManager(this, this);
        mRvUsers = findViewById(R.id.rvUsers);
        mUsers = new ArrayList<>();
        mAdapter = new UserSearchAdapter(this,mUsers, this);
        mRvUsers.setAdapter(mAdapter);
        mRvUsers.setLayoutManager(new LinearLayoutManager(this));

//        fetchUsers();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.user_search_menu, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setIcon(R.drawable.nav_logo_whiteout);
        MenuItem searchItem = menu.findItem(R.id.action_search_user);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                mDataManager.fetchUsers(UserSearchActivity.this,query);
//
//                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
//                // see https://code.google.com/p/android/issues/detail?id=24599
//                searchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // fetch new result
                mDataManager.fetchUsers(UserSearchActivity.this,newText);
                return true;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search_user) {
            // TODO: Search party
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUsersFetch(List<ParseUser> users) {
        mUsers.clear();
        mUsers.addAll(users);
        mAdapter.notifyDataSetChanged();
    }
}