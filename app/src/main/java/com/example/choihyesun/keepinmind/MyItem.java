package com.example.choihyesun.keepinmind;

import android.widget.CheckBox;

/**
 * Created by choihyesun on 15. 11. 9..
 */
public class MyItem {
    private int index;
    private boolean checked;
    private String message;
    private String time;

    public MyItem(){

    }
    public MyItem (boolean checked, String message, String time){
        //this.index = index;
        this.checked = checked;
        this.message = message;
        this.time = time;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
