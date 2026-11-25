package com.raposo.gterandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.raposo.gterandroid.client.RetrofitClient;
import com.raposo.gterandroid.components.Data;
import com.raposo.gterandroid.model.Ocupa;
import com.raposo.gterandroid.model.Terceirizado;
import com.raposo.gterandroid.service.OcupaService;
import com.raposo.gterandroid.service.TerceirizadoService;
import com.raposo.gterandroid.util.CampoEditavel;
import com.raposo.gterandroid.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raposo.gterandroid.util.SeletorData;

public class TerceirizadoActivity extends AppCompatActivity {

    private TerceirizadoService terceirizadoService;
    private OcupaService ocupaService;

    private List<Ocupa> listaPostosTrabalho;

    // Utilizado para salvar os KeyListene dos campos que serão colocados como Somente Leitura
    //private final Map<TextInputEditText, KeyListener> keyListenersOriginais = new HashMap<>();

    private final CampoEditavel campoEditavel = new CampoEditavel();
    private MaterialToolbar mtbTercCabecalho;
    private MenuItem mnuTercCabecalhoAlterar;
    private FloatingActionButton fabTercAdicionarPosto; // FAB
    private TextInputEditText etTercNomTerceirizado;
    private TextInputEditText etTercNumCpf;
    private TextInputEditText etTercDataNascimento;

    private RadioGroup rgTercCodSexo;
    private RadioButton rbTercMasculino;
    private RadioButton rbTercFeminino;

    private TextInputEditText etTercDesEmail;


    private Button btTercCancelar;

    private Button btTercSalvar;

    private ListView lvTercPostosTrabalho;

    private UUID ideContrato;
    private UUID ideTerceirizado;

    private Terceirizado terceirizado;

    private SeletorData<TextInputEditText> seletorData;


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

        // Cria os Serviços
        ocupaService = RetrofitClient.getRetrofit().create(OcupaService.class);
        terceirizadoService = RetrofitClient.getRetrofit().create(TerceirizadoService.class);

        // Iniciando o DatePicker
        seletorData = new SeletorData<>(this);

        // aponta para componentes
        mtbTercCabecalho = findViewById(R.id.mtbTercCabecalho);
        mnuTercCabecalhoAlterar = mtbTercCabecalho.getMenu().findItem(R.id.action_alterar);  // Botão de Edição da ToolBar

        etTercNomTerceirizado = findViewById(R.id.etTercNomTerceirizado);
        etTercNumCpf = findViewById(R.id.etTercNumCpf);
        //Mascara.cpf(etTercNumCpf);
        etTercDataNascimento = findViewById(R.id.etTercDataNascimento);
        etTercDataNascimento.setOnClickListener(v -> seletorData.mostrarCalendario(etTercDataNascimento)); // Chama o DatePiker
        //Mascara.data(etTercDataNascimento);
        rgTercCodSexo = findViewById(R.id.rgTercCodSexo);
        rbTercMasculino = findViewById(R.id.rbTercMasculino);
        rbTercFeminino = findViewById(R.id.rbTercFeminino);
        etTercDesEmail = findViewById(R.id.etTercDesEmail);
        lvTercPostosTrabalho = findViewById(R.id.lvTercPostosTrabalho);

        // Botões Inferiores
        btTercCancelar = findViewById(R.id.btTercCancelar);
        btTercSalvar = findViewById(R.id.btTercSalvar);

        // FAB
        fabTercAdicionarPosto = findViewById(R.id.fabTercAdicionarPosto);
        fabTercAdicionarPosto.setOnClickListener(v -> abrirInclusaoPostoTrabalho());

        // Obtem os valores transmitidos pela tela de TerceirizadosContrato

        String stIdeContrato = getIntent().getStringExtra("stIdeContrato");
        String stIdeTerceirizado = getIntent().getStringExtra("stIdeTerceirizado");

        // Verifica se os campos foram preenchidos

        if (stIdeContrato != null){
            ideContrato = UUID.fromString(stIdeContrato);
        } else {
            Toast.makeText(TerceirizadoActivity.this,"Não foi enviado o ideContrato",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (stIdeTerceirizado != null){
            ideTerceirizado = UUID.fromString(stIdeTerceirizado);
        } else {
            Toast.makeText(TerceirizadoActivity.this,"Não foi enviado o ideTerceirizado",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Leitura do Terceirizado que está Gravado no Banco via Rest
        Call<Terceirizado> servicoRestTerceirizado = terceirizadoService.buscarPorId(stIdeTerceirizado);
        servicoRestTerceirizado.enqueue(new Callback<Terceirizado>() {
            @Override
            public void onResponse(Call<Terceirizado> call, Response<Terceirizado> response) {
                if (response.isSuccessful() && response.body() != null) {
                    terceirizado = response.body();
                    preencheCamposTela(terceirizado); // Preenche os Campos da Tela

                } else {
                    Toast.makeText(
                            TerceirizadoActivity.this,
                            "Erro ao buscar ocupação (HTTP " + response.code() + ")",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Terceirizado> call, Throwable t) {
                Toast.makeText(
                        TerceirizadoActivity.this,
                        "Falha na comunicação com o servidor: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        // Listeners dos Botões
        btTercCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarOperacao();
            }
        });

        btTercSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarTerceirizado();
            }
        });

        mnuTercCabecalhoAlterar.setOnMenuItemClickListener( item -> {
            entraEmModoDeEdicao();
            return true;
        });

        // Manda para tela anterior
        mtbTercCabecalho.setNavigationOnClickListener(v->
                getOnBackPressedDispatcher().onBackPressed()
        );

        // Associa evento de selecionar no ListView
        lvTercPostosTrabalho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ocupa ocupacaoSelecionada  = listaPostosTrabalho.get(position);
                telaPostosTrabalhoTerceirizado(ocupacaoSelecionada);
            }
        });

        // Salva os KeyListener dos campos Text
        campoEditavel.registrarKeyListeners(
                etTercNomTerceirizado,
                etTercDataNascimento,
                etTercDesEmail
        );

        // O campo CPF está permanentemente bloqueado para Edição
        campoEditavel.setCampoEditavel(etTercNumCpf, false);
        // Deixa a Tela não editavel
        definirModoEdicao(false);

        // Oculta os Botões de Salvamento

        // Exibe os botões de Salvamento
        btTercCancelar.setVisibility(View.GONE);
        btTercSalvar.setVisibility(View.GONE);


        // Moveu para onResume
        //listarPostosTrabalhos(ideTerceirizado); // Lista os Postos de Trabalho do Terceirizado
    }

    private void preencheCamposTela(Terceirizado terceirizado) {
        // Preenche a tela
        etTercNomTerceirizado.setText(terceirizado.getNomTerceirizado());
        etTercNumCpf.setText(terceirizado.getNumCPFFormatado());
        //TODO Ver como está sendo gerada o formato Data
        etTercDataNascimento.setText(Data.dateToBr(terceirizado.getDatNascimento()));

        if (terceirizado.getCodSexo() != null) {
            if (terceirizado.getCodSexo().equals("M")) {
                rbTercMasculino.setChecked(true);
                rbTercFeminino.setChecked(false);
            } else {
                rbTercMasculino.setChecked(false);
                rbTercFeminino.setChecked(true);
            }
        } else {
            Toast.makeText(TerceirizadoActivity.this,"Não foi enviado o Sexo do Terceirizado!",Toast.LENGTH_LONG).show();
            return;
        }
        etTercDesEmail.setText(terceirizado.getDesEmail());

    }

    private void entraEmModoDeEdicao() {
        definirModoEdicao(true); // Habilita os campos para edição
        // esconde o botão de Edição

        mnuTercCabecalhoAlterar.setVisible(false);

        // Exibe os botões de Salvamento
        btTercCancelar.setVisibility(View.VISIBLE);
        btTercSalvar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ideTerceirizado != null) {
            listarPostosTrabalhos(ideTerceirizado);  // Refresh no Listview
        }
    }

    // Chama  atela de Posto de Trabalho de uma Terceirizado (ocupa), em modo de Visualização
    private void telaPostosTrabalhoTerceirizado(Ocupa ocupacaoSelecionada) {
        Intent intent = new Intent(TerceirizadoActivity.this, PostoTrabalhoTerceirizadoActivity.class);
        intent.putExtra("stOperacao", "V"); // Visualização
        intent.putExtra("stIdeContrato", ideContrato.toString());
        intent.putExtra("stIdeOcupa", ocupacaoSelecionada.getIdeOcupa().toString());
        intent.putExtra("stIdeTerceirizado", ocupacaoSelecionada.getTerceirizado().getIdeTerceirizado().toString());
        intent.putExtra("stNomTerceirizado", ocupacaoSelecionada.getTerceirizado().getNomTerceirizado());

        startActivity(intent);

    }

    // Chama  atela de Posto de Trabalho de uma Terceirizado (ocupa), em modo de Inclusão
    private void abrirInclusaoPostoTrabalho() {
        if (ideContrato == null) {
            Toast.makeText(this, "Contrato não informado!", Toast.LENGTH_LONG).show();
            return;
        }
        if (ideTerceirizado == null) {
            Toast.makeText(this, "Terceirizado não informado!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(TerceirizadoActivity.this, PostoTrabalhoTerceirizadoActivity.class);
        intent.putExtra("stOperacao", "I"); // Inclusão
        intent.putExtra("stIdeContrato", ideContrato.toString());
        intent.putExtra("stIdeTerceirizado", ideTerceirizado.toString());
        intent.putExtra("stNomTerceirizado", terceirizado.getNomTerceirizado());

        startActivity(intent);
    }

    private void cancelarOperacao() {
        preencheCamposTela(terceirizado); //
        definirModoEdicao(false); // Desabilita os campos para edição


        mnuTercCabecalhoAlterar.setVisible(true); // Exibe o botão de Edição

        // Esconde os botões de Salvamento
        btTercCancelar.setVisibility(View.GONE);
        btTercSalvar.setVisibility(View.GONE);

    }

    private void salvarTerceirizado() {
        if (terceirizado == null || terceirizado.getIdeTerceirizado() == null) {
            Toast.makeText(
                    TerceirizadoActivity.this,
                    "Dados do terceirizado ainda não foram carregados.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        String stNomTerceirizado = etTercNomTerceirizado.getText() != null
                ? etTercNomTerceirizado.getText().toString().trim()
                : "";
        String stDatNascimento = etTercDataNascimento.getText() != null
                ? etTercDataNascimento.getText().toString().trim()
                : "";
        String stEmail = etTercDesEmail.getText() != null
                ? etTercDesEmail.getText().toString().trim()
                : "";

        if (stNomTerceirizado.isEmpty()) {
            etTercNomTerceirizado.setError("Informe o nome");
            etTercNomTerceirizado.requestFocus();
            return;
        }

        int idSexoSelecionado = rgTercCodSexo.getCheckedRadioButtonId();
        if (idSexoSelecionado == -1) {
            Toast.makeText(
                    TerceirizadoActivity.this,
                    "Selecione o sexo do terceirizado.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        RadioButton radioButtonSexoSelecionado = findViewById(idSexoSelecionado);
        Object tagSexo = radioButtonSexoSelecionado.getTag();
        String stCodSexo = tagSexo != null
                ? tagSexo.toString()
                : radioButtonSexoSelecionado.getText().toString().substring(0, 1).toUpperCase();

        Date dtDatNascimento = null;
        if (!stDatNascimento.isEmpty()) {
            dtDatNascimento = stringParaDate(stDatNascimento);
            if (dtDatNascimento == null) {
                Toast.makeText(this, "A Data de Nascimento está inválida (use dd/MM/yyyy)!", Toast.LENGTH_LONG).show();
                return;
            }
        }


        // Monta o objeto atualizado preservando ID, CPF original e demais campos existentes
        Terceirizado terceirizadoAtualizado = new Terceirizado();
        terceirizadoAtualizado.setIdeTerceirizado(terceirizado.getIdeTerceirizado());
        terceirizadoAtualizado.setNomTerceirizado(stNomTerceirizado);
        terceirizadoAtualizado.setNumCPF(terceirizado.getNumCPF());
        terceirizadoAtualizado.setDatNascimento(dtDatNascimento);
        terceirizadoAtualizado.setCodSexo(stCodSexo);
        terceirizadoAtualizado.setDesEmail(stEmail.isEmpty() ? null : stEmail);
        terceirizadoAtualizado.setNomLogradouro(terceirizado.getNomLogradouro());
        terceirizadoAtualizado.setNomBairro(terceirizado.getNomBairro());
        terceirizadoAtualizado.setNomCidade(terceirizado.getNomCidade());
        terceirizadoAtualizado.setSigUF(terceirizado.getSigUF());
        terceirizadoAtualizado.setNumCep(terceirizado.getNumCep());
        terceirizadoAtualizado.setNomPais(terceirizado.getNomPais());

        btTercSalvar.setEnabled(false);

        Call<Terceirizado> chamadaAtualizar = terceirizadoService.atualizar(
                terceirizado.getIdeTerceirizado().toString(),
                terceirizadoAtualizado
        );

        chamadaAtualizar.enqueue(new Callback<Terceirizado>() {
            @Override
            public void onResponse(Call<Terceirizado> call, Response<Terceirizado> response) {
                btTercSalvar.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    terceirizado = response.body();
                    cancelarOperacao();

                    Toast.makeText(
                            TerceirizadoActivity.this,
                            "Terceirizado atualizado com sucesso!",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            TerceirizadoActivity.this,
                            "Erro ao salvar terceirizado (HTTP " + response.code() + ")",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Terceirizado> call, Throwable t) {
                btTercSalvar.setEnabled(true);
                Toast.makeText(
                        TerceirizadoActivity.this,
                        "Falha na comunicação com o servidor: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }


    private void listarPostosTrabalhos(UUID ideTerceirizado) {

        Call<List<Ocupa>> servicoRest = ocupaService.buscarPorTerceirizado(ideTerceirizado.toString());
        servicoRest.enqueue(new Callback<List<Ocupa>>() {
            @Override
            public void onResponse(Call<List<Ocupa>> call, Response<List<Ocupa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPostosTrabalho = response.body(); // Lib Moshi transforma  JSOn em Objeto

                    ArrayAdapter<Ocupa> adapter = new ArrayAdapter<Ocupa>(
                            TerceirizadoActivity.this,
                            android.R.layout.simple_list_item_2,
                            android.R.id.text1,
                            listaPostosTrabalho
                    ) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            // Deixa o Android reutilizar a linha se possível (performance)
                            View view = super.getView(position, convertView, parent);

                            Ocupa postoTrabalhoTerceirizado = getItem(position);

                            // Referências para os dois TextViews nativos do simple_list_item_2
                            TextView text1 = view.findViewById(android.R.id.text1);
                            TextView text2 = view.findViewById(android.R.id.text2);

                            if (postoTrabalhoTerceirizado != null) {
                                // Linha 1: Nome do Posto de Trabalho

                                String stNomPostoTrabalho = postoTrabalhoTerceirizado.getPostoTrabalho().getNomPostoTrabalho();
                                String nomePosto = postoTrabalhoTerceirizado.getPostoTrabalho() != null
                                        ? postoTrabalhoTerceirizado.getPostoTrabalho().getNomPostoTrabalho()
                                        : null;
                                text1.setText(nomePosto != null ? nomePosto : "Posto de trabalho não identificado");

                                // Linha 2: Período e Situação
                                if (postoTrabalhoTerceirizado.getDatFim() == null) {
                                    text2.setText("Início: " + postoTrabalhoTerceirizado.getDatInicioBr() + " Situação: " + postoTrabalhoTerceirizado.getDesSituacao());
                                } else {
                                    text2.setText("Período: " + postoTrabalhoTerceirizado.getDatInicioBr() + " a " + postoTrabalhoTerceirizado.getDatFimBr() + " Situação: " + postoTrabalhoTerceirizado.getDesSituacao());
                                }

                            }
                            return view;
                        }
                    };
                    // Associa o adapter à ListView
                    lvTercPostosTrabalho.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Ocupa>> call, Throwable t) {
                Toast.makeText(TerceirizadoActivity.this, "Falha na chamada do serviço remoto. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void definirModoEdicao(boolean editavel) {
        campoEditavel.setCampoEditavel(etTercNomTerceirizado, editavel);
        campoEditavel.setCampoEditavel(etTercNumCpf, editavel);
        campoEditavel.setCampoData(etTercDataNascimento, editavel);
        campoEditavel.setCampoEditavel(etTercDesEmail, editavel);
        campoEditavel.setRadioGroupEditavel(rgTercCodSexo, editavel);

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