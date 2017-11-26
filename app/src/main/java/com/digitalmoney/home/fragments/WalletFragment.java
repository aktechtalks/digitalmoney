package com.digitalmoney.home.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.models.User;
import com.digitalmoney.home.ui.RedeemActivity;
import com.digitalmoney.home.Utility.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shailesh on 6/11/17.
 */

public class WalletFragment extends Fragment {

    private TextView          tvWalletTag, tvWalletAmount;
    private Button            btnRedeem;
    private ViewPager         pagerWallet;
    private TabLayout         tabLayout;
    private Typeface          typefaceBold;
    private Typeface          typefaceLarge;
    private DatabaseReference mDatabase;


    public WalletFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewInflater = inflater.inflate(R.layout.fragment_wallet, container, false);
        initUI(viewInflater);
        return viewInflater;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SpannableString fragTitle = Utils.setSpannableString(getContext(),
                getResources().getString(R.string.title_wallet), Utils.TYPEFACE_LARGE);
        getActivity().setTitle(fragTitle);
    }


    private void initUI(View view){

        tvWalletTag = (TextView) view.findViewById(R.id.tvWallet);
        tvWalletAmount = (TextView) view.findViewById(R.id.tvWalletMoney);
        btnRedeem = (Button) view.findViewById(R.id.btnRedeem);
        pagerWallet = (ViewPager) view.findViewById(R.id.pagerWallet);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        typefaceBold = Typeface.createFromAsset(getContext().getAssets(), Utils.TYPEFACE_PATH_BOLD);
        typefaceLarge = Typeface.createFromAsset(getContext().getAssets(), Utils.TYPEFACE_PATH_LARGE);
        tvWalletTag.setTypeface(typefaceLarge);
        tvWalletAmount.setTypeface(typefaceBold);
        btnRedeem.setTypeface(typefaceBold);



        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object wallet = dataSnapshot.child("users").child(uid).child("wallet_money").getValue();

                if (wallet!=null){
                    Log.e("wallet_money::::::",wallet.toString());
                    tvWalletAmount.setText("₹ "+wallet.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error:-->", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });



        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RedeemActivity.class);
                startActivity(intent);
            }
        });


        initialisePager();
    }



    private void initialisePager(){

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new FWTransaction(), "Transaction");
        adapter.addFragment(new FWReceived(), "Received");
        pagerWallet.setAdapter(adapter);
        tabLayout.setupWithViewPager(pagerWallet);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
