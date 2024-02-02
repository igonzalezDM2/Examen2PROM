package com.examen.examen2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorElemento extends ArrayAdapter<Elemento> {
    public AdaptadorElemento(@NonNull Context context, @NonNull List<Elemento> objects) {
        super(context, R.layout.elemento_layout, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Elemento elemento = getItem(position);
        ViewHolder holder;

        View item = convertView;
        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.elemento_layout, null);
            holder = new ViewHolder();
            holder.nombre = item.findViewById(R.id.tvNombre);
            holder.simbolo = item.findViewById(R.id.tvSimbolo);
            holder.numero = item.findViewById(R.id.tvNumero);
            holder.estado = item.findViewById(R.id.tvEstado);
            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        TextView nom = holder.nombre;
        nom.setText(elemento.getNombre());

        TextView sim = holder.simbolo;
        sim.setText(elemento.getSimbolo());

        TextView num = holder.numero;
        num.setText(""+elemento.getNumero());

        TextView est = holder.estado;
        est.setText(elemento.getEstado().toString());

        return item;
    }

    private static class ViewHolder {
        TextView nombre, simbolo, numero, estado;
    }
}
