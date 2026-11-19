package com.raposo.gterandroid.service;

import com.raposo.gterandroid.model.Contrato;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;


public interface ContratoService {

    @GET("contrato")
    Call<List<Contrato>> listarTodos();

    @GET("contrato/{ideContrato}")
    Call<Contrato> buscarPorId(@Path("ideContrato") String ideContrato);

    @GET("contrato/orgao/{ideOrgao}")
    Call<List<Contrato>> buscarPorOrgao(@Path("ideOrgao") String ideOrgao);

    @POST("contrato")
    Call<Contrato> incluir(@Body Contrato contrato);

    @PUT("contrato")
    Call<Contrato> atualizar(@Body Contrato contratoAtualizado);

    @DELETE("contrato/{ideContrato}")
    Call<Void> excluir(@Path("ideContrato") String ideContrato);
}
