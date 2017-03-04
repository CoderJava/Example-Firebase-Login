package com.codepolitan.examplefirebaselogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.relative_layout_progress_activity_main)
    RelativeLayout relativeLayoutProgress;
    @BindView(R.id.edit_text_username_activity_main)
    EditText editTextUsername;
    @BindView(R.id.edit_text_password_activity_main)
    EditText editTextPassword;

    private FirebaseAuth firebaseAuth;
    private boolean loggedIn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFirebase();
        ButterKnife.bind(this);
        loggedIn = isLoggedIn();
        if (loggedIn) {
            //  go to dashboard
            goToDashboard();
        }
    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void goToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick({R.id.button_login_activity_main, R.id.button_sign_up_activity_main})
    public void onClick(Button button) {
        switch (button.getId()) {
            case R.id.button_login_activity_main:
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                login(username, password);
                break;
            case R.id.button_sign_up_activity_main:
                //  go to form pendaftaran
                startActivity(new Intent(this, SignupActivity.class));
                break;
        }
    }

    private void login(final String username, final String password) {
        if (TextUtils.isEmpty(username)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.error_message_username_empty, Snackbar.LENGTH_LONG)
                    .show();
        } else if (TextUtils.isEmpty(password)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.error_message_password_empty, Snackbar.LENGTH_LONG)
                    .show();
        } else {
            //  do login
            showProgress();
            firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideProgress();
                            if (task.isSuccessful()) {
                                //  login sucess
                                //  go to dashboard
                                goToDashboard();
                            } else {
                                //  login failed
                                showMessageBox("Login failed. Your username and password is not matched");
                            }
                        }
                    });

        }
    }

    private void hideProgress() {
        relativeLayoutProgress.setVisibility(View.GONE);
        editTextUsername.setEnabled(true);
        editTextPassword.setEnabled(true);
    }

    private void showProgress() {
        relativeLayoutProgress.setVisibility(View.VISIBLE);
        editTextUsername.setEnabled(false);
        editTextPassword.setEnabled(false);
    }

    private void showMessageBox(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Login");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    public boolean isLoggedIn() {
        if (firebaseAuth.getCurrentUser() != null) {
            //  user logged in
            return true;
        } else {
            return false;
        }
    }
}
