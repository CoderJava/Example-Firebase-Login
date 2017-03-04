package com.codepolitan.examplefirebaselogin.dashboard.home;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepolitan.examplefirebaselogin.dashboard.data.edit.EditDataDialogFragment;
import com.codepolitan.examplefirebaselogin.dashboard.data.add.NewDataFragment;
import com.codepolitan.examplefirebaselogin.R;
import com.codepolitan.examplefirebaselogin.adapter.AdapterData;
import com.codepolitan.examplefirebaselogin.model.Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<String> listKey;
    private AdapterData adapterData;
    private Paint paint = new Paint();
    private int editPosition;

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
        initFirebaseListener();
        initSwipe();
        return view;
    }

    private void initFirebaseListener() {
        final List<Data> listDataNew = new ArrayList<>();
        firebaseDatabase.getReference()
                .child("member")
                .child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_"))
                .child("data")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildAdded");
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        //  do something
                        Log.d(TAG, "onChildChanged");
                        Map<String, String> mapDataChanged = new HashMap<>();
                        for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                            mapDataChanged.put(dataItem.getKey(), dataItem.getValue().toString());
                        }
                        adapterData.refreshItem(mapDataChanged, editPosition);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onChildRemoved");
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildMoved");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled");
                    }
                });
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                String key = listKey.get(position);

                if (direction == ItemTouchHelper.LEFT) {
                    //  delete data
                    firebaseDatabase.getReference()
                            .child("member")
                            .child(firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_"))
                            .child("data")
                            .child(key)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        adapterData.removeItem(position);
                                        showSnackbarMsg("Data has been deleted!");
                                    } else {
                                        showSnackbarMsg("Data fail to deleted!");
                                    }
                                }
                            });
                } else {
                    //  update data
                    editPosition = position;
                    String username = firebaseAuth.getCurrentUser().getEmail().replaceAll("\\.", "_");
                    Data data = listData.get(position);
                    Map<String, String> mapData = new HashMap<>();
                    mapData.put("title", data.getTitle());
                    mapData.put("content", data.getContent());
                    mapData.put("key", key);
                    mapData.put("username", username);
                    EditDataDialogFragment editDataDialogFragment = new EditDataDialogFragment();
                    EventBus.getDefault().postSticky(mapData);
                    editDataDialogFragment.show(getFragmentManager(), null);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        //  effect for edit
                        paint.setColor(getResources().getColor(R.color.colorEdit));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF iconDest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, iconDest, paint);
                    } else {
                        //  effect for delete
                        paint.setColor(getResources().getColor(R.color.colorDelete));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_forever_white);
                        RectF iconDest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, iconDest, paint);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewData);
    }

    private void showSnackbarMsg(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .show();
    }

    private void removeView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
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
        listKey = new ArrayList<>();
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
                            listKey.add(dataItem.getKey());
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
