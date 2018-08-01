package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.EditText;

import com.example.bruno.myapplication.retrofit.Logradouro;

public class CadastrarLogradouroEtapa1 extends Fragment {

    private MainActivityViewModel mViewModel;

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
        View rootView = inflater.inflate(R.layout.fragment_cadastrar_logradouro_etapa1, container, false);

        EditText cep = rootView.findViewById(R.id.cadastrar_logradouro_cep);
        EditText rua = rootView.findViewById(R.id.cadastro_logradouro_lograoduro);
        EditText numero = rootView.findViewById(R.id.cadastro_logradouro_numero);
        EditText bairro = rootView.findViewById(R.id.cadastro_logradouro_bairro);
        EditText cidade = rootView.findViewById(R.id.cadastro_logradouro_cidade);
        EditText estado = rootView.findViewById(R.id.cadastro_logradouro_estado);
        EditText complemento = rootView.findViewById(R.id.cadastro_logradouro_complemento);

        FloatingActionButton fab_arrow_forward = rootView.findViewById(R.id.cadastro_logrouro_fab);
        fab_arrow_forward.setOnClickListener((View v) -> {
            if (checkFields()) {
                CadastrarLogradouroEtapa2 cadastrarLogradouroEtapa2 =
                        new CadastrarLogradouroEtapa2();

                Logradouro logradouro = new Logradouro();

                logradouro.setCep(cep.getText().toString());
                logradouro.setRua(rua.getText().toString());
                logradouro.setBairro(bairro.getText().toString());
                logradouro.setCidade(cidade.getText().toString());
                logradouro.setEstado(estado.getText().toString());
                logradouro.setComplemento(complemento.getText().toString());

                if (!numero.getText().toString().equals(""))
                    logradouro.setNumero(Integer.valueOf(numero.getText().toString()));

                Bundle bundle = new Bundle();
                bundle.putString("logradouro", logradouro.getFieldsJson().toString());

                cadastrarLogradouroEtapa2.setArguments(bundle);

                goToFragment(cadastrarLogradouroEtapa2);
            }
        });

        setHasOptionsMenu(true);

        return rootView;
    }

    public Boolean checkFields() {
        return true;
    }

    public void goToFragment(Fragment fragmentDestination) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();

            if (fragmentManager != null) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                        .replace(R.id.fragment_cadastrar_logradouro_etapa1,
                                fragmentDestination,
                                fragmentDestination.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null)
            activity.setTitle(getResources().getString(R.string.fragment_logradouro));
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
