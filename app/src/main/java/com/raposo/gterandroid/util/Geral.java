package com.raposo.gterandroid.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Geral {

    // Regex padrão para validação de e-mail
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Valida um endereço de e-mail.
     *
     * @param email String contendo o e-mail, podendo ser nulo.
     * @return true se o e-mail for válido, false caso contrário.
     */
    public static boolean isEmailValido(String email) {
        // 1. Verifica se é nulo ou vazio (removendo espaços em branco)
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // 2. Compara com a expressão regular
        Matcher matcher = EMAIL_PATTERN.matcher(email.trim());
        return matcher.matches();
    }


    /**
     * Valida um número de CPF.
     *
     * @param cpf String contendo o CPF (já sem máscara ou com dígitos puros, podendo ser nulo).
     * @return true se o CPF for válido, false caso contrário.
     */
    public static boolean isCpfValido(String cpf) {
        // 1. Verifica se é nulo ou vazio
        if (cpf.isBlank()) {
            return false;
        }

        // Remove qualquer caractere não numérico por segurança (caso venha com alguma máscara residual)
        cpf = cpf.replaceAll("\\D", "");

        // 2. CPF deve ter exatamente 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // 3. Descarta CPFs com todos os dígitos iguais (ex: "00000000000", "11111111111")
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            // 4. Cálculo do 1º Dígito Verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito > 9) {
                primeiroDigito = 0;
            }
            if (primeiroDigito != Character.getNumericValue(cpf.charAt(9))) {
                return false;
            }

            // 5. Cálculo do 2º Dígito Verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito > 9) {
                segundoDigito = 0;
            }
            if (segundoDigito != Character.getNumericValue(cpf.charAt(10))) {
                return false;
            }

            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

}
