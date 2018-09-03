package com.amycohen.lab39masterdetailflow;

import android.support.v4.app.Fragment;

public class MapsActivity extends SingleFragmentActivity  {

    @Override
    Fragment getFragment() {
        return new MapsFragment();
    }
}