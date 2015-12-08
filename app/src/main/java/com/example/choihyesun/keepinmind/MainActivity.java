package com.example.choihyesun.keepinmind;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    // custom listView adapter var
    private ListView list;
    private int itemPosition;
    private ArrayList<MyItem> checkList = new ArrayList<MyItem>();
    private FloatingActionButton floatingActionButton;
    CustomAdapter adapter;
    MyItem vo;

    LayoutInflater inflater;
    InputMethodManager inputMethodManager;

    // dialog menu
    String[] str = {"수정하기", "삭제하기", "보관하기"};

    // Activity RequestCode
    private static final int EDIT = 0;  // ModifyListActivity
    private static final int ADD = 1;   // CreateListActivity

    // today Date
    String currentDate;
    Calendar today;

    // SQLite, OpenHelper
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    // score
    private TextView scoreTxt;
    static int score = 0;
    static int origin;

    // App finish
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateList.class);
                startActivityForResult(intent, ADD);
            }
        });

        inflater = getLayoutInflater();

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        today = Calendar.getInstance();
        currentDate = today.get(Calendar.YEAR) + "/" + (today.get(Calendar.MONTH) + 1) + "/" + today.get(Calendar.DAY_OF_MONTH);

        adapter = new CustomAdapter(this, R.layout.layout_list_row, checkList);

        helper = new MySQLiteOpenHelper(MainActivity.this, "TodoList.db", null, 3);

        backPressCloseHandler = new BackPressCloseHandler(this);

        scoreTxt = (TextView) findViewById(R.id.scoreTxt);
        //origin = selectScore();
        scoreTxt.setText("목표 달성 점수 : " + score);

        select();

        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(listener);
        list.setOnItemLongClickListener(longListener);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            itemPosition = position;
            vo = new MyItem();
            checkList.get(itemPosition).setChecked(!checkList.get(itemPosition).isChecked());
            vo.setChecked(!checkList.get(itemPosition).isChecked());

            score += 20;
            insertScore(score);
            scoreTxt.setText("목표 달성 점수 : " + score);
            insertStorage(checkList.get(itemPosition).getMessage(), checkList.get(itemPosition).getTime(), checkList.get(itemPosition).isChecked());
            Toast.makeText(getApplicationContext(), "보관함으로 이동되었습니다", Toast.LENGTH_SHORT).show();
        }
    };

    AdapterView.OnItemLongClickListener longListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            itemPosition = position;
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("메뉴")
                    .setItems(str, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (str[which].equals("수정하기")) {
                                Intent intent = new Intent(MainActivity.this, ModifyList.class);
                                intent.putExtra("modifyMsg", adapter.getItem(itemPosition));
                                startActivityForResult(intent, EDIT);
                            } else if (str[which].equals("삭제하기")) {
                                delete(checkList.get(itemPosition).getIndex());
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        if (requestCode == EDIT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra("sendMsg");
                checkList.get(itemPosition).setMessage(str);
                update(checkList.get(itemPosition).getMessage(), checkList.get(itemPosition).getIndex());
                adapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
        if (requestCode == ADD) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra("createMsg");
                vo = new MyItem(false, str, currentDate);
                insert(vo.getMessage(), vo.getTime(), vo.isChecked());
                checkList.add(vo);
                select();
                adapter.notifyDataSetChanged();
            }
        } else if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }

    public void insert(String name, String time, boolean checked) {
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("time", time);
        values.put("checked", checked);
        db.insert("todoList1", null, values);
    }

    public void insertStorage(String name, String time, boolean checked) {
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("time", time);
        values.put("checked", checked);
        db.insert("storageList", null, values);
    }

    public void insertScore(int score) {
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("score", score);
        db.insert("score", null, values);
    }

    public void update(String name, int index) {
        String sql = "update " + "todoList1" + " set name = '" + name + "' where _id = " + index + ";";
        db.execSQL(sql);
    }

    public void delete(int index) {
        db = helper.getWritableDatabase();

        db.delete("todoList1", "_id=?", new String[]{String.valueOf(index)});
    }

    public void select() {
        db = helper.getReadableDatabase();

        Cursor c = db.query("todoList1", null, null, null, null, null, null);
        checkList.clear();
        while (c.moveToNext()) {
            MyItem vo;
            vo = new MyItem();
            vo.setIndex(c.getInt(c.getColumnIndex("_id")));
            vo.setMessage(c.getString(c.getColumnIndex("name")));
            vo.setTime(c.getString(c.getColumnIndex("time")));
            //vo.setChecked(c.getInt(c.getColumnIndex("checked")));
            checkList.add(vo);
        }
    }

    public int selectScore() {
        db = helper.getReadableDatabase();

        Cursor c = db.query("score", null, null, null, null, null, null);
        score = c.getColumnIndex("score");

        return score;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.storage) {
            Intent intent = new Intent(MainActivity.this, StorageActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
