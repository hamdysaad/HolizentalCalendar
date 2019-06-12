package com.hedyhidoury.calendar.horizontalcalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hedyhidoury.calendar.horizontallibrary.HorizontalCalendarView;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnDatePickedListener;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnDayClickListener;

import org.joda.time.DateTime;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HorizontalCalendarView horizontalCalendarView = (HorizontalCalendarView) findViewById(R.id.horizontal);

        horizontalCalendarView.setOnDayClickListner(new OnDayClickListener() {
            @Override
            public void onDateClick(DateTime dateTime) {
                Toast.makeText(MainActivity.this, dateTime.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
