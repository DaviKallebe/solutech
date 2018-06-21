package com.example.bruno.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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


public class Logado extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logado);

        Toolbar toolbar = findViewById(R.id.logado_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //
        Intent it = this.getIntent();

        ListView listView = findViewById(R.id.listViewGeral);
        listView.setOnItemClickListener(this);

        Call<List<Usuario>> call = new RetrofitConfig().getUsuarioService().listUsers();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.code() == 200) {
                    ListView listViewGeral = findViewById(R.id.listViewGeral);
                    listViewGeral.setAdapter(new CustomAdapter(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Snackbar.make(findViewById(R.id.activity_novo_usuario), "Não foi possível realizar o cadastro!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) parent;
        Usuario user = (Usuario)listView.getAdapter().getItem(position);
        Intent intent = new Intent(Logado.this, UsuarioDetalheActivity.class);
        intent.putExtra("nome", user.getFullName());
        intent.putExtra("id_user", user.getId_user());
        intent.putExtra("telefone", user.getTelefone());
        intent.putExtra("descricao", user.getDescricao());
        intent.putExtra("primeiroNome", user.getPrimeiroNome());
        intent.putExtra("ultimoNome", user.getUltimoNome());
        startActivity(intent);

        //verPerfil(view, (Usuario)listView.getItemAtPosition(position));
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
        }
        else
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void verPerfil(View view, Usuario user){
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


    public void meuspet(View view){
        Intent intent = new Intent(this, MeusPets.class);
        startActivity(intent);
    }

    public void deslogar(View view){
        finish();
        System.exit(0);
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
        //numero de pets(?)
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
            /*
            LayoutInflater inflater = (LayoutInflater)   mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View view = inflater.inflate(R.layout.customlayouthosp, parent, false);*/

            //viewHolder.imageView.setImageResource(imagem);
            viewHolder.textViewNome.setText(user.getFullName());
            viewHolder.textViewDesc.setText(user.getDescricao());
            //viewHolder.textViewBairro.setText(user.get);
            //viewHolder.textViewPontos.setText(pontos);
            //viewHolder.textViewComent.setText(coment);

            return convertView;
        }
    }
}
