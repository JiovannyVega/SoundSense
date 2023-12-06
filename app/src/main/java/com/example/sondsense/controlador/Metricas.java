package com.example.sondsense.controlador;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;


import com.example.sondsense.R;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Metricas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Metricas extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BarChart barChart;
    private RadarChart radarChart;
    public Metricas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Metricas.
     */
    // TODO: Rename and change types and number of parameters
    public static Metricas newInstance(String param1, String param2) {
        Metricas fragment = new Metricas();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_metricas, container, false);

        barChart = view.findViewById(R.id.barChart);
        radarChart = view.findViewById(R.id.radarChart);

        setupBarChart();
        setupRadarChart();

        return view;
    }
    private void setupBarChart() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 10));
        barEntries.add(new BarEntry(1, 15));
        barEntries.add(new BarEntry(2, 18));
        barEntries.add(new BarEntry(3, 8));
        barEntries.add(new BarEntry(4, 14));
        barEntries.add(new BarEntry(5, 5));
        barEntries.add(new BarEntry(6, 3));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Alertas activadas");

        BarData barData = new BarData(barDataSet);

        // Etiquetas para cada barra
        String[] barLabels = new String[]{"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};

        // Configuración de las etiquetas en el eje X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Posición de las etiquetas
        xAxis.setGranularity(1f); // Espaciado entre etiquetas
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(barLabels));

        barChart.setData(barData);
        barChart.getDescription().setEnabled(false); // Deshabilita la descripción
        barChart.setDrawGridBackground(false); // Deshabilita el fondo de cuadrícula
        barChart.animateY(1000); // Animación de barras
        barChart.invalidate(); // Actualiza el gráfico
    }
    private void setupRadarChart() {
        // Datos de ejemplo para el gráfico de radar
        ArrayList<RadarEntry> radarEntries1 = new ArrayList<>();
        radarEntries1.add(new RadarEntry(20f));
        radarEntries1.add(new RadarEntry(40f));
        radarEntries1.add(new RadarEntry(30f));
        radarEntries1.add(new RadarEntry(50f));

        ArrayList<RadarEntry> radarEntries2 = new ArrayList<>();
        radarEntries2.add(new RadarEntry(40f));
        radarEntries2.add(new RadarEntry(30f));
        radarEntries2.add(new RadarEntry(50f));
        radarEntries2.add(new RadarEntry(60f));

        RadarDataSet radarDataSet1 = new RadarDataSet(radarEntries1, "Vibraciones");
        RadarDataSet radarDataSet2 = new RadarDataSet(radarEntries2, "Alertas");

        // Configuración de colores y otros atributos para cada conjunto de datos
        radarDataSet1.setColor(Color.rgb(103, 110, 129));
        radarDataSet1.setFillColor(Color.rgb(103, 110, 129));
        radarDataSet1.setDrawFilled(true);

        radarDataSet2.setColor(Color.rgb(121, 162, 175));
        radarDataSet2.setFillColor(Color.rgb(121, 162, 175));
        radarDataSet2.setDrawFilled(true);

        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSet1);
        radarData.addDataSet(radarDataSet2);

        // Configuración del gráfico de radar
        radarChart.setData(radarData);

        // Configuración del eje X del gráfico de radar
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Label 1");
        labels.add("Label 2");
        labels.add("Label 3");
        labels.add("Label 4");

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        // Configuración de la leyenda del gráfico de radar
        Legend legend = radarChart.getLegend();
        legend.setEnabled(true);

        radarChart.getDescription().setEnabled(false);
        radarChart.invalidate(); // Actualiza el gráfico
    }
}