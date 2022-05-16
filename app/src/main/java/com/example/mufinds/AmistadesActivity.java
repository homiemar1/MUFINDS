package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AmistadesActivity extends AppCompatActivity {
    ListView lvSolicitudesAmistad, lvAmigos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amistades);

        lvSolicitudesAmistad = findViewById(R.id.lvSolicitudesAmistad);
        lvAmigos = findViewById(R.id.lvAmigos);

        ArrayList<String> al = new ArrayList<>();
        al.add("a");
        al.add("b");
        al.add("ac");
        al.add("ad");
        al.add("ae");
        al.add("af");
        al.add("ag");
        al.add("ah");
        al.add("ai");
        ArrayAdapter adapter =new ArrayAdapter(this,android.R.layout.simple_list_item_1,al);
        lvSolicitudesAmistad.setAdapter(adapter);
        lvAmigos.setAdapter(adapter);

    }
}