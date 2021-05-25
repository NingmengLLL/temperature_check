package com.jhlkdz.temperatremeasure.ui.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.jhlkdz.temperatremeasure.socket.Client;
import com.jhlkdz.temperatremeasure.socket.api.Response;
import com.jhlkdz.temperatremeasure.ui.activity.api.LoginInfo;
import com.jhlkdz.temperatremeasure.util.ConverseUtil;

import java.io.IOException;
import java.util.List;

public class InitTask extends Thread{

    private int barnIndex;
    private Context context;
    private ProgressDialog pd;
    private SharedPreferences spLogin;

    public InitTask(Context context, SharedPreferences spLogin, int barnIndex, ProgressDialog pd){
        this.barnIndex = barnIndex;
        this.context = context;
        this.pd = pd;
        this.spLogin = spLogin;
    }

    @Override
    public void run(){

        pd.setProgress(20);
        sleep(300);
        LoginInfo loginInfo = new LoginInfo(spLogin.getString("ip",null),
                String.valueOf(spLogin.getInt("address",0)),
                String.valueOf(spLogin.getInt("account",0)),
                String.valueOf(spLogin.getInt("password",0)));

        try {
            Client client = new Client(loginInfo.getIp(),loginInfo.getPort());
            client.executeHands(loginInfo.getAddress(),loginInfo.getAccount(),loginInfo.getPassword());
            Response systemParamResponse = client.executeSystemParam(loginInfo.getAddress(),barnIndex);
            int type = systemParamResponse.getParams().get(2);
            Response response = client.executeInit(loginInfo.getAddress(),barnIndex);
            if (response!=null&&response.getOperationType()!=0x11){
                for(int i=2;i<6;i++){
                    pd.setProgress(20*i);
                    sleep(150);
                }
                pd.cancel();
                List<Byte> params = response.getParams();
                int num = params.get(0)*256+params.get(1);
                String message = barnIndex+"仓共有"+num+"个传感器监测点";
                if (type==12){
                    int len = (response.getParamLength()-2)/2;
                    for(int i=0;i<len;i++){
                        message+="\n"+i+"号采集器RSSI="+ ConverseUtil.getRSSI(params.get(2+2*i))+","+ConverseUtil.getRSSI(params.get(2+2*i+1));
                    }
                }
                dialog(message);
            }
            else {
                pd.cancel();
                dialog("初始化失败");
            }
        }catch (IOException e){
            pd.cancel();
            dialog("网络无法连接到主机！");
            e.printStackTrace();
        }

    }

    private void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void dialog(final String text){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(text);
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
