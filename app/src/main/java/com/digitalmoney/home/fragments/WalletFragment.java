package com.digitalmoney.home.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.digitalmoney.home.R;
import com.digitalmoney.home.ui.RedeemActivity;
import com.digitalmoney.home.Utility.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shailesh on 6/11/17.
 */

public class WalletFragment extends Fragment {

    TextView tvWalletTag, tvWalletAmount;
    Button btnRedeem;
    ViewPager pagerWallet;
    TabLayout tabLayout;
    Typeface typefaceBold;
    Typeface typefaceLarge;


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
        getActivity().setTitle(getResources().getString(R.string.title_wallet));
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
