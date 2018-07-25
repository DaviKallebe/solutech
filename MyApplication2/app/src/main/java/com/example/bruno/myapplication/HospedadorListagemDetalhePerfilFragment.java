package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruno.myapplication.adapter.HospedadorListagemDetalhePefilAdapter;
import com.example.bruno.myapplication.retrofit.Comentario;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HospedadorListagemDetalhePerfilFragment extends Fragment {

    private MainActivityViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity fragmentActivity = getActivity();

        if (fragmentActivity != null)
            mViewModel = ViewModelProviders.of(fragmentActivity).get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hospedador_listagem_detalhe_perfil, container, false);

        Context context = this.getContext();

        if (context != null) {
            RecyclerView recyclerView = rootView.findViewById(R.id.fragment_hospedador_listagem_detalhe_perfil_recycler);
            ArrayList<Comentario> comentarios = new ArrayList<>();
            HospedadorListagemDetalhePefilAdapter hospedadorListagemDetalhePefilAdapter =
                    new HospedadorListagemDetalhePefilAdapter(comentarios, context, null);

            recyclerView.setAdapter(hospedadorListagemDetalhePefilAdapter);

            Disposable disposable = mViewModel.getMessages()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(comentariosList ->
                        Flowable.fromIterable(comentariosList)
                                .doOnNext(comentarios::add))
                    .subscribe();

            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);
        }

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
