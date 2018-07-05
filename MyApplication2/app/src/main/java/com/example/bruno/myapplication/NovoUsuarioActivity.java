package com.example.bruno.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bruno.myapplication.retrofit.RetrofitConfig;
import com.example.bruno.myapplication.retrofit.Usuario;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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

    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);

        mProgressBar = findViewById(R.id.progressBarNovo);
        mAuth = FirebaseAuth.getInstance();
        compositeDisposable = new CompositeDisposable();

        novoPasso1 = new NovoUsuarioPasso1();
        getFragmentManager().beginTransaction()
                .replace(R.id.activity_novo_usuario, novoPasso1, novoPasso1.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            finish();
            finish();
        }
    }

    public void createNewUser(FirebaseUser firebaseUser) {
        JSONObject json = new JSONObject();

        try {
            json.put("email", firebaseUser.getEmail());
            json.put("primeiroNome", this.firstName);
            json.put("ultimoNome", this.lastName);
            json.put("firebaseUid", firebaseUser.getUid());
            json.put("telefone", this.phone);
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
                .subscribe(this::goMainActicity);

        compositeDisposable.add(disposable);
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

        showProgressBar();

        mAuth.createUserWithEmailAndPassword(this.email, this.pword)
                .addOnCompleteListener(this, (task) -> {
                    closeProgressBar();

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        createNewUser(user);
                    } else {
                        Toast.makeText(NovoUsuarioActivity.this, "Cadastro falhou.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void goMainActicity(Usuario user) {
        Intent intent = new Intent(NovoUsuarioActivity.this, Logado.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("email", user.getEmail());
        intent.putExtra("primeiroNome", user.getPrimeiroNome());
        intent.putExtra("ultimoNome", user.getUltimoNome());
        intent.putExtra("id_user", user.getId_user());
        intent.putExtra("nome", user.getPrimeiroNome() + ' ' + user.getUltimoNome());

        closeProgressBar();
        startActivity(intent);
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