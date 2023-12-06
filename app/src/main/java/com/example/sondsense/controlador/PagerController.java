package com.example.sondsense.controlador;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerController extends FragmentPagerAdapter {
    int numTabs;

    public PagerController(@NonNull FragmentManager fm, int behaviors) {
        super(fm);
        this.numTabs = behaviors;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Sensibilidad();
            case 1:
                return new Metricas();
            case 2:
                return new Alertas();
            case 3:
                return new DispositivosVinculados();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
