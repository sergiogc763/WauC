package com.gcs.wauc.Servicios;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.gcs.wauc.AplicacionEmpleado.IniciarCoordenadas;
import com.gcs.wauc.Constantes;
import com.gcs.wauc.R;
import com.gcs.wauc.Retrofit.RestEngine;
import com.gcs.wauc.Retrofit.RetrofitInterface;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ServicioEmpleado extends Service {

    private RetrofitInterface retrofitInterface = RestEngine.getRetrofit().create(RetrofitInterface.class);
    ;

    static String dniEmpleado;

    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) {
                double latitud = locationResult.getLastLocation().getLatitude();
                double longitud = locationResult.getLastLocation().getLongitude();

                SimpleDateFormat formato;
                Date date = new Date();
                //FECHA
                formato = new SimpleDateFormat("yyyy-MM-dd");
                String fecha = formato.format(date);

                //HORA
                formato = new SimpleDateFormat("HH:mm:ss");
                String hora = formato.format(date);


                HashMap<String, String> map = new HashMap<>();

                map.put("dniU", dniEmpleado);
                map.put("fecha", fecha);
                map.put("hora", hora);
                map.put("latitud", "" + latitud);
                map.put("longitud", "" + longitud);


                Call<Void> call = retrofitInterface.registrarMovimiento(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Todavia no implementado");
    }

    @SuppressLint("MissingPermission")
    private void comenzarServicio() {

        Intent intentNotificacion = new Intent(this, IniciarCoordenadas.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intentNotificacion, 0);

        Notification notificacion = new NotificationCompat.Builder(this, Constantes.CANAL_ID_NOTIFICACION)
                .setContentTitle("WauC")
                .setContentText("La aplicación se encuentra trabajando en segundo plano")
                .setSmallIcon(R.drawable.ic_notificacion)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);//300000
        locationRequest.setFastestInterval(2000);//60000
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        startForeground(Constantes.ID_SERVICIO_LOCALIZACION, notificacion);
    }

    private void pararServicio() {

        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String accion = intent.getAction();
            if (accion != null) {
                if (accion.equals(Constantes.START_SERVICIO_LOCALIZACION)) {
                    dniEmpleado = intent.getExtras().getString("dniUsuarioActual");
                    comenzarServicio();
                } else if (accion.equals(Constantes.STOP_SERVICIO_LOCALIZACION)) {
                    pararServicio();
                }
            }
        }
        return START_NOT_STICKY;
    }
}
