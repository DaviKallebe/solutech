package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;

import com.example.bruno.myapplication.retrofit.Pet;

import static android.content.Context.MODE_PRIVATE;

public class PetCadastroFragment extends Fragment {

    private MainActivityViewModel mViewModel;
    private EditText nome;
    private EditText idade;
    private RadioButton macho;
    private RadioButton femea;
    private EditText especie;
    private EditText raca;
    private EditText peso;
    private Switch vacinado;
    private Switch castrado;
    private EditText descricao;
    private Integer id_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity activity = getActivity();

        if (activity != null)
            mViewModel = ViewModelProviders.of(activity).get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pet_cadastro, container, false);

        setHasOptionsMenu(true);

        Context context = getContext();

        if (context != null) {

            SharedPreferences prefs = context.getSharedPreferences("userfile", MODE_PRIVATE);
            id_user = prefs.getInt("id_user", 0);

            nome = rootView.findViewById(R.id.fragment_pet_cadastro_nome);
            idade = rootView.findViewById(R.id.fragment_pet_cadastro_idade);
            macho = rootView.findViewById(R.id.fragment_pet_cadastro_radio_macho);
            femea = rootView.findViewById(R.id.fragment_pet_cadastro_radio_femea);
            especie = rootView.findViewById(R.id.fragment_pet_cadastro_especie);
            raca = rootView.findViewById(R.id.fragment_pet_cadastro_raca);
            peso = rootView.findViewById(R.id.fragment_pet_cadastro_peso);
            vacinado = rootView.findViewById(R.id.fragment_pet_cadastro_vacinado);
            castrado = rootView.findViewById(R.id.fragment_pet_cadastro_castrado);
            descricao = rootView.findViewById(R.id.fragment_pet_cadastro_descricao);

            Button confirmar = rootView.findViewById(R.id.fragment_pet_cadastro_confirmar);
            confirmar.setOnClickListener(v -> {
                if (!isFieldsEmpty()) {
                    Pet pet = new Pet();

                    pet.setNome(nome.getText().toString());
                    pet.setIdade(Integer.valueOf(idade.getText().toString()));
                    pet.setSexo(macho.isChecked() ? 1 : 2);
                    pet.setEspecie(especie.getText().toString());
                    pet.setRaca(raca.getText().toString());
                    pet.setPeso(Double.valueOf(peso.getText().toString()));
                    pet.setVacinado(vacinado.isChecked());
                    pet.setCastrado(castrado.isChecked());
                    pet.setOutros(descricao.getText().toString());
                    pet.setId_user(id_user);

                    mViewModel.inserPet(pet);

                    AppCompatActivity activity = (AppCompatActivity) getActivity();

                    if (activity != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                        if (fragmentManager != null)
                            fragmentManager.popBackStackImmediate();
                    }
                }
            });
        }

        return rootView;
    }

    public boolean isFieldsEmpty() {
        if (idade.getText().toString().trim().isEmpty() ||
                peso.getText().toString().trim().isEmpty() ||
                nome.getText().toString().trim().isEmpty() ||
                especie.getText().toString().trim().isEmpty() ||
                raca.getText().toString().trim().isEmpty())
            return true;
        else
            return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
