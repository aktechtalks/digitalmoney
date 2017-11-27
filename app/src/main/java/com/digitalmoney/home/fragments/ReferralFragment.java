package com.digitalmoney.home.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.Utility.Utils;
import com.digitalmoney.home.Utility.VideoBanner;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.digitalmoney.home.R;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.ramotion.circlemenu.CircleMenuView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CLIPBOARD_SERVICE;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_LARGE;



public class ReferralFragment extends Fragment {

    private TextView       tvTagline, tvReferalCode;
    private Typeface       typefaceBold;
    private Typeface       typefaceLarge;
    private ImageView      ivCopy;
    private AdView         mAdViewTop, mAdViewBottom;
    private CircleMenuView menu ;
    private FloatingActionButton floatBtnShare;
    private String TAG = ReferralFragment.class.getSimpleName();
    private int REQUEST_INVITE = 101;


    public ReferralFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view       = inflater.inflate(R.layout.fragment_share, container, false);

        typefaceBold    = Typeface.createFromAsset(getContext().getAssets(), TYPEFACE_PATH_BOLD);
        typefaceLarge   = Typeface.createFromAsset(getContext().getAssets(), TYPEFACE_PATH_LARGE);
        tvTagline       = (TextView) view.findViewById(R.id.tvTagline);
        tvReferalCode   = (TextView)view.findViewById(R.id.tvReferalCode);
        ivCopy          = (ImageView)view.findViewById(R.id.ivCopy);
        menu            = (CircleMenuView) view.findViewById(R.id.circle_menu);
        mAdViewTop      = (AdView) view.findViewById(R.id.adViewShareTop);
        mAdViewBottom   = (AdView)view.findViewById(R.id.adViewShareBottom);
        floatBtnShare   = (FloatingActionButton) view.findViewById(R.id.floatBtnShare);
        AdRequest adRequest = new AdRequest.Builder().build();

        initialiseDynamicLink();

        tvTagline.setTypeface(typefaceLarge);
        tvReferalCode.setTypeface(typefaceBold);
        mAdViewBottom.loadAd(adRequest);
        mAdViewTop.loadAd(adRequest);

        clickHandler();
        return view;
    }





    private void initialiseDynamicLink(){

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getActivity().getIntent())
                .addOnSuccessListener(getActivity(), new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {

                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.e(TAG, deepLink.toString());
                        }

                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });
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
                onInviteClicked();

                //showVideoBanner();
            }
        });



        floatBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEntry = tvTagline.getText().toString();

                Intent textShareIntent = new Intent(Intent.ACTION_SEND);
                textShareIntent.putExtra(Intent.EXTRA_TEXT, userEntry);
                textShareIntent.setType("text/plain");
                startActivity(textShareIntent);
            }
        });


    }





    private void onInviteClicked() {

        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.app_name))
                .setMessage("please install digital money application")
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                //.setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {

            }
        }
    }





    private void showVideoBanner() {

        Intent intentVideoBanner = new Intent(getContext(), VideoBanner.class);
        intentVideoBanner.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentVideoBanner);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SpannableString fragTitle = Utils.setSpannableString(getContext(),
                getResources().getString(R.string.title_referral), Utils.TYPEFACE_LARGE);
        getActivity().setTitle(fragTitle);

    }


    private void copy(){

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("500059", "500059");
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Copied",Toast.LENGTH_SHORT).show();
    }




}
