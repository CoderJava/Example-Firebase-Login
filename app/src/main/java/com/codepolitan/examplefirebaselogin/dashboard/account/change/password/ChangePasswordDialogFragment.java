package com.codepolitan.examplefirebaselogin.dashboard.account.change.password;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.codepolitan.examplefirebaselogin.R;
import com.codepolitan.examplefirebaselogin.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 04/03/17.
 */

public class ChangePasswordDialogFragment extends DialogFragment {

    private View view;
    @BindView(R.id.edit_text_email_old_dialog_fragment_change_password)
    EditText editTextEmail;
    @BindView(R.id.edit_text_password_dialog_fragment_change_password)
    EditText editTextPassword;
    @BindView(R.id.edit_text_password_new_dialog_fragment_change_password)
    EditText editTextPasswordNew;
    @BindView(R.id.progress_bar_dialog_fragment_change_password)
    ProgressBar progressBar;
    @BindView(R.id.button_save_password_dialog_fragment_change_password)
    Button buttonSave;

    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_change_password, container, false);
        getDialog().setTitle("Change Password");
        ButterKnife.bind(this, view);
        initFirebaseAuth();
        return view;
    }

    private void initFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick({R.id.button_save_password_dialog_fragment_change_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save_password_dialog_fragment_change_password:
                //  do something
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                final String passwordNew = editTextPasswordNew.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    showMessageSnackbar("Email is empty!");
                } else if (TextUtils.isEmpty(password)) {
                    showMessageSnackbar("Password is empty!");
                } else if (TextUtils.isEmpty(passwordNew)) {
                    showMessageSnackbar("Please insert your password new!");
                } else {
                    showProgress();
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        firebaseAuth.getCurrentUser()
                                                .updatePassword(passwordNew)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        hideProgress();
                                                        if (task.isSuccessful()) {
                                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                                                            alertDialogBuilder.setTitle("Change Password");
                                                            alertDialogBuilder.setMessage("Your password has been changed! Please login again.");
                                                            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.dismiss();
                                                                    firebaseAuth.signOut();
                                                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                    getDialog().dismiss();
                                                                }
                                                            });
                                                            alertDialogBuilder.show();
                                                        } else {
                                                            showMessageSnackbar("Your password fail to changed!");
                                                            getDialog().dismiss();
                                                        }
                                                    }
                                                });
                                    } else {
                                        showMessageSnackbar("Your email and password is not matched!");
                                        getDialog().dismiss();
                                    }
                                }
                            });
                }
        }
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        buttonSave.setVisibility(View.GONE);
        editTextEmail.setEnabled(false);
        editTextPassword.setEnabled(false);
        editTextPasswordNew.setEnabled(false);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        buttonSave.setVisibility(View.VISIBLE);
        editTextEmail.setEnabled(true);
        editTextPassword.setEnabled(true);
        editTextPasswordNew.setEnabled(true);
    }

    private void showMessageSnackbar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .show();
    }
}
