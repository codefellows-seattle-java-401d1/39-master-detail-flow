package com.amycohen.lab39masterdetailflow;

import android.support.v4.app.Fragment;

public class ErrandListActivity extends SingleFragmentActivity {

    @Override
    Fragment getFragment() {
        return new ErrandListFragment();
    }
}
