package com.underdusken.kulturekalendar.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.underdusken.kulturekalendar.R;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 2/12/13
 * Time: 6:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class TabSetup extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_setup, container, false);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

}
