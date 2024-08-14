package com.example.snrs_01;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activer la gestion des bords de l'écran
        EdgeToEdge.enable(this);

        // Définir le layout XML de l'activité
        setContentView(R.layout.activity_splash_screen);

        // Handler pour retarder la transition vers MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent pour démarrer MainActivity
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Fermer l'activité SplashScreenActivity
            }
        }, 4000); // Délai de 4000 millisecondes (4 secondes)

        // Ajuster les insets de la fenêtre
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}

