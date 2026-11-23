
package com.raposo.gterandroid.util;

import android.text.method.KeyListener;
import android.view.View;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;
import android.widget.Spinner;

import java.util.Map;
import java.util.WeakHashMap;

public class CampoEditavel {

    private final Map<TextInputEditText, KeyListener> keyListenersOriginais = new WeakHashMap<>();

    public void registrarKeyListener(TextInputEditText editText) {
        if (editText != null && editText.getKeyListener() != null) {
            keyListenersOriginais.put(editText, editText.getKeyListener());
        }
    }

    public void registrarKeyListeners(TextInputEditText... campos) {
        if (campos == null) return;
        for (TextInputEditText campo : campos) {
            registrarKeyListener(campo);
        }
    }

    public void setCampoEditavel(TextInputEditText editText, boolean editavel) {
        if (editText == null) return;

        editText.setFocusable(editavel);
        editText.setFocusableInTouchMode(editavel);
        editText.setClickable(editavel);
        editText.setLongClickable(editavel);
        editText.setCursorVisible(editavel);

        if (editavel) {
            KeyListener listenerOriginal = keyListenersOriginais.get(editText);
            if (listenerOriginal != null) {
                editText.setKeyListener(listenerOriginal);
            }
        } else {
            editText.setKeyListener(null);
        }
    }

    public void setRadioGroupEditavel(RadioGroup radioGroup, boolean editavel) {
        if (radioGroup == null) return;

        radioGroup.setEnabled(editavel);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View child = radioGroup.getChildAt(i);
            child.setEnabled(editavel);
            child.setClickable(editavel);
            child.setFocusable(editavel);
            child.setOnTouchListener(editavel ? null : (v, event) -> true);
        }
    }

    public void setSpinnerEditavel(Spinner spinner, boolean editavel) {
        if (spinner == null) return;

        spinner.setEnabled(editavel);
        spinner.setClickable(editavel);
        spinner.setFocusable(editavel);
        spinner.setFocusableInTouchMode(false); // Spinner normalmente não usa foco de digitação
        spinner.setOnTouchListener(editavel ? null : (v, event) -> true);
    }
}