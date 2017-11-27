package com.digitalmoney.home.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginWithEmail extends BaseActivity {


    private final String  TAG =
            LoginWithEmail.class.getSimpleName();
    private EditText      et_password;
    private EditText      etEmailId;
    private Button        btnLogin;
    private ProgressBar   progressBar;
    private FirebaseAuth  mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);

        initUI();

    }

    private void initUI() {

        mAuth       = FirebaseAuth.getInstance();
        et_password = (EditText) findViewById(R.id.et_password);
        etEmailId   = (EditText) findViewById(R.id.et_emailId);
        btnLogin    = (Button) findViewById(R.id.btnLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_address = etEmailId.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                if (TextUtils.isEmpty(email_address)){
                    etEmailId.setError("Enter Email");
                }else if (!isValidEmail(email_address)){
                    etEmailId.setError("Please enter a valid email address");
                }else if (password.equalsIgnoreCase("")){
                    et_password.setError("Please enter password");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    validateLoginWithFirebase(email_address, password);
                }
            }
        });


        findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                finish();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }


    private void validateLoginWithFirebase(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "login:successful");
                            updateUI();
                        } else {

                            Log.e(TAG, "login:failed::"+task.getException());
                            Toast.makeText(LoginWithEmail.this,
                                    showError(task.getException().toString()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }




    private String showError(String exceptionString){

        String[] errorException = exceptionString.split(":");
        String rightStringMsg = errorException[1];

        return rightStringMsg;
    }



    private void updateUI() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}
