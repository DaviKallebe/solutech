package com.example.bruno.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruno.myapplication.R;
import com.example.bruno.myapplication.retrofit.Comentario;
import com.example.bruno.myapplication.retrofit.Mensagem;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HospedadorListagemDetalhePefilAdapter extends RecyclerView.Adapter<HospedadorListagemDetalhePefilAdapter.CustomViewHolder> {

    private List<Comentario> comentarios;
    private Context context;
    private OnItemClicked onClick;

    public HospedadorListagemDetalhePefilAdapter(List<Comentario> comentarios, Context context, OnItemClicked onClick) {
        this.comentarios = comentarios;
        this.context = context;
        this.onClick = onClick;
    }

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_hospedador_listagem_detalhe_perfil_recycler, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Comentario comentario = comentarios.get(position);

        if (comentario.getImagem() != null)
            Picasso.get().load(comentario.getImagem()).into(holder.imagePerfil);

        Spannable comment = new SpannableString(
                comentario.getFullName() + ' ' + comentario.getComentario());
        comment.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                comentario.getFullName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.textComentario.setText(comment, TextView.BufferType.SPANNABLE);
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView imagePerfil;
        TextView textComentario;

        public CustomViewHolder(View itemView) {
            super(itemView);

            imagePerfil = itemView.findViewById(R.id.imageViewUsuarioDetalhadoPerfilComentario);
            textComentario = itemView.findViewById(R.id.textViewUsuarioDetalhadoComentario);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClick != null) {
                Integer position = getAdapterPosition();
                onClick.onItemClick(v, position);
            }
        }
    }
}
