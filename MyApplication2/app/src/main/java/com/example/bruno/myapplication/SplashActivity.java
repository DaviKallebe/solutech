package com.example.bruno.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mProgressBar = findViewById(R.id.splashProgressBar);
        mAuth = FirebaseAuth.getInstance();
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

    public void redirectToScreen(FirebaseUser currentUser) {
        currentUser
            .reload()
            .addOnFailureListener(e -> {
                LoginManager.getInstance().logOut();
                closeProgressBar();
                startActivity(new Intent(SplashActivity.this, Login.class));
            })
            .addOnSuccessListener(aVoid -> {
                FirebaseUser user = mAuth.getCurrentUser();
                closeProgressBar();
                startActivity(new Intent(SplashActivity.this, Logado.class));
            });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            showProgressBar();

            redirectToScreen(currentUser);
        }
        else {
            startActivity(new Intent(SplashActivity.this, Login.class));
        }
    }
}
