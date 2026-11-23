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
import com.raposo.gterandroid.client.RetrofitClient;
import com.raposo.gterandroid.model.Contrato;
import com.raposo.gterandroid.model.Orgao;
import com.raposo.gterandroid.service.ContratoService;
import com.raposo.gterandroid.service.OrgaoService;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratoActivity extends AppCompatActivity {

    private ContratoService contratoService;

    private ListView lvContrContratos;
    private MaterialToolbar mtbContrCabecalho;

    private ArrayAdapter<Contrato> adapter;

    private List<Contrato> listaContratos;

    private UUID ideOrgao; // Orgao selecionado na tela de Orgãos
    private String nomOrgao ; // Nome do Orgao selecionadona tela de Orgãos


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contrato);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // aponta para os componentes
        lvContrContratos = findViewById(R.id.lvContrContratos);
        mtbContrCabecalho = findViewById(R.id.mtbContrCabecalho);



        contratoService = RetrofitClient.getRetrofit().create(ContratoService.class);

        // Obtem os valores passados na Intent Orgao

        ideOrgao = UUID.fromString(getIntent().getStringExtra("stIdeOrgao"));
        nomOrgao = getIntent().getStringExtra("stNomOrgao");

        // Carrega o Nome do Orgão no Cabecalho

        mtbContrCabecalho.setSubtitle(nomOrgao);
        mtbContrCabecalho.setSubtitleCentered(true);

        // Lista os Contratos do Orgão
        listarContratos(ideOrgao);

        // Se clicar em uma linah do ListView chama a Tela de Terceirizados do Contrato
        lvContrContratos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contrato contratoSelecionado = listaContratos.get(position);

                telaTerceirizadosContrato(contratoSelecionado);
            }
        });


        // Manda para tela anterior
        mtbContrCabecalho.setNavigationOnClickListener(v->
                getOnBackPressedDispatcher().onBackPressed()
        );
    }

    // Carrega o listView com os cotratos deste Orgao
    private void listarContratos(UUID ideOrgao) {
        //Call<List<Contrato>> servicoRest = contratoService.listarTodos();

        Call<List<Contrato>> servicoRest = contratoService.buscarPorOrgao(ideOrgao.toString());
        servicoRest.enqueue(new Callback<List<Contrato>>() {
            @Override
            public void onResponse(Call<List<Contrato>> call, Response<List<Contrato>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    listaContratos = response.body(); // Lib Moshi transporma JSOn em Objeto


                    adapter = new ArrayAdapter<Contrato>(
                            ContratoActivity.this,
                            android.R.layout.simple_list_item_2,
                            android.R.id.text1,
                            listaContratos
                    ) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            // Deixa o Android reutilizar a linha se possível (performance)
                            View view = super.getView(position, convertView, parent);

                            Contrato contrato = getItem(position);

                            // Referências para os dois TextViews nativos do simple_list_item_2
                            TextView text1 = view.findViewById(android.R.id.text1);
                            TextView text2 = view.findViewById(android.R.id.text2);

                            if (contrato != null) {
                                // Linha 1: Descrição e Valor

                                text1.setText(contrato.getNumContrato()+"/"+contrato.getAnoContrato());

                                // Linha 2: Data E Valor

                                text2.setText(contrato.getFornecedor().getNomFornecedor());
                            }
                            return view;
                        }
                    };
                    // Associa o adapter à ListView
                    lvContrContratos.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Contrato>> call, Throwable t) {
                Toast.makeText(ContratoActivity.this, "Falha na chamada do serviço remoto. Tente novamente mais tarde.",Toast.LENGTH_LONG).show();

            }
        });


    }

    private void telaTerceirizadosContrato(Contrato contratoSelecionado) {
        Intent intent = new Intent(ContratoActivity.this, TerceirizadosContratoActivity.class);
        intent.putExtra("stIdeContrato", contratoSelecionado.getIdeContrato().toString());
        intent.putExtra("inAnoContrato", contratoSelecionado.getAnoContrato());
        intent.putExtra("inNumContrato", contratoSelecionado.getNumContrato());
        intent.putExtra("stNomFornecedor",contratoSelecionado.getFornecedor().getNomFornecedor());
        startActivity(intent);
    }


}