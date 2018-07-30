package com.example.bruno.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bruno.myapplication.R;
import com.example.bruno.myapplication.retrofit.Hospedagem;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HospedadorServicoAdapter extends RecyclerView.Adapter<HospedadorServicoAdapter.CustomViewHolder> {

    private List<Hospedagem> hospedagens;
    private Context context;
    private OnItemClicked onClick;

    public HospedadorServicoAdapter(List<Hospedagem> hospedagens, Context context, OnItemClicked onClick) {
        this.hospedagens = hospedagens;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(
                R.layout.fragment_hospedador_servico_recycler, parent, false);

        return new CustomViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Hospedagem hospedagem = hospedagens.get(position);

        holder.nome.setText(hospedagem.getNomeCompleto());
        holder.status.setText(String.format("Status: %1$s",
                hospedagem.getStatus() == 1? "Ativo" : "Cancelado"));
        holder.intervalo.setText(String.format("De %1$s até %2$s",
                hospedagem.getDataInicio(),
                hospedagem.getDataFim()));

        if (hospedagem.getImagem() != null)
            Picasso.get().load(hospedagem.getImagem()).into(holder.perfil);
    }

    @Override
    public int getItemCount() {
        return hospedagens.size();
    }

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView perfil;
        TextView nome;
        TextView status;
        TextView intervalo;

        public CustomViewHolder(View itemView) {
            super(itemView);

            perfil = itemView.findViewById(R.id.fragmento_hospedador_servico_perfil);
            nome = itemView.findViewById(R.id.fragmento_hospedador_servico_nome);
            status = itemView.findViewById(R.id.fragmento_hospedador_servico_status);
            intervalo = itemView.findViewById(R.id.fragmento_hospedador_servico_intervalo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (onClick != null)
                onClick.onItemClick(v, position);
        }
    }
}
