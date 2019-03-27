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

public class MainActivity extends AppCompatActivity {

    ArrayList<AdPart> AdParts = new ArrayList<>();

    ListView listView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.adList);
        for (int i = 0 ; i < 10 ; i++) {
            AdPart part = new AdPart();
            AdParts.add(part);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();

        DocumentReference user = db.collection("ad").document("bK6GeO4jwrNKFexlyHR0");
        user.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    StringBuilder fields = new StringBuilder("");


                    String product = doc.get("product").toString();
                    String address = doc.get("address").toString();
                    int price = doc.getLong("price").intValue();;
                    String pricedescription = doc.get("pricedescription").toString();
                    String description = doc.get("description").toString();

                    Log.d("asdf", product);
                    Log.d("asdf", address);
                    Log.d("asdf", String.valueOf(price));
                    Log.d("asdf", pricedescription);
                    Log.d("asdf", description);





                    fields.append(doc.get("product"));
                    fields.append(doc.get("price")).append("€/").append(doc.get("pricedescription"));

                    TextView productName = findViewById(R.id.productNameText);
                    //sijainti
                    //TextView price = findViewById(R.id.priceText);


                    productName.setText(fields.toString());
                    //price.setText(fields.toString());
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

        AdAdapter adapter = new AdAdapter(this, AdParts);
        listView.setAdapter(adapter);
    }
}
