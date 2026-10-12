package com.raposo.gterandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.raposo.gterandroid.components.Mascara;

public class TerceirizadoActivity extends AppCompatActivity {

    private TextInputEditText tercEtNome;
    private TextInputEditText tercEtCpf;
    private TextInputEditText tercEtDataNascimento;

    private Button tercBtCancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terceirizado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tercEtNome = findViewById(R.id.tercEtNome);
        tercEtCpf = findViewById(R.id.tercEtCpf);
        Mascara.cpf(tercEtCpf);

        tercEtDataNascimento = findViewById(R.id.tercEtDataNascimento);
        Mascara.data(tercEtDataNascimento);

        tercBtCancelar = findViewById(R.id.tercBtCancelar);
        tercBtCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });






    }
}