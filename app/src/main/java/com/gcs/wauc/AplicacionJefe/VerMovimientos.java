package com.gcs.wauc.AplicacionJefe;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.gcs.wauc.Aplicacion.Login;
import com.gcs.wauc.Modelo.Movimiento;
import com.gcs.wauc.R;
import com.gcs.wauc.Retrofit.RestEngine;
import com.gcs.wauc.Retrofit.RetrofitInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerMovimientos extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ArrayList<Movimiento> movimientos;
    String dniU;
    String fecha;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_movimientos);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        retrofitInterface = RestEngine.getRetrofit().create(RetrofitInterface.class);

        movimientos = new ArrayList<>();

        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
              dniU =  parametros.getString("dniU");
              fecha = parametros.getString("fecha");
        }else{
            dniU = "error";
            fecha = "error";
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        HashMap<String, String> map = new HashMap<>();

        map.put("dniU", dniU);
        map.put("fecha",fecha);


        Call<List<Movimiento>> call = retrofitInterface.movimientosUsuario(map);
        call.enqueue(new Callback<List<Movimiento>>() {

            @Override
            public void onResponse(Call<List<Movimiento>> call, Response<List<Movimiento>> response) {

                Toast.makeText(VerMovimientos.this, "nº de movimientos: "+ response.body().size() ,Toast.LENGTH_LONG).show();

                movimientos.addAll(response.body());

                for(int i =0; i < movimientos.size();i++){

                    LatLng coordenadas = new LatLng(movimientos.get(i).getLatitud(), movimientos.get(i).getLongitud());
                    googleMap.addMarker(new MarkerOptions()
                            .position(coordenadas)
                            .title("Movimiento nº "+(i+1) + " "+movimientos.get(i).getHora()));

                }

/*
                Instant instant = response.body().getFecha().toInstant();
                ZoneId z = ZoneId.of( "Europe/Madrid" );
                ZonedDateTime zdt = instant.atZone( z );

                Toast.makeText(VerMovimientos.this, ""+zdt
                        ,Toast.LENGTH_LONG).show();*/

            }

            @Override
            public void onFailure(Call<List<Movimiento>> call, Throwable t) {
                Toast.makeText(VerMovimientos.this, "ERROR-> "+t
                        ,Toast.LENGTH_LONG).show();
            }
        });


        LatLngBounds ubicacionMedida = new LatLngBounds(
                new LatLng(37.89151, -4.79773),
                new LatLng(37.89194, -4.79765));

        //Punto exacto de nuestro recinto
        LatLng ubicacionEmpresa = ubicacionMedida.getCenter();

        GroundOverlayOptions imagenEstructura = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.raw.mapa))
                .position(ubicacionEmpresa, 20f, 50f).bearing(-15);

        mMap.addGroundOverlay(imagenEstructura);

        camaraFoco(-15f, ubicacionEmpresa, mMap);

    }

    private void camaraFoco(float bearing, LatLng position, GoogleMap map) {
        CameraPosition currentPlace = new CameraPosition.Builder()
                .target(position)
                .bearing(bearing).tilt(0f).zoom(20.65f).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));

        map.setMinZoomPreference(20.30f);
        map.setMaxZoomPreference(20.55f);
        map.getUiSettings().setAllGesturesEnabled(false);



    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(!hasFocus){
            finish();
        }

    }
}