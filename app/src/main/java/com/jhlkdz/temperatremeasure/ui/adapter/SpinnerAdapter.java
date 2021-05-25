package com.jhlkdz.temperatremeasure.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context mContext;

    /**
     * spinner绑定的数组
     */
    private String[] mStringArray;

    public SpinnerAdapter(Context context, String[] stringArray) {
        super(context, android.R.layout.simple_spinner_item, stringArray);
        mContext = context;
        mStringArray = stringArray;
    }

    //这里是Spinner展开后的文字修改
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(mStringArray[position]);
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
        tv.setText(mStringArray[position]);
        tv.setTextSize(14f);
        //tv.setTextColor(Color.BLUE);
        return convertView;
    }

}
