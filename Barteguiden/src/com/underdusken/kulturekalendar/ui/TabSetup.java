package com.underdusken.kulturekalendar.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.sharedpreference.UserFilterPreference;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 2/12/13
 * Time: 6:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class TabSetup extends Fragment {

    private CheckBox chk_auto_calendar = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_setup, container, false);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Send email
        RelativeLayout btEmail = (RelativeLayout) getActivity().findViewById(R.id.bt_send_feedback);
        btEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Tips for Barteguiden");
                intent.putExtra(Intent.EXTRA_TEXT, "From Android Client.     ");
                intent.setData(Uri.parse("mailto:tips@underdusken.no")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }
        });


        //AutoAdd to calendar
        chk_auto_calendar = (CheckBox) getActivity().findViewById(R.id.chk_auto_calendar);
        if(new UserFilterPreference(getActivity()).isAutoAddToCalendar()){
            chk_auto_calendar.setChecked(true);
        }else{
            chk_auto_calendar.setChecked(false);
        }
        chk_auto_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAutoCalendar();
            }
        });
    }

    private void checkAutoCalendar(){
        if(new UserFilterPreference(getActivity()).isAutoAddToCalendar()){
            chk_auto_calendar.setChecked(false);
            new UserFilterPreference(getActivity()).setAutoAddToCalendar(false);
        }else{
            chk_auto_calendar.setChecked(true);
            new UserFilterPreference(getActivity()).setAutoAddToCalendar(true);
        }
    }

}
