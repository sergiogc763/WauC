package com.gcs.wauc.Aplicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gcs.wauc.AplicacionEmpleado.IniciarCoordenadas;
import com.gcs.wauc.AplicacionJefe.ListaEmpleados;
import com.gcs.wauc.R;
import com.gcs.wauc.Modelo.Usuario;
import com.gcs.wauc.Retrofit.RestEngine;
import com.gcs.wauc.Retrofit.RetrofitInterface;
import com.gcs.wauc.tools.HerramientaMetodos;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText etDniLogin, etPassLogin;
    private Button btnLogin;
    private CheckBox checkRecordatorio;

    static String pass = "";

    //RETROFIT
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        retrofitInterface = RestEngine.getRetrofit().create(RetrofitInterface.class);

        etDniLogin = findViewById(R.id.etDniLogin);
        etPassLogin = findViewById(R.id.etPassLogin);
        btnLogin = findViewById(R.id.btnEntrar);
        checkRecordatorio = findViewById(R.id.checkRecordatorio);

        checkRecordatorio.setChecked(isChecked());


        if (checkRecordatorio.isChecked()) {
            SharedPreferences prefs =
                    getSharedPreferences("recordarDNI", Context.MODE_PRIVATE);

            String dni = prefs.getString("DNI", "");
            etDniLogin.setText(dni);

        }


        etPassLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    login();
                    handled = true;
                }
                return handled;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkRecordatorio.isChecked()) {
                    SharedPreferences prefs =
                            getSharedPreferences("recordarDNI", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("cheked", true);
                    editor.putString("DNI", etDniLogin.getText().toString());
                    editor.commit();

                } else {
                    SharedPreferences prefs =
                            getSharedPreferences("recordarDNI", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("cheked", false);
                    editor.putString("DNI", "");
                    editor.commit();
                }
                login();
            }
        });

    }

    public void login() {


        HashMap<String, String> map = new HashMap<>();

        map.put("DNI", etDniLogin.getText().toString());

        try {
            pass = HerramientaMetodos.encriptar(etPassLogin.getText().toString(), key());
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put("password", pass);

        Call<Boolean> call = retrofitInterface.loginUsuario(map);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (response.body()) {
                    Toast.makeText(Login.this, "ACCESO CONCEDIDO", Toast.LENGTH_LONG).show();
                    HashMap<String, String> map = new HashMap<>();

                    map.put("DNI", etDniLogin.getText().toString());
                    Call<Usuario> call2 = retrofitInterface.buscarUsuarioDNI(map);
                    call2.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.body().getPuesto().equalsIgnoreCase("Empleado")) {


                                Intent i = new Intent(Login.this, IniciarCoordenadas.class);
                                i.putExtra("Empleado", response.body());
                                startActivity(i);
                            } else {
                                Intent i = new Intent(Login.this, ListaEmpleados.class);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Toast.makeText(Login.this, "ERROR-> " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(Login.this, "ACCESO NO CONCEDIDO. DNI/CONTRASEÑA INCORRECTA"
                            , Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(Login.this, "ERROR-> " + t.getMessage(), Toast.LENGTH_LONG).show();
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

        String key = prefs.getString("key", "null");

        return key;

    }

    public Boolean isChecked() {

        Boolean flag;
        SharedPreferences prefs =
                getSharedPreferences("recordarDNI", Context.MODE_PRIVATE);
        flag = prefs.getBoolean("cheked", false);
        return flag;

    }
}