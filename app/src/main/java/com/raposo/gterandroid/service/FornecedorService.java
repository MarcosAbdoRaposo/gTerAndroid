package com.raposo.gterandroid.service;

import com.raposo.gterandroid.model.Fornecedor;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.UUID;

public interface FornecedorService {

    @GET("fornecedor")
    Call<List<Fornecedor>> listarTodos();

    @GET("fornecedor/{ideFornecedor}")
    Call<Fornecedor> buscarPorId(@Path("ideFornecedor") UUID ideFornecedor);

    @POST("fornecedor")
    Call<Fornecedor> incluir(@Body Fornecedor fornecedor);

    @PUT("fornecedor")
    Call<Fornecedor> atualizar(@Body Fornecedor fornecedorAtualizado);

    @DELETE("fornecedor/{ideFornecedor}")
    Call<Void> excluir(@Path("ideFornecedor") UUID ideFornecedor);
}

