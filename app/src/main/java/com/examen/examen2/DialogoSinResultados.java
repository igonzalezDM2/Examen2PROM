package com.examen.examen2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoSinResultados extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialogo_sin_resultados, null));

        Dialog dialogo =  builder.setPositiveButton(getString(R.string.vale), (dialog, which) -> {
            dialog.cancel();
        }).create();
        dialogo.setCancelable(false);
        dialogo.setCanceledOnTouchOutside(false);
        return dialogo;
    }
}
