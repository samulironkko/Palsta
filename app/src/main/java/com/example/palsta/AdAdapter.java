package com.example.palsta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdAdapter extends ArrayAdapter<Ad> {

    static final int VIEW_TYPE_AD = 0;
    static final int VIEW_TYPE_YOURAD = 1;
    static final int VIEW_TYPE_COUNT = 2;

    public AdAdapter(Context context, ArrayList<Ad> Adverts){
        super(context, 0, Adverts);
    }
    

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        Ad base = getItem(position);
        if(base instanceof YourPart)
            return VIEW_TYPE_YOURAD;
        else
            return VIEW_TYPE_AD;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        final Ad base = getItem(position);


        if(convertView == null){
            int layoutId = 0;
            if (getItemViewType(position) == VIEW_TYPE_AD){
                layoutId = R.layout.ad_list_item;
            }else{
                layoutId = R.layout.yourad_list_item;
            }
            convertView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        }


        if(getItemViewType(position) == VIEW_TYPE_YOURAD){
            ImageView remove = convertView.findViewById(R.id.removeIcon);
            ImageView edit = convertView.findViewById(R.id.editIcon);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), NewAdActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("EXTRA_LONGITUDE", base.getLatLng().getLongitude());
                    bundle.putDouble("EXTRA_LATITUDE", base.getLatLng().getLatitude());
                    bundle.putSerializable("EDITABLEAD", base);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("chad", Integer.toString(position));
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Remove ad")
                            .setMessage("Do you really want to remove this ad?")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO
                                    String adID = base.getAdid();
                                    //tietokanta remove ad.id



                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db = FirebaseFirestore.getInstance();


                                    Log.d("poisto", adID);
                                    Log.d("poisto", base.getAddress());
                                    Log.d("poisto", Float.toString(base.getPrice()));
                                    Log.d("poisto", base.getAdid());
                                    Log.d("poisto", "poisto");
                                    db.collection("ad").document(adID)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("poisto", "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("poisto", "Error deleting document", e);
                                                }
                                            });
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            })
                            //.setIcon(icon here)
                            .show();
                }
            });

        }
        //picture

        /*
        String imageUri = "https://i.imgur.com/tGbaZCY.jpg";
        ImageView ivBasicImage = (ImageView) findViewById(R.id.productImage);
        Picasso.with(context).load(imageUri).into(ivBasicImage);
*/
        ImageView image = convertView.findViewById(R.id.productImage);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/palsta-b6497.appspot.com/o/puhtaasti_tomaatti.jpg?alt=media&token=8d141497-ae6a-4ce4-80e9-9a4b1d3e94c6")
                .into(image);

        //name of product
        TextView name =convertView.findViewById(R.id.productNameText);
        name.setText(base.getProduct());

        //price
        TextView price =convertView.findViewById(R.id.priceText);
        price.setText(base.getPrice() + "€/" + base.getPricedescription());

        //address
        TextView address =convertView.findViewById(R.id.locationText);
        address.setText(base.getAddress());

        //description
        TextView description =convertView.findViewById(R.id.descriptionText);
        description.setText(base.getDescription());

        //distance
        TextView distance = convertView.findViewById(R.id.distanceText);
        distance.setText(String.format("%.1f", base.getDistance()/1000) + "km");

        return convertView;
    }

}
