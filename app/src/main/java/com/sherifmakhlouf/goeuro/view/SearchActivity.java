package com.sherifmakhlouf.goeuro.view;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sherifmakhlouf.goeuro.GoEuroApp;
import com.sherifmakhlouf.goeuro.R;
import com.sherifmakhlouf.goeuro.util.GooglePlayHandler;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;


public class SearchActivity extends ActionBarActivity implements GooglePlayHandler.LastLocationListener, LocationFragment.SearchListener, CalendarFragment.OnChooseDateListener {
    private GooglePlayHandler mGooglePlayHandler;

    private static final int FRAGMENT_CODE_FROM = 0;
    private static final int FRAGMENT_CODE_TO = 1;

    private boolean shouldFinish = true;
    private boolean shouldTakeAction = true;
    @InjectView(R.id.fromTextView)
    TextView fromTextView;
    @InjectView(R.id.dateTextView)
    TextView dateTextView;
    @InjectView(R.id.toTextView)
    EditText toTextView;
    @InjectView(R.id.formContainer)
    View formContainer;
    @InjectView(R.id.fragmentContainer)
    View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        GoEuroApp.get(getApplicationContext()).inject(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.inject(this);
        fromTextView.setCursorVisible(false);
        toTextView.setCursorVisible(false);
        mGooglePlayHandler = new GooglePlayHandler(this, getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGooglePlayHandler != null)
            mGooglePlayHandler.connect();

    }

    @Override
    protected void onStop() {
        if (mGooglePlayHandler != null)
            mGooglePlayHandler.disconnect();
        mGooglePlayHandler = null;
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (shouldFinish) {
            finish();
        } else {
            shouldTakeAction = true;
            shouldFinish = true;
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            getSupportFragmentManager().beginTransaction().remove(f).commit();
            resetAnimation();
        }
    }

    @Override
    public void onLocationReady(double latitude, double longitude) {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.goeuro.sharedprefs", Context.MODE_PRIVATE);
        prefs.edit().putLong("Longitude", Double.doubleToRawLongBits(longitude)).putLong("Latitude", Double.doubleToRawLongBits(latitude)).apply();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("shouldFinish", shouldFinish);
        outState.putBoolean("shouldTakeAction", shouldTakeAction);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        shouldFinish = savedInstanceState.getBoolean("shouldFinish");
        shouldTakeAction = savedInstanceState.getBoolean("shouldTakeAction");
    }


    @OnClick(R.id.searchButton)
    public void onSearchClicked() {
        Toast.makeText(getApplicationContext(), "Search is not yet implemented", Toast.LENGTH_SHORT).show();
    }

    @OnTextChanged({R.id.fromTextView, R.id.toTextView})
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        LocationFragment fragment = (LocationFragment) getSupportFragmentManager().findFragmentByTag("locationFragment");
        if (fragment != null) {
            fragment.onTextChanged(s.toString());
        }

    }

    private void showKeyboard(EditText v) {
        v.requestFocus();
        v.setCursorVisible(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    @OnTouch(R.id.fromTextView)
    public boolean onFromTextClicked(EditText v, MotionEvent ev) {
        if (shouldTakeAction) {
            shouldFinish = false;
            shouldTakeAction = false;

            showKeyboard(v);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, LocationFragment.newInstance(FRAGMENT_CODE_FROM), "locationFragment")
                    .commit();


            //Start Animations
            int topMargin = (int) dpToPx(16);
            slideUp(formContainer, topMargin);
            stickUpTo(v);
        }
        return false;
    }


    @OnTouch(R.id.toTextView)
    public boolean onToTextClicked(EditText v, MotionEvent ev) {
        if (shouldTakeAction) {
            shouldTakeAction = false;
            shouldFinish = false;

            showKeyboard(v);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, LocationFragment.newInstance(FRAGMENT_CODE_TO), "locationFragment")
                    .commit();

            //Start Animations
            int topMargin = (int) dpToPx(16);
            int slideby = topMargin + v.getTop();
            slideUp(formContainer, slideby);
            stickUpTo(v);

        }
        return false;
    }


    @OnClick(R.id.dateTextView)
    public void onDateClicked(TextView tv) {
        if (shouldTakeAction) {

            shouldTakeAction = false;
            shouldFinish = false;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new CalendarFragment(), "dateFragment")
                    .commit();

            //Start Animations
            slideUp(formContainer, tv.getTop());
            slideUp(tv, tv.getTop());
            stickUpTo(tv);
        }

    }

    private void stickUpTo(View v) {
        fragmentContainer.setY(fragmentContainer.getHeight() / 2);
        fragmentContainer.setVisibility(View.VISIBLE);
        fragmentContainer.setAlpha(0);
        fragmentContainer.animate().
                translationY(v.getHeight() - fragmentContainer.getTop()).alpha(1).
                setDuration(300).
                start();
    }

    private void slideUp(View v, int by) {
        v.animate().
                translationY(-by).
                setDuration(300).
                start();
    }

    private void resetAnimation() {
        formContainer.animate().
                translationY(0).
                setDuration(300).
                start();
        dateTextView.animate().
                translationY(0).
                setDuration(300).
                start();
        fragmentContainer.animate().
                translationY(fragmentContainer.getHeight() / 2).alpha(0).
                setDuration(300).start();
    }

    @Override
    public void onResultSelected(String result, int fragmentMode) {
        shouldFinish = true;
        shouldTakeAction = true;
        if (fragmentMode == FRAGMENT_CODE_FROM)
            fromTextView.setText(result);
        else
            toTextView.setText(result);
        resetAnimation();

    }


    @Override
    public void onDateChanged(String date) {
        dateTextView.setText(date);
    }

    @Override
    public void onActionDone() {
        shouldFinish = true;
        shouldTakeAction = true;
        resetAnimation();
    }

    /**
     * converts dp to pixel
     *
     * @param dp
     * @return
     */
    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


}
