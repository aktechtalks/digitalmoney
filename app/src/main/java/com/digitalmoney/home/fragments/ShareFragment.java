package com.digitalmoney.home.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.Utility.VidioBanner;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.digitalmoney.home.R;
import com.ramotion.circlemenu.CircleMenuView;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_LARGE;

/**
 * Created by shailesh on 6/11/17.
 */

public class ShareFragment extends Fragment {

    private TextView       tvTagline, tvReferalCode;
    private Typeface       typefaceBold;
    private Typeface       typefaceLarge;
    private ImageView      ivCopy;
    private AdView         mAdViewTop, mAdViewBottom;
    private CircleMenuView menu ;


    public ShareFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_share, container, false);

        typefaceBold = Typeface.createFromAsset(getContext().getAssets(), TYPEFACE_PATH_BOLD);
        typefaceLarge = Typeface.createFromAsset(getContext().getAssets(), TYPEFACE_PATH_LARGE);
        tvTagline = (TextView) view.findViewById(R.id.tvTagline);
        tvReferalCode = (TextView)view.findViewById(R.id.tvReferalCode);
        ivCopy = (ImageView)view.findViewById(R.id.ivCopy);
        menu = (CircleMenuView) view.findViewById(R.id.circle_menu);

        tvTagline.setTypeface(typefaceLarge);
        tvReferalCode.setTypeface(typefaceBold);

        mAdViewTop = (AdView) view.findViewById(R.id.adViewShareTop);
        mAdViewBottom = (AdView)view.findViewById(R.id.adViewShareBottom);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewBottom.loadAd(adRequest);
        mAdViewTop.loadAd(adRequest);


        clickHandler();
        return view;
    }



    private void clickHandler() {

        menu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuOpenAnimationStart");
            }

            @Override
            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuOpenAnimationEnd");
            }

            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuCloseAnimationStart");
            }

            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
                Log.d("D", "onMenuCloseAnimationEnd");
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonClickAnimationStart| index: " + index);
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonClickAnimationEnd| index: " + index);

            }});


        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy();

                showVideoBanner();
            }
        });
    }



    private void showVideoBanner() {

        Intent intentVideoBanner = new Intent(getContext(), VidioBanner.class);
        intentVideoBanner.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentVideoBanner);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.title_referral));
    }


    private void copy(){

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", "Text to copy");
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Copied",Toast.LENGTH_SHORT).show();
    }




}
