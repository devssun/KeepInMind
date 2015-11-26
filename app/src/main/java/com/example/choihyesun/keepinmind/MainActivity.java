package com.example.choihyesun.keepinmind;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView list;
    private int itemPosition;
    private ArrayList<MyItem> checkList = new ArrayList<MyItem>();
    private FloatingActionButton floatingActionButton;
    CustomAdapter adapter;
    MyItem vo;

    private TextView plusBtn;
    LayoutInflater inflater;
    View dialogView;

    CreateDialog createDialog;

    String[] str = {"체크리스트 수정", "삭제"};

    private static final int EDIT = 0;
    private static final int ADD = 1;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plusBtn = (TextView) findViewById(R.id.plusBtn);
        plusBtn.setVisibility(View.GONE);

        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateList.class);
                startActivityForResult(intent, ADD);
            }
        });

        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.create_list, null);

        adapter = new CustomAdapter(this, R.layout.layout_list_row, checkList);

        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(longListener);
    }

    AdapterView.OnItemLongClickListener longListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            itemPosition = position;
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("메뉴")
                    .setItems(str, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (str[which].equals("체크리스트 수정")) {
                                Intent intent = new Intent(MainActivity.this, ModifyList.class);
                                intent.putExtra("modifyMsg", adapter.getItem(itemPosition));
                                startActivityForResult(intent, EDIT);
                            } else if (str[which].equals("삭제")) {
                                checkList.remove(itemPosition);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .show();
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        if (requestCode == EDIT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra("sendMsg");
                checkList.get(itemPosition).setMessage(str);
                adapter.notifyDataSetChanged();
                //Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
        if (requestCode == ADD) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra("createMsg");
                vo = new MyItem(str);
                checkList.add(vo);
                //Toast.makeText(MainActivity.this, "입력된 메세지가 없습니다", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plus_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.setting){
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
