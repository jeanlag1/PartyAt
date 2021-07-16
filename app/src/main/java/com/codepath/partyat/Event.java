package com.codepath.partyat;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Event")
public class Event extends ParseObject {
    private String mTitle;
    private String mDetails;
    private ParseGeoPoint mLocation;
    private ParseUser mUser;
    private ParseFile mImage;

    // Getters

    public String getTitle() {
        return getString("title");
    }

    public String getDetails() {
        return getString("details");
    }

    public ParseGeoPoint geLocation() {
        return getParseGeoPoint("location");
    }

    public ParseUser geUser() {
        return getParseUser("user");
    }

    public ParseFile getImage() {
        return getParseFile("image");
    }

    // Setters

    public void setTitle(String mTitle) {
        put("title", mTitle);
    }

    public void setDetails(String mDetails) {
        put("details",mDetails);
    }

    public void setLocation(ParseGeoPoint mLocation) {
        put("location", mLocation);
    }

    public void setUser(ParseUser mUser) {
        put("user",mUser);
    }

    public void setImage(ParseFile mImage) {
        put("image", mImage);
    }
}
