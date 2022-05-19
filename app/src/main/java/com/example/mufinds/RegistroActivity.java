package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.Serializable;

public class RegistroActivity extends AppCompatActivity {
    Spinner sp_genero;
    EditText etNombreRegistro, etApellidosRegistro, etEmailRegistro, etContraseñaRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombreRegistro = findViewById(R.id.etNombreRegistro);
        etApellidosRegistro = findViewById(R.id.etApellidosRegistro);
        etEmailRegistro = findViewById(R.id.etEmailRegistro);
        etContraseñaRegistro = findViewById(R.id.etContraseñaRegistro);

        sp_genero = findViewById(R.id.spGeneroRegistro);
        String[] datos = new String[] {"Mujer", "No binario", "Prefiero no contestar", "Hombre"};
        sp_genero.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,datos));

    }

    public void onClickSiguiente (View view) {
        String nombre = etNombreRegistro.getText().toString();
        String apellido = etApellidosRegistro.getText().toString();
        String email = etEmailRegistro.getText().toString();
        String pwd = etContraseñaRegistro.getText().toString();
        String genero = sp_genero.getSelectedItem().toString();
        Usuario u;

        if ("".equals(nombre)) {
            etNombreRegistro.setError("Introduce tu nombre");
        }
        else if ("".equals(apellido)) {
            etApellidosRegistro.setError("Introduce tus apellidos");
        }
        else if ("".equals(email)) {
            etEmailRegistro.setError("Introduce tu email");
        }
        else if ("".equals(pwd)) {
            etContraseñaRegistro.setError("Introduce tu contraseño");
        }
        //comprobar que los datos esten bien
        else {
            u = new Usuario();
            u.setApellido(apellido);
            u.setNombre(nombre);
            u.setEmail(email);
            u.setPassword(pwd);
            u.setGenero(genero);

            Intent intent = new Intent(this, RegistroActivity2.class);
            intent.putExtra("usuario",u);
            startActivity(intent);
        }

    }
}