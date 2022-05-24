package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class RecuperarContrasenaActivity extends AppCompatActivity {
    EditText etNombreUsuarioRecuperarContraseña, etNombreRecuperarContraseña, etApellidosRecuperarContraseña, etdFechaNacimientoRecuperarContraseña;
    TextView tvRespuestaRecuperarContraseña;
    String nombreUsuario, nombre, apellidos, fecha = "";
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);
        database = FirebaseFirestore.getInstance();

        etNombreUsuarioRecuperarContraseña = findViewById(R.id.etNombreUsuarioRecuperarContraseña);
        etNombreRecuperarContraseña = findViewById(R.id.etNombreRecuperarContraseña);
        etApellidosRecuperarContraseña = findViewById(R.id.etApellidosRecuperarContraseña);
        etdFechaNacimientoRecuperarContraseña = findViewById(R.id.etdFechaNacimientoRecuperarContraseña);

    }

    public void onClickRecuperar(View view) {

        nombreUsuario = etNombreUsuarioRecuperarContraseña.getText().toString();
        nombre = etNombreRecuperarContraseña.getText().toString();
        apellidos = etApellidosRecuperarContraseña.getText().toString();


        if ("".equals(nombreUsuario)) {
            etNombreUsuarioRecuperarContraseña.setError("Introduce tu nombre de usuario");
        }
        else if ("".equals(nombre)) {
            etNombreRecuperarContraseña.setError("Introduce tu nombre de usuario");
        }
        else if ("".equals(apellidos)) {
            etApellidosRecuperarContraseña.setError("Introduce tu nombre de usuario");
        }
        else if ("".equals(fecha)) {
            etdFechaNacimientoRecuperarContraseña.setError("Introduce tu fecha de nacimiento");
        }
        else {
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
                                    intent.putExtra("variable", 2);
                                    startActivity(intent);
                                }
                                else {
                                    tvRespuestaRecuperarContraseña.setText("Los datos no coinciden. Vuelva a intentarlo");
                                }
                            }
                        }
                    } else {
                        System.out.println("Error getting documents." + task.getException());
                    }
                }
            });
        }

    }

    public void onClickFecha(View view) {
        switch (view.getId()) {
            case R.id.etdFechaNacimientoRecuperarContraseña:
                showDatePickerDialog();
                break;
        }
    }

    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        int any = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONDAY);
        int dia = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog elegirFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int año, int mes, int dia) {
                final int mesActual = mes+1;
                String diaFormateado = (dia <10)? "0" + String.valueOf(dia):String.valueOf(dia);
                String mesFormateado = (mesActual <10)? "0" + String.valueOf(mesActual): String.valueOf(mesActual);

                fecha = diaFormateado + "/" + mesFormateado + "/" + año;
                etdFechaNacimientoRecuperarContraseña.setText(fecha);

            }
        },any,mes,dia);
        elegirFecha.show();

    }
}