package com.digitalmoney.home.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitalmoney.home.R;
import com.digitalmoney.home.models.Task;

import java.util.List;

import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_LARGE;

/**
 * Created by shailesh on 9/11/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<Task> tasksList;
    private Typeface typeface,typefaceBold;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvHeadLine;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            tvHeadLine = (TextView) view.findViewById(R.id.tvHeadline);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    public NotificationAdapter(List<Task> tasksList) {
        this.tasksList = tasksList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        typeface = Typeface.createFromAsset(parent.getContext().getAssets(), TYPEFACE_PATH_LARGE);
        typefaceBold = Typeface.createFromAsset(parent.getContext().getAssets(), TYPEFACE_PATH_BOLD);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_row, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task movie = tasksList.get(position);
        holder.title.setText(movie.getTaskName());
        holder.tvHeadLine.setText(movie.getTaskId());
        holder.tvHeadLine.setTypeface(typefaceBold);
        holder.title.setTypeface(typeface);
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }
}
