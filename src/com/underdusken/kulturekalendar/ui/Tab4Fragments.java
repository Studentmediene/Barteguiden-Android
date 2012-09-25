package com.underdusken.kulturekalendar.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.underdusken.kulturekalendar.R;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 9/25/12
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tab4Fragments extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab4, container, false);
    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.tab4_checkbox_1:
                if (checked){
                // Put some meat on the sandwich
                }else{
                // Remove the meat
                break;}
            case R.id.tab4_checkbox_2:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}
            case R.id.tab4_checkbox_3:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}
            case R.id.tab4_checkbox_4:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}
            case R.id.tab4_checkbox_5:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}
            case R.id.tab4_checkbox_6:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}
            case R.id.tab4_checkbox_7:
                if (checked){
                    // Put some meat on the sandwich
                }else{
                    // Remove the meat
                    break;}

        }
    }
}