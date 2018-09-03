package com.amycohen.lab39masterdetailflow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErrandListActivity extends AppCompatActivity {

    @BindView(R.id.errands) RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ErrandAdapter errandAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        Fragment fragment = new ErrandListFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.single_fragment_container, fragment)
                .commit();

//        ButterKnife.bind(this);
//
//        DatabaseReference errands = FirebaseDatabase.getInstance().getReference("errands");
//        errands.addValueEventListener(this);
//
//        linearLayoutManager = new LinearLayoutManager(this);
//        errandAdapter = new ErrandAdapter();
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(errandAdapter);
    }

//    @Override
//    public void onDataChange(DataSnapshot dataSnapshot) {
//        List<Errand> errands = new ArrayList<>();
//
//        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//            errands.add(Errand.fromSnapshot(snapshot));
//        }
//        errandAdapter.errands = errands;
//        errandAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onCancelled(DatabaseError databaseError) {
//
//    }
}
