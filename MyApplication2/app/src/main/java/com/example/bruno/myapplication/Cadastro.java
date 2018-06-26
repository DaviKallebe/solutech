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

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
    }

    public void cadastrarr(View view){
        EditText editMail = findViewById(R.id.editMail);
        EditText editPass1 = findViewById(R.id.editPass1);
        EditText editPass2 = findViewById(R.id.editPass2);
        EditText editNome = findViewById(R.id.editNome);
        EditText editIdade = findViewById(R.id.editNascimento);
        EditText editTelefone = findViewById(R.id.editTelefone);
        EditText editDescricao = findViewById(R.id.editDescricao);

        final int viewId = view.getId();

        if (editMail.getText().toString().equals("") || editPass1.getText().toString().equals("") ||
            editPass2.getText().toString().equals("") || editNome.getText().toString().equals("")) {
            Snackbar.make(view, "Não deixe os campos vazios!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            return;
        }

        if (!editPass2.getText().toString().equals(editPass1.getText().toString())) {
            Snackbar.make(view, "As senhas devem ser iguais!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            return;
        }

        String json  = "{\"%s\": \"%s\",\"%s\": \"%s\",\"%s\": \"%s\",\"%s\": %s,\"%s\": \"%s\",\"%s\": \"%s\"}";

        json = String.format(json,  "email", editMail.getText().toString(),
                                    "password", editPass1.getText().toString(),
                                    "nome", editNome.getText().toString(),
                                    "idade", editIdade.getText().toString(),
                                    "telefone", editTelefone.getText().toString(),
                                    "descricao", editDescricao.getText().toString());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);

        Call<Usuario> call = new RetrofitConfig().getUsuarioService().createNewUser(body);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.code() == 200){
                    Usuario user = response.body();

                    Intent intent = new Intent(Cadastro.this, Logado.class);
                    intent.putExtra("email", user.getEmail());
                    //intent.putExtra("nome", user.getNome());
                    //intent.putExtra("idade", user.getIdade());
                    intent.putExtra("telefone", user.getTelefone());
                    intent.putExtra("descricao", user.getDescricao());
                    intent.putExtra("id_user", user.getId_user());

                    startActivity(intent);
                }
                else
                if (response.code() == 500) {
                    View view = findViewById(viewId);
                    Snackbar.make(view, "Erro ao realizar o cadastro!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                View view = findViewById(viewId);
                Snackbar.make(view, "Não foi possível realizar o cadastro!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
