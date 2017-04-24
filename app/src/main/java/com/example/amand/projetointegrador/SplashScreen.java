package com.example.amand.projetointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    // Timer da splash screen
    private static int SPLASH_TIME_OUT = 3000;
    Session session;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        session = new Session(this);

        token = session.getToken();

        new Handler().postDelayed(new Runnable() {
            /*
             * Exibindo splash com um timer.
             */
            @Override
            public void run() {
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal
                if(token.equals("") || token.isEmpty()) {
                    Intent login = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(login);
                } else {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }

                // Fecha esta activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
