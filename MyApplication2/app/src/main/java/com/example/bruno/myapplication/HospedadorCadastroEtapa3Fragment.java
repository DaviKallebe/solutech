package com.example.bruno.myapplication;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bruno.myapplication.retrofit.Hospedador;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okio.Buffer;

public class HospedadorCadastroEtapa3Fragment extends Fragment {

    private MainActivityViewModel mViewModel;

    Spinner quantidadeAnimal;
    CheckBox cuidaCachorro;
    CheckBox cuidaGato;
    CheckBox cuidaMamifero;
    CheckBox cuidaReptil;
    CheckBox cuidaAve;
    CheckBox cuidaPeixe;

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
        View rootView = inflater.inflate(R.layout.fragment_hospedador_cadastro_etapa3, container, false);

        setHasOptionsMenu(true);

        quantidadeAnimal = rootView.findViewById(R.id.fragment_hospedado_cadastro_quantidadeDeAnimais);
        cuidaCachorro = rootView.findViewById(R.id.fragment_hospedado_cadastro_preferenciaCachorro);
        cuidaGato = rootView.findViewById(R.id.fragment_hospedado_cadastro_preferenciaGatos);
        cuidaMamifero = rootView.findViewById(R.id.fragment_hospedado_cadastro_preferenciaMamiferos);
        cuidaReptil = rootView.findViewById(R.id.fragment_hospedado_cadastro_preferenciaRepteis);
        cuidaAve = rootView.findViewById(R.id.fragment_hospedado_cadastro_preferenciaAves);
        cuidaPeixe = rootView.findViewById(R.id.fragment_hospedado_cadastro_preferenciaPeixes);

        Context context = getContext();

        if (context != null) {
            Button cadastrar = rootView.findViewById(
                    R.id.fragment_hospedado_cadastro_action_cadastrar);

            cadastrar.setOnClickListener((View v) -> {
                if (checkFields()) {
                    Hospedador hospedador = new Hospedador();

                    hospedador.setQuantidadeAnimal((Integer)quantidadeAnimal.getSelectedItem());
                    hospedador.setCuidaCachorro(cuidaCachorro.isChecked());
                    hospedador.setCuidaGato(cuidaGato.isChecked());
                    hospedador.setCuidaMamifero(cuidaMamifero.isChecked());
                    hospedador.setCuidaReptil(cuidaReptil.isChecked());
                    hospedador.setCuidaAve(cuidaAve.isChecked());
                    hospedador.setCuidaPeixe(cuidaPeixe.isChecked());

                    mViewModel.updateHospedadorCadastro(hospedador);

                    AppCompatActivity activity = (AppCompatActivity) getActivity();

                    if (activity != null) {
                        ProgressDialog dialog = ProgressDialog.show(activity, "",
                                getResources().getString(R.string.salvando), true);

                        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        dialog.show();

                        Disposable disposable = mViewModel.createHospedador()
                                .subscribe((Hospedador resp) -> {
                                    Toast.makeText(context,
                                            "Parabéns seus cadastro foi realizado com sucesso!",
                                            Toast.LENGTH_LONG).show();

                                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    dialog.hide();

                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();

                                    if (fragmentManager != null) {
                                        fragmentManager.popBackStackImmediate(
                                                getResources().getString(R.string.perfil_fragment), 0);
                                    }
                                }, (Throwable error) -> {
                                    Toast.makeText(context,
                                            "Aconteceu um erro durante a tentativa de cadastro!",
                                            Toast.LENGTH_LONG).show();

                                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    dialog.hide();

                                });

                        CompositeDisposable compositeDisposable = new CompositeDisposable();
                        compositeDisposable.add(disposable);
                    }
                }
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

}
