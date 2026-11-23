package com.raposo.gterandroid.service;

import com.raposo.gterandroid.model.PostoTrabalho;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.UUID;

public interface PostoTrabalhoService {

    @GET("postotrabalho")
    Call<List<PostoTrabalho>> listarTodos();

    @GET("postotrabalho/contrato/{ideContrato}")
    Call<List<PostoTrabalho>> listarPostoTrabalhoContrato(@Path("ideContrato") String ideContrato);

    @GET("postotrabalho/{idePostoTrabalho}")
    Call<PostoTrabalho> buscarPorId(@Path("idePostoTrabalho") String idePostoTrabalho);

    @POST("postotrabalho")
    Call<PostoTrabalho> incluir(@Body PostoTrabalho postoTrabalho);

    @PUT("postotrabalho")
    Call<PostoTrabalho> atualizar(@Body PostoTrabalho postoTrabalhoAtualizado);

    @DELETE("postotrabalho/{idePostoTrabalho}")
    Call<Void> excluir(@Path("idePostoTrabalho") String idePostoTrabalho);
}
