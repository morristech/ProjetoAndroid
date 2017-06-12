package com.example.amand.projetointegrador.helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import com.example.amand.projetointegrador.R;

/**
 * Created by amanda on 11/06/17.
 */

public class AlertDialogFragment extends DialogFragment {

    private Spinner tipo;
    private Spinner porte;
    private Spinner sexo;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filtros, null);

        tipo = (Spinner) view.findViewById(R.id.spinnertipo);
        porte = (Spinner) view.findViewById(R.id.spinnerporte);
        sexo = (Spinner) view.findViewById(R.id.spinnersexo);

        String tipoStr = tipo.getSelectedItem().toString();
        String porteStr = porte.getSelectedItem().toString();
        String sexoStr = sexo.getSelectedItem().toString();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        return builder.create();
    }
}
