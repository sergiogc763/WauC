package com.gcs.wauc.AplicacionEmpleado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gcs.wauc.Constantes;
import com.gcs.wauc.R;
import com.gcs.wauc.Modelo.Usuario;
import com.gcs.wauc.Servicios.ServicioEmpleado;

/**
 * Actividad principal de los Empleados
 * <p>
 * Actualmente solo tiene la opción de iniciar el servicio de recuperación de movimientos o parar
 * dicho servicio. Al pulsar el botón iniciará el Servicio-> ServicioEmpleado
 * <p>
 * Además al pulsar dicho botón se pedirá la confirmación de los permisos para GPS
 *
 * @version BetaV1.5 04/03/2021
 * @author: Sergio García Calzada
 */
public class IniciarCoordenadas extends AppCompatActivity {

    private TextView tvNomUsuarioActual;
    private Button btnServicio;
    private static Usuario user;
    private static boolean flagState; //Si está en false no se está ejecutando.

    private final int LOCATION_PERMISSION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_coordenadas);

        flagState = false;

        tvNomUsuarioActual = findViewById(R.id.tvUsuarioActual);
        btnServicio = findViewById(R.id.btnServicio);

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null) {
            user = (Usuario) parametros.getSerializable("Empleado");
            tvNomUsuarioActual.setText(user.getNombre());
        } else {
            tvNomUsuarioActual.setText("ERROR");
        }

        btnServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            IniciarCoordenadas.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE
                    );
                } else {
                    if (flagState == false) {
                        flagState = true;
                        btnServicio.setBackgroundResource(R.drawable.estilo_btnpararcoordenadas);
                        btnServicio.setText("PARAR");
                        comenzarServicio();

                    } else if (flagState == true) {
                        flagState = false;
                        btnServicio.setText("INICIAR");
                        btnServicio.setBackgroundResource(R.drawable.estilo_btniniciarcoordenadas);
                        pararServicio();


                    }
                }

            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                // Permiso aceptado
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    comenzarServicio();
                    // Permiso denegado
                } else {

                    Toast.makeText(this, "No se ha aceptado el permiso. Debe reiniciar la App y aceptar los permisos." +
                            " Acceda a los ajustes de la aplicacion." +
                            "(Ajustes-> Aplicaciones-> WauC -> Permisos -> Permitir Ubicacion", Toast.LENGTH_LONG).show();
                }

                // Gestionar el resto de permisos
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void comenzarServicio() {

        Intent i = new Intent(getApplicationContext(), ServicioEmpleado.class);
        i.setAction(Constantes.START_SERVICIO_LOCALIZACION);
        i.putExtra("dniUsuarioActual", user.getDNI());
        startService(i);
        Toast.makeText(this, "PROCESO INICIADO. OBTENIENDO COORDENADAS", Toast.LENGTH_SHORT).show();

    }

    public void pararServicio() {

        Intent i = new Intent(getApplicationContext(), ServicioEmpleado.class);
        i.setAction(Constantes.STOP_SERVICIO_LOCALIZACION);
        startService(i);
        Toast.makeText(this, "PROCESO FINALIZADO", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        tvNomUsuarioActual.setText(user.getNombre());
    }

}