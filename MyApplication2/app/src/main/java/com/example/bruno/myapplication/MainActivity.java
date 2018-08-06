package com.example.bruno.myapplication;

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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        EditarValorFragment.OnFragmentInteractionListener,
        HospedadorListagemFragment.OnFragmentInteractionListener,
        HospedadorServicoFragment.OnFragmentInteractionListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PET_PICK_IMAGE = 2;

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

        List<FragmentOption> fragmentOptions = new ArrayList<>();

        fragmentOptions.add(new FragmentOption(HospedadorListagemFragment.class,
                getResources().getString(R.string.fragment_hospedador_listagem), null));
        fragmentOptions.add(new FragmentOption(HospedadorServicoFragment.class,
                getResources().getString(R.string.fragment_hospedador_servico), null));


        ViewPager mPager = findViewById(R.id.main_activity_viewPager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager, fragmentOptions);
        mPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.main_activity_tablayout);
        tabLayout.setupWithViewPager(mPager);
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
        else if (id == android.R.id.home) {
            getSupportFragmentManager().popBackStackImmediate();

            return true;
        }
        else if (id == R.id.action_pedido) {
            PedidoListagemFragment pedidoListagemFragment = new PedidoListagemFragment();

            startFragment(pedidoListagemFragment, null);
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
    public void startServicoFragment(Fragment fragment, String name) {
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
        else
        if (requestCode == PET_PICK_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            //if (mViewModel != null)

        }
    }

    @Override
    public <T> void saveFieldOnBackend(String fieldName, T fieldValue) {
        if (mViewModel != null)
            mViewModel.updateProfile(id_user, fieldName, fieldValue);
    }

    private class FragmentOption {
        private Class classFragment;
        private String fragmentName;
        private Bundle bundle;

        private <T extends Fragment> FragmentOption(Class<T> classFragment, String fragmentName) {
            this.classFragment = classFragment;
            this.fragmentName = fragmentName;
        }

        private <T extends Fragment> FragmentOption(Class<T> classFragment, String fragmentName, Bundle bundle) {
            this.classFragment = classFragment;
            this.fragmentName = fragmentName;
            this.bundle = bundle;
        }

        public String getFragmentName() {
            return fragmentName;
        }

        public Fragment getFragment() {
            try {
                Fragment fragment = (Fragment) classFragment.newInstance();
                fragment.setArguments(bundle);

                return fragment;
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        List<FragmentOption> fragmentOptions;

        ScreenSlidePagerAdapter(FragmentManager fragmentManager, List<FragmentOption> fragmentOptions) {
            super(fragmentManager);
            this.fragmentOptions = fragmentOptions;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentOptions.get(position).getFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentOptions.get(position).getFragmentName();
        }

        @Override
        public int getCount() {
            return fragmentOptions.size();
        }
    }
}