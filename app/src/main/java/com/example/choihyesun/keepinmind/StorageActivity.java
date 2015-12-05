package com.example.choihyesun.keepinmind;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by choihyesun on 15. 12. 5..
 */
public class StorageActivity extends AppCompatActivity {

    // custom listView adapter var
    private ListView list;
    private int itemPosition;
    private ArrayList<MyItem> checkList = new ArrayList<MyItem>();
    CustomAdapter adapter;
    MyItem vo;
    LayoutInflater inflater;

    // SQLite, OpenHelper
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    String[] str = {"삭제하기"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_layout);

        inflater = getLayoutInflater();

        adapter = new CustomAdapter(this, R.layout.layout_list_row, checkList);

        helper = new MySQLiteOpenHelper(StorageActivity.this, "CheckList.db", null, 1);

        select();

        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(longListener);
    }

    AdapterView.OnItemLongClickListener longListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            itemPosition = position;
            new AlertDialog.Builder(StorageActivity.this)
                    .setTitle("메뉴")
                    .setItems(str, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (str[which].equals("삭제하기")) {
                                checkList.remove(itemPosition);
                                delete(checkList.get(itemPosition).getIndex());
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .show();
            return false;
        }
    };

    public void delete(int index) {
        db = helper.getWritableDatabase();

        db.delete("CheckList", "_id=?", new String[]{String.valueOf(index)});
        //Log.i("db", name + "정상적으로 삭제 되었습니다");
    }

    public void select() {
        db = helper.getReadableDatabase();
        Cursor c = db.query("CheckList", null, null, null, null, null, null);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.store_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main){
            Intent intent = new Intent(StorageActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(StorageActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
