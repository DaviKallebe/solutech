package com.example.bruno.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.bruno.myapplication.retrofit.RetrofitConfig;
import com.example.bruno.myapplication.retrofit.Usuario;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NovoUsuarioActivity extends Activity
        implements NovoUsuarioPasso1.OnFragmentInteractionListener,
                    NovoUsuarioPasso2.OnFragmentInteractionListener {

    String email;
    String pword;
    String phone;
    String firstName;
    String lastName;
    Fragment novoPasso1;
    Fragment novoPasso2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);

        novoPasso1 = new NovoUsuarioPasso1();
        getFragmentManager().beginTransaction()
                .replace(R.id.activity_novo_usuario, novoPasso1, novoPasso1.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    public void createNewUser() {
        String json = String.format("{\"%s\": \"%s\",\"%s\": \"%s\",\"%s\": \"%s\",\"%s\": \"%s\",\"%s\": \"%s\"}",
                "email", this.email,
                "pword", this.pword,
                "primeiroNome", this.firstName,
                "ultimoNome", this.lastName,
                "telefone", this.phone);

        final ProgressBar mProgressBar = findViewById(R.id.progressBarNovo);

        mProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Call<Usuario> call = new RetrofitConfig().getUsuarioService().createNewUser(body);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.code() == 201){
                    Usuario user = response.body();

                    Intent intent = new Intent(NovoUsuarioActivity.this, Logado.class);
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("primeiroNome", user.getPrimeiroNome());
                    intent.putExtra("ultimoNome", user.getUltimoNome());
                    intent.putExtra("telefone", user.getTelefone());
                    intent.putExtra("id_user", user.getId_user());
                    intent.putExtra("nome", user.getPrimeiroNome() + ' ' + user.getUltimoNome());

                    startActivity(intent);
                }
                else
                if (response.code() == 500) {
                    Snackbar.make(findViewById(R.id.activity_novo_usuario), "Erro ao realizar o cadastro!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                mProgressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Snackbar.make(findViewById(R.id.activity_novo_usuario), "Não foi possível realizar o cadastro!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                mProgressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    @Override
    public void NovoUsuarioPasso1SaveToActivity(String email, String pass) {
        this.email = email;
        this.pword = pass;

        novoPasso2 = new NovoUsuarioPasso2();
        getFragmentManager().beginTransaction()
                .replace(R.id.activity_novo_usuario, novoPasso2, novoPasso2.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    @Override
    public void NovoUsuarioPasso2SaveToActivity(String phone, String firstName, String lastName) {
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;

        createNewUser();
    }
}