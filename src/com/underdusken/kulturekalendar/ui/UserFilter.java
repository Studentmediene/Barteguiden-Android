package com.underdusken.kulturekalendar.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.underdusken.kulturekalendar.R;
import com.underdusken.kulturekalendar.sharedpreference.UserFilterPreference;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 11/5/12
 * Time: 2:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserFilter extends Activity {

    // UI
    private CheckBox chkCategory1 = null;
    private CheckBox chkCategory2 = null;
    private CheckBox chkCategory3 = null;
    private CheckBox chkCategory4 = null;
    private CheckBox chkCategory5 = null;
    private CheckBox chkCategory6 = null;
    private CheckBox chkCategory7 = null;

    private EditText etMyAge = null;

    private RadioGroup radMyAge = null;
    private RadioGroup radPrice = null;

    private void initUI() {
        chkCategory1 = (CheckBox) findViewById(R.id.chk_1);
        chkCategory2 = (CheckBox) findViewById(R.id.chk_2);
        chkCategory3 = (CheckBox) findViewById(R.id.chk_3);
        chkCategory4 = (CheckBox) findViewById(R.id.chk_4);
        chkCategory5 = (CheckBox) findViewById(R.id.chk_5);
        chkCategory6 = (CheckBox) findViewById(R.id.chk_6);
        chkCategory7 = (CheckBox) findViewById(R.id.chk_7);

        etMyAge = (EditText)findViewById(R.id.et_my_age);

        radMyAge = (RadioGroup)findViewById(R.id.radioAge);
        radPrice = (RadioGroup)findViewById(R.id.radioPrice);
    }

    private void updateView(){
        UserFilterPreference userFilterPreference = new UserFilterPreference(this);
        chkCategory1.setChecked(userFilterPreference.isChk1());
        chkCategory2.setChecked(userFilterPreference.isChk2());
        chkCategory3.setChecked(userFilterPreference.isChk3());
        chkCategory4.setChecked(userFilterPreference.isChk4());
        chkCategory5.setChecked(userFilterPreference.isChk5());
        chkCategory6.setChecked(userFilterPreference.isChk6());
        chkCategory7.setChecked(userFilterPreference.isChk7());

        int myAge = userFilterPreference.getMyAge();
        if(myAge!=0)
            etMyAge.setText(""+myAge);
        int radioTest = userFilterPreference.getAgeLimit();
        switch (radioTest){
            case 0:
                radMyAge.check(R.id.radioAgeAll);
                break;
            case 1:
                radMyAge.check(R.id.radioAgeMy);
                break;
        }

        radioTest = userFilterPreference.getPrice();
        switch (radioTest){
            case 0:
                radPrice.check(R.id.radioPriceAll);
                break;
            case 1:
                radPrice.check(R.id.radioPricePaid);
                break;
            case 2:
                radPrice.check(R.id.radioPriceFree);
                break;
        }

    }

    private void saveState(){
        UserFilterPreference userFilterPreference = new UserFilterPreference(this);

        userFilterPreference.setChk1(chkCategory1.isChecked());
        userFilterPreference.setChk2(chkCategory2.isChecked());
        userFilterPreference.setChk3(chkCategory3.isChecked());
        userFilterPreference.setChk4(chkCategory4.isChecked());
        userFilterPreference.setChk5(chkCategory5.isChecked());
        userFilterPreference.setChk6(chkCategory6.isChecked());
        userFilterPreference.setChk7(chkCategory7.isChecked());

        String myAge = etMyAge.getText().toString();
        if(myAge.equals(""))
            myAge="0";
        userFilterPreference.setMyAge(Integer.parseInt(myAge));

        int radioTest = radMyAge.getCheckedRadioButtonId();
        switch (radioTest){
            case R.id.radioAgeAll:
                userFilterPreference.setAgeLimit(0);
                break;
            case R.id.radioAgeMy:
                userFilterPreference.setAgeLimit(1);
                break;
        }

        radioTest = radPrice.getCheckedRadioButtonId();
        switch (radioTest){
            case R.id.radioPriceAll:
                userFilterPreference.setPrice(0);
                break;
            case R.id.radioPricePaid:
                userFilterPreference.setPrice(1);
                break;
            case R.id.radioPriceFree:
                userFilterPreference.setPrice(2);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_filter);

        initUI();

        updateView();

    }

    @Override
    protected void onPause() {
        super.onPause();

        saveState();
    }
}
