package com.hedyhidoury.calendar.horizontallibrary.decorator.decoratorimpl;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.hedyhidoury.calendar.horizontallibrary.decorator.MonthDecorator;


/**
 * Created by hidou on 8/1/2017.
 */

public class DefaultMonthDecorator implements MonthDecorator {


    private final String titleText;
    private final float titleTextSize;
    private final int titleTextColor;
    private final int monthTextColor;
    private final float monthTextSize;
    private Typeface headerTypeFace;
    private Drawable previousHeaderIcon;
    private Drawable forwardHeaderIcon;

    public DefaultMonthDecorator(Typeface headerTypeFace, String titleText, float titleTextSize, int titleTextColor, int monthTextColor, float monthTextSize) {
        this.headerTypeFace = headerTypeFace;
        this.titleText = titleText;
        this.titleTextSize = titleTextSize;
        this.titleTextColor = titleTextColor;
        this.monthTextColor = monthTextColor;
        this.monthTextSize = monthTextSize;
    }

    @Override
    public void decorate(TextView monthYearTextView, TextView title) {

        if(headerTypeFace != null){
            monthYearTextView.setTypeface(headerTypeFace);
            monthYearTextView.setTextSize(monthTextSize);
            monthYearTextView.setTextColor(monthTextColor);
        }

        if(title != null){
            title.setText(titleText);
            title.setTextColor(titleTextColor);
            title.setTextSize(titleTextSize);
        }

    }
}
