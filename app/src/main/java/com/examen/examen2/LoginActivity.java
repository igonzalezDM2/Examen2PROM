package com.examen.examen2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private ImageView ivError;
    private EditText etUsuario, etContra;
    private Button btnLogin, btnCanelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ivError = findViewById(R.id.ivError);
        etUsuario = findViewById(R.id.etUsuario);
        etContra = findViewById(R.id.etContra);
        btnLogin = findViewById(R.id.btnLogin);
        btnCanelar = findViewById(R.id.btnCancelar);

        btnLogin.setOnClickListener(v -> {
            String usr = etUsuario.getText().toString();
            String pss = etContra.getText().toString();

            if ("admin".equals(usr) && "admin".equals(pss)) {
                Intent intent = new Intent(this, AdministrationActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.accesoincorrecto, Toast.LENGTH_SHORT).show();
                ivError.setVisibility(ImageView.VISIBLE);
            }

        });

        btnCanelar.setOnClickListener(v -> {
            finish();
        });
    }
}