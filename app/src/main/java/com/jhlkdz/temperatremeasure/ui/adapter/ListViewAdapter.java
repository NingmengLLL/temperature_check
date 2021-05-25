package com.jhlkdz.temperatremeasure.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jhlkdz.temperatremeasure.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter{

    // 填充数据的list
    private ArrayList<String> list;
    // 用来控制CheckBox的选中状况
    private HashMap<Integer, Boolean> isSelected;

    private Context context;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;


    // 构造器
    public ListViewAdapter(ArrayList<String> list, Context context) {
        this.context = context;
        this.list = list;
        //inflater = LayoutInflater.from(context);
        isSelected = new HashMap<>();
        // 初始化数据
        initDate();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnLongClickListener(AdapterView.OnItemLongClickListener listener){
        mOnItemLongClickListener = listener;
    }
    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = LayoutInflater.from(context).inflate(R.layout.choose_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
            holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);

            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        // 设置list中TextView的显示
        // 根据isSelected来设置checkbox的选中状况
        holder.tv.setText(list.get(position));
        holder.cb.setChecked(getIsSelected().get(position));

        final View clickedView = convertView;
        // set the on click listener for each of the items
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(null, clickedView, position, position);
                }

            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mOnItemLongClickListener!=null){
                    mOnItemLongClickListener.onItemLongClick(null,clickedView,position,position);
                }
                return false;
            }
        });

        return convertView;
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    public static class ViewHolder {
        public CheckBox cb;
        public TextView tv;
    }

}
