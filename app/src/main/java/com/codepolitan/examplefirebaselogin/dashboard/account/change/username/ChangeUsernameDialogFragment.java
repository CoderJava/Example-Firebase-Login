package com.codepolitan.examplefirebaselogin.dashboard.account.change.username;

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

public class ChangeUsernameDialogFragment extends DialogFragment {

    private View view;
    @BindView(R.id.edit_text_email_old_dialog_fragment_change_email)
    EditText editTextEmail;
    @BindView(R.id.edit_text_password_dialog_fragment_change_email)
    EditText editTextPassword;
    @BindView(R.id.edit_text_email_new_dialog_fragment_change_email)
    EditText editTextEmailNew;
    @BindView(R.id.progress_bar_dialog_fragment_change_email)
    ProgressBar progressBar;
    @BindView(R.id.button_save_email_dialog_fragment_new_email)
    Button buttonSaveEmail;

    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_change_email, container, false);
        getDialog().setTitle("Change Email");
        ButterKnife.bind(this, view);
        initFirebaseAuth();
        return view;
    }

    private void initFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick({R.id.button_save_email_dialog_fragment_new_email})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save_email_dialog_fragment_new_email:
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                final String emailNew = editTextEmailNew.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    showMessageSnackBar("email is empty!");
                } else if (TextUtils.isEmpty(password)) {
                    showMessageSnackBar("password is empty!");
                } else if (TextUtils.isEmpty(emailNew)) {
                    showMessageSnackBar("Please insert your email address!");
                } else {
                    showProgress();
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        firebaseAuth.getCurrentUser()
                                                .updateEmail(emailNew)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        hideProgress();
                                                        if (task.isSuccessful()) {
                                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                                                            alertDialogBuilder.setTitle("Change Email");
                                                            alertDialogBuilder.setMessage("Your email address has been updated! Please login again.");
                                                            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    firebaseAuth.signOut();
                                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                    getDialog().dismiss();
                                                                    dialogInterface.dismiss();
                                                                }
                                                            });
                                                            alertDialogBuilder.show();
                                                        } else {
                                                            getDialog().dismiss();
                                                            showMessageSnackBar("Your email address fail to updated!");
                                                        }
                                                    }
                                                });
                                    } else {
                                        showMessageSnackBar("Your email or password not matched!");
                                    }
                                }
                            });
                }
        }
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        buttonSaveEmail.setVisibility(View.VISIBLE);
        editTextEmail.setEnabled(true);
        editTextPassword.setEnabled(true);
        editTextEmailNew.setEnabled(true);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        buttonSaveEmail.setVisibility(View.GONE);
        editTextEmail.setEnabled(false);
        editTextPassword.setEnabled(false);
        editTextEmailNew.setEnabled(false);
    }

    private void showMessageSnackBar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .show();
    }
}
