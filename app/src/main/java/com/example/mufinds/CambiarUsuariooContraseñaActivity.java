package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CambiarUsuariooContraseñaActivity extends AppCompatActivity {
    private TextView tvTituloCambio;
    private EditText etDatoAntiguo, etDatoNuevo, etDatoConfirmacion;
    private int valor;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_usuario);

        database = FirebaseFirestore.getInstance();

        tvTituloCambio = findViewById(R.id.tvTituloCambio);
        etDatoAntiguo = findViewById(R.id.etDatoAntiguo);
        etDatoNuevo = findViewById(R.id.etDatoNuevo);
        etDatoConfirmacion = findViewById(R.id.etDatoConfirmacion);

        valor = getIntent().getIntExtra("variable", 1);
        if (valor == 1) {
            tvTituloCambio.setText("CAMBIAR NOMBRE DE USUARIO");
            etDatoAntiguo.setHint("Usuario");
            etDatoNuevo.setHint("Usuario nuevo");
            etDatoConfirmacion.setHint("Contraseña");

            etDatoConfirmacion.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        }
        else {
            tvTituloCambio.setText("CAMBIAR CONTRASEÑA");
            etDatoAntiguo.setHint("Contraseña");
            etDatoNuevo.setHint("Contraseña nueva");
            etDatoConfirmacion.setHint("Usuario");

            etDatoAntiguo.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            etDatoNuevo.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        }

    }

    public void onClickAceptar(View view) {
        String datoAntiguo = etDatoAntiguo.getText().toString();
        String datoNuevo = etDatoNuevo.getText().toString();
        String datoConfirmacion = etDatoConfirmacion.getText().toString();

        if ("".equals(datoAntiguo)) {
            etDatoAntiguo.setError("Introduce el dato antiguo");
            return;
        }
        else if ("".equals(datoNuevo)) {
            etDatoNuevo.setError("Introduce el dato nuevo");
            return;
        }
        else if ("".equals(datoConfirmacion)) {
            etDatoConfirmacion.setError("Introduce el dato confirmatorio");
            return;
        }

        //comprobar datos
        boolean check = consultaExistencia(datoAntiguo);
        if (check) {
            if (valor == 1) {
                updateNombreUsuario(datoNuevo);
            }
            else {
                updateConraseña(datoNuevo);
            }
            finish();
        }
    }

    public boolean consultaExistencia(String datoAntiguo) {
        final boolean[] check = {false};
        database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (valor == 1) {
                            if (document.getId().equals(datoAntiguo)) {
                                check[0] = true;
                            }
                        }
                        else {
                            if (document.getData().get("password").equals(datoAntiguo)) {
                                check[0] = true;
                            }
                        }

                    }
                } else {
                    System.out.println("Error getting documents." + task.getException());
                }
                if (!check[0]) {
                    if (valor == 1) {
                        etDatoAntiguo.setError("ERROR");
                    }
                    else {
                        etDatoAntiguo.setError("ERROR");
                    }
                }
            }
        });
        return check[0];
    }

    public void updateConraseña(String datoNuevo) {
        database.collection("users").document("nerea").update("password", datoNuevo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CambiarUsuariooContraseñaActivity.this, "Update satisfactorio", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CambiarUsuariooContraseñaActivity.this, "Updatn't satisfactorio", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateNombreUsuario(String datoNuevo) {
        database.collection("users").document("nerea").update("ID", datoNuevo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CambiarUsuariooContraseñaActivity.this, "Update satisfactorio", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CambiarUsuariooContraseñaActivity.this, "Updatn't satisfactorio", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };
}