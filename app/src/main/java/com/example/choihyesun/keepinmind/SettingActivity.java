package com.example.choihyesun.keepinmind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by choihyesun on 15. 11. 26..
 */
public class SettingActivity extends AppCompatActivity{

    private TextView versionTxt;
    private CheckBox lockMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        setTitle("설정");

        versionTxt = (TextView) findViewById(R.id.versionTxt);
        lockMode = (CheckBox) findViewById(R.id.lockCheck);

        lockMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lockMode.isChecked() == true){
                    Toast.makeText(getApplicationContext(), "잠금화면이 설정되었습니다", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "잠금화면이 해제되었습니다", Toast.LENGTH_SHORT).show();
                }
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
        if(item.getItemId() == R.id.ok){
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "저장되었습니다", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
