package com.zpauly.pickers.TimePickers;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

/**
 * Created by root on 16-4-19.
 */
public class TimePickerDialog extends DialogFragment {

    public TimePickerDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return new FrameLayout(getActivity());
    }
}
