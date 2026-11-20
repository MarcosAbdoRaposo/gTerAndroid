package com.raposo.gterandroid.service;

import com.raposo.gterandroid.model.Terceirizado;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.UUID;

public interface TerceirizadoService {

    @GET("terceirizado")
    Call<List<Terceirizado>> listarTodos();

    @GET("terceirizado/{id}")
    Call<Terceirizado> buscarPorId(@Path("id") String id);

    @GET("terceirizado/cpf/{numCPF}")
    Call<Terceirizado> buscarPorCpf(@Path("numCPF") String numCPF);

    @GET("terceirizado/contrato/{ideContrato}")
    Call<List<Terceirizado>> buscarPorContrato(@Path("ideContrato") String ideContrato);


    @GET("terceirizado/buscar")
    Call<List<Terceirizado>> buscarPorNome(@Query("nome") String nome);

    @POST("terceirizado")
    Call<Terceirizado> salvar(@Body Terceirizado terceirizado);

    @PUT("terceirizado/{id}")
    Call<Terceirizado> atualizar(@Path("id") String id, @Body Terceirizado terceirizado);

    @DELETE("terceirizado/{id}")
    Call<Void> deletar(@Path("id") String id);
}