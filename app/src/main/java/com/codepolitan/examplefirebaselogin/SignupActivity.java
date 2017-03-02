package com.codepolitan.examplefirebaselogin;

import android.content.DialogInterface;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_email_activity_signup)
    EditText editTextEmail;
    @BindView(R.id.edit_text_password_activity_signup)
    EditText editTextPassword;
    @BindView(R.id.relative_layout_progress_activity_signup)
    RelativeLayout relativeLayoutProgress;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        initFirebase();
    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick({R.id.button_next_activity_signup})
    public void onClick(Button button) {
        switch (button.getId()) {
            case R.id.button_next_activity_signup:
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                signup(email, password);
                break;
        }
    }

    private void signup(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.error_message_username_empty, Snackbar.LENGTH_LONG)
                    .show();
        } else if (TextUtils.isEmpty(password)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.error_message_password_empty, Snackbar.LENGTH_LONG)
                    .show();
        } else {
            showProgress();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideProgress();
                            if (task.isSuccessful()) {
                                //  signup success
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);
                                alertDialogBuilder.setTitle("Signup");
                                alertDialogBuilder.setMessage("Your account has been registered. Please sign in use your username and password.");
                                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });
                                alertDialogBuilder.show();
                            } else {
                                task.getException().printStackTrace();
                                //  signup fail
                                final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "registered has been failed! Please try again.", Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.dismiss();
                                    }
                                });
                                snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                                snackbar.show();
                            }
                        }
                    });
        }
    }

    private void hideProgress() {
        relativeLayoutProgress.setVisibility(View.GONE);
    }

    private void showProgress() {
        relativeLayoutProgress.setVisibility(View.VISIBLE);
    }
}
