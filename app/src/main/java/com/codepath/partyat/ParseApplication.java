package com.codepath.partyat;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initializes Parse SDK as soon as the application is created
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("FIvfvjqsDmEtXgKXZPld0Y6GoHhVFUrNEZAfY0U9")
                .clientKey("caSS82ZXnHZHcvc7aOKRQ7csDmHO8w34ed5mWrL6")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
