package com.example.bruno.myapplication;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bruno.myapplication.commons.PerfilOpcoes;
import com.example.bruno.myapplication.retrofit.Usuario;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity implements HospedadorListagemFragment.OnFragmentInteractionListener,
        ListagemMensagemFragment.OnFragmentInteractionListener,
        UsuarioPerfilFragment.OnFragmentInteractionListener,
        EditarValorFragment.OnFragmentInteractionListener {

    private static final int NUM_PAGES = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout tabLayout;
    private MainActivityViewModel mViewModel;
    private Fragment usuarioPerfil;
    private Integer id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.logado_toolbar);
        setSupportActionBar(toolbar);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.viewPagerLogado);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mPager);

        SharedPreferences prefs = getSharedPreferences("userfile", MODE_PRIVATE);
        id_user = prefs.getInt("id_user", 0);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.loadCurrentUser(id_user);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_perfil) {
            startPerfilFragment();

            return true;
        } else if (id == R.id.action_deslogar) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();

            Intent it = new Intent(MainActivity.this, Login.class);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(it);

            return true;
        }
        else if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startPerfilActivity() {
        Intent intent = new Intent(this, Perfil.class);
        startActivity(intent);
    }

    public void startPerfilFragment() {
        usuarioPerfil = new UsuarioPerfilFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                .replace(R.id.activity_logado,
                        usuarioPerfil,
                        usuarioPerfil.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void verUsuarioDetalhes(Usuario user) {
        Intent intent = new Intent(MainActivity.this, UsuarioDetalheActivity.class);
        intent.putExtra("nome", user.getFullName());
        intent.putExtra("id_user", user.getId_user());
        intent.putExtra("telefone", user.getTelefone());
        intent.putExtra("descricao", user.getDescricao());
        intent.putExtra("primeiroNome", user.getPrimeiroNome());
        intent.putExtra("ultimoNome", user.getUltimoNome());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            if (mViewModel != null)
                mViewModel.updateProfile(id_user, stream);
        }
    }

    @Override
    public void changeValue(PerfilOpcoes opt) {
        EditarValorFragment editarValorFragment = new EditarValorFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                .replace(R.id.activity_logado,
                        editarValorFragment,
                        editarValorFragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public <T> void saveFieldOnBackend(String fieldName, T fieldValue) {
        if (mViewModel != null)
            mViewModel.updateProfile(id_user, fieldName, fieldValue);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new HospedadorListagemFragment();
            else if (position == 1) {
                ListagemMensagemFragment fragment = new ListagemMensagemFragment();
                Intent it = MainActivity.this.getIntent();
                Bundle bundle = new Bundle();

                bundle.putInt("id_user", it.getIntExtra("id_user", 0));
                fragment.setArguments(bundle);
                return fragment;
            } else
                return new ListagemMensagemFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Hospedadores";
                case 1:
                    return "Mensagens";
                case 2:
                    return "Comentários";
                default:
                    return "Nada";
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}