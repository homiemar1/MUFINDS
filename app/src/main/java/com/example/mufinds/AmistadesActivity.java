package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AmistadesActivity extends AppCompatActivity {
    private AmigosList amigosList;
    private ListView lvSolicitudesAmistad, lvAmigos;
    private String[] users;
    String[] cancionesEnComun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amistades);

        users = new String[]{"enMic", "tu_xulo_99", "tu_jenny_fav"};
        cancionesEnComun = new String[]{"20 canciones en comun", "2 canciones en comun", "0 canciones en comun"};

        lvSolicitudesAmistad = findViewById(R.id.lvSolicitudesAmistad);

        Integer fotoPerfil = R.drawable.fotoperfil;

        //Solicitudes de amistad
        amigosList = new AmigosList(this, users, cancionesEnComun, fotoPerfil);
        lvSolicitudesAmistad.setAdapter(amigosList);
        lvSolicitudesAmistad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //añadir usuario
                AlertDialog.Builder alert = new AlertDialog.Builder(AmistadesActivity.this);
                alert.setTitle("Añadir amistad");
                alert.setMessage("Estas segur@ de que quieres añadir a este usuario?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alert.show();
                                users = removeElement(users, position);
                                cancionesEnComun = removeElement(cancionesEnComun, position);
                                amigosList.remove(position);
                                amigosList.notifyDataSetChanged();
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

            }
        });

        //Amigos actuales (hace falta cambiar las canciones en comun por una descripcion)
        amigosList = new AmigosList(this, users, cancionesEnComun, fotoPerfil);
        lvAmigos = findViewById(R.id.lvAmigos);
        lvAmigos.setAdapter(amigosList);
        lvAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //eliminar amigo
                AlertDialog.Builder alert = new AlertDialog.Builder(AmistadesActivity.this);
                alert.setTitle("Eliminar amistad");
                alert.setMessage("Estas segur@ de que quieres eliminar de tus contactos a este usuario? Esta acción será permanente")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                alert.show();
            }
        });
    }

    // remove element method
    public String[] removeElement(String[] arrGiven, int index)
    {
        // if empty
        if(arrGiven == null || index < 0 || index >= arrGiven.length)
        {
            return arrGiven;
        }
        // creating another array one less than initial array
        String[] newArray = new String[arrGiven.length - 1];
        // copying elements except index
        for(int a = 0, b = 0; a < arrGiven.length; a++)
        {
            if(a == index)
            {
                continue;
            }
            newArray[b++] = arrGiven[a];
        }
        return newArray;
    }
}