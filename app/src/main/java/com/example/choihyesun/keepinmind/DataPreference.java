package com.example.choihyesun.keepinmind;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by choihyesun on 15. 12. 2..
 */
public class DataPreference extends Activity{
    private final String PREF_NAME = "com.keepinmind.pref";

    public final static String PREF_MAIN_VALUE = "PREF_MAIN_VALUE";

    static Context mContext;

    public DataPreference(Context context){
        mContext = context;
    }

    public void put(String key, String value){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key, String dftValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getString(key, dftValue);
        }catch (Exception e){
            return dftValue;
        }
    }
}
