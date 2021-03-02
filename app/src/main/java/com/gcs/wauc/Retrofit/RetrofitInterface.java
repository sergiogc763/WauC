package com.gcs.wauc.Retrofit;

import com.gcs.wauc.Modelo.Movimiento;
import com.gcs.wauc.Modelo.Usuario;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {


    @POST("/usuario/registrar")
    Call<Void> registrarUsuario(@Body HashMap<String, String> map);

    @POST("/usuario/login")
    Call<Boolean> loginUsuario(@Body HashMap<String, String> map);

    @POST("/usuario/buscarDNI")
    Call<Usuario> buscarUsuarioDNI(@Body HashMap<String, String> map);

    @GET("/usuarios")
    Call<List<Usuario>> listaUsuarios();

    @POST("/usuario/registrarMovimiento")
    Call<Void> registrarMovimiento(@Body HashMap<String, String> map);

    @POST("/usuario/fechasUsuario")
    Call<List<String>> fechasUsuario(@Body HashMap<String, String> map);

    @POST("/usuario/movimientosUsuario")
    Call<List<Movimiento>> movimientosUsuario(@Body HashMap<String, String> map);


}
