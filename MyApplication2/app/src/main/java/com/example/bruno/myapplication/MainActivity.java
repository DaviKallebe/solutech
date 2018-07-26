package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity implements ListagemMensagemFragment.OnFragmentInteractionListener,
        EditarValorFragment.OnFragmentInteractionListener,
        HospedadorListagemFragment.OnFragmentInteractionListener {

    private static final int NUM_PAGES = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private MainActivityViewModel mViewModel;
    private Integer id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.logado_toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("userfile", MODE_PRIVATE);
        id_user = prefs.getInt("id_user", 0);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.loadCurrentUser(id_user);

        FragmentManager fragmentManager = getSupportFragmentManager();
        ActionBar actionBar = getSupportActionBar();

        fragmentManager.addOnBackStackChangedListener(() -> {
            if (actionBar != null) {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setDisplayShowHomeEnabled(true);
                }
                else {
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setDisplayShowHomeEnabled(false);
                }
            }
        });

        ViewPager mPager = findViewById(R.id.main_activity_viewPager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
        mPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.main_activity_tablayout);
        tabLayout.setupWithViewPager(mPager);

        //setFragment(new MainFragment());
        //goToFragment(new MainFragment(), null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        else if (id == android.R.id.home) {
            getSupportFragmentManager().popBackStackImmediate();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startPerfilFragment() {
        Fragment usuarioPerfil = new UsuarioPerfilFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                .replace(R.id.activity_logado,
                        usuarioPerfil,
                        usuarioPerfil.getClass().getSimpleName())
                .addToBackStack(getResources().getString(R.string.perfil_fragment))
                .commit();
    }

    public void goToFragment(Fragment fragment, String name) {
        Fragment usuarioPerfil = new UsuarioPerfilFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                .replace(R.id.activity_logado,
                        fragment,
                        fragment.getClass().getSimpleName())
                .commit();
    }

    /*
    protected void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                .replace(R.id.fragment_container,
                        fragment,
                        fragment.getClass().getSimpleName())
                .commit();
    }

    protected void replaceFragment(Fragment fragment, String name) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                .replace(R.id.fragment_container,
                        fragment,
                        fragment.getClass().getSimpleName())
                .addToBackStack(name)
                .commit();
    }*/

    @Override
    public void startFragment(Fragment fragment, String name) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                .replace(R.id.activity_logado,
                        fragment,
                        fragment.getClass().getSimpleName())
                .addToBackStack(name)
                .commit();
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
    public <T> void saveFieldOnBackend(String fieldName, T fieldValue) {
        if (mViewModel != null)
            mViewModel.updateProfile(id_user, fieldName, fieldValue);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new HospedadorListagemFragment();
            else if (position == 1) {
                return new ListagemMensagemFragment();
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