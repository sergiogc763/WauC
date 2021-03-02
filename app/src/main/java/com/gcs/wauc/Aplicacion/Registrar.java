package com.gcs.wauc.Aplicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gcs.wauc.R;
import com.gcs.wauc.Retrofit.RestEngine;
import com.gcs.wauc.Retrofit.RetrofitInterface;
import com.gcs.wauc.tools.HerramientaMetodos;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registrar extends AppCompatActivity {

    private Button btnRegistrarUsuario;
    private EditText etNombre, etTelefono, etDni, etPassword, etPasswordConfirmar;

    static String pass;


    //RETROFIT
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //Cargamos las consultas en retrofit
        retrofitInterface = RestEngine.getRetrofit().create(RetrofitInterface.class);


        btnRegistrarUsuario = findViewById(R.id.btnRegistrarR);
        etNombre = findViewById(R.id.etNombreR);
        etTelefono = findViewById(R.id.etTelefonoR);
        etDni = findViewById(R.id.etDniR);
        etPassword = findViewById(R.id.etPassR);
        etPasswordConfirmar = findViewById(R.id.etPassRConfirmar);


        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /**
                 * Comprobamos que los campos no se encuentran vacios
                 *
                 * A su vez, comprobaremos que se introduce un DNI correcto y la contraseña
                 tenga entre 4 y 8 caracteres (tienen que coincidir las dos)
                 */
                if (HerramientaMetodos.
                        herramienta().comprobarDatos(etDni, etNombre, etTelefono, etPassword, etPasswordConfirmar)) {

                    /**
                     * Pasamos las variables a la petición mediante el body
                     */
                    HashMap<String, String> map = new HashMap<>();

                    map.put("DNI", etDni.getText().toString().toUpperCase());

                    try {
                        pass = HerramientaMetodos.encriptar(etPassword.getText().toString(), key());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    map.put("password", pass);
                    map.put("nombre", etNombre.getText().toString().toUpperCase());
                    map.put("telefono", etTelefono.getText().toString());
                    map.put("puesto", "Empleado");


                    Call<Void> call = retrofitInterface.registrarUsuario(map);
                    call.enqueue(new Callback<Void>() {
                        //Si la consulta ha funcionado correctamente ejecutará onResponse
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(Registrar.this, "SE HA REGISTRADO CORRECTAMENTE", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Registrar.this, Inicio.class);
                            startActivity(i);
                            finish();
                        }

                        //Si existe algún fallo en la consulta ejecutará onFailure
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(Registrar.this, "ERROR-> " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(Registrar.this, "ERROR-> FALLO EN UNO DE LOS CAMPOS", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (!hasFocus) {
            finish();
        }

    }

    public String key() {

        SharedPreferences prefs =
                getSharedPreferences("key", Context.MODE_PRIVATE);

        String key = prefs.getString("key", "vacioo");

        return key;

    }


}