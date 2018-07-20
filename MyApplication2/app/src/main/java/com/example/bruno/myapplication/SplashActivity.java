package com.example.bruno.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mProgressBar = findViewById(R.id.splashProgressBar);
        mAuth = FirebaseAuth.getInstance();

        Resources res = getApplication().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        //conf.setLocale(new Locale(language_code.toLowerCase())); // API 17+ only.
        conf.locale = new Locale("pt", "BR");
        res.updateConfiguration(conf, dm);
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
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
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
