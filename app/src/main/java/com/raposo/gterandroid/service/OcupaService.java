package com.raposo.gterandroid.service;

import com.raposo.gterandroid.model.Ocupa;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.UUID;

public interface OcupaService {

    @GET("ocupa")
    Call<List<Ocupa>> listarTodas();

    @GET("ocupa/{ideOcupa}")
    Call<Ocupa> buscarPorId(@Path("id") String ideOcupa);

    @GET("ocupa/terceirizado/{ideTerceirizado}")
    Call<List<Ocupa>> buscarPorTerceirizado(@Path("ideTerceirizado") String ideTerceirizado);

    @GET("ocupa/posto-trabalho/{idePostoTrabalho}")
    Call<List<Ocupa>> buscarPorPostoTrabalho(@Path("idePostoTrabalho") String idePostoTrabalho);

    @GET("ocupa/terceirizado/{ideTerceirizado}/ativas")
    Call<List<Ocupa>> buscarAtivasPorTerceirizado(@Path("ideTerceirizado") String ideTerceirizado);

    @GET("ocupa/posto-trabalho/{idePostoTrabalho}/ativas")
    Call<List<Ocupa>> buscarAtivasPorPostoTrabalho(@Path("idePostoTrabalho") String idePostoTrabalho);

    @POST("ocupa")
    Call<Ocupa> salvar(@Body Ocupa ocupa);

    @PUT("ocupa/{id}")
    Call<Ocupa> atualizar(@Path("id") String id, @Body Ocupa ocupa);

    @DELETE("ocupa/{id}")
    Call<Void> deletar(@Path("id") String id);
}