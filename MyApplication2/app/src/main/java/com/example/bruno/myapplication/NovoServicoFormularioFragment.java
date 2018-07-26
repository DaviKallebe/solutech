package com.example.bruno.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.myapplication.adapter.NovoServicoFormularioAdapter;
import com.example.bruno.myapplication.commons.SimpleDividerItemDecoration;
import com.example.bruno.myapplication.commons.Status;
import com.example.bruno.myapplication.retrofit.Pet;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class NovoServicoFormularioFragment extends DialogFragment implements
        NovoServicoFormularioAdapter.OnItemClicked {

    private RecyclerView mRecyclerView;
    private NovoServicoFormularioAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainActivityViewModel mViewModel;
    private TextView textViewTotal;

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
        View rootView = inflater.inflate(R.layout.fragment_novo_servico_formulario, container, false);

        Context context = this.getContext();

        if (context != null) {
            mRecyclerView = rootView.findViewById(R.id.fragment_novo_servico_recycler);
            mLayoutManager = new LinearLayoutManager(context);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));

            textViewTotal = rootView.findViewById(R.id.fragment_novo_servico_textview_total);

            mViewModel.getPetList().observe(this, resource -> {
                if (resource != null && resource.status == Status.SUCCESS && resource.data != null) {

                    List<Pet> petList = resource.data;

                    mAdapter = new NovoServicoFormularioAdapter(petList, context, this);

                    mRecyclerView.setAdapter(mAdapter);
                }
            });

            Button confirm = rootView.findViewById(R.id.fragment_novo_servico_button_confirm);
            confirm.setOnClickListener((View v) -> {
                dismiss();
            });
        }

        Button cancel = rootView.findViewById(R.id.fragment_novo_servico_button_cancel);
        cancel.setOnClickListener((View v) -> this.dismiss());

        return rootView;
    }

    @Override
    public void onItemClick(View view, int position) {
        Pet pet = mAdapter.getItem(position);
    }

    @Override
    public void onCheckClick(SparseArray<Double> sparseArray) {
        FragmentActivity fragmentActivity = getActivity();
        Double valorTotal = 0.0;

        if (fragmentActivity != null) {
            for(int i = 0, nsize = sparseArray.size(); i < nsize; i++)
                valorTotal += sparseArray.valueAt(i);

            String valorString = String.format(new Locale("pt", "BR"),
                    "Preço total: R$ %.2f", valorTotal);

            textViewTotal.setText(valorString);
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
