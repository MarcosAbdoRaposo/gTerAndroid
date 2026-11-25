package com.raposo.gterandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.raposo.gterandroid.client.RetrofitClient;
import com.raposo.gterandroid.components.Data;
import com.raposo.gterandroid.components.Mascara;
import com.raposo.gterandroid.model.Ocupa;
import com.raposo.gterandroid.model.PostoTrabalho;
import com.raposo.gterandroid.model.Terceirizado;
import com.raposo.gterandroid.service.OcupaService;
import com.raposo.gterandroid.service.PostoTrabalhoService;
import com.raposo.gterandroid.service.TerceirizadoService;
import com.raposo.gterandroid.util.CampoEditavel;
import com.raposo.gterandroid.util.SeletorData;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InclusaoTerceirizadoActivity extends AppCompatActivity {

    private TerceirizadoService terceirizadoService;
    private OcupaService ocupaService;
    private PostoTrabalhoService postoTrabalhoService;

    private List<Ocupa> listaPostosTrabalho;
    private final CampoEditavel campoEditavel = new CampoEditavel();
    private MaterialToolbar mtbIncTercCabecalho;
    private TextInputEditText etIncTercNomTerceirizado;
    private TextInputLayout tilIncTercNumCPF;
    private TextInputEditText etIncTercNumCpf;
    private TextInputEditText etIncTercDatNascimento;
    private RadioGroup rgIncTercCodSexo;
    private RadioButton rbIncTercMasculino;
    private RadioButton rbIncTercFeminino;

    private TextInputEditText etIncTercDesEmail;

    private Spinner spnIncTercPostoTrabalho;
    private TextInputEditText etIncTercDatInicio;
    private RadioGroup rgIncTercCodSituacao;
    private RadioButton rbIncTercCodSituacaoTitular;
    private RadioButton rbIncTercCodSituacaoExtra;
    private RadioButton rbincTercCodSituacaoSelecionada;
    private TextInputEditText etIncTercDesLotacao;
    private TextInputEditText etIncTercDesLocalizacao;

    private Button btIncTercCancelar;

    private Button btIncTercSalvar;

    private UUID ideContrato;

    private Terceirizado terceirizado;
    private SeletorData<TextInputEditText> seletorData;

    private List<PostoTrabalho> listaPostoTrabalho;
    private ArrayAdapter<PostoTrabalho> adapterPostoTrabalho;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);  // Desabilitado para o Teclado Virtual não sobrepor os Botões
        setContentView(R.layout.activity_inclusao_terceirizado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Cria os Serviços
        ocupaService = RetrofitClient.getRetrofit().create(OcupaService.class);
        terceirizadoService = RetrofitClient.getRetrofit().create(TerceirizadoService.class);
        postoTrabalhoService = RetrofitClient.getRetrofit().create(PostoTrabalhoService.class);

        // Iniciando o DatePicker
        seletorData = new SeletorData<>(this);

        // aponta para componentes
        mtbIncTercCabecalho = findViewById(R.id.mtbIncTercCabecalho);
        tilIncTercNumCPF = findViewById(R.id.tilIncTercNumCpf);
        etIncTercNumCpf = findViewById(R.id.etIncTercNumCpf);
        Mascara.cpf(etIncTercNumCpf);

        etIncTercNomTerceirizado = findViewById(R.id.etIncTercNomTerceirizado);
        etIncTercDatNascimento = findViewById(R.id.etIncTercDatNascimento);
        etIncTercDatNascimento.setOnClickListener(v -> seletorData.mostrarCalendario(etIncTercDatNascimento));
        rgIncTercCodSexo = findViewById(R.id.rgIncTercCodSexo);
        rbIncTercMasculino = findViewById(R.id.rbIncTercMasculino);
        rbIncTercFeminino = findViewById(R.id.rbIncTercFeminino);
        etIncTercDesEmail = findViewById(R.id.etIncTerDesEmail);

        spnIncTercPostoTrabalho = findViewById(R.id.spnIncTercPostoTrabalho);
        etIncTercDatInicio = findViewById(R.id.etIncTercDatInicio);
        etIncTercDatInicio.setOnClickListener(v -> seletorData.mostrarCalendario(etIncTercDatInicio));

        rgIncTercCodSituacao = findViewById(R.id.rgIncTercCodSituacao);
        rbIncTercCodSituacaoTitular = findViewById(R.id.rbIncTercCodSituacaoTitular);
        rbIncTercCodSituacaoExtra = findViewById(R.id.rbIncTercCodSituacaoExtra);
        etIncTercDesLotacao = findViewById(R.id.etIncTercDesLotacao);
        etIncTercDesLocalizacao = findViewById(R.id.etIncTercDesLocalizacao);

        btIncTercCancelar = findViewById(R.id.btIncTercCancelar);
        btIncTercSalvar = findViewById(R.id.btIncTercSalvar);


        // Obtem os valores transmitidos pela tela de TerceirizadosContrato

        String stIdeContrato = getIntent().getStringExtra("stIdeContrato");
        if (stIdeContrato != null){
            ideContrato = UUID.fromString(stIdeContrato);
        } else {
            Toast.makeText(InclusaoTerceirizadoActivity.this,"Não foi enviado o ideContrato",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        tilIncTercNumCPF.setEndIconOnClickListener(v-> {
            String stNumCpf = etIncTercNumCpf.getText() != null ? etIncTercNumCpf.getText().toString().replaceAll("[.-]","") : "";
            buscarTerceirizado(stNumCpf);

        });

        // Listeners dos Botões
        btIncTercCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarOperacao();
            }
        });

        btIncTercSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarTerceirizado();
            }
        });

        // Manda para tela anterior
        mtbIncTercCabecalho.setNavigationOnClickListener(v->
                getOnBackPressedDispatcher().onBackPressed()
        );

        carregarPostoTrabalho(stIdeContrato);

    }

    private void buscarTerceirizado(String stNumCpf) {

        // Leitura do Terceirizado que está Gravado no Banco via Rest
        Call<Terceirizado> servicoRestTerceirizado = terceirizadoService.buscarPorCpf(stNumCpf);
        servicoRestTerceirizado.enqueue(new Callback<Terceirizado>() {
            @Override
            public void onResponse(Call<Terceirizado> call, Response<Terceirizado> response) {
                if (response.isSuccessful() && response.body() != null ) {
                    terceirizado = response.body();
                    terceirizadoJaExiste(terceirizado);
                } else if (response.code()  == 404) { // Terceirizado Não Cadastrado
                        telaNovoTerceirizado();
                } else {
                    Toast.makeText(
                            InclusaoTerceirizadoActivity.this,
                            "Erro ao buscar o terceirizado (HTTP " + response.code() + ")",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Terceirizado> call, Throwable t) {
                Toast.makeText(
                        InclusaoTerceirizadoActivity.this,
                        "Falha na comunicação com o servidor: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });

    }

    private void telaNovoTerceirizado() {
        campoEditavel.setCampoEditavel(etIncTercNumCpf, false); // Bloqueia a edição do CPF
        definirModoEdicao(true);
        btIncTercSalvar.setVisibility(View.VISIBLE);
        btIncTercCancelar.setVisibility(View.VISIBLE);

    }

    private void terceirizadoJaExiste(Terceirizado terceirizado) {
        // Verifica se o Terceirizado pertence a este contrato ou se esta ocupando outro cargo em outro lugar
        Call<List<Ocupa>> servicoRestOcupa = ocupaService.buscarPorTerceirizado(terceirizado.getIdeTerceirizado().toString());
        servicoRestOcupa.enqueue(new Callback<List<Ocupa>>() {
            @Override
            public void onResponse(Call<List<Ocupa>> call, Response<List<Ocupa>> response) {
                if (response.isSuccessful() && response.body() != null) { // Ocupações
                    List<Ocupa> ocupacoes = response.body();
                    for(Ocupa ocupa:ocupacoes) {
                        if (ocupa.getPostoTrabalho().getContrato().getIdeContrato().toString().equalsIgnoreCase(ideContrato.toString())) {
                            // Já está neste contrato.
                            Toast.makeText(
                                    InclusaoTerceirizadoActivity.this,
                                    "O terceirizado "+ terceirizado.getNomTerceirizado()+" já pertence a este contrato!",
                                    Toast.LENGTH_LONG
                            ).show();
                            return;
                        }
                        if (ocupa.getDatFim() == null) { // Terceirizado já possui outro emprego
                            Toast.makeText(
                                    InclusaoTerceirizadoActivity.this,
                                    "O terceirizado "+ terceirizado.getNomTerceirizado()+" já está alocado no contrato "+ocupa.getPostoTrabalho().getContrato().getNumContrato()+"/"+ocupa.getPostoTrabalho().getContrato().getAnoContrato(),
                                    Toast.LENGTH_LONG
                            ).show();
                            return;
                        }
                    }

                } else {
                    Toast.makeText(
                            InclusaoTerceirizadoActivity.this,
                            "Erro ao buscar as Ocupações do Terceirizado (HTTP " + response.code() + ")",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<List<Ocupa>> call, Throwable t) {
                Toast.makeText(
                        InclusaoTerceirizadoActivity.this,
                        "Falha na comunicação com o servidor: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
                return;
            }
        });





    }

    private void salvarTerceirizado() {
        String stNumCpfComMascara = etIncTercNumCpf.getText() != null
                ? etIncTercNumCpf.getText().toString().trim()
                : "";
        String stNumCpf = stNumCpfComMascara.replaceAll("[.-]","");
        String stNomTerceirizado = etIncTercNomTerceirizado.getText() != null
                ? etIncTercNomTerceirizado.getText().toString().trim()
                : "";
        String stDatNascimento = etIncTercDatNascimento.getText() != null
                ? etIncTercDatNascimento.getText().toString().trim()
                : "";
        String stEmail = etIncTercDesEmail.getText() != null
                ? etIncTercDesEmail.getText().toString().trim()
                : "";

        PostoTrabalho postoTrabalho = (PostoTrabalho) spnIncTercPostoTrabalho.getSelectedItem();
        String stDatInicio = etIncTercDatInicio.getText() != null
                ? etIncTercDatInicio.getText().toString().trim()
                : "";
        String stDesLotacao = etIncTercDesLotacao.getText() != null
                ? etIncTercDesLotacao.getText().toString().trim()
                : "";
        String stDesLocalizacao = etIncTercDesLocalizacao.getText() != null
                ? etIncTercDesLocalizacao.getText().toString().trim()
                : "";

        if (stNumCpf.isEmpty()) {
            etIncTercNumCpf.setError("Informe o CPF");
            etIncTercNumCpf.requestFocus();
            return;
        }

        if (stNomTerceirizado.isEmpty()) {
            etIncTercNomTerceirizado.setError("Informe o nome");
            etIncTercNomTerceirizado.requestFocus();
            return;
        }

        int idSexoSelecionado = rgIncTercCodSexo.getCheckedRadioButtonId();
        if (idSexoSelecionado == -1) {
            Toast.makeText(this, "Selecione o sexo do terceirizado.", Toast.LENGTH_LONG).show();
            return;
        }

        RadioButton rbSexo = findViewById(idSexoSelecionado);
        String stCodSexo = rbSexo.getTag() != null
                ? rbSexo.getTag().toString()
                : rbSexo.getText().toString().substring(0, 1).toUpperCase();

        Date dtDatNascimento = Data.brToDate(stDatNascimento);
        if (dtDatNascimento == null) {
            Toast.makeText(this, "Data de Nascimento inválida (use dd/MM/yyyy).", Toast.LENGTH_LONG).show();
            return;
        }

        if (postoTrabalho == null) {
            Toast.makeText(this, "Selecione um Posto de Trabalho.", Toast.LENGTH_LONG).show();
            return;
        }

        Date dtDatInicio = Data.brToDate(stDatInicio);
        if (dtDatInicio == null) {
            Toast.makeText(this, "Data de Início inválida (use dd/MM/yyyy).", Toast.LENGTH_LONG).show();
            return;
        }

        int idSituacaoSelecionada = rgIncTercCodSituacao.getCheckedRadioButtonId();
        if (idSituacaoSelecionada == -1) {
            Toast.makeText(this, "Selecione a situação (Titular ou Extra).", Toast.LENGTH_LONG).show();
            return;
        }

        RadioButton rbSituacao = findViewById(idSituacaoSelecionada);
        String stCodSituacao = rbSituacao.getTag() != null
                ? rbSituacao.getTag().toString()
                : (rbSituacao.getText().toString().equalsIgnoreCase("Titular") ? "T" : "E");

        definirBotoesSalvarCancelarHabilitados(false);

        // Se terceirizado já veio da busca por CPF, reaproveita; caso contrário, cria um novo
        if (terceirizado != null && terceirizado.getIdeTerceirizado() != null) {
            terceirizado.setNomTerceirizado(stNomTerceirizado);
            terceirizado.setDatNascimento(dtDatNascimento);
            terceirizado.setCodSexo(stCodSexo);
            terceirizado.setDesEmail(stEmail.isEmpty() ? null : stEmail);

            salvarOcupacao(terceirizado, postoTrabalho, stCodSituacao, dtDatInicio, stDesLotacao, stDesLocalizacao);
        } else {
            Terceirizado novoTerceirizado = new Terceirizado();
            novoTerceirizado.setNumCPF(stNumCpf);
            novoTerceirizado.setNomTerceirizado(stNomTerceirizado);
            novoTerceirizado.setDatNascimento(dtDatNascimento);
            novoTerceirizado.setCodSexo(stCodSexo);
            novoTerceirizado.setDesEmail(stEmail.isEmpty() ? null : stEmail);

            terceirizadoService.salvar(novoTerceirizado).enqueue(new Callback<Terceirizado>() {
                @Override
                public void onResponse(Call<Terceirizado> call, Response<Terceirizado> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        terceirizado = response.body();
                        salvarOcupacao(
                                terceirizado,
                                postoTrabalho,
                                stCodSituacao,
                                dtDatInicio,
                                stDesLotacao,
                                stDesLocalizacao
                        );
                    } else {
                        definirBotoesSalvarCancelarHabilitados(true);
                        Toast.makeText(
                                InclusaoTerceirizadoActivity.this,
                                "Erro ao salvar terceirizado (HTTP " + response.code() + ")",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(Call<Terceirizado> call, Throwable t) {
                    definirBotoesSalvarCancelarHabilitados(true);
                    Toast.makeText(
                            InclusaoTerceirizadoActivity.this,
                            "Falha na comunicação ao salvar terceirizado: " + t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        }
    }

    private void salvarOcupacao(
            Terceirizado tercSalvo,
            PostoTrabalho postoTrabalho,
            String stCodSituacao,
            Date dtDatInicio,
            String stDesLotacao,
            String stDesLocalizacao
    ) {
        Ocupa novaOcupa = new Ocupa(
                null,
                tercSalvo,
                postoTrabalho,
                stCodSituacao,
                dtDatInicio,
                null,
                stDesLotacao,
                stDesLocalizacao
        );

        ocupaService.salvar(novaOcupa).enqueue(new Callback<Ocupa>() {
            @Override
            public void onResponse(Call<Ocupa> call, Response<Ocupa> response) {
                definirBotoesSalvarCancelarHabilitados(true);
                if (response.isSuccessful()) {
                    Toast.makeText(
                            InclusaoTerceirizadoActivity.this,
                            "Terceirizado e posto de trabalho salvos com sucesso!",
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                } else {
                    Toast.makeText(
                            InclusaoTerceirizadoActivity.this,
                            "Erro ao salvar ocupação (HTTP " + response.code() + ")",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Ocupa> call, Throwable t) {
                definirBotoesSalvarCancelarHabilitados(true);
                Toast.makeText(
                        InclusaoTerceirizadoActivity.this,
                        "Falha na comunicação ao salvar ocupação: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void definirBotoesSalvarCancelarHabilitados(boolean habilitado) {
        btIncTercSalvar.setEnabled(habilitado);
        btIncTercCancelar.setEnabled(habilitado);
    }


    private void cancelarOperacao() {
        limparCpf();
        campoEditavel.setCampoEditavel(etIncTercNumCpf, true);
        limparCampos();
        definirModoEdicao(false);
        btIncTercCancelar.setVisibility(View.GONE);
        btIncTercSalvar.setVisibility(View.GONE);
    }

    private void limparCpf() {
        etIncTercNumCpf.setText("");
    }
    private void limparCampos() {

        etIncTercNomTerceirizado.setText("");
        etIncTercDatNascimento.setText("");

        rgIncTercCodSexo.clearCheck();

        etIncTercDesEmail.setText("");

        spnIncTercPostoTrabalho.setSelection(0);
        etIncTercDatInicio.setText("");
        rgIncTercCodSituacao.clearCheck();
        etIncTercDesLotacao.setText("");
        etIncTercDesLocalizacao.setText("");

    }

    private void preencherCampos(Terceirizado terceirizado) {

        etIncTercNomTerceirizado.setText(terceirizado.getNomTerceirizado());
        etIncTercDatNascimento.setText(Data.dateToBr(terceirizado.getDatNascimento()));
        if (terceirizado.getCodSexo().equalsIgnoreCase("M")) {
            rbIncTercMasculino.setChecked(true);
            rbIncTercFeminino.setChecked(false);
        } else {
            rbIncTercMasculino.setChecked(true);
            rbIncTercFeminino.setChecked(false);
        }
        etIncTercDesEmail.setText(terceirizado.getDesEmail());

        // Demais campos ficam em Branco
        spnIncTercPostoTrabalho.setSelection(0);
        etIncTercDatInicio.setText("");
        rgIncTercCodSituacao.clearCheck();
        etIncTercDesLotacao.setText("");
        etIncTercDesLocalizacao.setText("");

    }

    private void definirModoEdicao(boolean editavel) {
        //campoEditavel.setCampoEditavel(etIncTercNumCpf, ! editavel); // CPF será sempre o inverso
        campoEditavel.setCampoEditavel(etIncTercNomTerceirizado, editavel);

        campoEditavel.setCampoData(etIncTercDatNascimento, editavel);

        campoEditavel.setRadioGroupEditavel(rgIncTercCodSexo, editavel);
        campoEditavel.setCampoEditavel(etIncTercDesEmail, editavel);

        campoEditavel.setSpinnerEditavel(spnIncTercPostoTrabalho, editavel);
        campoEditavel.setCampoData(etIncTercDatInicio, editavel);
        campoEditavel.setRadioGroupEditavel(rgIncTercCodSituacao, editavel);
        campoEditavel.setCampoEditavel(etIncTercDesLotacao, editavel);
        campoEditavel.setCampoEditavel(etIncTercDesLocalizacao,editavel);
    }

    private void carregarPostoTrabalho(String stIdeContrato) {

        postoTrabalhoService.listarPostoTrabalhoContrato(stIdeContrato).enqueue(new retrofit2.Callback<List<PostoTrabalho>>() {
            @Override
            public void onResponse(retrofit2.Call<List<PostoTrabalho>> call, retrofit2.Response<List<PostoTrabalho>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPostoTrabalho = response.body();

                     adapterPostoTrabalho = new ArrayAdapter<>(
                            InclusaoTerceirizadoActivity.this,
                            android.R.layout.simple_spinner_item,
                            listaPostoTrabalho
                    );
                    // Carrega a Spinner
                    adapterPostoTrabalho.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnIncTercPostoTrabalho.setAdapter(adapterPostoTrabalho);


                } else {
                    Toast.makeText(
                            InclusaoTerceirizadoActivity.this,
                            "Erro ao carregar postos de trabalho (HTTP " + response.code() + ")",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<PostoTrabalho>> call, Throwable t) {
                Toast.makeText(
                        InclusaoTerceirizadoActivity.this,
                        "Falha na comunicação com o servidor: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    }