package com.gbbeard.masterflow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErrandListFragment extends Fragment implements ValueEventListener {

    @BindView(R.id.errands)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ErrandAdapter errandAdapter;

    private boolean twoPane;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_errand_list, container, false);

        ButterKnife.bind(this, view);

        DatabaseReference errands = FirebaseDatabase.getInstance().getReference("errand");
        errands.addValueEventListener(this);

        twoPane = false;
        if (view.findViewById(R.id.detail_fragment_contains) != null) {
            twoPane = true;

            Fragment fragment = new TaskMapFragment();
            FragmentManager fm = getChildFragmentManager();
            fm.beginTransaction()
                    .add(R.id.detail_fragment_contains, fragment)
                    .commit();
        }

        linearLayoutManager = new LinearLayoutManager(getActivity());
        errandAdapter = new ErrandAdapter(twoPane, getChildFragmentManager());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(errandAdapter);

        return view;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Errand> errands = new ArrayList<>();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            errands.add(Errand.fromSnapshot(snapshot));
        }
        errandAdapter.errand = errands;
        errandAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}