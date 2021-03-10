package com.gcs.wauc.AplicacionJefe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.gcs.wauc.Modelo.Usuario;
import com.gcs.wauc.R;
import com.gcs.wauc.Retrofit.RestEngine;
import com.gcs.wauc.Retrofit.RetrofitInterface;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad segundoa de los Jefes
 * En dicha actividad el usuario podrá ver todas las fechas que un Empleado tiene.
 * Dicchas fechas hace referencia al día en el que se recogieron los datos/movimientos del Empleado.
 * Al puslsar sobre una de ellas, redirige a la acticdad VerMovimientos, donde el usuario podrá
 * ver en un mapa los movimientios/coordernadas por donde ha estado dentro de la empresa.
 *
 * @version BetaV1.5 04/03/2021
 * @author: Sergio García Calzada
 */
public class ListaFechasEmpleado extends AppCompatActivity {

    private Usuario user;
    private RecyclerView recycler;
    private Context contexto;
    private ArrayList<String> listaFechas;

    //RETROFIT
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_fechas_empleado);

        contexto = this;
        retrofitInterface = RestEngine.getRetrofit().create(RetrofitInterface.class);
        listaFechas = new ArrayList<>();


        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null) {
            user = (Usuario) parametros.getSerializable("DatosEmpleado");
        } else {
            user = new Usuario();
        }


        recycler = findViewById(R.id.listaFechas);
        recycler.setLayoutManager
                (new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setHasFixedSize(true);

        HashMap<String, String> map = new HashMap<>();

        map.put("dniU", user.getDNI());


        Call<List<String>> call = retrofitInterface.fechasEmpleado(map);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                listaFechas.addAll(response.body());

                AdapterListaFechasEmpleado adapter = new AdapterListaFechasEmpleado(contexto, listaFechas, user.getDNI());
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(ListaFechasEmpleado.this, "ERROR-> " + t
                        , Toast.LENGTH_LONG).show();
            }
        });

    }
}