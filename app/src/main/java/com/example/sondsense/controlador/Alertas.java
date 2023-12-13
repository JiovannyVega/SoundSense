package com.example.sondsense.controlador;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.sondsense.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Alertas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Alertas extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private Switch switchBatteryLow, switchNoise, switchRestrictedZone, switchTemperatureChange, switchNearbyEvents, switchIntenseActivity, switchExcessiveSunlight;

    public Alertas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Alertas.
     */
    // TODO: Rename and change types and number of parameters
    public static Alertas newInstance(String param1, String param2) {
        Alertas fragment = new Alertas();
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
        View view = inflater.inflate(R.layout.fragment_alertas, container, false);
        ActivityCompat.requestPermissions(requireActivity(), new String[]{
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN
        }, 1234);

        // Encuentra los switches por sus IDs en el layout del fragmento
        switchBatteryLow = view.findViewById(R.id.switchBatteryLow);
        switchNoise = view.findViewById(R.id.switchNoise);
        switchRestrictedZone = view.findViewById(R.id.switchRestrictedZone);
        switchTemperatureChange = view.findViewById(R.id.switchTemperatureChange);
        switchNearbyEvents = view.findViewById(R.id.switchNearbyEvents);
        switchIntenseActivity = view.findViewById(R.id.switchIntenseActivity);
        switchExcessiveSunlight = view.findViewById(R.id.switchExcessiveSunlight);

        cargarPreferencias();

        switchBatteryLow.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("BatteryLow", isChecked));
        switchNoise.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("Noise", isChecked));
        switchRestrictedZone.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("RestrictedZone", isChecked));
        switchTemperatureChange.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("TemperatureChange", isChecked));
        switchNearbyEvents.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("NearbyEvents", isChecked));
        switchIntenseActivity.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("IntenseActivity", isChecked));
        switchExcessiveSunlight.setOnCheckedChangeListener((buttonView, isChecked) -> savePreference("ExcessiveSunlight", isChecked));

        return view;
    }

    private void cargarPreferencias() {
        preferences = getActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor = preferences.edit();

        switchBatteryLow.setChecked(preferences.getBoolean("BatteryLow", false));
        switchNoise.setChecked(preferences.getBoolean("Noise", false));
        switchRestrictedZone.setChecked(preferences.getBoolean("RestrictedZone", false));
        switchTemperatureChange.setChecked(preferences.getBoolean("TemperatureChange", false));
        switchNearbyEvents.setChecked(preferences.getBoolean("NearbyEvents", false));
        switchIntenseActivity.setChecked(preferences.getBoolean("IntenseActivity", false));
        switchExcessiveSunlight.setChecked(preferences.getBoolean("ExcessiveSunlight", false));
    }

    private void savePreference(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }
}