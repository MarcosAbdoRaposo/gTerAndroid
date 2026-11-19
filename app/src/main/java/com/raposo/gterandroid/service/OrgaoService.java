package com.raposo.gterandroid.service;

import com.raposo.gterandroid.model.Orgao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrgaoService {

    @GET("orgao")
    Call<List<Orgao>> listarTodos();

    @GET("orgao/{ideOrgao}")
    Call<Orgao> buscarPorId(@Path("ideOrgao") String ideOrgao);

    @POST("orgao")
    Call<Orgao> incluir(@Body Orgao orgao);

    @PUT("orgao")
    Call<Orgao> atualizar(@Body Orgao orgaoAtualizado);

    @DELETE("orgao/{ideOrgao}")
    Call<Void> excluir(@Path("ideOrgao") String ideOrgao);
}
