package com.digitalmoney.home.fragments;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.adapters.NotificationAdapter;
import com.digitalmoney.home.models.Task;
import com.digitalmoney.home.models.User;
import com.digitalmoney.home.ui.MainActivity;
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

public class NotificationFragment extends Fragment {

    public NotificationFragment() {

    }

    private List<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotificationAdapter mAdapter;
    private DatabaseReference mDatabase;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.title_notification));


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Task notification_model = dataSnapshot.child("users").child("notification").getValue(Task.class);

                if (notification_model!=null){

                    String heading = notification_model.getTaskId();
                    String description = notification_model.getTaskName();

                    prepareTaskData(heading, description);

                }else {
                    Toast.makeText(getContext(), "No Notification Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error:-->", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }


    private void initUI(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new NotificationAdapter(taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new NotificationFragment.RecyclerTouchListener(getContext(), recyclerView, new TaskFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                /*Task movie = taskList.get(position);
                Snackbar.make(view,movie.getTaskName() + " is selected!",Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), TaskReportActivity.class);
                intent.putExtra("taskTitle", movie.getTaskName().toString());
                startActivity(intent);*/
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    private void prepareTaskData(String notificationTitle, String notificationDescription) {

        Task taskNotification = new Task(notificationDescription, notificationTitle);
        taskList.add(taskNotification);
        mAdapter.notifyDataSetChanged();
    }




    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public  class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private TaskFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TaskFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
