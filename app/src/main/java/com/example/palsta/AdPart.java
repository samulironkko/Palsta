package com.example.palsta;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;

public class AdPart implements Serializable {
    //private String photo = new String();
    private String address = new String();
    private  String description = new String();
    private float price;
    private String pricedescription = new String();
    private String product = new String();

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private double distance;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    private LatLng latLng;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPricedescription() {
        return pricedescription;
    }

    public void setPricedescription(String pricedescription) {
        this.pricedescription = pricedescription;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public AdPart(){

    }


    public AdPart(){

    }
}