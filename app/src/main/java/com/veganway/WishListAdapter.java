package com.veganway;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class WishListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> array;
    LayoutInflater inflater;

    //constructor
    public WishListAdapter(Context context, ArrayList<String> newsArrayList) {
        super();
        this.context = context;
        this.array = newsArrayList;
        this.inflater = ((Activity) context).getLayoutInflater();

    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public String getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(context, R.layout.items_lista_wishes, null);

        TextView titulo = (TextView) convertView.findViewById(R.id.wish);

        titulo.setText(array.get(position));

        return convertView;
    }
}
