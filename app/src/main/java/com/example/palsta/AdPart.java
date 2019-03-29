package com.example.palsta;

import android.view.View;
import android.widget.TextView;

public class AdPart {
    //private String photo = new String();
    private String address = new String();
    private  String description = new String();
    private float price;
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
