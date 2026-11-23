package com.raposo.gterandroid.components;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Data {

    private static final DateTimeFormatter FORMATO_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static String dateToIso(Date data) {
        if (data == null) {
            return null;
        }
        String dataIso = data.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
        return dataIso;
    }

    public static Date isoToDate(String dataIso) {
        if (dataIso == null) {
            return null;
        }
        LocalDate localDate = LocalDate.parse(
                dataIso,
                DateTimeFormatter.ISO_LOCAL_DATE
        );

        return Date.from(
                localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
    }

    public static String dateToBr(Date data) {
        if (data == null) {
            return null;
        }
        return data.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(FORMATO_BR);
    }

    public static Date brToDate(String dataBr) {
        if (dataBr == null) {
            return null;
        }
        LocalDate localDate = LocalDate.parse(
                dataBr.trim(),
                FORMATO_BR
        );

        return Date.from(
                localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
    }

}
