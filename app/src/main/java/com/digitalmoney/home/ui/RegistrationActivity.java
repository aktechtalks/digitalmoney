package com.digitalmoney.home.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.models.LoginUser;
import com.digitalmoney.home.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_LARGE;

public class RegistrationActivity extends AppCompatActivity {

    private Button                                      btnSignUp, btnLogin;
    private TextView                                    tvTAG;
    private EditText                                    et_password;
    private EditText                                    et_confirm_password;
    private EditText                                    et_name, et_emailId, et_mobile;
    private Typeface                                    typefaceBold, typefaceLarge;
    private ProgressBar                                 validate_progressbar;
    private FirebaseAuth                                mAuth;
    private DatabaseReference                           mDatabase;




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

        btnSignUp.setTypeface(typefaceBold);
        btnLogin.setTypeface(typefaceBold);
        et_password.setTypeface(typefaceLarge);
        et_confirm_password.setTypeface(typefaceLarge);
        tvTAG.setTypeface(typefaceBold);

        buttonClickHandler();
    }



    private void buttonClickHandler() {

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate_progressbar.setVisibility(View.GONE);
                String name=et_name.getText().toString().trim();
                String emailId=et_emailId.getText().toString().trim();
                String mobile=et_mobile.getText().toString().trim();
                String password=et_password.getText().toString().trim();
                String confirm_password=et_confirm_password.getText().toString().trim();

                if (name.equalsIgnoreCase("")){
                    et_name.requestFocus();
                    et_name.setError("Enter name");
                }else if (emailId.equalsIgnoreCase("")){
                    et_emailId.requestFocus();
                    et_emailId.setError(getResources().getString(R.string.provide_email));
                }else if (mobile.equalsIgnoreCase("")){
                    et_mobile.requestFocus();
                    et_mobile.setError(getResources().getString(R.string.provide_mobile));
                }else if (mobile.length()!=10){
                    et_mobile.requestFocus();
                    et_mobile.setError(getResources().getString(R.string.provide_valid_mobile));
                }else if (password.equalsIgnoreCase("")){
                    et_password.requestFocus();
                    et_password.setError(getResources().getString(R.string.provide_password));
                }else if (!confirm_password.equalsIgnoreCase(password)){
                    et_confirm_password.requestFocus();
                    et_confirm_password.setError(getResources().getString(R.string.mismach_password));
                }else {

                    validate_progressbar.setVisibility(View.VISIBLE);
                    writeNewUser(name, emailId, mobile, password);
                }
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getApplicationContext(), LoginWithMobile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }



    private void writeNewUser( String name, String email, String mobile, String password) {

        String userUId = mAuth.getCurrentUser().getUid();
        User userProfile = new User(userUId, name, email, mobile,password);
        LoginUser userLogin = new LoginUser(mobile, password);

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
