package com.raposo.gterandroid.util;

import android.content.Context;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import android.util.Log;

public class DateUtils {

    /**
     * Converte uma String para LocalDate utilizando o formato de data
     * configurado no dispositivo Android.
     *
     * @param context Contexto da aplicação ou Activity (necessário para ler as configurações do aparelho).
     * @param dataStr String contendo a data, podendo ser nula ou vazia.
     * @return LocalDate correspondente ou null se houver erro/estiver vazio.
     */
    public static LocalDate converterParaLocalDate(Context context, String dataStr) {
        // 1. Trata se for nulo ou vazio
        if (dataStr == null || dataStr.trim().isEmpty()) {
            throw new IllegalArgumentException("A Data está vazia.");

        }

        try {
            // 2. Obtém o formatador padrão configurado no sistema operacional do dispositivo
            DateFormat deviceDateFormat = android.text.format.DateFormat.getDateFormat(context);

            // Extrai o padrão (pattern) do dispositivo para usar com o LocalDate (DateTimeFormatter)
            String pattern = "dd/MM/yyyy"; // Fallback de segurança padrão brasileiro
            if (deviceDateFormat instanceof SimpleDateFormat) {
                pattern = ((SimpleDateFormat) deviceDateFormat).toPattern();
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            //Log.d("DateUtils","Dados do Formatter:"+formatter.toString());

            // 3. Tenta fazer o parse usando o formato dinâmico do aparelho
            return LocalDate.parse(dataStr.trim(), formatter);

        } catch (Exception e) {
            // Captura falhas de parse ou exceções caso a string seja inválida para o formato do aparelho
            throw e;

        }
    }
}