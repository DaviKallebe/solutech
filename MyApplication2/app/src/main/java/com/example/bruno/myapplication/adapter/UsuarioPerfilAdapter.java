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
import com.example.bruno.myapplication.commons.PerfilOpcoes;

import java.util.List;

public class UsuarioPerfilAdapter extends RecyclerView.Adapter<UsuarioPerfilAdapter.CustomViewHolder> {

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    private List<PerfilOpcoes> opcoes;
    private Context context;
    private OnItemClicked onClick;

    public UsuarioPerfilAdapter(List<PerfilOpcoes> opcoes, Context context, OnItemClicked onClick) {
        this.opcoes = opcoes;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_usuario_perfil_reclycer, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        PerfilOpcoes opt = opcoes.get(position);
        String name = opt.getName();
        String value = opt.getValueAsString();

        holder.textViewLabel.setText(name);
        holder.textViewField.setText(value);

        if (opt.getTypeField() != null && opt.getTypeField() == 6) {
            holder.textViewField.setSingleLine(false);
            holder.textViewField.setLines(5);
        }
    }

    @Override
    public int getItemCount() {
        return opcoes.size();
    }

    public PerfilOpcoes getItem(int position) {
        return opcoes.get(position);
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewLabel;
        TextView textViewField;

        public CustomViewHolder(View itemView) {
            super(itemView);

            textViewLabel = itemView.findViewById(R.id.label_primeiro_nome);
            textViewField = itemView.findViewById(R.id.textview_primeiro_nome);

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
