package com.gcs.wauc.Aplicacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gcs.wauc.R;

/**
 * Clase que dará dos opciones al usuario.
 * Registrarse en la aplicación o Entrar/Logear dentro de esta
 *
 * @version BetaV1.5 04/03/2021
 * @author: Sergio García Calzada
 */

public class Inicio extends AppCompatActivity {

    private Button btnAcceder, btnRegistrar;
    private ImageView help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        help = findViewById(R.id.imgErrores);

        /**
         * OnClick donde muestra un diálogo explicativo
         */
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Inicio.this);
                builder.setTitle("Acerda de... IES TRASSIERRA" +
                        "");
                builder.setMessage("¡ Muchas gracias por utilizar nuestra Aplicación !" +
                        "\nActualmente se encuentra en fase Beta. " +
                        "\nSi encuentra errores " +
                        "o tiene algunas ideas para " +
                        "mejorar la aplicación, pongase en contacto a nuestro correo -> waucproyect@gmail.com");
                builder.show();
            }
        });

        /**
         * OnClick que pasa/inicia la Actividad Registrar
         */
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inicio.this, Registrar.class);
                startActivity(i);
            }
        });

        /**
         * OnClick que pasa/inicia la Actividad Acceder
         */
        btnAcceder = findViewById(R.id.btnAcceder);
        btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inicio.this, Login.class);
                startActivity(i);
            }
        });
    }

}