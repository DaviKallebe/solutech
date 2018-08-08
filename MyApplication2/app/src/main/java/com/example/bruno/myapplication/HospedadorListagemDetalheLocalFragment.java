package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruno.myapplication.retrofit.Logradouro;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

public class HospedadorListagemDetalheLocalFragment extends Fragment {

    private MainActivityViewModel mViewModel;
    private CompositeDisposable compositeDisposable;

    public HospedadorListagemDetalheLocalFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_hospedador_listagem_local, container, false);

        TextView rua = rootView.findViewById(R.id.fragmento_hospedador_local_rua);
        TextView numero = rootView.findViewById(R.id.fragmento_hospedador_local_numero);
        TextView bairro = rootView.findViewById(R.id.fragmento_hospedador_local_bairro);
        TextView cidade = rootView.findViewById(R.id.fragmento_hospedador_local_cidade);
        TextView estado = rootView.findViewById(R.id.fragmento_hospedador_local_estado);
        TextView complemento = rootView.findViewById(R.id.fragmento_hospedador_local_complemento);
        TextView tipo = rootView.findViewById(R.id.fragmento_hospedador_local_tipo_residencia);
        TextView descricao = rootView.findViewById(R.id.fragmento_hospedador_local_descricao);
        ImageView local = rootView.findViewById(R.id.fragmento_hospedador_local_image);

        Context context = getContext();
        Bundle bundle = getArguments();

        if (context != null && bundle != null) {
            Integer id_user = bundle.getInt("id_user");

            Disposable disposable = mViewModel
                    .selecionarLogradouro(id_user)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((Logradouro logradouro) -> {
                        rua.setText(logradouro.getRua());
                        numero.setText(String.valueOf(logradouro.getNumero()));
                        bairro.setText(logradouro.getBairro());
                        cidade.setText(logradouro.getCidade());
                        estado.setText(logradouro.getEstado());
                        complemento.setText(logradouro.getComplemento());
                        tipo.setText(logradouro.getTipo());
                        descricao.setText(logradouro.getDescricao());

                        if (logradouro.getImagem() != null) {
                            Picasso.get().load(logradouro.getImagem()).into(local);
                        }

                        local.setOnClickListener(v -> {
                            local.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale));
                        });
                    }, Throwable::printStackTrace);

            compositeDisposable = new CompositeDisposable();
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

        if (compositeDisposable != null)
            compositeDisposable.clear();
    }
}
