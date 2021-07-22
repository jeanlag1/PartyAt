package com.codepath.partyat;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

@ParseClassName("Event")
public class Event extends ParseObject implements Serializable {
    private static final long serialVersionUID = 5177222050535318633L;
    private String mTitle;
    private String mDetails;
    private String mDate;
    private String mTime;
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

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public ParseFile getImage() {
        return getParseFile("image");
    }

    public String getDate() {
        return getString("date");
    }

    public String getTime() {
        return getString("time");
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

    public void setDate(String mDate) {
         put("date", mDate);
    }

    public void setTime(String mTime) {
         put("time", mTime);
    }
}
