package com.gcs.wauc;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import static com.gcs.wauc.Constantes.CANAL_ID_NOTIFICACION;

/**
 * Clase que solamente tiene la función de controlar la notificación de nuestra aplicación. Ya que
 * a partir de la version de SDK Oreo se necesita crear NotificationChannel para las notificaciones
 *
 * @version BetaV1.5 04/03/2021
 * @author: Sergio García Calzada
 */
public class Notificacion extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        crearCanalNotificacion();
    }

    private void crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canalServicio = new NotificationChannel(
                    CANAL_ID_NOTIFICACION,
                    "WauC-Servicio",
                    NotificationManager.IMPORTANCE_DEFAULT

            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(canalServicio);
        }
    }


}
