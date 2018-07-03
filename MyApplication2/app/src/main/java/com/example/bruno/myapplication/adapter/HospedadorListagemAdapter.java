package com.example.bruno.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruno.myapplication.R;
import com.example.bruno.myapplication.retrofit.Usuario;

import java.util.List;

public class HospedadorListagemAdapter extends RecyclerView.Adapter<HospedadorListagemAdapter.CustomViewHolder> {

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    private OnItemClicked onClick;
    private List<Usuario> usuarios;
    private Context context;

    public HospedadorListagemAdapter(List<Usuario> usuarios, Context context) {
        this.usuarios = usuarios;
        this.context = context;
    }

    public HospedadorListagemAdapter(List<Usuario> usuarios, Context context, OnItemClicked onClick) {
        this.usuarios = usuarios;
        this.context = context;
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return this.usuarios.size();
    }

    public Usuario getItem(int position) {
        return this.usuarios.get(position);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.customlayouthosp, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Usuario user = usuarios.get(position);

        holder.textViewNome.setText(user.getFullName());
        holder.textViewDesc.setText(user.getDescricao());
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textViewNome;
        TextView textViewDesc;

        public CustomViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewH);
            textViewNome = itemView.findViewById(R.id.textViewNomeH);
            textViewDesc = itemView.findViewById(R.id.textViewDescricaoH);

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