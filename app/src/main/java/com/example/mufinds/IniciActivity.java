package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class IniciActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inici);
    }

    public void onClickRegistrar (View view) {
        //Intent intent = new Intent(this, );
        //intent.start();
    }

    public void onClickInicairSesion (View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}