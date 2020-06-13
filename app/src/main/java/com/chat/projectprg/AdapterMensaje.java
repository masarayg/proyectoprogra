package com.chat.projectprg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.chat.projectprg.Entidades.MensajeRecibir;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AdapterMensaje extends RecyclerView.Adapter<HolderMensaje> {

 private   List<MensajeRecibir>  listMensaje = new ArrayList<>();

 private Context c;

    public AdapterMensaje (Context c) {
        this.c = c;
    }

    public void addMensaje (MensajeRecibir m)
    {
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @Override
    public HolderMensaje onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes,parent,false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder( HolderMensaje holder, int position) {
        holder.getNombre().setText(listMensaje.get(position).getNombre());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());
        holder.getMensaje().setVisibility(View.VISIBLE);

        long Codigohora = listMensaje.get(position).getHora();
        Date d = new Date(Codigohora);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        holder.getHora().setText(sdf.format(d));
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
