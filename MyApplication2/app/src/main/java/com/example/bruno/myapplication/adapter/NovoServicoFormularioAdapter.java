package com.example.bruno.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.bruno.myapplication.R;
import com.example.bruno.myapplication.retrofit.Pet;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NovoServicoFormularioAdapter extends RecyclerView.Adapter<NovoServicoFormularioAdapter.CustomViewHolder> {

    private List<Pet> pets;
    private Context context;
    private OnItemClicked onClick;
    private SparseArray<Integer> checkedPets;

    public NovoServicoFormularioAdapter(List<Pet> pets, Context context, OnItemClicked onClick) {
        this.pets = pets;
        this.context = context;
        this.onClick = onClick;

        checkedPets = new SparseArray<>();
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

        holder.textViewNome.setText(pet.getNome());

        if (pet.getImagem() != null)
            Picasso.get().load(pet.getImagem()).into(holder.imageViewNome);

        holder.checkBox.setChecked(false);
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedPets.put(position, pet.getId_pet());
            }
            else
                checkedPets.put(position, pet.getId_pet());

            if (onClick != null)
                onClick.onCheckClick(checkedPets);
        });
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public Pet getItem(Integer position) {
        return pets.get(position);
    }

    public SparseArray<Integer> getCheckedPets() {
        return checkedPets;
    }

    public interface OnItemClicked {
        void onItemClick(View view, int position);
        void onCheckClick(SparseArray<Integer> sparseArray);
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

            if (onClick != null) {
                checkBox.setChecked(!checkBox.isChecked());

                onClick.onCheckClick(checkedPets);

                //onClick.onItemClick(view, position);
            }
        }
    }
}
