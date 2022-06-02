package com.example.mufinds;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AmigosList extends ArrayAdapter {
    private List<String> userNames;
    private List<String> cancionComun;
    private List<String> perfilId;
    private Activity context;
    private FirebaseFirestore database;
    private DocumentReference docRef;


    public AmigosList(Activity context, List<String> userNames, List<String> cancionComun, List<String> perfilId) {
        super(context, R.layout.row_item, userNames);
        this.context = context;
        this.userNames = userNames;
        this.cancionComun = cancionComun;
        this.perfilId = perfilId;
        database = FirebaseFirestore.getInstance();
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


        textViewUsuario.setText(userNames.get(position));
        textViewCancionesComun.setText(cancionComun.get(position));
        if ("".equals(perfilId.get(position))) {
            imageViewUsuario.setImageResource(R.drawable.fotoperfil);
        }
        else {
            Picasso.with(context).load(Uri.parse(perfilId.get(position))).noFade().into(imageViewUsuario);
        }

        return row;
    }

    public void removeData(int position, String colectionPath, String nombreUsuario, String idCancion) {
        this.userNames.remove(position);
        this.cancionComun.remove(position);
        this.perfilId.remove(position);

        docRef = database.collection(colectionPath).document(nombreUsuario);
        Map<String,Object> eliminar = new HashMap<>();
        eliminar.put(idCancion, FieldValue.delete());
        docRef.update(eliminar);
        AmigosList.this.notifyDataSetChanged();
    }

}
