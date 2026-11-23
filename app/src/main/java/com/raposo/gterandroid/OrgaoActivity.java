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
import com.raposo.gterandroid.model.Orgao;
import com.raposo.gterandroid.service.OrgaoService;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrgaoActivity extends AppCompatActivity {

    private OrgaoService orgaoService;

    private ListView lvOrgaoOrgaos;

    private MaterialToolbar mtbOrgaoCabecalho;
    private ArrayAdapter<Orgao> adapter;

    private  List<Orgao> listaOrgaos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orgao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Aponta para os caomponentes
        lvOrgaoOrgaos = findViewById(R.id.lvOrgaoOrgaos);
        mtbOrgaoCabecalho = findViewById(R.id.mtbOrgaoCabecalho);

        // Aponta para o serviço
        orgaoService = RetrofitClient.getRetrofit().create(OrgaoService.class);

        listarOrgaos(); // Carrega o ListView com os Orgãos


        // Clique Simples no List View, vai chamar a tela de Contratos do Orgao
        lvOrgaoOrgaos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Orgao orgaoSelecionado = listaOrgaos.get(position);

                telaContratosOrgao(orgaoSelecionado);
            }
        });

        // Manda para tela anterior
        mtbOrgaoCabecalho.setNavigationOnClickListener(v->
                getOnBackPressedDispatcher().onBackPressed()
                );






    }

    private void telaContratosOrgao(Orgao orgaoSelecionado) {

        Intent intent = new Intent(OrgaoActivity.this, ContratoActivity.class);

        // 4. Inicia a nova tela
        intent.putExtra("stIdeOrgao", orgaoSelecionado.getIdeOrgao().toString());
        intent.putExtra("stNomOrgao",orgaoSelecionado.getNomOrgao());
        startActivity(intent);

    }

    private void listarOrgaos() {
        //Toast.makeText(OrgaoActivity.this, "Entrou na Tela",Toast.LENGTH_LONG).show();


        Call<List<Orgao>> servicoRest = orgaoService.listarTodos();
        servicoRest.enqueue(new Callback<List<Orgao>>() {
            @Override
            public void onResponse(Call<List<Orgao>> call, Response<List<Orgao>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    listaOrgaos = response.body(); // Lib Moshi transporma JSOn em Objeto


                    adapter = new ArrayAdapter<Orgao>(
                            OrgaoActivity.this,
                            android.R.layout.simple_list_item_2,
                            android.R.id.text1,
                            listaOrgaos
                    ) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            // Deixa o Android reutilizar a linha se possível (performance)
                            View view = super.getView(position, convertView, parent);

                            Orgao orgao = getItem(position);

                            // Referências para os dois TextViews nativos do simple_list_item_2
                            TextView text1 = view.findViewById(android.R.id.text1);
                            TextView text2 = view.findViewById(android.R.id.text2);

                            if (orgao != null) {
                                // Linha 1: Descrição e Valor

                                text1.setText(orgao.getNomOrgao());

                                // Linha 2: Data E Valor

                                text2.setText(orgao.getNumCNPJFormatado());
                            }
                            return view;
                        }
                    };
                    // Associa o adapter à ListView
                    lvOrgaoOrgaos.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Orgao>> call, Throwable t) {
                Toast.makeText(OrgaoActivity.this, "Falha na chamada do serviço remoto. Tente novamente mais tarde.",Toast.LENGTH_LONG).show();



            }
        });

     ;




    }
}