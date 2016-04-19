package com.zpauly.materialpickers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zpauly.pickers.TimePickers.TimePicker;

public class MainActivity extends AppCompatActivity {
    private TimePicker mPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPicker = (TimePicker) findViewById(R.id.picker);
    }
}
