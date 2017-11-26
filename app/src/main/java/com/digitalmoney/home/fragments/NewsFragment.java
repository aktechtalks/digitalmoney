package com.digitalmoney.home.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;

/**
 * Created by shailesh on 6/11/17.
 */

public class NewsFragment extends Fragment {

    public NewsFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_banner_view, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

        SpannableString fragTitle = Utils.setSpannableString(getContext(),
                getResources().getString(R.string.title_news), Utils.TYPEFACE_LARGE);
        getActivity().setTitle(fragTitle);
    }
}
