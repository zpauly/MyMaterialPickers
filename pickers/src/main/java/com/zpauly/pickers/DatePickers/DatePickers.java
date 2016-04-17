package com.zpauly.pickers.DatePickers;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by root on 16-4-16.
 */
public class DatePickers extends LinearLayout {
    private Context mContext;

    public DatePickers(Context context) {
        this(context, null);
    }

    public DatePickers(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePickers(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DatePickers(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }
}
