package com.zpauly.materialpickers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * Created by root on 16-4-23.
 */
public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    private Button mTimePickerButton;

    private Button mDatePickerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Main");
        setSupportActionBar(mToolbar);

        mTimePickerButton = (Button) findViewById(R.id.timepicker_activity);
        mDatePickerButton = (Button) findViewById(R.id.datepicker_activity);

        mTimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TimePickerActivity.class);
                startActivity(intent);
            }
        });

        mDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DatePickerActivity.class);
                startActivity(intent);
            }
        });
    }
}
