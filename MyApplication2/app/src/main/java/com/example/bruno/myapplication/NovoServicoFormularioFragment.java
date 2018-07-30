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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.myapplication.adapter.NovoServicoFormularioAdapter;
import com.example.bruno.myapplication.commons.SimpleDividerItemDecoration;
import com.example.bruno.myapplication.commons.Status;
import com.example.bruno.myapplication.retrofit.Hospedagem;
import com.example.bruno.myapplication.retrofit.Pet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

            SharedPreferences prefs = context.getSharedPreferences("userfile", MODE_PRIVATE);
            Integer id_user = prefs.getInt("id_user", 0);

            mViewModel.getPetList().observe(this, resource -> {
                if (resource != null && resource.status == Status.SUCCESS && resource.data != null) {

                    List<Pet> petList = resource.data;

                    mAdapter = new NovoServicoFormularioAdapter(petList, context, this);

                    mRecyclerView.setAdapter(mAdapter);
                }
            });

            EditText dataInicio = rootView.findViewById(R.id.fragment_novo_servico_edit_data_inicio);
            EditText dataFim = rootView.findViewById(R.id.fragment_novo_servico_edit_data_final);

            Calendar myCalendar = Calendar.getInstance();

            DatePickerDialog.OnDateSetListener picker1 = (view, year, monthOfYear, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                dataInicio.setText(simpleDateFormat.format(myCalendar.getTime()));
            };

            DatePickerDialog.OnDateSetListener picker2 = (view, year, monthOfYear, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                dataFim.setText(simpleDateFormat.format(myCalendar.getTime()));
            };

            dataInicio.setOnClickListener((View v) -> {
                new DatePickerDialog(context, picker1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            });

            dataFim.setOnClickListener((View v) -> {
                new DatePickerDialog(context, picker2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            });

            Button confirm = rootView.findViewById(R.id.fragment_novo_servico_button_confirm);
            confirm.setOnClickListener((View v) -> {
                Hospedagem hospedagem = new Hospedagem();
                Bundle bundle = getArguments();

                if (bundle != null) {
                    SparseArray<Integer> checkedPets = mAdapter.getCheckedPets();
                    StringBuilder pets = new StringBuilder();

                    for (int i = 0, nsize = checkedPets.size(); i < nsize; ++i) {
                        if (pets.length() > 0)
                            pets.append(";");

                        pets.append(checkedPets.valueAt(i));
                    }

                    hospedagem.setDataInicio(dataInicio.getText().toString());
                    hospedagem.setDataFim(dataFim.getText().toString());
                    hospedagem.setId_user_pedinte(id_user);
                    hospedagem.setId_user_hospedador(bundle.getInt("id_user"));
                    hospedagem.setId_pets(pets.toString());

                    Disposable disposable = mViewModel
                            .novaHospedagem(hospedagem)
                            .observeOn(Schedulers.newThread())
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe((Hospedagem hosp) -> {
                                getActivity().runOnUiThread(() -> {
                                Toast.makeText(context,
                                        getResources().getString(R.string.novo_servico_sucesso),
                                        Toast.LENGTH_SHORT).show();
                                });
                                dismiss();
                            }, (Throwable e) -> {
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(context,
                                            "Erro",
                                            Toast.LENGTH_SHORT).show();
                                });
                            });

                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    compositeDisposable.add(disposable);
                }
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
    public void onCheckClick(SparseArray<Integer> sparseArray) {
        FragmentActivity fragmentActivity = getActivity();
        Integer valorTotal = 0;

        if (fragmentActivity != null) {
            valorTotal = sparseArray.size();

            String valorString = String.format(new Locale("pt", "BR"),
                    "Selecionado: %d", valorTotal);

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
