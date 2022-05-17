package com.example.mufinds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.PreparedStatement;

public class PrincipalActivity extends AppCompatActivity {
    int condicion = 1;
    int tema = 1;
    Button btEditarPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().show();

        btEditarPerfil = findViewById(R.id.btEditarPerfil);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_configuration:
                Intent intent  = new Intent(this, ConfiguracionActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_cambiarTema:
                Toast.makeText(this, "Has seleccionado cambiar tema", Toast.LENGTH_SHORT).show();
                if (tema == 1) {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    tema = 2;
                }
                else {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    tema = 1;
                }
                return true;

            case R.id.action_logout:
                Intent i1 = new Intent(this, IniciActivity.class);
                startActivity(i1);
                return true;

            case R.id.action_informacio:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("INFORMACION DE LA APLICACION");
                dialog.setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur lorem mauris, dapibus ac turpis sed, elementum porttitor urna. Sed risus lectus, venenatis et bibendum dictum, venenatis eget erat. Aenean in enim quis quam rutrum luctus eu placerat odio. Donec nec dolor sit amet magna tincidunt venenatis mattis vitae risus. Morbi luctus ac felis et tempus. Duis eget fringilla lorem, ac molestie diam. Donec a pellentesque velit. Aliquam rhoncus vitae erat et interdum. Mauris est enim, fermentum eu ultricies id, dignissim a lorem. Duis porta elit id est fringilla, quis pharetra tortor suscipit. Proin bibendum neque nec magna gravida vehicula. Quisque iaculis magna venenatis massa lobortis, eget bibendum ipsum faucibus. Proin scelerisque odio sem, et semper erat lobortis vitae. Nullam dolor leo, rutrum sit amet mauris sed, ultricies fringilla nulla.\n" +
                        "\n" +
                        "Aenean in nibh lacinia, vestibulum purus sed, dapibus metus. Donec malesuada facilisis efficitur. Duis aliquam suscipit ornare. Aenean vel neque tempor, ullamcorper erat ac, venenatis eros. Quisque dictum risus vel interdum pretium. Aliquam auctor viverra nisi, at maximus eros pulvinar a. Aenean quis eros tincidunt, tempor leo et, laoreet lorem. Phasellus malesuada tortor sem, a ultrices enim gravida sit amet. Sed cursus fermentum tincidunt. Ut est nibh, aliquam vel est sit amet, convallis pretium nisl. In tempus nisl non mollis commodo. In ullamcorper ante ac auctor rutrum. In pretium sem fringilla diam egestas vestibulum. Sed consequat iaculis tempus. Nunc ornare mi at dolor sollicitudin aliquam. Fusce quis leo vehicula, porttitor justo vitae, pellentesque lacus.");
                dialog.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });
                dialog.show();
                return true;
            case R.id.action_exit:
                finishAffinity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickEditarPerfil(View view) {
        if (condicion == 1) {
            Intent intent = new Intent(PrincipalActivity.this, MusicaActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(PrincipalActivity.this, EditarPerfilActivity.class);
            startActivity(intent);
        }

    }

    public void onClickAmistades(View view) {
        Intent intent = new Intent(PrincipalActivity.this, AmistadesActivity.class);
        startActivity(intent);
    }

    public void onClickMusica(View view) {
        condicion = 1;
        btEditarPerfil.setText("Gestionar Musica");
    }

    public void onClickPersonas (View view) {
        condicion = 2;
        btEditarPerfil.setText("Editar Perfil");
    }
}