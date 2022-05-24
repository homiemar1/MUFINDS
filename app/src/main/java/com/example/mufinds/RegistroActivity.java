package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class RegistroActivity extends AppCompatActivity {
    Spinner sp_genero;
    EditText etNombreRegistro, etApellidosRegistro, etEmailRegistro, etContraseñaRegistro, etdFechaNacimientoRegistro;
    String fecha = "";
    int edad = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombreRegistro = findViewById(R.id.etNombreRegistro);
        etApellidosRegistro = findViewById(R.id.etApellidosRegistro);
        etEmailRegistro = findViewById(R.id.etEmailRegistro);
        etContraseñaRegistro = findViewById(R.id.etContraseñaRegistro);
        etdFechaNacimientoRegistro = findViewById(R.id.etdFechaNacimientoRegistro);

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
            return;
        }
        if ("".equals(apellido)) {
            etApellidosRegistro.setError("Introduce tus apellidos");
            return;
        }
        if ("".equals(email)) {
            etEmailRegistro.setError("Introduce tu email");
            return;
        }
        if (!email.contains("@")) {
            etEmailRegistro.setError("Este correo no es valido");
            return;
        }
        if ("".equals(pwd)) {
            etContraseñaRegistro.setError("Introduce tu contraseña");
            return;
        }
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
        if (!pwd.matches(pattern)) {
            etContraseñaRegistro.setError("La contraseña debe inculir una letra en minuscula [a-z], " +
                    "una en mayuscula[A-Z], un numero[0-9] y que tenga 8 caracteres como mínimo");
            return;
        }

        if (edad == 0) {
            etdFechaNacimientoRegistro.setError("Introduce tu fecha de nacimiento");
            return;
        }
        else if (edad < 16) {
            etdFechaNacimientoRegistro.setError("Para acceder a esta app debes ser mayor de 16 años");
            return;
        }

        pwd = EncriptarContraseña.encriptarMensaje(pwd);

        u = new Usuario();
        u.setApellido(apellido);
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPassword(pwd);
        u.setGenero(genero);
        u.setDataNaixement(fecha);

        Intent intent = new Intent(this, RegistroActivity2.class);
        intent.putExtra("usuario",u);
        startActivity(intent);
    }

    public void onClickFecha(View view) {
        switch (view.getId()) {
            case R.id.etdFechaNacimientoRegistro:
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
                etdFechaNacimientoRegistro.setText(fecha);

                int añoActual = año;
                int ma = Integer.parseInt(mesFormateado);
                edad = calcular(any, (mes+1), añoActual, ma);
            }
        },any,mes,dia);
        elegirFecha.show();

    }
    public int calcular(int any, int mes, int añoActual, int mesActual) {
        int años = 0;
        if (mesActual < mes) {
            años = any - añoActual;
        }
        else {
            años = any - añoActual -1;
        }
        return años;
    }
}