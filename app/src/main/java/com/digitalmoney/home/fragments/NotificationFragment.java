package com.digitalmoney.home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.digitalmoney.home.R;
import com.digitalmoney.home.adapters.NotificationAdapter;
import com.digitalmoney.home.models.Task;

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


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.title_notification));
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


        prepareTaskData();
    }

    private void prepareTaskData() {

        Task task1 = new Task(getResources().getString(R.string.notificationOne), getResources().getString(R.string.notificationIdOne));
        taskList.add(task1);

        Task task2 = new Task(getResources().getString(R.string.notificationTwo), getResources().getString(R.string.notificationIdTwo));
        taskList.add(task2);

        Task task3 = new Task(getResources().getString(R.string.notificationThree), getResources().getString(R.string.notificationIdThree));
        taskList.add(task3);

        Task task4 = new Task(getResources().getString(R.string.notificationFour), getResources().getString(R.string.notificationIdFour));
        taskList.add(task4);

        Task task5 = new Task(getResources().getString(R.string.notificationOne), getResources().getString(R.string.notificationIdOne));
        taskList.add(task5);

        Task task6 = new Task(getResources().getString(R.string.notificationTwo), getResources().getString(R.string.notificationIdTwo));
        taskList.add(task6);

        Task task7 = new Task(getResources().getString(R.string.notificationThree), getResources().getString(R.string.notificationIdThree));
        taskList.add(task7);

        Task task8 = new Task(getResources().getString(R.string.notificationFour), getResources().getString(R.string.notificationIdFour));
        taskList.add(task8);

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
