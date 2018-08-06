package com.example.bruno.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bruno.myapplication.retrofit.Logradouro;

import org.json.JSONException;
import org.json.JSONObject;


public class DetalheLocal extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detalhe_local, container, false);

        //TextView cep = rootView.findViewById(R.id.localDetalhecep);
        TextView rua = rootView.findViewById(R.id.localDetalheRua);
        TextView numero = rootView.findViewById(R.id.localDetalheNumero);
        TextView bairro = rootView.findViewById(R.id.localDetalheBairro);
        TextView cidade = rootView.findViewById(R.id.localDetalheCidade);
        TextView estado = rootView.findViewById(R.id.localDetalheEstado);
        TextView complemento = rootView.findViewById(R.id.localDetalheComp);
        TextView tipo = rootView.findViewById(R.id.localDetalheTipoResidencia);
        TextView descricao = rootView.findViewById(R.id.localDetalheDescricao);

        Bundle bundle = getArguments();
        Context context = getContext();
        Logradouro logradouro = new Logradouro();
        JSONObject json = null;

        if (bundle != null) {
            try {
                json = new JSONObject(bundle.getString("logradouro"));
                logradouro.setFieldsByJson(json);

                rua.setText(logradouro.getRua());
                numero.setText(String.valueOf(logradouro.getNumero()));
                bairro.setText(logradouro.getBairro());
                cidade.setText(logradouro.getCidade());
                estado.setText(logradouro.getEstado());
                complemento.setText(logradouro.getComplemento());
                tipo.setText(logradouro.getTipo());
                descricao.setText(logradouro.getDescricao());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        setHasOptionsMenu(true);

        return rootView;
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
