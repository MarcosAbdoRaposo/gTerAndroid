package com.raposo.gterandroid.components;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;

public class Mascara {
    public static void cpf(TextInputEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                isUpdating = true;

                String str = s.toString().replaceAll("\\D", "");
                if (str.length() > 11) str = str.substring(0, 11);

                String mascara = "";
                for (int i = 0; i < str.length(); i++) {
                    if (i == 3 || i == 6) mascara += ".";
                    else if (i == 9) mascara += "-";
                    mascara += str.charAt(i);
                }

                s.replace(0, s.length(), mascara);
                isUpdating = false;
            }
        });
    }


    public static void data(TextInputEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não utilizado
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Não utilizado
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) {
                    return;
                }

                isUpdating = true;

                // Remove tudo o que não for número
                String str = s.toString().replaceAll("\\D", "");

                // Limita a 8 dígitos (DDMMAAAA)
                if (str.length() > 8) {
                    str = str.substring(0, 8);
                }

                // Aplica a máscara DD/MM/AAAA
                String mascara = "";
                for (int i = 0; i < str.length(); i++) {
                    if (i == 2 || i == 4) {
                        mascara += "/";
                    }
                    mascara += str.charAt(i);
                }

                s.replace(0, s.length(), mascara);
                isUpdating = false;
            }
        });
    }
}
