package com.example.choihyesun.keepinmind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by choihyesun on 15. 11. 9..
 */
public class CustomAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MyItem> items;
    private int layout;

    public CustomAdapter(Context context, int layout, ArrayList<MyItem> items){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position).getMessage();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int itemPosition = position;
        if(convertView == null){
            convertView = inflater.inflate(layout, parent, false);
        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
        //checkBox.setChecked(items.get(itemPosition).isChecked());
        checkBox.setChecked(((ListView)parent).isItemChecked(itemPosition));

        TextView listTxt = (TextView) convertView.findViewById(R.id.listTxt);
        listTxt.setText(items.get(itemPosition).getMessage());

        TextView timeTxt = (TextView) convertView.findViewById(R.id.timeTxt);
        timeTxt.setText(items.get(itemPosition).getTime());

        return convertView;
    }
}
