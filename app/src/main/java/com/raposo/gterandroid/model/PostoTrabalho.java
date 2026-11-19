package com.raposo.gterandroid.model;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;
import java.util.UUID;


public class PostoTrabalho {

    @Json(name = "idePostoTrabalho")
    private String idePostoTrabalho;

    @Json(name = "contrato")
    private Contrato contrato;

    @Json(name = "nomPostoTrabalho")
    private String nomPostoTrabalho;

    @Json(name = "qtdPostoTrabalho")
    private int qtdPostoTrabalho;

    public PostoTrabalho() {
    }

    public PostoTrabalho(String idePostoTrabalho, Contrato contrato, String nomPostoTrabalho, int qtdPostoTrabalho) {
        this.idePostoTrabalho = idePostoTrabalho;
        this.contrato = contrato;
        this.nomPostoTrabalho = nomPostoTrabalho;
        this.qtdPostoTrabalho = qtdPostoTrabalho;
    }

    public String getIdePostoTrabalho() {
        return idePostoTrabalho;
    }

    public void setIdePostoTrabalho(String idePostoTrabalho) {
        this.idePostoTrabalho = idePostoTrabalho;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public String getNomPostoTrabalho() {
        return nomPostoTrabalho;
    }

    public void setNomPostoTrabalho(String nomPostoTrabalho) {
        this.nomPostoTrabalho = nomPostoTrabalho;
    }

    public int getQtdPostoTrabalho() {
        return qtdPostoTrabalho;
    }

    public void setQtdPostoTrabalho(int qtdPostoTrabalho) {
        this.qtdPostoTrabalho = qtdPostoTrabalho;
    }
}