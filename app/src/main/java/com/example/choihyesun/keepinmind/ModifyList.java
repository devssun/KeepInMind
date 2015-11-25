package com.example.choihyesun.keepinmind;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


public class ModifyList extends AppCompatActivity {

    private EditText modifyList;
    private TextView countTxt;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_list_layout);
        setTitle("체크리스트 수정");

        modifyList = (EditText) findViewById(R.id.inputList);
        countTxt = (TextView) findViewById(R.id.count);
        modifyList.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        Intent intent = getIntent();
        str = intent.getStringExtra("modifyMsg");
        if (!str.equals("")) {
            modifyList.setText(str);
            modifyList.setHint(str);
            modifyList.setSelection(str.length());
        }
        /////

        countTxt.setText(modifyList.getText().toString().length() + "/30");

        modifyList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countTxt.setText(modifyList.getText().toString().length() + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ok) {
            Intent intent = new Intent();

            if (modifyList.length() == 0) {
                intent.putExtra("sendMsg", str);
            } else {
                intent.putExtra("sendMsg", modifyList.getText().toString());
            }
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
