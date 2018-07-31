package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.myapplication.retrofit.Hospedagem;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;


public class PedidoFinalizarFragment extends DialogFragment {

    MainActivityViewModel mViewModel;
    OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null)
            mViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pedido_finalizar, container, false);
        JSONObject json = null;

        Bundle bundle = getArguments();

        if (bundle != null) {
            try {
                json = new JSONObject(bundle.getString("pedido"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setFieldsByJson(json);

        if (getContext() != null) {
            RatingBar ratingBar = rootView.findViewById(R.id.pedido_finalizar_nota);
            EditText comentario = rootView.findViewById(R.id.pedido_finalizar_comentario);
            Button finalizar = rootView.findViewById(R.id.pedido_finalizar_finalizar);

            finalizar.setOnClickListener((View v) -> {
                Hospedagem attHospedagem = new Hospedagem();

                attHospedagem.setId(hospedagem.getId());
                attHospedagem.setStatus(6);
                attHospedagem.setId_user_pedinte(hospedagem.getId_user_pedinte());
                attHospedagem.setId_user_hospedador(hospedagem.getId_user_hospedador());
                attHospedagem.setNota((double)ratingBar.getRating());
                attHospedagem.setComentario(comentario.getText().toString());

                Disposable disposable = mViewModel
                        .finalizarHospedagem(attHospedagem)
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(),
                                    "Pedido finalizado",
                                    Toast.LENGTH_LONG).show();

                            if (mListener != null)
                                mListener.refreshAdpter(
                                        attHospedagem.getId(),
                                        attHospedagem.getStatus());

                            dismiss();
                        }), (Throwable e) -> getActivity().runOnUiThread(() -> {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Erro ao finalizar pedido",
                                    Toast.LENGTH_LONG).show();
                        }));

                CompositeDisposable compositeDisposable = new CompositeDisposable();
                compositeDisposable.add(disposable);
            });
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

    public void setListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    public interface OnFragmentInteractionListener {
        void refreshAdpter(Integer id, Integer status);
    }
}
