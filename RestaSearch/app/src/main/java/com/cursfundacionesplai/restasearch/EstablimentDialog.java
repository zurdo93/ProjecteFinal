package com.cursfundacionesplai.restasearch;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class EstablimentDialog extends DialogFragment {

    public static final String TAG = "establiment_dialog";

    private Toolbar toolbar;

    private String placeId;

    public EstablimentDialog(String placeId) {
        this.placeId = placeId;
    }

    public static EstablimentDialog display(FragmentManager fragmentManager, String placeId) {
        EstablimentDialog dialog = new EstablimentDialog(placeId);
        dialog.show(fragmentManager, TAG);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        // si el dialeg no es null canviar la mida per que ocupi tota la pantalla
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);

            // aplicar animacions quan s'obre i es tancar el dialeg
            dialog.getWindow().setWindowAnimations(R.style.Theme_RestaSearch_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.example_dialog, container, false);

        toolbar = view.findViewById(R.id.toolbar);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(placeId);
        /*toolbar.inflateMenu(R.menu.example_dialog);
        toolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });*/
    }
}
