package assignment.codefellows.master_detail_map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskListFragment extends Fragment implements ValueEventListener{
    @BindView(R.id.tasks)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    TaskAdapter taskAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_task_list,container,false);
        ButterKnife.bind(this,root);
        DatabaseReference tasks = FirebaseDatabase.getInstance().getReference("tasks");
        tasks.addValueEventListener(this);

        boolean isTwoPane = false;
        if(root.findViewById(R.id.detail_fragment_container)!=null){
            isTwoPane = true;

            Fragment fragment = new MapsFragment();
            FragmentManager fm = getChildFragmentManager();
            fm.beginTransaction()
                    .add(R.id.detail_fragment_container,fragment)
                    .commit();
        }

        linearLayoutManager = new LinearLayoutManager(getContext());
        taskAdapter = new TaskAdapter(getChildFragmentManager(), isTwoPane);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(taskAdapter);

        return root;
    }
    @Override
    public void onDataChange(DataSnapshot ds){
        List<Task> tasks = new ArrayList<>();
        for(DataSnapshot snapshot : ds.getChildren()){
            tasks.add(Task.fromSnapshot(snapshot));
        }
        taskAdapter.tasks = tasks;
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError dbError){}

}