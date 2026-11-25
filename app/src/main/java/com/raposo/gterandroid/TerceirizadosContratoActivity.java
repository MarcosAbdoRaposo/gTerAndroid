package com.raposo.gterandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raposo.gterandroid.client.RetrofitClient;
import com.raposo.gterandroid.components.Data;
import com.raposo.gterandroid.model.Contrato;
import com.raposo.gterandroid.model.Terceirizado;
import com.raposo.gterandroid.service.TerceirizadoService;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TerceirizadosContratoActivity extends AppCompatActivity {


    private TerceirizadoService terceirizadoService;

    private ListView lvTerContTerceirizados;

    private MaterialToolbar mtbTerContCabecalho;
    private FloatingActionButton fabTerContAdicionarTerceirizado;

    private ArrayAdapter<Terceirizado> adapter;

    private List<Terceirizado> listaTerceirizados;

    private UUID ideContrato; // Contrato selecionado na tela de Contratos
    private String nomFornecedor ; // Nome do Fornecedor do Contrato

    private int anoContrato;  // Ano do Contrato
    private int numContrato; // Número do Contrato

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terceirizados_contrato);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Cria o Serviço
        terceirizadoService = RetrofitClient.getRetrofit().create(TerceirizadoService.class);

        // Mapeia os componentes da Tela
        lvTerContTerceirizados = findViewById(R.id.lvTerContTerceirizados);
        mtbTerContCabecalho = findViewById(R.id.mtbTerContCabecalho);
        fabTerContAdicionarTerceirizado = findViewById(R.id.fabTercAdicionarTerceirizado);



        // Obtem os valores passados na Intent Orgao

        ideContrato = UUID.fromString(getIntent().getStringExtra("stIdeContrato"));
        nomFornecedor = getIntent().getStringExtra("stNomFornecedor");
        anoContrato = getIntent().getIntExtra("inAnoContrato", 0);
        numContrato = getIntent().getIntExtra("inNumContrato", 0);

        // Preenche o Cabecalho
        mtbTerContCabecalho.setTitle("Contrato "+String.valueOf(numContrato)+"/"+String.valueOf(anoContrato));
        mtbTerContCabecalho.setTitleCentered(true);
        mtbTerContCabecalho.setSubtitle(nomFornecedor);
        mtbTerContCabecalho.setSubtitleCentered(true);





        // Associa evento de selecionar no ListView
        lvTerContTerceirizados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Terceirizado terceirizadoSelecionado = listaTerceirizados.get(position);

                telaTerceirizados(terceirizadoSelecionado);
            }
        });

        // Manda para tela anterior
        mtbTerContCabecalho.setNavigationOnClickListener(v->
                getOnBackPressedDispatcher().onBackPressed()
        );

        // FAB
        fabTerContAdicionarTerceirizado.setOnClickListener(v -> abrirTelaInclusaoTerceirizado());

    }

    @Override
    protected void onResume() {
        super.onResume();
        listarTerceirizados(ideContrato);  // Lista os Terceirizados
    }

    private void abrirTelaInclusaoTerceirizado() {
        Intent intent = new Intent(TerceirizadosContratoActivity.this, InclusaoTerceirizadoActivity.class);
        intent.putExtra("stIdeContrato", ideContrato.toString());
        startActivity(intent);
    }


    private void listarTerceirizados(UUID ideContrato) {
        //Call<List<Contrato>> servicoRest = contratoService.listarTodos();

        Call<List<Terceirizado>> servicoRest = terceirizadoService.buscarPorContrato(ideContrato.toString());
        servicoRest.enqueue(new Callback<List<Terceirizado>>() {
            @Override
            public void onResponse(Call<List<Terceirizado>> call, Response<List<Terceirizado>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    listaTerceirizados = response.body(); // Lib Moshi transforma  JSOn em Objeto


                    adapter = new ArrayAdapter<Terceirizado>(
                            TerceirizadosContratoActivity.this,
                            android.R.layout.simple_list_item_2,
                            android.R.id.text1,
                            listaTerceirizados
                    ) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            // Deixa o Android reutilizar a linha se possível (performance)
                            View view = super.getView(position, convertView, parent);

                            Terceirizado terceirizado = getItem(position);

                            // Referências para os dois TextViews nativos do simple_list_item_2
                            TextView text1 = view.findViewById(android.R.id.text1);
                            TextView text2 = view.findViewById(android.R.id.text2);

                            if (terceirizado != null) {
                                // Linha 1: Descrição e Valor

                                text1.setText(terceirizado.getNomTerceirizado());

                                // Linha 2: Data E Valor

                                text2.setText("CPF: "+terceirizado.getNumCPFFormatado());
                            }
                            return view;
                        }
                    };
                    // Associa o adapter à ListView
                    lvTerContTerceirizados.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Terceirizado>> call, Throwable t) {
                Toast.makeText(TerceirizadosContratoActivity.this, "Falha na chamada do serviço remoto. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void telaTerceirizados(Terceirizado terceirizado) {
        Intent intent = new Intent(TerceirizadosContratoActivity.this, TerceirizadoActivity.class);
        intent.putExtra("stIdeContrato", ideContrato.toString());
        intent.putExtra("stIdeTerceirizado", terceirizado.getIdeTerceirizado().toString());
        intent.putExtra("stNomTerceirizado", terceirizado.getNomTerceirizado());
        intent.putExtra("stNumCPF", terceirizado.getNumCPF());
        intent.putExtra("stCodSexo",terceirizado.getCodSexo());
        intent.putExtra("stDatNascimento", Data.dateToIso(terceirizado.getDatNascimento())); // Formato ISO
        intent.putExtra("stDesEmail",terceirizado.getDesEmail());


        startActivity(intent);

    }

}