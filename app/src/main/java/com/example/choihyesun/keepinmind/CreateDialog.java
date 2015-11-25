package com.example.choihyesun.keepinmind;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by choihyesun on 15. 11. 20..
 */
public class CreateDialog extends Dialog implements View.OnClickListener{

    Context context;
    private EditText editCheck;
    private Button saveBtn;
    private Button cancelBtn;

    public CreateDialog(Context context, View.OnClickListener onClickListener){
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_list);

        editCheck = (EditText) findViewById(R.id.editCheck);
        editCheck.setSelection(editCheck.getText().length());

        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);

        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveBtn:

                dismiss();
                break;
            case R.id.cancelBtn:
                dismiss();
                break;
        }
    }
}
