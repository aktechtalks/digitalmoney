package com.digitalmoney.home.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.digitalmoney.home.R;
import com.digitalmoney.home.models.OfferModel;

import java.util.List;

import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_LARGE;

/**
 * Created by shailesh on 14/11/17.
 */

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {

    private List<OfferModel> offerList;
    private Typeface typefaceLarge, typefaceBold;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            title = (TextView) view.findViewById(R.id.tvItem);
        }
    }

    public OfferAdapter(Context context, List<OfferModel> tasksList) {
        this.mContext = context;
        this.offerList = tasksList;
    }


    @Override
    public OfferAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        typefaceLarge = Typeface.createFromAsset(parent.getContext().getAssets(), TYPEFACE_PATH_LARGE);
        typefaceBold = Typeface.createFromAsset(parent.getContext().getAssets(), TYPEFACE_PATH_BOLD);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
        return new OfferAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(OfferAdapter.MyViewHolder holder, int position) {
        OfferModel model = offerList.get(position);

        holder.title.setText(model.getTitle());
        Glide.with(mContext)
                .load("https://image.flaticon.com/teams/slug/google.jpg")
                .centerCrop()
                .placeholder(R.drawable.ic_image)
                .into(holder.ivIcon);
        holder.title.setTypeface(typefaceBold);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }
}
