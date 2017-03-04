package com.codepolitan.examplefirebaselogin.dashboard.data.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.codepolitan.examplefirebaselogin.R;
import com.codepolitan.examplefirebaselogin.model.Data;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 04/03/17.
 */

public class EditDataDialogFragment extends DialogFragment {

    private static final String TAG = "EditDataDialog";
    private View view;
    @BindView(R.id.edit_text_title_layout_dialog_edit)
    EditText editTextTitle;
    @BindView(R.id.edit_text_content_layout_dialog_edit)
    EditText editTextContent;

    private String title;
    private String content;
    private String key;
    private String username;
    private FirebaseDatabase firebaseDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_edit_data, container, false);
        getDialog().setTitle("Edit Data");
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initFirebaseDatabase();
        editTextTitle.setText(title);
        editTextContent.setText(content);
        return view;
    }

    private void initFirebaseDatabase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Subscribe(sticky = true)
    public void onMessageEvent(Map<String, String> mapData) {
        title = mapData.get("title");
        content = mapData.get("content");
        key = mapData.get("key");
        username = mapData.get("username");
    }

    @OnClick({R.id.button_save_change_layout_dialog_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save_change_layout_dialog_edit:
                String title = editTextTitle.getText().toString().trim();
                String content = editTextContent.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Title is empty!", Snackbar.LENGTH_LONG)
                            .show();
                } else if (TextUtils.isEmpty(content)) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Content is empty!", Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    Data data = new Data(username, title, content, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    firebaseDatabase.getReference()
                            .child("member")
                            .child(username)
                            .child("data")
                            .child(key)
                            .setValue(data);
                    getDialog().dismiss();
                }
        }
    }

}
