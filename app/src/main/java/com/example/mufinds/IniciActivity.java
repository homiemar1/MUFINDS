package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class IniciActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inici);

    }

    public void onClickRegistrar (View view) {
        Intent intent = new Intent(IniciActivity.this, RegistroActivity.class);
        startActivity(intent);
    }

    public void onClickIniciarSesion (View view) {
        Intent intent = new Intent(IniciActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}