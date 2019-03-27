package com.example.palsta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

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
    protected void onResume() {
        super.onResume();

        AdAdapter adapter = new AdAdapter(this, AdParts);
        listView.setAdapter(adapter);
    }
}
