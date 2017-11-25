package com.digitalmoney.home.ui;

import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        mDatabase            = FirebaseDatabase.getInstance().getReference();
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





        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object wallet = dataSnapshot.child("users").child(uid).child("wallet_money").getValue();

                if (wallet!=null){
                    Log.e("wallet_money::::::",wallet.toString());
                    tvWalletMoney.setText(wallet.toString());
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


    private void pickImageFromGallery() {

        RxPhoto.requestBitmap(getApplicationContext(), TypeRequest.GALLERY).doOnNext((bitmap) -> {
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
