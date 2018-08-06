package com.example.bruno.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.bruno.myapplication.retrofit.Hospedador;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopupFiltro extends DialogFragment implements View.OnClickListener {

    private Spinner tipoSupervisao;
    private CheckBox cuidaCachorro;
    private CheckBox cuidaGato;
    private CheckBox cuidaMamifero;
    private CheckBox cuidaReptil;
    private CheckBox cuidaAve;
    private CheckBox cuidaPeixe;
    private Hospedador filtro;
    private PopUpInterface mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.popup_filtro, container, false);

        Bundle bundle = getArguments();
        JSONObject json = null;
        filtro = new Hospedador();

        if (bundle != null) {
            try {
                json = new JSONObject(bundle.getString("filtro"));
                filtro.setFieldsByJson(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (getContext() != null && json != null) {
            tipoSupervisao = rootView.findViewById(R.id.popup_tipo_supervisao);
            cuidaCachorro = rootView.findViewById(R.id.popup_cachorro);
            cuidaGato = rootView.findViewById(R.id.popup_gato);
            cuidaMamifero = rootView.findViewById(R.id.popup_mamifero);
            cuidaReptil = rootView.findViewById(R.id.popup_reptil);
            cuidaAve = rootView.findViewById(R.id.popup_ave);
            cuidaPeixe = rootView.findViewById(R.id.popup_peixe);
            List<String> supervisao = new ArrayList<>();

            supervisao.add("A cada 12h");
            supervisao.add("A cada 08h");
            supervisao.add("A cada 04h");
            supervisao.add("Nada selecionado");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_spinner_item, supervisao);

            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            tipoSupervisao.setAdapter(arrayAdapter);

            Button cancelar = rootView.findViewById(R.id.popup_cancelar);
            Button confirmar = rootView.findViewById(R.id.popup_confirmar);

            cancelar.setOnClickListener(this);
            confirmar.setOnClickListener(this);

            tipoSupervisao.setSelection(filtro.getTipoSupervisao() != null ?
                    filtro.getTipoSupervisao() : 3);
            cuidaCachorro.setChecked(filtro.getCuidaCachorro() != null ?
                    filtro.getCuidaCachorro() : false);
            cuidaGato.setChecked(filtro.getCuidaGato() != null ?
                    filtro.getCuidaGato() : false);
            cuidaMamifero.setChecked(filtro.getCuidaMamifero() != null ?
                    filtro.getCuidaMamifero() : false);
            cuidaReptil.setChecked(filtro.getCuidaReptil() != null ?
                    filtro.getCuidaReptil() : false);
            cuidaAve.setChecked(filtro.getCuidaReptil() != null ?
                    filtro.getCuidaAve() : false);
            cuidaPeixe.setChecked(filtro.getCuidaPeixe() != null ?
                    filtro.getCuidaPeixe() : false);
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Integer viewId = v.getId();

        switch (viewId) {
            case R.id.popup_confirmar:
                filtro.setTipoSupervisao(tipoSupervisao.getSelectedItemPosition() != 3 ?
                        tipoSupervisao.getSelectedItemPosition() : null);
                filtro.setCuidaCachorro(cuidaCachorro.isChecked() ? true : null);
                filtro.setCuidaGato(cuidaGato.isChecked() ? true : null);
                filtro.setCuidaMamifero(cuidaMamifero.isChecked() ? true : null);
                filtro.setCuidaReptil(cuidaReptil.isChecked() ? true : null);
                filtro.setCuidaAve(cuidaAve.isChecked() ? true : null);
                filtro.setCuidaPeixe(cuidaPeixe.isChecked() ? true : null);

                if (mListener != null)
                    mListener.filtroResult(filtro);

                dismiss();
            case R.id.popup_cancelar:
                dismiss();
                break;
        }
    }

    public void setListener(PopUpInterface listener) {
        this.mListener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface PopUpInterface {
        void filtroResult(Hospedador filtro);
    }
}
