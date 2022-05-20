package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {
    TextView tv_olvidado_contraseña, tv_olvidado_usuario;
    EditText editTextTextPersonName, etContraseñaIniciarSesion;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_olvidado_contraseña = findViewById(R.id.tv_olvidado_contraseña);
        tv_olvidado_usuario = findViewById(R.id.tv_olvidado_usuario);
        tv_olvidado_contraseña.setPaintFlags(tv_olvidado_contraseña.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_olvidado_usuario.setPaintFlags(tv_olvidado_usuario.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        etContraseñaIniciarSesion = findViewById(R.id.etContraseñaIniciarSesion);

        database = FirebaseFirestore.getInstance();
    }

    public void onClickIniciarSession (View view) {
        String nombreUsuario = editTextTextPersonName.getText().toString();
        String password = etContraseñaIniciarSesion.getText().toString();

        if ("".equals(nombreUsuario)){
            editTextTextPersonName.setError("Introduce tu nombre de usuario");
            return;
        }
        else if ("".equals(password)) {
            etContraseñaIniciarSesion.setError("Introduce tu contraseña");
            return;
        }
        database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean usuarioCheck = false;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + " => " + document.getData());
                                if (document.getId().equals(nombreUsuario)) {
                                    usuarioCheck = true;
                                    String pwd = document.getData().get("password").toString();
                                    if (pwd.equals(password)) {
                                        Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        editTextTextPersonName.setError("Usuario o contraseña incorrecto");
                                        etContraseñaIniciarSesion.setError("Usuario o contraseña incorrecto");
                                        return;
                                    }
                                }
                            }
                        } else {
                            System.out.println("Error getting documents." + task.getException());
                        }
                        if (usuarioCheck == false) {
                            editTextTextPersonName.setError("Usuario o contraseña incorrecto");
                            etContraseñaIniciarSesion.setError("Usuario o contraseña incorrecto");
                        }
                    }
                });
    }

    public void onClickRecuperarContraseña (View view) {
        Intent intent = new Intent(LoginActivity.this, RecuperarContrasenaActivity.class);
        intent.putExtra("confirmacon", 1);
        startActivity(intent);
    }

    public void onClickRecuperarUsuario (View view) {
        Intent intent = new Intent(LoginActivity.this, RecuperarUsuarioActivity.class);
        intent.putExtra("confirmacon", 2);
        startActivity(intent);
    }
}