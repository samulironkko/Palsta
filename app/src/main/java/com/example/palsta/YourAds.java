package com.example.palsta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class YourAds extends AppCompatActivity {

    ArrayList<Ad> YourAdParts = new ArrayList<>();
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_ads);

        listView = findViewById(R.id.adList123);
        AdAdapter adAdapter = new AdAdapter(this, YourAdParts);
        listView.setAdapter(adAdapter);

    }
}
