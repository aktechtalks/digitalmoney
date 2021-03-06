package com.digitalmoney.home.ui;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.models.LoginUser;
import com.digitalmoney.home.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_LARGE;

public class RegistrationActivity extends AppCompatActivity {

    private Button             btnSignUp, btnLogin;
    private TextView           tvTAG;
    private EditText           et_password;
    private EditText           et_confirm_password;
    private EditText           et_name, et_emailId, et_mobile;
    private EditText           et_referral_code;
    private Typeface           typefaceBold, typefaceLarge;
    private ProgressBar        validate_progressbar;
    private FirebaseAuth       mAuth;
    private DatabaseReference  mDatabase;
    private final String TAG = "RegistrationActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        initUi();
    }


    private void initUi() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        typefaceBold = Typeface.createFromAsset(getAssets(), TYPEFACE_PATH_BOLD);
        typefaceLarge = Typeface.createFromAsset(getAssets(), TYPEFACE_PATH_LARGE);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        et_name = (EditText) findViewById(R.id.et_name);
        et_name.setTypeface(typefaceLarge);
        et_emailId = (EditText)findViewById(R.id.et_emailId);
        et_emailId.setTypeface(typefaceLarge);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_mobile.setTypeface(typefaceLarge);
        TextView tvAlreadyAccount = (TextView) findViewById(R.id.tvAlreadyAccount);
        tvAlreadyAccount.setTypeface(typefaceLarge);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        tvTAG = (TextView) findViewById(R.id.tvTAG);
        validate_progressbar = (ProgressBar) findViewById(R.id.validate_progressbar);
        et_referral_code = (EditText) findViewById(R.id.et_referral_code);


        btnSignUp.setTypeface(typefaceBold);
        btnLogin.setTypeface(typefaceBold);
        et_password.setTypeface(typefaceLarge);
        et_confirm_password.setTypeface(typefaceLarge);
        et_referral_code.setTypeface(typefaceLarge);
        tvTAG.setTypeface(typefaceBold);

        buttonClickHandler();
    }



    private void buttonClickHandler() {

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate_progressbar.setVisibility(View.GONE);

                String usrName          = et_name.getText().toString().trim();
                String emailId          = et_emailId.getText().toString().trim();
                String mobile           = et_mobile.getText().toString().trim();
                String password         = et_password.getText().toString().trim();
                String confirm_password = et_confirm_password.getText().toString().trim();
                String referral_code    = et_referral_code.getText().toString().trim();

                String photoUrl = "";

                if (usrName.equalsIgnoreCase("")) {
                    et_name.requestFocus();
                    et_name.setError("Provide Name");
                } else if (emailId.equalsIgnoreCase("")) {
                    et_emailId.requestFocus();
                    et_emailId.setError(getResources().getString(R.string.provide_email));
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
                    et_emailId.requestFocus();
                    et_emailId.setError(getResources().getString(R.string.provide_valid_email));
                } else if (mobile.equalsIgnoreCase("")) {
                    et_mobile.requestFocus();
                    et_mobile.setError(getResources().getString(R.string.provide_mobile));
                } else if (mobile.length() != 10) {
                    et_mobile.requestFocus();
                    et_mobile.setError(getResources().getString(R.string.provide_valid_mobile));
                }else if (referral_code.equalsIgnoreCase("")) {
                    et_referral_code.requestFocus();
                    et_referral_code.setError(getResources().getString(R.string.provide_referral_code));
                } else if (password.equalsIgnoreCase("")) {
                    et_password.requestFocus();
                    et_password.setError(getResources().getString(R.string.provide_password));
                } else if (!confirm_password.equalsIgnoreCase(password)) {
                    et_confirm_password.requestFocus();
                    et_confirm_password.setError(getResources().getString(R.string.mismach_password));
                } else {

                    createUserInWithPhoneAuthCredential(emailId, password, usrName, photoUrl, mobile, referral_code);
                }
            }});


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getApplicationContext(), LoginWithEmail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


    }




    private void createUserInWithPhoneAuthCredential(String email, String password, String userName, String photoUrl, String mobile, String referral_code){


        validate_progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Log.d(TAG, "User Registration Is Successful");
                    FirebaseUser user = mAuth.getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(userName).setPhotoUri(Uri.parse(photoUrl)).build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            validate_progressbar.setVisibility(View.GONE);
                            if (task.isSuccessful()){

                                validate_progressbar.setVisibility(View.GONE);
                                writeNewUser(userName, email, mobile, password, referral_code);
                                Log.d(TAG, "User Profile Updated Successfully.");

                            }else {
                                validate_progressbar.setVisibility(View.GONE);
                                Log.e(TAG, "User Profile Updation failed.");

                            }

                        }
                    });
                }else {
                    validate_progressbar.setVisibility(View.GONE);
                    Log.e(TAG, "User Registration Is Failed::"+task.getException());
                    Toast.makeText(RegistrationActivity.this,
                            showError(task.getException().toString()),
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



    private String showError(String exceptionString){

        String[] errorException = exceptionString.split(":");
        String rightStringMsg = errorException[1];

        return rightStringMsg;
    }



    private void writeNewUser( String name, String email, String mobile, String password, String referral_code) {

        String userUId = mAuth.getCurrentUser().getUid();
        User userProfile = new User(userUId, name, email, mobile, password, referral_code);
        LoginUser userLogin = new LoginUser(mobile, password, referral_code);

        mDatabase.child("users").child(userUId).child("login").setValue(userLogin);
        mDatabase.child("users").child(userUId).child("username").setValue(name);
        mDatabase.child("users").child(userUId).child("profile").setValue(userProfile, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                validate_progressbar.setVisibility(View.GONE);

                if (databaseError == null){

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
                else { Toast.makeText(getApplicationContext(),"Registration failed", Toast.LENGTH_LONG).show(); }
            }
        });


    }



}
