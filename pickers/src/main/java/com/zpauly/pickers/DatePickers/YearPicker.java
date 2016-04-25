package com.zpauly.pickers.DatePickers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zpauly.pickers.R;
import com.zpauly.pickers.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 16-4-21.
 */
public class YearPicker extends LinearLayout {
    private Context mContext;

    private ListView mPicker;
    private Adapter mAdapter;

    private List<Integer> mYears = new ArrayList<>();

    private int mViewHeight;
    private int mViewWidth;
    private int mItemHeight;
    private int mPadding;
    private int mSimpleYearSize;
    private int mSelectedYearSize;
    private int mScreenHeight;
    private int mScreenWidth;
    private int mPrimaryColor;

    private int selectedYear;

    private AbsListView.OnScrollListener mOnScrollChangeListener;

    public YearPicker(Context context) {
        this(context, null);
    }

    public YearPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public YearPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;

        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = 7 * mItemHeight;
        mViewWidth = mScreenWidth - 10 * mPadding;
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private void initView() {
        initParams();
        setGravity(Gravity.CENTER_HORIZONTAL);
        setOrientation(LinearLayout.VERTICAL);
        mPicker = new ListView(mContext);
        mAdapter = new Adapter(mContext, mYears);
        mPicker.setDividerHeight(0);
        mPicker.setAdapter(mAdapter);
        mPicker.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                for (int i = 0; i < visibleItemCount; i++) {
                    if (i == 3) {
                        View item = getViewByPositionInListView(firstVisibleItem + i, mPicker);
                        TextView textView = (TextView) item.findViewById(R.id.item_textview);
                        textView.setTextColor(mPrimaryColor);
                        textView.setTextSize(mSelectedYearSize);
                        selectedYear = Integer.parseInt(textView.getText().toString());
                    } else {
                        View item = getViewByPositionInListView(firstVisibleItem + i, mPicker);
                        TextView textView = (TextView) item.findViewById(R.id.item_textview);
                        textView.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 1f));
                        textView.setTextSize(mSimpleYearSize);
                    }
                }
                if (mOnScrollChangeListener != null) {
                    mOnScrollChangeListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }
        });

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(mPicker, -1, layoutParams);
    }

    private void initParams() {
        fetchPrimaryColor();
        getWindowParams();

        mItemHeight = getResources().getDimensionPixelOffset(R.dimen.item_text_height);
        mPadding = getResources().getDimensionPixelOffset(R.dimen.horizontal_padding);
        mSimpleYearSize = getResources().getDimensionPixelOffset(R.dimen.simple_year_text_size);
        mSelectedYearSize = getResources().getDimensionPixelOffset(R.dimen.selected_year_text_size);

        for (int i = 1970; i <= 2037; i ++) {
            mYears.add(i);
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

    private View getViewByPositionInListView(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public void setOnScrollChangeListener(AbsListView.OnScrollListener listener) {
        mOnScrollChangeListener = listener;
    }

    public int getSelectedYear() {
        return this.selectedYear;
    }

    private class Adapter implements ListAdapter {
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private View mItemView;

        private List<Integer> mYears;

        public Adapter(Context context, List<Integer> years) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
            mYears = years;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return mYears.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            mItemView = mLayoutInflater.inflate(R.layout.year_picker_listview_item, parent, false);
            holder.mTextView = (TextView) mItemView.findViewById(R.id.item_textview);
            holder.mTextView.setText(String.valueOf(mYears.get(position)));
            return mItemView;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return this.mYears.isEmpty() ? true : false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        public class ViewHolder {
            TextView mTextView;
        }
    }
}
