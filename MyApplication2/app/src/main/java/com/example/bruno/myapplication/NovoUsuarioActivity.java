package com.example.bruno.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bruno.myapplication.retrofit.RetrofitConfig;
import com.example.bruno.myapplication.retrofit.Usuario;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NovoUsuarioActivity extends AppCompatActivity
        implements NovoUsuarioPasso1.OnFragmentInteractionListener,
                    NovoUsuarioPasso2.OnFragmentInteractionListener {

    String email;
    String pword;
    String telefone;
    String primeiroNome;
    String ultimoNome;
    Fragment novoPasso1;
    Fragment novoPasso2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);

        FragmentManager fragmentManager = getSupportFragmentManager();
        ActionBar actionBar = getSupportActionBar();

        fragmentManager.addOnBackStackChangedListener(() -> {
            if (actionBar != null) {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setDisplayShowHomeEnabled(true);
                }
                else {
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setDisplayShowHomeEnabled(false);
                }
            }
        });


        novoPasso1 = new NovoUsuarioPasso1();
        fragmentManager.
                beginTransaction()
                .replace(R.id.activity_novo_usuario,
                        novoPasso1,
                        novoPasso1.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }

    public void saveToPreferences(Usuario user) {
        SharedPreferences.Editor editor = getSharedPreferences("userfile", MODE_PRIVATE).edit();
        editor.putInt("id_user", user.getId_user());
        editor.apply();
    }

    public void goMainActicity(Usuario user) {
        Intent intent = new Intent(NovoUsuarioActivity.this, MainActivity.class);

        saveToPreferences(user);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("email", user.getEmail());
        intent.putExtra("primeiroNome", user.getPrimeiroNome());
        intent.putExtra("ultimoNome", user.getUltimoNome());
        intent.putExtra("id_user", user.getId_user());
        intent.putExtra("nome", user.getPrimeiroNome() + ' ' + user.getUltimoNome());

        startActivity(intent);
    }

    public void createNewUser() {
        Usuario usuario = new Usuario();

        usuario.setEmail(this.email);
        usuario.setPassword(this.pword);
        usuario.setPrimeiroNome(this.primeiroNome);
        usuario.setUltimoNome(this.ultimoNome);
        usuario.setTelefone(this.telefone);

        final ProgressBar mProgressBar = findViewById(R.id.progressBarNovo);

        mProgressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(this.email, this.pword)
                .addOnCompleteListener((Task<AuthResult> task) -> {
                    if (task.isSuccessful())
                        new RetrofitConfig().getObservableUsuarioService()
                                .doFirebaseCreateUser(mAuth.getCurrentUser().getUid(),
                                        usuario.generateRequestBody())
                                .observeOn(Schedulers.newThread())
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(user -> {
                                    runOnUiThread(() -> {
                                        mProgressBar.setVisibility(View.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    });

                                    goMainActicity(user);
                                });
                    else {
                        mProgressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                })
                .addOnFailureListener((Exception e) -> {
                    mProgressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    if (e instanceof FirebaseAuthUserCollisionException)
                        Toast.makeText(this,
                                getResources().getString(R.string.error_user_already_exist),
                                Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(this,
                                getResources().getString(R.string.error_create_new_user),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragmentManager != null) {
                if (fragmentManager.getBackStackEntryCount() > 1)
                    fragmentManager.popBackStackImmediate();
                else
                if (fragmentManager.getBackStackEntryCount() == 1) {
                    this.finish();
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void NovoUsuarioPasso1SaveToActivity(String email, String pass) {
        this.email = email;
        this.pword = pass;

        novoPasso2 = new NovoUsuarioPasso2();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_novo_usuario,
                        novoPasso2,
                        novoPasso2.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void NovoUsuarioPasso2SaveToActivity(String phone, String firstName, String lastName) {
        this.telefone = phone;
        this.primeiroNome = firstName;
        this.ultimoNome = lastName;

        createNewUser();
    }
}