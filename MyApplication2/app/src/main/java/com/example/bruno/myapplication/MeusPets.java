package com.example.bruno.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MeusPets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_pets);

        ListView listView = (ListView)findViewById(R.id.listViewMeusPets);

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }


    class CustomAdapter extends BaseAdapter{

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
            view = getLayoutInflater().inflate(R.layout.customlayout,null);

            //carregar imagens
            ImageView imageView = (ImageView)view.findViewById(R.id.imageViewPet);
            //imageView.setImageResource(imagem);

            //carregar nomes
            TextView textViewNome = (TextView)view.findViewById(R.id.textViewNomePet);
            //textViewNome.setText(nome);

            //carregar especie - raça
            TextView textViewDesc = (TextView)view.findViewById(R.id.textViewDescricaoPet);
            //textViewDesc.setText(desc);

            return view;
        }
    }

    public void cadastrar(View view){
        Intent intent = new Intent(this, CadastroPet.class);
        startActivity(intent);
    }
}
