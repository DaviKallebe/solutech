package com.example.bruno.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class Logado extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logado);
        //
        Intent it = this.getIntent();

        //Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        //setSupportActionBar(myToolbar);

    }

    public void verPerfil(View view){
        Intent intent = new Intent(this, Perfil.class);
        Intent it = this.getIntent();

        intent.putExtra("email", it.getStringExtra("email"));
        intent.putExtra("nome", it.getStringExtra("nome"));
        intent.putExtra("idade", it.getIntExtra("idade", 0));
        intent.putExtra("telefone", it.getStringExtra("telefone"));
        intent.putExtra("descricao", it.getStringExtra("descricao"));
        intent.putExtra("id_user", it.getIntExtra("id_user", 0));

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

    class CustomAdapter extends BaseAdapter {

        @Override
        //numero de pets(?)
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.customlayouthosp,null);

            //carregar imagem
            ImageView imageView = (ImageView)view.findViewById(R.id.imageViewH);
            //imageView.setImageResource(imagem);

            //carregar nome
            TextView textViewNome = (TextView)view.findViewById(R.id.textViewNomeH);
            //textViewNome.setText(nome);

            //carregar descricao
            TextView textViewDesc = (TextView)view.findViewById(R.id.textViewDescricaoH);
            //textViewDesc.setText(desc);

            //carregar bairro
            TextView textViewBairro = (TextView)view.findViewById(R.id.textViewBairro);
            //textViewBairro.setText(bairro);

            //carregar pontos
            TextView textViewPontos = (TextView)view.findViewById(R.id.textViewPontos);
            //textViewPontos.setText(pontos);

            //carregar comentario
            TextView textViewComent = (TextView)view.findViewById(R.id.textViewComent);
            //textViewComent.setText(coment);


            return view;
        }
    }

}
