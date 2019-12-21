package com.example.shoppingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends BaseAdapter {
    private List<Upload> list;
    private Context applicationContext;
    private LayoutInflater inflater;
    private final String TAG = "ItemAdapter";

    public ItemAdapter(Context applicationContext ,List<Upload> list){
        this.applicationContext = applicationContext;
        this.list = list;
        inflater = LayoutInflater.from(applicationContext);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }
    @Override
    public int getCount(){
        return list.size();
    }
    @Override
    public Upload getItem(int position){
        return list.get(position);
    }

    //gets view of the item, displays price, description, item name
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view =inflater.inflate(R.layout.layout_of_list,null);
        ImageView imgview = view.findViewById(R.id.itemImage_listLayout);
        TextView price = view.findViewById(R.id.ItemPrice_listLayout);
        TextView name= view.findViewById(R.id.ItemName_listLayout);

        price.setText("$"+list.get(position).getPrice());
        name.setText(list.get(position).getName());
        //Piccasso adapts the image url from database to ImageView in UI
        Picasso.get()
                .load(list.get(position).getmImageUrl())
                .fit()
                .centerCrop()
                .tag(applicationContext)
                .into(imgview);

        Log.d(TAG,"img url"+list.get(position).getmImageUrl());



        return view;

    }


}
