package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruno.myapplication.adapter.PedidoListagemAdapter;
import com.example.bruno.myapplication.retrofit.Hospedagem;
import com.example.bruno.myapplication.retrofit.Pet;
import com.example.bruno.myapplication.retrofit.RetrofitConfig;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

public class PedidoListagemFragment extends Fragment implements PedidoListagemAdapter.OnItemClicked,
        PedidoDetalheFragment.OnFragmentInteractionListener {

    MainActivityViewModel mViewModel;
    PedidoListagemAdapter mAdapter;

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
        View rootView = inflater.inflate(R.layout.fragment_pedido_listagem, container, false);

        Context context = getContext();

        if (context != null) {
            RecyclerView recyclerView = rootView.findViewById(R.id.fragment_pedido_recycler);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);

            SharedPreferences prefs = context.getSharedPreferences("userfile", MODE_PRIVATE);
            Integer id_user = prefs.getInt("id_user", 0);

            Disposable disposable = mViewModel
                    .selecionarHospedagemPedinte(id_user)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .retryWhen(errors ->
                            errors.flatMap(error -> Flowable.timer(3, TimeUnit.SECONDS)))
                    .subscribe((List<Hospedagem> hospedagens) -> {
                        if (getActivity() != null) {
                            mAdapter = new PedidoListagemAdapter(hospedagens, context, this);

                            getActivity().runOnUiThread(() -> {
                                recyclerView.setAdapter(mAdapter);
                            });
                        }
                    }, Throwable::printStackTrace);

            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(disposable);
        }

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onItemClick(View view, int position) {
        Hospedagem hospedagem = mAdapter.getItem(position);

        try {
            if (hospedagem.getStatus() == 1) {
                PedidoDetalheFragment pedidoDetalheFragment = new PedidoDetalheFragment();

                Bundle bundle = new Bundle();
                bundle.putString("servico", hospedagem.getFieldsJson().toString());

                pedidoDetalheFragment.setArguments(bundle);
                pedidoDetalheFragment.setListener(this);
                goToFragment(pedidoDetalheFragment);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void goToFragment(Fragment fragmentDestination) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();

            if (fragmentManager != null) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                        .replace(R.id.fragment_pedido_listagem,
                                fragmentDestination,
                                fragmentDestination.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public void refreshAdpter(Integer id, Integer status) {
        mAdapter.updateAdapter(id, status);

        getActivity().runOnUiThread(() -> {
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        if (getActivity() != null)
            getActivity().setTitle(getResources().getString(R.string.fragment_pedido_listagem));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
