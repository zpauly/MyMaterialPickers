package com.zpauly.pickers.TimePickers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.zpauly.pickers.R;
import com.zpauly.pickers.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 16-4-16.
 */
public class TimePicker extends FrameLayout {
    private Context mContext;

    private boolean isPhone = true;
    private boolean isDarkTheme = false;

    private int mPrimaryColor;
    private int mPadding;
    private final int mHourAngle = 30;
    private final int mMinuteAngle = 6;
    private float textBackgroundRadius;
    private int mTextSize;
    private float clockRadius;
    private float textRadius;
    private float centerX;
    private float centerY;
    private boolean isHours = true;
    private boolean isMinutes = false;
    private Point presentTime;
    private int currentHour;
    private int currentMinute;
    private int currentAngle;
    private int aimAngle;
    private Point touchTime = new Point(0, 0);
    private List<Point> clockTime = new ArrayList<>();

    private int mScreenWidth;
    private int mScreenHeight;

    private ClockHand mClockHand;
    private Clock mClock;
    private ClockText mClockText;

    private Paint mPaint = new Paint();

    private OnTimeSelectedlistener mOnTimeSelectedListener;

    public interface OnTimeSelectedlistener {
        void onTimeSelected();
    }

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
    }

    private void initView() {
        /*isPhone = DevicesUtils.isTablet(mContext);*/
        initArguments();
        mClock = new Clock(mContext);
        mClockHand = new ClockHand(mContext);
        mClockText = new ClockText(mContext);

        FrameLayout.LayoutParams clockLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        clockLayoutParams.gravity = Gravity.CENTER;
        clockLayoutParams.setMargins(mPadding, mPadding, mPadding, mPadding);
        addView(mClock, -1, clockLayoutParams);

        FrameLayout.LayoutParams handLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        handLayoutParams.gravity = Gravity.CENTER;
        handLayoutParams.setMargins( mPadding, mPadding, mPadding,
                mPadding);
        addView(mClockHand, -1, handLayoutParams);

        FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.gravity = Gravity.CENTER;
        textLayoutParams.setMargins(mPadding, mPadding, mPadding, mPadding);
        addView(mClockText, -1, textLayoutParams);
    }

    @SuppressWarnings("deprecation")
    private void initArguments() {
        mPadding = getResources().getDimensionPixelOffset(R.dimen.horizontal_padding);
        textBackgroundRadius = getResources().getDimensionPixelOffset(R.dimen.text_background_radius);
        mTextSize = getResources().getDimensionPixelSize(R.dimen.clock_text_size);

        getWindowParams();
        fetchPrimaryColor();

        Date date = new Date();
        currentHour = date.getHours();
        currentMinute = date.getMinutes();

        Log.i("hour", String.valueOf(currentHour));
        Log.i("minute", String.valueOf(currentMinute));

        if (isPhone) {
            clockRadius = (mScreenWidth - 14 * mPadding) / 2;
        } else {
            clockRadius = (mScreenHeight - 14 * mPadding) / 2;
        }

        centerX = clockRadius;
        centerY = centerX;

        textRadius = clockRadius - 2 * mPadding;
        final double oneAngle = 30;
        double totalAngle = 0;

        for (int i = 0; i < 12; i++) {
            totalAngle += oneAngle;
            float textX = (float) (textRadius * Math.sin(totalAngle * (Math.PI / 180)));
            float textY = (float) (textRadius * Math.cos(totalAngle * (Math.PI / 180)));
            float x = centerX + textX;
            float y = centerY - textY;
            Point point = new Point((int) x - 15, (int) y + 15);
            clockTime.add(point);
        }
        if (isHours) {
            presentTime = new Point((int) (centerX + textRadius * Math.sin(currentHour * mHourAngle * (Math.PI / 180))),
                    (int) (centerY - textRadius * Math.cos(currentHour * mHourAngle * (Math.PI / 180))));
            currentAngle = currentHour * mHourAngle;
        }
        if (isMinutes) {
            presentTime = new Point((int) (centerX + textRadius * Math.sin(currentMinute * mMinuteAngle * (Math.PI / 180))),
                    (int) (centerY - textRadius * Math.cos(currentMinute * mMinuteAngle * (Math.PI / 180))));
            currentAngle = currentMinute * mMinuteAngle;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getWindowParams();
        if (isPhone) {
            int width = mScreenWidth - 10 * mPadding;
            int height = width;
            setMeasuredDimension(width, height);
        } else {
            int width = mScreenHeight - 10 * mPadding;
            int height = width;
            setMeasuredDimension(width, height);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                touchTime.set((int) event.getX(), (int) event.getY());
                int i = isClockTimePressed();
                if (i != -1) {
                    if (isHours) {
                        aimAngle = mHourAngle * i;
                        clockHandAnim();
                        currentAngle = aimAngle;
                        currentHour = currentAngle / mHourAngle;
                    } else {
                        aimAngle = mHourAngle * i;
                        clockHandAnim();
                        currentAngle = aimAngle;
                        currentMinute = currentAngle / mMinuteAngle;
                    }
                }
                if (mOnTimeSelectedListener != null) {
                    mOnTimeSelectedListener.onTimeSelected();
                }
                mClockText.invalidate();
                break;
            case MotionEvent.ACTION_MOVE :
                break;
            case MotionEvent.ACTION_UP :
                break;
        }
        return super.onTouchEvent(event);
    }

    private int isClockTimePressed() {
        int i = 0;
        for (; i < 12; i++) {
            if (touchTime.x <= (clockTime.get(i).x + 2 * textBackgroundRadius) && touchTime.x >= (clockTime.get(i).x - 2 * textBackgroundRadius)
                    && touchTime.y <= (clockTime.get(i).y + 2 * textBackgroundRadius) && touchTime.y >= (clockTime.get(i).y - 2 * textBackgroundRadius)) {
                return i + 1;
            }
        }
        return -1;
    }

    private void clockHandAnim() {
        if (aimAngle >= currentAngle) {
            ViewPropertyAnimator.animate(mClockHand).rotationBy(aimAngle - currentAngle).setDuration(300);
        } else {
            ViewPropertyAnimator.animate(mClockHand).rotationBy(aimAngle + 360 - currentAngle).setDuration(300);
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

    public void setOnTimeSelectedListener(OnTimeSelectedlistener listener) {
        this.mOnTimeSelectedListener = listener;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.isDarkTheme = darkTheme;
        invalidate();
    }

    public boolean isDarkTheme() {
        return this.isDarkTheme;
    }

    public void setShowHours() {
        this.isHours = true;
        this.isMinutes = false;
        aimAngle = currentHour * mHourAngle;
        clockHandAnim();
        currentAngle = aimAngle;

        mClockText.invalidate();
    }

    public void setShowMinutes() {
        this.isMinutes = true;
        this.isHours = false;
        aimAngle = currentMinute * mMinuteAngle;
        clockHandAnim();
        currentAngle = aimAngle;

        mClockText.invalidate();
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public int getCurrentMinute() {
        return currentMinute;
    }

    public void setCurrentHour(int hour) {
        if (currentHour == hour) {
            currentAngle = mHourAngle * currentHour;
            aimAngle = hour * mHourAngle;
            clockHandAnim();
            currentAngle = aimAngle;
            currentHour = currentAngle / mHourAngle;
        }
    }

    public void setCurrentMinute(int minute) {
        if (currentMinute == minute) {
            currentAngle = mMinuteAngle * currentMinute;
            aimAngle = minute * mMinuteAngle;
            clockHandAnim();
            currentAngle = aimAngle;
            currentMinute = currentAngle / mMinuteAngle;
        }
    }

    private class Clock extends View {

        public Clock(Context context) {
            super(context);
        }

        public Clock(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public Clock(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public Clock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawClock(canvas);
            drawCenterPoint(canvas);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension((int) clockRadius * 2, (int) clockRadius * 2);
        }

        private void drawClock(Canvas canvas) {
            mPaint.setColor(Color.rgb(224, 224, 224));
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            RectF rectF = new RectF(0, 0, 2 * clockRadius, 2 * clockRadius);
            canvas.drawOval(rectF, mPaint);
        }

        private void drawCenterPoint(Canvas canvas) {
            mPaint.setColor(mPrimaryColor);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            RectF rectF = new RectF(centerX - 6, centerY - 6,
                    centerX + 6, centerY + 6);
            canvas.drawOval(rectF, mPaint);
        }
    }

    private class ClockHand extends View {
        public ClockHand(Context context) {
            super(context);
            initView();
        }

        public ClockHand(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView();
        }

        public ClockHand(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public ClockHand(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            initView();
        }

        public void initView() {
            setBackground(null);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension((int) clockRadius * 2, (int) clockRadius * 2);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            drawClockHand(canvas);
            drawTextBackground(canvas);
        }

        private void drawClockHand(Canvas canvas) {
            mPaint.setColor(mPrimaryColor);
            mPaint.setStrokeWidth(4f);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            canvas.drawLine(centerX, centerY,
                    presentTime.x, presentTime.y, mPaint);
        }

        private void drawTextBackground(Canvas canvas) {
            mPaint.setColor(mPrimaryColor);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            RectF rectF = new RectF(presentTime.x - textBackgroundRadius, presentTime.y - textBackgroundRadius,
                    presentTime.x + textBackgroundRadius, presentTime.y + textBackgroundRadius);
            canvas.drawOval(rectF, mPaint);
        }
    }

    private class ClockText extends View {

        public ClockText(Context context) {
            super(context);
        }

        public ClockText(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ClockText(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public ClockText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            initView();
        }

        private void initView() {
            setBackground(null);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (isHours) {
                drawHours(canvas);
            }
            if (isMinutes) {
                drawMinutes(canvas);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension((int) clockRadius * 2, (int) clockRadius * 2);
        }

        private void drawHours(Canvas canvas) {
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setTextSize(mTextSize);
            for (int i = 0; i < 12; i++) {
                if (i == (currentHour - 1)) {
                    if (isDarkTheme) {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 0.54f));
                    } else {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
                    }
                    canvas.drawText(String.valueOf(i + 1), clockTime.get(i).x, clockTime.get(i).y, mPaint);
                } else {
                    if (isDarkTheme) {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
                    } else {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 0.54f));
                    }
                    canvas.drawText(String.valueOf(i + 1), clockTime.get(i).x, clockTime.get(i).y, mPaint);
                }
                /*if (clockTime.get(i).x == presentTime.x && clockTime.get(i).y == presentTime.y) {
                    if (isDarkTheme) {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 0.87f));
                    } else {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 1f));
                    }
                    canvas.drawText(String.valueOf(i + 1), clockTime.get(i).x, clockTime.get(i).y, mPaint);
                } else {
                    if (isDarkTheme) {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 1f));
                    } else {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 0.87f));
                    }
                    canvas.drawText(String.valueOf(i + 1), clockTime.get(i).x, clockTime.get(i).y, mPaint);
                }*/
            }
        }

        private void drawMinutes(Canvas canvas) {
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setTextSize(mTextSize);
            for (int i = 0; i < 12; i++) {
                if ((i + 1) == currentMinute / 5 && currentMinute % 5 == 0) {
                    if (isDarkTheme) {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 0.54f));
                    } else {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
                    }
                    canvas.drawText(String.valueOf((i + 1) * 5), clockTime.get(i).x, clockTime.get(i).y, mPaint);
                } else {
                    if (isDarkTheme) {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
                    } else {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 0.54f));
                    }
                    canvas.drawText(String.valueOf((i + 1) * 5), clockTime.get(i).x, clockTime.get(i).y, mPaint);
                }
            }
        }
    }
}
