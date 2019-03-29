package com.example.palsta;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.palsta.R.id.productNameText;

public class MainActivity extends AppCompatActivity {

    ArrayList<AdPart> AdParts = new ArrayList<>();

    ListView listView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.adList);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();

        final CollectionReference ad = db.collection("ad");
        //ad.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
        db.collection("ad")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String address = document.get("address").toString();
                        String description = document.get("description").toString();
                        int price = document.getLong("price").intValue();
                        String pricedescription = document.get("pricedescription").toString();
                        String product = document.get("product").toString();
                        //String location = document.get("location").toString();
                        Log.d("asdf", product);
                        Log.d("asdf", address);
                        Log.d("asdf", String.valueOf(price));
                        Log.d("asdf", pricedescription);
                        Log.d("asdf", description);

                        AdPart part = new AdPart(product, address, price, pricedescription, description);
                        AdParts.add(part);

                        //TextView productTextView = findViewById(productNameText);
                        //productTextView.setText(product);

                        Log.d("asdf", document.getId() + " => " + document.getData());

                    }
                    QuerySnapshot doc = task.getResult();
                    //StringBuilder fields = new StringBuilder("");

                    /*
                    add new table

                    Map<String, Object> data1 = new HashMap<>();
                    data1.put("product", "Kurkku");
                    data1.put("address", "Jousimiehentie 2");
                    data1.put("price", 2);
                    data1.put("pricedescription", "kg");
                    data1.put("description", "kasvihuoneessa kasvatettuja kurkkuja");
                    ad.document("Ilmoitus5").set(data1);

                    */

                    //TextView productName = findViewById(R.id.productNameText);
                    //TextView addressField = findViewById(R.id.locationText);


/*
                    productName.append(product);
                    addressField.append(address);
/*
                    Log.d("asdf", product);
                    Log.d("asdf", address);
                    Log.d("asdf", String.valueOf(price));
                    Log.d("asdf", pricedescription);
                    Log.d("asdf", description);
                    Log.d("asdf", location);
*/
                    //fields.append(product + "\n");
                    //fields.append(price).append("€/").append(pricedescription + "\n");
                    //fields.append(address);

                    //sijainti
                    //TextView price = findViewById(R.id.priceText);

                    //productName.setText(fields.toString());
                    //price.setText(fields.toString());
                    AdAdapter adapter = new AdAdapter(MainActivity.this, AdParts);
                    listView.setAdapter(adapter);
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


/*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        FirebaseStorage storage = FirebaseStorage.getInstance();

        db = FirebaseApp.getInstance();

        DatabaseReference = FirebaseDatabase.getInstance().getReference().child("singleMessage").push();
*/




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.newAd){
            Intent intent = new Intent(this, NewAdActivity.class);
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.yourAds){
            Intent intent = new Intent(this, YourAds.class);
            startActivity(intent);
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
