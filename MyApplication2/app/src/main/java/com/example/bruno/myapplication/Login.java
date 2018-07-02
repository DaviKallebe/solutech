package com.example.bruno.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class Login extends AppCompatActivity {

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Facebook Login Cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("onError", error.getMessage());
                Toast.makeText(getApplicationContext(), "Facebook Login Erro", Toast.LENGTH_SHORT).show();
            }
        });

        EditText mail = findViewById(R.id.editMail);
        mail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        final ProgressBar mProgressBar = findViewById(R.id.progressBar);

        mProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            String userName[] = user.getDisplayName().split(" ", 2);

                            Intent intent = new Intent(Login.this, Logado.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("email", user.getEmail());
                            intent.putExtra("nome", user.getDisplayName());
                            intent.putExtra("primeiroNome", userName[0]);
                            intent.putExtra("ultimoNOme", userName[1]);
                            intent.putExtra("telefone", user.getPhoneNumber());
                            //intent.putExtra("id_user", user.getId_user());

                            startActivity(intent);

                            mProgressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            goTelaLogado();
                        } else {
                            Toast.makeText(getApplicationContext(), "Autenticação falhou.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            currentUser.reload().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        //createUser();
                    }
                }
            });

            currentUser.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    String userName[] = user.getDisplayName().split(" ", 2);

                    Intent intent = new Intent(Login.this, Logado.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("nome", user.getDisplayName());
                    intent.putExtra("primeiroNome", userName[0]);
                    intent.putExtra("ultimoNOme", userName[1]);
                    intent.putExtra("telefone", user.getPhoneNumber());
                    //intent.putExtra("id_user", user.getId_user());

                    startActivity(intent);

                }
            });
        }


        //updateUI(currentUser);
    }

    private void goTelaLogado() {
        Intent intent = new Intent (this, Logado.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

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
                if (response.code() == 200){
                    Usuario user = response.body();

                    Intent intent = new Intent(Login.this, Logado.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
