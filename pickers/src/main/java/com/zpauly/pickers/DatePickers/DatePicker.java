package com.zpauly.pickers.DatePickers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Created by zpauly on 16-4-16.
 */
public class DatePicker extends LinearLayout {
    private Context mContext;

    private int mPrimaryColor;

    private LinearLayout mContainer;

    private DayPicker mDayPicker;
    private YearPicker mYearPicker;

    private boolean isYearShowing = true;

    private OnDateSelectedListener onDateSelectedListener;

    public interface OnDateSelectedListener {
        void onYearSelected();

        void onMonthSelected();

        void onDaySelected();
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mYearPicker.getMeasuredWidth(), mYearPicker.getMeasuredHeight());
    }

    private void initView() {
        initParams();
        setOrientation(VERTICAL);

        mContainer = new LinearLayout(mContext);
        mDayPicker = new DayPicker(mContext);
        mYearPicker = new YearPicker(mContext);

        mContainer.setOrientation(VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mContainer, -1, layoutParams);

        setupYearPicker();
        setupDayPicker();
    }

    private void initParams() {
        fetchPrimaryColor();
    }

    private void setupYearPicker() {
        mYearPicker.setOnScrollChangeListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    mDayPicker.setYear(mYearPicker.getSelectedYear());
                    if (onDateSelectedListener != null) {
                        onDateSelectedListener.onYearSelected();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mContainer.addView(mYearPicker, -1, layoutParams);
    }

    private void setupDayPicker() {
        mDayPicker.setYear(mYearPicker.getSelectedYear());
        mDayPicker.setOnSelectedListener(new DayPicker.OnSelectedListener() {
            @Override
            public void onMonthSelected() {
                if (onDateSelectedListener != null) {
                    onDateSelectedListener.onMonthSelected();
                }
            }

            @Override
            public void onDaySelected() {
                if (onDateSelectedListener != null) {
                    onDateSelectedListener.onDaySelected();
                }
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mContainer.addView(mDayPicker, -1, layoutParams);
    }

    private void fetchPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = mContext.obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.colorPrimary});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        mPrimaryColor = color;
    }

    public void yearDayChangeAnim() {
        if (isYearShowing) {
            isYearShowing = false;
            ViewPropertyAnimator.animate(mContainer).translationY(-mYearPicker.getMeasuredHeight()).start();
        } else {
            isYearShowing = true;
            ViewPropertyAnimator.animate(mContainer).translationY(0).start();
        }
    }

    public boolean isYearShowing() {
        return this.isYearShowing;
    }

    public int getYear() {
        return mDayPicker.getYear();
    }

    public int getMonth() {
        return mDayPicker.getMonth() + 1;
    }

    public int getDay() {
        return mDayPicker.getDay();
    }
}
