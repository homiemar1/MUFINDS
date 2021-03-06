package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RecuperarUsuarioActivity extends AppCompatActivity {
    private EditText etEmailRecuperarUsuario, etContraseñaRecuperarUsuario;
    private FirebaseFirestore database;
    private String contraseña, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_usuario);
        database = FirebaseFirestore.getInstance();

        etEmailRecuperarUsuario = findViewById(R.id.etEmailRecuperarUsuario);
        etContraseñaRecuperarUsuario = findViewById(R.id.etContraseñaRecuperarUsuario);
    }

    public void onClickRecuperar(View view) {
        //comprobar que sea igual al de la base de datos
        email = etEmailRecuperarUsuario.getText().toString();
        contraseña = etContraseñaRecuperarUsuario.getText().toString();
        contraseña = EncriptarContraseña.encriptarMensaje(contraseña);

        if ("".equals(email)) {
            etEmailRecuperarUsuario.setError("Introduce tu email");
            return;
        }
        else if ("".equals(contraseña)) {
            etContraseñaRecuperarUsuario.setError("Introduce tu contraseña");
            return;
        }
        else {
            database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    boolean comprobar = true;
                    String usuario = "";
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String dbEmail = document.getData().get("email").toString();
                            String dbPassword = document.getData().get("password").toString();
                            if (dbEmail.equals(email) && dbPassword.equals(contraseña)) {
                                usuario = document.getData().get("nombreUsuari").toString();
                                comprobar = true;
                                mensajeFinal(usuario);
                                break;
                            }
                            else {
                                comprobar = false;
                            }
                        }
                    }

                    if (!comprobar) {
                        etEmailRecuperarUsuario.setError("Los datos no coinciden.");
                        etContraseñaRecuperarUsuario.setError("Los datos no coinciden.");
                    }
                }
            });
        }

    }

    private void mensajeFinal(String usuario) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("RECUPERAR CONTRASEÑA");
        dialog.setMessage("Su nombre de usuario es --> " + usuario);
        dialog.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }
}