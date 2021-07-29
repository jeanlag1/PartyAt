package com.codepath.partyat;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

@ParseClassName("Preferences")
public class Preference extends ParseObject implements Serializable {
    private static final long serialVersionUID = 5177222050535318633L;
    private Boolean mIsPrivate;
    private Boolean mIsWeekend;
    private Integer mMaxDistance;
    private Integer mPrice;
    private ParseUser mUser;

    // Getters

    public Boolean getIsPrivate() {
        return getBoolean("isPivate");
    }

    public Boolean getIsWeekend() {
        return getBoolean("isWeekend");
    }

    public Integer getMaxDistance() {
        return getInt("maxDistance");
    }

    public Integer getMaxPrice() {
        return getInt("maxPrice");
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    // Setters

    public void setIsPrivate(Boolean mIsPrivate) {
        put("isPrivate", mIsPrivate);
    }

    public void setIsWeekend(Boolean mIsWeekend) {
        put("isWeekend", mIsWeekend);
    }

    public void setMaxDistance(Integer mMaxDistance) {
        put("maxDistance", mMaxDistance);
    }

    public void setMaxPrice(Integer mPrice) {
        put("maxPrice", mPrice);
    }

    public void setUser(ParseUser mUser) {
        put("user",mUser);
    }

}
