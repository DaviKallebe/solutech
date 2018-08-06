package com.example.bruno.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bruno.myapplication.retrofit.Hospedador;

import org.json.JSONException;
import org.json.JSONObject;


public class DetalheHospedador1 extends Fragment implements View.OnClickListener {

    private TextView primeiroNome;
    private TextView ultimoNome;
    private TextView rg;
    private TextView cpf;
    private TextView nascimento;
    private TextView telefone;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detalhe_hospedador1, container, false);

        Bundle bundle = getArguments();
        JSONObject json = null;
        Hospedador hospedador = new Hospedador();

        if (bundle != null) {
            try {
                json = new JSONObject(bundle.getString("hospedador"));
                hospedador.setFieldsByJson(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (getContext() != null && json != null) {
            primeiroNome = rootView.findViewById(R.id.fragment_hosp_detalhe_nome);
            ultimoNome = rootView.findViewById(R.id.fragment_hosp_detalhe_sobrenome);
            rg = rootView.findViewById(R.id.fragment_hosp_detalhe_rg);
            cpf = rootView.findViewById(R.id.fragment_hosp_detalhe_cpf);
            nascimento = rootView.findViewById(R.id.fragment_hosp_detalhe_dataNascimento);
            telefone = rootView.findViewById(R.id.fragment_hosp_detalhe_telefone);

            primeiroNome.setText(hospedador.getPrimeiroNome());
            ultimoNome.setText(hospedador.getUltimoNome());
            rg.setText(hospedador.getRg());
            cpf.setText(hospedador.getCpf());
            nascimento.setText(hospedador.getNascimento());
            telefone.setText(hospedador.getTelefone());

            primeiroNome.setOnClickListener(this);
            ultimoNome.setOnClickListener(this);
            rg.setOnClickListener(this);
            cpf.setOnClickListener(this);
            nascimento.setOnClickListener(this);
            telefone.setOnClickListener(this);
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_hosp_detalhe_nome:
                break;
        }
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
