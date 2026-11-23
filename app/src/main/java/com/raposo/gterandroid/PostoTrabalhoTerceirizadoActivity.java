package com.raposo.gterandroid;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.raposo.gterandroid.client.RetrofitClient;
import com.raposo.gterandroid.components.Data;
import com.raposo.gterandroid.model.PostoTrabalho;
import com.raposo.gterandroid.service.PostoTrabalhoService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PostoTrabalhoTerceirizadoActivity extends AppCompatActivity {
    private PostoTrabalhoService postoTrabalhoService;

    private List<PostoTrabalho> listaPostoTrabalho; // Para preencher o Spinner

    private ArrayAdapter<PostoTrabalho> adapterPostoTrabalho;// Para preencher o Spinner

    private MaterialToolbar mtbPtbCabecalho;
    private Spinner spnPtbPostoTrabalho;

    private TextInputEditText etPtbDatInicio;

    private TextInputEditText etPtbDatFim;

    private RadioGroup rgPtbCodSituacao;
    private RadioButton rbPtbCodSituacaoTitular;
    private RadioButton rbPtbCodSituacaoExtra;
    private TextInputEditText etPtbDesLotacao;
    private TextInputEditText etPtbDesLocalizacao;

    private Button btPtbCancelar;
    private Button btPtbSalvar;

    private UUID ideOcupa;
    private UUID ideContrato;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_posto_trabalho_terceirizado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Cria os Serviços
        postoTrabalhoService = RetrofitClient.getRetrofit().create(PostoTrabalhoService.class);

        // Aponta os componentes
        mtbPtbCabecalho = findViewById(R.id.mtbPtbCabecalho);
        spnPtbPostoTrabalho = findViewById(R.id.spnPtbPostoTrabalho);
        etPtbDatInicio = findViewById(R.id.etPtbDatInicio);
        etPtbDatFim = findViewById(R.id.etPtbDatFim);
        rgPtbCodSituacao = findViewById(R.id.rgPtbCodSituacao);
        rbPtbCodSituacaoTitular = findViewById(R.id.rbPtbCodSituacaoTitular);
        rbPtbCodSituacaoExtra = findViewById(R.id.rbPtbCodSituacaoExtra);
        etPtbDesLotacao = findViewById(R.id.etPtbLotacao);
        etPtbDesLocalizacao = findViewById(R.id.etPtbDesLocalizacao);

        // obtem os valores transmitidos pela Tela de Terceirizados

        String stIdeContrato = getIntent().getStringExtra("stIdeContrato");
        String stIdeOcupa = getIntent().getStringExtra("stIdeOcupa");
        String stIdePostoTrabalho = getIntent().getStringExtra("stIdePostoTrabalho");
        String stDatInicio = getIntent().getStringExtra("stDatInicio");
        String stDatFim = getIntent().getStringExtra("stDatFim");
        String stCodSituacao = getIntent().getStringExtra("stCodSituacao");
        String stDesLotacao =  getIntent().getStringExtra("stDesLotacao");
        String stDesLocalizacao =  getIntent().getStringExtra("stDesLocalizacao");

        if (stIdeContrato != null) {
            ideContrato = UUID.fromString(stIdeContrato);
        } else {
            Toast.makeText(PostoTrabalhoTerceirizadoActivity.this,"Não foi enviado o IdeContrato!",Toast.LENGTH_LONG).show();
        }

        if (stIdeOcupa != null) {
            ideOcupa = UUID.fromString(stIdeOcupa);
        } else {
            Toast.makeText(PostoTrabalhoTerceirizadoActivity.this,"Não foi enviado a IdeOcupa",Toast.LENGTH_LONG).show();
        }

        if (stDatInicio != null) {
            Date dtDatInicio = Data.isoToDate(stDatInicio);
            etPtbDatInicio.setText(Data.dateToBr(dtDatInicio));
        } else {
            Toast.makeText(PostoTrabalhoTerceirizadoActivity.this,"Não foi enviado a Data de Início",Toast.LENGTH_LONG).show();
        }

        if (stDatFim != null) {
            Date dtDatFim = Data.isoToDate(stDatFim);
            etPtbDatFim.setText(Data.dateToBr(dtDatFim));
        }

        if (stCodSituacao != null) {
            if (stCodSituacao.equals("T")) {
                rbPtbCodSituacaoTitular.setChecked(true);
                rbPtbCodSituacaoExtra.setChecked(false);
            } else {
                rbPtbCodSituacaoTitular.setChecked(false);
                rbPtbCodSituacaoExtra.setChecked(true);
            }
        } else {
            Toast.makeText(PostoTrabalhoTerceirizadoActivity.this,"Não foi enviado a Situação no Posto de Trabalho do Terceirizado!",Toast.LENGTH_LONG).show();
        }

        if (stDesLotacao != null) {
            etPtbDesLotacao.setText(stDesLotacao);
        }

        if (stDesLocalizacao != null) {
            etPtbDesLocalizacao.setText(stDesLocalizacao);
        }

        // Manda para tela anterior
        mtbPtbCabecalho.setNavigationOnClickListener(v->
                getOnBackPressedDispatcher().onBackPressed()
        );


        // Carrega o Spinner de Posto de Trabalho
        carregarPostoTrabalho(stIdeContrato, stIdePostoTrabalho);
    }

    private void carregarPostoTrabalho(String stIdeContrato, String stIdePostoTrabalhoSelecionado) {

        postoTrabalhoService.listarPostoTrabalhoContrato(stIdeContrato).enqueue(new retrofit2.Callback<List<PostoTrabalho>>() {
            @Override
            public void onResponse(retrofit2.Call<List<PostoTrabalho>> call, retrofit2.Response<List<PostoTrabalho>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPostoTrabalho = response.body();

                    adapterPostoTrabalho = new ArrayAdapter<>(
                            PostoTrabalhoTerceirizadoActivity.this,
                            android.R.layout.simple_spinner_item,
                            listaPostoTrabalho
                    );
                    // Carrega a Spinner
                    adapterPostoTrabalho.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnPtbPostoTrabalho.setAdapter(adapterPostoTrabalho);

                    // Se já veio um stIdePostoTrabalho (ex.: edição), seleciona ele no Spinner
                    if (stIdePostoTrabalhoSelecionado != null && !stIdePostoTrabalhoSelecionado.isEmpty()) {
                        selecionarPostoPorId(stIdePostoTrabalhoSelecionado);
                    }
                } else {
                    Toast.makeText(
                            PostoTrabalhoTerceirizadoActivity.this,
                            "Erro ao carregar postos de trabalho (HTTP " + response.code() + ")",
                            Toast.LENGTH_LONG
                    ).show();
                }
                /*
                Obtendo o PostoTrabalho selecionado ao salvar: Quando for salvar o formulário,
                 use (PostoTrabalho) spnPtbPostoTrabalho.getSelectedItem() para recuperar o objeto completo
                 (incluindo o idePostoTrabalho e nomPostoTrabalho).
                 */

            }

            @Override
            public void onFailure(retrofit2.Call<List<PostoTrabalho>> call, Throwable t) {
                Toast.makeText(
                        PostoTrabalhoTerceirizadoActivity.this,
                        "Falha na comunicação com o servidor: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });

    }

    private void selecionarPostoPorId(String idePostoTrabalho) {
        if (listaPostoTrabalho == null) return;

        for (int i = 0; i < listaPostoTrabalho.size(); i++) {
            PostoTrabalho item = listaPostoTrabalho.get(i);
            if (item != null && item.getIdePostoTrabalho() != null
                    && item.getIdePostoTrabalho().toString().equalsIgnoreCase(idePostoTrabalho)) {
                spnPtbPostoTrabalho.setSelection(i);
                break;
            }
        }
    }
}