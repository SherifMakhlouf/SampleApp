package com.sherifmakhlouf.goeuro.view;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sherifmakhlouf.goeuro.GoEuroApp;
import com.sherifmakhlouf.goeuro.R;
import com.sherifmakhlouf.goeuro.api.GoEuroService;
import com.sherifmakhlouf.goeuro.api.model.Location;
import com.sherifmakhlouf.goeuro.rx.DefaultObserver;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class LocationFragment extends Fragment {
    interface SearchListener {
        /**
         * will be called when item is selected
         *
         * @param result       selected country text
         * @param fragmentMode fragment type (From = 0, To = 1)
         */
        void onResultSelected(String result, int fragmentMode);
    }

    private static final int FRAGMENT_CODE_FROM = 0;
    private static final int FRAGMENT_CODE_TO = 1;

    @InjectView(R.id.resultsListView)
    ListView mResultListView;
    @Inject
    GoEuroService goEuroService;
    @Inject
    List<Location> mLocations;


    private ArrayAdapter<Location> adapter;
    private Subscription subscription;
    private SearchListener resultDelegate;
    private final BehaviorSubject<String> subject = BehaviorSubject.create();

    static LocationFragment newInstance(int mode) {
        LocationFragment f = new LocationFragment();

        Bundle args = new Bundle();
        args.putInt("mode", mode);
        f.setArguments(args);

        return f;
    }

    public LocationFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        GoEuroApp.get(getActivity().getApplicationContext()).inject(this);
        ButterKnife.inject(this, rootView);
        adapter = new ArrayAdapter<Location>(getActivity().getApplicationContext(), R.layout.location_item, mLocations);
        mResultListView.setAdapter(adapter);

        setTextChangeListener();

        return rootView;
    }

    private void setTextChangeListener() {
        //Throttle events to half a second to reduce useless requests
        subject.sample(500, TimeUnit.MILLISECONDS).subscribe(new DefaultObserver<String>() {

            @Override
            public void onNext(String text) {
                loadData(text);
            }
        });
    }

    private void loadData(String text) {

        if (!text.isEmpty())
            subscription = goEuroService.getLocations(GoEuroApp.locale, text).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(getLocationObserver());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        resultDelegate = (SearchActivity) activity;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        resultDelegate = null;

    }

    @Override
    public void onPause() {
        super.onPause();
        if (subscription != null)
            subscription.unsubscribe();
    }

    @Override
    public void onStop() {
        adapter = null;
        super.onStop();
    }


    public void onTextChanged(String text) {
        //emit the value
        subject.onNext(text);
    }


    @OnItemClick(R.id.resultsListView)
    public void onItemSelected(TextView v) {
        int mode = getArguments() != null ? getArguments().getInt("mode") : 0;
        resultDelegate.onResultSelected(v.getText().toString(), mode);

    }

    /**
     * Gets a Location observer
     *
     * @return Observer
     */
    private Observer<List<Location>> getLocationObserver() {
        return new DefaultObserver<List<Location>>() {

            @Override
            public void onNext(List<Location> locations) {
                mLocations.clear();
                mLocations.addAll(locations);
                Collections.sort(mLocations);
                adapter.notifyDataSetChanged();
            }
        };
    }


}
