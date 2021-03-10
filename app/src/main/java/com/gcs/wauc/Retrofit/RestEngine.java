package com.gcs.wauc.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Archivo/clase para configurar la librería de Retrofit.
 * Donde se indica la URL donde realizaremos las peticios http/https y nos devolverá informacion en
 * JSON.
 *
 * @version BetaV1.5 04/03/2021
 * @author: Sergio García Calzada
 */
public class RestEngine {

    private final static String BASE_URL = "http://www.iestrassierra.net:63135/";
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
