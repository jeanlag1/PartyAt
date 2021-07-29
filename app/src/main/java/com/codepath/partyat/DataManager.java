package com.codepath.partyat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

public class DataManager {

    private ParseUser mCurrentUser;

    public interface EventsQueryCallback{
        public void onEventsFetch(List<Event> events) throws ParseException;
    }
    private static final String TAG = DataManager.class.getSimpleName();
    private static final double SCORE_SCALE_FACTOR = 10000;
    private List<Event> mEvents;
    private Preference mUserPref;
    private Location mCurrentLocation;
    private Context mContext;

    public DataManager(Activity activity, Context context){
        getLocation(activity);
        mCurrentUser = ParseUser.getCurrentUser();
        mContext = context;
    }

    // Add bool shouldSort as param
    public void queryEvents(EventsQueryCallback cb) {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.include("user");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                Log.i(TAG, events.toString());
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting events", e);
                    return;
                }
                try {
                    cb.onEventsFetch(events);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            }
        });
    }

    /* This method returns a score describing how compatible
     *  a given event is to the current user and helps sorting
     *  the main feed.
     *  The higher the score, the more compatible the event is.
     */
    public double compatibilityScore(Event o1) {

        // retrieve preferences
        int max_distance = mUserPref.getMaxDistance();
        int maxPrice = mUserPref.getMaxPrice();
        boolean user_weekend_pref = mUserPref.getIsWeekend();
        boolean user_private_pref = mUserPref.getIsPrivate();

        double dist_to_event = computeDistToEvent(o1.getLocation());

        // factors
        double dist_factor = (max_distance - dist_to_event) / max_distance;
        double priceFactor = (maxPrice - o1.getPrice()) / maxPrice;
        int event_type = o1.getIsPrivate() ? 1 : 0;
        int event_time = o1.getIsWeekend() ? 1 : 0;
        int weekend_factor = user_weekend_pref ? event_time : 0;
        int private_factor = user_private_pref ? event_type : 0;

        double score = weekend_factor + private_factor +  dist_factor + priceFactor;

        return SCORE_SCALE_FACTOR * score;
    }

    // This method computes the Euclidean distance between two location points
    private double computeDistToEvent(ParseGeoPoint o1) {
        return Math.sqrt(Math.pow(o1.getLatitude() - mCurrentLocation.getLatitude(), 2)
                + Math.pow(o1.getLongitude() - mCurrentLocation.getLongitude(), 2));
    }

    // Get User's Current location
    @SuppressWarnings({"MissingPermission"})
    @NeedsPermission({android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void getLocation(Activity activity) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener( new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("MapFragment", "Successfully got last GPS location");
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mCurrentLocation = location;
                        } else {
                            // For emulator
                            Location l = new Location("");
                            l.setLongitude(-73);
                            l.setLatitude(41);
                            mCurrentLocation = l;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MapFragment", "Error trying to get last GPS location",e);
                        e.printStackTrace();
                    }
                });;
    }

    public void queryPreferences(EventsQueryCallback cb) {
        ParseQuery<Preference> query = ParseQuery.getQuery(Preference.class);
        query.include("user");
        query.whereEqualTo("user", mCurrentUser);
        query.findInBackground(new FindCallback<Preference>() {
            @Override
            public void done(List<Preference> prefs, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting pref", e);
                    return;
                }
                mUserPref = prefs.get(0);
                queryEvents(cb);
            }
        });
    }

    public void addPartyToWishlist(Event mEvent) {
        // Create new list
        List<Event> liked;
        // fetch current array
        liked = new ArrayList<>();
        //odl list
        List<Event> old = ParseUser.getCurrentUser().getList("liked");
        if (old != null) {
            liked.addAll(old);
        }
        //Add party
        liked.add(mEvent);

        // Update list
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("liked", liked);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Toast.makeText(mContext, "Save Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
