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
import com.facebook.login.LoginManager;
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
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import okhttp3.RequestBody;
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

        EditText mail = findViewById(R.id.editMail);
        mail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                //
            }

            @Override
            public void onError(FacebookException error) {
                //
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        final ProgressBar mProgressBar = findViewById(R.id.progressBar);
        final AccessToken givenToken = token;

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

                            createOrLoginWithFacebook(user, mProgressBar);
                        } else {
                            Toast.makeText(Login.this, "Autenticação falhou.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            currentUser.reload().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    LoginManager.getInstance().logOut();
                }
            });

            currentUser.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    createOrLoginWithFacebook(user, null);
                }
            });
        }
    }

    public void entrar(View view) {
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
                if (response.code() == 200) {
                    goMainActicity(response.body());
                }
                else
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

    public void cadastrar(View view) {
        Intent intent = new Intent(this, NovoUsuarioActivity.class);
        startActivity(intent);
    }

    public void createOrLoginWithFacebook(FirebaseUser user, final ProgressBar mProgressBar) {
        String userName[] = user.getDisplayName().split(" ", 2);
        JSONObject json = new JSONObject();

        try {
            json.put("email", user.getEmail());
            json.put("primeiroNome", userName[0]);
            json.put("ultimoNome", userName[1]);
            json.put("facebookId", user.getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json.toString());
        Call<Usuario> call = new RetrofitConfig().getUsuarioService().createNewFacebookUser(body);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (mProgressBar != null) {
                    mProgressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                if (response.code() == 201 || response.code() == 200){
                    goMainActicity(response.body());
                }
                else
                    Log.d("ERROU", response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                if (mProgressBar != null) {
                    mProgressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                Log.d("ERROU", t.getMessage());

                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();

                Toast.makeText(Login.this, "Autenticação falhou, tente novamente mais tarde.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goMainActicity(Usuario user) {
        Intent intent = new Intent(Login.this, Logado.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("email", user.getEmail());
        intent.putExtra("primeiroNome", user.getPrimeiroNome());
        intent.putExtra("ultimoNome", user.getUltimoNome());
        //intent.putExtra("telefone", user.getTelefone());
        intent.putExtra("id_user", user.getId_user());
        intent.putExtra("nome", user.getPrimeiroNome() + ' ' + user.getUltimoNome());

        startActivity(intent);
    }

}
