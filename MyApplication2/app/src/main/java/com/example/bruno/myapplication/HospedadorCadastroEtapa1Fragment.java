package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruno.myapplication.commons.BrDataFormatter;
import com.example.bruno.myapplication.commons.BrPhoneNumberFormatter;
import com.example.bruno.myapplication.retrofit.Hospedador;

import java.lang.ref.WeakReference;
import java.util.Calendar;

public class HospedadorCadastroEtapa1Fragment extends Fragment {

    EditText primeiroNome;
    EditText ultimoNome;
    EditText rg;
    EditText orgaoEmissor;
    EditText cpf;
    EditText nascimento;
    EditText telefone;

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
        View rootView = inflater.inflate(R.layout.fragment_hospedador_cadastro_etapa1, container, false);

        setHasOptionsMenu(true);

        primeiroNome = rootView.findViewById(R.id.fragment_hospedador_cadastro_primeironome);
        ultimoNome = rootView.findViewById(R.id.fragment_hospedador_cadastro_ultimonome);
        rg = rootView.findViewById(R.id.fragment_hospedador_cadastro_rg);
        orgaoEmissor = rootView.findViewById(R.id.fragment_hospedador_cadastro_orgaoemissor);
        cpf = rootView.findViewById(R.id.fragment_hospedador_cadastro_cpf);
        nascimento = rootView.findViewById(R.id.fragment_hospedador_cadastro_nascimento);
        telefone = rootView.findViewById(R.id.fragment_hospedador_cadastro_telefone);

        Context context = getContext();

        if (context != null) {
            FloatingActionButton fab_arrow_forward = rootView.findViewById(
                    R.id.fragment_hospedador_cadastro_etapa1_arrow_forward);
            fab_arrow_forward.setOnClickListener(v -> {
                if (checkFields()) {
                    HospedadorCadastroEtapa2Fragment cadastroEtapa2Fragment = new HospedadorCadastroEtapa2Fragment();

                    Hospedador hospedador = new Hospedador();

                    hospedador.setPrimeiroNome(primeiroNome.getText().toString());
                    hospedador.setUltimoNome(ultimoNome.getText().toString());
                    hospedador.setRg(rg.getText().toString());
                    hospedador.setOrgaoEmissor(orgaoEmissor.getText().toString());
                    hospedador.setCpf(cpf.getText().toString());
                    hospedador.setNascimento(nascimento.getText().toString());
                    hospedador.setTelefone(telefone.getText().toString());

                    mViewModel.updateHospedadorCadastro(hospedador);

                    goToFragment(cadastroEtapa2Fragment);
                } else
                    Toast.makeText(context, getResources().getString(R.string.empty_fields),
                            Toast.LENGTH_SHORT).show();
            });
        }

        nascimento.addTextChangedListener(new BrDataFormatter(
                new WeakReference<>(nascimento)));

        telefone.addTextChangedListener(new BrPhoneNumberFormatter(
                new WeakReference<>(telefone)));

        return rootView;
    }

    public Boolean checkFields() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null)
            activity.setTitle(getResources().getString(R.string.fragment_hospedador));
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void goToFragment(Fragment fragmentDestination) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();

            if (fragmentManager != null) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                        .replace(R.id.fragment_hospedador_cadastro_etapa1,
                                fragmentDestination,
                                fragmentDestination.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
}
