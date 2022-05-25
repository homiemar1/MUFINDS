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
    private String[] cancionComun;
    private String[] perfilId;
    private Activity context;


    public AmigosList(Activity context, String[] userNames, String[] cancionComun, String[] perfilId) {
        super(context, R.layout.row_item, userNames);
        this.context = context;
        this.userNames = userNames;
        this.cancionComun = cancionComun;
        this.perfilId = perfilId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null) {
            row = inflater.inflate(R.layout.row_item, null, true);
        }

        TextView textViewUsuario = (TextView) row.findViewById(R.id.userName);
        TextView textViewCancionesComun = (TextView) row.findViewById(R.id.tvCancionesComunAmigosList);
        ImageView imageViewUsuario = (ImageView) row.findViewById(R.id.fotoPerfilAmigosList);

        textViewUsuario.setText(userNames[position]);
        textViewCancionesComun.setText(cancionComun[position]);
        //Picasso.with(context).load(Uri.parse(perfilId[position])).into(imageViewUsuario);
        imageViewUsuario.setImageResource(R.drawable.fotoperfil);
        return row;
    }
}
