package com.raposo.gterandroid.model;


import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;
import java.util.Date;
import java.util.UUID;

@JsonClass(generateAdapter = true)
public class Terceirizado {

    @Json(name = "ideTerceirizado")
    private String ideTerceirizado;

    @Json(name = "nomTerceirizado")
    private String nomTerceirizado;

    @Json(name = "numCPF")
    private String numCPF;

    @Json(name = "datNascimento")
    private Date datNascimento;

    @Json(name = "codSexo")
    private String codSexo;

    @Json(name = "nomLogradouro")
    private String nomLogradouro;

    @Json(name = "nomBairro")
    private String nomBairro;

    @Json(name = "nomCidade")
    private String nomCidade;

    @Json(name = "sigUF")
    private String sigUF;

    @Json(name = "nomPais")
    private String nomPais;

    @Json(name = "numCep")
    private String numCep;

    public Terceirizado() {
    }

    public Terceirizado(String ideTerceirizado, String nomTerceirizado, String numCPF, Date datNascimento, String codSexo, String nomLogradouro, String nomBairro, String nomCidade, String sigUF, String nomPais, String numCep) {
        this.ideTerceirizado = ideTerceirizado;
        this.nomTerceirizado = nomTerceirizado;
        this.numCPF = numCPF;
        this.datNascimento = datNascimento;
        this.codSexo = codSexo;
        this.nomLogradouro = nomLogradouro;
        this.nomBairro = nomBairro;
        this.nomCidade = nomCidade;
        this.sigUF = sigUF;
        this.nomPais = nomPais;
        this.numCep = numCep;
    }

    public String getIdeTerceirizado() {
        return ideTerceirizado;
    }

    public void setIdeTerceirizado(String ideTerceirizado) {
        this.ideTerceirizado = ideTerceirizado;
    }

    public String getNomTerceirizado() {
        return nomTerceirizado;
    }

    public void setNomTerceirizado(String nomTerceirizado) {
        this.nomTerceirizado = nomTerceirizado;
    }

    public String getNumCPF() {
        return numCPF;
    }

    public void setNumCPF(String numCPF) {
        this.numCPF = numCPF;
    }

    public Date getDatNascimento() {
        return datNascimento;
    }

    public void setDatNascimento(Date datNascimento) {
        this.datNascimento = datNascimento;
    }

    public String getCodSexo() {
        return codSexo;
    }

    public void setCodSexo(String codSexo) {
        this.codSexo = codSexo;
    }

    public String getNomLogradouro() {
        return nomLogradouro;
    }

    public void setNomLogradouro(String nomLogradouro) {
        this.nomLogradouro = nomLogradouro;
    }

    public String getNomBairro() {
        return nomBairro;
    }

    public void setNomBairro(String nomBairro) {
        this.nomBairro = nomBairro;
    }

    public String getNomCidade() {
        return nomCidade;
    }

    public void setNomCidade(String nomCidade) {
        this.nomCidade = nomCidade;
    }

    public String getSigUF() {
        return sigUF;
    }

    public void setSigUF(String sigUF) {
        this.sigUF = sigUF;
    }

    public String getNomPais() {
        return nomPais;
    }

    public void setNomPais(String nomPais) {
        this.nomPais = nomPais;
    }

    public String getNumCep() {
        return numCep;
    }

    public void setNumCep(String numCep) {
        this.numCep = numCep;
    }
}