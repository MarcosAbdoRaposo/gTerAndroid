package com.raposo.gterandroid.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SeletorData<T extends TextView> {

    public interface OnDataSelecionadaListener {
        void aoSelecionarData(String dataFormatada, Calendar calendario);
    }

    private final Context context;
    private final String padraoData;
    private final Locale locale;

    public SeletorData(Context context) {
        this(context, "dd/MM/yyyy", Locale.getDefault());
    }

    public SeletorData(Context context, String padraoData, Locale locale) {
        this.context = context;
        this.padraoData = padraoData;
        this.locale = locale;
    }

    public void mostrarCalendario(T campoTexto) {
        mostrarCalendario(campoTexto, null);
    }

    public void mostrarCalendario(T campoTexto, OnDataSelecionadaListener listener) {
        final Calendar calendario = Calendar.getInstance();

        if (campoTexto != null && campoTexto.getText() != null) {
            String textoData = campoTexto.getText().toString().trim();
            if (!textoData.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat(padraoData, locale);
                sdf.setLenient(false); // Rejeita datas inválidas como 32/13/2026
                try {
                    Date dataExistente = sdf.parse(textoData);
                    if (dataExistente != null) {
                        calendario.setTime(dataExistente);
                    }
                } catch (ParseException e) {
                    // Mantém a data atual caso o texto não corresponda ao formato esperado
                }
            }
        }

        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, anoSelecionado, mesSelecionado, diaSelecionado) -> {
                    Calendar dataSelecionada = Calendar.getInstance();
                    dataSelecionada.set(Calendar.YEAR, anoSelecionado);
                    dataSelecionada.set(Calendar.MONTH, mesSelecionado);
                    dataSelecionada.set(Calendar.DAY_OF_MONTH, diaSelecionado);

                    String dataFormatada = String.format(
                            locale,
                            "%02d/%02d/%d",
                            diaSelecionado,
                            (mesSelecionado + 1),
                            anoSelecionado
                    );

                    if (campoTexto != null) {
                        campoTexto.setText(dataFormatada);
                    }

                    if (listener != null) {
                        listener.aoSelecionarData(dataFormatada, dataSelecionada);
                    }
                },
                ano,
                mes,
                dia
        );

        datePickerDialog.show();
    }
}