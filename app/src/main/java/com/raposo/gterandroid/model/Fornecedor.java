package com.raposo.gterandroid.model;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;
import java.util.UUID;


public class Fornecedor {

    @Json(name = "ideFornecedor")
    private String ideFornecedor;

    @Json(name = "nomFornecedor")
    private String nomFornecedor;

    @Json(name = "numCNPJ")
    private String numCNPJ;

    public Fornecedor() {
    }

    public Fornecedor(String ideFornecedor, String nomFornecedor, String numCNPJ) {
        this.ideFornecedor = ideFornecedor;
        this.nomFornecedor = nomFornecedor;
        this.numCNPJ = numCNPJ;
    }

    public String getIdeFornecedor() {
        return ideFornecedor;
    }

    public void setIdeFornecedor(String ideFornecedor) {
        this.ideFornecedor = ideFornecedor;
    }

    public String getNomFornecedor() {
        return nomFornecedor;
    }

    public void setNomFornecedor(String nomFornecedor) {
        this.nomFornecedor = nomFornecedor;
    }

    public String getNumCNPJ() {
        return numCNPJ;
    }

    public void setNumCNPJ(String numCNPJ) {
        this.numCNPJ = numCNPJ;
    }

    public String getNumCNPJFormatado() {
        if (numCNPJ == null || numCNPJ.length() != 14) return numCNPJ;
        return numCNPJ.substring(0, 2) + "." +
                numCNPJ.substring(2, 5) + "." +
                numCNPJ.substring(5, 8) + "/" +
                numCNPJ.substring(8, 12) + "-" +
                numCNPJ.substring(12, 14);
    }
}