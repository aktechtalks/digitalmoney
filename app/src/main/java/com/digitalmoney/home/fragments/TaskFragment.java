package com.digitalmoney.home.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.TypefaceSpan;
import com.digitalmoney.home.Utility.Utils;
import com.digitalmoney.home.ui.BaseActivity;
import com.digitalmoney.home.ui.TaskReportActivity;
import com.digitalmoney.home.adapters.TaskAdapter;
import com.digitalmoney.home.models.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shailesh on 6/11/17.
 */

public class TaskFragment extends Fragment {


    private List<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapter mAdapter;

    protected SpannableString setSpannableString(String titleString, String typefaceName){
        SpannableString spannableText = new SpannableString(titleString);
        spannableText.setSpan(new TypefaceSpan(getContext(), typefaceName), 0, spannableText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableText;
    }


    public TaskFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        initUI(view);
        return view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SpannableString fragTitle = Utils.setSpannableString(getContext(),
                getResources().getString(R.string.title_today_task), Utils.TYPEFACE_LARGE);
        getActivity().setTitle(fragTitle);
    }


    private void initUI(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new TaskAdapter(taskList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Task movie = taskList.get(position);

                Intent intent = new Intent(getContext(), TaskReportActivity.class);
                intent.putExtra("taskTitle", movie.getTaskName().toString());
                intent.putExtra("POSITION", String.valueOf(position));
                startActivity(intent);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        prepareTaskData();
    }

    private void prepareTaskData() {

        Task task1 = new Task("Task 1", "1");
        taskList.add(task1);

        Task task2 = new Task("Task 2", "2");
        taskList.add(task2);

        Task task3 = new Task("Task 3", "3");
        taskList.add(task3);

        Task task4 = new Task("Task 4", "4");
        taskList.add(task4);

        Task task5 = new Task("Task 5", "5");
        taskList.add(task5);

        Task task6 = new Task("Task 6", "6");
        taskList.add(task6);

        Task task7 = new Task("Task 7", "7");
        taskList.add(task7);

        Task task8 = new Task("Task 8", "8");
        taskList.add(task8);

        Task task9 = new Task("Task 9", "9");
        taskList.add(task9);

        Task task10 = new Task("Task 10", "10");
        taskList.add(task10);

        mAdapter.notifyDataSetChanged();
    }




    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public  class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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
