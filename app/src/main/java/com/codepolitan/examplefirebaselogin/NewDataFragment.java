package com.codepolitan.examplefirebaselogin;


import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.codepolitan.examplefirebaselogin.model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewDataFragment extends Fragment {

    private View view;
    @BindView(R.id.coordinator_layout_fragment_new_data)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.edit_text_title_fragment_new_data)
    EditText editTextTitle;
    @BindView(R.id.edit_text_content_fragment_new_data)
    EditText editTextContent;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;

    public NewDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_data, container, false);
        ButterKnife.bind(this, view);
        initFirebase();
        initAuthStateListener();
        return view;
    }

    private void initAuthStateListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    //  user still sign in
                } else {
                    //  user session sign in has been timeout
                    Snackbar.make(coordinatorLayout, "Please sign in again!", Snackbar.LENGTH_INDEFINITE)
                            .show();
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @OnClick({R.id.fab_save_fragment_new_data})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_save_fragment_new_data:
                //  save data
                String username = firebaseAuth.getCurrentUser().getEmail();
                String title = editTextTitle.getText().toString().trim();
                String content = editTextContent.getText().toString().trim();
                String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                if (TextUtils.isEmpty(title)) {
                    Snackbar.make(coordinatorLayout, "Title is empty!", Snackbar.LENGTH_LONG)
                            .show();
                } else if (TextUtils.isEmpty(content)) {
                    Snackbar.make(coordinatorLayout, "Content is empty!", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    Data data = new Data(username, title, content, datetime);
                    databaseReference
                            .child("member")
                            .child(username.replaceAll("\\.", "_"))
                            .child("data")
                            .push()
                            .setValue(data);
                    clearFields();
                    final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Data has been saved!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                            getFragmentManager().popBackStack();
                        }
                    });
                    snackbar.show();
                }
                break;
        }
    }

    private void clearFields() {
        editTextTitle.setText("");
        editTextContent.setText("");
    }

}
