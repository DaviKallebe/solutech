package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruno.myapplication.adapter.UsuarioPerfilAdapter;
import com.example.bruno.myapplication.commons.PerfilOpcoes;
import com.example.bruno.myapplication.commons.Status;
import com.example.bruno.myapplication.retrofit.Usuario;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class UsuarioPerfilFragment extends Fragment implements UsuarioPerfilAdapter.OnItemClicked {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private UsuarioPerfilAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LogadoViewModel mViewModel;
    CircleImageView mImageView;

    public UsuarioPerfilFragment() {
        // Required empty public constructor
    }

    public static UsuarioPerfilFragment newInstance(String param1, String param2) {
        UsuarioPerfilFragment fragment = new UsuarioPerfilFragment();
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

        //
        mViewModel = ViewModelProviders.of(getActivity()).get(LogadoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(
                R.layout.fragment_usuario_perfil, container, false);

        setHasOptionsMenu(true);

        Context context = this.getContext();

        if (context != null) {
            mRecyclerView = rootView.findViewById(R.id.recycler_perfil_listagem);
            mLayoutManager = new LinearLayoutManager(context);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));

            mImageView = rootView.findViewById(R.id.image_perfil);
            mImageView.setOnClickListener(this::dispatchTakePictureIntent);

            SharedPreferences prefs = context.getSharedPreferences("userfile", MODE_PRIVATE);
            int id_user = prefs.getInt("id_user", 0);

            mViewModel.getCurrentUser(id_user).observe(this, resource -> {
                if (resource != null && resource.status == Status.SUCCESS) {
                    List<PerfilOpcoes> opt = new ArrayList<>();
                    Usuario user = resource.data;

                    //1 - String, 2 - Integer, 3 - Real, 4 - Date, 5 - Phone
                    opt.add(new PerfilOpcoes(getStr(R.string.primeiro_nome), user.getPrimeiroNome(), 1));
                    opt.add(new PerfilOpcoes(getStr(R.string.ultimo_nome), user.getUltimoNome(), 1));
                    opt.add(new PerfilOpcoes(getStr(R.string.data_nascimento), user.getNascimento(), 4));
                    opt.add(new PerfilOpcoes(getStr(R.string.telefone), user.getTelefone(), 1));
                    opt.add(new PerfilOpcoes(getStr(R.string.descricao), user.getDescricao(), 1));

                    mRecyclerView.setAdapter(new UsuarioPerfilAdapter(opt, context, this));

                    if (user.getImagem() != null) {
                        Picasso.get().load(user.getImagem())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(mImageView);

                        Picasso.get().load(user.getImagem())
                                .into(mImageView);
                    }
                }
                else
                    mRecyclerView.setAdapter(new UsuarioPerfilAdapter(defaultOptions(), context, this));
            });
        }

        return rootView;
    }

    private void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public List<PerfilOpcoes> defaultOptions() {
        List<PerfilOpcoes> opt = new ArrayList<>();

        opt.add(new PerfilOpcoes(getStr(R.string.primeiro_nome),
                getResources().getString(R.string.primeiro_nome), 1));
        opt.add(new PerfilOpcoes(getStr(R.string.ultimo_nome),
                getResources().getString(R.string.ultimo_nome), 1));
        opt.add(new PerfilOpcoes(getStr(R.string.data_nascimento),
                getResources().getString(R.string.data_nascimento), 1));
        opt.add(new PerfilOpcoes(getStr(R.string.telefone),
                getResources().getString(R.string.telefone), 1));
        opt.add(new PerfilOpcoes(getStr(R.string.descricao),
                getResources().getString(R.string.descricao), 1));

        return opt;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_perfil).setVisible(false);
        menu.findItem(R.id.action_deslogar).setVisible(false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            activity.setTitle(getResources().getString(R.string.fragment_perfil));
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            default:
                return super.onOptionsItemSelected(item);
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
        //
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
            activity.setTitle(getResources().getString(R.string.app_label));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        ActionBar actionBar = activity.getSupportActionBar();

        //if (actionBar != null) actionBar.hide();
    }
    @Override
    public void onStop() {
        super.onStop();

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        ActionBar actionBar = activity.getSupportActionBar();

        //if (actionBar != null) actionBar.show();
    }

    public interface OnFragmentInteractionListener {
        //
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mAdapter == null)
            return;

        PerfilOpcoes opt = mAdapter.getItem(position);
    }

    public String getStr(int id) {
        return getResources().getString(id);
    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.recycler_horizontal_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left + 15, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
