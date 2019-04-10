package com.example.palsta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.Point;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONObject;

import java.util.UUID;

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


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Query ad = db.collection("ad").whereEqualTo("UUID", sharedPreferences.getString("UUID", null));
        //final CollectionReference ad = db.collection("ad");
        //ad.get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot> () {
        //db.collection("ad")
        ad
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String address = document.get("address").toString();
                                String description = document.get("description").toString();
                                float price = document.getLong("price").floatValue();
                                String pricedescription = document.get("pricedescription").toString();
                                String product = document.get("product").toString();
                                String id = document.getId();

                                AdPart part = new AdPart();
                                part.setAddress(address);
                                part.setProduct(product);
                                part.setDescription(description);
                                part.setPricedescription(pricedescription);
                                part.setPrice(price);
                                //AdParts.add(part);

                                Log.d("jolo", document.getId() + " => " + document.getData());
                                //String location = document.get("location").toString();
                                //Log.d("dada", product);
                                //Log.d("dada", address);
                                //Log.d("dada", String.valueOf(price));
                                //Log.d("dada", pricedescription);
                                //Log.d("dada", description);
                                //Log.d("asdf", String.valueOf(geoLatitude));
                                //Log.d("asdf", String.valueOf(geoLongitude));
                            }
                        }
                    }
                });
    }
}






/*
                                final CollectionReference myads = db.collection("ad");
        //ad.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
        db.collection("ad")
                .get()
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());
        Query queries=myRef.child("ItemName").orderByChild("name").equals(UUID);
*/