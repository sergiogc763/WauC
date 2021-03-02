package com.gcs.wauc.AplicacionJefe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.gcs.wauc.R;

import java.util.ArrayList;

public class AdapterListaFechasEmpleado extends RecyclerView.Adapter {

    ArrayList<String> listaFechas;
    Context contexto;
    String dniU;

    public AdapterListaFechasEmpleado(Context contexto, ArrayList<String> listaFechas, String dniU) {
        this.listaFechas = listaFechas;
        this.contexto = contexto;
        this.dniU = dniU;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_fechas_empleado, null, false);

        return new AdapterListaFechasEmpleado.ViewHolderDatos(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ViewHolderDatos) holder).asignarFechas(listaFechas.get(position));
        ((ViewHolderDatos) holder).layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] opciones = new CharSequence[2];
                opciones[0] = "Ver movimientos";
                opciones[1] = "Cancelar";

                AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                builder.setTitle("Opciones")
                        .setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int posicion) {

                                if (posicion == 0) {
                                    Intent i = new Intent(contexto, VerMovimientos.class);
                                    i.putExtra("dniU", dniU);
                                    i.putExtra("fecha", listaFechas.get(position));
                                    contexto.startActivity(i);

                                } else if (posicion == 1) {
                                    Toast.makeText(contexto, "OPERACION CANCELADA...", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                builder.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return listaFechas.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView itemFecha;
        View layout;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            itemFecha = (TextView) itemView.findViewById(R.id.itemFecha);
            layout = itemView.findViewById(R.id.layoutFecha);
        }

        public void asignarFechas(String fecha) {
            itemFecha.setText(fecha);

        }
    }
}
