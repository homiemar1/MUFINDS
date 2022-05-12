package com.example.mufinds;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ActionBar actionBar = getSupportActionBar();
    }

    public void onClickEditarPerfil(View view) {
        /*if () {   LO DEL SWITCH

        }
        else {

        }*/
        Intent intent = new Intent(PrincipalActivity.this, EditarPerfilActivity.class);
        startActivity(intent);
    }
}