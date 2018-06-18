package com.example.bruno.myapplication;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.bruno.myapplication.retrofit.RetrofitConfig;
import com.example.bruno.myapplication.retrofit.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void entrar(View view){
        EditText mail = findViewById(R.id.editMail);
        EditText pass = findViewById(R.id.editPass);
        final int viewId = view.getId();

        if (mail.getText().toString().equals("") || pass.getText().toString().equals("")) {
            Snackbar.make(view, "Não deixe os campos vazios!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            return;
        }

        Call<Usuario> call = new RetrofitConfig().getUsuarioService().doNormalLogin(
                mail.getText().toString(), pass.getText().toString());

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Log.d("code", Integer.toString(response.code()));
                if (response.code() == 200){
                    Usuario user = response.body();

                    Intent intent = new Intent(Login.this, Logado.class);
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("nome", user.getNome());
                    intent.putExtra("idade", user.getIdade());
                    intent.putExtra("telefone", user.getTelefone());
                    intent.putExtra("descricao", user.getDescricao());
                    intent.putExtra("id_user", user.getId_user());

                    startActivity(intent);
                }
                if (response.code() == 401) {
                    View view = findViewById(viewId);
                    Snackbar.make(view, "Email ou senha não encontrados!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                View view = findViewById(viewId);
                Snackbar.make(view, "Email ou senha não encontrados.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void cadastrar(View view){
        Intent intent = new Intent(this, Cadastro.class);
        startActivity(intent);
    }

}
