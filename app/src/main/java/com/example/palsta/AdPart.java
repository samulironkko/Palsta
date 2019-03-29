package com.example.palsta;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

public class AdPart implements Serializable {
    //private String photo = new String();
    private String address = new String();
    private  String description = new String();
    private float price;

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

    private String pricedescription = new String();
    private String product = new String();


    public AdPart(String product1, String address1, float price1, String pricedescription1, String description1){
        //photo = photo1;
        product = product1;
        address = address1;
        price = price1;
        pricedescription = pricedescription1;
        description = description1;
    }
}