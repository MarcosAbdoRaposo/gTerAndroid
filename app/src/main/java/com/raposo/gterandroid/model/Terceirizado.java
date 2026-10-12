package com.raposo.gterandroid.model;

import java.time.LocalDate;

import com.raposo.gterandroid.util.Geral;

public class Terceirizado {
    private String nome;
    private String cpf;
    private LocalDate datNascimento;
    private String codSexo;
    private String email;



    public Terceirizado(String nome, String cpf, LocalDate datNascimento, String codSexo, String email) {
        if (nome.isBlank()) {
            throw new IllegalArgumentException("O nome do terceirizado não foi informado.");
        }
        if (cpf.isBlank()) {
            throw new IllegalArgumentException("O CPF do terceirizado não foi informado.");
        }
        if (! Geral.isCpfValido(cpf)) {
            throw new IllegalArgumentException("O CPF informado não é válido.");
        }
        if (codSexo.isBlank()) {
            throw new IllegalArgumentException("O Sexo do Terceirizado não foi informado");
        }
        if (! (codSexo.equals("M") || codSexo.equals("F"))) {
            throw new IllegalArgumentException("O Sexo do Terceirizado informado não é válido!");
        }
        if (datNascimento == null) {
            throw new IllegalArgumentException("A Data de Nascimento não foi informada!");
        }
        // Verifica se o e-mail foi informado. Em caso positivo, testar
        if (!email.isBlank()) {
            if (!Geral.isEmailValido(email)) {
                throw new IllegalArgumentException("O e-Mail que foi informado está inválido!");
            }
        }

        this.nome = nome;
        this.cpf = cpf;
        this.datNascimento = datNascimento;
        this.codSexo = codSexo;
        this.email = email;
    }
}
