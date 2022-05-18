package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AmistadesActivity extends AppCompatActivity {
    ListView lvSolicitudesAmistad, lvAmigos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amistades);

        lvSolicitudesAmistad = findViewById(R.id.lvSolicitudesAmistad);
        lvAmigos = findViewById(R.id.lvAmigos);

        String[] users = new String[]{"tu", "madre"};

        Integer imgs = R.drawable.error;

        AmigosList customCountryList = new AmigosList(this, users, imgs);
        lvSolicitudesAmistad.setAdapter(customCountryList);

        lvSolicitudesAmistad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(),"You Selected " + users[position-1] + " as Country",Toast.LENGTH_SHORT).show();
            }
        });

    }
}