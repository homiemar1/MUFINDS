package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class CambiarUsuariooContrase├▒aActivity extends AppCompatActivity {
    private TextView tvTituloCambio;
    private EditText etDatoAntiguo, etDatoNuevo, etDatoConfirmacion;
    private int valor;
    private FirebaseFirestore database;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_usuario);

        database = FirebaseFirestore.getInstance();
        sharedPref = this.getSharedPreferences(getString(R.string.preferences),
                Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        valor = getIntent().getIntExtra("variable", 1);

        usuario = "";
        if (valor != 3) {
            usuario = recogerUsuario();
        }
        else {
            usuario = getIntent().getStringExtra("usuario");
        }

        tvTituloCambio = findViewById(R.id.tvTituloCambio);
        etDatoAntiguo = findViewById(R.id.etDatoAntiguo);
        etDatoNuevo = findViewById(R.id.etDatoNuevo);
        etDatoConfirmacion = findViewById(R.id.etDatoConfirmacion);

        if (valor == 1) {
            tvTituloCambio.setText("CAMBIAR NOMBRE DE USUARIO");
            etDatoAntiguo.setHint("Usuario actual");
            etDatoNuevo.setHint("Usuario nuevo");
            etDatoConfirmacion.setHint("Confirmar usuario nuevo");
        }
        else {
            if (valor == 3) {
                etDatoAntiguo.setVisibility(View.INVISIBLE);
            }
            tvTituloCambio.setText("CAMBIAR CONTRASE├ĹA");
            etDatoAntiguo.setHint("Contrase├▒a actual");
            etDatoAntiguo.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

            etDatoNuevo.setHint("Contrase├▒a nueva");
            etDatoNuevo.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

            etDatoConfirmacion.setHint("Confirmar contrase├▒a nueva");
            etDatoConfirmacion.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

            etDatoAntiguo.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            etDatoNuevo.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            etDatoConfirmacion.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        }

    }

    public void onClickAceptar(View view) {
        String datoAntiguo = etDatoAntiguo.getText().toString();
        String datoNuevo = etDatoNuevo.getText().toString();
        String datoConfirmacion = etDatoConfirmacion.getText().toString();

        if ("".equals(datoAntiguo) && valor != 3) {
            etDatoAntiguo.setError("Introduce el dato antiguo");
            return;
        }
        if ("".equals(datoNuevo)) {
            etDatoNuevo.setError("Introduce el dato nuevo");
            return;
        }
        if ("".equals(datoConfirmacion)) {
            etDatoConfirmacion.setError("Introduce el dato de confirmacion");
            return;
        }

        if (valor == 1) {
            if (!datoAntiguo.equals(usuario)) {
                etDatoAntiguo.setError("El usuario no coincide con el de la sesi├│n actual");
                return;
            }
        }
        else {
            if (valor != 3) {
                String contrase├▒a = recogerContrase├▒a();
                datoAntiguo = EncriptarContrase├▒a.encriptarMensaje(datoAntiguo);
                if (!datoAntiguo.equals(contrase├▒a)) {
                    etDatoAntiguo.setError("La contrase├▒a no coincide con el de la sesi├│n actual");
                    return;
                }
            }
        }
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";
        if (!datoNuevo.matches(pattern) && valor!=1) {
            etDatoNuevo.setError("La contrase├▒a debe inculir una letra en minuscula [a-z], " +
                    "una en may├║scula[A-Z], un n├║mero[0-9] y que tenga 8 caracteres como m├şnimo");
            return;
        }
        if (!datoNuevo.matches(pattern) && valor!=1) {
            etDatoConfirmacion.setError("La contrase├▒a debe inculir una letra en minuscula [a-z], " +
                    "una en may├║scula[A-Z], un n├║mero[0-9] y que tenga 8 caracteres como m├şnimo");
            return;
        }
        if (!datoNuevo.equals(datoConfirmacion)) {
            etDatoNuevo.setError("Los nuevos datos deben coincidir");
            etDatoConfirmacion.setError("Los nuevos datos deben coincidir");
            return;
        }
        if (datoAntiguo.equals(datoNuevo) && datoAntiguo.equals(datoConfirmacion) && valor != 3) {
            etDatoNuevo.setError("Los nuevos datos no pueden ser iguales que el dato a cambiar");
            etDatoConfirmacion.setError("Los nuevos datos no pueden ser iguales que el dato a cambiar");
            return;
        }

        //comprobar datos
        if (valor == 1) {
            database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    boolean check = true;
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getId().equals(datoNuevo)){
                                check = false;
                            }
                        }
                    } else {
                        System.out.println("Error getting documents." + task.getException());
                    }
                    if (check) {
                        updateNombreUsuario(datoNuevo);
                    }
                    else {
                        etDatoNuevo.setError("Ese nombre de usuario ya existe");
                    }
                }
            });
        }
        else {
            updateContrase├▒a(usuario, datoNuevo);
        }

    }

    private String recogerUsuario() {
        String usuario = sharedPref.getString("nombreUsuario", "");
        return usuario;
    }

    private String recogerContrase├▒a() {
        String contrase├▒a = sharedPref.getString("password", "");
        return contrase├▒a;
    }

    public void updateContrase├▒a(String usuario, String datoNuevo) {
        String contrase├▒a = EncriptarContrase├▒a.encriptarMensaje(datoNuevo);
        database.collection("users").document(usuario).update("password", contrase├▒a)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CambiarUsuariooContrase├▒aActivity.this, "Contrase├▒a cambiada", Toast.LENGTH_SHORT).show();
                        editor.putString("password", contrase├▒a);
                        editor.commit();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CambiarUsuariooContrase├▒aActivity.this, "No se ha podido cambiar su contrase├▒a\nVuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateNombreUsuario(String datoNuevo) {
        Usuario user = recogerDatosUsuario();
        database.collection("users").document(user.getNombreUsuari()).delete();
        user.setNombreUsuari(datoNuevo);

        database.collection("users").document(user.getNombreUsuari()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                String datoAntiguo = sharedPref.getString("nombreUsuario", "").toString();
                editor.putString("nombreUsuario", datoNuevo);
                editor.commit();
                Toast.makeText(CambiarUsuariooContrase├▒aActivity.this, "Nombre usuario cambiado correctamente", Toast.LENGTH_SHORT).show();
                actualizarDatosUsuarioMusica(datoNuevo, datoAntiguo);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CambiarUsuariooContrase├▒aActivity.this,
                                "No se ha podido cambiar su nombre de usuario\n" +
                                        "Vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                    }
                });
        finish();
    }

    private void actualizarDatosUsuarioMusica(String datoNuevo, String datoAntiguo) {
        database.collection("relacionUsuarioMusica").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> datos = null;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(datoAntiguo)) {
                            datos = document.getData();
                        }
                    }
                    database.collection("relacionUsuarioMusica").document(datoAntiguo).delete();
                    database.collection("relacionUsuarioMusica").document(datoNuevo).set(datos);
                    actualizarDatosUsuarioUsuario(datoNuevo, datoAntiguo);
                }
            }
        });
    }

    private void actualizarDatosUsuarioUsuario(String datoNuevo, String datoAntiguo) {
        database.collection("relacionUsuarioUsuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> datos = null;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(datoAntiguo)) {
                            datos = document.getData();
                        }
                    }
                    database.collection("relacionUsuarioUsuario").document(datoAntiguo).delete();
                    database.collection("relacionUsuarioUsuario").document(datoNuevo).set(datos);

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().get(datoAntiguo) != null) {
                            boolean valor = (boolean) document.getData().get(datoAntiguo);
                            Map<String, Object> mapa = new HashMap<String, Object>();
                            mapa.put(datoNuevo, valor);

                            Map<String,Object> updates = new HashMap<>();
                            updates.put(datoAntiguo, FieldValue.delete());
                            DocumentReference docRef = database.collection("relacionUsuarioUsuario").document(document.getId());
                            docRef.update(updates);

                            WriteBatch batch = database.batch();
                            DocumentReference docRef2 = document.getReference();
                            batch.update(docRef2, mapa);
                            batch.commit();

                        }

                    }
                }
            }
        });
    }

    private Usuario recogerDatosUsuario() {
        Usuario user = new Usuario();
        user.setNombre(sharedPref.getString("nombre", ""));
        user.setApellido(sharedPref.getString("apellido", ""));
        user.setEmail(sharedPref.getString("email", ""));
        user.setPassword(sharedPref.getString("password", ""));
        user.setGenero(sharedPref.getString("genero", ""));
        user.setDataNaixement(sharedPref.getString("fechaNacimiento", ""));
        user.setDescripcion(sharedPref.getString("descripcion", ""));
        user.setNombreUsuari(sharedPref.getString("nombreUsuario", ""));
        user.setFotoPerfil(sharedPref.getString("idFoto", ""));
        user.setInstagram(sharedPref.getString("instagram",""));
        return user;
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source;
            }
            public char charAt(int index) {
                return 'ÔÇó';
            }
            public int length() {
                return mSource.length();
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end);
            }
        }
    };
}