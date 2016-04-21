package com.zpauly.materialpickers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zpauly.pickers.TimePickers.TimePickerDialog;
import com.zpauly.pickers.support.OnButtonClickedListener;

public class MainActivity extends AppCompatActivity {
    private Button mButton;

    private Toolbar mToolbar;

    private TextView mHour;

    private TextView mMinute;

    private TextView mAMPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.show_dialog);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mHour = (TextView) findViewById(R.id.hour);

        mMinute = (TextView) findViewById(R.id.minute);

        mAMPM = (TextView) findViewById(R.id.am_or_pm);

        mToolbar.setTitle("TimePickerDialog");
        setSupportActionBar(mToolbar);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimePickerDialog dialog = TimePickerDialog.newInstance();

                dialog.setOnButtonClickedListener(new OnButtonClickedListener() {
                    @Override
                    public void onCancelClicked() {

                    }

                    @Override
                    public void onOKClicked() {
                        mHour.setText(String.valueOf(dialog.getSelectedHour()));
                        mMinute.setText(String.valueOf(dialog.getSelectedMinute()));
                        if (dialog.isAM())
                            mAMPM.setText("AM");
                        else
                            mAMPM.setText("PM");
                    }
                });

                dialog.show(getFragmentManager(), "timepickerdialog");
            }
        });
    }
}
