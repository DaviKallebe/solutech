package com.example.bruno.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CadastroPet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet);
    }

    public void cadastrar(View view){
        Intent intent = new Intent(this, MeusPets.class);
        startActivity(intent);
    }
}
