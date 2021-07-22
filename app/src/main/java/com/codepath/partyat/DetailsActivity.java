package com.codepath.partyat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class DetailsActivity extends AppCompatActivity {
    private TextView mDetails;
    private TextView mDate;
    private TextView mLocation;
    private TextView mTitle;
    private ImageView mImage;
    private Button mAdd;
    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mAdd = findViewById(R.id.btnAddToWishlist);
        mLocation = findViewById(R.id.tvLocationD);
        mDetails = findViewById(R.id.tvDetailsD);
        mDate = findViewById(R.id.tvDateD);
        mTitle = findViewById(R.id.tvTitleD);
        mImage = findViewById(R.id.ivImageD);

        mEvent = (Event) getIntent().getSerializableExtra("event");

        mTitle.setText(mEvent.getTitle());
        mDetails.setText(mEvent.getDetails());
        Glide.with(this).load(mEvent.getImage().getUrl()).into(mImage);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailsActivity.this, "Party added to wishlist", Toast.LENGTH_SHORT).show();
            }
        });
    }
}