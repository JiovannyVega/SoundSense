package com.example.sondsense.controlador;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private MainActivity.ConnectedThread MyConexionBT;

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
        Intent intent = requireActivity().getIntent();
        if (intent != null) {
            MyConexionBT = (MainActivity.ConnectedThread) intent.getSerializableExtra("conexion_bt");
        }
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
        cargarPreferencias();
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                texto.setText("Sensibididad actual: " + (progress * 10) + "%");
                editor.putInt("Sensibilidad", progress);
                editor.apply();
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
                editor.putString("Modo" ,modo.getText().toString());
                editor.apply();
                if(MyConexionBT != null) {
                    MyConexionBT.write('a');
                }
            }
        });

        btnExterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modo.setText("Modo: Exterior");
                editor.putString("Modo" ,modo.getText().toString());
                editor.apply();
                if(MyConexionBT != null) {
                    MyConexionBT.write('b');
                }
            }
        });
        btnPersonalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modo.setText("Modo: Personalizado");
                editor.putString("Modo" ,modo.getText().toString());
                editor.apply();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString() + " must be MainActivity");
        }
    }

    private void cargarPreferencias() {
        preferences = getActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //Seek Bar
        texto.setText("Sensibididad actual: " + (preferences.getInt("Sensibilidad", 5) * 10) + "%");
        seek.setProgress(preferences.getInt("Sensibilidad", 5));
        //Text View
        modo.setText(preferences.getString("Modo", "Modo: Personalizado"));
    }

}