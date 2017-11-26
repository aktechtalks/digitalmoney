package com.digitalmoney.home.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;
import com.digitalmoney.home.adapters.OfferAdapter;
import com.digitalmoney.home.models.OfferModel;

import java.util.ArrayList;
import java.util.List;

import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_LARGE;

/**
 * Created by shailesh on 6/11/17.
 */

public class OfferFragment extends Fragment {

    private TextView term_condition, refer_terms;
    private RecyclerView recyclerView;
    private List<OfferModel> offerList = new ArrayList<>();
    private Typeface typefaceLarge,typefaceBold;
    private OfferAdapter mAdapter;


    public OfferFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_offer, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SpannableString fragTitle = Utils.setSpannableString(getContext(),
                getResources().getString(R.string.title_offer), Utils.TYPEFACE_LARGE);
        getActivity().setTitle(fragTitle);
    }

    private void initUI(View view) {

        typefaceLarge = Typeface.createFromAsset(getContext().getAssets(), TYPEFACE_PATH_LARGE);
        typefaceBold = Typeface.createFromAsset(getContext().getAssets(), TYPEFACE_PATH_BOLD);

        term_condition = (TextView) view.findViewById(R.id.term_condition);
        term_condition.setTypeface(typefaceBold);
        term_condition.setText(getResources().getString(R.string.terms_condition));

        refer_terms = (TextView) view.findViewById(R.id.refer_terms);
        refer_terms.setTypeface(typefaceBold);
        refer_terms.setText(getResources().getString(R.string.bottom_terms));

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new OfferAdapter(getContext(), offerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareTaskData();
    }


    private void prepareTaskData() {

        OfferModel model1 = new OfferModel("5000 Joining redme phone", "");
        offerList.add(model1);

        OfferModel model2 = new OfferModel("4000 joining jio phone", "");
        offerList.add(model2);

        OfferModel model3 = new OfferModel("2000 joining 3000 paytm", "");
        offerList.add(model3);

        //OfferModel model4 = new OfferModel("1000 joining 2000 paytm", "");
        //offerList.add(model4);

        mAdapter.notifyDataSetChanged();
    }
}
