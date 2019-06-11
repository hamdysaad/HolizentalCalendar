package com.hedyhidoury.calendar.horizontallibrary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.hedyhidoury.calendar.horizontallibrary.R;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.BusProvider;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.Event;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.util.Locale;


/**
 * Created by hidou on 8/2/2017.
 */

public class MonthFragment extends Fragment{
    public static String DATE_KEY = "date_key";

    private TextView dateHeaderTitle;

    private boolean isVisible;
    private TextView titleView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.month_view, container, false);
        dateHeaderTitle = (TextView)  rootView.findViewById(R.id.date_title);
        titleView = (TextView)  rootView.findViewById(R.id.titleTV);
        init();
        return rootView;
    }

    private void init() {
        // start with header view and customize it
        BusProvider.getInstance().post(new Event.OnMonthDecorate(dateHeaderTitle , titleView));
        final DateTime midDate = (DateTime) getArguments().getSerializable(DATE_KEY);

        String actualMonthName = midDate.monthOfYear().getAsText(Locale.getDefault());
        int actualYear = midDate.getYear();

        dateHeaderTitle.setText(actualMonthName.substring(0, 1).toUpperCase()+actualMonthName.substring(1)+" "+actualYear);

    }

    @Subscribe
    public void updateUi(Event.OnUpdateUi event) {
    }

    @Subscribe
    public void invalidate(Event.OnMonthDateChanged event) {
        setMonthHeaderDate(event.getDateTime());
    }

    @Override
    public void onStart() {
        BusProvider.getInstance().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        BusProvider.getInstance().unregister(this);
        super.onStop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isVisible = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void setMonthHeaderDate(DateTime monthHeaderDate) {

    }
}
