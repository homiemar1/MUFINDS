package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CambiarUsuariooContraseñaActivity extends AppCompatActivity {
    private TextView tvTituloCambio;
    private EditText etDatoAntiguo, etDatoNuevo, etDatoConfirmacion;
    private int valor;
    private FirebaseFirestore database;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_usuario);

        database = FirebaseFirestore.getInstance();
        sharedPref = this.getSharedPreferences(getString(R.string.preferences),
                Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        tvTituloCambio = findViewById(R.id.tvTituloCambio);
        etDatoAntiguo = findViewById(R.id.etDatoAntiguo);
        etDatoNuevo = findViewById(R.id.etDatoNuevo);
        etDatoConfirmacion = findViewById(R.id.etDatoConfirmacion);

        valor = getIntent().getIntExtra("variable", 1);
        if (valor == 1) {
            tvTituloCambio.setText("CAMBIAR NOMBRE DE USUARIO");
            etDatoAntiguo.setHint("Usuario actual");
            etDatoNuevo.setHint("Usuario nuevo");
            etDatoConfirmacion.setHint("Confirmar usuario nuevo");
        }
        else {
            tvTituloCambio.setText("CAMBIAR CONTRASEÑA");
            etDatoAntiguo.setHint("Contraseña actual");
            etDatoNuevo.setHint("Contraseña nueva");
            etDatoConfirmacion.setHint("Confirmar contraseña nueva");

            etDatoAntiguo.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            etDatoNuevo.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            etDatoConfirmacion.setTransformationMethod(new AsteriskPasswordTransformationMethod());
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
            etDatoConfirmacion.setError("Introduce el dato de confirmacion");
            return;
        }
        else if (!datoNuevo.equals(datoConfirmacion)) {
            etDatoNuevo.setError("Los nuevos datos deben coincidir");
            etDatoConfirmacion.setError("Los nuevos datos deben coincidir");
            return;
        }

        String usuario = recogerUsuario();
        if (valor == 1) {
            if (!datoAntiguo.equals(usuario) || "".equals(usuario)) {
                etDatoAntiguo.setError("El usuario no coincide con el de la sesion actual");
                return;
            }
        }
        else {
            String contraseña = recogerContraseña();
            if (!datoAntiguo.equals(contraseña) || "".equals(contraseña)) {
                etDatoAntiguo.setError("La contraseña no coincide con el de la sesion actual");
                return;
            }
        }

        //comprobar datos
        if (valor == 1) {
            updateNombreUsuario(usuario, datoNuevo);
        }
        else {
            updateContraseña(usuario, datoNuevo);
        }

    }

    private String recogerUsuario() {
        String usuario = sharedPref.getString("nombreUsuario", "");
        return usuario;
    }

    private String recogerContraseña() {
        String contraseña = sharedPref.getString("password", "");
        return contraseña;
    }

    public void updateContraseña(String usuario, String datoNuevo) {
        database.collection("users").document(usuario).update("password", datoNuevo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CambiarUsuariooContraseñaActivity.this, "Contraseña cambiada", Toast.LENGTH_SHORT).show();
                        editor.putString("password", datoNuevo);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CambiarUsuariooContraseñaActivity.this, "No se ha podido cambiar su contraseña\nVuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateNombreUsuario(String usuario, String datoNuevo) {
        database.collection("users").document(usuario).update("ID", datoNuevo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CambiarUsuariooContraseñaActivity.this, "Usuario cambiado correctamente", Toast.LENGTH_SHORT).show();
                        editor.putString("nombreUsuario", datoNuevo);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CambiarUsuariooContraseñaActivity.this, "No se ha podido cambiar el nombre de usuario\nVuelve a intentarlo", Toast.LENGTH_SHORT).show();
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