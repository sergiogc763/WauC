package com.gcs.wauc.ElementoVisual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.gcs.wauc.Aplicacion.Inicio;
import com.gcs.wauc.R;
import com.gcs.wauc.tools.HerramientaMetodos;

/**
 * Actividad Main de la aplicación.
 * Tiene la función de mostrar el logo de la aplicación utilizando SpashScreen
 *
 * @version BetaV1.5 04/03/2021
 * @author: Sergio García Calzada
 */
public class SplashScreen extends AppCompatActivity {

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);


        precargaKey();

        //Animacion logo
        Animation animacion = AnimationUtils.loadAnimation(this, R.anim.animacion_splashscreen);

        logo = (ImageView) findViewById(R.id.logoSplash);
        logo.setAnimation(animacion);

        //Realizamos el Splash Screen y nos movemos a la actividad Inicio
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, Inicio.class);
                startActivity(i);
                finish();
            }
        }, 3500);


    }


    private void precargaKey() {
        SharedPreferences sp = this.getSharedPreferences("key", Context.MODE_PRIVATE);

        String key = sp.getString("key", "null");

        if (key.contentEquals("null")) {

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("key", HerramientaMetodos.generarPalabraKey());
            editor.commit();

        }
    }


}