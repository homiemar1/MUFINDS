package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MusicaActivity extends AppCompatActivity {
    ListView lvGestionarMusica;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);

        lvGestionarMusica = findViewById(R.id.lvGestionarMusica);
        ArrayList<String> al = new ArrayList<>();
        al.add("a");
        al.add("b");
        al.add("c");
        ArrayAdapter adapter =new ArrayAdapter(this,android.R.layout.simple_list_item_1,al);
        lvGestionarMusica.setAdapter(adapter);
    }
}