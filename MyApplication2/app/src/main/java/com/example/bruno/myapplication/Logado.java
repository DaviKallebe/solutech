package com.example.bruno.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.bruno.myapplication.retrofit.Usuario;


public class Logado extends AppCompatActivity implements HospedadorListagemFragment.OnFragmentInteractionListener,
        ListMessageFragment.OnFragmentInteractionListener {

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logado);

        Toolbar toolbar = findViewById(R.id.logado_toolbar);
        setSupportActionBar(toolbar);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.viewPagerLogado);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mPager);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) Logado.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(Logado.this.getComponentName()));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_perfil) {
            return true;
        } else if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void verPerfil(View view, Usuario user) {
        Intent intent = new Intent(this, Perfil.class);

        intent.putExtra("email", user.getEmail());
        intent.putExtra("primeiroNome", user.getPrimeiroNome());
        intent.putExtra("ultimoNome", user.getUltimoNome());
        intent.putExtra("nome", user.getFullName());
        intent.putExtra("nascimento", user.getNascimento());
        intent.putExtra("telefone", user.getTelefone());
        intent.putExtra("descricao", user.getDescricao());
        intent.putExtra("id_user", user.getId_user());

        startActivity(intent);
    }


    public void meuspet(View view) {
        Intent intent = new Intent(this, MeusPets.class);
        startActivity(intent);
    }

    public void deslogar(View view) {
        finish();
        System.exit(0);
    }

    @Override
    public void verUsuarioDetalhes(Usuario user) {
        Intent intent = new Intent(Logado.this, UsuarioDetalheActivity.class);
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
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
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
                ListMessageFragment fragment = new ListMessageFragment();
                Intent it = Logado.this.getIntent();
                Bundle bundle = new Bundle();

                bundle.putInt("id_user", it.getIntExtra("id_user", 0));
                fragment.setArguments(bundle);
                return fragment;
            } else
                return new ListMessageFragment();
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
