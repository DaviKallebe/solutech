package com.example.bruno.myapplication;

import android.app.DownloadManager;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bruno.myapplication.adapter.HospedadorListagemAdapter;
import com.example.bruno.myapplication.retrofit.Hospedador;
import com.example.bruno.myapplication.retrofit.RetrofitConfig;
import com.example.bruno.myapplication.retrofit.Usuario;

import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class HospedadorListagemFragment extends Fragment implements HospedadorListagemAdapter.OnItemClicked {
    private RecyclerView mRecyclerView;
    private HospedadorListagemAdapter mAdapter;
    private List<Hospedador> hospedadors;
    private MainActivityViewModel mViewModel;
    private OnFragmentInteractionListener mListener;
    private Integer id_user;

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_hospedador_listagem, container, false);

        Context context = getContext();

        if (context != null) {
            mRecyclerView = rootView.findViewById(R.id.hospedador_listagem_recycler);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            SharedPreferences prefs = context.getSharedPreferences("userfile", MODE_PRIVATE);
            id_user = prefs.getInt("id_user", 0);

            CallQuery(id_user, null);
        }

        setHasOptionsMenu(true);
        setRetainInstance(true);

        return rootView;
    }

    public void CallQuery(Integer id_user, String query) {
        Disposable disposable = mViewModel
                .searchUsers(id_user, query)
                .retry((retryCount, throwable) -> throwable instanceof SocketTimeoutException)
                //.retry((retryCount, throwable) -> retryCount < 3 &&
                //        throwable instanceof SocketTimeoutException)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setHospedadoresAdapter, this::handlerErrorHospedadores);

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(disposable);
    }

    public void setHospedadoresAdapter(List<Hospedador> hospedadores) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                this.hospedadors = hospedadores;

                mAdapter = new HospedadorListagemAdapter(this.hospedadors,
                        this.getContext(), this);

                mRecyclerView.setAdapter(mAdapter);
            });
        }
    }

    public void handlerErrorHospedadores(Throwable e) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Não foi possivel carregar os hospedadores",
                        Toast.LENGTH_LONG).show();
            });
        }
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
        void startFragment(Fragment fragment, String name);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        FragmentActivity fragmentActivity = getActivity();

        if (fragmentActivity != null) {
            MenuItem searchItem = menu.findItem(R.id.action_search);

            SearchManager searchManager = (SearchManager)
                    fragmentActivity.getSystemService(Context.SEARCH_SERVICE);

            SearchView searchView = null;

            if (searchItem != null) {
                searchView = (SearchView) searchItem.getActionView();
            }
            if (searchView != null && searchManager != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(fragmentActivity.getComponentName()));

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    Long beforeTime;
                    Long afterTime;
                    Handler handler;

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //CallQuery(id_user, query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (handler != null)
                            handler.removeCallbacksAndMessages(null);

                        beforeTime = SystemClock.uptimeMillis();
                        handler = new Handler();

                        handler.postDelayed(() -> {
                            afterTime = SystemClock.uptimeMillis();

                            if (getContext() != null && afterTime - beforeTime >= 500)
                                CallQuery(id_user, newText);

                            handler.removeCallbacksAndMessages(null);
                        }, 700);
                        return true;
                    }
                });
            }

            fragmentActivity.setTitle(getResources().getString(R.string.app_label));
        }

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
    public void onItemClick(View view, int position) {
        Hospedador user = mAdapter.getItem(position);

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            HospedadorListagemDetalheFragment hospedadorListagemDetalheFragment =
                    new HospedadorListagemDetalheFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("id_user", user.getId_user());
            bundle.putString("nome", user.getFullName());
            bundle.putString("telefone", user.getTelefone());
            bundle.putString("descricao", user.getDescricao());
            bundle.putString("primeiroNome", user.getPrimeiroNome());
            bundle.putString("ultimoNome", user.getUltimoNome());
            bundle.putString("imagem", user.getImagem());

            hospedadorListagemDetalheFragment.setArguments(bundle);

            if (mListener != null)
                mListener.startFragment(hospedadorListagemDetalheFragment, null);
        }
    }
}
