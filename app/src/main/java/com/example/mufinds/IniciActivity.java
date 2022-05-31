package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class IniciActivity extends AppCompatActivity {
    ImageView ivLogoInicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inici);

        ivLogoInicio = findViewById(R.id.ivLogoInicio);
        ivLogoInicio.setImageResource(R.drawable.logomufinds);

    }

    public void onClickRegistrar (View view) {
        Intent intent = new Intent(IniciActivity.this, RegistroActivity.class);
        startActivity(intent);
    }

    public void onClickIniciarSesion (View view) {
        Intent intent = new Intent(IniciActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}