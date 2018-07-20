package com.example.bruno.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruno.myapplication.R;
import com.example.bruno.myapplication.retrofit.Pet;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NovoServicoFormularioAdapter extends RecyclerView.Adapter<NovoServicoFormularioAdapter.CustomViewHolder> {

    private List<Pet> pets;
    private Context context;
    private OnItemClicked onClick;

    public NovoServicoFormularioAdapter(List<Pet> pets, Context context) {
        this.pets = pets;
        this.context = context;
    }

    public NovoServicoFormularioAdapter(List<Pet> pets, Context context, OnItemClicked onClick) {
        this.pets = pets;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_novo_servico_formulario_recycler, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Pet pet = pets.get(position);


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checkBox;
        CircleImageView imageViewNome;
        TextView textViewNome;

        public CustomViewHolder(View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.recycler_novo_servico_check_selected);
            imageViewNome = itemView.findViewById(R.id.recycler_novo_servico_image_pet);
            textViewNome = itemView.findViewById(R.id.recycler_novo_servico_nome);

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
