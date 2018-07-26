package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bruno.myapplication.adapter.HospedadorListagemDetalhePefilAdapter;
import com.example.bruno.myapplication.retrofit.Comentario;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

public class HospedadorListagemDetalhePerfilFragment extends Fragment {

    private MainActivityViewModel mViewModel;

    public HospedadorListagemDetalhePerfilFragment() {
        //
    }

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

            SharedPreferences prefs = context.getSharedPreferences("userfile", MODE_PRIVATE);
            Integer id_user = prefs.getInt("id_user", 0);

            Bundle bundle = getArguments();

            if (bundle != null) {
                Disposable disposable = mViewModel
                        .getComments(bundle.getInt("id_user"))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(comentariosList ->
                                Flowable.fromIterable(comentariosList)
                                        .doOnNext(comentarios::add))
                        .subscribe();

                CompositeDisposable compositeDisposable = new CompositeDisposable();
                compositeDisposable.add(disposable);

                FragmentActivity fragmentActivity = getActivity();

                if (fragmentActivity != null) {
                    fragmentActivity.runOnUiThread(() -> {
                        TextView viewNome = rootView.findViewById(R.id.textViewNome);
                        viewNome.setText(bundle.getString("nome"));
                        TextView viewTelefone = rootView.findViewById(R.id.textViewTelefone);
                        viewTelefone.setText(bundle.getString("telefone"));
                        TextView viewDescricao = rootView.findViewById(R.id.textViewDescricaoUsuario);
                        viewDescricao.setText(bundle.getString("descricao"));

                        CircleImageView circleImageView = rootView.findViewById(R.id.imagePerfil);

                        if (bundle.getString("imagem") != null)
                            Picasso.get().load(bundle.getString("imagem"))
                                    .into(circleImageView);
                    });
                }

                Button solicitar = rootView.findViewById(
                        R.id.fragmento_hospedador_listagem_detalhe_perfil_requisitar);

                solicitar.setOnClickListener((View v) -> {
                    NovoServicoFormularioFragment novoServicoFormularioFragment =
                            new NovoServicoFormularioFragment();

                    mViewModel.loadPetList(id_user);

                    novoServicoFormularioFragment.show(getFragmentManager(), "Solicitar");
                });
            }
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
