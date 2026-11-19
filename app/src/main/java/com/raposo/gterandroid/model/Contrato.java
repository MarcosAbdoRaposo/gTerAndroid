package com.raposo.gterandroid.model;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;

import java.util.Date;


public class Contrato {

    @Json(name = "ideContrato")
    private String ideContrato;

    @Json(name = "orgao")
    private Orgao orgao;

    @Json(name = "fornecedor")
    private Fornecedor fornecedor;

    @Json(name = "anoContrato")
    private int anoContrato;

    @Json(name = "numContrato")
    private int numContrato;

    @Json(name = "datInicioVigencia")
    private Date datInicioVigencia;

    @Json(name = "datFimVigencia")
    private Date datFimVigencia;

    public Contrato() {
    }

    public Contrato(String ideContrato, Orgao orgao, Fornecedor fornecedor, int anoContrato, int numContrato, Date datInicioVigencia, Date datFimVigencia) {
        this.ideContrato = ideContrato;
        this.orgao = orgao;
        this.fornecedor = fornecedor;
        this.anoContrato = anoContrato;
        this.numContrato = numContrato;
        this.datInicioVigencia = datInicioVigencia;
        this.datFimVigencia = datFimVigencia;
    }

    public String getIdeContrato() {
        return ideContrato;
    }

    public void setIdeContrato(String ideContrato) {
        this.ideContrato = ideContrato;
    }

    public Orgao getOrgao() {
        return orgao;
    }

    public void setOrgao(Orgao orgao) {
        this.orgao = orgao;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public int getAnoContrato() {
        return anoContrato;
    }

    public void setAnoContrato(int anoContrato) {
        this.anoContrato = anoContrato;
    }

    public int getNumContrato() {
        return numContrato;
    }

    public void setNumContrato(int numContrato) {
        this.numContrato = numContrato;
    }

    public Date getDatInicioVigencia() {
        return datInicioVigencia;
    }

    public void setDatInicioVigencia(Date datInicioVigencia) {
        this.datInicioVigencia = datInicioVigencia;
    }

    public Date getDatFimVigencia() {
        return datFimVigencia;
    }

    public void setDatFimVigencia(Date datFimVigencia) {
        this.datFimVigencia = datFimVigencia;
    }
}