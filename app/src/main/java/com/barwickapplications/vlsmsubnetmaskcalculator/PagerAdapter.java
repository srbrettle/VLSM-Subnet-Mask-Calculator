package com.barwickapplications.vlsmsubnetmaskcalculator;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                EquivalentValues equivalentValues = new EquivalentValues();
                return equivalentValues;
            case 1:
                SubnetCalculator subnetCalculator = new SubnetCalculator();
                return subnetCalculator;
            case 2:
                VlsmAllocation vlsmAllocation = new VlsmAllocation();
                return vlsmAllocation;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
