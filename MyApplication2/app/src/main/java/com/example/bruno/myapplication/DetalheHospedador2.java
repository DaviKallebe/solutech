package com.example.bruno.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.bruno.myapplication.retrofit.Hospedador;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DetalheHospedador2 extends Fragment implements View.OnClickListener {

    TextView limitePets;
    TextView tipoSupervisao;
    CheckBox cuidaIdoso;
    CheckBox cuidaFilhote;
    CheckBox cuidaFemea;
    CheckBox cuidaMacho;
    CheckBox cuidaCastrado;
    CheckBox cuidaGrande;
    CheckBox cuidaPequeno;
    CheckBox cuidaExotico;
    CheckBox cuidaCachorro;
    CheckBox cuidaGato;
    CheckBox cuidaMamifero;
    CheckBox cuidaReptil;
    CheckBox cuidaAve;
    CheckBox cuidaPeixe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detalhe_hospedador2, container, false);
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
            limitePets = rootView.findViewById(R.id.fragment_hosp_detalhe_limitePets);
            tipoSupervisao = rootView.findViewById(R.id.fragment_hosp_detalhe_tipoSupervisao);
            cuidaIdoso = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoIdoso);
            cuidaFilhote = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoFilhote);
            cuidaFemea = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoFemea);
            cuidaMacho = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoMacho);
            cuidaCastrado = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoCastrado);
            cuidaGrande = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoAnimaisGrandes);
            cuidaPequeno = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoAnimaisPequenos);
            cuidaExotico = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoAnimaisExoticos);
            cuidaCachorro = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoCachorro);
            cuidaGato = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoGato);
            cuidaMamifero = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoAnimaisMamiferos);
            cuidaReptil = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoRepteis);
            cuidaAve = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoAves);
            cuidaPeixe = rootView.findViewById(R.id.fragment_hosp_detalhe_restricaoAnimaisPeixes);

            limitePets.setOnClickListener(this);
            tipoSupervisao.setOnClickListener(this);
            cuidaIdoso.setOnClickListener(this);
            cuidaFilhote.setOnClickListener(this);
            cuidaFemea.setOnClickListener(this);
            cuidaMacho.setOnClickListener(this);
            cuidaCastrado.setOnClickListener(this);
            cuidaGrande.setOnClickListener(this);
            cuidaPequeno.setOnClickListener(this);
            cuidaExotico.setOnClickListener(this);
            cuidaCachorro.setOnClickListener(this);
            cuidaGato.setOnClickListener(this);
            cuidaMamifero.setOnClickListener(this);
            cuidaReptil.setOnClickListener(this);
            cuidaAve.setOnClickListener(this);
            cuidaPeixe.setOnClickListener(this);

            cuidaIdoso.setChecked(hospedador.getCuidaIdoso());
            cuidaFilhote.setChecked(hospedador.getCuidaFilhote());
            cuidaFemea.setChecked(hospedador.getCuidaFemea());
            cuidaMacho.setChecked(hospedador.getCuidaMacho());
            cuidaCastrado.setChecked(hospedador.getCuidaCastrado());
            cuidaGrande.setChecked(hospedador.getCuidaGrande());
            cuidaPequeno.setChecked(hospedador.getCuidaPequeno());
            cuidaExotico.setChecked(hospedador.getCuidaExotico());
            cuidaCachorro.setChecked(hospedador.getCuidaCachorro());
            cuidaGato.setChecked(hospedador.getCuidaGato());
            cuidaMamifero.setChecked(hospedador.getCuidaMamifero());
            cuidaReptil.setChecked(hospedador.getCuidaReptil());
            cuidaAve.setChecked(hospedador.getCuidaAve());
            cuidaPeixe.setChecked(hospedador.getCuidaPeixe());

            List<String> supervisao = new ArrayList<>();

            supervisao.add("A cada 12h");
            supervisao.add("A cada 08h");
            supervisao.add("A cada 04h");

            if (hospedador.getTipoSupervisao() != null)
                tipoSupervisao.setText(supervisao.get(hospedador.getTipoSupervisao()));

            if (hospedador.getQuantidadeAnimal() != null)
                limitePets.setText(String.valueOf(hospedador.getQuantidadeAnimal()));
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_hosp_detalhe_limitePets:
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
