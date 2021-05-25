package com.jhlkdz.temperatremeasure.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Spinner2Adapter extends ArrayAdapter<String> {

    private Context mContext;

    /**
     * spinner绑定的数组
     */
    private List<String> list;

    public Spinner2Adapter(Context context, List<String> list) {
        super(context, android.R.layout.simple_spinner_item, list);
        mContext = context;
        this.list = list;
    }

    //这里是Spinner展开后的文字修改
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(list.get(position));
        tv.setTextSize(14f);

        return convertView;

    }

    //这里是Spinner收起后的文字修改
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(list.get(position));
        tv.setTextSize(14f);
        //tv.setTextColor(Color.BLUE);
        return convertView;
    }
}
