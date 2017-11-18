package com.digitalmoney.home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.digitalmoney.home.R;
import com.digitalmoney.home.adapters.PlanAdapter;
import com.digitalmoney.home.models.PlanModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shailesh on 6/11/17.
 */

public class PlanFragment extends Fragment {


    private List<PlanModel> planList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlanAdapter mAdapter;


    public PlanFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.title_today_task));
    }


    private void initUI(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new PlanAdapter(planList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PlanModel movie = planList.get(position);
                Snackbar.make(view,movie.getLevel()+" has completed with "+movie.getPercent() ,Snackbar.LENGTH_SHORT).show();
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        prepareTaskData();
    }

    private void prepareTaskData() {

        planList.add(new PlanModel("25%","Level 1"));
        planList.add(new PlanModel("35%","Level 2"));
        planList.add(new PlanModel("15%","Level 3"));
        planList.add(new PlanModel("20%","Level 4"));
        planList.add(new PlanModel("16%","Level 5"));
        planList.add(new PlanModel("90%","Level 6"));
        planList.add(new PlanModel("22%","Level 7"));
        planList.add(new PlanModel("19%","Level 8"));
        planList.add(new PlanModel("50%","Level 9"));
        planList.add(new PlanModel("5%","Level 10"));



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
