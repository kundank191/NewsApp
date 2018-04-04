package com.example.android.newsapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailedActivity extends AppCompatActivity {

    ImageView imageView;
    TextView title, subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        imageView = (ImageView) findViewById(R.id.fullImageView);
        title = (TextView) findViewById(R.id.fullTitleTextView);
        subTitle = (TextView) findViewById(R.id.fullSubTextView);

        Intent intent = getIntent();
        if(intent.hasExtra("url")){
            Picasso.with(this).load(intent.getStringExtra("url")).into(imageView);
        }
        if(intent.hasExtra("name")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setTransitionName(intent.getStringExtra("name"));
            }
        }
        if(intent.hasExtra("title")){
            String titleText = intent.getStringExtra("title");
                title.setText(titleText);
        }
        if(intent.hasExtra("subTitle")){
            String subTitleText = intent.getStringExtra("subTitle");
                subTitle.setText(subTitleText);
        }
        if(intent.hasExtra("imgUrl")){
            String url = intent.getStringExtra("imgUrl");
            //Picasso.with(this).load(url).into(imageView);
        }
    }
}
