package com.gcs.wauc.AplicacionJefe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.gcs.wauc.R;
import com.gcs.wauc.Modelo.Usuario;
import com.gcs.wauc.Retrofit.RestEngine;
import com.gcs.wauc.Retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Primera actividad que aprecia los usuarios/clientes con el puesto/"rol" de Jefe
 * En dicha actividad podrán ver una lista con todos los empleados que disponen.
 * Al pulsar sobre uno de ellos aparecerá una ventana con diferentes opciones, ya ser ver las fechas
 * en las que el empleado tiene movimientos o llamar al empleado directamente
 *
 * @version BetaV1.5 04/03/2021
 * @author: Sergio García Calzada
 */
public class ListaEmpleados extends AppCompatActivity {

    private ArrayList<Usuario> listaUsuarios;
    private RecyclerView recycler;
    private Context contexto;

    //RETROFIT
    private RetrofitInterface retrofitInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_empleados);
        contexto = this;

        retrofitInterface = RestEngine.getRetrofit().create(RetrofitInterface.class);

        recycler = findViewById(R.id.listaEmpleados);
        recycler.setLayoutManager
                (new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //recycler.setHasFixedSize(true);

        listaUsuarios = new ArrayList<>();

        Call<List<Usuario>> call = retrofitInterface.listaEmpleados();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {

                listaUsuarios.addAll(response.body());
                AdapterListaEmpleados adapter = new AdapterListaEmpleados(contexto, listaUsuarios);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });


    }
}