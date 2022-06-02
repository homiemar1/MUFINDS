package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class RecuperarContrasenaActivity extends AppCompatActivity {
    EditText etNombreUsuarioRecuperarContraseña, etNombreRecuperarContraseña, etApellidosRecuperarContraseña, etdFechaNacimientoRecuperarContraseña;
    String nombreUsuario, nombre, apellidos, fecha = "";
    FirebaseFirestore database;
    int any, mes, dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);
        database = FirebaseFirestore.getInstance();

        etNombreUsuarioRecuperarContraseña = findViewById(R.id.etNombreUsuarioRecuperarContraseña);
        etNombreRecuperarContraseña = findViewById(R.id.etNombreRecuperarContraseña);
        etApellidosRecuperarContraseña = findViewById(R.id.etApellidosRecuperarContraseña);
        etdFechaNacimientoRecuperarContraseña = findViewById(R.id.etdFechaNacimientoRecuperarContraseña);

        Calendar c = Calendar.getInstance();
        any = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONDAY);
        dia = c.get(Calendar.DAY_OF_MONTH);
    }

    public void onClickRecuperar(View view) {
        nombreUsuario = etNombreUsuarioRecuperarContraseña.getText().toString();
        nombre = etNombreRecuperarContraseña.getText().toString();
        apellidos = etApellidosRecuperarContraseña.getText().toString();


        if ("".equals(nombreUsuario)) {
            etNombreUsuarioRecuperarContraseña.setError("Introduce tu nombre de usuario");
            return;
        }
        if ("".equals(nombre)) {
            etNombreRecuperarContraseña.setError("Introduce tu nombre de usuario");
            return;
        }
        if ("".equals(apellidos)) {
            etApellidosRecuperarContraseña.setError("Introduce tu nombre de usuario");
            return;
        }
        if ("".equals(fecha)) {
            etdFechaNacimientoRecuperarContraseña.setError("Introduce tu fecha de nacimiento");
            return;
        }

        database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getId() + " => " + document.getData());
                        if (document.getId().equals(nombreUsuario)) {
                            String dbNombre = document.getData().get("nombre").toString();
                            String dbApellidos = document.getData().get("apellido").toString();
                            String dbFecha = document.getData().get("dataNaixement").toString();
                            if (dbNombre.equals(nombre) && dbApellidos.equals(apellidos) && dbFecha.equals(fecha)) {
                                Intent intent = new Intent(RecuperarContrasenaActivity.this, CambiarUsuariooContraseñaActivity.class);
                                intent.putExtra("variable", 3);
                                intent.putExtra("usuario", nombreUsuario);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                etNombreUsuarioRecuperarContraseña.setError("Los datos no coinciden. Vuelva a intentarlo");
                                etNombreRecuperarContraseña.setError("Los datos no coinciden. Vuelva a intentarlo");
                                etApellidosRecuperarContraseña.setError("Los datos no coinciden. Vuelva a intentarlo");
                                etdFechaNacimientoRecuperarContraseña.setError("Los datos no coinciden. Vuelva a intentarlo");
                            }
                        }
                    }
                }
            }
        });
    }

    public void onClickFecha(View view) {
        switch (view.getId()) {
            case R.id.etdFechaNacimientoRecuperarContraseña:
                showDatePickerDialog();
                break;
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog elegirFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int año, int mes, int dia) {
                RecuperarContrasenaActivity.this.any = año;
                RecuperarContrasenaActivity.this.mes = mes;
                RecuperarContrasenaActivity.this.dia = dia;

                fecha = dia + "/" + String.format("%02d", (mes+1)) + "/" + año;
                etdFechaNacimientoRecuperarContraseña.setText(fecha);
            }
        },any,mes,dia);
        elegirFecha.show();

    }
}