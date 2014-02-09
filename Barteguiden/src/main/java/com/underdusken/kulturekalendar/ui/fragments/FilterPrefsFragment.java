package com.underdusken.kulturekalendar.ui.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.underdusken.kulturekalendar.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FilterPrefsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.filter_prefs);
    }
}
