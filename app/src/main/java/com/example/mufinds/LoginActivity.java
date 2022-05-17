package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    TextView tv_olvidado_contraseña, tv_olvidado_usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_olvidado_contraseña = findViewById(R.id.tv_olvidado_contraseña);
        tv_olvidado_usuario = findViewById(R.id.tv_olvidado_usuario);

        tv_olvidado_contraseña.setPaintFlags(tv_olvidado_contraseña.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_olvidado_usuario.setPaintFlags(tv_olvidado_usuario.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("cancion");
        mDatabase.child("1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println("hola");
                }
                else {
                    System.out.println("adios");
                }
            }
        });
    }

    public void onClickIniciarSession (View view) {
        Intent intent = new Intent(this, PrincipalActivity.class);
        startActivity(intent);
        finish();
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