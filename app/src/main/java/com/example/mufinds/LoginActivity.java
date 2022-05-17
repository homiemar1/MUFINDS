package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("cancion");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    System.out.println(snapshot.child("1").getValue());
                }
                else {
                    System.out.println("hola");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };
        reference.addListenerForSingleValueEvent(listener);
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