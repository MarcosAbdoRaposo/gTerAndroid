package com.raposo.gterandroid.model;

import com.squareup.moshi.Json;

public class Orgao {

    @Json(name = "ideOrgao")
    private String ideOrgao;

    @Json(name = "nomOrgao")
    private String nomOrgao;

    @Json(name = "numCNPJ")
    private String numCNPJ;

    public Orgao() {
    }

    public Orgao(String nomOrgao, String numCNPJ) {
        this.nomOrgao = nomOrgao;
        this.numCNPJ = numCNPJ;
    }

    public String getIdeOrgao() {
        return ideOrgao;
    }

    public void setIdeOrgao(String ideOrgao) {
        this.ideOrgao = ideOrgao;
    }

    public String getNomOrgao() {
        return nomOrgao;
    }

    public void setNomOrgao(String nomOrgao) {
        this.nomOrgao = nomOrgao;
    }

    public String getNumCNPJ() {
        return numCNPJ;
    }

    public void setNumCNPJ(String numCNPJ) {
        this.numCNPJ = numCNPJ;
    }

    public String getNumCNPJFormatado() {
        if (numCNPJ == null || numCNPJ.length() != 14) {
            return numCNPJ;
        }
        return numCNPJ.substring(0, 2) + "." +
                numCNPJ.substring(2, 5) + "." +
                numCNPJ.substring(5, 8) + "/" +
                numCNPJ.substring(8, 12) + "-" +
                numCNPJ.substring(12, 14);
    }
}