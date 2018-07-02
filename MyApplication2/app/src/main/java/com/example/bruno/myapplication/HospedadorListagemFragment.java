package com.example.bruno.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bruno.myapplication.adapter.HospedadorListagemAdapter;
import com.example.bruno.myapplication.retrofit.RetrofitConfig;
import com.example.bruno.myapplication.retrofit.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HospedadorListagemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HospedadorListagemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HospedadorListagemFragment extends Fragment implements HospedadorListagemAdapter.OnItemClicked, Callback<List<Usuario>> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public HospedadorListagemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HospedadorListagemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HospedadorListagemFragment newInstance(String param1, String param2) {
        HospedadorListagemFragment fragment = new HospedadorListagemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_hospedador_listagem, container, false);

        mRecyclerView = rootView.findViewById(R.id.hospedador_listagem_recycler);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Call<List<Usuario>> call = new RetrofitConfig().getUsuarioService().listUsers();
        call.enqueue(this);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
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
        public void verUsuarioDetalhes(Usuario user);
    }

    @Override
    public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
        if (response.code() == 200) {
            mAdapter = new HospedadorListagemAdapter(response.body(), this.getContext(), this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onFailure(Call<List<Usuario>> call, Throwable t) {
        View view = getView();

        if (view != null)
            Snackbar.make(view, "Problemas de comunicação!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Usuario user = ((HospedadorListagemAdapter)mAdapter).getItem(position);

        mListener.verUsuarioDetalhes(user);
    }
}
