package com.example.sondsense.controlador;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sondsense.MainActivity;
import com.example.sondsense.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sensibilidad#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sensibilidad extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView texto;
    private TextView modo;
    private SeekBar seek;
    private MainActivity.ConnectedThread myConexionBT;

    public Sensibilidad() {
        // Required empty public constructor
    }

    public static Sensibilidad newInstance(String param1, String param2) {
        Sensibilidad fragment = new Sensibilidad();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sensibilidad, container, false);

        texto = view.findViewById(R.id.Modo);
        seek = view.findViewById(R.id.seekBar);

        modo = view.findViewById(R.id.Modalidad);
        Button btnInterior = view.findViewById(R.id.Int);
        Button btnExterior = view.findViewById(R.id.Ext);
        Button btnPersonalizado = view.findViewById(R.id.Pers);
        MainActivity main = new MainActivity();
        main.getMyConexionBT();

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                texto.setText("Sensibididad actual: " + (progress * 10) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnInterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modo.setText("Modo: Interior");

                // Obtener la actividad que contiene este fragmento
                MainActivity mainActivity = (MainActivity) getActivity();

                if (mainActivity != null) {
                    // Obtener la conexión Bluetooth desde MainActivity
                    MainActivity.ConnectedThread myConexionBT = mainActivity.getMyConexionBT();

                    if (myConexionBT != null) {
                        // Enviar datos a través de la conexión Bluetooth
                        myConexionBT.write("A");
                    } else {
                        // Manejar el caso cuando myConexionBT es nulo
                        Toast.makeText(getActivity(), "Conexión Bluetooth no disponible", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Manejar el caso cuando MainActivity es nulo
                    Toast.makeText(getActivity(), "MainActivity no disponible", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnExterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modo.setText("Modo: Exterior");
            }
        });
        btnPersonalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modo.setText("Modo: Personalizado");
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            myConexionBT = mainActivity.getMyConexionBT();
            // Ahora puedes usar myConexionBT aquí en Sensibilidad
        } else {
            throw new RuntimeException(context.toString() + " must be MainActivity");
        }
    }

}