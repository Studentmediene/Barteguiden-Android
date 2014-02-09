package com.underdusken.kulturekalendar.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.ui.fragments.FilterPrefsFragment;

public class PrefsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prefs_activity);

        getFragmentManager().beginTransaction().
                replace(R.id.fragment, new FilterPrefsFragment()).commit();
    }
}
