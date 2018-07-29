package com.example.bruno.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;


public class Login extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText mail = findViewById(R.id.editMail);
        mail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        mProgressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        compositeDisposable = new CompositeDisposable();

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
        showProgressBar();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, (Task<AuthResult> task) -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        createOrLoginWithFacebook(user);
                    } else {
                        Toast.makeText(Login.this, "Autenticação falhou.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }

    public void entrar(View view) {
        EditText edtMail = findViewById(R.id.editMail);
        EditText edtPass = findViewById(R.id.editPass);

        String email = edtMail.getText().toString();
        String password = edtPass.getText().toString();

        if (email.equals("") || password.equals("")) {
            Snackbar.make(view, "Não deixe os campos vazios!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            return;
        }

        showProgressBar();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, (Task<AuthResult> task) -> {

                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        Single<Usuario> backendUser = new RetrofitConfig()
                                .getObservableUsuarioService()
                                .doFirebaseLogin(firebaseUser.getUid());

                        Disposable disposable = backendUser.subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::goMainActicity, this::handleLoginError);

                        compositeDisposable.add(disposable);
                    } else {
                        closeProgressBar();
                        Toast.makeText(Login.this, "Authentication falhou.",
                                Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(this, (task) -> {
                    closeProgressBar();
                    Toast.makeText(Login.this, "Authentication falhou.",
                            Toast.LENGTH_SHORT).show();
                });
    }

    public void cadastrar(View view) {
        Intent intent = new Intent(this, NovoUsuarioActivity.class);
        startActivity(intent);
    }

    public void createOrLoginWithFacebook(FirebaseUser firebaseUser) {
        String userName[] = firebaseUser.getDisplayName().split(" ", 2);
        JSONObject json = new JSONObject();

        try {
            json.put("email", firebaseUser.getEmail());
            json.put("primeiroNome", userName[0]);
            json.put("ultimoNome", userName[1]);
            json.put("firebaseUid", firebaseUser.getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody
                .create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json.toString());

        Single<Usuario> backendUser = new RetrofitConfig()
                .getObservableUsuarioService()
                .doFirebaseCreateUser(firebaseUser.getUid(), body);

        Disposable disposable = backendUser.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::goMainActicity, this::handleNewUserError);

        compositeDisposable.add(disposable);
    }

    public void goMainActicity(Usuario user) {
        Intent intent = new Intent(Login.this, MainActivity.class);

        saveToPreferences(user);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("email", user.getEmail());
        intent.putExtra("primeiroNome", user.getPrimeiroNome());
        intent.putExtra("ultimoNome", user.getUltimoNome());
        intent.putExtra("id_user", user.getId_user());
        intent.putExtra("nome", user.getPrimeiroNome() + ' ' + user.getUltimoNome());

        closeProgressBar();
        startActivity(intent);
    }

    public void saveToPreferences(Usuario user) {
        SharedPreferences.Editor editor = getSharedPreferences("userfile", MODE_PRIVATE).edit();
        editor.putInt("id_user", user.getId_user());
        editor.apply();
    }

    public void handleNewUserError(Throwable e) {
        closeProgressBar();

        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

        if (e instanceof IOException) {
            Toast.makeText(Login.this,
                    "Não foi possível conectar ao servidor, tente novamente mais tarde!",
                    Toast.LENGTH_SHORT).show();
        }
        //
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;

            Toast.makeText(Login.this,
                    "Opa! Aconteceu algo que não deveria.",
                    Toast.LENGTH_SHORT).show();

            if (httpException.code() == 500) {
                //
            }
        }
    }

    public void handleLoginError(Throwable e) {
        closeProgressBar();
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

        if (e instanceof IOException) {
            Toast.makeText(Login.this,
                    "Não foi possível conectar ao servidor, tente novamente mais tarde!",
                    Toast.LENGTH_SHORT).show();
        }
        else
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;

            if (httpException.code() == 401) {
                Toast.makeText(Login.this,
                        "Usuário ou senha inválidos!",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,
                    getResources().getString(R.string.error_unkown_signin),
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public void closeProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}