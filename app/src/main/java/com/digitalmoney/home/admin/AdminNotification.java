package com.digitalmoney.home.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.models.Task;
import com.digitalmoney.home.ui.MainActivity;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shailesh on 18/11/17.
 */

public class AdminNotification extends AppCompatActivity{

    private DatabaseReference mDatabase;
    private Button            btnAdd;
    private ProgressBar       loading;
    private TextInputLayout   layout_title;
    private TextInputLayout   layout_description;
    private TextInputEditText et_title;
    private TextInputEditText et_description;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_notification);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnAdd =         (Button) findViewById(R.id.addNotification);
        loading =        (ProgressBar) findViewById(R.id.loading);
        layout_title =   (TextInputLayout) findViewById(R.id.layout_title);
        layout_title =   (TextInputLayout) findViewById(R.id.layout_description);
        et_title =       (TextInputEditText) findViewById(R.id.et_title);
        et_description = (TextInputEditText) findViewById(R.id.et_description);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layout_title.setError(null);
                layout_description.setError(null);

                String title = et_title.getText().toString().trim();
                String description = et_description.getText().toString().trim();

                if (title.equalsIgnoreCase("")){
                    Snackbar.make(view, getResources().getString(R.string.provide_notification_title), Snackbar.LENGTH_SHORT).show();
                    layout_title.setError(getResources().getString(R.string.provide_notification_title));
                }else if (description.equalsIgnoreCase("")){
                    Snackbar.make(view, getResources().getString(R.string.provide_notification_description), Snackbar.LENGTH_SHORT).show();
                    layout_description.setError(getResources().getString(R.string.provide_notification_description));
                }else {
                    loading.setVisibility(View.VISIBLE);
                    Task taskNotification = new Task(title, description);
                    addNotification(taskNotification);
                }
            }
        });

    }


    private void addNotification(Task taskNotification) {

        mDatabase.child("users").child("notification").setValue(taskNotification, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                loading.setVisibility(View.GONE);

                if (databaseError == null){

                    Toast.makeText(AdminNotification.this,
                            getResources().getString(R.string.added_successfully),
                            Toast.LENGTH_SHORT).show();
                }
                else {

                    Toast.makeText(AdminNotification.this,
                            getResources().getString(R.string.addedion_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}

