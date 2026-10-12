package com.raposo.gterandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.raposo.gterandroid.components.Mascara;
import com.raposo.gterandroid.model.Terceirizado;
import com.raposo.gterandroid.util.DateUtils;

import java.time.LocalDate;

public class TerceirizadoActivity extends AppCompatActivity {

    private TextInputEditText tercEtNome;
    private TextInputEditText tercEtCpf;
    private TextInputEditText tercEtDataNascimento;

    private RadioGroup tercRgSexo;
    private TextInputEditText tercEtEmail;


    private Button tercBtCancelar;

    private Button tercBtSalvar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);  // Desabilitado para o Teclado Virtual não sobrepor os Botões
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

        tercRgSexo = findViewById(R.id.tercRgSexo);

        tercEtEmail = findViewById(R.id.tercEdtEmail);

        tercBtCancelar = findViewById(R.id.tercBtCancelar);
        tercBtCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TerceirizadoActivity.this,"Falta Implementar",Toast.LENGTH_LONG).show();
            }
        });

        tercBtSalvar = findViewById(R.id.tercBtSalvar);
        tercBtSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarTerceirizado();
            }
        });
    }


    private void salvarTerceirizado () {
        String stNome = tercEtNome.getText().toString();
        String stCpf = tercEtCpf.getText().toString();
        String stDatNascimento = tercEtDataNascimento.getText().toString();
        String stEmail = tercEtEmail.getText().toString();
        String stCodSexo;
        int idSexoSelecionado = tercRgSexo.getCheckedRadioButtonId();
        if (idSexoSelecionado == -1) {
            stCodSexo = "não informado"; // Vei gerar erro no Construtor
        } else {
            RadioButton radioButtonSexoSelecionado = findViewById(idSexoSelecionado);
            stCodSexo = radioButtonSexoSelecionado.getText().toString().substring(0,1); // Obtem M ou F
        }

        // Tenta converter a Data de Nascimento
        LocalDate ldDataNascimento;
        try {
            ldDataNascimento = DateUtils.converterParaLocalDate(TerceirizadoActivity.this,stDatNascimento);
        } catch (Exception e) {
            Toast.makeText(TerceirizadoActivity.this,"Data de Nascimento inválida: "+e.getMessage(),Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Terceirizado terceizado = new Terceirizado(stNome,stCpf,ldDataNascimento,stCodSexo,stEmail);
        } catch (Exception e) {
            Toast.makeText(TerceirizadoActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            return;
        }

        // DOTO Continua Por aqui!
        Toast.makeText(TerceirizadoActivity.this,"Aguarde o fim da implementação",Toast.LENGTH_LONG).show();










    }
}