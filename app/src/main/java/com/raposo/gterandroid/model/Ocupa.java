package com.raposo.gterandroid.model;

import com.raposo.gterandroid.components.Data;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;
import java.util.Date;
import java.util.UUID;


public class Ocupa {

    @Json(name = "ideOcupa")
    private String ideOcupa;

    @Json(name = "terceirizado")
    private Terceirizado terceirizado;

    @Json(name = "postoTrabalho")
    private PostoTrabalho postoTrabalho;

    @Json(name = "codSituacao")
    private String codSituacao;

    @Json(name = "datInicio")
    private Date datInicio;

    @Json(name = "datFim")
    private Date datFim;

    @Json(name = "desLotacao")
    private String desLotacao;

    @Json(name = "desLocalizacao")
    private String desLocalizacao;


    public Ocupa() {
    }

    public Ocupa(String ideOcupa, Terceirizado terceirizado, PostoTrabalho postoTrabalho, String codSituacao, Date datInicio, Date datFim, String desLotacao, String desLocalizacao) {
        this.ideOcupa = ideOcupa;
        this.terceirizado = terceirizado;
        this.postoTrabalho = postoTrabalho;
        this.codSituacao = codSituacao;
        this.datInicio = datInicio;
        this.datFim = datFim;
        this.desLotacao = desLotacao;
        this.desLocalizacao = desLocalizacao;
    }

    public String getIdeOcupa() {
        return ideOcupa;
    }

    public void setIdeOcupa(String ideOcupa) {
        this.ideOcupa = ideOcupa;
    }

    public Terceirizado getTerceirizado() {
        return terceirizado;
    }

    public void setTerceirizado(Terceirizado terceirizado) {
        this.terceirizado = terceirizado;
    }

    public PostoTrabalho getPostoTrabalho() {
        return postoTrabalho;
    }

    public void setPostoTrabalho(PostoTrabalho postoTrabalho) {
        this.postoTrabalho = postoTrabalho;
    }

    public String getCodSituacao() {
        return codSituacao;
    }

    public String getDesSituacao() {
        if (this.codSituacao == null) return "";
        if (this.codSituacao.equalsIgnoreCase("T")) {
            return "Titular";
        } else {
            return "Extra";
        }
    }

    public void setCodSituacao(String codSituacao) {
        this.codSituacao = codSituacao;
    }

    public Date getDatInicio() {
        return datInicio;
    }

    public String getDatInicioBr(){
        return Data.dateToBr(datInicio);
    }


    public void setDatInicio(Date datInicio) {
        this.datInicio = datInicio;
    }

    public Date getDatFim() {
        return datFim;
    }

    public String getDatFimBr() {
        return Data.dateToBr(datFim);
    }

    public void setDatFim(Date datFim) {
        this.datFim = datFim;
    }


    public String getDesLotacao() {
        return desLotacao;
    }

    public void setDesLotacao(String desLotacao) {
        this.desLotacao = desLotacao;
    }

    public String getDesLocalizacao() {
        return desLocalizacao;
    }

    public void setDesLocalizacao(String desLocalizacao) {
        this.desLocalizacao = desLocalizacao;
    }
}