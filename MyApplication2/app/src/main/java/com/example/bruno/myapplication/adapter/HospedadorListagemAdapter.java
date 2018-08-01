package com.example.bruno.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruno.myapplication.R;
import com.example.bruno.myapplication.retrofit.Hospedador;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HospedadorListagemAdapter extends RecyclerView.Adapter<HospedadorListagemAdapter.CustomViewHolder> {

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    private OnItemClicked onClick;
    private List<Hospedador> hospedadores;
    private Context context;

    public HospedadorListagemAdapter(List<Hospedador> hospedadores, Context context, OnItemClicked onClick) {
        this.hospedadores = hospedadores;
        this.context = context;
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return this.hospedadores.size();
    }

    public Hospedador getItem(int position) {
        return this.hospedadores.get(position);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_hospedador_listagem_recycler, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Hospedador hospedador = hospedadores.get(position);

        holder.textViewNome.setText(hospedador.getFullName());
        holder.textViewDesc.setText(hospedador.getDescricao());

        if (hospedador.getUsuarioNota() != null) {
            holder.textViewNota.setText(String.format(new Locale("pt", "BR"),
                    "%.1f", hospedador.getUsuarioNota()));
        }
        else {
            holder.textViewNota.setVisibility(View.GONE);
            holder.star.setVisibility(View.GONE);
        }


        if (hospedador.getImagem() != null)
            Picasso.get().load(hospedador.getImagem()).into(holder.circleImagemPerfil);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView circleImagemPerfil;
        TextView textViewNome;
        TextView textViewDesc;
        ImageView star;
        TextView textViewNota;

        public CustomViewHolder(View itemView) {
            super(itemView);

            circleImagemPerfil = itemView.findViewById(R.id.fragment_hospedador_listagem_imagemperfil);
            textViewNome = itemView.findViewById(R.id.textViewNomeH);
            textViewDesc = itemView.findViewById(R.id.textViewDescricaoH);
            star = itemView.findViewById(R.id.imageViewStar);
            textViewNota = itemView.findViewById(R.id.textViewNota);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (onClick != null)
                onClick.onItemClick(view, position);
        }
    }
}