package assignment.codefellows.master_detail_map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>{
    public List<Task> tasks;
    private boolean isTwoPane;
    private TaskListFragment parentActivity;
    private FragmentManager fm;

    public TaskAdapter(FragmentManager fm, boolean twoPane){
        this.fm = fm;
        this.isTwoPane = twoPane;
        tasks = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.task_item,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i){
        viewHolder.bind(tasks.get(i));
    }
    @Override
    public int getItemCount(){
        return tasks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        View mView;
        TextView desc;
        CheckBox checkbox;

        Task task;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setOnClickListener(this);

            desc = itemView.findViewById(R.id.description);
            checkbox = itemView.findViewById(R.id.isComplete);
        }

        public void bind(Task task){
            this.task = task;
            desc.setText(task.description);
            checkbox.setChecked(task.isDone);
        }

        @Override
        public void onClick(View view) {
            if(isTwoPane){
                Bundle arguments = new Bundle();
                arguments.putString("id",task.id);

                MapsFragment fragment = new MapsFragment();
                fragment.setArguments(arguments);

                parentActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container,fragment)
                        .commit();
            }else {
                Intent intent = new Intent(mView.getContext(), MapsActivity.class);
                intent.putExtra("id", task.id);
                mView.getContext().startActivity(intent);
            }
        }
    }
}
