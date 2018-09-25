package assignment.codefellows.master_detail_map;

import android.support.v4.app.Fragment;

public class TaskListActivity extends SingleFragmentActivity{
    public Fragment getFragment() {
        return new TaskListFragment();
    }
}
