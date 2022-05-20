package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MusicaActivity extends AppCompatActivity {
    private AmigosList amigosList;
    private ListView lvGestionarMusica;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);

        lvGestionarMusica = findViewById(R.id.lvGestionarMusica);
        String[] nombreCanciones = new String[] {"Tus Ojos", "xti-19"};
        String[] artistasCanciones = new String[] {"Sticky MA", "Slappy AV"};
        Integer[] fotosPerfil = new Integer[]{R.drawable.xti19, R.drawable.tusojos};
        amigosList = new AmigosList(this, nombreCanciones, artistasCanciones, fotosPerfil);
        lvGestionarMusica.setAdapter(amigosList);

        lvGestionarMusica.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                final int posicion=i;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MusicaActivity.this);
                dialogo1.setTitle("Eliminar canción");
                dialogo1.setMessage("La acción será permanente. ¿Quieres eliminar la canción seleccionada?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.show();
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