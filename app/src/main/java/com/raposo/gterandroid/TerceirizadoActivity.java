package com.raposo.gterandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.raposo.gterandroid.components.Mascara;
import com.raposo.gterandroid.util.DateUtils;

import java.time.LocalDate;
import java.util.UUID;

public class TerceirizadoActivity extends AppCompatActivity {

    private TextInputEditText etTercNomTerceirizado;
    private TextInputEditText etTercNumCpf;
    private TextInputEditText etTercDataNascimento;

    private RadioGroup rgTercCodSexo;
    private TextInputEditText etTercDesEmail;


    private Button tercBtCancelar;

    private Button tercBtSalvar;

    private UUID ideTerceirizado;


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

        etTercNomTerceirizado = findViewById(R.id.etTercNomTerceirizado);
        etTercNumCpf = findViewById(R.id.etTercNumCpf);
        Mascara.cpf(etTercNumCpf);

        etTercDataNascimento = findViewById(R.id.etTercDataNascimento);
        Mascara.data(etTercDataNascimento);

        rgTercCodSexo = findViewById(R.id.rgTercCodSexo);

        etTercDesEmail = findViewById(R.id.etTercDesEmail);

        tercBtCancelar = findViewById(R.id.tercBtCancelar);

        // Obtem os valores transmitidos pela tela de TerceirizadosContrato

        ideTerceirizado = UUID.fromString(getIntent().getStringExtra("stIdeTerceirizado"));
        etTercNomTerceirizado.setText(getIntent().getStringExtra("stNomTerceirizado"));
        etTercNumCpf.setText(getIntent().getStringExtra("stNumCPF"));
        //intent.putExtra("stNumCPF", terceirizado.getNumCPF());
        //intent.putExtra("stCodSexo",terceirizado.getCodSexo());
        //intent.putExtra("dtDatNascimento",terceirizado.getDatNascimento().toString()); // Formato ISO



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
        String stNomTerceirizado = etTercNomTerceirizado.getText().toString();
        String stCpf = etTercNumCpf.getText().toString();
        String stDatNascimento = etTercDataNascimento.getText().toString();
        String stEmail = etTercDesEmail.getText().toString();
        String stCodSexo;
        int idSexoSelecionado = rgTercCodSexo.getCheckedRadioButtonId();
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


        // DOTO Continua Por aqui!
        Toast.makeText(TerceirizadoActivity.this,"Aguarde o fim da implementação",Toast.LENGTH_LONG).show();




    }
}