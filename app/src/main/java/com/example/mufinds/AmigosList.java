package com.example.mufinds;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AmigosList extends ArrayAdapter {
    private String[] userNames;
    private Integer imageid;
    private Activity context;

    public AmigosList(Activity context, String[] countryNames, Integer imageid) {
        super(context, R.layout.activity_amistades, countryNames);
        this.context = context;
        this.userNames = countryNames;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.row_item, null, true);
        TextView textViewCountry = (TextView) row.findViewById(R.id.userName);
        ImageView imageFlag = (ImageView) row.findViewById(R.id.imgError);

        textViewCountry.setText(userNames[position]);
        imageFlag.setImageResource(imageid);
        return row;
    }
}
