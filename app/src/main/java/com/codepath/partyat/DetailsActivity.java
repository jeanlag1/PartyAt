package com.codepath.partyat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private TextView mDetails;
    private TextView mDate;
    private TextView mLocation;
    private TextView mTitle;
    private ImageView mImage;
    private Button mAdd;
    private Event mEvent;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = new DataManager(DetailsActivity.this, DetailsActivity.this);
        setContentView(R.layout.activity_details);
        mAdd = findViewById(R.id.btnAddToWishlist);
        mLocation = findViewById(R.id.tvLocationD);
        mDetails = findViewById(R.id.tvDetailsD);
        mDate = findViewById(R.id.tvDateD);
        mTitle = findViewById(R.id.tvTitleD);
        mImage = findViewById(R.id.ivImageD);

        mEvent = (Event) getIntent().getSerializableExtra("event");

        try {
            mTitle.setText(mEvent.getTitle());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mDetails.setText(mEvent.getDetails());
        Glide.with(this).load(mEvent.getImage().getUrl()).into(mImage);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataManager.addPartyToWishlist(mEvent);
            }
        });
    }


}