package com.example.home_planing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.home_planing.R;

public class MainActivity extends AppCompatActivity {

    Button sign_in, sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToSignUp (View view){ //Ir a la pantalla de registro
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);

    } // end of goToSingUp method
    public void goToSignIn (View view){ //Ir a la pantalla de inicio de sesion
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);

    } // end of goToSingIn method
}