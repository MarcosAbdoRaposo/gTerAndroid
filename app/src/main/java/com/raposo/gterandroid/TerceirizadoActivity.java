package com.raposo.gterandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
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
import com.raposo.gterandroid.util.CampoEditavel;
import com.raposo.gterandroid.util.DateUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TerceirizadoActivity extends AppCompatActivity {

    private OcupaService ocupaService;

    private List<Ocupa> listaPostosTrabalho;

    private ArrayAdapter<Ocupa> adapter;

    // Utilizado para salvar os KeyListene dos campos que serão colocados como Somente Leitura
    //private final Map<TextInputEditText, KeyListener> keyListenersOriginais = new HashMap<>();

    private final CampoEditavel campoEditavel = new CampoEditavel();
    private MaterialToolbar mtbTercCabecalho;
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

        ocupaService = RetrofitClient.getRetrofit().create(OcupaService.class);

        mtbTercCabecalho = findViewById(R.id.mtbTercCabecalho);
        // aponta para componentes
        etTercNomTerceirizado = findViewById(R.id.etTercNomTerceirizado);
        etTercNumCpf = findViewById(R.id.etTercNumCpf);
        //Mascara.cpf(etTercNumCpf);

        etTercDataNascimento = findViewById(R.id.etTercDataNascimento);
        //Mascara.data(etTercDataNascimento);

        rgTercCodSexo = findViewById(R.id.rgTercCodSexo);
        rbTercMasculino = findViewById(R.id.rbTercMasculino);
        rbTercFeminino = findViewById(R.id.rbTercFeminino);

        etTercDesEmail = findViewById(R.id.etTercDesEmail);

        lvTercPostosTrabalho = findViewById(R.id.lvTercPostosTrabalho);

        btTercCancelar = findViewById(R.id.btTercCancelar);

        // Obtem os valores transmitidos pela tela de TerceirizadosContrato

        String stIdeContrato = getIntent().getStringExtra("stIdeContrato");
        String stIdeTerceirizado = getIntent().getStringExtra("stIdeTerceirizado");

        String stNomTerceirizado = getIntent().getStringExtra("stNomTerceirizado");
        String stNumCPF = getIntent().getStringExtra("stNumCPF"); // stNumCPF
        String stDataNascimento = getIntent().getStringExtra("stDatNascimento");
        String stCodSexo = getIntent().getStringExtra("stCodSexo");
        String stDesEmail = getIntent().getStringExtra("stDesEmail");

        // Verifica se os campos foram preenchidos

        if (stIdeContrato != null){
            ideContrato = UUID.fromString(stIdeContrato);
        } else {
            Toast.makeText(TerceirizadoActivity.this,"Não foi enviado o ideContrato",Toast.LENGTH_LONG).show();
        }

        if (stIdeTerceirizado != null){
            ideTerceirizado = UUID.fromString(stIdeTerceirizado);
        } else {
            Toast.makeText(TerceirizadoActivity.this,"Não foi enviado o ideTerceirizado",Toast.LENGTH_LONG).show();
        }

        // Preenche a tela
        etTercNomTerceirizado.setText(stNomTerceirizado);
        if (stNumCPF != null) {
            String stNumCPFFormatado = stNumCPF.substring(0, 3) + "." + stNumCPF.substring(3, 6) + "." + stNumCPF.substring(6, 9) + "-" + stNumCPF.substring(9, 11);
            etTercNumCpf.setText(stNumCPFFormatado);
        } else {
            Toast.makeText(TerceirizadoActivity.this,"Não foi enviado o ideTerceirizado",Toast.LENGTH_LONG).show();
        }
        //TODO Ver como está sendo gerada o formato Data
        if (stDataNascimento != null) {
            Date dtDataNascimento = Data.isoToDate(stDataNascimento);
            etTercDataNascimento.setText(Data.dateToBr(dtDataNascimento));
        } else {
            Toast.makeText(TerceirizadoActivity.this,"Não foi enviado a Data de Nascimento",Toast.LENGTH_LONG).show();
        }
        if (stCodSexo != null) {
            if (stCodSexo.equals("M")) {
                rbTercMasculino.setChecked(true);
                rbTercFeminino.setChecked(false);
            } else {
                rbTercMasculino.setChecked(false);
                rbTercFeminino.setChecked(true);
            }
        } else {
            Toast.makeText(TerceirizadoActivity.this,"Não foi enviado o Sexo do Terceirizado!",Toast.LENGTH_LONG).show();
        }


        etTercDesEmail.setText(stDesEmail);

        // Deixa os campos somente leitura

        // Salva os KeyListener dos campos Text
        campoEditavel.registrarKeyListeners(
                etTercNomTerceirizado,
                etTercNumCpf,
                etTercDataNascimento,
                etTercDesEmail
        );

        // Deiza a Tela não editavel
        definirModoEdicao(false);

        btTercCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TerceirizadoActivity.this,"Falta Implementar",Toast.LENGTH_LONG).show();
            }
        });

        btTercSalvar = findViewById(R.id.btTercSalvar);
        btTercSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarTerceirizado();
            }
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

        listarPostosTrabalhos(ideTerceirizado); // Lista os Postos de Trabalho do Terceirizado
    }

    private void telaPostosTrabalhoTerceirizado(Ocupa ocupacaoSelecionada) {
        Intent intent = new Intent(TerceirizadoActivity.this, PostoTrabalhoTerceirizadoActivity.class);
        intent.putExtra("stIdeContrato", ideContrato.toString());
        intent.putExtra("stIdeOcupa", ocupacaoSelecionada.getIdeOcupa().toString());
        intent.putExtra("stIdePostoTrabalho", ocupacaoSelecionada.getPostoTrabalho().getIdePostoTrabalho().toString());
        intent.putExtra("stDatInicio", Data.dateToIso(ocupacaoSelecionada.getDatInicio())); // Formato ISO
        intent.putExtra("stDatFim", Data.dateToIso(ocupacaoSelecionada.getDatFim())); // Formato ISO
        intent.putExtra("stCodSituacao", ocupacaoSelecionada.getCodSituacao());
        intent.putExtra("stDesLotacao", ocupacaoSelecionada.getDesLotacao());
        intent.putExtra("stDesLocalizacao", ocupacaoSelecionada.getDesLocalizacao());

        startActivity(intent);

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

    private void listarPostosTrabalhos(UUID ideTerceirizado) {

        Call<List<Ocupa>> servicoRest = ocupaService.buscarPorTerceirizado(ideTerceirizado.toString());
        servicoRest.enqueue(new Callback<List<Ocupa>>() {
            @Override
            public void onResponse(Call<List<Ocupa>> call, Response<List<Ocupa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPostosTrabalho = response.body(); // Lib Moshi transforma  JSOn em Objeto


                    adapter = new ArrayAdapter<Ocupa>(
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
                                // Linha 1: Descrição e Valor

                                String stNomPostoTrabalho = postoTrabalhoTerceirizado.getPostoTrabalho().getNomPostoTrabalho();
                                String nomePosto = postoTrabalhoTerceirizado.getPostoTrabalho() != null
                                        ? postoTrabalhoTerceirizado.getPostoTrabalho().getNomPostoTrabalho()
                                        : null;
                                text1.setText(nomePosto != null ? nomePosto : "Posto de trabalho não identificado");

                                // Linha 2: Data E Valor
                                if (postoTrabalhoTerceirizado.getDatFim() == null) {
                                    text2.setText("Início: " + postoTrabalhoTerceirizado.getDatInicioBr()+" Situação: "+postoTrabalhoTerceirizado.getDesSituacao());
                                } else {
                                    text2.setText("Período: " + postoTrabalhoTerceirizado.getDatInicioBr()+" a "+postoTrabalhoTerceirizado.getDatFimBr()+" Situação: "+postoTrabalhoTerceirizado.getDesSituacao());
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
        campoEditavel.setCampoEditavel(etTercDataNascimento, editavel);
        campoEditavel.setCampoEditavel(etTercDesEmail, editavel);

        campoEditavel.setRadioGroupEditavel(rgTercCodSexo, editavel);

    }

}