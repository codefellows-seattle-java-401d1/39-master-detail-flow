package assignment.codefellows.master_detail_map;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MapsActivity extends SingleFragmentActivity {
    public Fragment getFragment(){
        return new MapsFragment();
    }
}
