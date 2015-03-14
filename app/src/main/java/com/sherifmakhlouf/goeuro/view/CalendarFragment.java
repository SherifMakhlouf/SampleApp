package com.sherifmakhlouf.goeuro.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateChangedListener;
import com.sherifmakhlouf.goeuro.GoEuroApp;
import com.sherifmakhlouf.goeuro.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

;


public class CalendarFragment extends Fragment implements ActionMode.Callback, OnDateChangedListener {

    interface OnChooseDateListener {
        public void onDateChanged(String date);

        public void onActionDone();
    }

    @InjectView(R.id.calendarView)
    MaterialCalendarView calendarView;
    private ActionMode actionMode;
    private OnChooseDateListener dateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calenadr, container, false);
        GoEuroApp.get(getActivity().getApplicationContext()).inject(this);
        ButterKnife.inject(this, rootView);
        calendarView.setOnDateChangedListener(this);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actionMode = ((ActionBarActivity) activity).startSupportActionMode(this);
        dateListener = (SearchActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (actionMode != null)
            actionMode.finish();
    }


    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        return true;

    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        if (dateListener != null)
            dateListener.onActionDone();

    }

    @Override
    public void onDateChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {

        String date = calendarDay.getDay() + " / " + calendarDay.getMonth() + " / " + calendarDay.getYear();
        if (dateListener != null)
            dateListener.onDateChanged(date);
    }
}
