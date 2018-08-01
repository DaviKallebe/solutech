package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruno.myapplication.retrofit.Logradouro;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;


public class CadastrarLogradouroEtapa2 extends Fragment {

    private MainActivityViewModel mViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null)
            mViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cadastrar_logradouro_etapa2, container, false);

        Bundle bundle = getArguments();
        Context context = getContext();
        Logradouro logradouro = new Logradouro();
        JSONObject json = null;

        if (bundle != null) {
            try {
                json = new JSONObject(bundle.getString("logradouro"));
                logradouro.setFieldsByJson(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (context != null && json != null) {
            EditText tipo = rootView.findViewById(R.id.cadastro_logradouro_tipo);
            EditText descricao = rootView.findViewById(R.id.cadastro_logradouro_descricao);
            Button cadastrar = rootView.findViewById(R.id.cadastro_logradouro_cadastrar);

            SharedPreferences prefs = context.getSharedPreferences("userfile", MODE_PRIVATE);
            Integer id_user = prefs.getInt("id_user", 0);

            cadastrar.setOnClickListener((View v) -> {
                logradouro.setTipo(tipo.getText().toString());
                logradouro.setDescricao(descricao.getText().toString());
                logradouro.setId_user(id_user);

                Disposable disposable = mViewModel
                        .criarLogradouro(logradouro, null)
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe((Logradouro lg) -> {
                            if (getActivity() != null)
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(context,
                                            "Cadastro sucesso",
                                            Toast.LENGTH_SHORT).show();

                                    if (getFragmentManager() != null)
                                        getFragmentManager().popBackStackImmediate(
                                                getResources().getString(R.string.perfil_fragment), 0);

                                });
                        }, Throwable::printStackTrace);

                CompositeDisposable compositeDisposable = new CompositeDisposable();
                compositeDisposable.add(disposable);
            });

        }

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        if (getActivity() != null)
            getActivity().setTitle(getResources().getString(R.string.fragment_logradouro));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
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
