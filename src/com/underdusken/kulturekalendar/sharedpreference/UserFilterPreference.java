package com.underdusken.kulturekalendar.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created with IntelliJ IDEA.
 * User: pavelarteev
 * Date: 11/5/12
 * Time: 3:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserFilterPreference {
    private SharedPreferences settings = null;
    private SharedPreferences.Editor editor = null;

    private static final String NAME_SHARED_PREFERENCE = "user_filter";


    // check list with true direction
    private static final String CHK_1 = "chk_concerts";
    private static final String CHK_2 = "chk_nightlife";
    private static final String CHK_3 = "chk_theatre";
    private static final String CHK_4 = "chk_dance";
    private static final String CHK_5 = "chk_art_exhibition";
    private static final String CHK_6 = "chk_sports";
    private static final String CHK_7 = "chk_presentations";

    private static final String MY_AGE = "my_age";

    private static final String AGE_LIMIT = "age_limit";
    private static final String PRICE = "price";

    public UserFilterPreference(Context context){
        settings = context.getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        editor = settings.edit();
    }


    // get categories
    public boolean isChk1(){
        return settings.getBoolean(CHK_1, true);
    }
    public boolean isChk2(){
        return settings.getBoolean(CHK_2, true);
    }
    public boolean isChk3(){
        return settings.getBoolean(CHK_3, true);
    }
    public boolean isChk4(){
        return settings.getBoolean(CHK_4, true);
    }
    public boolean isChk5(){
        return settings.getBoolean(CHK_5, true);
    }
    public boolean isChk6(){
        return settings.getBoolean(CHK_6, true);
    }
    public boolean isChk7(){
        return settings.getBoolean(CHK_7, true);
    }
    // set categories
    public void setChk1(boolean state){
        editor.putBoolean(CHK_1, state).commit();
    }
    public void setChk2(boolean state){
        editor.putBoolean(CHK_2, state).commit();
    }
    public void setChk3(boolean state){
        editor.putBoolean(CHK_3, state).commit();
    }
    public void setChk4(boolean state){
        editor.putBoolean(CHK_4, state).commit();
    }
    public void setChk5(boolean state){
        editor.putBoolean(CHK_5, state).commit();
    }
    public void setChk6(boolean state){
        editor.putBoolean(CHK_6, state).commit();
    }
    public void setChk7(boolean state){
        editor.putBoolean(CHK_7, state).commit();
    }

    public int getMyAge(){
        return settings.getInt(MY_AGE, 0);
    }
    public void setMyAge(int myAge){
        editor.putInt(MY_AGE, myAge).commit();
    }

    public int getAgeLimit(){
        return settings.getInt(AGE_LIMIT, 0);
    }
    public void setAgeLimit(int ageLimit){
        editor.putInt(AGE_LIMIT, ageLimit).commit();
    }

    public int getPrice(){
        return settings.getInt(PRICE, 0);
    }
    public void setPrice(int price){
        editor.putInt(PRICE, price).commit();
    }
}
