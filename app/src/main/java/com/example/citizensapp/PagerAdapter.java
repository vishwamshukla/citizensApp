package com.example.citizensapp;
import android.content.Context;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.example.citizensapp.Fragments.EasyModeFragment;
import com.example.citizensapp.Fragments.OverviewFragment;

import java.util.ArrayList;


public class PagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    private int tabCount;
    private boolean inCar;
    //Constructor to the class
    public PagerAdapter(FragmentManager fm, int tabCount, boolean inCar) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
        this.inCar = inCar;
        // Log.i(getClass().getSimpleName(), this.inCar + "");
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putBoolean("inCar", inCar);
                EasyModeFragment tab1 = new EasyModeFragment();
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                OverviewFragment tab2 = new OverviewFragment();
                return tab2;
            default:
                return null;
        }
    }

    //Overriden method getHits to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
