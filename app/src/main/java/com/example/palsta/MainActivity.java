package com.example.palsta;

import android.animation.LayoutTransition;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.palsta.R.id.gone;
import static com.example.palsta.R.id.invisible;
import static com.example.palsta.R.id.productImage;
import static com.example.palsta.R.id.productNameText;

public class MainActivity extends AppCompatActivity {

    ArrayList<AdPart> AdParts = new ArrayList<>();

    ListView listView = null;

    public static final String EXTRA_MESSAGE ="com.example.palsta.MESSAGE";

    int MY_PERMISSION_ACCESS_COURSE_LOCATION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    MY_PERMISSION_ACCESS_COURSE_LOCATION );
        }


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
                        float price = document.getLong("price").floatValue();
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
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                long arg3) {
                            Log.d("gona", arg1.toString());
                            if(arg1.findViewById(R.id.descriptionText).getVisibility() == View.GONE){
                                arg1.findViewById(R.id.descriptionText).setVisibility(View.VISIBLE);
                                /*ImageView imageView = (ImageView)arg1.findViewById(R.id.productImage);
                                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams)imageView.getLayoutParams();
                                params.width = -1;
                                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)imageView.getLayoutParams();
                                marginLayoutParams.leftMargin = 0;
                                marginLayoutParams.topMargin = 0;
                                marginLayoutParams.bottomMargin = 0;
                                ConstraintLayout.LayoutParams constraintLayout = (ConstraintLayout.LayoutParams) findViewById(R.id.con2).getLayoutParams();
                                constraintLayout.topToBottom = R.id.productImage;
                                arg1.findViewById(R.id.con2).requestLayout();*/
                            }else{
                                arg1.findViewById(R.id.descriptionText).setVisibility(View.GONE);
                            }
                            //Log.d("asdf","Items " +  AdParts.get(arg2).getStr() );
                            //Intent intent = new Intent(getBaseContext(), SingleAd.class);
                            //intent.putExtra(EXTRA_MESSAGE, AdParts.get(arg2));
                            //startActivity(intent);
                        }

                    });
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
