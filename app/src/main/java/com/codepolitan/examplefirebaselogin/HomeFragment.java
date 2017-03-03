package com.codepolitan.examplefirebaselogin;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepolitan.examplefirebaselogin.adapter.AdapterData;
import com.codepolitan.examplefirebaselogin.model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragmentTAG";
    private View view;
    @BindView(R.id.recycler_view_fragment_home)
    RecyclerView recyclerViewData;
    @BindView(R.id.progress_bar_fragment_home)
    ProgressBar progressBar;
    @BindView(R.id.fab_new_data_fragment_home)
    FloatingActionButton floatingActionButtonNewData;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private List<Data> listData;
    private AdapterData adapterData;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        initFirebase();
        loadDataFirebase();
        return view;
    }

    @OnClick({R.id.fab_new_data_fragment_home})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_new_data_fragment_home:
                floatingActionButtonNewData.hide();
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout_content_dashboard, new NewDataFragment(), NewDataFragment.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        floatingActionButtonNewData.show();
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        recyclerViewData.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewData.addItemDecoration(dividerItemDecoration);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void loadDataFirebase() {
        showProgress();
        listData = new ArrayList<>();
        firebaseDatabase.getReference()
                .child("member")
                .child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_"))
                .child("data")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        hideProgress();
                        for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                            Data data = dataItem.getValue(Data.class);
                            listData.add(data);
                        }
                        adapterData = new AdapterData(listData);
                        recyclerViewData.setAdapter(adapterData);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        hideProgress();
                    }
                });
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }
    
}
