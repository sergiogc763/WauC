package com.gcs.wauc.AplicacionJefe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.gcs.wauc.R;
import com.gcs.wauc.Modelo.Usuario;

import java.util.ArrayList;

public class AdapterListaEmpleados extends RecyclerView.Adapter {

    ArrayList<Usuario> listaUsuarios;
    Context contexto;

    public AdapterListaEmpleados(Context contexto, ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_empleados, null, false);

        return new ViewHolderDatos(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolderDatos) holder).asignarDatos(listaUsuarios.get(position));

        ((ViewHolderDatos) holder).layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(contexto, listaUsuarios.get(position).getDNI(), Toast.LENGTH_LONG).show();

                final CharSequence[] opciones = new CharSequence[3];
                opciones[0] = "Mostrar fechas de los movimientos";
                opciones[1] = "Llamar";
                opciones[2] = "Cancelar";

                AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                builder.setTitle("Opciones: " + listaUsuarios.get(position).getNombre())
                        .setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int posicion) {

                                if (posicion == 0) {
                                    Intent i = new Intent(contexto, ListaFechasEmpleado.class);
                                    i.putExtra("DatosEmpleado", listaUsuarios.get(position));
                                    contexto.startActivity(i);

                                } else if (posicion == 1) {
                                    String telefono = String.valueOf(listaUsuarios.get(position).getTelefono());
                                    String dial = "tel:+34" + telefono;
                                    Intent i = new Intent(android.content.Intent.ACTION_DIAL,
                                            Uri.parse(dial)); //
                                    contexto.startActivity(i);


                                } else if (posicion == 2) {
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
        return listaUsuarios.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView nombreE, telefonoE, dniE;
        View layout;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombreE = (TextView) itemView.findViewById(R.id.nomUsuarioLista);
            telefonoE = (TextView) itemView.findViewById(R.id.telUsuarioLista);
            dniE = (TextView) itemView.findViewById(R.id.dniUsuarioLista);
            layout = itemView.findViewById(R.id.layoutItemUsuarios);
        }

        public void asignarDatos(Usuario usuario) {
            nombreE.setText(usuario.getNombre());
            telefonoE.setText(usuario.getTelefono() + "");
            dniE.setText(usuario.getDNI());
        }

    }


}
