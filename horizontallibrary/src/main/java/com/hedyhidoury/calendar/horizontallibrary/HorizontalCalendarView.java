package com.hedyhidoury.calendar.horizontallibrary;

/**
 * Created by hidou on 7/31/2017.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hedyhidoury.calendar.horizontallibrary.decorator.DayDecorator;
import com.hedyhidoury.calendar.horizontallibrary.decorator.MonthDecorator;
import com.hedyhidoury.calendar.horizontallibrary.decorator.WeekDecorator;
import com.hedyhidoury.calendar.horizontallibrary.decorator.decoratorimpl.DefaultDayDecorator;
import com.hedyhidoury.calendar.horizontallibrary.decorator.decoratorimpl.DefaultMonthDecorator;
import com.hedyhidoury.calendar.horizontallibrary.decorator.decoratorimpl.DefaultWeekDecorator;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.BusProvider;
import com.hedyhidoury.calendar.horizontallibrary.eventbus.Event;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnDateClickListener;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnDatePickedListener;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnDayClickListener;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnMonthChangeListener;
import com.hedyhidoury.calendar.horizontallibrary.listener.OnWeekChangeListener;
import com.hedyhidoury.calendar.horizontallibrary.views.MonthPager;
import com.hedyhidoury.calendar.horizontallibrary.views.WeekPager;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.hedyhidoury.calendar.horizontallibrary.utils.CalendarUtils.AVG_WEEKS_IN_MONTH;
import static com.hedyhidoury.calendar.horizontallibrary.utils.CalendarUtils.DEFAULT_END_OF_HOURS;
import static com.hedyhidoury.calendar.horizontallibrary.utils.CalendarUtils.DEFAULT_MONTHS_NUMBER;


/**
 * Created by nor on 12/6/2015.
 */
public class HorizontalCalendarView extends LinearLayout implements
        OnWeekChangeListener,
        OnMonthChangeListener {
    // statics
    private static final String TAG = "WeekCalendarTAG";
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;
    public static ArrayList<Integer> inValidatedDays = new ArrayList<Integer>();

    // other vars
    private OnDateClickListener listener;
    private OnDatePickedListener datePickedListener;
    private TypedArray typedArray;
    private DayDecorator dayDecorator;
    private MonthDecorator monthDecorator;
    private WeekPager weekPager;
    private MonthPager monthPager;
    private RadioGroup hoursRadioGroup;
    public static DateTime actualDate;
    private Drawable hoursBackgroundDrawable;
    private WeekDecorator weekDecorator;
    private Typeface hoursTypeFace;
    private boolean isHourPicked = false;
    private String hourContent;
    private Date dateDayContent = new Date();
    private OnDayClickListener onDayClickListner;

    public void setOnDayClickListner(OnDayClickListener onDayClickListner) {
        this.onDayClickListner = onDayClickListner;

        weekPager.setOnDayClickListner(onDayClickListner);
    }

    public HorizontalCalendarView(Context context) {
        super(context);
        init(context, null);
    }

    public HorizontalCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public HorizontalCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        actualDate = new DateTime();
        if (attrs != null) {



            typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalCalendarView);

            String titleText = typedArray.getString(R.styleable.HorizontalCalendarView_titleText);
            float titleTextSize = typedArray.getDimension(R.styleable.HorizontalCalendarView_titleTextSize  , -1);
            int titleTextColor = typedArray.getColor(R.styleable.HorizontalCalendarView_titleTextColor  , -1);

            int monthTextColor = typedArray.getColor(R.styleable.HorizontalCalendarView_monthTextColor  , -1);
            float monthTextSize =typedArray.getDimension(R.styleable.HorizontalCalendarView_monthTextSize  , -1);

            // selected date color
            int nbOfMonths = typedArray.getColor(R.styleable
                    .HorizontalCalendarView_nbOfMonths, DEFAULT_MONTHS_NUMBER);

            // Setting up week view
            // selected date color
            int selectedDateColor = typedArray.getColor(R.styleable
                    .HorizontalCalendarView_selectedBgColor, Color.BLUE);
            // today date color
            int todayDateColor = typedArray.getColor(R.styleable
                    .HorizontalCalendarView_todaysDateBgColor, Color.BLUE);
            // days text color
            int daysTextColor = typedArray.getColor(R.styleable
                    .HorizontalCalendarView_daysTextColor, Color.BLACK);
            // calendar background color
            int calendarBgColor = typedArray.getColor(R.styleable
                    .HorizontalCalendarView_calendarBgColor, Color.WHITE);
            // days of week text size
            float daysTextSize = typedArray.getDimension(R.styleable
                    .HorizontalCalendarView_daysTextSize, -1);
            // today date text color
            int todayDateTextColor = typedArray.getColor(R.styleable
                    .HorizontalCalendarView_todaysDateTextColor, Color.WHITE);
            // week font path
            String weekDayFontPath = typedArray.getString(R.styleable.HorizontalCalendarView_weekTypeFace);
            Typeface weekDayTypeFace;
            if (weekDayFontPath != null) {
                weekDayTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/" + weekDayFontPath);
            } else {
                weekDayTypeFace = Typeface.create(Typeface.SANS_SERIF, 0);
            }




            // using decorator for a week view
            setWeekDecorator(new DefaultWeekDecorator());
            // using decorator for single day
            setDayDecorator(new DefaultDayDecorator(getContext(),
                    selectedDateColor,
                    todayDateColor,
                    todayDateTextColor,
                    daysTextColor,
                    daysTextSize
                    , weekDayTypeFace));

            setBackgroundColor(calendarBgColor);

            // Setting up month view
            // get header month and year typeface
            String headerFontPath = typedArray.getString(R.styleable.HorizontalCalendarView_headerTypeFace);
            Typeface headerTypeFace;
            if (headerFontPath != null) {
                headerTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/" + headerFontPath);
            } else {
                headerTypeFace = Typeface.create(Typeface.SANS_SERIF, 0);
            }



            setMonthDecorator(new DefaultMonthDecorator(headerTypeFace , titleText , titleTextSize , titleTextColor , monthTextColor , monthTextSize));

            int marginViews = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());

            // setting up current view
            setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            setOrientation(VERTICAL);

            // adding month view
            LayoutParams monthParamsLayout = new LayoutParams(LayoutParams.MATCH_PARENT,
                    (int) getResources().getDimension(R.dimen.full_month_view_height));
            monthParamsLayout.setMargins(0, marginViews, 0, marginViews);
            monthPager = new MonthPager(getContext(), nbOfMonths, idCheck(), this);
            addView(monthPager, monthParamsLayout);

            // Adding week view
            LayoutParams weekParamsLayout = new LayoutParams(LayoutParams.MATCH_PARENT,
                    (int) getResources().getDimension(R.dimen.full_week_view_height));
            weekPager = new WeekPager(getContext(), (int) (nbOfMonths * AVG_WEEKS_IN_MONTH), idCheck(), this, this);


            addView(weekPager, weekParamsLayout);
        }

        invalidate();
    }


    /**
     * Valide date pickedListener to detect if date picked or not
     *
     * @param datePickedListener
     */
    public void setDatePickedListener(OnDatePickedListener datePickedListener) {
        this.datePickedListener = datePickedListener;
    }

    /**
     * Use this method to make a callback to inform of date picked
     * We need to make sure if hour is already picked or not
     *
     * @param event
     */
    @Subscribe
    public void onDateDayPicked(Event.OnDateClickEvent event) {

        if (datePickedListener != null && isHourPicked) {
            dateDayContent = event.getDateTime().toDate();
            informDatePicked();
        }
    }

    @Subscribe
    public void onDayDecorate(Event.OnDayDecorateEvent event) {
        if (dayDecorator != null) {
            // test if invalidate date or not to pick the right decorator
            if (!event.isInvalidate()) {
                dayDecorator.decorate(event.getView(), event.getDayTextView(), event.getDayNameTextView(), event.getDateTime(),
                        event.getFirstDay(), event.getSelectedDateTime());
            } else {
                dayDecorator.decorateInvalidate(event.getView(), event.getDayTextView(), event.getDayNameTextView(), event.getDateTime(),
                        event.getFirstDay(), event.getSelectedDateTime());
            }

        }
    }

    @Subscribe
    public void onWeekDecorate(Event.OnWeekDecorateEvent event) {
        if (weekDecorator != null) {
            weekDecorator.decorate();
        }
    }


    @Subscribe
    public void onMonthDecorate(Event.OnMonthDecorate event) {
        if (monthDecorator != null) {
            monthDecorator.decorate(event.getDateHeaderTitle() , event.getTitle()  );
        }
    }

    private void setOnDateClickListener(OnDateClickListener listener) {
        this.listener = listener;
    }

    private void setDayDecorator(DayDecorator decorator) {
        this.dayDecorator = decorator;
    }

    private void setWeekDecorator(WeekDecorator decorator) {
        this.weekDecorator = decorator;
    }

    private void setMonthDecorator(MonthDecorator monthDecorator) {
        this.monthDecorator = monthDecorator;
    }

    /**
     * Renders the days again. If you depend on deferred data which need to update the calendar
     * after it's resolved to decorate the days.
     */
    private void updateUi() {
        BusProvider.getInstance().post(new Event.OnUpdateUi());
    }

    private void moveToPrevious() {
        BusProvider.getInstance().post(new Event.UpdateSelectedDateEvent(-1));
    }

    private void moveToNext() {
        BusProvider.getInstance().post(new Event.UpdateSelectedDateEvent(1));
    }

    private void reset() {
        BusProvider.getInstance().post(new Event.ResetEvent());
    }

    private void setSelectedDate(DateTime selectedDate) {
        BusProvider.getInstance().post(new Event.SetSelectedDateEvent(selectedDate));
    }

    private void setStartDate(DateTime startDate) {
        BusProvider.getInstance().post(new Event.SetStartDateEvent(startDate));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusProvider.getInstance().unregister(this);
        BusProvider.disposeInstance();
    }

    /**
     * Checking id exist on views or not
     *
     * @return the first inexsting id
     */
    private int idCheck() {
        int id = 0;
        while (true) {
            if (findViewById(++id) == null) break;
        }
        return id;

    }

    /**
     * Here we are going to inform of date picked from calendar
     * On Each click of hours content, or days ( in case hours already picked )
     */
    private void informDatePicked() {
        Date dateToReturn = null;
        Calendar calendar = null;
        // we will split here by ":" because of hour format took from radioButton
        String[] hourContentSplitted = hourContent.split(":");
        dateToReturn = dateDayContent;
        calendar = Calendar.getInstance();
        calendar.setTime(dateToReturn);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourContentSplitted[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hourContentSplitted[1]));
        calendar.set(Calendar.SECOND, 0); // make seconds to zero by defaults

        datePickedListener.OnDatePicked(calendar.getTime());
    }

    /**
     * Callback for Week time changes
     *
     * @param firstDayOfTheWeek first day of the week
     * @param forward           if swipe is going forward or not
     */
    @Override
    public void onWeekChange(DateTime firstDayOfTheWeek, boolean forward) {

    }

    /**
     * Callback for changing of the week time
     *
     * @param firstDayOfTheWeek first day of the week
     * @param forward           if swipe is going forward or not
     * @param isFromMonthSwipe  if this swipe is from month swipe or not
     */
    @Override
    public void onWeekChange(DateTime firstDayOfTheWeek, boolean forward, boolean isFromMonthSwipe) {

    }

    /**
     * Callback for month date changed
     *
     * @param dateTime  the new date of the month
     * @param onForward if swipe is going forward or not
     */
    @Override
    public void onMonthChange(DateTime dateTime, boolean onForward) {
        if (weekPager != null) {
            // inform week pager of the new date
            weekPager.onMonthDateChanged(dateTime, onForward);
        }
    }

    @Override
    public void onMonthChangeFromWeeks(DateTime dateTime, boolean onForward) {
        if (monthPager != null) {
            monthPager.onSwipeRequested(dateTime, onForward, false);
        }
    }


}