package com.jhlkdz.temperatremeasure.ui.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.jhlkdz.temperatremeasure.service.CheckInfoService;
import com.jhlkdz.temperatremeasure.service.impl.CheckInfoServiceImpl;
import com.jhlkdz.temperatremeasure.ui.activity.api.LoginInfo;
import com.jhlkdz.temperatremeasure.ui.adapter.ListViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class SyncTask extends Thread {

    private SharedPreferences spLogin;
    private Context context;
    ProgressDialog pd;
    private TimeoutThread tTime;

    private CheckInfoService checkInfoService;

    private ListViewAdapter listViewAdapter;

    public ArrayList<Integer> syncList = new ArrayList<>();

    public SyncTask(Context context, SharedPreferences spLogin, ListViewAdapter listViewAdapter, ProgressDialog p){
        this.spLogin = spLogin;
        this.context = context;
        this.listViewAdapter = listViewAdapter;

        pd=p;
        tTime = new TimeoutThread(context,p,5000,new TimeoutException("连接超时"));

        checkInfoService = new CheckInfoServiceImpl(context,pd);
    }

    @Override
    public void run() {

        LoginInfo loginInfo = new LoginInfo(spLogin.getString("ip",null),
                String.valueOf(spLogin.getInt("address",0)),
                String.valueOf(spLogin.getInt("account",0)),
                String.valueOf(spLogin.getInt("password",0)));
        try {
            syncList = checkInfoService.syncCheckInfo(loginInfo);
        } catch (IOException e) {
            pd.cancel();
            dialog("网络无法连接到主机！");
            tTime.cancel();
            e.printStackTrace();
        }

        for (int i = 0; i < listViewAdapter.getIsSelected().size(); i++) {
            if (listViewAdapter.getIsSelected().get(i)) {
                listViewAdapter.getIsSelected().put(i, false);
            }
        }
        if (syncList.size()==0){
            dialog("没有需要同步的数据");
        }
        else {
            // 遍历list的长度，将已选的按钮设为未选
            System.out.println(syncList+"!!!!!!!!!");
            String text = "";
            for(int i:syncList){
                text = text+i+" ";
            }
            text+="仓数据";
            dialog("已同步"+text);
            for (int i = 0; i < listViewAdapter.getIsSelected().size(); i++) {
                if(syncList.contains(i+1)){
                    listViewAdapter.getIsSelected().put(i, true);
                }
            }
        }
        // 刷新listview和TextView的显示
        //listViewAdapter.notifyDataSetChanged();
        refresh();
    }

    private void dialog(final String text){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(text);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }

    private void setTitle(final ProgressDialog pd, final String text){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                pd.setTitle(text);
            }
        });
    }

    private void refresh(){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                listViewAdapter.notifyDataSetChanged();
            }
        });
    }
}
