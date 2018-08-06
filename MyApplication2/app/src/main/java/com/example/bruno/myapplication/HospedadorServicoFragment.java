package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bruno.myapplication.adapter.HospedadorServicoAdapter;
import com.example.bruno.myapplication.retrofit.Hospedagem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;


public class HospedadorServicoFragment extends Fragment implements
        HospedadorServicoAdapter.OnItemClicked,
        HospedadorServicoDetalheFragment.OnFragmentInteractionListener {

    MainActivityViewModel mViewModel;
    HospedadorServicoAdapter mAdapter;
    private OnFragmentInteractionListener mListener;

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
        View rootView = inflater.inflate(R.layout.fragment_hospedador_servico, container, false);

        Context context = getContext();

        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("userfile", MODE_PRIVATE);
            Integer id_user = prefs.getInt("id_user", 0);

            RecyclerView recyclerView = rootView.findViewById(R.id.hospedador_servico_recycler);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);

            recyclerView.setLayoutManager(layoutManager);

            Disposable disposable = mViewModel
                    .selecionarHospedagemHospedador(id_user)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .retry((retryCount, throwable) -> {
                        if (getActivity() != null)
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(context,
                                        getResources().getString(R.string.time_out),
                                        Toast.LENGTH_SHORT).show();
                            });

                        return throwable instanceof SocketTimeoutException ||
                               throwable instanceof ConnectException;})
                    .subscribe((List<Hospedagem> hospedagens) -> {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                mAdapter = new HospedadorServicoAdapter(hospedagens,
                                        context,
                                        this);
                                recyclerView.setAdapter(mAdapter);
                            });
                        }
                    }, (Throwable e) -> {
                        e.printStackTrace();
                    });

            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);
        }

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void refreshAdpter(Integer id, Integer status) {
        mAdapter.updateAdapter(id, status);

        getActivity().runOnUiThread(() -> {
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Hospedagem hospedagem = mAdapter.getItem(position);

        try {
            FragmentManager fragmentManager = getFragmentManager();

            if (fragmentManager != null) {
                if (hospedagem.getStatus() == 1) {
                    HospedadorServicoDetalheFragment hospedadorServicoDetalheFragment =
                            new HospedadorServicoDetalheFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("servico", hospedagem.getFieldsJson().toString());

                    hospedadorServicoDetalheFragment.setArguments(bundle);
                    hospedadorServicoDetalheFragment.setListener(this);
                    hospedadorServicoDetalheFragment.show(getFragmentManager(), "Serviço");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        if (mListener != null)
            mListener.startServicoFragment(hospedadorServicoDetalheFragment, null);*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        MenuItem itemFilter = menu.findItem(R.id.action_filter);


        if (itemSearch != null)
            itemSearch.setVisible(false);

        if (itemFilter != null)
            itemFilter.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void startServicoFragment(Fragment fragment, String name);
    }
}
