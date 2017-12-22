package com.digitalmoney.home.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;
import com.github.oliveiradev.lib.RxPhoto;
import com.github.oliveiradev.lib.shared.TypeRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paytm.pgsdk.PaytmConstants;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.Checksum;

import static com.digitalmoney.home.Utility.Utils.PREF_KEY_COIN_COUNT;
import static com.digitalmoney.home.Utility.Utils.WALLET_MONEY;


public class RedeemActivity extends BaseActivity {

    private Toolbar           toolbar;
    private Typeface          typefaceBold;
    private Typeface          typefaceLarge;
    private ImageView         ivQRImage;
    private EditText          etMobileNo;
    private TextView          tvWalletMoney;
    private TextView          tvQRCode;
    private Button            btnSubmit;
    private TextView          tvWallet;
    private AdView            mAdViewTop, mAdViewBottom;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        initUI();

    }


    private void initUI(){
        mDatabase       = FirebaseDatabase.getInstance().getReference();
        typefaceBold    = Typeface.createFromAsset(getAssets(), Utils.TYPEFACE_PATH_BOLD);
        typefaceLarge   = Typeface.createFromAsset(getAssets(), Utils.TYPEFACE_PATH_LARGE);
        toolbar         = (Toolbar) findViewById(R.id.toolbar);

        ivQRImage       = (ImageView) findViewById(R.id.ivQRImage);
        etMobileNo      = (EditText) findViewById(R.id.etMobileNo);
        tvWalletMoney   = (TextView) findViewById(R.id.tvWalletMoney);
        tvQRCode        = (TextView) findViewById(R.id.tvQRCode);
        btnSubmit       = (Button) findViewById(R.id.btnSubmit);
        tvWallet        = (TextView) findViewById(R.id.tvWallet);

        String walletMoney = getLocale(this, WALLET_MONEY);
        tvWalletMoney.setText(walletMoney);

        setTypeface();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNo = etMobileNo.getText().toString().trim();

                if (mobileNo.length()!=10){

                    Snackbar.make(view,"Enter a valid mobile", Snackbar.LENGTH_LONG).show();
                }else {
                    startTransaction();
                    Snackbar.make(view,"Mobile no: "+mobileNo, Snackbar.LENGTH_LONG).show();
                }
            }
        });


        ivQRImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"Pick QR Image", Snackbar.LENGTH_LONG).show();
                pickFromGallery();
            }
        });


        fetchDataFromFirebase();

    }




    private void startTransaction(){
        //startActivity(new Intent(getApplicationContext(), MerchantActivity.class));
        onStartTransaction();
    }



    public void onStartTransaction() {
        PaytmPGService Service = PaytmPGService.getStagingService();


        //Kindly create complete Map and checksum on your server side and then put it here in paramMap.

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID" , "WorldP64425807474247");
        paramMap.put("ORDER_ID" , "TestMerchant000111007");
        paramMap.put("CUST_ID" , "mohit.aggarwal@paytm.com");
        paramMap.put("INDUSTRY_TYPE_ID" , "Retail");
        paramMap.put("CHANNEL_ID" , "WAP");
        paramMap.put("TXN_AMOUNT" , "1");
        paramMap.put("WEBSITE" , "worldpressplg");
        paramMap.put("CALLBACK_URL" , "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
        paramMap.put("CHECKSUMHASH" , "w2QDRMgp1/BNdEnJEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");
        PaytmOrder Order = new PaytmOrder(paramMap);


        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {

                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction : " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction response "+inResponse.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() {
                        // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }




    private void generatePaytmChecksum(){

//          String MID = "XXXXXXXXXXXXXXXXXXXXXXXX";
//          MercahntKey = "XXXXXXXXXXXXXXXX";
//          String INDUSTRY_TYPE_ID = "XXXXXXXXXXXX";
//          String CHANNLE_ID = "WAP";
//          String WEBSITE = "XXXXXXXXXX";
//          String CALLBACK_URL = "XXXXXXXXXXXXXX";
//
//
//            TreeMap<String,String> paramMap = new TreeMap<String,String>();
//            paramMap.put("MID" , MID);
//            paramMap.put("ORDER_ID" , "ORDER00011");
//            paramMap.put("CUST_ID" , "CUST00011");
//            paramMap.put("INDUSTRY_TYPE_ID" , INDUSTRY_TYPE_ID);
//            paramMap.put("CHANNEL_ID" , CHANNLE_ID);
//            paramMap.put("TXN_AMOUNT" , "1.00");
//            paramMap.put("WEBSITE" , WEBSITE);
//            paramMap.put("EMAIL" , "test@gmail.com");
//            paramMap.put("MOBILE_NO" , "9999999999");
//            paramMap.put("CALLBACK_URL" , CALLBACK_URL);
//
//            try{
//
//                String checkSum =  CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(MercahntKey, paramMap);
//                paramMap.put("CHECKSUMHASH" , checkSum);
//
//                System.out.println("Paytm Payload: "+ paramMap);
//
//            }catch(Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        }




    private void fetchDataFromFirebase(){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object wallet = dataSnapshot.child("users").child(uid).child("wallet_money").getValue();

                if (wallet!=null){
                    Log.e("wallet_money::::::",wallet.toString());
                    tvWalletMoney.setText("₹ "+wallet.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error:-->", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setTypeface() {

        tvWallet.setTypeface(typefaceLarge);
        etMobileNo.setTypeface(typefaceLarge);
        tvWalletMoney.setTypeface(typefaceBold);
        tvQRCode.setTypeface(typefaceBold);
        btnSubmit.setTypeface(typefaceBold);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Paytm");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAdViewTop    = (AdView)findViewById(R.id.adViewShareTop);
        mAdViewBottom = (AdView)findViewById(R.id.adViewShareBottom);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewBottom.loadAd(adRequest);
        mAdViewTop.loadAd(adRequest);
    }


    private void pickFromGallery() {

        RxPhoto.requestBitmap(getApplicationContext(), TypeRequest.GALLERY).doOnNext((bitmap) -> {
            ivQRImage.setImageBitmap(bitmap);
            ivQRImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }).subscribe();

    }



    private void captureFromCamera() {

        RxPhoto.requestBitmap(getApplicationContext(), TypeRequest.CAMERA).doOnNext((bitmap) -> {
            ivQRImage.setImageBitmap(bitmap);
            ivQRImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }).subscribe();

    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
