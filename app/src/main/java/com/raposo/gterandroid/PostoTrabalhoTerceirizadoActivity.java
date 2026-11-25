package com.raposo.gterandroid;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.raposo.gterandroid.model.Ocupa;
import com.raposo.gterandroid.model.PostoTrabalho;
import com.raposo.gterandroid.model.Terceirizado;
import com.raposo.gterandroid.service.OcupaService;
import com.raposo.gterandroid.service.PostoTrabalhoService;
import com.raposo.gterandroid.service.TerceirizadoService;
import com.raposo.gterandroid.util.CampoEditavel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import kotlin.TuplesKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostoTrabalhoTerceirizadoActivity extends AppCompatActivity {
    private PostoTrabalhoService postoTrabalhoService;

    private OcupaService ocupaService;
    private TerceirizadoService terceirizadoService;


    private final CampoEditavel campoEditavel = new CampoEditavel(); // Utilizado para deixar os campos como Read-Only
    private List<PostoTrabalho> listaPostoTrabalho; // Para preencher o Spinner

    private ArrayAdapter<PostoTrabalho> adapterPostoTrabalho;// Para preencher o Spinner

    String stOperacao; // informa se a tela foi chamada para visualizar ou para incluir um Posto de Trabalho
    private MaterialToolbar mtbPtbCabecalho;
    private MenuItem mnuPtbCabecalhoAlterar;
    private Spinner spnPtbPostoTrabalho;

    private TextInputEditText etPtbDatInicio;

    private TextInputEditText etPtbDatFim;

    private RadioGroup rgPtbCodSituacao;
    private RadioButton rbPtbCodSituacaoTitular;
    private RadioButton rbPtbCodSituacaoExtra;
    private RadioButton rbPtbCodSituacaoSelecionada;
    private TextInputEditText etPtbDesLotacao;
    private TextInputEditText etPtbDesLocalizacao;

    private Button btPtbCancelar;
    private Button btPtbSalvar;

    private UUID ideContrato;
    //ivate UUID ideTerceirizado;
    private Terceirizado terceirizado;

    private Ocupa ocupa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_posto_trabalho_terceirizado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottom = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottom);
            return insets;
        });

        // Cria os Serviços
        postoTrabalhoService = RetrofitClient.getRetrofit().create(PostoTrabalhoService.class);
        ocupaService = RetrofitClient.getRetrofit().create(OcupaService.class);
        terceirizadoService = RetrofitClient.getRetrofit().create(TerceirizadoService.class);

        // Aponta os componentes
        mtbPtbCabecalho = findViewById(R.id.mtbPtbCabecalho);
        mnuPtbCabecalhoAlterar = mtbPtbCabecalho.getMenu().findItem(R.id.action_alterar);  // Botão de Edição da ToolBar

        spnPtbPostoTrabalho = findViewById(R.id.spnPtbPostoTrabalho);
        etPtbDatInicio = findViewById(R.id.etPtbDatInicio);
        etPtbDatFim = findViewById(R.id.etPtbDatFim);
        rgPtbCodSituacao = findViewById(R.id.rgPtbCodSituacao);
        rbPtbCodSituacaoTitular = findViewById(R.id.rbPtbCodSituacaoTitular);
        rbPtbCodSituacaoExtra = findViewById(R.id.rbPtbCodSituacaoExtra);
        etPtbDesLotacao = findViewById(R.id.etPtbLotacao);
        etPtbDesLocalizacao = findViewById(R.id.etPtbDesLocalizacao);

        btPtbCancelar = findViewById(R.id.btPtbCancelar);
        btPtbSalvar = findViewById(R.id.btPtbSalvar);


        etPtbDatInicio.setOnClickListener(v -> mostrarCalendario(etPtbDatInicio));
        etPtbDatFim.setOnClickListener(v -> mostrarCalendario(etPtbDatFim));


        // Salva os KeyListener dos campos Text
        campoEditavel.registrarKeyListeners(
                etPtbDatInicio,
                etPtbDatFim,
                etPtbDesLotacao,
                etPtbDesLocalizacao
        );

        // obtem os valores transmitidos pela Tela de Terceirizados

        // Contrato será passado de qualque jeito
        String stIdeContrato = getIntent().getStringExtra("stIdeContrato");
        if (stIdeContrato != null) {
            ideContrato = UUID.fromString(stIdeContrato);
        } else {
            Toast.makeText(PostoTrabalhoTerceirizadoActivity.this,"Não foi enviado o IdeContrato!",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String stIdeTerceirizado = getIntent().getStringExtra("stIdeTerceirizado");


        if (stIdeTerceirizado != null) {
            carregarTerceirizado(stIdeTerceirizado);
        } else {
            Toast.makeText(PostoTrabalhoTerceirizadoActivity.this,"Não foi enviado o IdeTerceirizado!",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String stNomTerceirizado = getIntent().getStringExtra("stNomTerceirizado");
        if (stNomTerceirizado == null) {
            Toast.makeText(PostoTrabalhoTerceirizadoActivity.this,"Não foi enviado o NomTerceirizado!",Toast.LENGTH_LONG).show();
            finish();
            return;
        }



        // Identifica se a tela está sendo chamada para Visualizar ou Incluir um registro
        stOperacao = getIntent().getStringExtra("stOperacao");

        if (stOperacao == null) {
            Toast.makeText(PostoTrabalhoTerceirizadoActivity.this,"Não foi enviado o valor de stOperacao!",Toast.LENGTH_LONG).show();
        } else if (stOperacao.equals("V")) { // Visualização
            String stIdeOcupa = getIntent().getStringExtra("stIdeOcupa");
            if (stIdeOcupa == null) {
                Toast.makeText(PostoTrabalhoTerceirizadoActivity.this,"Não foi enviado a IdeOcupa",Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            // Leitura da Ocupa que está Gravado no Banco
            Call<Ocupa> servicoRestOcupa = ocupaService.buscarPorId(stIdeOcupa);
            servicoRestOcupa.enqueue(new Callback<Ocupa>() {
                @Override
                public void onResponse(Call<Ocupa> call, Response<Ocupa> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ocupa = response.body();
                        preencheCamposTela(ocupa);
                        // Use o objeto 'ocupa' aqui (ex.: preencher campos na tela)
                    } else {
                        Toast.makeText(
                                PostoTrabalhoTerceirizadoActivity.this,
                                "Erro ao buscar ocupação (HTTP " + response.code() + ")",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(Call<Ocupa> call, Throwable t) {
                    Toast.makeText(
                            PostoTrabalhoTerceirizadoActivity.this,
                            "Falha na comunicação com o servidor: " + t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });


            // Esconde os botões inferiores

            btPtbCancelar.setVisibility(View.GONE);
            btPtbSalvar.setVisibility(View.GONE);

            // Exibe o botão de Edição

            mnuPtbCabecalhoAlterar.setVisible(true);

            definirModoEdicao(false); //


        } else if (stOperacao.equals("I")){ // Inclusão
            // Carrega o Spinner de Posto de Trabalho, porém sem selecionar.
            carregarPostoTrabalho(stIdeContrato, null);

            // Exibe os botões de Salvamento
            btPtbCancelar.setVisibility(View.VISIBLE);
            btPtbSalvar.setVisibility(View.VISIBLE);

            // Oculta o botão de edição
            mnuPtbCabecalhoAlterar.setVisible(false);


        } else {
            Toast.makeText(PostoTrabalhoTerceirizadoActivity.this,"Valor de stOperação inválido: "+stOperacao,Toast.LENGTH_LONG).show();
            return;
        }

        mnuPtbCabecalhoAlterar.setOnMenuItemClickListener( item -> {
            entraEmModoDeEdicao();
            return true;
        });



        // Manda para tela anterior
        mtbPtbCabecalho.setNavigationOnClickListener(v->
                getOnBackPressedDispatcher().onBackPressed()
        );

        // Botão de Cancelamento, que poderá ou não estar visível
        btPtbCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarOperacao();
            }
        });
        // Botão de Salvamento, que poderá ou não estar visível
        btPtbSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarOperacao();
            }
        });

        // Preenche o Cabecalho

        mtbPtbCabecalho.setTitle(stNomTerceirizado);
        mtbPtbCabecalho.setTitleCentered(true);
        mtbPtbCabecalho.setSubtitle("Posto de Trabalho");
        mtbPtbCabecalho.setSubtitleCentered(true);

    }

    private void preencheCamposTela (Ocupa ocupa) {

        String stIdePostoTrabalho = ocupa.getPostoTrabalho().getIdePostoTrabalho().toString();

        Date dtDatInicio = ocupa.getDatInicio();
        if (dtDatInicio != null) {
            etPtbDatInicio.setText(Data.dateToBr(dtDatInicio));
        } else {
            Toast.makeText(PostoTrabalhoTerceirizadoActivity.this,"Não foi enviado a Data de Início",Toast.LENGTH_LONG).show();
        }

        Date dtDatFim = ocupa.getDatFim();
        if (dtDatFim != null) {
            etPtbDatFim.setText(Data.dateToBr(dtDatFim));
        }

        String stCodSituacao = ocupa.getCodSituacao();
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

        String stDesLotacao = ocupa.getDesLotacao();
        if (stDesLotacao != null) {
            etPtbDesLotacao.setText(stDesLotacao);
        }

        String stDesLocalizacao =  ocupa.getDesLocalizacao();
        if (stDesLocalizacao != null) {
            etPtbDesLocalizacao.setText(stDesLocalizacao);
        }
        // Carrega o Spinner de Posto de Trabalho
        carregarPostoTrabalho(ideContrato.toString(), stIdePostoTrabalho);

    }

    private void carregarTerceirizado (String stIdeTerceirizado) {
        Call<Terceirizado> servicoRestTerceirizado = terceirizadoService.buscarPorId(stIdeTerceirizado);
        servicoRestTerceirizado.enqueue(new Callback<Terceirizado>() {
            @Override
            public void onResponse(Call<Terceirizado> call, Response<Terceirizado> response) {
                if (response.isSuccessful() && response.body() != null) {
                    terceirizado = response.body();
                } else {
                    Toast.makeText(
                            PostoTrabalhoTerceirizadoActivity.this,
                            "Erro ao buscar Terceirizado (HTTP " + response.code() + ")",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Terceirizado> call, Throwable t) {
                Toast.makeText(
                        PostoTrabalhoTerceirizadoActivity.this,
                        "Falha na comunicação com o servidor: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });


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

    private void entraEmModoDeEdicao() {
        definirModoEdicao(true); // Habilita os campos para edição
        // esconde o botão de Edição

        mnuPtbCabecalhoAlterar.setVisible(false);

        // Exibe os botões de Salvamento
        btPtbCancelar.setVisibility(View.VISIBLE);
        btPtbSalvar.setVisibility(View.VISIBLE);

    }

    private void cancelarOperacao() {
        if (stOperacao.equalsIgnoreCase("V")) { // a tela foi chamada ra visualizar e entrou no modo de Edição
            definirModoEdicao(false); // Habilita os campos para edição
            // Exibe o botão de Edição

            mnuPtbCabecalhoAlterar.setVisible(true);

            // Esconde os botões de Salvamento
            btPtbCancelar.setVisibility(View.GONE);
            btPtbSalvar.setVisibility(View.GONE);
        } else { // Inclusão
            // Manda para a telaanterior
            finish();

        }
    }

    private void salvarOperacao() {
        PostoTrabalho postoTrabalho = (PostoTrabalho) spnPtbPostoTrabalho.getSelectedItem();
        if (postoTrabalho == null) {
            Toast.makeText(this, "Escolha um Posto de Trabalho!", Toast.LENGTH_LONG).show();
            return;
        }



        String stDataInicio = etPtbDatInicio.getText() != null ? etPtbDatInicio.getText().toString().trim() : "";
        if (stDataInicio.isEmpty()) {
            Toast.makeText(this, "Informe uma Data de Início", Toast.LENGTH_LONG).show();
            return;
        }

        Date dtDatInicio = stringParaDate(stDataInicio);
        if (dtDatInicio == null) {
            Toast.makeText(this, "Data de Início inválida (use dd/MM/yyyy)!", Toast.LENGTH_LONG).show();
            return;
        }

        String stDatFim = etPtbDatFim.getText() != null ? etPtbDatFim.getText().toString().trim() : "";
        Date dtDatFim = null;
        if (!stDatFim.isEmpty()) {
            dtDatFim = stringParaDate(stDatFim);
            if (dtDatFim == null) {
                Toast.makeText(this, "Data de Fim inválida (use dd/MM/yyyy)!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        int inCodSituacaoSelecionada = rgPtbCodSituacao.getCheckedRadioButtonId();
        if (inCodSituacaoSelecionada == -1) {
            Toast.makeText(this, "Escolha um Tipo de Situação: Titular ou Extra!", Toast.LENGTH_LONG).show();
            return;
        }

        rbPtbCodSituacaoSelecionada = findViewById(inCodSituacaoSelecionada);
        String stCodSituacao = rbPtbCodSituacaoSelecionada.getText().toString().equalsIgnoreCase("Titular") ? "T" : "E";

        String stDesLotacao = etPtbDesLotacao.getText() != null ? etPtbDesLotacao.getText().toString().trim() : "";
        String stDesLocalizacao = etPtbDesLocalizacao.getText() != null ? etPtbDesLocalizacao.getText().toString().trim() : "";


        if ("V".equalsIgnoreCase(stOperacao)) {


            String idOcupaStr = ocupa.getIdeOcupa().toString();
            Ocupa novaOcupa = new Ocupa(
                    idOcupaStr,
                    terceirizado,
                    postoTrabalho,
                    stCodSituacao,
                    dtDatInicio,
                    dtDatFim,
                    stDesLotacao,
                    stDesLocalizacao
            );

            btPtbSalvar.setEnabled(false);
            ocupaService.atualizar(idOcupaStr, novaOcupa).enqueue(new retrofit2.Callback<Ocupa>() {
                @Override
                public void onResponse(retrofit2.Call<Ocupa> call, retrofit2.Response<Ocupa> response) {
                    btPtbSalvar.setEnabled(true);
                    if (response.isSuccessful()) {
                        Toast.makeText(PostoTrabalhoTerceirizadoActivity.this, "Registro atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        definirModoEdicao(false);
                        mnuPtbCabecalhoAlterar.setVisible(true);
                        btPtbCancelar.setVisibility(View.GONE);
                        btPtbSalvar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(PostoTrabalhoTerceirizadoActivity.this, "Erro ao atualizar (HTTP " + response.code() + ")", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Ocupa> call, Throwable t) {
                    btPtbSalvar.setEnabled(true);
                    Toast.makeText(PostoTrabalhoTerceirizadoActivity.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else { // Inclusão
            Ocupa ocupa = new Ocupa(
                    null,
                    terceirizado,
                    postoTrabalho,
                    stCodSituacao,
                    dtDatInicio,
                    dtDatFim,
                    stDesLotacao,
                    stDesLocalizacao
            );

            btPtbSalvar.setEnabled(false);
            ocupaService.salvar(ocupa).enqueue(new retrofit2.Callback<Ocupa>() {
                @Override
                public void onResponse(retrofit2.Call<Ocupa> call, retrofit2.Response<Ocupa> response) {
                    btPtbSalvar.setEnabled(true);
                    if (response.isSuccessful()) {
                        Toast.makeText(PostoTrabalhoTerceirizadoActivity.this, "Registro salvo com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PostoTrabalhoTerceirizadoActivity.this, "Erro ao salvar (HTTP " + response.code() + ")", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Ocupa> call, Throwable t) {
                    btPtbSalvar.setEnabled(true);
                    Toast.makeText(PostoTrabalhoTerceirizadoActivity.this, "Falha na comunicação: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }





    private void definirModoEdicao(boolean editavel) {
        campoEditavel.setCampoData(etPtbDatInicio, editavel);
        campoEditavel.setCampoData(etPtbDatFim, editavel);
        campoEditavel.setCampoEditavel(etPtbDesLotacao, editavel);
        campoEditavel.setCampoEditavel(etPtbDesLocalizacao, editavel);

        campoEditavel.setRadioGroupEditavel(rgPtbCodSituacao, editavel);
        campoEditavel.setSpinnerEditavel(spnPtbPostoTrabalho,editavel);

    }

    private void mostrarCalendario(TextInputEditText etData) {
        final Calendar calendario = Calendar.getInstance();
        String textoData = etData.getText().toString().trim();

        if (!textoData.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            sdf.setLenient(false); // rejeita datas inválidas como 32/13/2026
            try {
                Date dataExistente = sdf.parse(textoData);
                if (dataExistente != null) {
                    calendario.setTime(dataExistente);
                }
            } catch (ParseException e) {
                // Se o texto não estiver no formato dd/MM/yyyy, mantém a data atual do calendário
            }
        }

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, anoSelecionado, mesSelecionado, diaSelecionado) -> {
                    String dataFormatada = String.format(
                            Locale.getDefault(),
                            "%02d/%02d/%d",
                            diaSelecionado,
                            (mesSelecionado + 1),
                            anoSelecionado
                    );
                    etData.setText(dataFormatada);
                },
                ano, mes, dia
        );

        datePickerDialog.show();
    }

    public Date stringParaDate(String dataTexto) {
        if (dataTexto == null || dataTexto.trim().isEmpty()) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false); // Exige uma data real (ex.: rejeita 31/02/2026)

        try {
            return sdf.parse(dataTexto.trim());
        } catch (ParseException e) {
            return null; // Texto não segue o padrão dd/MM/yyyy ou é inválido
        }
    }
}