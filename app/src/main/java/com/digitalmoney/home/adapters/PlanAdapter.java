package com.digitalmoney.home.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitalmoney.home.R;
import com.digitalmoney.home.models.PlanModel;

import java.util.List;

import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_LARGE;

/**
 * Created by shailesh on 9/11/17.
 */

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> {

    private List<PlanModel> planList;
    private Typeface typefaceLarge, typefaceBold;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titlePercent,titleLevel;

        public MyViewHolder(View view) {
            super(view);
            titlePercent = (TextView) view.findViewById(R.id.titlePercent);
            titlePercent.setTypeface(typefaceBold);

            titleLevel =  (TextView) view.findViewById(R.id.titleLevel);
            titleLevel.setTypeface(typefaceBold);
        }
    }

    public PlanAdapter(List<PlanModel> tasksList) {
        this.planList = tasksList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        typefaceLarge = Typeface.createFromAsset(parent.getContext().getAssets(), TYPEFACE_PATH_LARGE);
        typefaceBold = Typeface.createFromAsset(parent.getContext().getAssets(), TYPEFACE_PATH_BOLD);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_row, parent, false);
        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PlanModel movie = planList.get(position);
        holder.titlePercent.setText(movie.getPercent());
        holder.titleLevel.setText(movie.getLevel());
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }
}
