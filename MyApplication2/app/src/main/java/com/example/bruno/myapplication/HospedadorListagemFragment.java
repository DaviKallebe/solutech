package com.example.bruno.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
public class HospedadorListagemFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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

        final ViewGroup finalView = rootView;

        ListView listView = rootView.findViewById(R.id.listViewGeral);
        listView.setOnItemClickListener(this);

        Call<List<Usuario>> call = new RetrofitConfig().getUsuarioService().listUsers();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.code() == 200) {
                    ListView listViewGeral = getView().findViewById(R.id.listViewGeral);
                    listViewGeral.setAdapter(new CustomAdapter(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Snackbar.make(finalView, "Verifique sua conexão!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) parent;
        Usuario user = (Usuario)listView.getAdapter().getItem(position);

        mListener.verUsuarioDetalhes(user);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void verUsuarioDetalhes(Usuario user);
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textViewNome;
        TextView textViewDesc;

        ViewHolder(View view) {
            imageView = view.findViewById(R.id.imageViewH);
            textViewNome = view.findViewById(R.id.textViewNomeH);
            textViewDesc = view.findViewById(R.id.textViewDescricaoH);;
        }
    }

    class CustomAdapter extends BaseAdapter {

        private List<Usuario> usuarios;

        public CustomAdapter(List<Usuario> usuarios) {
            this.usuarios = usuarios;
        }

        @Override
        public int getCount() {
            return this.usuarios.size();
        }

        @Override
        public Object getItem(int position) {
            return this.usuarios.get(position);
        }

        @Override
        public long getItemId(int position) {
            return this.usuarios.get(position).getId_user();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            Usuario user = usuarios.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlayouthosp, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //viewHolder.imageView.setImageResource(imagem);
            viewHolder.textViewNome.setText(user.getFullName());
            viewHolder.textViewDesc.setText(user.getDescricao());

            return convertView;
        }
    }
}
