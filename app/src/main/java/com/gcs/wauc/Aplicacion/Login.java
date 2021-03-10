package com.gcs.wauc.Aplicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
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
import com.gcs.wauc.Constantes;
import com.gcs.wauc.R;
import com.gcs.wauc.Modelo.Usuario;
import com.gcs.wauc.Retrofit.RestEngine;
import com.gcs.wauc.Retrofit.RetrofitInterface;
import com.gcs.wauc.tools.HerramientaMetodos;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Clase que dará la opción de loguearse
 * Existen dos caminos y depende del "rol" del usuario:
 * -Si es jefe será redirigido a la actividad de ver todos los empleados de la empresa
 * -Si es un empleado será redirigido a la actividad de iniciar el servicio de movimiento
 *
 * @version BetaV1.5 04/03/2021
 * @author: Sergio García Calzada
 */
public class Login extends AppCompatActivity {

    private EditText etDniLogin, etPassLogin;
    private Button btnLogin;
    private CheckBox checkRecordatorio;

    private String pass = "";

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

    /**
     * Método que realiza la petición a la API de NodeJS y espera un resultado booleano.
     * Si es true, existe un usuario con dicho DNI y password
     * Si es false, no existe un usuario con dichos credenciales
     * <p>
     * A su vez, al exister un usuario con dichos credenciales, comprueba cual es su puesto/rol
     * y lo redirige según este
     */
    public void login() {

        if((!etDniLogin.getText().toString().contentEquals("")) &&
                (!etPassLogin.getText().toString().contentEquals(""))){


            HashMap<String, String> map = new HashMap<>();

            map.put("DNI", etDniLogin.getText().toString());

            try {
                pass = HerramientaMetodos.encriptar(etPassLogin.getText().toString(), Constantes.KEY);
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
                    Toast.makeText(Login.this, "ERROR-> " + t.getMessage()
                            , Toast.LENGTH_SHORT).show();
                }
            });

        }else{

            Snackbar.make(findViewById(R.id.layoutLogin),
                    "LOS CAMPOS NO PUEDEN ESTAR VACIOS. INTRODUZCA DATOS CORRECTOS"
                    , Snackbar.LENGTH_LONG);

        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (!hasFocus) {
            finish();
        }
    }

    /**
     * Método que recupera la clave de encriptación personal
     *
     * @return
     */
    public String key() {

        SharedPreferences prefs =
                getSharedPreferences("key", Context.MODE_PRIVATE);

        String key = prefs.getString("key", "null");

        return key;

    }

    /**
     * Método que comprueba si el usuario al hacer login en la aplicación marcó la casilla ->
     * Recordar DNI. Si fue pulsada recuperamos el DNI que se encuentra guardado en un fichero
     * mediante la clase SahredPreferences
     *
     * @return
     */
    public Boolean isChecked() {

        Boolean flag;
        SharedPreferences prefs =
                getSharedPreferences("recordarDNI", Context.MODE_PRIVATE);
        flag = prefs.getBoolean("cheked", false);
        return flag;

    }
}