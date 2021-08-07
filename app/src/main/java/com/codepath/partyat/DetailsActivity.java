package com.codepath.partyat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseException;

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
        mDate = findViewById(R.id.tvDateAndTimeD);
        mTitle = findViewById(R.id.tvTitleAnddPrice);
        mImage = findViewById(R.id.ivImageD);

        mEvent = (Event) getIntent().getSerializableExtra("event");

        try {
            mTitle.setText(mEvent.getTitle() + " - $" + mEvent.getPrice());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mDate.setText(mEvent.getDate() + " @ " + mEvent.getTime());
        mDetails.setText(mEvent.getDetails());
        Glide.with(this)
                .load(mEvent.getImage().getUrl())
                .transform(new RoundedCorners(100))
                .into(mImage);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataManager.addPartyToWishlist(mEvent);
            }
        });
    }


}