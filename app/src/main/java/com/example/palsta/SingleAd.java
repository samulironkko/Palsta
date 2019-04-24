package com.example.palsta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SingleAd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_ad);

        Intent intent = getIntent();
        AdPart ad = (AdPart) intent.getSerializableExtra("com.example.palsta.MESSAGE");
        ((TextView)findViewById(R.id.productNameSingle)).setText(ad.getProduct());
        ((TextView)findViewById(R.id.priceTextSingle)).setText(Double.toString(ad.getPrice()) + "€/" + ad.getPricedescription());
        ((TextView)findViewById(R.id.addressSingle)).setText(ad.getAddress());
        ((TextView)findViewById(R.id.descriptionSingle)).setText(ad.getDescription());
    }
}
