package com.hedyhidoury.calendar.horizontallibrary.decorator.decoratorimpl;

/**
 * Created by hidou on 7/31/2017.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;


import com.hedyhidoury.calendar.horizontallibrary.R;
import com.hedyhidoury.calendar.horizontallibrary.decorator.DayDecorator;
import com.hedyhidoury.calendar.horizontallibrary.fragment.WeekFragment;

import org.joda.time.DateTime;


/**
 * Created by gokhan on 7/27/16.
 */
public class DefaultDayDecorator implements DayDecorator {

    private final int dayNameTextColor;
    private final int dayNameTextSelectedColor;
    private final int dayValueBgColor;
    private final int selectedDayValueBgColor;
    private final int dayValueTextColor;
    private final int SelecteDayValueTextColor;
    private Context context;
    private final int selectedDateColor;
    private final int todayDateColor;
    private int todayDateTextColor;
    private int textColor;
    private float textSize;
    private Typeface weekTypeFace;

    public DefaultDayDecorator(Context context,
                               @ColorInt int selectedDateColor,
                               @ColorInt int todayDateColor,
                               @ColorInt int todayDateTextColor,
                               @ColorInt int textColor,
                               float textSize,
                               Typeface weekTypeFace,
                               int dayNameTextColor,
                               int dayNameTextSelectedColor,
                               int dayValueBgColor,
                               int selectedDayValueBgColor,
                               int dayValueTextColor,
                               int SelecteDayValueTextColor) {
        this.context = context;
        this.selectedDateColor = selectedDateColor;
        this.todayDateColor = todayDateColor;
        this.todayDateTextColor = todayDateTextColor;
        this.textColor = textColor;
        this.textSize = textSize;
        this.weekTypeFace = weekTypeFace;
        this.dayNameTextColor = dayNameTextColor;
        this.dayNameTextSelectedColor = dayNameTextSelectedColor;

        this.dayValueBgColor = dayValueBgColor;
        this.selectedDayValueBgColor = selectedDayValueBgColor;
        this.dayValueTextColor = dayValueTextColor;

        this.SelecteDayValueTextColor = SelecteDayValueTextColor;

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void decorate(
            View view,
            TextView dayTextView,
            TextView dayNameTextView,
            DateTime dateTime,
            DateTime firstDayOfTheWeek,
            DateTime selectedDateTime) {

        //DateTime dt = new DateTime();

        Drawable holoCircle = ContextCompat.getDrawable(context, R.drawable.holo_circle);
//        Drawable solidCircle = ContextCompat.getDrawable(context, R.drawable.solid_circle);
        LayerDrawable solidCircle = (LayerDrawable)ContextCompat.getDrawable(context, R.drawable.solid_circle);
        Drawable solidWhiteCircle = ContextCompat.getDrawable(context, R.drawable.solid_white_circle);

        holoCircle.setColorFilter(selectedDayValueBgColor, PorterDuff.Mode.SRC_ATOP);
        solidCircle.getDrawable(1).mutate().setColorFilter(selectedDayValueBgColor, PorterDuff.Mode.SRC_ATOP);
        solidWhiteCircle.setColorFilter(dayValueBgColor, PorterDuff.Mode.SRC_ATOP);


//        final LayerDrawable layerDrawable = solidCircle.mutate();
//        layerDrawable.getDrawable(index).mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        // solidCircle.mutate().setAlpha(200);
        //holoCircle.mutate().setAlpha(200);


        if (firstDayOfTheWeek.getMonthOfYear() < dateTime.getMonthOfYear()
                || firstDayOfTheWeek.getYear() < dateTime.getYear())
            dayTextView.setTextColor(Color.GRAY);

        DateTime calendarStartDate = WeekFragment.CalendarStartDate;


        // testing to see if selected date ot not
        if (dateTime.toLocalDate().equals(calendarStartDate.toLocalDate())) {
            dayTextView.setBackground(solidCircle);
            dayTextView.setTextColor(selectedDayValueBgColor);
        } else {
            dayTextView.setTextColor(dayValueTextColor);
            dayTextView.setBackground(solidWhiteCircle);
        }


        if (selectedDateTime != null) {
            if (selectedDateTime.toLocalDate().equals(dateTime.toLocalDate())) {
                if (!selectedDateTime.toLocalDate().equals(calendarStartDate.toLocalDate()))
                    dayTextView.setBackground(solidCircle);
                    dayTextView.setTextColor(this.SelecteDayValueTextColor);
                    dayNameTextView.setTextColor(this.dayNameTextSelectedColor);
            } else {
                dayTextView.setBackground(solidWhiteCircle);
                dayTextView.setTextColor(dayValueTextColor);
                dayNameTextView.setTextColor(dayNameTextColor);

            }
        }


        float size = textSize;
        if (size == -1)
            size = dayTextView.getTextSize();
        dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        dayTextView.setTypeface(weekTypeFace);
        dayNameTextView.setTypeface(weekTypeFace);

    }

    @Override
    public void decorateInvalidate(View view, TextView dayTextView, TextView dayNameTextView, DateTime dateTime, DateTime firstDayOfTheWeek, DateTime selectedDateTime) {

        dayTextView.setTextColor(context.getResources().getColor(R.color.invalidate_day));

        float size = textSize;
        if (size == -1)
            size = dayTextView.getTextSize();
        dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        dayTextView.setTypeface(weekTypeFace);
        dayNameTextView.setTypeface(weekTypeFace);
    }


}