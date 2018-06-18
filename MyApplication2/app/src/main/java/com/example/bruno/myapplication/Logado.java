package com.example.bruno.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Logado extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logado);
        //
        Intent it = this.getIntent();

        TextView labelNome = findViewById(R.id.labelNome);
        labelNome.setText(String.format("Bem vindo %s!", it.getStringExtra("nome")));
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

}
