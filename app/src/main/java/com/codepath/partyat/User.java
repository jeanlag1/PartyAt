package com.codepath.partyat;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("User")
public class User extends ParseUser {
    private List<Event> mLiked;

    //getters

    public List<Event> getLiked() {
        return getList("liked");
    }

    //setters

    public void setLiked(List<Event> mLiked) {
        put("liked", mLiked);
    }
}
