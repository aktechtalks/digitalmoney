package com.digitalmoney.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.digitalmoney.home.R;
import com.digitalmoney.home.admin.AdminNotification;

/**
 * Created by shailesh on 6/11/17.
 */

public class AdminFragment extends Fragment {

    Button btnAddNotification;

    public AdminFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewAdmin = inflater.inflate(R.layout.layout_admin, container, false);
        btnAddNotification = (Button) viewAdmin.findViewById(R.id.btnAddNotification);
        return viewAdmin;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnClickHandler();
        getActivity().setTitle(getResources().getString(R.string.title_admin));
    }


    private void btnClickHandler(){

        btnAddNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminNotification.class));
            }
        });
    }
}
