package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CambiarUsuariooContraseñaActivity extends AppCompatActivity {
    TextView tvTituloCambio;
    EditText etDatoAntiguo, etDatoNuevo, etDatoCOnfirmacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_usuario);

        tvTituloCambio = findViewById(R.id.tvTituloCambio);
        etDatoAntiguo = findViewById(R.id.etDatoAntiguo);
        etDatoNuevo = findViewById(R.id.etDatoNuevo);
        etDatoCOnfirmacion = findViewById(R.id.etDatoCOnfirmacion);
        int valor = getIntent().getIntExtra("variable", 1);
        if (valor == 1) {
            tvTituloCambio.setText("CAMBIAR NOMBRE DE USUARIO");
            etDatoAntiguo.setHint("Usuario");
            etDatoNuevo.setHint("Usuario nuevo");
            etDatoCOnfirmacion.setHint("Contraseña");

            etDatoCOnfirmacion.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        }
        else {
            tvTituloCambio.setText("CAMBIAR CONTRASEÑA");
            etDatoAntiguo.setHint("Contraseña");
            etDatoNuevo.setHint("Contraseña nueva");
            etDatoCOnfirmacion.setHint("Usuario");

            etDatoAntiguo.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            etDatoNuevo.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        }

    }

    public void onClickAceptar(View view) {
        finish();
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