package com.codepath.partyat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView mUsername;
    private TextView mPassword;
    private Button mBtnLogin;
    private TextView mBtnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if there's a user already logged in
        if( ParseUser.getCurrentUser() != null ) {
            goToMainActivity();
        }

        mBtnLogin = findViewById(R.id.btnLogin);
        mBtnSignUp = findViewById(R.id.btnSignup);
        mUsername = findViewById(R.id.etUsername);
        mPassword = findViewById(R.id.etPassword);

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : fill out
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                signUpUser(username,password);
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                loginUser(username,password);
//                TODO: fill out
            }
        });

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("FIvfvjqsDmEtXgKXZPld0Y6GoHhVFUrNEZAfY0U9")
                .clientKey("caSS82ZXnHZHcvc7aOKRQ7csDmHO8w34ed5mWrL6")
                .server("https://parseapi.back4app.com")
                .build()
        );

    }

    private void signUpUser(String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(e -> {
            if (e == null) {
                // Hooray! Let them use the app now.
                Toast.makeText(this, "Successfully signed up", Toast.LENGTH_SHORT).show();
//                goToMainActivity();
                goToPreferences();
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToPreferences() {
        Intent i = new Intent(this, PreferencesActivity.class);
        startActivity(i);
//        finish();
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (user != null) {
                // Hooray! The user is logged in.
                Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                goToMainActivity();
            } else {
                // Login failed. Look at the ParseException to see what happened.
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setIcon(R.drawable.logo3);
        menu.findItem(R.id.btnSearchUser).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }
}