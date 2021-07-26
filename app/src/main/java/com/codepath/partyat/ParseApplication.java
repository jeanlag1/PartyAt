package com.codepath.partyat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Register the subclasses
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Preference.class);
        // Initializes Parse SDK as soon as the application is created
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("FIvfvjqsDmEtXgKXZPld0Y6GoHhVFUrNEZAfY0U9")
                .clientKey("caSS82ZXnHZHcvc7aOKRQ7csDmHO8w34ed5mWrL6")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
