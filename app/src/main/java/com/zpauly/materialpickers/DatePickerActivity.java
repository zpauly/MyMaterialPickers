package com.zpauly.materialpickers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.zpauly.pickers.DatePickers.DatePickerDialog;
import com.zpauly.pickers.DatePickers.YearPicker;

/**
 * Created by root on 16-4-23.
 */
public class DatePickerActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    private YearPicker mYearPicker;

    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepicker);

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

        /*mYearPicker = (YearPicker) findViewById(R.id.yearpicker);*/
        mButton = (Button) findViewById(R.id.show_dialog);
        mButton.setText("DatePickerDialog");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.newInstance().show(getFragmentManager(), "DatePickerDialog");
            }
        });
    }
}
