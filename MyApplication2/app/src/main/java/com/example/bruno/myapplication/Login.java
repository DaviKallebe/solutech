package com.example.bruno.myapplication;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bruno.myapplication.retrofit.RetrofitConfig;
import com.example.bruno.myapplication.retrofit.Usuario;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class Login extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(AccessToken.getCurrentAccessToken() != null){
            goTelaLogado();
        }else {

            callbackManager = CallbackManager.Factory.create();

            loginButton = (LoginButton) findViewById(R.id.login_button);
            
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    goTelaLogado();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "Facebook Login Cancelado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(getApplicationContext(), "Facebook Login Erro", Toast.LENGTH_SHORT).show();
                }
            });

        }

        EditText mail = findViewById(R.id.editMail);
        mail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    private void goTelaLogado() {
        Intent intent = new Intent (this, Logado.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }



    public void entrar(View view){
        EditText mail = findViewById(R.id.editMail);
        EditText pass = findViewById(R.id.editPass);

        //
        final int viewId = view.getId();
        final ProgressBar mProgressBar = findViewById(R.id.progressBar);

        if (mail.getText().toString().equals("") || pass.getText().toString().equals("")) {
            Snackbar.make(view, "Não deixe os campos vazios!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            return;
        }

        Call<Usuario> call = new RetrofitConfig().getUsuarioService().doNormalLogin(
                mail.getText().toString(), pass.getText().toString());

        mProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Log.d("code", Integer.toString(response.code()));
                if (response.code() == 200){
                    Usuario user = response.body();

                    Intent intent = new Intent(Login.this, Logado.class);
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("primeiroNome", user.getPrimeiroNome());
                    intent.putExtra("ultimoNOme", user.getUltimoNome());
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

                mProgressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                View view = findViewById(viewId);
                Snackbar.make(view, "Erro, verifique sua coxenão.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                mProgressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    public void cadastrar(View view){

        Intent intent = new Intent(this, NovoUsuarioActivity.class);
        startActivity(intent);
    }

}
