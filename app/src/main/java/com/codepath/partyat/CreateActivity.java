package com.codepath.partyat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.codepath.partyat.Fragments.DatePickerFragment;
import com.codepath.partyat.Fragments.TimePickerFragment;
import com.google.android.material.timepicker.TimeFormat;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "CreateActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 53 ;
    public String mPhotoFileName = "photo.jpg";
    private File mPhotoFile;
    private Button mPublish;
    private Button mUpload;
    private ImageButton mBtnDate;
    private ImageButton mBtnTime;
    private EditText mEtTitle;
    private EditText mEtDetails;
    private EditText mDate;
    private EditText mTime;
    private ImageView mIvUploadPicture;
    private ImageView mBtnLocation;
    private EditText mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mPublish = findViewById(R.id.btnPublish);
        mUpload = findViewById(R.id.btnUpload);
        mEtDetails = findViewById(R.id.etDetails);
        mBtnDate = findViewById(R.id.btnDate);
        mBtnTime = findViewById(R.id.btnTime);
        mDate = findViewById(R.id.etDate);
        mTime = findViewById(R.id.etTime);
        mEtTitle = findViewById(R.id.etTitlte);
        mIvUploadPicture = findViewById(R.id.ivUploadImg);
        mLocation = findViewById(R.id.etLocation);
        mBtnLocation = findViewById(R.id.btnLocation);

        mBtnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        mPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postParty();
            }
        });

        mBtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { addLocation();}
        });
    }

    private void addLocation() {
//        PlacePicker.IntentBuilder builder =

    }


    private void postParty() {
        Event event = new Event();
        if (mEtTitle.getText().toString().isEmpty()) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (mEtDetails.getText().toString().isEmpty()) {
            Toast.makeText(this, "Details cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        event.setTitle(mEtTitle.getText().toString());
        event.setDetails(mEtDetails.getText().toString());
        // Set Location
        String[] geo = mLocation.getText().toString().split(",");
        ParseGeoPoint location = new ParseGeoPoint();
        location.setLatitude(Double.parseDouble(geo[0]));
        location.setLongitude(Double.parseDouble(geo[1]));
        event.setLocation(location);

        if (mPhotoFile != null) {
            event.setImage(new ParseFile(mPhotoFile));
        }
        event.setUser(ParseUser.getCurrentUser());
        event.setDate(mDate.getText().toString());
        event.setTime(mTime.getText().toString());
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving!", e);
                    Toast.makeText(CreateActivity.this, "Error while saving!", Toast.LENGTH_LONG).show();
                }
                Log.i(TAG, "Post saved successfully");
                mEtDetails.setText("");
                mEtTitle.setText("");
                mIvUploadPicture.setImageResource(0);
                mDate.setText("");
                mTime.setText("");
                finish();
            }
        });
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access

        mPhotoFile = getPhotoFileUri(mPhotoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider.jeanlaguerre", mPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }
    // Returns the File for a photo stored on disk given the fileName
    private File getPhotoFileUri(String mPhotoFileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + mPhotoFileName);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                mIvUploadPicture.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());
        mDate.setText(date);
    }

    private void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        mTime.setText(time);
    }
}
