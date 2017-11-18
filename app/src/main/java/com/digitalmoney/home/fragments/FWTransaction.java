package com.digitalmoney.home.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;

/**
 * Created by shailesh on 13/11/17.
 */

public class FWTransaction extends Fragment{

    TextView tvWallerTransaction;
    Typeface typefaceBold, typefaceLarge;

    public FWTransaction() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewInflator = inflater.inflate(R.layout.wallet_transaction, container,false);
        tvWallerTransaction = viewInflator.findViewById(R.id.tvWalletTransaction);
        typefaceBold = Typeface.createFromAsset(getContext().getAssets(), Utils.TYPEFACE_PATH_BOLD);
        typefaceLarge = Typeface.createFromAsset(getContext().getAssets(), Utils.TYPEFACE_PATH_LARGE);
        tvWallerTransaction.setTypeface(typefaceLarge);

        return viewInflator;
    }
}
