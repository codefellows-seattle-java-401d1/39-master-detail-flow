package com.gbbeard.masterflow;

import android.support.v4.app.Fragment;

public class ErrandListActivity extends SingleFragmentActivity {

    @Override
    Fragment getFragment() {
        return new ErrandListFragment();
    }
}