package com.amycohen.lab39masterdetailflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class ErrandAdapter extends RecyclerView.Adapter<ErrandAdapter.MyViewHolder> {
    public List<Errand> errands;
    private boolean isTwoPane;
    private FragmentManager fragmentManager;

    public ErrandAdapter (boolean isTwoPane, FragmentManager fragmentManager) {
        errands = new ArrayList<>();
        this.isTwoPane = isTwoPane;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.errand_item, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.bind(errands.get(i));
    }

    @Override
    public int getItemCount() {
        return errands.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        TextView description;
        CheckBox checkbox;
        Errand errand;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mView.setOnClickListener(this);

            description = itemView.findViewById(R.id.description);
            checkbox = itemView.findViewById(R.id.isComplete);
        }

        public void bind (Errand errand) {
            this.errand = errand;
            description.setText(errand.description);
            checkbox.setChecked(errand.isComplete);
        }

        @Override
        public void onClick(View v) {

            if (isTwoPane) {
                Fragment fragment = new MapsFragment();

                Bundle arguments = new Bundle();
                arguments.putString("key", errand.id);
                fragment.setArguments(arguments);

                fragmentManager.beginTransaction()
                        .replace(R.id.detail_fragment_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(mView.getContext(), MapsActivity.class);
                intent.putExtra("key", errand.id);
                mView.getContext().startActivity(intent);
            }
        }
    }
}

