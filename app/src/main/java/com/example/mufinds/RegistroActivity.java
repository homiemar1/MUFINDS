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

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {
    private Spinner sp_genero;
    private EditText etNombreRegistro, etApellidosRegistro, etEmailRegistro, etContraseñaRegistro, etdFechaNacimientoRegistro;
    private String fecha = "";
    private int edad = 0;
    private FirebaseFirestore database;
    private int any, mes ,dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        database = FirebaseFirestore.getInstance();

        etNombreRegistro = findViewById(R.id.etNombreRegistro);
        etApellidosRegistro = findViewById(R.id.etApellidosRegistro);
        etEmailRegistro = findViewById(R.id.etEmailRegistro);
        etContraseñaRegistro = findViewById(R.id.etContraseñaRegistro);
        etdFechaNacimientoRegistro = findViewById(R.id.etdFechaNacimientoRegistro);

        sp_genero = findViewById(R.id.spGeneroRegistro);
        String[] datos = new String[] {"Mujer", "No binario", "Prefiero no contestar", "Hombre"};
        sp_genero.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,datos));

        Calendar c = Calendar.getInstance();
        any = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONDAY);
        dia = c.get(Calendar.DAY_OF_MONTH);
    }

    public void onClickSiguiente (View view) {
        String nombre = etNombreRegistro.getText().toString();
        String apellido = etApellidosRegistro.getText().toString();
        String email = etEmailRegistro.getText().toString();
        String pwd = etContraseñaRegistro.getText().toString();
        String genero = sp_genero.getSelectedItem().toString();
        Usuario u;

        //pattern nombre y apellido
        String patternNombre = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern patronNombre = Pattern.compile(patternNombre);
        Matcher emparejadorNombre = patronNombre.matcher(nombre);
        Matcher emparejadorApellido = patronNombre.matcher(apellido);
        String numeros = ".*\\d.*";
        //pattern contraseña
        String patternContraseña = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
        //pattern email
        String patternEmail = "[A-Za-z0-9]+@[a-z]+\\.[a-z]+";
        Pattern patronEmail = Pattern.compile(patternEmail);
        Matcher emparejadorEmail = patronEmail.matcher(email);

        if ("".equals(nombre)) {
            etNombreRegistro.setError("Introduce tu nombre");
            return;
        }
        else if (emparejadorNombre.find() || nombre.matches(numeros)) {
            etNombreRegistro.setError("El nombre no acepta caracteres especiales ni números");
            return;
        }
        if ("".equals(apellido)) {
            etApellidosRegistro.setError("Introduce tus apellidos");
            return;
        }
        else if (emparejadorApellido.find() || apellido.matches(numeros)) {
            etApellidosRegistro.setError("El apellido no acepta caracteres especiales ni numeros");
            return;
        }

        if ("".equals(email)) {
            etEmailRegistro.setError("Introduce tu email");
            return;
        }

        if (!emparejadorEmail.find()) {
            etEmailRegistro.setError("Este email no es valido");
            return;
        }
        boolean valor = comprobarEmail(email);
        if (valor) {
            etEmailRegistro.setError("Este email ya existe");
            return;
        }

        if ("".equals(pwd)) {
            etContraseñaRegistro.setError("Introduce tu contraseña");
            return;
        }

        if (!pwd.matches(patternContraseña)) {
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

    private boolean comprobarEmail(String email) {
        boolean valor = false;
        Task<QuerySnapshot> task1 = database.collection("users").get();
        while(!task1.isComplete()){}
        for (DocumentSnapshot document : task1.getResult()) {
            if (email.equals(document.getData().get("email"))) {
                valor = true;
            }
        }

        return valor;
    }

    public void onClickFecha(View view) {
        switch (view.getId()) {
            case R.id.etdFechaNacimientoRegistro:
                showDatePickerDialog();
                break;
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog elegirFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int año, int mes, int dia) {
                RegistroActivity.this.any = año;
                RegistroActivity.this.mes = mes;
                RegistroActivity.this.dia = dia;

                fecha = dia + "/" + String.format("%02d", (mes+1)) + "/" + año;
                etdFechaNacimientoRegistro.setText(fecha);

                edad = calcular(año, (mes + 1));
            }
        },any,mes,dia);
        elegirFecha.show();

    }
    public int calcular(int añoActual, int mesActual) {
        Calendar c = Calendar.getInstance();
        int any = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONDAY);
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