package com.ricardo.controlasistenciaipd.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ricardo.controlasistenciaipd.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlumnoFragment extends Fragment {

    String recuperado = "", evento =  "", nomEvento = "";
    public AlumnoFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AlumnoFragment(Bundle recupera) {
        // Required empty public constructor
        final Bundle r = recupera;
        if(r!=null){
            recuperado = recupera.getString("cod");
            evento = recupera.getString("eve");
            nomEvento = recupera.getString("nomEve");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alumno, container, false);
    }

}
