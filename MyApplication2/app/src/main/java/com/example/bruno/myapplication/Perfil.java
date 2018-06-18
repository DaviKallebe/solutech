package com.example.bruno.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Perfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        //
        Intent it = this.getIntent();

        Log.d("NAME", it.getStringExtra("email"));
        
        ((TextView)findViewById(R.id.labelNome2)).setText("Email: " + it.getStringExtra("email"));
        ((TextView)findViewById(R.id.labelNome)).setText("Nome: " +it.getStringExtra("nome"));
        ((TextView)findViewById(R.id.labelIdade)).setText("Data Nascimento: " +it.getStringExtra("idade"));
        ((TextView)findViewById(R.id.labelTelefone)).setText("Telefone: " +it.getStringExtra("telefone"));
        ((TextView)findViewById(R.id.labelDescricao)).setText(it.getStringExtra("descricao"));
    }


    public void editarPerfil(View view){
        Intent intent = new Intent(this, EditarPerfil.class);
        startActivity(intent);
    }
}
