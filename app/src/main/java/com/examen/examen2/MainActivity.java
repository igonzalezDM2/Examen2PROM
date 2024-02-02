package com.examen.examen2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int totalVeces = 0;
    private Button btnConsultar, btnLogin, btnSalir;
    private TextView tvNumConsultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConsultar = findViewById(R.id.btnConsultar);
        btnLogin = findViewById(R.id.btnLogin);
        btnSalir = findViewById(R.id.btnSalir);
        tvNumConsultas = findViewById(R.id.tvNumConsultas);

        tvNumConsultas.setText(Integer.toString(totalVeces));

        btnConsultar.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchActivity.class);
            activityResultLauncher.launch(intent);
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        btnSalir.setOnClickListener(v -> {
            Toast.makeText(this, "Chao", Toast.LENGTH_SHORT).show();
            finish();
        });

    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (RESULT_OK == result.getResultCode()) {
                    int veces = result.getData().getIntExtra("veces", 0);
                    totalVeces += veces;
                    tvNumConsultas.setText(Integer.toString(totalVeces));
                }
            });
}