package com.digitalmoney.home.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;
import com.digitalmoney.home.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

/**
 * Created by AJ
 * Created on 09-Jun-17.
 */

public class LoginWithMobile extends AppCompatActivity {

    private final String TAG = LoginWithMobile.class.getSimpleName();
    private EditText                                                mPhoneNumberField;
    private EditText                                                mVerificationField;
    private FirebaseAuth                                            mAuth;
    private Typeface                                                typefaceBold;
    private Typeface                                                typefaceLarge;
    private ProgressBar                                             progressBar;
    private DatabaseReference                                       mDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        mAuth = FirebaseAuth.getInstance();

        intUI();

    }




    private void intUI() {

        typefaceBold       = Typeface.createFromAsset(getAssets(), Utils.TYPEFACE_PATH_BOLD);
        typefaceLarge      = Typeface.createFromAsset(getAssets(), Utils.TYPEFACE_PATH_LARGE);
        mPhoneNumberField  = (EditText) findViewById(R.id.field_phone_number);
        mVerificationField = (EditText) findViewById(R.id.field_verification_code);
        progressBar        = (ProgressBar) findViewById(R.id.progressBar);


        mPhoneNumberField. setTypeface(typefaceLarge);
        mVerificationField.setTypeface(typefaceLarge);

    }




    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");





                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mVerificationField.setError("Invalid code.");
                            }
                        }
                    }
                });}





    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(LoginWithMobile.this, MainActivity.class));
            finish();
        }
    }



}
