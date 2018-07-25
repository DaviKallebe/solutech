package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bruno.myapplication.retrofit.Hospedador;

public class HospedadorCadastroEtapa2Fragment extends Fragment {

    private MainActivityViewModel mViewModel;

    Spinner tipoSupervisao;
    CheckBox cuidaIdoso;
    CheckBox cuidaFilhote;
    CheckBox cuidaFemea;
    CheckBox cuidaMacho;
    CheckBox cuidaCastrado;
    CheckBox cuidaGrande;
    CheckBox cuidaPequeno;
    CheckBox cuidaExotico;


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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hospedador_cadastro_etapa2, container, false);

        setHasOptionsMenu(true);

        tipoSupervisao = rootView.findViewById(R.id.fragment_hospedador_cadastro_tipoDeSupervisao);
        cuidaIdoso = rootView.findViewById(R.id.fragment_hospedador_cadastro_restricaoIdoso);
        cuidaFilhote = rootView.findViewById(R.id.fragment_hospedador_cadastro_restricaoFilhote);
        cuidaMacho = rootView.findViewById(R.id.fragment_hospedador_cadastro_restricaoMacho);
        cuidaFemea = rootView.findViewById(R.id.fragment_hospedador_cadastro_restricaoFemea);
        cuidaCastrado = rootView.findViewById(R.id.fragment_hospedador_cadastro_restricaoCastrado);
        cuidaPequeno = rootView.findViewById(R.id.fragment_hospedador_cadastro_restricaoAnimaisPequenos);
        cuidaGrande = rootView.findViewById(R.id.fragment_hospedador_cadastro_restricaoAnimaisGrandes);
        cuidaExotico = rootView.findViewById(R.id.fragment_hospedador_cadastro_restricaoAnimaisExoticos);

        Context context = getContext();

        if (context != null) {
            FloatingActionButton fab_arrow_forward = rootView.findViewById(
                    R.id.fragment_hospedador_cadastro_etapa2_arrow_forward);
            fab_arrow_forward.setOnClickListener(v -> {
                if (checkFields()) {
                    HospedadorCadastroEtapa3Fragment cadastroEtapa3Fragment = new HospedadorCadastroEtapa3Fragment();

                    Hospedador hospedador = new Hospedador();

                    hospedador.setTipoSupervisao(tipoSupervisao.getSelectedItemPosition());
                    hospedador.setCuidaIdoso(cuidaIdoso.isChecked());
                    hospedador.setCuidaFilhote(cuidaFilhote.isChecked());
                    hospedador.setCuidaMacho(cuidaMacho.isChecked());
                    hospedador.setCuidaFemea(cuidaFemea.isChecked());
                    hospedador.setCuidaCastrado(cuidaCastrado.isChecked());
                    hospedador.setCuidaPequeno(cuidaPequeno.isChecked());
                    hospedador.setCuidaGrande(cuidaGrande.isChecked());
                    hospedador.setCuidaExotico(cuidaExotico.isChecked());

                    mViewModel.updateHospedadorCadastro(hospedador);

                    goToFragment(cadastroEtapa3Fragment);
                }
                else
                    Toast.makeText(context, getResources().getString(R.string.empty_fields),
                            Toast.LENGTH_LONG).show();
            });
        }

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
        int menu_id = item.getItemId();

        switch (menu_id) {
            case android.R.id.home:
                AppCompatActivity activity = (AppCompatActivity) getActivity();

                if (activity != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    if (fragmentManager != null)
                        fragmentManager.popBackStackImmediate();
                }

                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void goToFragment(Fragment fragmentDestination) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();

            if (fragmentManager != null) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                        .replace(R.id.fragment_hospedador_cadastro_etapa2,
                                fragmentDestination,
                                fragmentDestination.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

}
