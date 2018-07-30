package com.example.bruno.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class HospedadorServicoDetalhePetFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hospedador_servico_detalhe_pet, container, false);

        Bundle bundle = getArguments();

        if (bundle != null) {
            TextView nome = rootView.findViewById(R.id.servico_detalhe_nome);
            TextView raca = rootView.findViewById(R.id.servico_detalhe_raca);
            TextView altura = rootView.findViewById(R.id.servico_detalhe_altura);
            TextView peso = rootView.findViewById(R.id.servico_detalhe_peso);

            try {
                JSONObject jsonObject = new JSONObject(bundle.getString("pet"));

                nome.setText(jsonObject.getString("nome"));
                raca.setText(String.format("Raça: %1$s - %2$s",
                        jsonObject.getString("especie"),
                        jsonObject.getString("raca")));
                altura.setText(String.format(new Locale("pt", "BR"),
                        "Altura: %.2f",
                        jsonObject.getDouble("altura")));
                peso.setText(String.format(new Locale("pt", "BR"),
                        "Peso: %.2f kg",
                        jsonObject.getDouble("peso")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setHasOptionsMenu(false);
        return rootView;
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
