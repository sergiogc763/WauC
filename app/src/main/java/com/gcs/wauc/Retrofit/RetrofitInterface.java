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

/**
 * Interfaz que contiene los métodos para las consultas que se realiza a la API REST
 *
 * @version BetaV1.5 04/03/2021
 * @author: Sergio García Calzada
 */
public interface RetrofitInterface {


    @POST("/usuario/registrar")
    Call<Void> registrarUsuario(@Body HashMap<String, String> map);

    @POST("/usuario/login")
    Call<Boolean> loginUsuario(@Body HashMap<String, String> map);

    @POST("/usuario/buscarDNI")
    Call<Usuario> buscarUsuarioDNI(@Body HashMap<String, String> map);

    @GET("/usuarios")
    Call<List<Usuario>> listaEmpleados();

    @POST("/usuario/registrarMovimiento")
    Call<Void> registrarMovimiento(@Body HashMap<String, String> map);

    @POST("/usuario/fechasUsuario")
    Call<List<String>> fechasEmpleado(@Body HashMap<String, String> map);

    @POST("/usuario/movimientosUsuario")
    Call<List<Movimiento>> movimientosEmpleado(@Body HashMap<String, String> map);


}
