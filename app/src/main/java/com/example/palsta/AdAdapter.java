package com.example.palsta;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class AdAdapter extends ArrayAdapter<AdPart> {

    static final int VIEW_TYPE_AD = 0;
    static final int VIEW_TYPE_COUNT = 1;

    public AdAdapter(Context context, ArrayList<AdPart> Adverts){
        super(context, 0, Adverts);
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        //AdPart base = getItem(position);
        //if(base instanceof AdPart)
            //return VIEW_TYPE_AD;
        //else
            return VIEW_TYPE_AD;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        AdPart base = getItem(position);

        if(convertView == null){
            int layoutId = R.layout.ad_list_item;
            convertView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        }
        return convertView;
    }

}