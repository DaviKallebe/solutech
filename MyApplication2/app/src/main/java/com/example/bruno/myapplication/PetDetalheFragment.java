package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class PetDetalheFragment extends Fragment {
    private static final String param1_id_pet = "param1_id_pet";
    private static final int PET_PICK_IMAGE = 2;

    private MainActivityViewModel mViewModel;
    private Integer id_pet;

    public PetDetalheFragment() {
        // Required empty public constructor
    }

    public static PetDetalheFragment newInstance(Integer id_pet) {
        PetDetalheFragment fragment = new PetDetalheFragment();
        Bundle args = new Bundle();
        args.putInt(PetDetalheFragment.param1_id_pet, id_pet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity activity = getActivity();

        if (activity != null)
            mViewModel = ViewModelProviders.of(activity).get(MainActivityViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(
                R.layout.fragment_pet_detalhe, container, false);

        setHasOptionsMenu(true);

        Context context = this.getContext();
        Bundle args = getArguments();

        if (context != null && mViewModel != null && args != null) {
            id_pet = args.getInt(PetDetalheFragment.param1_id_pet);

            TextView nome = rootView.findViewById(R.id.fragment_pet_detalhe_nome);
            ImageView imagem = rootView.findViewById(R.id.fragment_pet_detalhe_imagem_perfil);
            TextView idade = rootView.findViewById(R.id.fragment_pet_detalhe_idade);
            TextView sexo = rootView.findViewById(R.id.fragment_pet_detalhe_sexo);
            TextView tamamho = rootView.findViewById(R.id.fragment_pet_detalhe_peso);
            TextView raca = rootView.findViewById(R.id.fragment_pet_detalhe_raca);
            TextView especie = rootView.findViewById(R.id.fragment_pet_detalhe_especie);
            TextView descricao = rootView.findViewById(R.id.fragment_pet_detalhe_descricao);

            mViewModel.getPet(id_pet).observe(this, pet -> {
                nome.setText(pet.getNome());
                idade.setText(pet.getIdade() != null ? Integer.toString(pet.getIdade()) : "");
                sexo.setText(pet.getSexo() != null ? pet.getSexo() == 1 ?
                        getResources().getString(R.string.fragment_pet_cadastro_macho) :
                        getResources().getString(R.string.fragment_pet_cadastro_femea) : "");
                tamamho.setText(pet.getTamanho() != null ? String.format(new Locale("pt", "BR"),
                        "%.2f kg", pet.getTamanho()) : "");
                raca.setText(pet.getRaca());
                especie.setText(pet.getEspecie());
                descricao.setText(pet.getOutros());

                if (pet.getImagem() != null)
                    Picasso.get().load(pet.getImagem()).into(imagem);

            });

            imagem.setOnClickListener(this::openImageSelector);
        }

        return rootView;
    }

    public void openImageSelector(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Selecione uma image"),
                PET_PICK_IMAGE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_pet_detalhe_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null)
            activity.setTitle(getResources().getString(R.string.fragment_pet));
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menu_id = item.getItemId();

        switch (menu_id) {
            case R.id.fragment_pet_detalhe_menu_editar:
                break;
            case R.id.fragment_pet_detalhe_menu_remover:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirmar a remoção")
                        .setMessage("Deseja remover esse pet da lista?")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            //mViewModel.
                        })
                        .setNegativeButton("No", null);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}
