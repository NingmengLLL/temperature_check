package com.jhlkdz.temperatremeasure.ui.Task;;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.jhlkdz.temperatremeasure.service.CheckInfoService;
import com.jhlkdz.temperatremeasure.service.impl.CheckInfoServiceImpl;
import com.jhlkdz.temperatremeasure.ui.Task.TimeoutThread;
import com.jhlkdz.temperatremeasure.ui.activity.api.LoginInfo;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class CheckTask extends Thread {

    private SharedPreferences spLogin;
    private Context context;
    private List<Integer> list;

    boolean run = true;
    ProgressDialog pd;

    private CheckInfoService checkInfoService;

    private TimeoutThread tTime;

    public CheckTask(Context context, SharedPreferences spLogin, List<Integer> list, ProgressDialog p){
        this.spLogin = spLogin;
        this.context = context;
        this.list = list;

        pd=p;
        tTime = new TimeoutThread(context,p,5000+list.size()*40000,new TimeoutException("连接超时"));

        checkInfoService = new CheckInfoServiceImpl(context,pd);
    }

    @Override
    public void run() {
        boolean isCompleted = true;

        List<Integer> passList = new ArrayList<>();
        List<Integer> failList = new ArrayList<>();

        tTime.start();

        LoginInfo loginInfo = new LoginInfo(spLogin.getString("ip",null),
                    String.valueOf(spLogin.getInt("address",0)),
                    String.valueOf(spLogin.getInt("account",0)),
                    String.valueOf(spLogin.getInt("password",0)));


        setTitle(pd,"正在检测");
        try {
            passList = checkInfoService.addCheckInfo(list,loginInfo,tTime);
        } catch (IOException e) {
            pd.cancel();
            dialog("网络无法连接到主机！");
            tTime.cancel();
            e.printStackTrace();
        }

        if (passList==null){
            return;
        }
        else {
            for(int i:list){
                if (!passList.contains(i)){
                    failList.add(i);
                }
            }
        }


        tTime.cancel();
        String passStr="";
        String failStr="";
        String res = "";
        for(int i=0;i<passList.size();i++){
            if (i==passList.size()-1)
                passStr=passStr+passList.get(i);
            else
                passStr=passStr+passList.get(i)+",";
        }
        for(int i=0;i<failList.size();i++){
            if (i==failList.size()-1)
                failStr=failStr+failList.get(i);
            else
                failStr=failStr+failList.get(i)+",";
        }
        if (passStr!="")
            res=res+passStr+"仓检测完成！";
        if (failStr!="")
            res=res+failStr+"仓检测失败！";

        if (isCompleted){
            dialog(res);
            pd.cancel();
        }
        //pd.dismiss();

    }

    private void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void message(final String text){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,text,Toast.LENGTH_LONG).show();
            }
        });
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

}
