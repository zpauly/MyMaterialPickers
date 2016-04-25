package com.zpauly.pickers.DatePickers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.zpauly.pickers.R;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by root on 16-4-23.
 */
public class DayPicker extends LinearLayout {
    private Context mContext;

    private int thisMonth;
    private int count;
    private boolean hasBefore;
    private boolean hasNext;
    private boolean isPhone = true;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mPrimaryColor;
    private int mPadding;
    private int mViewWidth;
    private int mViewHeight;
    private int mItemHeight;
    private int mSimpleTextSize;

    private FrameLayout mMainLayout;
    private LinearLayout mDatelayout;
    private LinearLayout[] mDaysLayout;
    private LinearLayout[][] mWeekLayout;

    private TextView[] mMonth;
    private TextView[][][] mDays;
    private ImageView mBefore;
    private ImageView mNext;

    private int mYear;

    public DayPicker(Context context) {
        this(context, null);
    }

    public DayPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DayPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
    }

    public DayPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private void initView() {
        initParams();

        mMonth = new TextView[12];
        mMainLayout = new FrameLayout(mContext);
        mDatelayout = new LinearLayout(mContext);
        mDaysLayout = new LinearLayout[12];
        mWeekLayout = new LinearLayout[12][7];

        mBefore = new ImageView(mContext);
        mNext = new ImageView(mContext);

        mDays = new TextView[12][7][7];
        for (int k = 0; k < 12; k ++) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j ++) {
                    mDays[k][i][j] = new TextView(mContext);
                    mDays[k][i][j].setGravity(Gravity.CENTER);
                    mDays[k][i][j].setTextSize(mSimpleTextSize);
                    mDays[k][i][j].setWidth(mViewWidth / 7);
                    mDays[k][i][j].setHeight(mItemHeight);
                }
            }
        }
        for (int i = 0; i < 12; i ++) {
            for (int j = 0; j < 7; j++) {
                mWeekLayout[i][j] = new LinearLayout(mContext);
                mWeekLayout[i][j].setOrientation(LinearLayout.HORIZONTAL);
            }
        }
        for (int i = 0; i < 12; i ++) {
            mDaysLayout[i] = new LinearLayout(mContext);
            mDaysLayout[i].setOrientation(VERTICAL);
        }
        for (int i = 0; i < 12; i++) {
            mMonth[i] = new TextView(mContext);
            mMonth[i].setGravity(Gravity.CENTER);
            mMonth[i].setTextColor(Color.rgb(0, 0, 0));
        }
        mMonth[0].setText("JANUARY");
        mMonth[1].setText("FEBRUARY");
        mMonth[2].setText("MARCH");
        mMonth[3].setText("APRIL");
        mMonth[4].setText("MAY");
        mMonth[5].setText("JUNE");
        mMonth[6].setText("JULY");
        mMonth[7].setText("AUGUST");
        mMonth[8].setText("SEPTEMBER");
        mMonth[9].setText("OCTOBER");
        mMonth[10].setText("NOVEMBER");
        mMonth[11].setText("DECEMBER");


        for (int i = 0; i < 12; i ++) {
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mDaysLayout[i].addView(mMonth[i], -1, layoutParams);
        }

        setupMainLayout();
    }

    private void initParams() {
        fetchPrimaryColor();
        getWindowParams();
        hasBefore = true;
        hasNext = true;

        GregorianCalendar d = new GregorianCalendar();

        thisMonth = d.get(Calendar.MONTH);
        if (thisMonth == 0) {
            hasBefore = false;
        }
        if (thisMonth == 11) {
            hasNext = false;
        }
        count = thisMonth;

        mPadding = getResources().getDimensionPixelOffset(R.dimen.horizontal_padding);
        mItemHeight = getResources().getDimensionPixelOffset(R.dimen.item_height);
        mSimpleTextSize = getResources().getDimensionPixelSize(R.dimen.simple_date_text_size);

        if (isPhone) {
            mViewWidth = mScreenWidth - 10 * mPadding;
            mViewHeight = 8 * mItemHeight;
        } else {
            mViewHeight= 8 * mItemHeight;
            mViewWidth = mViewHeight;
        }
    }

    private void setupDateLayout() {
        setupDaysLayout();
        mDatelayout.setOrientation(HORIZONTAL);
        for (int i = 0; i < 12; i ++){
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mDatelayout.addView(mDaysLayout[i], -1, layoutParams);
        }
        LayoutParams dateLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mMainLayout.addView(mDatelayout, -1, dateLayoutParams);
        ViewPropertyAnimator.animate(mDatelayout).x(-thisMonth * mViewWidth).start();
    }

    private void setupMainLayout() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, -12 * mViewWidth, 0);
        addView(mMainLayout, -1, layoutParams);
        setupDateLayout();

        FrameLayout.LayoutParams beforeLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        beforeLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mBefore.setImageResource(R.drawable.ic_before_24dp);
        mMainLayout.addView(mBefore, -1, beforeLayoutParams);

        FrameLayout.LayoutParams nextLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mNext.setImageResource(R.drawable.ic_next_24dp);
        nextLayoutParams.setMargins(mViewWidth - mItemHeight, 0, 0, 0);
        mMainLayout.addView(mNext, -1, nextLayoutParams);

        mBefore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasBefore) {
                    count--;
                    ViewPropertyAnimator.animate(mDatelayout).x(-count * mViewWidth).start();
                    hasNext = true;
                    if (count == 0) {
                        hasBefore = false;
                    }
                }
            }
        });

        mNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasNext) {
                    count++;
                    ViewPropertyAnimator.animate(mDatelayout).x(-count * mViewWidth).start();
                    hasBefore = true;
                    if (count == 11) {
                        hasNext = false;
                    }
                }
            }
        });
    }

    private void setupDaysLayout() {
        setupDays();
        setupWeek();
    }

    private void setupDays() {
        for (int i = 0; i < 12; i++) {
            mDays[i][0][0].setText("S");
            mDays[i][0][1].setText("M");
            mDays[i][0][2].setText("T");
            mDays[i][0][3].setText("W");
            mDays[i][0][4].setText("T");
            mDays[i][0][5].setText("F");
            mDays[i][0][6].setText("S");
            for (int j = 0; j < 7; j ++) {
                for (int k = 0; k < 7; k++) {
                    mDays[i][j][k].setText(String.valueOf(j * k));
                }
            }
        }
    }

    private void setupWeek() {
        for (int k = 0; k < 12; k++) {
            for (int i = 0; i < 7; i ++) {
                for (int j = 0; j < 7; j ++) {
                    LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    mWeekLayout[k][i].addView(mDays[k][i][j], -1, layoutParams);
                }
            }
        }

        for (int i = 0; i < 12; i ++) {
            for (int j = 0; j < 7; j ++) {
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                mDaysLayout[i].addView(mWeekLayout[i][j], -1, layoutParams);
            }
        }
    }

    private void fetchPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = mContext.obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.colorPrimary});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        mPrimaryColor = color;
    }

    private void getWindowParams() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        mScreenHeight = point.y;
        mScreenWidth = point.x;
    }

    private void setCalendar() {
        GregorianCalendar d = new GregorianCalendar();
        int today = d.get(Calendar.DAY_OF_MONTH);
        int month = d.get(Calendar.MONTH);

        d.set(Calendar.DAY_OF_MONTH, 1);

        int weekday = d.get(Calendar.DAY_OF_WEEK);

        int firstDayOfWeek = d.getFirstDayOfWeek();

        int indent = 0;
        while(weekday != firstDayOfWeek) {
            indent++;
            d.add(Calendar.DAY_OF_MONTH, -1);
            weekday = d.get(Calendar.DAY_OF_WEEK);
        }

        String[] weekdayNames = new DateFormatSymbols().getShortWeekdays();
        do {
            d.add(Calendar.DAY_OF_MONTH, 1);
        } while (weekday != firstDayOfWeek);

        for (int i = 1; i < indent; i ++) {

        }

        d.set(Calendar.DAY_OF_MONTH, 1);
    }
}