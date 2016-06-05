package com.zpauly.materialpickers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zpauly.pickers.DatePickers.DatePickerDialog;
import com.zpauly.pickers.DatePickers.DayPicker;
import com.zpauly.pickers.DatePickers.YearPicker;
import com.zpauly.pickers.support.OnButtonClickedListener;

/**
 * Created by zpauly on 16-4-23.
 */
public class DatePickerActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    private Button mButton;

    private DatePickerDialog dialog;

    private TextView mYear, mMonth, mDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepicker);

        mYear = (TextView) findViewById(R.id.year);
        mMonth = (TextView) findViewById(R.id.month);
        mDay = (TextView) findViewById(R.id.day);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("DatePickerActivity");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mButton = (Button) findViewById(R.id.show_dialog);
        mButton.setText("DatePickerDialog");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = DatePickerDialog.newInstance();
                dialog.show(getFragmentManager(), "DatePickerDialog");
                dialog.setOnbuttonClickedListener(new OnButtonClickedListener() {
                    @Override
                    public void onCancelClicked() {

                    }

                    @Override
                    public void onOKClicked() {
                        mYear.setText(String.valueOf(dialog.getYear()));
                        mMonth.setText(String.valueOf(dialog.getMonth()));
                        mDay.setText(String.valueOf(dialog.getDay()));
                    }
                });
            }
        });

    }
}
