package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.myapplication.adapter.UsuarioPerfilAdapter;
import com.example.bruno.myapplication.commons.PerfilOpcoes;
import com.example.bruno.myapplication.commons.Status;
import com.example.bruno.myapplication.retrofit.Hospedador;
import com.example.bruno.myapplication.retrofit.Logradouro;
import com.example.bruno.myapplication.retrofit.Usuario;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;


public class UsuarioPerfilFragment extends Fragment implements UsuarioPerfilAdapter.OnItemClicked {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private RecyclerView mRecyclerView;
    private UsuarioPerfilAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainActivityViewModel mViewModel;
    CircleImageView mImageView;
    private Integer id_user;
    private Usuario usuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity activity = getActivity();

        if (activity != null)
            mViewModel = ViewModelProviders.of(activity).get(MainActivityViewModel.class);
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
            mLayoutManager = new LinearLayoutManager(context) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));

            mImageView = rootView.findViewById(R.id.image_perfil);
            mImageView.setOnClickListener(this::dispatchTakePictureIntent);

            SharedPreferences prefs = context.getSharedPreferences("userfile", MODE_PRIVATE);
            id_user = prefs.getInt("id_user", 0);

            TextView textViewAnuncio = rootView.findViewById(R.id.fragment_usuario_perfil_anuncio);
            textViewAnuncio.setOnClickListener(v -> {
                HospedadorCadastroEtapa1Fragment hospedadorCadastroFragment = new HospedadorCadastroEtapa1Fragment();

                Hospedador hospedador = new Hospedador();
                hospedador.setId_user(id_user);
                mViewModel.updateHospedadorCadastro(hospedador);

                goToFragment(hospedadorCadastroFragment);
            });

            mViewModel.getCurrentUser().observe(this, resource -> {
                if (resource != null && resource.status == Status.SUCCESS) {
                    List<PerfilOpcoes> opt = new ArrayList<>();
                    Usuario user = resource.data;

                    if (user != null) {
                        usuario = user;
                        Boolean cadastrou = user.getCadastrouComoHospedador();

                        if (cadastrou)
                            textViewAnuncio.setVisibility(View.GONE);
                        else
                            textViewAnuncio.setVisibility(View.VISIBLE);

                        //1 - String, 2 - Integer, 3 - Real, 4 - Date, 5 - Phone
                        opt.add(new PerfilOpcoes<>(getStr(R.string.primeiro_nome),
                                "primeiroNome", user.getPrimeiroNome(), 1));
                        opt.add(new PerfilOpcoes<>(getStr(R.string.ultimo_nome),
                                "ultimoNome", user.getUltimoNome(), 1));
                        opt.add(new PerfilOpcoes<>(getStr(R.string.data_nascimento),
                                "nascimento", user.getNascimento(), 4));
                        opt.add(new PerfilOpcoes<>(getStr(R.string.telefone),
                                "telefone", user.getTelefone(), 5));
                        opt.add(new PerfilOpcoes<>(getStr(R.string.descricao),
                                "descricao", user.getDescricao(), 6));

                        mAdapter = new UsuarioPerfilAdapter(opt, context, this);

                        mRecyclerView.setAdapter(mAdapter);

                        if (user.getImagem() != null) {
                            Picasso.get().load(user.getImagem())
                                    .into(mImageView);
                        }
                    }
                }
                /*else
                    mRecyclerView.setAdapter(new UsuarioPerfilAdapter(defaultOptions(), context, this));*/
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

        opt.add(new PerfilOpcoes<>(getStr(R.string.primeiro_nome),
                getResources().getString(R.string.primeiro_nome)));
        opt.add(new PerfilOpcoes<>(getStr(R.string.ultimo_nome),
                getResources().getString(R.string.ultimo_nome)));
        opt.add(new PerfilOpcoes<>(getStr(R.string.data_nascimento),
                getResources().getString(R.string.data_nascimento)));
        opt.add(new PerfilOpcoes<>(getStr(R.string.telefone),
                getResources().getString(R.string.telefone)));
        opt.add(new PerfilOpcoes<>(getStr(R.string.descricao),
                getResources().getString(R.string.descricao)));

        return opt;
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.usuario_perfil_menu, menu);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null)
            activity.setTitle(getResources().getString(R.string.fragment_perfil));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menu_id = item.getItemId();

        switch (menu_id) {
            case R.id.action_perfil_pets:
                PetListagemFragment petListagemFragment = new PetListagemFragment();

                mViewModel.loadPetList(id_user);

                goToFragment(petListagemFragment);
                break;
            case R.id.action_perfil_local:
                CadastrarLogradouroEtapa1 cadastrarLogradouroEtapa1 =
                        new CadastrarLogradouroEtapa1();

                Disposable disposable = mViewModel
                        .selecionarLogradouro(id_user)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((Logradouro logradouro) -> {
                            DetalheLocal detalheLocal = new DetalheLocal();

                            Bundle bundle = new Bundle();
                            bundle.putString("logradouro", logradouro.getFieldsJson().toString());

                            detalheLocal.setArguments(bundle);
                            goToFragment(detalheLocal);
                        }, (Throwable e) -> {
                            e.printStackTrace();
                            if (e instanceof HttpException) {

                                if (((HttpException) e).code() == 404)
                                    goToFragment(cadastrarLogradouroEtapa1,
                                            getResources().getString(R.string.fragment_logradouro));
                            }
                        });

                CompositeDisposable compositeDisposable = new CompositeDisposable();
                compositeDisposable.add(disposable);

                break;
            case R.id.action_perfil_hospedador:
                Disposable disposable1 = mViewModel
                        .getHospedador(id_user)
                        .subscribe((Hospedador hospedador) -> {
                            HospedadorPerfilFragment hospedadorPerfilFragment = new HospedadorPerfilFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("hospedador", hospedador.getFieldsJson().toString());

                            hospedadorPerfilFragment.setArguments(bundle);
                            goToFragment(hospedadorPerfilFragment);
                        }, (Throwable e) -> {
                            Toast.makeText(getContext(), "Erro, verifique sua conexão", Toast.LENGTH_SHORT).show();
                        });
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

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

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onItemClick(View view, int position) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (mAdapter == null || activity == null)
            return;

        PerfilOpcoes opt = mAdapter.getItem(position);
        EditarValorFragment editarValorFragment = new EditarValorFragment();

        editarValorFragment.setOption(opt);
        goToFragment(editarValorFragment);

    }

    public void goToFragment(Fragment fragmentDestination) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();

            if (fragmentManager != null) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                        .replace(R.id.fragment_usuario_perfil,
                                fragmentDestination,
                                fragmentDestination.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    public void goToFragment(Fragment fragmentDestination, String name) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();

            if (fragmentManager != null) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                        .replace(R.id.fragment_usuario_perfil,
                                fragmentDestination,
                                fragmentDestination.getClass().getSimpleName())
                        .addToBackStack(name)
                        .commit();
            }
        }
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
