package com.example.bruno.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bruno.myapplication.R;
import com.example.bruno.myapplication.retrofit.Pet;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListagemPetAdapter extends RecyclerView.Adapter<ListagemPetAdapter.CustomViewHolder> {

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    private List<Pet> pets;
    private Context context;
    private OnItemClicked onClick;

    public ListagemPetAdapter(List<Pet> pets, Context context, OnItemClicked onClick) {
        this.pets = pets;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.fragment_listagem_pet_recycler, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Pet pet = pets.get(position);

        holder.textNome.setText(pet.getNome());

        if (pet.getImagem() != null)
            Picasso.get().load(pet.getImagem())
                    .into(holder.imagePerfil);
    }

    public Pet getItem(Integer position) {
        return pets.get(position);
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView imagePerfil;
        TextView textNome;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.imagePerfil = itemView.findViewById(R.id.listagem_pet_image_perfil);
            this.textNome = itemView.findViewById(R.id.listagem_pet_text_nome);

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
