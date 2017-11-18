package com.digitalmoney.home.ui;

import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;
import com.github.oliveiradev.lib.RxPhoto;
import com.github.oliveiradev.lib.shared.TypeRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class RedeemActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private Typeface typefaceBold;
    private Typeface typefaceLarge;
    private ImageView ivQRImage;
    private EditText etMobileNo;
    private TextView tvWalletMoney;
    private TextView tvQRCode;
    private Button btnSubmit;
    private TextView tvWallet;
    private AdView mAdViewTop, mAdViewBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        initUI();

    }


    private void initUI(){

        typefaceBold = Typeface.createFromAsset(getAssets(), Utils.TYPEFACE_PATH_BOLD);
        typefaceLarge = Typeface.createFromAsset(getAssets(), Utils.TYPEFACE_PATH_LARGE);

        mAdViewTop = (AdView)findViewById(R.id.adViewShareTop);
        mAdViewBottom = (AdView)findViewById(R.id.adViewShareBottom);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewBottom.loadAd(adRequest);
        mAdViewTop.loadAd(adRequest);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setTypeface(typefaceBold);
        toolbarTitle.setText("Paytm");
        setSupportActionBar(toolbar);

        ivQRImage = (ImageView) findViewById(R.id.ivQRImage);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etMobileNo.setTypeface(typefaceLarge);
        tvWalletMoney = (TextView) findViewById(R.id.tvWalletMoney);
        tvWalletMoney.setTypeface(typefaceBold);
        tvQRCode = (TextView) findViewById(R.id.tvQRCode);
        tvQRCode.setTypeface(typefaceBold);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(typefaceBold);
        tvWallet = (TextView) findViewById(R.id.tvWallet);
        tvWallet.setTypeface(typefaceLarge);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNo = etMobileNo.getText().toString().trim();

                if (mobileNo.length()!=10){
                    Snackbar.make(view,"Enter a valid mobile", Snackbar.LENGTH_LONG).show();
                }else {
                    Snackbar.make(view,"Mobile no: "+mobileNo, Snackbar.LENGTH_LONG).show();
                }
            }
        });


        ivQRImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"Pick QR Image", Snackbar.LENGTH_LONG).show();
                pickImageFromGallery();
            }
        });
    }


    private void pickImageFromGallery() {

        RxPhoto.requestBitmap(getApplicationContext(), TypeRequest.GALLERY).doOnNext((bitmap) -> {
            ivQRImage.setImageBitmap(bitmap);
            ivQRImage.setScaleType(ImageView.ScaleType.CENTER);
        }).subscribe();

    }


}
